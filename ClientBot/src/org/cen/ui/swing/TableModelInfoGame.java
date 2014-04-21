package org.cen.ui.swing;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.cen.ui.gameboard.IGameBoardElement;
import org.cen.ui.gameboard.ISimulGameBoard;
import org.cen.ui.gameboard.elements.SimulOpponentElement;
import org.cen.ui.gameboard.elements.SimulRobotElement;

public class TableModelInfoGame extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private final List<IGameBoardElement> elements;

	private final String[] entetes = { "Element", "Position", "Orientation" };

	private ISimulGameBoard gameboard;

	public TableModelInfoGame(ISimulGameBoard pGameboard) {
		super();
		this.gameboard = pGameboard;
		elements = new ArrayList<IGameBoardElement>();
		// elements.add(new SimulRobotElement(0, 0, 0));
		// elements.add(new SimulRobotElement(0, 0, 0));
		updateModel();
	}

	@Override
	public int getColumnCount() {
		return entetes.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return entetes[columnIndex];
	}

	@Override
	public int getRowCount() {
		return elements.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return elements.get(rowIndex).getName();
		case 1:
			Point2D elementPosition = elements.get(rowIndex).getPosition();
			return (int) elementPosition.getX() + ", "
					+ (int) elementPosition.getY();
		case 2:
			return elements.get(rowIndex).getOrientation();
			// case 3:
			// return
		default:
			return null; // Ne devrait jamais arriver
		}
	}

	private void updateModel() {
		// elements.clear();
		elements.addAll(gameboard.getElements(SimulRobotElement.class));
		elements.addAll(gameboard.getElements(SimulOpponentElement.class));
	}

	// @Override
	// public Class getColumnClass(int columnIndex){
	// switch(columnIndex){
	// case 1:
	// return Point2D.class;
	// case 2:
	// return Double.class;
	// default:
	// return Object.class;
	// }
	// }

	// public void addElement(AbstractGameBoardElement pElement) {
	// this.elements.add(pElement);
	//
	// fireTableRowsInserted(elements.size() -1, elements.size() -1);
	// }

}
