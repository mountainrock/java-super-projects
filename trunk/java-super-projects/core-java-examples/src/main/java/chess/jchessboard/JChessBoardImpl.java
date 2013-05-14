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

/* $Id: JChessBoard.java 7 2009-11-15 18:58:42Z cdivossen $ */

package chess.jchessboard;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * JChessBoard is the main class. It does the following things: - manage the gui - create the other class instances: AI, VirtualBoard (the main baord) VisualBoard, BoardConnector, ConnectionIndicator,
 * ConnectionListener - dispatch the moves and other events to the appropriate recipients All communication between the other classes will be done through JChessBoard, or using its public methods and
 * attributes.
 * @see AI
 * @see VirtualBoard
 * @see VisualBoard
 * @see ConnectionListener
 * @see BoardConnector
 * @see Protocol
 * @see ConnectionIndicator
 * @see Move
 * @see PGN
 * @see GameTable
 * @see GameNode
 * @see Notation
 * @see History
 * @see InfoPanel
 * @see Chat
 * @see BoardEditor
 * @see ChessClock
 * @see Settings
 * */

public class JChessBoardImpl extends JFrame implements JChessBoard {
	static final String TITLE = "JChessBoard v1.5.1";
	static final String COPYRIGHT_MESSAGE = "JChessBoard -- a chess game\n" + "Copyright (C) 2000 Claus Divossen <claus.divossen\u0040gmx.de>\n" + "\n"
			+ "This program is free software; you can redistribute it and/or modify\n" + "it under the terms of the GNU General Public License as published by\n"
			+ "the Free Software Foundation; either version 2 of the License, or\n" + "(at your option) any later version.\n" + "\n"
			+ "This program is distributed in the hope that it will be useful,\n" + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
			+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" + "GNU General Public License for more details.\n" + "\n"
			+ "You should have received a copy of the GNU General Public License\n" + "along with this program; if not, write to the Free Software\n"
			+ "Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.\n";
	static final int MIN_WIDTH = 350;
	static final int MIN_HEIGHT = 471;
	final static String HUMAN = "Human";
	final static String COMPUTER = "Computer";
	final static String PEER = "Peer";
	final static String UNKNOWN = "-----";
	VisualBoard visualBoard;
	ConnectionIndicator connectionIndicator;
	BoardConnector boardConnector;
	private AI ai;
	private ChessClock chessClock;
	private Chat chat;
	private String whitePlayer = HUMAN;
	private String blackPlayer = COMPUTER;
	private History history;
	static java.util.List windowList = new Vector();
	// Menu items that need to be known:
	private JMenuItem menuItemNewGame;
	private JMenuItem menuItemLoadFile;
	private JMenuItem menuItemSave;
	private JMenuItem menuItemSaveAs;
	private JMenuItem menuItemUploadGame;
	private JMenuItem menuItemOfferDraw;
	private JMenuItem menuItemResign;
	private JMenuItem menuItemSwitchSides;
	private JMenuItem menuItemConnect;
	private JMenuItem menuItemCloseConnection;
	private JMenuItem about;
	private JMenuItem help;
	private JCheckBoxMenuItem clockCheckbox;
	private JCheckBoxMenuItem enableBeep;
	private JCheckBoxMenuItem reverseBoard;
	private JCheckBoxMenuItem showPossibleMoves;
	// private JCheckBoxMenuItem whiteIsAI;
	// private JCheckBoxMenuItem blackIsAI;
	private JMenuItem undoMove;
	// ConnectionListener is the connection server
	private static ConnectionListener connectionListener;
	private String otherHostname = "";
	private static String fileChooserDir;
	private String fileName = "";
	private boolean fileIsChanged = false;
	private final static String settingsFileName = "JChessBoard.conf";
	Settings settings;
	private JPanel buttonPanel;
	private JButton buttonPrevious, buttonNext, buttonStart, buttonEnd;
	private boolean gameIsFinished = false;
	// Reguired for the timeout selector:
	JSlider whiteSlider, blackSlider;
	long newWhiteTime, newBlackTime;
	JLabel whiteTimeoutLabel, blackTimeoutLabel;
	boolean coupleSliders;
	JCheckBox coupleSlidersCheckBox;
	Protocol protocol = new Protocol(this);
	GameTable gameTable = null;
	int currentGameIndex = 0;
	private InfoPanel infoPanel;
	private JTabbedPane tabbedPane;
	private Notation notation;
	boolean isThreaded=true;

