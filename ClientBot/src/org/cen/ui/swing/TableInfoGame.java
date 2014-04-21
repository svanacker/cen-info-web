package org.cen.ui.swing;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.cen.ui.gameboard.ISimulGameBoard;

public class TableInfoGame extends JTable {

	private static final long serialVersionUID = 1L;

	public class CellRendererInfoGame extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
			return this;
		}
	}

	public TableInfoGame(ISimulGameBoard gameBoard) {
		super(new TableModelInfoGame(gameBoard));
		setDefaultRenderer(Object.class, new CellRendererInfoGame());
	}
}
