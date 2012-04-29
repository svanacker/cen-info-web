package org.cen.ui.gameboard.elements;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.cen.robot.match.MatchData;

public abstract class ARobotElement extends AMovableElement{

	private static final String PROPERTY_ALPHA = "alphaDegrees";

	private static final String PROPERTY_X = "x";

	private static final String PROPERTY_Y = "y";


	private List<ARobotAttributeElement> robotAttributeElements = new ArrayList<ARobotAttributeElement>();

	/**
	 * constructor
	 * 
	 * @param NAME
	 * @param position
	 * @param orientation
	 */

	public ARobotElement(String NAME, Point2D position, double orientation) {
		super(NAME, position, orientation);
	}

	/**
	 * constructor
	 * 
	 * @param NAME
	 * @param pProperties
	 * @param pData
	 */
	public ARobotElement(String NAME, Properties pProperties, MatchData pData){
		super(NAME, null, 0);
	}

	public <E extends ARobotAttributeElement>ARobotAttributeElement getRobotAttributeElement(Class<E> type){
		Iterator<ARobotAttributeElement> it = robotAttributeElements.iterator();
		while(it.hasNext()){
			ARobotAttributeElement element = it.next();
			if(element.getClass().equals(type)){
				return element;
			}
		}
		return null;
	};

	@Override
	public void paint(Graphics2D g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setPaint(color);
		g2d.setStroke(stroke);
		g2d.fill(bounds);
		for (ARobotAttributeElement element : robotAttributeElements) {
			element.paint(g);
		}
	}

	protected void setFromProperties( Properties properties, String prefix) {
		String value = properties.getProperty(prefix + PROPERTY_X);
		double x = Double.valueOf(value);
		value = properties.getProperty(prefix + PROPERTY_Y);
		double y = Double.valueOf(value);
		position = new Point2D.Double(x, y);
		value = properties.getProperty(prefix + PROPERTY_ALPHA);
		orientation = Math.toRadians(Double.valueOf(value));
		order = 1;
	}

	public void setRobotAttributeElement(ARobotAttributeElement  pRobotAttributeElement){
		robotAttributeElements.add(pRobotAttributeElement);
	}
}
