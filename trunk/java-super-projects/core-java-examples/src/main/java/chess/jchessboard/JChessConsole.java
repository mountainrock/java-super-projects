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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

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

public class JChessConsole implements JChessBoard {
	static String title = "JChessBoard v1.5.1";
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
	boolean isThreaded=false;
	ConnectionIndicator connectionIndicator;
	BoardConnector boardConnector;
	AI ai;
	ChessClock chessClock;
	Chat chat;
	String whitePlayer = HUMAN;
	String blackPlayer = COMPUTER;
	History history;
	static java.util.List windowList = new Vector();

	// ConnectionListener is the connection server
	private static ConnectionListener connectionListener;
	private String otherHostname = "";
	private static String fileChooserDir;
	private String fileName = "";
	private boolean fileIsChanged = false;
	private final static String settingsFileName = "JChessBoard.conf";
	Settings settings;
	private boolean gameIsFinished = false;
	// Reguired for the timeout selector:
	long newWhiteTime, newBlackTime;
	boolean coupleSliders;
	Protocol protocol = new Protocol(this);
	int currentGameIndex = 0;
	private Notation notation;

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

	/**
	 * @return
	 */
	public BoardConnector getBoardConnector() {
		return boardConnector;
	}

	public GameTable getGameTable() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setGameTable(GameTable gameTable) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param connector
	 */
	public void setBoardConnector(BoardConnector connector) {
		boardConnector = connector;
	}

	/**
	 * Test whether this board is currently connected.
	 */
	public boolean isConnected() {
		return (protocol.isConnected() && (boardConnector != null));
	}

	public void setEnableClock(boolean enable) {
		chessClock.setEnabled(enable);
		settings.enableClock = enable;
	}

	public boolean getEnableClock() {
		return settings.enableClock;
	}

	public void connectionClosed() {
		// Protocol stuff
		protocol.connectionClosed();
		// Visual stuff
		chessClock.stopClock();
		connectionIndicator.setNoConnection();
		showMessage("Connection closed.");
		setEnableClock(false);
		if (fileName != null && fileName.length() > 0)
			setTitle(title + " - " + fileName);
		else
			setTitle(title);
		whitePlayer = HUMAN;
		blackPlayer = HUMAN;
		boardConnector = null;
		prepareMove();
		// --JAM: Switch the board around. If Black is disconnected at the
		// beginning of the game, the board is still oriented for Black.
		// It's disconcerting.
	}

	private void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Performs all the steps necessary to begin playing a connected game. This includes setting the chess clocks, clearing the connection indicator, starting a new game, resetting the title, and
	 * setting variables to allow the first mover to be White.
	 */
	public void connectionEstablished() {
		protocol.connectionEstablished();
		connectionIndicator.setReady();
		setEnableClock(true);
		settings.whiteTime = 600000;
		settings.blackTime = 600000;
		newGame();
		protocol.sendPlayerName(settings.userName);
		if (fileName != null && fileName.length() > 0)
			setTitle(title + " - Connected with \"" + otherHostname + "\" - " + fileName);
		else
			setTitle(title + " - Connected with \"" + otherHostname + "\"");
		showMessage("Connected.");
		showMessage("Your are now playing with \"" + otherHostname + "\".");
		showMessage("Either player can make a move. The first move decides who plays which side.");
		showMessage("You can set your chat name with /name <your name>");
		prepareMove();
	}

	public void updateNavigationButtons() {
		// Activate/deactivate navigation buttons:
		boolean hasPrev;
		if (history.getPreviousGameNode() == history.getCurrentGameNode()) {
			hasPrev = false;
		} else {
			hasPrev = true;
		}
	}

	public VirtualBoard getCurrentVirtualBoard() {
		return history.getCurrentBoard();
	}

	public boolean isWhiteTurn() {
		return history.getCurrentBoard().isWhiteTurn();
	}

