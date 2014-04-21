package org.cen.ui.web;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.cen.com.out.OutData;
import org.cen.cup.cup2010.gameboard.elements.Trajectory;
import org.cen.logging.LoggingUtils;
import org.cen.navigation.INavigationMap;
import org.cen.navigation.ITrajectoryService;
import org.cen.navigation.Location;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotPosition;
import org.cen.robot.RobotUtils;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.navigation.MoveRequest;
import org.cen.robot.device.navigation.NavigationDevice;
import org.cen.robot.device.navigation.NavigationRequest;
import org.cen.robot.device.navigation.RotationRequest;
import org.cen.robot.device.navigation.StopRequest;
import org.cen.robot.device.navigation.com.MoveOutData;
import org.cen.robot.match.MatchData;
import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.IGameBoardService;
import org.cen.util.Holder;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Trajectory builder. Builds a trajectory and adds its graphical representation
 * on the gameboard.
 * 
 * @author Emmanuel ZURMELY
 */
public class TrajectoryBuilder implements ResourceLoaderAware {
	private static final String TRAJECTORY_ELEMENT = "trajectory";

	private String configuration;

	private String displayedTrajectory;

	private Properties properties;

	private ResourceLoader resourceLoader;

	private IRobotServiceProvider servicesProvider;

	private String trajectory;

	private int trajectoryId;

	private String trajectoryOutData;

	private static final Logger LOGGER = LoggingUtils.getClassLogger();

	private void buildOutData(List<OutData> list, RobotDeviceRequest request) {
		if (request instanceof NavigationRequest) {
			List<OutData> outData = getOutData(new StopRequest());
			outData = getOutData((NavigationRequest) request);
			list.addAll(outData);
		}
	}

	private void buildTrajectoryOutData() {
		List<RobotDeviceRequest> requests = new ArrayList<RobotDeviceRequest>();
		Holder<Double> o = new Holder<Double>(new Double(0));
		List<RobotDeviceRequest> phaseRequests = getRequests(displayedTrajectory, o);
		requests.addAll(phaseRequests);
		List<OutData> s = new ArrayList<OutData>();
		StringBuilder b = new StringBuilder();
		for (RobotDeviceRequest request : phaseRequests) {
			// build out data
			buildOutData(s, request);
			// build out data messages list
			for (OutData outData : s) {
				b.append(outData.getMessage());
				if (outData instanceof MoveOutData) {
					if (request instanceof MoveRequest) {
						MoveRequest mr = (MoveRequest) request;
						b.append(" : ");
						b.append(mr.getDistance());
						b.append(" mm");
					}
					if (request instanceof RotationRequest) {
						RotationRequest rr = (RotationRequest) request;
						b.append(" : ");
						b.append(Math.toDegrees(rr.getAngle()));
						b.append("°");
					}
				}
				b.append("<br />");
			}
			s.clear();
		}
		trajectoryOutData = b.toString();
	}

	/**
	 * Returns the configuration string.
	 * 
	 * @return the configuration string
	 */
	public String getConfiguration() {
		return configuration;
	}

	/**
	 * Returns the trajectory displayed on the game board.
	 * 
	 * @return the trajectory displayed on the game board
	 */
	public String getDisplayedTrajectory() {
		return displayedTrajectory;
	}

