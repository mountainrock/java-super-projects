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

/* $Id: BoardConnector.java 5 2009-11-10 07:56:47Z cdivossen $ */

package chess.jchessboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

/**
 * BoardConnector implements two-way non-blocking communication for
 * two JChessBoard programs.  It monitors a Socket for incoming messages,
 *
 * Earlier versions also included much protocol information.  The 
 * protocol (including the steps performed when each message is
 * received) is now part of the Protocol class. 
 */
class BoardConnector implements Runnable {
	// This class provides the connection between two JChessBoard's.
	// Connections to gnuchess and chess servers will be made with
	// other classes or subclasses of a generic Connector class, that
	// will be an super class of this class.
	private Socket so;
	private BufferedReader fromOther;
	private BufferedWriter toOther;
	private String otherHostname = "";
	private boolean isEnabled = false;
	private boolean isServer;
	private JChessBoardImpl jcb; //Shortcut to call methods of this instance.
	private Thread thisThread;
	private int port;

	public static String getVersion() {
		return "$Id: BoardConnector.java 5 2009-11-10 07:56:47Z cdivossen $";
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void sendString(String s) {
		if (isEnabled) {
			try {
				toOther.write(s + "\n");
				toOther.flush();
			} catch (IOException e) {
				// Something happenned to the socket.  It's no longer connected.
				isEnabled = false;
				// Board has been disconnected.
				jcb.connectionClosed();
				JOptionPane.showMessageDialog(
						null,
						"The connection was lost!\n" + e.toString(),
						"IOException in outOfSync",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Blocks waiting for input; when a message is received, places the entire
	 * message into the message queue.
	 */
	public void run() {

		// Finish low-level protocol connection
		if (isServer) {
			// Only the basic functions to ensure the connection is valid
			isEnabled = serverHandshake();
		} else {
			isEnabled = clientHandshake();
		}

		// Handle messages until disconnected
		try {
			boolean done = false;
			while (isEnabled && !done) {
				// Block for the next line
				final String line = fromOther.readLine();
				if(line!=null) {
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							jcb.protocol.handleInput(line);
						}
					});
				}
			}
		} catch (IOException e) {
		}
		isEnabled = false;
		try {
			if (so != null)
				so.close();
		} catch (IOException e) {
		}
		// Thread is done.  We'll be garbage collected later.
	}

	public void shutdown() {
		isEnabled = false;
		thisThread.interrupt();
	}

	public void closeConnection() {
		try {
			if (so != null) {
				toOther.write("quit\n");
				toOther.flush();
				toOther.close();
				fromOther.close();
				if (so != null) {
					//                    so.shutdownInput();
					so.close();
				}
			}
		} catch (IOException e) {
		}
		isEnabled = false;
		thisThread.interrupt();
		if (thisThread != Thread.currentThread()) {
			try {
				thisThread.join();
			} catch (InterruptedException e) {
			}
		}
	}

	public String getOtherHostName() {
		if (otherHostname != null) {
			return otherHostname;
		} else {
			return "";
		}
	}

	/**
	 * Encapsulates the steps taken by the server when initiating a 
	 * connection.  Too bad it's not this easy for people.
	 */
	public boolean serverHandshake() {
		boolean handshakeStatus = false;
		try {
			// Same as sendString(), but doesn't need isEnabled
			toOther.write(getVersion() + "\n");
			toOther.flush();
			String line = fromOther.readLine();
			if (line != null && line.equals(getVersion())) {
				// The protocols match!
				// Ask the user to accept or decline the connection.  This gets
				// invokeLater()ed; meanwhile, we just sit around queueing
				// messages and waiting.
				final BoardConnector bc = this;
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// Make the items for the incoming connection dialog
						Object[] options = { "Accept", "Reject" };
						Object[] message = new Object[2];
						message[0] =
							"Incoming game request from "
							+ bc.getOtherHostName()
							+ ".";
						JCheckBox newWindowCheckBox =
							new JCheckBox("New window");
						// Look for unconnected windows
						JChessBoardImpl newJCB = null;
						java.util.Iterator windowIter =
							JChessBoardImpl.windowList.iterator();
						while (windowIter.hasNext() && (newJCB == null)) {
							JChessBoardImpl candidate =
								(JChessBoardImpl) windowIter.next();
							if (candidate.getBoardConnector()==null) {
								newJCB = candidate;
							}
						}
						// If there is at least one window not currently connected,
						if (newJCB != null) {
							newWindowCheckBox.setEnabled(true);
							newWindowCheckBox.setSelected(false);
							newJCB.showMessage("Incoming connection...");
						}
						// But if all windows are already connected,
						else {
							newWindowCheckBox.setEnabled(false);
							newWindowCheckBox.setSelected(true);
						}
						message[1] = newWindowCheckBox;
						int selection =
							JOptionPane.showOptionDialog(
									newJCB,
									message,
									"Game request",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE,
									null,
									options,
									options[0]);
						if (selection == 0) {
							// Connection was accepted
							if (newWindowCheckBox.isSelected()) {
								// Wants to open in a new window
								if (newJCB != null) {
									// Set that mangled JCB window back to normal
									newJCB.showMessage("Not connected.");
									newJCB.prepareMove();
								}
								// Create the new JChessboard
								newJCB = new JChessBoardImpl("");
								newJCB.gameTable.clear(); // Remove the first game created by default.
							}
							// If newJCB was null, the newWindowCheckBox was forced selected,
							// and we've got a new newJCB.  If there was a newJCB, then we've
							// still got it here.  Either way, newJCB is valid.
							// Finally, set up for a new game
							newJCB.setBoardConnector(bc);
							jcb = newJCB;
							jcb.connectionEstablished();
							jcb.protocol.sendWelcome();
							// Must have BoardConnector before we can send the message!
						} else {
							bc.sendString(Protocol.REJECTED_MESSAGE);
							bc.closeConnection();
							if (newJCB != null) {
								newJCB.showMessage("Connection rejected.");
								newJCB.connectionIndicator.setNoConnection();
							}
						}
					}
				});
				handshakeStatus = true;
			} else {
				handshakeStatus = false;
			}
		} catch (IOException e) {
			// Something happenned to the socket.  It's no longer connected.
			handshakeStatus = false;
		}
		return handshakeStatus;
	}

	/**
	 * Encapsulates the steps taken by the client when initiating a 
	 * connection.  Too bad it's not this easy for people.
	 */
	public boolean clientHandshake() {
		boolean handshakeStatus = false;

		String line = null;
		// Would show the user a message, but we don't even know we're 
		// connecting, yet...

		try {
			// Step 1: wait for version and acknowledge
			// Read the protocol line
			line = fromOther.readLine();
			if (line != null) {
				// If it's the right protocol
				if (line.equals(getVersion())) {
					// Send our protocol (assures other side all is correct)
					toOther.write(getVersion() + "\n");
					toOther.flush();
					// We're ready to go!  Our JChessBoard has been polling our status;  
					// tell it to get ready for welcome/reject
					handshakeStatus = true;
				} else {
					// Protocol didn't match
					// --JAM: Would be nice to give the user an indication, but
					// we're still not in the Swing thread
					closeConnection();
					handshakeStatus = false;
				}
			} else {
				// --JAM: Would be nice to give the user an indication, but
				// we're still not in the Swing thread
				handshakeStatus = false;
			}
		} catch (IOException ioe) {
			handshakeStatus = false;
		}

		return handshakeStatus;
	}

	public BoardConnector(JChessBoardImpl b, Socket s) {
		// Used for incoming connections.
		jcb = b;
		so = s;
		try {
			fromOther =
				new BufferedReader(new InputStreamReader(so.getInputStream()));
			toOther =
				new BufferedWriter(
						new OutputStreamWriter(so.getOutputStream()));
			otherHostname = so.getInetAddress().getHostName();
			isServer = true;
			isEnabled = true;
			// Must start thread to avoid garbage collection
			(thisThread = new Thread(this)).start();
		} catch (Exception e) {
			// Probably useless, but just in case...
			isEnabled = false;
		}
	}

	public BoardConnector(JChessBoardImpl b, String hostname, int networkPort) {
		// Used for outgoing connections.
		port = networkPort;
		otherHostname = hostname;

		// Open the socket
		try {
			// Start the connection indicator
			b.connectionIndicator.setWaiting();
			// These things are handled while GUI thread draws.
			so = new Socket(otherHostname, port);
			fromOther =
				new BufferedReader(new InputStreamReader(so.getInputStream()));
			toOther =
				new BufferedWriter(
						new OutputStreamWriter(so.getOutputStream()));

			jcb = b;
			isServer = false;
			isEnabled = true;
			(thisThread = new Thread(this)).start();
		} catch (Exception e) {
			b.showMessage("Could not connect to \""+hostname+"\": "+e.getMessage());
			b.connectionIndicator.setNoConnection();
			isEnabled = false;
		}
	}
}
