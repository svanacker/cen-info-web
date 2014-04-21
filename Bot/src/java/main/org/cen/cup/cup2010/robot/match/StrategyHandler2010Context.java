package org.cen.cup.cup2010.robot.match;

@SuppressWarnings("serial")
/* package */final class StrategyHandler2010Context extends statemap.FSMContext {
	// ---------------------------------------------------------------
	// Member methods.
	//

	public StrategyHandler2010Context(StrategyHandler2010 owner) {
		super();

		_owner = owner;
		setState(GlobalStrategy.StateInitialization);
		GlobalStrategy.StateInitialization.Entry(this);
	}

	public StrategyHandler2010Context(StrategyHandler2010 owner, StrategyHandler2010State initState) {
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

	public void MatchStarted() {
		_transition = "MatchStarted";
		getState().MatchStarted(this);
		_transition = "";
		return;
	}

	public void MatchStopped() {
		_transition = "MatchStopped";
		getState().MatchStopped(this);
		_transition = "";
		return;
	}

	public void RobotInitializationDone() {
		_transition = "RobotInitializationDone";
		getState().RobotInitializationDone(this);
		_transition = "";
		return;
	}

	public void Start() {
		_transition = "Start";
		getState().Start(this);
		_transition = "";
		return;
	}

	public StrategyHandler2010State getState() throws statemap.StateUndefinedException {
		if (_state == null) {
			throw (new statemap.StateUndefinedException());
		}

		return ((StrategyHandler2010State) _state);
	}

	protected StrategyHandler2010 getOwner() {
		return (_owner);
	}

	public void setOwner(StrategyHandler2010 owner) {
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

	transient private StrategyHandler2010 _owner;

	// ---------------------------------------------------------------
	// Inner classes.
	//

	public static abstract class StrategyHandler2010State extends statemap.State {
		// -----------------------------------------------------------
		// Member methods.
		//

		protected StrategyHandler2010State(String name, int id) {
			super(name, id);
		}

		protected void Entry(StrategyHandler2010Context context) {
		}

		protected void Exit(StrategyHandler2010Context context) {
		}

		protected void ConfigurationDone(StrategyHandler2010Context context) {
			Default(context);
		}

		protected void MatchStarted(StrategyHandler2010Context context) {
			Default(context);
		}

		protected void MatchStopped(StrategyHandler2010Context context) {
			Default(context);
		}

		protected void RobotInitializationDone(StrategyHandler2010Context context) {
			Default(context);
		}

		protected void Start(StrategyHandler2010Context context) {
			Default(context);
		}

		protected void Default(StrategyHandler2010Context context) {
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
		public static final GlobalStrategy_Default.GlobalStrategy_StateRobotInitialization StateRobotInitialization = new GlobalStrategy_Default.GlobalStrategy_StateRobotInitialization(
				"GlobalStrategy.StateRobotInitialization", 1);
		public static final GlobalStrategy_Default.GlobalStrategy_StateConfiguration StateConfiguration = new GlobalStrategy_Default.GlobalStrategy_StateConfiguration(
				"GlobalStrategy.StateConfiguration", 2);
		public static final GlobalStrategy_Default.GlobalStrategy_StateWaitMatchStart StateWaitMatchStart = new GlobalStrategy_Default.GlobalStrategy_StateWaitMatchStart(
				"GlobalStrategy.StateWaitMatchStart", 3);
		public static final GlobalStrategy_Default.GlobalStrategy_StateRunTrajectory StateRunTrajectory = new GlobalStrategy_Default.GlobalStrategy_StateRunTrajectory(
				"GlobalStrategy.StateRunTrajectory", 4);
		public static final GlobalStrategy_Default.GlobalStrategy_StateStopTrajectory StateStopTrajectory = new GlobalStrategy_Default.GlobalStrategy_StateStopTrajectory(
				"GlobalStrategy.StateStopTrajectory", 5);
		private static final GlobalStrategy_Default Default = new GlobalStrategy_Default("GlobalStrategy.Default", -1);

	}

	protected static class GlobalStrategy_Default extends StrategyHandler2010State {
		// -----------------------------------------------------------
		// Member methods.
		//

		protected GlobalStrategy_Default(String name, int id) {
			super(name, id);
		}

		@Override
		protected void Default(StrategyHandler2010Context context) {
			StrategyHandler2010 ctxt = context.getOwner();

			StrategyHandler2010State endState = context.getState();

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
			protected void Start(StrategyHandler2010Context context) {

				(context.getState()).Exit(context);
				context.setState(GlobalStrategy.StateRobotInitialization);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class GlobalStrategy_StateRobotInitialization extends GlobalStrategy_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private GlobalStrategy_StateRobotInitialization(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(StrategyHandler2010Context context) {
				StrategyHandler2010 ctxt = context.getOwner();

				ctxt.doWaitForRobotInitialization();
				return;
			}

			@Override
			protected void RobotInitializationDone(StrategyHandler2010Context context) {

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
			protected void Entry(StrategyHandler2010Context context) {
				StrategyHandler2010 ctxt = context.getOwner();

				ctxt.doConfiguration();
				return;
			}

			@Override
			protected void ConfigurationDone(StrategyHandler2010Context context) {
				StrategyHandler2010 ctxt = context.getOwner();

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
			protected void Entry(StrategyHandler2010Context context) {
				StrategyHandler2010 ctxt = context.getOwner();

				ctxt.doWaitForMatchStart();
				return;
			}

			@Override
			protected void MatchStarted(StrategyHandler2010Context context) {

				(context.getState()).Exit(context);
				context.setState(GlobalStrategy.StateRunTrajectory);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class GlobalStrategy_StateRunTrajectory extends GlobalStrategy_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private GlobalStrategy_StateRunTrajectory(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(StrategyHandler2010Context context) {
				StrategyHandler2010 ctxt = context.getOwner();

				ctxt.doStartTrajectory();
				return;
			}

			@Override
			protected void MatchStopped(StrategyHandler2010Context context) {

				(context.getState()).Exit(context);
				context.setState(GlobalStrategy.StateStopTrajectory);
				(context.getState()).Entry(context);
				return;
			}

			// -------------------------------------------------------
			// Member data.
			//
		}

		private static final class GlobalStrategy_StateStopTrajectory extends GlobalStrategy_Default {
			// -------------------------------------------------------
			// Member methods.
			//

			private GlobalStrategy_StateStopTrajectory(String name, int id) {
				super(name, id);
			}

			@Override
			protected void Entry(StrategyHandler2010Context context) {
				StrategyHandler2010 ctxt = context.getOwner();

				ctxt.doStopTrajectory();
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
