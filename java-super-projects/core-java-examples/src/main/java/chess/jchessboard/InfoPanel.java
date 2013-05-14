/* JChessBoard -- a chess game
 * Copyright (C) 2000-2004 Claus Divossen <claus.divossen@gmx.de>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

/* $Id: InfoPanel.java 5 2009-11-10 07:56:47Z cdivossen $ */
package chess.jchessboard;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;

/**
 * @author cd
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class InfoPanel extends JScrollPane {
	private PGN.STR str = new PGN.STR();
	private TagTableModel tableModel;
	private JTable jTable;
	private JPopupMenu tagTableContextMenu;

	class TagTableModel extends javax.swing.table.AbstractTableModel {
		public int getColumnCount() {
			return 2;
		}
		public int getRowCount() {
			return str.size();
		}
		public String getColumnName(int col) {
			if (col == 0)
				return "Tag";
			if (col == 1)
				return "Value";
			return null;
		}
		public Object getValueAt(int row, int col) {
			if (col == 0)
				return str.tags()[row];
			else if (col == 1)
				return str.getTag(str.tags()[row]);
			else
				return null;
		}
		public boolean isCellEditable(int row, int column) {
			return column == 1;
		}
		public void setValueAt(Object value, int row, int column) {
			if (column == 1 && value instanceof String) {
				str.setTag(str.tags()[row], (String) value);
			}
			// TODO: Update gameTable to show the new value.
		}
	}

	private void updateFields() {
		if (str == null)
			return;
		tableModel.fireTableChanged(new TableModelEvent(tableModel));
	}

	public void setSTR(PGN.STR str) {
		this.str = str;
		updateFields();
	}

	public PGN.STR getSTR() {
		return str;
	}

	public InfoPanel() {
		super();
		tableModel = new TagTableModel();
		jTable = new JTable(tableModel);
		//            jTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setViewportView(jTable);
		jTable.setFont(new Font("SansSerrif", Font.PLAIN, 10));
		jTable.setRowSelectionAllowed(false);
		tagTableContextMenu = new JPopupMenu();
		JMenuItem removeTagItem = new JMenuItem("Remove tag");
		removeTagItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				str.removeTag(
						(String) tableModel.getValueAt(
								jTable.getSelectedRow(),
								0));
				tableModel.fireTableChanged(
						new TableModelEvent(tableModel));
			}
		});
		tagTableContextMenu.add(removeTagItem);

		JMenuItem addTagItem = new JMenuItem("Add tag...");
		addTagItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tagName =
					JOptionPane.showInputDialog(
							null,
							"Tag name:",
							"Add tag",
							JOptionPane.QUESTION_MESSAGE);
				if (tagName != null
						&& !tagName.equals("")
						&& !str.hasTag(tagName)) {
					str.setTag(tagName, "");
					tableModel.fireTableChanged(
							new TableModelEvent(tableModel));
				}
			}
		});
		tagTableContextMenu.add(addTagItem);

		jTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					tagTableContextMenu.show(
							(Component) e.getSource(),
							e.getX(),
							e.getY());
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					tagTableContextMenu.show(
							(Component) e.getSource(),
							e.getX(),
							e.getY());
				}
			}
		});
	}
 }