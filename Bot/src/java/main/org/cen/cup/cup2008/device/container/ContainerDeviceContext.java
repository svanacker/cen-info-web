package org.cen.cup.cup2008.device.container;

@SuppressWarnings("serial")
/* package */final class ContainerDeviceContext extends statemap.FSMContext {
	// ---------------------------------------------------------------
	// Member methods.
	//

	public ContainerDeviceContext(ContainerDevice owner) {
		super();

		_owner = owner;
		setState(ContainerFSM.StateWaiting);
		ContainerFSM.StateWaiting.Entry(this);
	}

	public ContainerDeviceContext(ContainerDevice owner, ContainerDeviceState initState) {
		super();
		_owner = owner;
		setState(initState);
		initState.Entry(this);
	}

	public void Receive(ContainerResult result) {
		_transition = "Receive";
		getState().Receive(this, result);
		_transition = "";
		return;
	}

	public void Send(ContainerRequest request) {
		_transition = "Send";
		getState().Send(this, request);
		_transition = "";
		return;
	}

	public ContainerDeviceState getState() throws statemap.StateUndefinedException {
		if (_state == null) {
			throw (new statemap.StateUndefinedException());
		}

		return ((ContainerDeviceState) _state);
	}

	protected ContainerDevice getOwner() {
		return (_owner);
	}

	public void setOwner(ContainerDevice owner) {
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

	transient private ContainerDevice _owner;

	// ---------------------------------------------------------------
	// Inner classes.
	//

	public static abstract class ContainerDeviceState extends statemap.State {
		// -----------------------------------------------------------
		// Member methods.
		//

		protected ContainerDeviceState(String name, int id) {
			super(name, id);
		}

		protected void Entry(ContainerDeviceContext context) {
		}

		protected void Exit(ContainerDeviceContext context) {
		}

		protected void Receive(ContainerDeviceContext context, ContainerResult result) {
			Default(context);
		}

		protected void Send(ContainerDeviceContext context, ContainerRequest request) {
			Default(context);
		}

		protected void Default(ContainerDeviceContext context) {
			throw (new statemap.TransitionUndefinedException("State: " + context.getState().getName()
					+ ", Transition: " + context.getTransition()));
		}

		// -----------------------------------------------------------
		// Member data.
		//
	}

	/* package */static abstract class ContainerFSM {
		// -----------------------------------------------------------
		// Member methods.
		//

		// -----------------------------------------------------------
		// Member data.
		//

		// -------------------------------------------------------
		// Constants.
		//
		public static final ContainerFSM_Default.ContainerFSM_StateWaiting StateWaiting = new ContainerFSM_Default.ContainerFSM_StateWaiting(
				"ContainerFSM.StateWaiting", 0);
		public static final ContainerFSM_Default.ContainerFSM_StateSending StateSending = new ContainerFSM_Default.ContainerFSM_StateSending(
				"ContainerFSM.StateSending", 1);
		private static final ContainerFSM_Default Default = new ContainerFSM_Default("ContainerFSM.Default", -1);

	}

	protected static class ContainerFSM_Default extends ContainerDeviceState {
		// -----------------------------------------------------------
		// Member methods.
		//

		protected ContainerFSM_Default(String name, int id) {
			super(name, id);
		}

		@Override
		protected void Default(ContainerDeviceContext context) {
			ContainerDevice ctxt = context.getOwner();

			ContainerDeviceState endState = context.getState();

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

		private static final class ContainerFSM_StateWaiting extends ContainerFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private ContainerFSM_StateWaiting(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Send(ContainerDeviceContext context, ContainerRequest request) {
				ContainerDevice ctxt = context.getOwner();

				(context.getState()).Exit(context);
				context.clearState();
				try {
					ctxt.sendData(request);
				} finally {
					context.setState(ContainerFSM.StateSending);
					(context.getState()).Entry(context);
				}
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class ContainerFSM_StateSending extends ContainerFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private ContainerFSM_StateSending(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Receive(ContainerDeviceContext context, ContainerResult result) {
				ContainerDevice ctxt = context.getOwner();

				(context.getState()).Exit(context);
				context.clearState();
				try {
					ctxt.notifyResult(result);
				} finally {
					context.setState(ContainerFSM.StateWaiting);
					(context.getState()).Entry(context);
				}
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
