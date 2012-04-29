package org.cen.ui.rtp;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import org.cen.robot.IRobotServiceProvider;
import org.cen.ui.gameboard.GameBoardPainter;

public class GameBoardImageDataSource extends PushImageDataSource {
	GameBoardPainter painter;

	public GameBoardImageDataSource(IRobotServiceProvider provider) {
		super();
		Dimension d = new Dimension(300, 400);
		painter = new GameBoardPainter(provider);
		painter.setSize(d);
		image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
	}

	@Override
	public void update() {
		painter.paint(image.getGraphics());
		super.update();
	}
}
