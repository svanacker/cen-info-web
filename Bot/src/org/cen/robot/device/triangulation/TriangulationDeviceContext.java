package org.cen.robot.device.triangulation;

@SuppressWarnings("unused")
/* package */final class TriangulationDeviceContext extends statemap.FSMContext {
	// ---------------------------------------------------------------
	// Member methods.
	//

	public TriangulationDeviceContext(TriangulationDevice owner) {
		super();

		_owner = owner;
		setState(TriangulationFSM.StateWaiting);
		TriangulationFSM.StateWaiting.Entry(this);
	}

	public TriangulationDeviceContext(TriangulationDevice owner, TriangulationDeviceState initState) {
		super();
		_owner = owner;
		setState(initState);
		initState.Entry(this);
	}

	public void DataReceived(TriangulationData data) {
		_transition = "DataReceived";
		getState().DataReceived(this, data);
		_transition = "";
		return;
	}

	public void Next() {
		_transition = "Next";
		getState().Next(this);
		_transition = "";
		return;
	}

	public void Read() {
		_transition = "Read";
		getState().Read(this);
		_transition = "";
		return;
	}

	public void Read(TriangulationRequest request) {
		_transition = "Read";
		getState().Read(this, request);
		_transition = "";
		return;
	}

	public TriangulationDeviceState getState() throws statemap.StateUndefinedException {
		if (_state == null) {
			throw (new statemap.StateUndefinedException());
		}

		return ((TriangulationDeviceState) _state);
	}

	protected TriangulationDevice getOwner() {
		return (_owner);
	}

	public void setOwner(TriangulationDevice owner) {
		if (owner == null) {
			throw (new NullPointerException("null owner"));
		} else {
			_owner = owner;
		}

		return;
	}

	// ---------------------------------------------------------------
	// Member data.
	//

	transient private TriangulationDevice _owner;

	// ---------------------------------------------------------------
	// Inner classes.
	//

	public static abstract class TriangulationDeviceState extends statemap.State {
		// -----------------------------------------------------------
		// Member methods.
		//

		protected TriangulationDeviceState(String name, int id) {
			super(name, id);
		}

		protected void Entry(TriangulationDeviceContext context) {
		}

		protected void Exit(TriangulationDeviceContext context) {
		}

		protected void DataReceived(TriangulationDeviceContext context, TriangulationData data) {
			Default(context);
		}

		protected void Next(TriangulationDeviceContext context) {
			Default(context);
		}

		protected void Read(TriangulationDeviceContext context) {
			Default(context);
		}

		protected void Read(TriangulationDeviceContext context, TriangulationRequest request) {
			Default(context);
		}

		protected void Default(TriangulationDeviceContext context) {
			throw (new statemap.TransitionUndefinedException("State: " + context.getState().getName()
					+ ", Transition: " + context.getTransition()));
		}

		// -----------------------------------------------------------
		// Member data.
		//
	}

	/* package */static abstract class TriangulationFSM {
		// -----------------------------------------------------------
		// Member methods.
		//

		// -----------------------------------------------------------
		// Member data.
		//

		// -------------------------------------------------------
		// Constants.
		//
		public static final TriangulationFSM_Default.TriangulationFSM_StateWaiting StateWaiting = new TriangulationFSM_Default.TriangulationFSM_StateWaiting(
				"TriangulationFSM.StateWaiting", 0);
		public static final TriangulationFSM_Default.TriangulationFSM_StateRequest StateRequest = new TriangulationFSM_Default.TriangulationFSM_StateRequest(
				"TriangulationFSM.StateRequest", 1);
		public static final TriangulationFSM_Default.TriangulationFSM_StateRead StateRead = new TriangulationFSM_Default.TriangulationFSM_StateRead(
				"TriangulationFSM.StateRead", 2);
		public static final TriangulationFSM_Default.TriangulationFSM_StateDone StateDone = new TriangulationFSM_Default.TriangulationFSM_StateDone(
				"TriangulationFSM.StateDone", 3);
		private static final TriangulationFSM_Default Default = new TriangulationFSM_Default(
				"TriangulationFSM.Default", -1);

	}

	protected static class TriangulationFSM_Default extends TriangulationDeviceState {
		// -----------------------------------------------------------
		// Member methods.
		//

		protected TriangulationFSM_Default(String name, int id) {
			super(name, id);
		}

		@Override
		protected void Default(TriangulationDeviceContext context) {
			TriangulationDevice ctxt = context.getOwner();

			TriangulationDeviceState endState = context.getState();

			context.clearState();
			try {
				ctxt.unhandled();
			} finally {
				context.setState(endState);
			}
			return;
		}

		// -----------------------------------------------------------
		// Inner classse.
		//

		private static final class TriangulationFSM_StateWaiting extends TriangulationFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private TriangulationFSM_StateWaiting(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Read(TriangulationDeviceContext context, TriangulationRequest request) {
				TriangulationDevice ctxt = context.getOwner();

				(context.getState()).Exit(context);
				context.clearState();
				try {
					ctxt.clearHistory();
					ctxt.setRequest(request);
				} finally {
					context.setState(TriangulationFSM.StateRequest);
					(context.getState()).Entry(context);
				}
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class TriangulationFSM_StateRequest extends TriangulationFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private TriangulationFSM_StateRequest(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(TriangulationDeviceContext context) {
				TriangulationDevice ctxt = context.getOwner();

				ctxt.send();
				return;
			}

			@Override
			protected void DataReceived(TriangulationDeviceContext context, TriangulationData data) {
				TriangulationDevice ctxt = context.getOwner();

				(context.getState()).Exit(context);
				context.clearState();
				try {
					ctxt.storeData(data);
				} finally {
					context.setState(TriangulationFSM.StateRead);
					(context.getState()).Entry(context);
				}
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class TriangulationFSM_StateRead extends TriangulationFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private TriangulationFSM_StateRead(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Next(TriangulationDeviceContext context) {
				TriangulationDevice ctxt = context.getOwner();

				if (ctxt.hasEnoughData()) {

					(context.getState()).Exit(context);
					// No actions.
					context.setState(TriangulationFSM.StateDone);
					(context.getState()).Entry(context);
				} else {
					super.Next(context);
				}

				return;
			}

			@Override
			protected void Read(TriangulationDeviceContext context) {
				TriangulationDevice ctxt = context.getOwner();

				if (!ctxt.hasEnoughData()) {

					(context.getState()).Exit(context);
					// No actions.
					context.setState(TriangulationFSM.StateRequest);
					(context.getState()).Entry(context);
				} else {
					super.Read(context);
				}

				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class TriangulationFSM_StateDone extends TriangulationFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private TriangulationFSM_StateDone(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(TriangulationDeviceContext context) {
				TriangulationDevice ctxt = context.getOwner();

				ctxt.updatePosition();
				return;
			}

			@Override
			protected void Next(TriangulationDeviceContext context) {

				(context.getState()).Exit(context);
				context.setState(TriangulationFSM.StateWaiting);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		// -----------------------------------------------------------
		// Member data.
		//
	}
}
