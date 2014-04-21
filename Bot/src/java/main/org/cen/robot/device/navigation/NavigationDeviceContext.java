package org.cen.robot.device.navigation;

@SuppressWarnings("serial")
public final class NavigationDeviceContext extends statemap.FSMContext {
	// ---------------------------------------------------------------
	// Member methods.
	//

	public NavigationDeviceContext(NavigationDevice owner) {
		super();

		_owner = owner;
		setState(NavigationFSM.StateStanding);
		NavigationFSM.StateStanding.Entry(this);
	}

	public NavigationDeviceContext(NavigationDevice owner, NavigationDeviceState initState) {
		super();
		_owner = owner;
		setState(initState);
		initState.Entry(this);
	}

	public void CollisionDetected() {
		_transition = "CollisionDetected";
		getState().CollisionDetected(this);
		_transition = "";
		return;
	}

	public void NewPosition(NavigationRequest request) {
		_transition = "NewPosition";
		getState().NewPosition(this, request);
		_transition = "";
		return;
	}

	public void Next() {
		_transition = "Next";
		getState().Next(this);
		_transition = "";
		return;
	}

	public void PositionFailed() {
		_transition = "PositionFailed";
		getState().PositionFailed(this);
		_transition = "";
		return;
	}

	public void PositionReached() {
		_transition = "PositionReached";
		getState().PositionReached(this);
		_transition = "";
		return;
	}

	public void ReadPosition() {
		_transition = "ReadPosition";
		getState().ReadPosition(this);
		_transition = "";
		return;
	}

	public void Restart() {
		_transition = "Restart";
		getState().Restart(this);
		_transition = "";
		return;
	}

	public void Stop() {
		_transition = "Stop";
		getState().Stop(this);
		_transition = "";
		return;
	}

	public NavigationDeviceState getState() throws statemap.StateUndefinedException {
		if (_state == null) {
			throw (new statemap.StateUndefinedException());
		}

		return ((NavigationDeviceState) _state);
	}

	protected NavigationDevice getOwner() {
		return (_owner);
	}

