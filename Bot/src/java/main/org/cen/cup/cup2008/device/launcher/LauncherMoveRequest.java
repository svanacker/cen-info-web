package org.cen.cup.cup2008.device.launcher;

public class LauncherMoveRequest extends LauncherRequest {
	public enum LauncherMove {
		CLOSE, DOWN, OPEN, UP;
	}

	private LauncherMove move;

	public LauncherMoveRequest(LauncherMove move) {
		super();
		this.move = move;
	}

	public LauncherMove getMove() {
		return move;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[move: " + move + "]";
	}
}
