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

/* $Id: Protocol.java 5 2009-11-10 07:56:47Z cdivossen $ */

package chess.jchessboard;

import java.util.StringTokenizer;

import javax.swing.JOptionPane;

/**
 * @author cd
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Protocol {
	public final static String MOVE_MESSAGE = "move";
	public final static String BATCH_MOVE_MESSAGE = "batch_move";
	public final static String BATCH_FINISHED_MESSAGE = "batch_finished";
	public final static String BATCH_CANCELED_MESSAGE = "batch_canceled";
	public final static String START_UPLOAD_MESSAGE = "start_upload";
	public final static String NEW_GAME_MESSAGE = "new_game";
	public final static String REQUEST_NEW_GAME_MESSAGE = "request_new";
	public final static String NEW_GAME_REJECTED_MESSAGE = "new_rejected";
	public final static String UNDO_MESSAGE = "undo";
	public final static String UNDO_REJECTED_MESSAGE = "undo_rejected";
	public final static String GAME_TIME_MESSAGE = "gametime";
	public final static String PLAYERS_TIME_MESSAGE = "playerstime";
	public final static String REQUEST_UNDO_MESSAGE = "request_undo";
	public final static String REQUEST_CLOCK_CHANGE_MESSAGE = "request_clock_change";
	public final static String CLOCK_CHANGE_REJECTED_MESSAGE = "clock_change_rejected";
	public final static String REQUEST_TOGGLE_CLOCK_MESSAGE = "request_enable_clock";
	public final static String REJECT_TOGGLE_CLOCK_MESSAGE = "reject_enable_clock";
	public final static String ACCEPT_TOGGLE_CLOCK_MESSAGE = "accept_enable_clock";
	public final static String REQUEST_DISABLE_CLOCK_MESSAGE = "request_disable_clock";
	public final static String REJECT_DISABLE_CLOCK_MESSAGE = "reject_disable_clock";
	public final static String ACCEPT_DISABLE_CLOCK_MESSAGE = "accept_disable_clock";
	public final static String OFFER_DRAW_MESSAGE = "offer_draw";
	public final static String DRAW_ACCEPTED_MESSAGE = "draw_accepted";
	public final static String DRAW_REJECTED_MESSAGE = "draw_rejected";
	public final static String REQUEST_SIDE_SWITCH_MESSAGE = "request_side_switch";
	public final static String SIDE_SWITCH_ACCEPTED_MESSAGE = "side_switch_accepted";
	public final static String SIDE_SWITCH_REJECTED_MESSAGE = "side_switch_rejected";
	public final static String REQUEST_UPLOAD_MESSAGE = "request_upload";
	public final static String REJECT_UPLOAD_MESSAGE = "reject_upload";
	public final static String ACCEPT_UPLOAD_MESSAGE = "accept_upload";
	public final static String RESIGNED_MESSAGE = "resign";
	public final static String USER_MESSAGE = "message";
	public final static String RESTART_MESSAGE = "restart";
	public final static String QUIT_MESSAGE = "quit";
	public final static String WELCOME_MESSAGE = "Welcome!";
	public final static String REJECTED_MESSAGE = "Go away!";
	public final static String INIT_FROM_FEN_MESSAGE = "init_from_fen";
	public final static String ERROR_MESSAGE = "error";
	public final static String PLAYER_NAME_MESSAGE = "playername";

	protected java.util.LinkedList pendingAcks = new java.util.LinkedList();
	protected boolean gameIsLoading = false;
	protected boolean protocolIsConnected = false;
	protected boolean protocolServer = false;
	private JChessBoard jcb; // The parent JChessBoard
	//    private BoardConnector boardConnector;
	private String otherPlayersName = "Opponent";

	public boolean isConnected() {
		return protocolIsConnected;
	}

	public void clearPendingAcks() {
		pendingAcks.clear();
	}

	public void sendWelcome() {
		sendMessage(WELCOME_MESSAGE);
	}

	public void connectionClosed() {
		protocolIsConnected = false;
		protocolServer = false;
		pendingAcks.clear();
	}

	public void connectionEstablished() {
		protocolIsConnected = true;
		clearPendingAcks();
	}

	public void connecting() {
		protocolServer = false;
		protocolIsConnected = false;
	}

	public void sendMove(Move move) {
		sendMessage(MOVE_MESSAGE, move.toString());
	}

	public void sendBatchMove(Move move) {
		sendMessage(BATCH_MOVE_MESSAGE, move.toString());
	}

	public void startUpload() {
		sendMessage(START_UPLOAD_MESSAGE);
	}

	public void uploadDone() {
		sendMessage(BATCH_FINISHED_MESSAGE);
	}

	public void requestNewGame() {
		sendMessage(REQUEST_NEW_GAME_MESSAGE);
	}

	public void offerDraw() {
		sendMessage(OFFER_DRAW_MESSAGE);
	}

	public void resigned() {
		sendMessage(RESIGNED_MESSAGE);
	}

	public void requestSideSwitch() {
		sendMessage(REQUEST_SIDE_SWITCH_MESSAGE);
	}

	public void requestToggleClock() {
		sendMessage(REQUEST_TOGGLE_CLOCK_MESSAGE);
	}

	public void sendPlayersTime(long playersTime) {
		sendMessage(PLAYERS_TIME_MESSAGE, "" + playersTime);
	}

	public void sendError(String message) {
		sendMessage(ERROR_MESSAGE, message);
	}

	public void requestClockChange(long newWhiteTime, long newBlackTime) {
		sendMessage(REQUEST_CLOCK_CHANGE_MESSAGE, "" + newWhiteTime + " " + newBlackTime);
	}

	public void requestUndo() {
		sendMessage(REQUEST_UNDO_MESSAGE);
	}

	public void requestUpload() {
		sendMessage(REQUEST_UPLOAD_MESSAGE);
	}

	public void initFromFEN(String fen) {
		sendMessage(INIT_FROM_FEN_MESSAGE, fen);
	}

	public void sendPlayerName(String name) {
		sendMessage(PLAYER_NAME_MESSAGE,name);
	}

	/**
	 * Handles all the messages from a connected board.  The heart of the 
	 * protocol; it must handle all the incoming commands, as well as receive
	 * and send acknowledgements.
	 *
	 * @param line A line of input retrieved from the connected board.
	 */
	public void handleInput(String line) {
		StringTokenizer st;
		String message = null;
		String value = null;
		String value2 = null;

		// Pull the first three words out of the line: message, value, value2
		st = new StringTokenizer(line);
		if (st.hasMoreTokens())
			message = st.nextToken();
		if (st.hasMoreTokens())
			value = st.nextToken();
		if (st.hasMoreTokens())
			value2 = st.nextToken();

		// That first token tells us what to do
		if (message != null) {
			if (message.equals(QUIT_MESSAGE)) {
				jcb.showMessage("Opponent has quit!");
				jcb.getBoardConnector().closeConnection();
				jcb.connectionClosed();
			}
			// Checking the answers:
			else if (message.equals("?")) // Peer didn't understand.
			{
				// Reset communications
				jcb.showMessage("Out of sync! Trying to restart...");
				jcb.getBoardConnector().sendString(RESTART_MESSAGE);
				pendingAcks.clear();
				jcb.getConnectionIndicator().setError();
			} else if (message.equals("OK " + RESTART_MESSAGE)) {
				jcb.newGame();
				jcb.getConnectionIndicator().setReady();
				jcb.showMessage("Restarted due to unkown error.");
				
			} else if (message.equals("OK") && value != null && pendingAcks.size() > 0) {
				// Our latest message was received
				if (line.equals("OK " + (String) pendingAcks.get(0))) {
					pendingAcks.remove(0);
					if (pendingAcks.size() == 0) {
						jcb.getConnectionIndicator().setReady();
					}
				}
			} else {
				// Got new command from peer
				if (message.equals(WELCOME_MESSAGE)) {
					// Only clients get this message.
					// We're connected and ready to go 
					acknowledge(line);
					protocolIsConnected = true;
					jcb.getConnectionIndicator().setReady();
					jcb.showMessage("Game accepted.");
					jcb.connectionEstablished();
				} else if (message.equals(REJECTED_MESSAGE)) {
					// We've been rejected
					
					jcb.showMessage("Other player doesn't want to play.");
					jcb.connectionClosed();
				} else if (message.equals(UNDO_MESSAGE)) {
					// Undo handling
					jcb.undoMove();
					
					acknowledge(line);
				} else if (message.equals(REQUEST_UNDO_MESSAGE)) {
					Object[] options = { "Accept", "Reject" };
					int selection =
						JOptionPane.showOptionDialog(
								null,
								"Opponent wants to take back the last move.",
								"Undo request",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[0]);
					acknowledge(line);
					if (selection == JOptionPane.YES_OPTION) {
						jcb.undoMove();
						sendMessage(UNDO_MESSAGE);
					} else {
						sendMessage(UNDO_REJECTED_MESSAGE);
					}
				} else if (message.equals(UNDO_REJECTED_MESSAGE)) {
					jcb.showMessage("Undo request rejected.");
					acknowledge(line);
				} else if (message.equals(REQUEST_TOGGLE_CLOCK_MESSAGE)) {
					// Enabling the clock
					Object[] options = { "Accept", "Reject" };
					int selection =
						JOptionPane.showOptionDialog(
								null,
								"Opponent wants to turn " + (jcb.getEnableClock() ? "off" : "on") + " the clock",
								"Clock request",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[0]);
					acknowledge(line);
					if (selection == JOptionPane.YES_OPTION) {
						jcb.setEnableClock(!jcb.getEnableClock());
						jcb.update();
						jcb.showMessage("Chess clocks " + (jcb.getEnableClock() ? "enabled" : "disabled") + ".");
						sendMessage(ACCEPT_TOGGLE_CLOCK_MESSAGE);
					} else {
						sendMessage(REJECT_TOGGLE_CLOCK_MESSAGE);
					}
				} else if (message.equals(ACCEPT_TOGGLE_CLOCK_MESSAGE)) {
					jcb.setEnableClock(!jcb.getEnableClock());
					//	jcb.setEnableClock(false);
					jcb.update();
					jcb.showMessage("Chess clocks " + (jcb.getEnableClock() ? "enabled" : "disabled") + ".");
					acknowledge(line);
				} else if (message.equals(REJECT_TOGGLE_CLOCK_MESSAGE)) {
					jcb.showMessage(
							"Your wish to turn " + (jcb.getEnableClock() ? "off" : "on") + " the clock was rejected.");
					acknowledge(line);
				}
				// Draw handling
				else if (message.equals(OFFER_DRAW_MESSAGE)) {
					Object[] options = { "Accept", "Reject" };
					int selection =
						JOptionPane.showOptionDialog(
								null,
								"Opponent offers a draw.",
								"Draw offered",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[0]);
					acknowledge(line);
					if (selection == JOptionPane.YES_OPTION) {
						sendMessage(DRAW_ACCEPTED_MESSAGE);
						jcb.showMessage("Draw accepted.");
						//                        jcb.stopGame();
						jcb.getHistory().setResult("1/2-1/2");
					} else {
						sendMessage(DRAW_REJECTED_MESSAGE);
					}
				} else if (message.equals(DRAW_ACCEPTED_MESSAGE)) {
					jcb.showMessage("Draw accepted.");
					
					//                    jcb.stopGame();
					jcb.getHistory().setResult("1/2-1/2");
					acknowledge(line);
				} else if (message.equals(DRAW_REJECTED_MESSAGE)) {
					jcb.showMessage("Draw rejected.");
					acknowledge(line);
				}
				// Resign
				else if (message.equals(RESIGNED_MESSAGE)) {
					acknowledge(line);
					jcb.showMessage("Opponent resigned.");
					
					//                    jcb.stopGame();
					jcb.getHistory().setResult((jcb.getWhitePlayer() == JChessBoardImpl.HUMAN) ? "1-0" : "0-1");
				}
				// New Game
				else if (message.equals(NEW_GAME_MESSAGE)) {
					
					jcb.getHistory().clear();
					jcb.newGame();
					jcb.showMessage("New game.");
					acknowledge(line);
				} else if (message.equals(REQUEST_NEW_GAME_MESSAGE)) {
					
					Object[] options = { "Accept", "Reject" };
					int selection =
						JOptionPane.showOptionDialog(
								null,
								"Opponent requests a new game.",
								"New game?",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[0]);
					acknowledge(line);
					if (selection == JOptionPane.YES_OPTION) {
						jcb.getHistory().clear();
						jcb.newGame();
						jcb.showMessage("New game.");
						sendMessage(NEW_GAME_MESSAGE);
					} else {
						sendMessage(NEW_GAME_REJECTED_MESSAGE);
					}
				} else if (message.equals(NEW_GAME_REJECTED_MESSAGE)) {
					jcb.showMessage("New game rejected.");
					acknowledge(line);
				}
				// Time handling
				else if (message.equals(REQUEST_CLOCK_CHANGE_MESSAGE)) {
					long newWhiteTime = Long.parseLong(value);
					long newBlackTime = Long.parseLong(value2);
					Object[] options = { "Accept", "Reject" };
					int selection =
						JOptionPane.showOptionDialog(
								null,
								"Opponent wants to change the clocks to:\n "
								+ "White: "
								+ jcb.getChessClock().formatTime(newWhiteTime)
								+ "  Black: "
								+ jcb.getChessClock().formatTime(newBlackTime),
								"Change clocks?",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[0]);
					acknowledge(line);
					if (selection == JOptionPane.YES_OPTION) {
						sendMessage(GAME_TIME_MESSAGE, "" + newWhiteTime + " " + newBlackTime);
						jcb.setClocks(newWhiteTime, newBlackTime);
					} else {
						sendString(CLOCK_CHANGE_REJECTED_MESSAGE);
					}
				} else if (message.equals(CLOCK_CHANGE_REJECTED_MESSAGE)) {
					jcb.showMessage("Clock change rejected.");
					acknowledge(line);
				} else if (message.equals(GAME_TIME_MESSAGE) && value != null && value2 != null) {
					jcb.setClocks(Long.parseLong(value), Long.parseLong(value2));
					acknowledge(line);
				} else if (message.equals(PLAYERS_TIME_MESSAGE) && value != null) {
					// Required to synchronize the clocks.
					if (jcb.getBlackPlayer() == JChessBoardImpl.PEER) {
						long time = Long.parseLong(value);
						jcb.getChessClock().setBlackPlayersTime(time);
						jcb.checkFinish();
					} else if (jcb.getWhitePlayer() == JChessBoardImpl.PEER) {
						long time = Long.parseLong(value);
						jcb.getChessClock().setWhitePlayersTime(time);
						jcb.checkFinish();
					}
					acknowledge(line);
					// Restart
				} else if (message.equals(RESTART_MESSAGE)) {
					// The other board must have detected an error.
					pendingAcks.clear();
					jcb.newGame();
					jcb.showMessage("Restarted due to unkown error.");
					
					acknowledge(line);
				} else if (message.equals(MOVE_MESSAGE) && value != null) {
					// Move
					acknowledge(line);
					jcb.makePeersMove(new Move(value));
				} else if (message.equals(REQUEST_UPLOAD_MESSAGE)) {
					Object[] options = { "Accept", "Reject" };
					int selection =
						JOptionPane.showOptionDialog(
								null,
								"Opponent wants to upload a saved game.",
								"Accept upload?",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[0]);
					acknowledge(line);
					if (selection == JOptionPane.YES_OPTION) {
						sendMessage(ACCEPT_UPLOAD_MESSAGE);
					} else {
						sendString(REJECT_UPLOAD_MESSAGE);
					}
				} else if (message.equals(ACCEPT_UPLOAD_MESSAGE)) {
					acknowledge(line);
					jcb.showMessage("Upload accepted.");
					jcb.uploadGame();
				} else if (message.equals(REJECT_UPLOAD_MESSAGE)) {
					acknowledge(line);
					jcb.showMessage("Upload rejected.");
				} else if (message.equals(START_UPLOAD_MESSAGE)) {
					jcb.newGame();
					jcb.showMessage("Peer started to upload a game...");
					gameIsLoading = true;
					
					acknowledge(line);
				} else if (message.equals(BATCH_MOVE_MESSAGE) && value != null) {
					Move move = new Move(value);
					jcb.makeBatchMove(move);
					gameIsLoading = true;
					acknowledge(line);
				} else if (message.equals(BATCH_FINISHED_MESSAGE)) {
					acknowledge(line);
					jcb.showMessage("Game successfully loaded.");
					jcb.setWhitePlayer(JChessBoardImpl.UNKNOWN); 
					jcb.setBlackPlayer(JChessBoardImpl.UNKNOWN);
					jcb.getGameTable().setNetworkGameIndex(jcb.getGameTable().getCurrentGameIndex());
					jcb.getChessClock().stopClock();
					jcb.getChessClock().resetClocks();
					jcb.update();
					jcb.prepareMove();
					gameIsLoading = false;
				} else if (message.equals(BATCH_CANCELED_MESSAGE)) {
					acknowledge(line);
					jcb.showMessage("Game upload canceled!");
					gameIsLoading = false;
				}
				// Initializaton from FEN
				else if (message.equals(INIT_FROM_FEN_MESSAGE) && value != null) {
					acknowledge(line);
					VirtualBoard vb = new VirtualBoard();
					vb.initFromFEN(value);
					jcb.newGame(vb);
					jcb.showMessage("Opponent initialized board from FEN.");
				}
				// Side switch
				else if (message.equals(REQUEST_SIDE_SWITCH_MESSAGE)) {
					
					Object[] options = { "Accept", "Reject" };
					int selection =
						JOptionPane.showOptionDialog(
								null,
								"Opponents wants to switch sides.",
								"Switch sides?",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[0]);
					acknowledge(line);
					if (selection == JOptionPane.YES_OPTION) {
						jcb.switchSides();
						sendMessage(SIDE_SWITCH_ACCEPTED_MESSAGE);
					} else {
						sendMessage(SIDE_SWITCH_REJECTED_MESSAGE);
					}
				} else if (message.equals(SIDE_SWITCH_ACCEPTED_MESSAGE)) {
					acknowledge(line);
					jcb.switchSides();
				} else if (message.equals(SIDE_SWITCH_REJECTED_MESSAGE)) {
					acknowledge(line);
					jcb.showMessage("Side switch rejected");
				}
				// Message
				else if (message.equals(USER_MESSAGE) && value != null) {
					acknowledge(line);
					jcb.showMessage(otherPlayersName + ": " + stripCmd(line), "chatIn");
				}
				// Player name
				else if (message.equals(PLAYER_NAME_MESSAGE) && value != null) {
					acknowledge(line);
					otherPlayersName = value;
					jcb.showMessage("Opponent set its name to \"" + value + "\".");
					jcb.updateSTR();
				}
				// Error
				else if (message.equals(ERROR_MESSAGE)) {
					acknowledge(line);
					if (value == null) {
						jcb.showMessage("An unspecified error occurred.");
					} else {
						jcb.showMessage("Error message from peer: " + value);
					}
					//                            history.clear();
					//                            newGame();
				} else {
					// Send outOfSync message
					sendString("?\n");
				}
			} // Not an acknowledgement
		} // Not a null message
	}

	/**
	 * Sends string, setting GUI connection indicator and debug output.
	 */
	protected void sendString(String s) {
		jcb.getConnectionIndicator().setWaiting();
		if (jcb.getSettings().debugDumpTraffic) {
			jcb.showMessage(">>>" + s, "debug");
		}
		pendingAcks.add(s);
		jcb.getBoardConnector().sendString(s);
	}

	/**
	 * Sends a message with values.
	 * @param message The message indicator, one of an enumeration of Strings
	 * @param value_line The values required for the message indicator
	 */
	public void sendMessage(String message, String value_line) {
		if (value_line == null)
			value_line = "";
		sendString(message + " " + value_line);
	}

	/**
	 * Sends a message with no values.
	 * @param message The message indicator, one of an enumeration of Strings
	 */
	public void sendMessage(String message) {
		sendString(message);
	}

	/**
	 * Sends acknowledgement string.  This indicates that we have received
	 * a message send by the connected board.  It <em>does not</em> indicate
	 * that the message has been correctly processed.
	 */
	protected void acknowledge(String s) {
		if (jcb.getSettings().debugDumpTraffic) {
			jcb.showMessage(">>>OK " + s, "debug");
		}
		jcb.getBoardConnector().sendString("OK " + s);
	}

	/**
    	Strip off the first "word" which is the command, leaving
    	just the remaining line.
	 */
	private static String stripCmd(String line) {
		StringTokenizer st = new StringTokenizer(line);
		if (st.hasMoreTokens()) {
			String cmd = st.nextToken();
			return (line.substring(cmd.length()));
		} else {
			return ("EmptyString");
		}
	}

	public Protocol(JChessBoard _jcb) {
		jcb = _jcb;
	}

	/**
	 * @return
	 */
	public String getOtherPlayersName() {
		return otherPlayersName;
	}

	/**
	 * @param string
	 */
	public void setOtherPlayersName(String string) {
		otherPlayersName = string;
	}

}