	public void setOwner(NavigationDevice owner) {
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

	transient private NavigationDevice _owner;

	// ---------------------------------------------------------------
	// Inner classes.
	//

	public static abstract class NavigationDeviceState extends statemap.State {
		// -----------------------------------------------------------
		// Member methods.
		//

		protected NavigationDeviceState(String name, int id) {
			super(name, id);
		}

		protected void Entry(NavigationDeviceContext context) {
		}

		protected void Exit(NavigationDeviceContext context) {
		}

		protected void CollisionDetected(NavigationDeviceContext context) {
			Default(context);
		}

		protected void NewPosition(NavigationDeviceContext context, NavigationRequest request) {
			Default(context);
		}

		protected void Next(NavigationDeviceContext context) {
			Default(context);
		}

		protected void PositionFailed(NavigationDeviceContext context) {
			Default(context);
		}

		protected void PositionReached(NavigationDeviceContext context) {
			Default(context);
		}

		protected void ReadPosition(NavigationDeviceContext context) {
			Default(context);
		}

		protected void Restart(NavigationDeviceContext context) {
			Default(context);
		}

		protected void Stop(NavigationDeviceContext context) {
			Default(context);
		}

		protected void Default(NavigationDeviceContext context) {
			throw (new statemap.TransitionUndefinedException("State: " + context.getState().getName()
					+ ", Transition: " + context.getTransition()));
		}

		// -----------------------------------------------------------
		// Member data.
		//
	}

	/* package */static abstract class NavigationFSM {
		// -----------------------------------------------------------
		// Member methods.
		//

		// -----------------------------------------------------------
		// Member data.
		//

		// -------------------------------------------------------
		// Constants.
		//
		public static final NavigationFSM_Default.NavigationFSM_StateStanding StateStanding = new NavigationFSM_Default.NavigationFSM_StateStanding(
				"NavigationFSM.StateStanding", 0);
		public static final NavigationFSM_Default.NavigationFSM_StateMoving StateMoving = new NavigationFSM_Default.NavigationFSM_StateMoving(
				"NavigationFSM.StateMoving", 1);
		public static final NavigationFSM_Default.NavigationFSM_StateCollision StateCollision = new NavigationFSM_Default.NavigationFSM_StateCollision(
				"NavigationFSM.StateCollision", 2);
		public static final NavigationFSM_Default.NavigationFSM_StateInterrupted StateInterrupted = new NavigationFSM_Default.NavigationFSM_StateInterrupted(
				"NavigationFSM.StateInterrupted", 3);
		public static final NavigationFSM_Default.NavigationFSM_StateStopped StateStopped = new NavigationFSM_Default.NavigationFSM_StateStopped(
				"NavigationFSM.StateStopped", 4);
		public static final NavigationFSM_Default.NavigationFSM_StateFailed StateFailed = new NavigationFSM_Default.NavigationFSM_StateFailed(
				"NavigationFSM.StateFailed", 5);
		public static final NavigationFSM_Default.NavigationFSM_StateReached StateReached = new NavigationFSM_Default.NavigationFSM_StateReached(
				"NavigationFSM.StateReached", 6);
		private static final NavigationFSM_Default Default = new NavigationFSM_Default("NavigationFSM.Default", -1);

	}

	protected static class NavigationFSM_Default extends NavigationDeviceState {
		// -----------------------------------------------------------
		// Member methods.
		//

		protected NavigationFSM_Default(String name, int id) {
			super(name, id);
		}

		@Override
		protected void Restart(NavigationDeviceContext context) {
			NavigationDevice ctxt = context.getOwner();

			boolean loopbackFlag = context.getState().getName().equals(NavigationFSM.StateStanding.getName());

			if (loopbackFlag == false) {
				(context.getState()).Exit(context);
			}

			context.clearState();
			try {
				ctxt.notifyRestart();
			} finally {
				context.setState(NavigationFSM.StateStanding);

				if (loopbackFlag == false) {
					(context.getState()).Entry(context);
				}

			}
			return;
		}

		@Override
		protected void Default(NavigationDeviceContext context) {
			NavigationDevice ctxt = context.getOwner();

			NavigationDeviceState endState = context.getState();

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

		private static final class NavigationFSM_StateStanding extends NavigationFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private NavigationFSM_StateStanding(String name, int id) {
				super(name, id);
			}

			@Override
			protected void NewPosition(NavigationDeviceContext context, NavigationRequest request) {
				NavigationDevice ctxt = context.getOwner();

				(context.getState()).Exit(context);
				context.clearState();
				try {
					ctxt.setRequest(request);
					ctxt.sendData();
				} finally {
					context.setState(NavigationFSM.StateMoving);
					(context.getState()).Entry(context);
				}
				return;
			}

			@Override
			protected void ReadPosition(NavigationDeviceContext context) {

				(context.getState()).Exit(context);
				context.setState(NavigationFSM.StateMoving);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class NavigationFSM_StateMoving extends NavigationFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private NavigationFSM_StateMoving(String name, int id) {
				super(name, id);
			}

			@Override
			protected void CollisionDetected(NavigationDeviceContext context) {

				(context.getState()).Exit(context);
				context.setState(NavigationFSM.StateCollision);
				(context.getState()).Entry(context);
				return;
			}

			@Override
			protected void PositionFailed(NavigationDeviceContext context) {

				(context.getState()).Exit(context);
				context.setState(NavigationFSM.StateFailed);
				(context.getState()).Entry(context);
				return;
			}

			@Override
			protected void PositionReached(NavigationDeviceContext context) {

				(context.getState()).Exit(context);
				context.setState(NavigationFSM.StateReached);
				(context.getState()).Entry(context);
				return;
			}

			@Override
			protected void Stop(NavigationDeviceContext context) {

				(context.getState()).Exit(context);
				context.setState(NavigationFSM.StateStopped);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class NavigationFSM_StateCollision extends NavigationFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private NavigationFSM_StateCollision(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(NavigationDeviceContext context) {
				NavigationDevice ctxt = context.getOwner();

				ctxt.setCollision();
				return;
			}

			@Override
			protected void Next(NavigationDeviceContext context) {

				(context.getState()).Exit(context);
				context.setState(NavigationFSM.StateStanding);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class NavigationFSM_StateInterrupted extends NavigationFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private NavigationFSM_StateInterrupted(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(NavigationDeviceContext context) {
				NavigationDevice ctxt = context.getOwner();

				ctxt.setInterrupted();
				return;
			}

			@Override
			protected void Next(NavigationDeviceContext context) {

				(context.getState()).Exit(context);
				context.setState(NavigationFSM.StateStanding);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class NavigationFSM_StateStopped extends NavigationFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private NavigationFSM_StateStopped(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(NavigationDeviceContext context) {
				NavigationDevice ctxt = context.getOwner();

				ctxt.setStopped();
				return;
			}

			@Override
			protected void Next(NavigationDeviceContext context) {

				(context.getState()).Exit(context);
				context.setState(NavigationFSM.StateStanding);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class NavigationFSM_StateFailed extends NavigationFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private NavigationFSM_StateFailed(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(NavigationDeviceContext context) {
				NavigationDevice ctxt = context.getOwner();

				ctxt.setFailed();
				ctxt.notifyResult();
				return;
			}

			@Override
			protected void Next(NavigationDeviceContext context) {

				(context.getState()).Exit(context);
				context.setState(NavigationFSM.StateStanding);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class NavigationFSM_StateReached extends NavigationFSM_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private NavigationFSM_StateReached(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(NavigationDeviceContext context) {
				NavigationDevice ctxt = context.getOwner();

				ctxt.setReached();
				ctxt.notifyResult();
				return;
			}

			@Override
			protected void Next(NavigationDeviceContext context) {

				(context.getState()).Exit(context);
				context.setState(NavigationFSM.StateStanding);
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
