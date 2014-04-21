package org.cen.cup.cup2008.robot.match;

@SuppressWarnings("serial")
/* package */final class Strategy2008Context extends statemap.FSMContext {
	// ---------------------------------------------------------------
	// Member methods.
	//

	public Strategy2008Context(Strategy2008 owner) {
		super();

		_owner = owner;
		setState(GlobalStrategy.StateInitialization);
		GlobalStrategy.StateInitialization.Entry(this);
	}

	public Strategy2008Context(Strategy2008 owner, Strategy2008State initState) {
		super();
		_owner = owner;
		setState(initState);
		initState.Entry(this);
	}

	public void ConfigurationDone() {
		_transition = "ConfigurationDone";
		getState().ConfigurationDone(this);
		_transition = "";
		return;
	}

	public void Failed() {
		_transition = "Failed";
		getState().Failed(this);
		_transition = "";
		return;
	}

	public void MatchStarted() {
		_transition = "MatchStarted";
		getState().MatchStarted(this);
		_transition = "";
		return;
	}

	public void Ok() {
		_transition = "Ok";
		getState().Ok(this);
		_transition = "";
		return;
	}

	public void Start() {
		_transition = "Start";
		getState().Start(this);
		_transition = "";
		return;
	}

	public Strategy2008State getState() throws statemap.StateUndefinedException {
		if (_state == null) {
			throw (new statemap.StateUndefinedException());
		}

		return ((Strategy2008State) _state);
	}

	protected Strategy2008 getOwner() {
		return (_owner);
	}

	public void setOwner(Strategy2008 owner) {
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

	transient private Strategy2008 _owner;

	// ---------------------------------------------------------------
	// Inner classes.
	//

	public static abstract class Strategy2008State extends statemap.State {
		// -----------------------------------------------------------
		// Member methods.
		//

		protected Strategy2008State(String name, int id) {
			super(name, id);
		}

		protected void Entry(Strategy2008Context context) {
		}

		protected void Exit(Strategy2008Context context) {
		}

		protected void ConfigurationDone(Strategy2008Context context) {
			Default(context);
		}

		protected void Failed(Strategy2008Context context) {
			Default(context);
		}

		protected void MatchStarted(Strategy2008Context context) {
			Default(context);
		}

		protected void Ok(Strategy2008Context context) {
			Default(context);
		}

		protected void Start(Strategy2008Context context) {
			Default(context);
		}

		protected void Default(Strategy2008Context context) {
			throw (new statemap.TransitionUndefinedException("State: " + context.getState().getName()
					+ ", Transition: " + context.getTransition()));
		}

		// -----------------------------------------------------------
		// Member data.
		//
	}

	/* package */static abstract class GlobalStrategy {
		// -----------------------------------------------------------
		// Member methods.
		//

		// -----------------------------------------------------------
		// Member data.
		//

		// -------------------------------------------------------
		// Constants.
		//
		public static final GlobalStrategy_Default.GlobalStrategy_StateInitialization StateInitialization = new GlobalStrategy_Default.GlobalStrategy_StateInitialization(
				"GlobalStrategy.StateInitialization", 0);
		public static final GlobalStrategy_Default.GlobalStrategy_StateConfiguration StateConfiguration = new GlobalStrategy_Default.GlobalStrategy_StateConfiguration(
				"GlobalStrategy.StateConfiguration", 1);
		public static final GlobalStrategy_Default.GlobalStrategy_StateWaitMatchStart StateWaitMatchStart = new GlobalStrategy_Default.GlobalStrategy_StateWaitMatchStart(
				"GlobalStrategy.StateWaitMatchStart", 2);
		public static final GlobalStrategy_Default.GlobalStrategy_StateMoveToDispenser StateMoveToDispenser = new GlobalStrategy_Default.GlobalStrategy_StateMoveToDispenser(
				"GlobalStrategy.StateMoveToDispenser", 3);
		public static final GlobalStrategy_Default.GlobalStrategy_StateCollecting StateCollecting = new GlobalStrategy_Default.GlobalStrategy_StateCollecting(
				"GlobalStrategy.StateCollecting", 4);
		public static final GlobalStrategy_Default.GlobalStrategy_StateLaunching StateLaunching = new GlobalStrategy_Default.GlobalStrategy_StateLaunching(
				"GlobalStrategy.StateLaunching", 5);
		private static final GlobalStrategy_Default Default = new GlobalStrategy_Default("GlobalStrategy.Default", -1);

	}

	protected static class GlobalStrategy_Default extends Strategy2008State {
		// -----------------------------------------------------------
		// Member methods.
		//

		private static final long serialVersionUID = 1L;

		protected GlobalStrategy_Default(String name, int id) {
			super(name, id);
		}

		@Override
		protected void Default(Strategy2008Context context) {
			Strategy2008 ctxt = context.getOwner();

			Strategy2008State endState = context.getState();

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

		private static final class GlobalStrategy_StateInitialization extends GlobalStrategy_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private GlobalStrategy_StateInitialization(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Start(Strategy2008Context context) {

				(context.getState()).Exit(context);
				context.setState(GlobalStrategy.StateConfiguration);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class GlobalStrategy_StateConfiguration extends GlobalStrategy_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private GlobalStrategy_StateConfiguration(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(Strategy2008Context context) {
				Strategy2008 ctxt = context.getOwner();

				ctxt.doConfiguration();
				return;
			}

			@Override
			protected void ConfigurationDone(Strategy2008Context context) {
				Strategy2008 ctxt = context.getOwner();

				(context.getState()).Exit(context);
				context.clearState();
				try {
					ctxt.setInitialPosition();
				} finally {
					context.setState(GlobalStrategy.StateWaitMatchStart);
					(context.getState()).Entry(context);
				}
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class GlobalStrategy_StateWaitMatchStart extends GlobalStrategy_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private GlobalStrategy_StateWaitMatchStart(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(Strategy2008Context context) {
				Strategy2008 ctxt = context.getOwner();

				ctxt.doWaitForMatchStart();
				return;
			}

			@Override
			protected void MatchStarted(Strategy2008Context context) {

				(context.getState()).Exit(context);
				context.setState(GlobalStrategy.StateMoveToDispenser);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class GlobalStrategy_StateMoveToDispenser extends GlobalStrategy_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private GlobalStrategy_StateMoveToDispenser(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Failed(Strategy2008Context context) {

				return;
			}

			@Override
			protected void Ok(Strategy2008Context context) {

				(context.getState()).Exit(context);
				context.setState(GlobalStrategy.StateCollecting);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class GlobalStrategy_StateCollecting extends GlobalStrategy_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private GlobalStrategy_StateCollecting(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Failed(Strategy2008Context context) {

				(context.getState()).Exit(context);
				context.setState(GlobalStrategy.StateMoveToDispenser);
				(context.getState()).Entry(context);
				return;
			}

			@Override
			protected void Ok(Strategy2008Context context) {

				(context.getState()).Exit(context);
				context.setState(GlobalStrategy.StateLaunching);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class GlobalStrategy_StateLaunching extends GlobalStrategy_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private GlobalStrategy_StateLaunching(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Failed(Strategy2008Context context) {

				(context.getState()).Exit(context);
				context.setState(GlobalStrategy.StateMoveToDispenser);
				(context.getState()).Entry(context);
				return;
			}

			@Override
			protected void Ok(Strategy2008Context context) {

				(context.getState()).Exit(context);
				context.setState(GlobalStrategy.StateMoveToDispenser);
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