	/**
	 * Executes the move your opponent has done. This is an interface method used by Protocol.
	 */
	public void makePeersMove(Move move) {
		chessClock.stopClock();
		history.gotoLast();
		// The first move decides!
		if (whitePlayer.equals(UNKNOWN) || blackPlayer == UNKNOWN) {
			if (isWhiteTurn()) {
				whitePlayer = PEER;
				blackPlayer = HUMAN;
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
			history.addMove(algMove, virtualBoard);
			chessClock.startClock();
			checkFinish();
		} catch (VirtualBoard.ImpossibleMoveException e) {
			showMessage("Error parsing opponents move: " + move);
			protocol.sendError("Error parsing move: " + move);
		}
		prepareMove();
	}

	public void makeBatchMove(Move move) {
		VirtualBoard virtualBoard = getCurrentVirtualBoard().clonedBoard();
		String algMove = virtualBoard.algebraic(move);
		try {
			virtualBoard.makeMove(move);
		} catch (VirtualBoard.ImpossibleMoveException e) {
			showMessage("Error parsing batch move: " + move);
		}
		history.addMove(algMove, virtualBoard);
	}

	/**
	 * Executes the move of the AI. This is an interface method for the AI.
	 */
	public void makeAIsMove(Move move) {
		chessClock.stopClock();
		VirtualBoard virtualBoard = getCurrentVirtualBoard().clonedBoard();
		String algMove = virtualBoard.algebraic(move);
		showMessage("AI's move: " + algMove);
		try {
			virtualBoard.makeMove(move);
			history.addMove(algMove, virtualBoard);
			chessClock.startClock();
			triggerAI();
			checkFinish();
		} catch (VirtualBoard.ImpossibleMoveException e) {
		}
	}

	/**
	 * Executes the users move. This is an interface method for VisualBoard.
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
		history.addMove(algMove, virtualBoard);
		if (isConnected()) {
			protocol.sendMove(move);
			// The first move decides!
			if (whitePlayer.equals(UNKNOWN) || blackPlayer == UNKNOWN) {
				if (isWhiteTurn()) {
					whitePlayer = PEER;
					blackPlayer = HUMAN;
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

	public void triggerAI() {
		if (!(whitePlayer == COMPUTER) && !(blackPlayer == COMPUTER))
			return;
		if (((whitePlayer == COMPUTER) || (blackPlayer == COMPUTER)) && ai == null) {
			
			ai = new AI(this,isThreaded);
		}
		if (ai != null && !(whitePlayer == COMPUTER) && !(blackPlayer == COMPUTER)) {
			ai.shutdown();
			ai = null;
			return;
		}
		if (isWhiteTurn()) {
			if (whitePlayer == COMPUTER) {
				long timeout;
				if (getClockSelected()) {
					if (history.getFullMoveNumber() < 40)
						timeout = chessClock.getWhiteTime() / ((long) (60 - history.getFullMoveNumber()));
					else
						timeout = chessClock.getWhiteTime() / 20L;
					if (timeout < 500) // Minimum time: 500ms.
						timeout = 500;
				} else
					timeout = 5000; // Fixed time, 5 sec.
				ai.prepareMove(timeout);
			}
		} else {
			if (blackPlayer == COMPUTER) {
				long timeout;
				if (getClockSelected()) {
					if (history.getFullMoveNumber() < 40)
						timeout = chessClock.getBlackTime() / ((long) (60 - history.getFullMoveNumber()));
					else
						timeout = chessClock.getBlackTime() / 20;
					if (timeout < 500) // Minimum time: 500ms.
						timeout = 500;
				} else
					timeout = 5000; // Fixed time, 5 sec.
				ai.prepareMove(timeout);
			}
		}
	}

	private boolean getClockSelected() {
		return true;
	}

	/**
	 * Pepares some settings for the next move. Mainly, it starts and stops the chess clocks.
	 */
	public void prepareMove() {
		chessClock.updatePlayerSelectors();
	}

	public void switchSides() {
		String tmp = whitePlayer;
		whitePlayer = blackPlayer;
		blackPlayer = tmp;
		updateSTR();
		showMessage("Sides switched.");
		prepareMove();
	}

	private boolean getReverseBoard(){
		return false;//TODO:
	}

	public void updateSTR() {
	}

	/*
	 * Stops the game and creates the appropriate message if the game comes out to be finished.
	 */
	public void checkFinish() {
		VirtualBoard virtualBoard = history.getCurrentBoard();
		if (virtualBoard.gameIsFinished()) {
			if (virtualBoard.onlyKingsLeft()) {
				showMessage("Draw!");
			} else if (virtualBoard.isWhiteTurn()) {
				if (virtualBoard.isAttackedByBlack(virtualBoard.getWhiteKingPos())) {
					Object[] options = { "Accept", "Reject" };
					showMessage("Black wins!");
				} else {
					showMessage("Draw (stalemate)!");
				}
			} else {
				if (virtualBoard.isAttackedByWhite(virtualBoard.getBlackKingPos())) {
					showMessage("White wins!");
				} else {
					showMessage("Draw (stalemate)!");
				}
			}
			chessClock.stopClock();
		} else if (getClockSelected() && (chessClock.getWhiteTime() <= 0 || chessClock.getBlackTime() <= 0)) {
			chessClock.stopClock();
			if (chessClock.getWhiteTime() <= 0) {
				showMessage("Black wins (time forfeit)!");
			} else {
				showMessage("White wins (time forfeit)!");
			}
			showMessage("White: " + chessClock.formatTime(chessClock.getWhiteTime()) + ", Black: " + chessClock.formatTime(chessClock.getBlackTime()));
			setEnableClock(false);
			showMessage("The clocks have been disabled, you can continue playing, if you want to.");
		} else
			prepareMove();
	}

	public void newGame() {
		PGN.STR str = new PGN.STR();
		if (isConnected()) {
			whitePlayer = UNKNOWN;
			blackPlayer = UNKNOWN;
		} else {
			whitePlayer = HUMAN;
			blackPlayer = COMPUTER;
		}
		Calendar cal = Calendar.getInstance();
		str.setTag("Date", cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH));
		chessClock.updatePlayerSelectors();
		updateSTR();
		if (ai != null)
			ai.newGame();
		chessClock.stopClock();
		chessClock.resetClocks();
		prepareMove();
		// history.setResult("*");
		gameIsFinished = false;
		fileIsChanged = true;
	}

