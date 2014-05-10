package org.cen.cup.cup2011.actions;

import org.cen.actions.AbstractGameActionMap;
import org.cen.actions.IGameAction;
import org.cen.robot.services.IRobotServiceProvider;

public class GameActionMap2011 extends AbstractGameActionMap {

	private static final IGameAction ACTION_KING_PICKUP = new KingPickUpAction();

	private static final IGameAction ACTION_PAWN_PICKUP = new PawnPickUpAction();

	private static final IGameAction ACTION_PAWN_DROP = new PawnDropAction();

	private static final IGameAction ACTION_PICKER_OPEN = new PickerOpenAction();

	private static final IGameAction ACTION_PICKER_CLOSE = new PickerCloseAction();

	private static final IGameAction ACTION_PAWN_PICKUP_ANALYZE = new PawnPickUpAnalyzeAction();

	private static final IGameAction ACTION_KING_DROP = new KingDropAction();

	private IRobotServiceProvider servicesProvider;

	public GameActionMap2011(IRobotServiceProvider servicesProvider) {
		super();
		this.servicesProvider = servicesProvider;
	}

	public IGameAction getKingPickUpAction() {
		return ACTION_KING_PICKUP;
	}

	public IGameAction getPawnDropAction() {
		return ACTION_PAWN_DROP;
	}

	public IGameAction getPawnDropKingAction() {
		return ACTION_KING_DROP;
	}

	public IGameAction getPawnPickUpAction() {
		return ACTION_PAWN_PICKUP;
	}

	public IGameAction getPawnPickUpAndAnalyzeAction() {
		return ACTION_PAWN_PICKUP_ANALYZE;
	}

	public IGameAction getPickerCloseAction() {
		return ACTION_PICKER_CLOSE;
	}

	public IGameAction getPickerOpenAction() {
		return ACTION_PICKER_OPEN;
	}
}
