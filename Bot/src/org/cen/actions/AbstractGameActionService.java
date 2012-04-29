package org.cen.actions;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cen.math.Angle;
import org.cen.navigation.Location;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotUtils;
import org.cen.robot.device.RobotDeviceRequest;

public abstract class AbstractGameActionService implements IGameActionService {
	protected IRobotServiceProvider servicesProvider;

	private static final double ANGLE_ALIGNED = Math.toRadians(0.5);

	private static final double DISTANCE_MINIMUM_FROM_LAST = 10d;

	public AbstractGameActionService(IRobotServiceProvider servicesProvider) {
		super();
		this.servicesProvider = servicesProvider;
	}

	@Override
	public List<RobotDeviceRequest> buildNextRequests(TrajectoryData data, IGameActionMap map) {
		ArrayList<RobotDeviceRequest> requests = new ArrayList<RobotDeviceRequest>();
		ArrayList<TrajectoryActionData> actionsData = new ArrayList<TrajectoryActionData>();

		Collection<IGameActionHandler> handlers = getRobotActionHandlers();
		TrajectoryPathElement pe = new TrajectoryPathElement(data.startPosition);
		while (pe.isExtendable()) {
			int size = data.locations.size();
			if (data.startIndex == size) {
				// Traite le dernier segment
				pe.setEnd(data.endPosition);
				if (data.endAction != null) {
					handleActions(actionsData, data.endAction, pe, handlers);
				}
				data.startIndex++;
				break;
			} else if (data.startIndex > size) {
				return requests;
			}

			Location l = data.locations.get(data.startIndex++);
			Point2D p = l.getPosition();

			Point2D end = pe.getEnd();
			if ((end == null && pe.getStart().distance(p) < DISTANCE_MINIMUM_FROM_LAST) || (end != null && p.distance(end) < DISTANCE_MINIMUM_FROM_LAST)) {
				// Si on est trop proche du point précédent, on ignore cette
				// étape
				continue;
			}

			if (end != null && (!isPointAligned(pe, p) || getNewElementLength(pe, p) < 0)) {
				// Les points ne sont pas alignés, on ne traite pas la
				// destination en cours. La destination sera traitée à la
				// prochaine itération
				data.startIndex--;
				break;
			}
			pe.setEnd(p);

			List<IGameAction> actions = map.getActions(l);
			if (actions != null) {
				actions: for (IGameAction a : actions) {
					if (!handleActions(actionsData, a, pe, handlers)) {
						// Les actions ne peuvent pas être prises en charge pour
						// la trajectoire courante, elles seront traitées à la
						// prochaine itération
						data.startIndex--;
						break actions;
					}
				}
			}
		}

		// Déplacement
		data.orientation = handleMove(requests, pe.getStart(), pe.getEnd(), data.orientation);

		// Actions
		for (TrajectoryActionData d : actionsData) {
			handleAction(requests, d.position, d.action, d.handler);
		}

		return requests;
	}

	private double getNewElementLength(TrajectoryPathElement pe, Point2D p) {
		// renvoie la distance supplémentaire entre la fin du segment et le
		// point spécifié, si le point se trouve sur le segment, la valeur sera
		// négative
		Point2D end = pe.getEnd();
		if (end == null) {
			return 0;
		}
		Point2D start = pe.getStart();
		return start.distance(p) - start.distance(end);
	}

	@Override
	public Collection<IGameActionHandler> getRobotActionHandlers() {
		GameActionAttribute attribute = RobotUtils.getRobotAttribute(GameActionAttribute.class, servicesProvider);
		if (attribute != null) {
			return attribute.getHandlers();
		} else {
			return null;
		}
	}

	protected abstract void handleAction(List<RobotDeviceRequest> requests, Point2D position, IGameAction action, IGameActionHandler handler);

	private boolean handleActions(ArrayList<TrajectoryActionData> actionsData, IGameAction action, TrajectoryPathElement pe, Collection<IGameActionHandler> handlers) {
		if (handlers != null) {
			for (IGameActionHandler h : handlers) {
				Point2D p = action.getActingPosition(h, pe);
				if (p != null) {
					if (!isPointAligned(pe, p)) {
						// Le point ne figure pas sur la trajectoire en cours.
						// On arrête le traitement.
						return false;
					}
					// Nouvelle destination
					pe.setEnd(p);
					// Traite les actions
					actionsData.add(new TrajectoryActionData(p, action, h));
					if (action.mustStop(h)) {
						pe.setExtendable(false);
					}
				}
			}
		}
		return true;
	}

	protected abstract double handleMove(List<RobotDeviceRequest> requests, Point2D start, Point2D end, double orientation);

	private boolean isPointAligned(TrajectoryPathElement e, Point2D p) {
		Point2D end = e.getEnd();
		if (end == null) {
			return true;
		}

		double a = Angle.getPointsAngle(e.getStart(), end, p);
		// le point est considéré comme aligné si'l est sur la même droite
		// (angle de 0° entre le départ et les deux points de contrôle)
		return (Math.abs(a) < ANGLE_ALIGNED);
	}

	@Override
	public void setServicesProvider(IRobotServiceProvider provider) {
		servicesProvider = provider;
	}
}