	public void newGame(VirtualBoard vb) {
		newGame();
		history.setBoard(vb.clonedBoard());
		update();
	}

	/**
	 * Removes this window and whats used by it and calls System.exit() if there is no window left.
	 */
	public void exit() {
		if (isConnected()) {
				boardConnector.closeConnection();
				connectionClosed();
		}
		if (isFileChanged())
			askForSaving();
		if (ai != null)
			ai.shutdown();
		try {
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

	/**
	 * Enables/disables certain menu entries depending on the current connection state. The checkbox menu items are also updated.
	 */
	public void updateMenu() {
	}

	/**
	 * Takes back the last move. (What did you expect?)
	 */
	public void undoMove() {
		history.removeLastMove();
		showMessage("Last move reversed.");
		// Take back two moves if playing against AI:
		if (((!isWhiteTurn() && blackPlayer == COMPUTER) || (isWhiteTurn() && whitePlayer == COMPUTER)) && history.getFullMoveNumber() > 0) {
			history.removeLastMove();
		}
		gameIsFinished = false;
		prepareMove();
	}


	public void showMessage(String message) {
		chat.showMessage(message);
	}

	public void showMessage(String message, String style) {
		chat.showMessage(message, style);
	}

	public void showReplaceableMessage(String message) {
		chat.showReplaceableMessage(message);
	}

	public void showReplaceableMessage(String message, String style) {
		chat.showReplaceableMessage(message, style);
	}

	public void timeForfeit() {
		if (isConnected() && whitePlayer == HUMAN)
			protocol.sendPlayersTime(chessClock.getWhiteTime());
		if (isConnected() && blackPlayer == HUMAN)
			protocol.sendPlayersTime(chessClock.getBlackTime());
		checkFinish();
	}

	/**
	 * Creates a requester that allows to select time each player has to make moves. If the board is connected, a confirmation request for this is send via the BoardConnector. In that case, the clock
	 * will not yet be changed when this method returns.
	 */
	public void showTimeoutSelector() {
		Object message[] = new Object[6];
		message[0] = "Select the time each player has to make his moves:";
		newWhiteTime = settings.whiteTime;
		newBlackTime = settings.blackTime;

		
	}

	public void setClocks(long whiteTime, long blackTime) {
		settings.whiteTime = whiteTime;
		settings.blackTime = blackTime;
		chessClock.resetClocks();
		showMessage("Game time was changed.");
		prepareMove();
	}

	public void gotoGame(int newGameIndex) {
		if (currentGameIndex != -1 && history.isChanged()) {
			// Save the current game back to the list first.
			fileIsChanged = true;
		}
		int lastIndex = currentGameIndex;
		currentGameIndex = newGameIndex;
		history.gotoLast();
		update();
		prepareMove();
	}

	public void update() {
		chessClock.updatePlayerSelectors();
	}

	public boolean isFileChanged() {
		if (fileName == null || fileName.length() == 0)
			return false;
		if (fileIsChanged)
			return true;
		if (currentGameIndex != -1 && history.isChanged()) {
			fileIsChanged = true;
			return true;
		}
		return false;
	}


	public void askForSaving() {
			save(fileName, false);
	}

	public void save(String fileName, boolean append) {
	}


	public void uploadGame() {
	}

	/**
	 * The JChessBoard constructor.
	 * @param title The title of the created JFrame.
	 */
	public JChessConsole(String title) {
		this.title = title;
		settings = new Settings(settingsFileName);
		
		boolean settingsHaveBeenLoaded = settings.read();
		windowList.add(this);

		// GameTable

		connectionIndicator = new ConnectionIndicator();
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
		chat = new Chat(this);

		showMessage("Welcome to " + title + "!");
		chat.showMessagePart("Enter ");
		chat.showActionMessagePart("/help", "/help");
		chat.showMessagePart(" for a list of available commands.\n");
		if (settingsHaveBeenLoaded)
			showMessage("Settings have been loaded from " + settingsFileName + ".");

		fileIsChanged = false;
		history = new History(this);
		newGame();
		ai = new AI(this,isThreaded);
		ai.setLevel(settings.aiLevel);
	}

	public ConnectionIndicator getConnectionIndicator() {
		return connectionIndicator;

	}

	public ChessClock getChessClock() {
		return chessClock;

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

	public boolean isCoupleSliders() {
		return coupleSliders;
	}

	public void setCoupleSliders(boolean coupleSliders) {
		this.coupleSliders = coupleSliders;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
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

	
	// main
	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JChessConsole jcb = new JChessConsole(title);
				if (args.length > 0) {
					jcb.loadFile(new java.io.File(args[0]));
				}
			}
		});
	}

	public AI getAi() {
		return ai;
	}

	public History getHistory() {
		return history;
	}

	public void loadFile(File file) {
	}

	public void saveAs() {
	}
}
