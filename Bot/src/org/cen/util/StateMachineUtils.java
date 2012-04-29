package org.cen.util;

import org.cen.ApplicationConst;
import org.cen.robot.IRobotConfiguration;

import statemap.FSMContext;

public class StateMachineUtils {
	public static void initialize(final FSMContext fsm, IRobotConfiguration configuration) {
		String propertyDebug = configuration.getProperty(ApplicationConst.PROPERTY_FSMDEBUG);
		boolean debug = (propertyDebug != null) && (propertyDebug.length() > 0);
		fsm.setDebugFlag(debug);
		fsm.setDebugStream(System.out);

		String propertyListener = configuration.getProperty(ApplicationConst.PROPERTY_FSMLISTENER);
		boolean fsmListener = (propertyListener != null) && (propertyListener.length() > 0);
		if (fsmListener) {
			fsm.addStateChangeListener(new FSMListener(fsm));
		}
	}
}
