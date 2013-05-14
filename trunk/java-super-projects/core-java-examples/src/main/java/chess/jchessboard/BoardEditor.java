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

/* $Id: BoardEditor.java 5 2009-11-10 07:56:47Z cdivossen $ */

package chess.jchessboard;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author cd
 *
 * The BoardConnector is window in wich current board setting can be changed.
 * TODO: Elements to be implemented: Castling abilities
 * 
 */
public class BoardEditor extends JFrame {
	private JChessBoardImpl jcb=null;
	private VisualBoard visualBoard=null;
	private Container contentPane;

	public static String getVersion() {
		return "$Id: BoardEditor.java 5 2009-11-10 07:56:47Z cdivossen $";
	}

	public void exit() {
		jcb.newGame(visualBoard.virtualBoard);
		dispose();
	}

	public BoardEditor(JChessBoardImpl _jcb) {
		super("JChessBoard - Editor");
		jcb=_jcb;	

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu editorMenu = new JMenu("Editor");
		menuBar.add(editorMenu);

		JMenuItem setupItem = new JMenuItem("Setup");
		setupItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				visualBoard.virtualBoard.init();
				visualBoard.redraw();
			}
		});
		editorMenu.add(setupItem);

		JMenuItem clearItem = new JMenuItem("Clear");
		clearItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				visualBoard.virtualBoard.clear();
				visualBoard.redraw();
			}
		});
		editorMenu.add(clearItem);

		JMenuItem closeItem = new JMenuItem("Close & Save");
		closeItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exit();
			}
		});
		editorMenu.add(closeItem);

		contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(1,1));
		visualBoard = new VisualBoard(jcb,true);
		contentPane.add(visualBoard);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		pack();
		setResizable(false);
		setVisible(true);

	}
}
