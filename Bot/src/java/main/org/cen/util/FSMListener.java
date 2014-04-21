package org.cen.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cen.logging.LoggingUtils;

import statemap.FSMContext;

public class FSMListener implements PropertyChangeListener {

	private final static Logger LOGGER = LoggingUtils.getClassLogger();

	private final FSMContext fsm;

	public FSMListener(FSMContext fsm) {
		super();
		this.fsm = fsm;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		LOGGER.log(
				Level.FINEST,
				"property changed",
				new FSMEvent(fsm.getName(), evt.getPropertyName(), evt
						.getOldValue(), evt.getNewValue()));
	}
}
