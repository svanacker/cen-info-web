package org.cen.cup.cup2008.robot;

import javax.annotation.PostConstruct;

import org.cen.logging.LoggingUtils;
import org.cen.robot.AbstractRobot;

/**
 * Implementation of the eurobot cup 2008 edition.
 * 
 * @author svanacker
 */
public class Robot2008 extends AbstractRobot {
	public Robot2008() {
		super();
	}

	public String getName() {
		return "Robot 2008";
	}

	@PostConstruct
	void initialize() {
		LoggingUtils.getClassLogger().config("robot initialized");
	}
}
