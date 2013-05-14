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

/* $Id: VisualBoard.java 5 2009-11-10 07:56:47Z cdivossen $ */

package chess.jchessboard;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

/**
 * This class creates the visual representation of the chess board, 
 * and gives the user an interface move the figures.
 */
class VisualBoard extends JLayeredPane {
	private static final int FIELDSIZE = 40;
	// Other board sizes could be implemented by
	// changing this value and replacing the
	// figure icons.
	private static final int MAX = (FIELDSIZE * 7);
	private static final int rankLabelWidth = 15;
	private static final int rankLabelHeight = FIELDSIZE;
	private static final int fileLabelWidth = FIELDSIZE;
	private static final int fileLabelHeight = 15;
	private FigureList[] allLists = new FigureList[13];
	private boolean reverseBoard = false; //White at bottom.
	private boolean isLocked = false; // Locks the board
	private JLabel[][] background = new JLabel[8][8];
	private JLayeredPane boardPane = new JLayeredPane();
	private JLabel[] rankLabel = new JLabel[16];
	private JLabel[] fileLabel = new JLabel[16];
	protected VirtualBoard virtualBoard = null;
	private boolean isEditorBoard = false;
	private HashMap icons = new HashMap();
	private java.awt.event.ActionListener listener = new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			restoreBackground();
		}
	};
	private javax.swing.Timer restoreBackgroundTimer = new javax.swing.Timer(500, listener);

	private JChessBoardImpl jcb; // The parent JChessBoard

	public static String getVersion() {
		return "$Id: VisualBoard.java 5 2009-11-10 07:56:47Z cdivossen $";
	}

	public byte selectPromotion() {
		Object[] option = new Object[4];
		if (virtualBoard.isWhiteTurn()) {
			option[0] = (ImageIcon) icons.get(new Integer(VirtualBoard.WHITE_QUEEN));
			option[1] = (ImageIcon) icons.get(new Integer(VirtualBoard.WHITE_ROOK));
			option[2] = (ImageIcon) icons.get(new Integer(VirtualBoard.WHITE_BISHOP));
			option[3] = (ImageIcon) icons.get(new Integer(VirtualBoard.WHITE_KNIGHT));
		} else {
			option[0] = (ImageIcon) icons.get(new Integer(VirtualBoard.BLACK_QUEEN));
			option[1] = (ImageIcon) icons.get(new Integer(VirtualBoard.BLACK_ROOK));
			option[2] = (ImageIcon) icons.get(new Integer(VirtualBoard.BLACK_BISHOP));
			option[3] = (ImageIcon) icons.get(new Integer(VirtualBoard.BLACK_KNIGHT));
		}
		int choice =
			JOptionPane.showOptionDialog(
					null,
					"Promote pawn to:",
					"",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					option,
					option[0]);
		if (choice == 0)
			return VirtualBoard.QUEEN;
		else if (choice == 1)
			return VirtualBoard.ROOK;
		else if (choice == 2)
			return VirtualBoard.BISHOP;
		else if (choice == 3)
			return VirtualBoard.KNIGHT;
		else
			return VirtualBoard.NONE;
	}

	public void setLocked(boolean lock) {
		isLocked = lock;
	}

	class VisualFigure extends JLabel implements MouseListener, MouseMotionListener {
		int rank, file; // The field where this figure should be.
		int mousePressedX, mousePressedY; //Where this figure started
		// to be dragged.
		boolean isWhiteFigure;
		int type;

		public VisualFigure(int _type) {
			super((ImageIcon) icons.get(new Integer(_type)));
			type = _type;
			isWhiteFigure = type < 7;
			setSize(getIcon().getIconWidth(), getIcon().getIconHeight());
			addMouseMotionListener(this);
			addMouseListener(this);
		}
		public int getRank() {
			if (reverseBoard)
				return ((getLocation().y + (FIELDSIZE / 2)) / FIELDSIZE);
			else
				return 7 - ((getLocation().y + (FIELDSIZE / 2)) / FIELDSIZE);
		}
		public int getFile() {
			if (reverseBoard)
				return 7 - ((getLocation().x + (FIELDSIZE / 2)) / FIELDSIZE);
			else
				return ((getLocation().x + (FIELDSIZE / 2)) / FIELDSIZE);
		}
		public void setField(int newRank, int newFile) {
			if (reverseBoard)
				setLocation((7 - newFile) * FIELDSIZE, newRank * FIELDSIZE);
			else
				setLocation(newFile * FIELDSIZE, (7 - newRank) * FIELDSIZE);
		}

		// The following methods are required for the MouseListener
		// and MouseMotionListener interfaces.
		public void mousePressed(MouseEvent e) {
			if (isLocked)
				return;
			rank = getRank();
			file = getFile();
			mousePressedX = e.getX();
			mousePressedY = e.getY();
			synchronized (virtualBoard) {
				if (virtualBoard.field[(rank << 3) + file] == VirtualBoard.EMPTY_FIELD) {
					VisualBoard.this.update();
					return;
				}
				if (jcb.settings.showPossibleMoves)
					for (int r = 0; r < 8; r++)
						for (int f = 0; f < 8; f++)
							if (virtualBoard.isPossibleMove(new Move(rank, file, r, f)))
								if (reverseBoard)
									background[r][7 - f].setBackground(jcb.settings.boardHighlightColor);
								else
									background[7 - r][f].setBackground(jcb.settings.boardHighlightColor);
			}
		}
		public void mouseDragged(MouseEvent e) {
			if (isLocked)
				return;
			int newX, newY;
			newX = getLocation().x + e.getX() - mousePressedX;
			if (newX > MAX)
				newX = MAX;
			if (newX < 0)
				newX = 0;
			newY = getLocation().y + e.getY() - mousePressedY;
			if (newY > MAX)
				newY = MAX;
			if (newY < 0)
				newY = 0;
			setLocation(newX, newY);
			boardPane.setLayer(this, 10, 0);
		}
		public void mouseReleased(MouseEvent e) {
			if (isLocked)
				return;
			synchronized (virtualBoard) {
				int newRank = getRank();
				int newFile = getFile();
				boardPane.setLayer(this, 2, 0);
				if (rank != newRank || file != newFile) {
					final Move move = new Move(rank, file, newRank, newFile);
					if (virtualBoard.isPossibleMove(move)) {
						if ((virtualBoard.field[move.fromField()] == VirtualBoard.WHITE_PAWN && move.toRank() == 7)
								|| (virtualBoard.field[move.fromField()] == VirtualBoard.BLACK_PAWN && move.toRank() == 0)) {
							move.setPawnPromotion(selectPromotion());
						}
						//isLocked=true;
						synchronized (jcb) {
							jcb.makeUsersMove(move);
						}
					} else
						VisualBoard.this.update();
				} else
					VisualBoard.this.update();
			}
		}
		public void mouseMoved(MouseEvent e) {
		}
		public void mouseClicked(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
		public void mouseEntered(MouseEvent e) {
		}
	}

	class VisualEditorFigure extends VisualFigure {

		public VisualEditorFigure(int type) {
			super(type);
		}

		// The following methods are required for the MouseListener
		// and MouseMotionListener interfaces.
		public void mousePressed(MouseEvent e) {
			if (isLocked)
				return;
			rank = getRank();
			file = getFile();
			mousePressedX = e.getX();
			mousePressedY = e.getY();
			synchronized (virtualBoard) {
				if (virtualBoard.field[(rank << 3) + file] == VirtualBoard.EMPTY_FIELD) {
					VisualBoard.this.redraw();
					return;
				}
			}
		}
		public void mouseReleased(MouseEvent e) {
			if (isLocked)
				return;
			synchronized (virtualBoard) {
				int newRank = getRank();
				int newFile = getFile();
				boardPane.setLayer(this, 2, 0);
				if (rank != newRank || file != newFile) {
					virtualBoard.field[(rank << 3) + file] = VirtualBoard.EMPTY_FIELD;
					virtualBoard.field[(newRank << 3) + newFile] = type;
					if (type == VirtualBoard.WHITE_KING)
						virtualBoard.whiteKingPos = (newRank << 3) + newFile;
					if (type == VirtualBoard.BLACK_KING)
						virtualBoard.blackKingPos = (newRank << 3) + newFile;
				}
				VisualBoard.this.redraw();
			}
		}
	}

	class VisualPoolFigure extends VisualFigure {
		public int basePosX, basePosY;

		public VisualPoolFigure(int type, int posX, int posY) {
			super(type);
			basePosX = posX;
			basePosY = posY;
			setLocation(basePosX, basePosY);
		}

		public int getRank() {
			if (reverseBoard)
				return ((getLocation().y - fileLabelHeight + (FIELDSIZE / 2)) / FIELDSIZE);
			else
				return 7 - ((getLocation().y - fileLabelHeight + (FIELDSIZE / 2)) / FIELDSIZE);
		}
		public int getFile() {
			if (reverseBoard)
				return 7 - ((getLocation().x - rankLabelWidth + (FIELDSIZE / 2)) / FIELDSIZE);
			else
				return ((getLocation().x - rankLabelWidth + (FIELDSIZE / 2)) / FIELDSIZE);
		}

		// The following methods are required for the MouseListener
		// and MouseMotionListener interfaces.
		public void mousePressed(MouseEvent e) {
			if (isLocked)
				return;
			rank = getRank();
			file = getFile();
			mousePressedX = e.getX();
			mousePressedY = e.getY();
		}

		public void mouseDragged(MouseEvent e) {
			if (isLocked)
				return;
			int newX, newY;
			newX = getLocation().x + e.getX() - mousePressedX;
			newY = getLocation().y + e.getY() - mousePressedY;
			setLocation(newX, newY);
		}

		public void mouseReleased(MouseEvent e) {
			if (isLocked)
				return;
			synchronized (virtualBoard) {
				int newRank = getRank();
				int newFile = getFile();
				if (newFile >= 0 && newFile < 8 && newRank >= 0 && newRank < 8)
					virtualBoard.field[(newRank << 3) + newFile] = type;
				if (type == VirtualBoard.WHITE_KING)
					virtualBoard.whiteKingPos = (newRank << 3) + newFile;
				if (type == VirtualBoard.BLACK_KING)
					virtualBoard.blackKingPos = (newRank << 3) + newFile;
				VisualBoard.this.redraw();
			}
			setLocation(basePosX, basePosY);
		}
	}

	/**
	 * This class maintains a dynamic list of figures (VisualFigure) for each figure type.
	 */
	class FigureList {
		// Each List begins empty. When a figure is requested with getFigure
		// and there are no more figures in the list, a new VisualFigure will be
		// created and appended to the list.
		// Ok, this could be done with a static list of a defined length for
		// each type, too, but I wanted to try it this way. I like dynamic
		// structures, allthough this is static from some point of view: because
		// the lists are rewound before the board is updated, already created
		// figures will be reused, and the laws of chess don't allow unlimited
		// figures. But don't forget: There could be up to nine queens of one
		// color! Or ten rooks, knights, bishops...
		private ArrayList list = new ArrayList(8);
		private int type;
		private ImageIcon icon;
		private int index = 0;

		public VisualFigure getFigure() {
			VisualFigure figure;
			if (index >= list.size()) {
				if (isEditorBoard)
					list.add(new VisualEditorFigure(type));
				else
					list.add(new VisualFigure(type));
				figure = (VisualFigure) list.get(index);
			} else
				figure = (VisualFigure) list.get(index);
			if (!figure.isVisible())
				figure.setVisible(true);
			index++;
			return (figure);
		}
		public void rewind() {
			index = 0;
		}
		public void hideRest() {
			if (list.size() > 0)
				while (index < list.size()) {
					((VisualFigure) list.get(index)).setVisible(false);
					index++;
				}
		}
		public FigureList(int _type) {
			type = _type;
		}
	}

	public void update() {
		synchronized (this) {
			virtualBoard = jcb.getCurrentVirtualBoard();
			redraw();
		}
	}

	public void redraw() {
		VisualFigure figure;
		int x, y;
		restoreBackground();
		for (int t = 1; t < allLists.length; t++)
			allLists[t].rewind();
		synchronized (virtualBoard) {
			for (int rank = 0; rank < 8; rank++) {
				for (int file = 0; file < 8; file++) {
					int type = virtualBoard.field[(rank << 3) + file];
					if (type != VirtualBoard.EMPTY_FIELD) {
						figure = allLists[type].getFigure();
						boardPane.add(figure, new Integer(2), 0);
						figure.setField(rank, file);
					}
				}
			}
			for (int t = 1; t < allLists.length; t++)
				allLists[t].hideRest();
		}
//		if (!isEditorBoard) {
	//          setLocked(virtualBoard.gameIsFinished());
	//    }
//            repaint();
	}

	public boolean reverseBoard() {
		return reverseBoard;
	}

	public void setReverseBoard(boolean reverse) {
		reverseBoard = reverse;
		updateLabels();
		redraw();
	}

	public void updateLabels() {
		if (!reverseBoard) {
			for (int n = 0; n < 8; n++) {
				rankLabel[n].setText("" + (8 - n));
				rankLabel[n + 8].setText("" + (8 - n));
				fileLabel[n].setText("" + (char) (((int) 'a') + n));
				fileLabel[n + 8].setText("" + (char) (((int) 'a') + n));
			}
		} else {
			for (int n = 0; n < 8; n++) {
				rankLabel[7 - n].setText("" + (8 - n));
				rankLabel[15 - n].setText("" + (8 - n));
				fileLabel[7 - n].setText("" + (char) (((int) 'a') + n));
				fileLabel[15 - n].setText("" + (char) (((int) 'a') + n));
			}
		}
	}

	public void restoreBackground() {
		for (int rank = 0; rank < 8; rank++)
			for (int file = 0; file < 8; file++)
				if ((rank + file & 1) == 1)
					background[rank][file].setBackground(jcb.settings.boardColor1);
				else
					background[rank][file].setBackground(jcb.settings.boardColor2);
	}

	public void showMove(Move move, int duration) {
		if (reverseBoard) {
			background[move.fromRank()][7 - move.fromFile()].setBackground(jcb.settings.boardHighlightColor);
			background[move.toRank()][7 - move.toFile()].setBackground(jcb.settings.boardHighlightColor);
		} else {
			background[7 - move.fromRank()][move.fromFile()].setBackground(jcb.settings.boardHighlightColor);
			background[7 - move.toRank()][move.toFile()].setBackground(jcb.settings.boardHighlightColor);
		}
		restoreBackgroundTimer.setRepeats(false);
		restoreBackgroundTimer.setDelay(duration);
		restoreBackgroundTimer.restart();
	}

	private void loadIcon(int type, String filename) {
		String fullFilename = "/chess/jchessboard/images/" + filename;
		URL url = VisualBoard.class.getResource(fullFilename);
		ImageIcon icon = url != null ? new ImageIcon((URL) url) : new ImageIcon("null");
		if (icon == null) { // Doesn't work, damn!
			System.out.println("Could not load image from jar file: " + filename + "\n");
			System.exit(-1);
		}
		icons.put(new Integer(type), icon);
	}

	private void initialize() {
		loadIcon(VirtualBoard.WHITE_KING, "WK.gif");
		loadIcon(VirtualBoard.WHITE_QUEEN, "WQ.gif");
		loadIcon(VirtualBoard.WHITE_ROOK, "WR.gif");
		loadIcon(VirtualBoard.WHITE_BISHOP, "WB.gif");
		loadIcon(VirtualBoard.WHITE_KNIGHT, "WN.gif");
		loadIcon(VirtualBoard.WHITE_PAWN, "WP.gif");
		loadIcon(VirtualBoard.BLACK_KING, "BK.gif");
		loadIcon(VirtualBoard.BLACK_QUEEN, "BQ.gif");
		loadIcon(VirtualBoard.BLACK_ROOK, "BR.gif");
		loadIcon(VirtualBoard.BLACK_BISHOP, "BB.gif");
		loadIcon(VirtualBoard.BLACK_KNIGHT, "BN.gif");
		loadIcon(VirtualBoard.BLACK_PAWN, "BP.gif");

		virtualBoard = jcb.getCurrentVirtualBoard();

		for (int f = 1; f < 13; f++) {
			allLists[f] = new FigureList(f);
		}
		setLayout(null);
		setBackground(jcb.settings.boardBackgroundColor);
		setOpaque(true);
		add(boardPane);
		boardPane.setBounds(rankLabelWidth, fileLabelHeight, 8 * FIELDSIZE, 8 * FIELDSIZE);
		// Create field labels
		for (int n = 0; n < 8; n++) {
			rankLabel[n] = new JLabel("" + (8 - n), SwingConstants.CENTER);
			add(rankLabel[n]);
			rankLabel[n].setBounds(0, fileLabelHeight + n * rankLabelHeight, rankLabelWidth, rankLabelHeight);
			rankLabel[n].setForeground(jcb.settings.boardLabelColor);

			rankLabel[n + 8] = new JLabel("" + (8 - n), SwingConstants.CENTER);
			add(rankLabel[n + 8]);
			rankLabel[n
			          + 8].setBounds(
			        		  rankLabelWidth + 8 * FIELDSIZE,
			        		  fileLabelHeight + n * rankLabelHeight,
			        		  rankLabelWidth,
			        		  rankLabelHeight);
			rankLabel[n + 8].setForeground(jcb.settings.boardLabelColor);

			fileLabel[n] = new JLabel("" + (char) (((int) 'a') + n), SwingConstants.CENTER);
			add(fileLabel[n]);
			fileLabel[n].setBounds(
					rankLabelWidth + n * fileLabelWidth,
					8 * FIELDSIZE + fileLabelHeight,
					fileLabelWidth,
					fileLabelHeight);
			fileLabel[n].setForeground(jcb.settings.boardLabelColor);

			fileLabel[n + 8] = new JLabel("" + (char) (((int) 'a') + n), SwingConstants.CENTER);
			add(fileLabel[n + 8]);
			fileLabel[n + 8].setBounds(rankLabelWidth + n * fileLabelWidth, 0, fileLabelWidth, fileLabelHeight);
			fileLabel[n + 8].setForeground(jcb.settings.boardLabelColor);
		}
		// Create the background:
		for (int x = 0; x < 8; x++)
			for (int y = 0; y < 8; y++) {
				JLabel backgroundLabel = new JLabel();
				backgroundLabel.setOpaque(true);
				backgroundLabel.setBounds(x * FIELDSIZE, y * FIELDSIZE, FIELDSIZE, FIELDSIZE);
				boardPane.add(backgroundLabel, new Integer(1), 0);
				background[y][x] = backgroundLabel;
			}
		// Create the reverse board button
		ImageIcon reverseIcon = new ImageIcon(VisualBoard.class.getResource("/chess/jchessboard/images/reverse.png"));
		//JLabel reverseButton = new JLabel("R",SwingConstants.CENTER);
		JButton reverseButton = new JButton(reverseIcon);
		add(reverseButton);
		reverseButton.setForeground(jcb.settings.boardLabelColor);
		reverseButton.setBackground(jcb.settings.boardBackgroundColor);
		reverseButton.setBorder(null);
		reverseButton.setOpaque(true);
		reverseButton.setBounds(0, 0, rankLabelWidth, fileLabelHeight);
		reverseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setReverseBoard(!reverseBoard());
				jcb.updateMenu();
				// TODO: Shouldn't be done by an editor board.
			}
		});
		if (isEditorBoard) {
			JLabel backgroundLabel = new JLabel();
			backgroundLabel.setBackground(new java.awt.Color(150, 150, 150));
			backgroundLabel.setOpaque(true);
			backgroundLabel.setBounds(8 * FIELDSIZE + 2 * rankLabelWidth, 0, 2 * FIELDSIZE, 6 * FIELDSIZE);
			add(backgroundLabel, new Integer(1));

			VisualFigure figure = new VisualPoolFigure(VirtualBoard.WHITE_KING, 8 * FIELDSIZE + 2 * rankLabelWidth, 0);
			add(figure, new Integer(10));
			figure = new VisualPoolFigure(VirtualBoard.WHITE_QUEEN, 8 * FIELDSIZE + 2 * rankLabelWidth, FIELDSIZE);
			add(figure, new Integer(10));
			figure = new VisualPoolFigure(VirtualBoard.WHITE_ROOK, 8 * FIELDSIZE + 2 * rankLabelWidth, FIELDSIZE * 2);
			add(figure, new Integer(10));
			figure = new VisualPoolFigure(VirtualBoard.WHITE_BISHOP, 8 * FIELDSIZE + 2 * rankLabelWidth, FIELDSIZE * 3);
			add(figure, new Integer(10));
			figure = new VisualPoolFigure(VirtualBoard.WHITE_KNIGHT, 8 * FIELDSIZE + 2 * rankLabelWidth, FIELDSIZE * 4);
			add(figure, new Integer(10));
			figure = new VisualPoolFigure(VirtualBoard.WHITE_PAWN, 8 * FIELDSIZE + 2 * rankLabelWidth, FIELDSIZE * 5);
			add(figure, new Integer(10));

			figure = new VisualPoolFigure(VirtualBoard.BLACK_KING, 9 * FIELDSIZE + 2 * rankLabelWidth, 0);
			add(figure, new Integer(10));
			figure = new VisualPoolFigure(VirtualBoard.BLACK_QUEEN, 9 * FIELDSIZE + 2 * rankLabelWidth, FIELDSIZE);
			add(figure, new Integer(10));
			figure = new VisualPoolFigure(VirtualBoard.BLACK_ROOK, 9 * FIELDSIZE + 2 * rankLabelWidth, FIELDSIZE * 2);
			add(figure, new Integer(10));
			figure = new VisualPoolFigure(VirtualBoard.BLACK_BISHOP, 9 * FIELDSIZE + 2 * rankLabelWidth, FIELDSIZE * 3);
			add(figure, new Integer(10));
			figure = new VisualPoolFigure(VirtualBoard.BLACK_KNIGHT, 9 * FIELDSIZE + 2 * rankLabelWidth, FIELDSIZE * 4);
			add(figure, new Integer(10));
			figure = new VisualPoolFigure(VirtualBoard.BLACK_PAWN, 9 * FIELDSIZE + 2 * rankLabelWidth, FIELDSIZE * 5);
			add(figure, new Integer(10));

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(3, 1));
			JLabel turnLabel = new JLabel("Turn:");
			buttonPanel.add(turnLabel);
			JRadioButton whiteTurnButton = new JRadioButton("White");
			whiteTurnButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					virtualBoard.isWhiteTurn = true;
				}
			});
			whiteTurnButton.setSelected(virtualBoard.isWhiteTurn);
			JRadioButton blackTurnButton = new JRadioButton("Black");
			blackTurnButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					virtualBoard.isWhiteTurn = false;
				}
			});
			blackTurnButton.setSelected(!virtualBoard.isWhiteTurn);
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(whiteTurnButton);
			buttonGroup.add(blackTurnButton);
			buttonPanel.add(whiteTurnButton);
			buttonPanel.add(blackTurnButton);
			add(buttonPanel);
			buttonPanel.setBounds(
					8 * FIELDSIZE + 2 * rankLabelWidth,
					FIELDSIZE * 6,
					2 * FIELDSIZE,
					2 * FIELDSIZE + 2 * fileLabelHeight);
		}

		update();
		Dimension panelSize = null;
		if (isEditorBoard) {
			panelSize = new Dimension(10 * FIELDSIZE + 2 * rankLabelWidth, 8 * FIELDSIZE + 2 * fileLabelHeight);
		} else {
			panelSize = new Dimension(8 * FIELDSIZE + 2 * rankLabelWidth, 8 * FIELDSIZE + 2 * fileLabelHeight);
		}
		setPreferredSize(panelSize);
		setMinimumSize(panelSize);
		if (isEditorBoard) {
			setMaximumSize(panelSize);
		}
		//        setSize(8*FIELDSIZE+2*rankLabelWidth,8*FIELDSIZE+2*fileLabelHeight);
	}

	public VisualBoard(JChessBoardImpl b) {
		jcb = b;
		initialize();
	}

	public VisualBoard(JChessBoardImpl b, boolean _isEditorBoard) {
		jcb = b;
		isEditorBoard = _isEditorBoard;
		initialize();
	}

}