	public void connectionClosed() {
		// Protocol stuff
		protocol.connectionClosed();
		// Visual stuff
		chessClock.stopClock();
		connectionIndicator.setNoConnection();
		showMessage("Connection closed.");
		gameTable.setEnabled(true);
		setEnableClock(false);
		gameTable.setNetworkGameIndex(-1);
		if (fileName != null && fileName.length() > 0)
			setTitle(TITLE + " - " + fileName);
		else
			setTitle(TITLE);
		beep();
		whitePlayer = HUMAN;
		blackPlayer = HUMAN;
		boardConnector = null;
		prepareMove();
		// --JAM: Switch the board around. If Black is disconnected at the
		// beginning of the game, the board is still oriented for Black.
		// It's disconcerting.
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void connectionEstablished() {
		protocol.connectionEstablished();
		connectionIndicator.setReady();
		setEnableClock(true);
		settings.whiteTime = 600000;
		settings.blackTime = 600000;
		newGame();
		gameTable.setNetworkGameIndex(gameTable.getCurrentGameIndex());
		protocol.sendPlayerName(settings.userName);
		if (fileName != null && fileName.length() > 0)
			setTitle(TITLE + " - Connected with \"" + otherHostname + "\" - " + fileName);
		else
			setTitle(TITLE + " - Connected with \"" + otherHostname + "\"");
		beep();
		showMessage("Connected.");
		showMessage("Your are now playing with \"" + otherHostname + "\".");
		showMessage("Either player can make a move. The first move decides who plays which side.");
		showMessage("You can set your chat name with /name <your name>");
		prepareMove();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void updateNavigationButtons() {
		// Activate/deactivate navigation buttons:
		boolean hasPrev;
		if (getHistory().getPreviousGameNode() == getHistory().getCurrentGameNode()) {
			buttonStart.setEnabled(false);
			buttonPrevious.setEnabled(false);
			hasPrev = false;
		} else {
			buttonStart.setEnabled(true);
			buttonPrevious.setEnabled(true);
			hasPrev = true;
		}
		if (getHistory().getNextGameNode() == getHistory().getCurrentGameNode()) {
			buttonNext.setEnabled(false);
			buttonEnd.setEnabled(false);
		} else {
			buttonNext.setEnabled(true);
			buttonEnd.setEnabled(true);
		}
	}

	public History getHistory() {
		return history;
	}

	/*
		 * 
	 	 */
	/*
		 */
	public VirtualBoard getCurrentVirtualBoard() {
		return getHistory().getCurrentBoard();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public boolean isWhiteTurn() {
		return getHistory().getCurrentBoard().isWhiteTurn();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void makePeersMove(Move move) {
		chessClock.stopClock();
		beep();
		gotoGame(gameTable.getNetworkGameIndex());
		getHistory().gotoLast();
		// The first move decides!
		if (whitePlayer.equals(UNKNOWN) || blackPlayer == UNKNOWN) {
			if (isWhiteTurn()) {
				whitePlayer = PEER;
				blackPlayer = HUMAN;
				visualBoard.setReverseBoard(true);
				reverseBoard.setSelected(true);
			} else {
				whitePlayer = HUMAN;
				blackPlayer = PEER;
			}
		}
		updateSTR();
		VirtualBoard virtualBoard = getCurrentVirtualBoard().clonedBoard();
		String algMove = virtualBoard.algebraic(move);
		if (isConnected())
			showMessage("Opponents move: " + algMove);
		try {
			virtualBoard.makeMove(move);
			getHistory().addMove(algMove, virtualBoard);
			visualBoard.showMove(move, 1000);
			chessClock.startClock();
			checkFinish();
		} catch (VirtualBoard.ImpossibleMoveException e) {
			showMessage("Error parsing opponents move: " + move);
			protocol.sendError("Error parsing move: " + move);
		}
		prepareMove();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void makeBatchMove(Move move) {
		VirtualBoard virtualBoard = getCurrentVirtualBoard().clonedBoard();
		String algMove = virtualBoard.algebraic(move);
		try {
			virtualBoard.makeMove(move);
		} catch (VirtualBoard.ImpossibleMoveException e) {
			showMessage("Error parsing batch move: " + move);
		}
		getHistory().addMove(algMove, virtualBoard);
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void makeAIsMove(Move move) {
		chessClock.stopClock();
		VirtualBoard virtualBoard = getCurrentVirtualBoard().clonedBoard();
		String algMove = virtualBoard.algebraic(move);
		showMessage("AI's move: " + algMove);
		try {
			virtualBoard.makeMove(move);
			getHistory().addMove(algMove, virtualBoard);
			visualBoard.showMove(move, 1000);
			chessClock.startClock();
			triggerAI();
			checkFinish();
		} catch (VirtualBoard.ImpossibleMoveException e) {
		}
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void makeUsersMove(Move move) {
		chessClock.stopClock();
		VirtualBoard virtualBoard = getCurrentVirtualBoard().clonedBoard();
		String algMove = virtualBoard.algebraic(move);
		if (isConnected())
			showMessage("Your move: " + algMove);
		try {
			virtualBoard.makeMove(move);
		} catch (VirtualBoard.ImpossibleMoveException e) {
		}
		getHistory().addMove(algMove, virtualBoard);
		if (isConnected() && gameTable.getCurrentGameIndex() == gameTable.getNetworkGameIndex()) {
			protocol.sendMove(move);
			// The first move decides!
			if (whitePlayer.equals(UNKNOWN) || blackPlayer == UNKNOWN) {
				if (isWhiteTurn()) {
					whitePlayer = PEER;
					blackPlayer = HUMAN;
					visualBoard.setReverseBoard(true);
					reverseBoard.setSelected(true);
				} else {
					whitePlayer = HUMAN;
					blackPlayer = PEER;
				}
			}
			updateSTR();
			if (getEnableClock()) {
				if (virtualBoard.isWhiteTurn())
					protocol.sendPlayersTime(chessClock.getBlackTime());
				else
					protocol.sendPlayersTime(chessClock.getWhiteTime());
			}
		}
		chessClock.startClock();
		triggerAI();
		checkFinish();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void triggerAI() {
		if (!(whitePlayer == COMPUTER) && !(blackPlayer == COMPUTER))
			return;
		if (((whitePlayer == COMPUTER) || (blackPlayer == COMPUTER)) && getAi() == null) {
			ai = new AI(this, isThreaded);
		}
		if (getAi() != null && !(whitePlayer == COMPUTER) && !(blackPlayer == COMPUTER)) {
			getAi().shutdown();
			ai = null;
			return;
		}
		if (isWhiteTurn()) {
			if (whitePlayer == COMPUTER) {
				long timeout;
				if (clockCheckbox.isSelected()) {
					if (getHistory().getFullMoveNumber() < 40)
						timeout = chessClock.getWhiteTime() / ((long) (60 - getHistory().getFullMoveNumber()));
					else
						timeout = chessClock.getWhiteTime() / 20L;
					if (timeout < 500) // Minimum time: 500ms.
						timeout = 500;
				} else
					timeout = 5000; // Fixed time, 5 sec.
				getAi().prepareMove(timeout);
			}
		} else {
			if (blackPlayer == COMPUTER) {
				long timeout;
				if (clockCheckbox.isSelected()) {
					if (getHistory().getFullMoveNumber() < 40)
						timeout = chessClock.getBlackTime() / ((long) (60 - getHistory().getFullMoveNumber()));
					else
						timeout = chessClock.getBlackTime() / 20;
					if (timeout < 500) // Minimum time: 500ms.
						timeout = 500;
				} else
					timeout = 5000; // Fixed time, 5 sec.
				getAi().prepareMove(timeout);
			}
		}
	}

	public AI getAi() {
		return ai;
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void prepareMove() {
		// Create the appropiate setting to receive the next move.
		// Lock/Unlock the visualBoard:
		// if (gameIsFinished)
		// visualBoard.setLocked(true);
		// else
		visualBoard.setLocked(false);
		if (isConnected() && getHistory().getNextGameNode() != getHistory().getCurrentGameNode())
			visualBoard.setLocked(true);
		if (isConnected() && (gameTable.getCurrentGameIndex() != gameTable.getNetworkGameIndex()))
			visualBoard.setLocked(true);
		if (isWhiteTurn() && ((whitePlayer == PEER) || (whitePlayer == COMPUTER)))
			visualBoard.setLocked(true);
		if (!isWhiteTurn() && ((blackPlayer == PEER) || (blackPlayer == COMPUTER)))
			visualBoard.setLocked(true);
		updateMenu();
		chessClock.updatePlayerSelectors();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void switchSides() {
		String tmp = whitePlayer;
		whitePlayer = blackPlayer;
		blackPlayer = tmp;
		reverseBoard.setSelected(!reverseBoard.isSelected());
		visualBoard.setReverseBoard(reverseBoard.isSelected());
		updateSTR();
		showMessage("Sides switched.");
		prepareMove();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void updateSTR() {
		PGN.STR str = gameTable.getSTR(gameTable.getCurrentGameIndex());
		str.setTag("White", "?");
		str.setTag("Black", "?");
		if (whitePlayer.equals(PEER))
			str.setTag("White", protocol.getOtherPlayersName());
		if (whitePlayer.equals(HUMAN))
			str.setTag("White", settings.userName);
		if (whitePlayer.equals(COMPUTER))
			str.setTag("White", TITLE);
		if (blackPlayer.equals(PEER))
			str.setTag("Black", protocol.getOtherPlayersName());
		if (blackPlayer.equals(HUMAN))
			str.setTag("Black", settings.userName);
		if (blackPlayer.equals(COMPUTER))
			str.setTag("Black", TITLE);
		infoPanel.repaint();
		gameTable.repaint();
	}

	/*
	 * Stops the game and creates the appropriate message if the game comes out to be finished.
	 */
	/*
		 * 
	 	 */
	/*
		 */
	public void checkFinish() {
		VirtualBoard virtualBoard = getHistory().getCurrentBoard();
		if (virtualBoard.gameIsFinished()) {
			if (virtualBoard.onlyKingsLeft()) {
				showMessage("Draw!");
			} else if (virtualBoard.isWhiteTurn()) {
				if (virtualBoard.isAttackedByBlack(virtualBoard.getWhiteKingPos())) {
					Object[] options = { "Accept", "Reject" };
					showMessage("Black wins!");
					JOptionPane.showMessageDialog(null, "Black wins!");
				} else {
					showMessage("Draw (stalemate)!");
					JOptionPane.showMessageDialog(null, "Draw!");
				}
			} else {
				if (virtualBoard.isAttackedByWhite(virtualBoard.getBlackKingPos())) {
					showMessage("White wins!");
					JOptionPane.showMessageDialog(null, "White wins!");
				} else {
					showMessage("Draw (stalemate)!");
					JOptionPane.showMessageDialog(null, "Draw!");
				}
			}
			chessClock.stopClock();
		} else if (clockCheckbox.isSelected() && (chessClock.getWhiteTime() <= 0 || chessClock.getBlackTime() <= 0)) {
			chessClock.stopClock();
			if (chessClock.getWhiteTime() <= 0) {
				showMessage("Black wins (time forfeit)!");
				JOptionPane.showMessageDialog(null, "Black wins!");
			} else {
				showMessage("White wins (time forfeit)!");
				JOptionPane.showMessageDialog(null, "White wins!");
			}
			showMessage("White: " + chessClock.formatTime(chessClock.getWhiteTime()) + ", Black: " + chessClock.formatTime(chessClock.getBlackTime()));
			setEnableClock(false);
			showMessage("The clocks have been disabled, you can continue playing, if you want to.");
		} else
			prepareMove();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void newGame() {
		PGN.STR str = new PGN.STR();
		gotoGame(gameTable.addGame("", str));
		visualBoard.setReverseBoard(false);
		if (isConnected()) {
			whitePlayer = UNKNOWN;
			blackPlayer = UNKNOWN;
			gameTable.setNetworkGameIndex(gameTable.getCurrentGameIndex());
		} else {
			whitePlayer = HUMAN;
			blackPlayer = COMPUTER;
		}
		Calendar cal = Calendar.getInstance();
		str.setTag("Date", cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH));
		chessClock.updatePlayerSelectors();
		updateSTR();
		if (getAi() != null)
			getAi().newGame();
		chessClock.stopClock();
		chessClock.resetClocks();
		prepareMove();
		// history.setResult("*");
		gameIsFinished = false;
		fileIsChanged = true;
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void newGame(VirtualBoard vb) {
		newGame();
		getHistory().setBoard(vb.clonedBoard());
		PGN.STR str = gameTable.getSTR(gameTable.getCurrentGameIndex());
		str.setTag("SetUp", "1");
		str.setTag("FEN", vb.getFEN());
		update();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void exit() {
		if (isConnected()) {
			int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Close connection?", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				boardConnector.closeConnection();
				connectionClosed();
			} else
				return;
		}
		if (isFileChanged())
			askForSaving();
		if (getAi() != null)
			getAi().shutdown();
		try {
			dispose();
			finalize();
		} catch (Throwable t) {
			System.out.println(t);
		}
		System.gc();
		windowList.remove(this);
		if (windowList.size() == 0) {
			System.exit(0);
		}
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void updateMenu() {
		reverseBoard.setSelected(visualBoard.reverseBoard());
		if (isConnected()) {
			menuItemOfferDraw.setEnabled(true);
			menuItemResign.setEnabled(true);
			menuItemSwitchSides.setEnabled(true);
			menuItemUploadGame.setEnabled(true);
			menuItemCloseConnection.setEnabled(true);
			menuItemConnect.setEnabled(false);
		} else {
			menuItemUploadGame.setEnabled(false);
			menuItemOfferDraw.setEnabled(false);
			menuItemResign.setEnabled(false);
			menuItemSwitchSides.setEnabled(false);
			menuItemCloseConnection.setEnabled(false);
			menuItemConnect.setEnabled(true);
		}
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void undoMove() {
		getHistory().removeLastMove();
		showMessage("Last move reversed.");
		// Take back two moves if playing against AI:
		if (((!isWhiteTurn() && blackPlayer == COMPUTER) || (isWhiteTurn() && whitePlayer == COMPUTER)) && getHistory().getFullMoveNumber() > 0) {
			getHistory().removeLastMove();
		}
		gameIsFinished = false;
		prepareMove();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void beep() {
		if (enableBeep.isSelected()) {
			getToolkit().beep();
		}
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void showMessage(String message) {
		chat.showMessage(message);
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void showMessage(String message, String style) {
		chat.showMessage(message, style);
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void showReplaceableMessage(String message) {
		chat.showReplaceableMessage(message);
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void showReplaceableMessage(String message, String style) {
		chat.showReplaceableMessage(message, style);
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void timeForfeit() {
		if (isConnected() && whitePlayer == HUMAN)
			protocol.sendPlayersTime(chessClock.getWhiteTime());
		if (isConnected() && blackPlayer == HUMAN)
			protocol.sendPlayersTime(chessClock.getBlackTime());
		checkFinish();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void showTimeoutSelector() {
		Object message[] = new Object[6];
		message[0] = "Select the time each player has to make his moves:";
		newWhiteTime = settings.whiteTime;
		newBlackTime = settings.blackTime;
		coupleSlidersCheckBox = new JCheckBox("Couple sliders");
		coupleSlidersCheckBox.setSelected(settings.whiteTime == settings.blackTime);
		whiteTimeoutLabel = new JLabel("White: " + newWhiteTime / 60000 + " min.");
		whiteSlider = new JSlider(1, 60, (int) (newWhiteTime / 60000));
		whiteSlider.setPreferredSize(new Dimension(300, 40));
		whiteSlider.setPaintTicks(true);
		whiteSlider.setMajorTickSpacing(10);
		whiteSlider.setMinorTickSpacing(5);
		whiteSlider.setPaintLabels(false);
		whiteSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				newWhiteTime = whiteSlider.getValue() * 60000L;
				whiteTimeoutLabel.setText("White: " + newWhiteTime / 60000 + " min.");
				if (coupleSlidersCheckBox.isSelected()) {
					newBlackTime = newWhiteTime;
					blackSlider.setValue((int) newBlackTime / 60000);
					blackTimeoutLabel.setText("Black: " + newBlackTime / 60000 + " min.");
				}
			}
		});
		blackTimeoutLabel = new JLabel("Black: " + newBlackTime / 60000 + " min.");
		blackSlider = new JSlider(1, 60, (int) (newBlackTime / 60000));
		blackSlider.setPreferredSize(new Dimension(300, 40));
		blackSlider.setPaintTicks(true);
		blackSlider.setMajorTickSpacing(10);
		blackSlider.setMinorTickSpacing(5);
		blackSlider.setPaintLabels(false);
		blackSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				newBlackTime = blackSlider.getValue() * 60000L;
				blackTimeoutLabel.setText("Black: " + newBlackTime / 60000 + " min.");
				if (coupleSlidersCheckBox.isSelected()) {
					newWhiteTime = newBlackTime;
					whiteSlider.setValue((int) newWhiteTime / 60000);
					whiteTimeoutLabel.setText("White: " + newWhiteTime / 60000 + " min.");
				}
			}
		});
		message[1] = whiteSlider;
		message[2] = whiteTimeoutLabel;
		message[3] = blackSlider;
		message[4] = blackTimeoutLabel;
		message[5] = coupleSlidersCheckBox;
		Object options[] = { "OK", "Cancel" };
		int result = JOptionPane.showOptionDialog(null, message, "Set game time", 0, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (result == 0) {
			if (isConnected()) {
				showMessage("Requesting clock change...");
				protocol.requestClockChange(newWhiteTime, newBlackTime);
			} else {
				settings.whiteTime = newWhiteTime;
				settings.blackTime = newBlackTime;
				chessClock.resetClocks();
				prepareMove();
			}
		}
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void setClocks(long whiteTime, long blackTime) {
		settings.whiteTime = whiteTime;
		settings.blackTime = blackTime;
		chessClock.resetClocks();
		showMessage("Game time was changed.");
		prepareMove();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void gotoGame(int newGameIndex) {
		if (currentGameIndex != -1 && getHistory().isChanged()) {
			// Save the current game back to the list first.
			gameTable.setPGN(currentGameIndex, getHistory().getPGNBody());
			fileIsChanged = true;
		}
		if (newGameIndex >= gameTable.getGameCount())
			newGameIndex = gameTable.getGameCount() - 1;
		int lastIndex = currentGameIndex;
		currentGameIndex = newGameIndex;
		gameTable.setSelectedIndex(currentGameIndex);
		gameTable.setCurrentGameIndex(currentGameIndex);
		gameTable.updateRow(lastIndex);
		try {
			getHistory().loadGame(gameTable.getPGN(currentGameIndex), gameTable.getSTR(currentGameIndex));
		} catch (VirtualBoard.ImpossibleMoveException e) {
			showMessage(e.toString());
		}
		infoPanel.setSTR(gameTable.getSTR(currentGameIndex));
		getHistory().gotoLast();
		update();
		prepareMove();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void update() {
		visualBoard.update();
		gameTable.repaint();
		updateMenu();
		updateNavigationButtons();
		notation.update();
		chessClock.updatePlayerSelectors();
	}

	/*
		 * 
	 	 */
	/*
		 */
	public boolean isFileChanged() {
		if (fileName == null || fileName.length() == 0)
			return false;
		if (fileIsChanged)
			return true;
		if (currentGameIndex != -1 && getHistory().isChanged()) {
			fileIsChanged = true;
			return true;
		}
		return false;
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void loadFile(java.io.File file) {
		fileChooserDir = file.getPath();
		String netPGN = null;
		PGN.STR netSTR = null;
		if (isConnected()) {
			netPGN = gameTable.getPGN(gameTable.getNetworkGameIndex());
			netSTR = gameTable.getSTR(gameTable.getNetworkGameIndex());
		}
		getHistory().clear();
		gameTable.clear();
		PGN.loadPGN(file, JChessBoardImpl.this);
		if (isConnected()) {
			gameTable.setNetworkGameIndex(gameTable.addGame(netPGN, netSTR));
		}
		chessClock.resetClocks();
		chessClock.stopClock();
		whitePlayer = HUMAN;
		blackPlayer = HUMAN;
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void askForSaving() {
		int choice = JOptionPane.showConfirmDialog(null, fileName + " was changed. Save file?", "Save?", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			save(fileName, false);
		}
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void save(String fileName, boolean append) {
		if (currentGameIndex != -1 && getHistory().isChanged()) {
			gameTable.setPGN(currentGameIndex, getHistory().getPGNBody());
		}
		try {
			PGN.writePGN(new File(fileName), gameTable, append);
			showMessage("Game(s) saved to " + fileName + ".");
			fileIsChanged = false;
		} catch (IOException o) {
			showMessage("IOException: " + o);
		}
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void saveAs() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File(fileChooserDir));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.resetChoosableFileFilters();
		fileChooser.addChoosableFileFilter(new PGNFileFilter());
		int choice = fileChooser.showSaveDialog(null);
		if (choice == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			fileChooserDir = file.getPath();
			fileName = file.getName();
			if (!fileName.endsWith(".pgn")) {
				fileName += ".pgn";
				file = new File(fileName);
			}
			setTitle(TITLE + " - " + fileName);
			if (file.exists()) {
				Object[] option = new Object[3];
				option[0] = "Append";
				option[1] = "Overwrite";
				option[2] = "Cancel";
				choice = JOptionPane.showOptionDialog(null, "File \"" + file.getName() + "\" exists.", "File exists", 0, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
			}
			if (choice == 0) { // Append
				save(file.getName(), true);
				fileIsChanged = false;
				showMessage("Reloading complete file...");
				loadFile(file);
			} else if (choice == 1) { // Overwrite
				save(file.getName(), false);
			} else
				// Cancel
				return;
		}
	}

	/*
		 * 
	 	 */
	/*
		 */
	public void uploadGame() {
		getHistory().gotoLast();
		List allBoards = getHistory().getAllBoards();
		protocol.startUpload();
		PGN.STR str = gameTable.getSTR(gameTable.getCurrentGameIndex());
		if (str.hasTag("SetUp"))
			protocol.initFromFEN(str.getTag("FEN"));
		if (allBoards.size() > 0) {
			for (int n = allBoards.size() - 1; n >= 0; n--) {
				Move move = ((VirtualBoard) allBoards.get(n)).getLastMove();
				if (move != null)
					protocol.sendBatchMove(move);
			}
		}
		protocol.uploadDone();
		whitePlayer = UNKNOWN;
		blackPlayer = UNKNOWN;
		showMessage("Upload finished");
		gameTable.setNetworkGameIndex(gameTable.getCurrentGameIndex());
		chessClock.stopClock();
		chessClock.resetClocks();
		update();
		prepareMove();
	}

	/**
	 * The JChessBoard constructor.
	 * @param title The title of the created JFrame.
	 */
	public JChessBoardImpl(String title) {
		super(title);
		settings = new Settings(settingsFileName);
		boolean settingsHaveBeenLoaded = settings.read();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		windowList.add(this);

		JSplitPane splitPane;
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		//
		// GameTable
		//
		gameTable = new GameTable();
		// gameTable.addGame("", new PGN.STR());

		final JPopupMenu gameTableContextMenu = new JPopupMenu();
		JMenuItem removeGameItem = new JMenuItem("Remove game(s)");
		removeGameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gotoGame(gameTable.removeSelectedGames());
				fileIsChanged = true;
			}
		});
		gameTableContextMenu.add(removeGameItem);

		gameTable.getJTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = gameTable.getSelectedIndex();
					gotoGame(index);
				}
			}

			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					gameTableContextMenu.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					gameTableContextMenu.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}
		});

		KeyStroke enter = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0);
		gameTable.getJTable().unregisterKeyboardAction(enter);
		gameTable.getJTable().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				synchronized (this) {
					gotoGame(gameTable.getSelectedIndex());
				}
			}
		}, enter, JComponent.WHEN_FOCUSED);
		KeyStroke del = KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0);
		gameTable.getJTable().unregisterKeyboardAction(del);
		gameTable.getJTable().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				gotoGame(gameTable.removeSelectedGames());
				fileIsChanged = true;
			}
		}, del, JComponent.WHEN_FOCUSED);

		splitPane.setTopComponent(gameTable);
		tabbedPane = new JTabbedPane();

		history = new History(this);
		infoPanel = new InfoPanel();
		notation = new Notation(getHistory());

		tabbedPane.add("Notation", notation);
		tabbedPane.add("Tree", getHistory());
		tabbedPane.add("Info", infoPanel);

		splitPane.setBottomComponent(tabbedPane);

		visualBoard = new VisualBoard(this);
		connectionIndicator = new ConnectionIndicator();
		chat = new Chat(this);
		boardConnector = null;

		if (connectionListener == null)
			connectionListener = new ConnectionListener();
		connectionListener.setPort(settings.networkPort);
		connectionListener.setEnabled(settings.enableServer);
		ai = null; // Because of memory consumption, the AI
		// will be started on demand.
		chessClock = new ChessClock(this);
		chessClock.setEnabled(settings.enableClock);
		fileChooserDir = System.getProperty("user.dir");

		// Button pane:
		buttonPanel = new JPanel(new GridLayout(1, 5));
		java.net.URL url = JChessBoardImpl.class.getResource("/chess/jchessboard/images/start.gif");
		ImageIcon icon = url != null ? new ImageIcon(url) : new ImageIcon("null");
		buttonStart = new JButton(icon);
		buttonStart.setContentAreaFilled(false);
		buttonStart.setMargin(new Insets(2, 2, 2, 2));
		buttonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getHistory().gotoFirst();
			}
		});
		buttonPanel.add(buttonStart);

		url = JChessBoardImpl.class.getResource("/chess/jchessboard/images/prev.gif");
		icon = url != null ? new ImageIcon(url) : new ImageIcon("null");
		buttonPrevious = new JButton(icon);
		buttonPrevious.setContentAreaFilled(false);
		buttonPrevious.setMargin(new Insets(2, 2, 2, 2));
		buttonPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getHistory().prev();
			}
		});
		buttonPanel.add(buttonPrevious);

		url = JChessBoardImpl.class.getResource("/chess/jchessboard/images/next.gif");
		icon = url != null ? new ImageIcon(url) : new ImageIcon("null");
		buttonNext = new JButton(icon);
		buttonNext.setContentAreaFilled(false);
		buttonNext.setMargin(new Insets(2, 2, 2, 2));
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getHistory().next();
			}
		});
		buttonPanel.add(buttonNext);

		url = JChessBoardImpl.class.getResource("/chess/jchessboard/images/end.gif");
		icon = url != null ? new ImageIcon(url) : new ImageIcon("null");
		buttonEnd = new JButton(icon);
		buttonEnd.setContentAreaFilled(false);
		buttonEnd.setMargin(new Insets(2, 2, 2, 2));
		buttonEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getHistory().gotoLast();
			}
		});
		buttonPanel.add(buttonEnd);

		Container contentPane = getContentPane();
		GridBagLayout gridBag = new GridBagLayout();
		contentPane.setLayout(gridBag);

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridBag.setConstraints(chessClock, c);
		contentPane.add(chessClock);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		gridBag.setConstraints(visualBoard, c);
		contentPane.add(visualBoard);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridBag.setConstraints(buttonPanel, c);
		contentPane.add(buttonPanel);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 3;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		gridBag.setConstraints(splitPane, c);
		splitPane.setPreferredSize(new Dimension(210, 1));
		splitPane.setMinimumSize(new Dimension(210, 1));
		contentPane.add(splitPane);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weightx = 0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		gridBag.setConstraints(chat, c);
		contentPane.add(chat);

		showMessage("Welcome to " + TITLE + "!");
		chat.showMessagePart("Enter ");
		chat.showActionMessagePart("/help", "/help");
		chat.showMessagePart(" for a list of available commands.\n");
		if (settingsHaveBeenLoaded)
			showMessage("Settings have been loaded from " + settingsFileName + ".");

		//
		// Create the menus:
		// 
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu gameMenu = new JMenu("Game");
		JMenu moveMenu = new JMenu("Move");
		JMenu settingsMenu = new JMenu("Settings");
		JMenu aboutMenu = new JMenu("About");
		menuBar.add(gameMenu);
		menuBar.add(moveMenu);
		menuBar.add(settingsMenu);
		menuBar.add(aboutMenu);

		JMenuItem menuItemNewWindow = new JMenuItem("New window");
		menuItemNewWindow.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JChessBoardImpl jcb = new JChessBoardImpl(TITLE);
			}
		});
		gameMenu.add(menuItemNewWindow);

		menuItemNewGame = new JMenuItem("New game");
		menuItemNewGame.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (isConnected()) {
					showMessage("Requesting new game...");
					protocol.requestNewGame();
				} else {
					newGame();
					showMessage("New game.");
				}
			}
		});
		gameMenu.add(menuItemNewGame);

		menuItemLoadFile = new JMenuItem("Load file...");
		menuItemLoadFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (isFileChanged())
					askForSaving();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new java.io.File(fileChooserDir));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.resetChoosableFileFilters();
				fileChooser.addChoosableFileFilter(new PGNFileFilter());
				int choice = fileChooser.showOpenDialog(null);
				if (choice == JFileChooser.APPROVE_OPTION) {
					loadFile(fileChooser.getSelectedFile());
					fileName = fileChooser.getSelectedFile().getName();
					setTitle(TITLE + " - " + fileName);
					fileIsChanged = false;
				}
			}
		});
		gameMenu.add(menuItemLoadFile);

		menuItemSave = new JMenuItem("Save");
		menuItemSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (fileName != null && fileName.length() > 0) {
					save(fileName, false);
				} else {
					saveAs();
				}
			}
		});
		gameMenu.add(menuItemSave);

		menuItemSaveAs = new JMenuItem("Save as...");
		menuItemSaveAs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveAs();
			}
		});
		gameMenu.add(menuItemSaveAs);

		JMenuItem menuItemOpenBoardEditor = new JMenuItem("Open board editor...");
		menuItemOpenBoardEditor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				new BoardEditor(JChessBoardImpl.this);
			}
		});
		gameMenu.add(menuItemOpenBoardEditor);

		gameMenu.addSeparator();

		menuItemConnect = new JMenuItem("Connect...");
		menuItemConnect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Object message[] = new Object[4];
				message[0] = "Hostname: ";
				JTextField hostField = new JTextField(otherHostname, 30);
				message[1] = hostField;
				message[2] = "Port: ";
				JTextField portField = new JTextField("" + settings.networkPort, 5);
				message[3] = portField;
				Object options[] = { "OK", "Cancel" };
				int choice = JOptionPane.showOptionDialog(null, message, "Connect...", 0, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (choice == 0) {
					try {
						otherHostname = hostField.getText();
						int tmpPort = Integer.parseInt(portField.getText());
						if (tmpPort > 65535)
							throw (new NumberFormatException());
						settings.networkPort = tmpPort;
						boardConnector = new BoardConnector(JChessBoardImpl.this, otherHostname, settings.networkPort);
						whitePlayer = UNKNOWN;
						blackPlayer = UNKNOWN;
						protocol.connecting();
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Illegal port: " + portField.getText(), "Illegal port", JOptionPane.ERROR_MESSAGE);
					}
				}
				updateMenu();
			}
		});
		gameMenu.add(menuItemConnect);

		menuItemCloseConnection = new JMenuItem("Close connection");
		menuItemCloseConnection.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Close connection?", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					boardConnector.closeConnection();
					connectionClosed();
				}
			}
		});
		gameMenu.add(menuItemCloseConnection);
		menuItemCloseConnection.setEnabled(false);

		menuItemOfferDraw = new JMenuItem("Offer draw");
		menuItemOfferDraw.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (isConnected()) {
					int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Offer draw?", JOptionPane.YES_NO_OPTION);
					if (choice == JOptionPane.YES_OPTION) {
						showMessage("Offering draw...");
						protocol.offerDraw();
					}
				}
			}
		});
		gameMenu.add(menuItemOfferDraw);
		menuItemOfferDraw.setEnabled(false);

		menuItemResign = new JMenuItem("Resign");
		menuItemResign.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (isConnected()) {
					int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Resign?", JOptionPane.YES_NO_OPTION);
					if (choice == JOptionPane.YES_OPTION) {
						showMessage("Resigned.");
						// stopGame();
						protocol.resigned();
						if (whitePlayer == HUMAN)
							getHistory().setResult("0-1");
						else if (blackPlayer == HUMAN)
							getHistory().setResult("1-0");
					}
				}
			}
		});
		gameMenu.add(menuItemResign);
		menuItemResign.setEnabled(false);

		menuItemSwitchSides = new JMenuItem("Switch sides");
		menuItemSwitchSides.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (isConnected()) {
					int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Switch sides?", JOptionPane.YES_NO_OPTION);
					if (choice == JOptionPane.YES_OPTION) {
						showMessage("Requesting side switch...");
						protocol.requestSideSwitch();
					}
				}
			}
		});
		gameMenu.add(menuItemSwitchSides);

		menuItemUploadGame = new JMenuItem("Upload this game to peer");
		menuItemUploadGame.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (isConnected()) {
					showMessage("Asking opponent...");
					protocol.requestUpload();
				}
			}
		});
		gameMenu.add(menuItemUploadGame);

		gameMenu.addSeparator();

		JMenuItem item2 = new JMenuItem("Exit");
		item2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exit();
			}
		});
		gameMenu.add(item2);

		undoMove = new JMenuItem("Take back last move");
		undoMove.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (isConnected()) {
					showMessage("Requesting undo...");
					protocol.requestUndo();
				} else
					undoMove();
			}
		});
		moveMenu.add(undoMove);

		JMenuItem editComment = new JMenuItem("Edit comment");
		editComment.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				getHistory().editComment();
				JChessBoardImpl.this.update();
			}
		});
		moveMenu.add(editComment);

		clockCheckbox = new JCheckBoxMenuItem("Clock");
		clockCheckbox.setSelected(settings.enableClock);
		clockCheckbox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (isConnected()) {
					clockCheckbox.setSelected(!clockCheckbox.isSelected());
					// Reverse what Swing has done until accepted by peer.
					showMessage("Asking opponent...");
					protocol.requestToggleClock();
				} else {
					setEnableClock(clockCheckbox.isSelected());
				}
			}
		});
		settingsMenu.add(clockCheckbox);

		JMenuItem gameTimeItem = new JMenuItem("Set time...");
		gameTimeItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showTimeoutSelector();
			}
		});
		settingsMenu.add(gameTimeItem);

		settingsMenu.addSeparator();

		enableBeep = new JCheckBoxMenuItem("Beep");
		enableBeep.setSelected(settings.enableBeep);
		enableBeep.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				settings.enableBeep = enableBeep.isSelected();
			}
		});
		settingsMenu.add(enableBeep);

		reverseBoard = new JCheckBoxMenuItem("Reverse board");
		reverseBoard.setSelected(false);
		reverseBoard.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				visualBoard.setReverseBoard(reverseBoard.isSelected());
			}
		});
		settingsMenu.add(reverseBoard);

		showPossibleMoves = new JCheckBoxMenuItem("Show possible moves");
		showPossibleMoves.setSelected(settings.showPossibleMoves);
		showPossibleMoves.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				settings.showPossibleMoves = showPossibleMoves.isSelected();
			}
		});
		settingsMenu.add(showPossibleMoves);

		settingsMenu.addSeparator();

		final JMenuItem aiLevelEasy = new JRadioButtonMenuItem("AI level: Easy");
		final JMenuItem aiLevelMedium = new JRadioButtonMenuItem("AI level: Medium");
		final JMenuItem aiLevelBest = new JRadioButtonMenuItem("AI level: Best");

		aiLevelEasy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				settings.aiLevel = AI.EASY;
				getAi().setLevel(settings.aiLevel);
				aiLevelEasy.setSelected(true);
				aiLevelMedium.setSelected(false);
				aiLevelBest.setSelected(false);
			}
		});
		aiLevelMedium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				settings.aiLevel = AI.MEDIUM;
				getAi().setLevel(settings.aiLevel);
				aiLevelEasy.setSelected(false);
				aiLevelMedium.setSelected(true);
				aiLevelBest.setSelected(false);
			}
		});
		aiLevelBest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				settings.aiLevel = AI.BEST;
				getAi().setLevel(settings.aiLevel);
				aiLevelEasy.setSelected(false);
				aiLevelMedium.setSelected(false);
				aiLevelBest.setSelected(true);
			}
		});
		settingsMenu.add(aiLevelEasy);
		settingsMenu.add(aiLevelMedium);
		settingsMenu.add(aiLevelBest);

		aiLevelEasy.setSelected(settings.aiLevel == AI.EASY);
		aiLevelMedium.setSelected(settings.aiLevel == AI.MEDIUM);
		aiLevelBest.setSelected(settings.aiLevel == AI.BEST);

		settingsMenu.addSeparator();

		JMenuItem serverSettingsItem = new JMenuItem("Server settings...");
		serverSettingsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				connectionListener.showSettingsPane();
				settings.enableServer = connectionListener.isEnabled();
				settings.networkPort = connectionListener.getPort();
			}
		});
		settingsMenu.add(serverSettingsItem);

		settingsMenu.addSeparator();

		JMenuItem saveSettingsItem = new JMenuItem("Save settings");
		saveSettingsItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Dimension size = JChessBoardImpl.this.getSize();
				settings.windowWidth = (int) size.getWidth();
				settings.windowHeight = (int) size.getHeight();
				try {
					settings.save();
					showMessage("Settings saved to " + settingsFileName + ".");
				} catch (IOException e) {
					showMessage("IOException saving settings: " + e);
				}
			}
		});
		settingsMenu.add(saveSettingsItem);

		JMenuItem licensingItem = new JMenuItem("Licensing");
		licensingItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JOptionPane.showMessageDialog(null, COPYRIGHT_MESSAGE, "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		aboutMenu.add(licensingItem);

		JMenuItem versionsItem = new JMenuItem("Versions");
		versionsItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JOptionPane.showMessageDialog(null, AI.getVersion() + "\n" + BoardConnector.getVersion() + "\n" + Chat.getVersion() + "\n" + ChessClock.getVersion() + "\n"
						+ ConnectionListener.getVersion() + "\n" + ConnectionIndicator.getVersion() + "\n" + History.getVersion() + "\n" + JChessBoardImpl.getVersion() + "\n" + Move.getVersion()
						+ "\n" + PGN.getVersion() + "\n" + VirtualBoard.getVersion() + "\n" + VisualBoard.getVersion(), "Version Information", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		aboutMenu.add(versionsItem);

		updateMenu();
		updateNavigationButtons();
		pack();
		setSize(settings.windowWidth, settings.windowHeight);
		// setResizable(false);
		setVisible(true);

		addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				int width = getWidth();
				int height = getHeight();
				if (width < MIN_WIDTH || height < MIN_HEIGHT) {
					width = (width < MIN_WIDTH) ? MIN_WIDTH : width;
					height = (height < MIN_HEIGHT) ? MIN_HEIGHT : height;
					JChessBoardImpl.this.setSize(width, height);
				}
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentShown(ComponentEvent e) {
			}

			public void componentHidden(ComponentEvent e) {
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		fileIsChanged = false;
		newGame();
		ai = new AI(JChessBoardImpl.this, isThreaded);
		getAi().setLevel(settings.aiLevel);
	}

	public ConnectionIndicator getConnectionIndicator() {
		return connectionIndicator;

	}

	public ChessClock getChessClock() {
		return chessClock;

	}

	public VisualBoard getVisualBoard() {
		return visualBoard;
	}

	public void setVisualBoard(VisualBoard visualBoard) {
		this.visualBoard = visualBoard;
	}

	public String getWhitePlayer() {
		return whitePlayer;
	}

	public void setWhitePlayer(String whitePlayer) {
		this.whitePlayer = whitePlayer;
	}

	public String getBlackPlayer() {
		return blackPlayer;
	}

	public void setBlackPlayer(String blackPlayer) {
		this.blackPlayer = blackPlayer;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public JSlider getWhiteSlider() {
		return whiteSlider;
	}

	public void setWhiteSlider(JSlider whiteSlider) {
		this.whiteSlider = whiteSlider;
	}

	public JSlider getBlackSlider() {
		return blackSlider;
	}

	public void setBlackSlider(JSlider blackSlider) {
		this.blackSlider = blackSlider;
	}

	public long getNewWhiteTime() {
		return newWhiteTime;
	}

	public void setNewWhiteTime(long newWhiteTime) {
		this.newWhiteTime = newWhiteTime;
	}

	public long getNewBlackTime() {
		return newBlackTime;
	}

	public void setNewBlackTime(long newBlackTime) {
		this.newBlackTime = newBlackTime;
	}

	public JLabel getWhiteTimeoutLabel() {
		return whiteTimeoutLabel;
	}

	public void setWhiteTimeoutLabel(JLabel whiteTimeoutLabel) {
		this.whiteTimeoutLabel = whiteTimeoutLabel;
	}

	public JLabel getBlackTimeoutLabel() {
		return blackTimeoutLabel;
	}

	public void setBlackTimeoutLabel(JLabel blackTimeoutLabel) {
		this.blackTimeoutLabel = blackTimeoutLabel;
	}

	public boolean isCoupleSliders() {
		return coupleSliders;
	}

	public void setCoupleSliders(boolean coupleSliders) {
		this.coupleSliders = coupleSliders;
	}

	public JCheckBox getCoupleSlidersCheckBox() {
		return coupleSlidersCheckBox;
	}

	public void setCoupleSlidersCheckBox(JCheckBox coupleSlidersCheckBox) {
		this.coupleSlidersCheckBox = coupleSlidersCheckBox;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public GameTable getGameTable() {
		return gameTable;
	}

	public void setGameTable(GameTable gameTable) {
		this.gameTable = gameTable;
	}

	public int getCurrentGameIndex() {
		return currentGameIndex;
	}

	public void setCurrentGameIndex(int currentGameIndex) {
		this.currentGameIndex = currentGameIndex;
	}

	public void setConnectionIndicator(ConnectionIndicator connectionIndicator) {
		this.connectionIndicator = connectionIndicator;
	}

	public static String getVersion() {
		return "$Id: JChessBoard.java 7 2009-11-15 18:58:42Z cdivossen $";
	}

	static class PGNFileFilter extends javax.swing.filechooser.FileFilter {
		public String getDescription() {
			return "PGN Files (*.pgn)";
		}

		public boolean accept(java.io.File file) {
			return file.exists() && file.canRead() && (file.getName().endsWith(".pgn") || file.isDirectory());
		}
	}

	public BoardConnector getBoardConnector() {
		return boardConnector;
	}

	public void setBoardConnector(BoardConnector connector) {
		boardConnector = connector;
	}

	public boolean isConnected() {
		return (protocol.isConnected() && (boardConnector != null));
	}

	public void setEnableClock(boolean enable) {
		clockCheckbox.setSelected(enable);
		chessClock.setEnabled(enable);
		settings.enableClock = enable;
	}

	public boolean getEnableClock() {
		return clockCheckbox.isSelected();
	}

	// main
	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JChessBoard jcb = new JChessBoardImpl(TITLE);
				if (args.length > 0) {
					jcb.loadFile(new java.io.File(args[0]));
				}
			}
		});
	}

}
