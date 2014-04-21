package org.cen.com;

import javax.swing.event.EventListenerList;

import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.in.RawInData;
import org.cen.com.out.OutData;
import org.cen.com.out.OutDataListener;

public class ComServiceListener implements IComServiceListener {

	private final EventListenerList listeners = new EventListenerList();

	@Override
	public void addDebugListener(ComDebugListener listener) {
		listeners.add(ComDebugListener.class, listener);
	}

	@Override
	public void addInDataListener(InDataListener comDataListener) {
		listeners.add(InDataListener.class, comDataListener);
	}

	@Override
	public void addOutDataListener(OutDataListener comDataListener) {
		listeners.add(OutDataListener.class, comDataListener);
	}

	public void fireDebugListener(String data) {
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == ComDebugListener.class) {
				try {
					((ComDebugListener) l[i + 1]).onRawInData(new RawInData(data));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Notify all observers that requires to be notify off data which arrives in
	 * the serial Port
	 */
	public void fireInDataListener(InData data) {
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == InDataListener.class) {
				try {
					((InDataListener) l[i + 1]).onInData(data);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Notify all observers that requires to be notify when data are sent to the
	 * serial Port
	 */
	public void fireOutDataListener(OutData outData) {
		Object[] l = listeners.getListenerList();
		for (int i = l.length - 2; i >= 0; i -= 2) {
			if (l[i] == OutDataListener.class) {
				try {
					((OutDataListener) l[i + 1]).onOutDataEvent(outData);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

}