	private List<OutData> getOutData(NavigationRequest request) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		NavigationDevice device = (NavigationDevice) handler.getDevice(NavigationDevice.NAME);
		return device.getOutData(request);
	}

	private Properties getProperties() {
		if (properties == null) {
			Resource resource = resourceLoader.getResource(configuration);
			try {
				InputStream is = resource.getInputStream();
				properties = new Properties();
				properties.load(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

	private List<RobotDeviceRequest> getRequests(String trajectory, Holder<Double> initialOrientation) {
		String prefix = "";
		List<RobotDeviceRequest> requests = new ArrayList<RobotDeviceRequest>();
		List<Point2D> path = new ArrayList<Point2D>();

		// initial orientation of the robot
		double orientation = initialOrientation.getValue();
		if (Double.isNaN(orientation)) {
			orientation = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider).getAlpha();
		}

		ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
		INavigationMap map = trajectoryService.getNavigationMap();
		Map<String, Location> locations = new HashMap<String, Location>();
		for (Location l : map.getLocations()) {
			locations.put(l.getName(), l);
		}

		// trajectory data
		if (trajectory != null) {
			boolean backward = false;
			String[] positionIds = trajectory.split(",");
			for (String id : positionIds) {
				// Commands
				if (id.startsWith("^")) {
					if (id.equals("^b")) {
						backward ^= true;
					}
				} else {
					Location l = locations.get(prefix + id);
					if (l == null) {
						LOGGER.severe("Location with " + (prefix + id) + " does not exist !");
					} else {
						path.add(l.getPosition());
						if (path.size() > 1) {
							orientation = trajectoryService.buildTrajectoryRequests(path, orientation, requests, backward);
							path.remove(0);
						}
					}
				}
			}
		}
		initialOrientation.setValue(orientation);
		return requests;
	}

	/**
	 * Returns the trajectory string.
	 * 
	 * @return the trajectory string
	 */
	public String getTrajectory() {
		MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
		String s = data.getProperty("trajectory");
		if (s != null && !s.equals(trajectory)) {
			trajectory = s;
			updateView();
		}
		return trajectory;
	}

	private List<Point2D> getTrajectory(int trajectoryId, int index) {
		List<Point2D> results = new ArrayList<Point2D>();
		ITrajectoryService trajectoryService = servicesProvider.getService(ITrajectoryService.class);
		INavigationMap map = trajectoryService.getNavigationMap();
		Map<String, Location> locations = new HashMap<String, Location>();
		for (Location l : map.getLocations()) {
			locations.put(l.getName(), l);
		}

		if (trajectory != null && !trajectory.isEmpty()) {
			displayedTrajectory = trajectory;
		} else {
			Properties p = getProperties();
			displayedTrajectory = p.getProperty("trajectory" + trajectoryId + "." + index);
		}

		if (displayedTrajectory != null) {
			String[] positionIds = StringUtils.split(displayedTrajectory, ",");
			displayedTrajectory = "";
			for (String id : positionIds) {
				if (id.startsWith("^")) {
					displayedTrajectory += id + ",";
					continue;
				}
				// Si une position n'existe pas, on lève une exception, pour
				// être sûr de ne pas passer à côté
				if (!locations.containsKey(id)) {
					displayedTrajectory += "(error: " + id + "),";
				} else {
					displayedTrajectory += id + ",";
					Location l = locations.get(id);
					if (l != null) {
						results.add(l.getPosition());
					}
				}
			}
			displayedTrajectory = displayedTrajectory.substring(0, displayedTrajectory.length() - 1);
		}
		return results;
	}

	/**
	 * Returns the trajectory identifier.
	 * 
	 * @return the trajectory identifier
	 */
	public int getTrajectoryId() {
		return trajectoryId;
	}

	public String getTrajectoryOutData() {
		return trajectoryOutData;
	}

	/**
	 * Sets the configuration string
	 * 
	 * @param configuration
	 *            the configuration string
	 */
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
		updateView();
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		updateView();
	}

	@Required
	public void setServicesProvider(IRobotServiceProvider provider) {
		this.servicesProvider = provider;
		updateView();
	}

	/**
	 * Sets the trajectory string.
	 * 
	 * @param trajectory
	 *            the trajectory string
	 */
	public void setTrajectory(String trajectory) {
		this.trajectory = trajectory;
		updateView();
	}

	/**
	 * Sets the trajectory identifier.
	 * 
	 * @param trajectoryId
	 *            the trajectory identifier
	 */
	public void setTrajectoryId(int trajectoryId) {
		this.trajectoryId = trajectoryId;
	}

	/**
	 * Updates the view.
	 */
	public void updateView() {
		if (servicesProvider == null || configuration == null || resourceLoader == null) {
			return;
		}
		// clear existing configurations
		IGameBoardService gameBoard = servicesProvider.getService(IGameBoardService.class);
		List<IGameBoardElement> elements = gameBoard.getElements();
		Iterator<IGameBoardElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			IGameBoardElement element = iterator.next();
			String name = element.getName();
			if (name.startsWith(TRAJECTORY_ELEMENT)) {
				iterator.remove();
			}
		}
		// obtain trajectory
		int index = 0;
		List<Point2D> positions = getTrajectory(trajectoryId, index);
		elements.add(new Trajectory(TRAJECTORY_ELEMENT + index, positions, index));

		buildTrajectoryOutData();
	}
}
