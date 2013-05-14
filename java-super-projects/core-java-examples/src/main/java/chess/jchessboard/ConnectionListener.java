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

/* $Id: ConnectionListener.java 5 2009-11-10 07:56:47Z cdivossen $ */

package chess.jchessboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

class ConnectionListener implements Runnable {
	public static String getVersion() {
		return "$Id: ConnectionListener.java 5 2009-11-10 07:56:47Z cdivossen $";
	}

	private Socket so;
	private ServerSocket serverso;
	boolean isEnabled = false;
	private Thread thisThread;
	private int port;
	private JCheckBox enableCheckBox;
	private JTextField portField;
	private JLabel portLabel;

	public void run() {
		try {
			serverso = new ServerSocket(port);
			while (isEnabled) {
				so = serverso.accept();
				// --JAM: The socket BoardConnector will have to search for its own window
				new BoardConnector((JChessBoardImpl) null, so);
			}
			serverso.close(); // No further connections.
		} catch (IOException e) {
			isEnabled = false;
			JOptionPane.showMessageDialog(
					null,
					e.toString(),
					"IOException in ConnectionListener",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void shutdown() {
		isEnabled = false;
	}

	public void showSettingsPane() {

		Object message[] = new Object[3];
		enableCheckBox = new JCheckBox("Enable server");
		enableCheckBox.setSelected(isEnabled);
		message[0] = enableCheckBox;
		portLabel = new JLabel("Port: ");
		portLabel.setEnabled(enableCheckBox.isSelected());
		message[1] = portLabel;
		portField = new JTextField(Integer.toString(port), 5);
		portField.setEnabled(enableCheckBox.isSelected());
		enableCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				portField.setEnabled(enableCheckBox.isSelected());
				portLabel.setEnabled(enableCheckBox.isSelected());
			}
		});
		message[2] = portField;
		Object options[] = { "OK", "Cancel" };

		int result =
			JOptionPane.showOptionDialog(
					null,
					message,
					"Server settings.",
					0,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);

		if (result == 0) {
			if (enableCheckBox.isSelected()) {
				try {
					int port = Integer.parseInt(portField.getText());
					if (port > 65535 || port <= 0)
						throw (new NumberFormatException());
					setPort(port);
					setEnabled(true);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(
							null,
							"Illegal port: " + portField.getText(),
							"Illegal port",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				setEnabled(false);
			}
		}
	}

	public void setEnabled(boolean enable) {
		synchronized (this) {
			if (enable == isEnabled)
				return;
			if (enable && !isEnabled) {
				isEnabled = true;
				thisThread = new Thread(this);
				thisThread.setDaemon(true);
				thisThread.start();
			}
			if (!enable && isEnabled) {
				isEnabled = false;
				try {
					if (serverso != null) {
						so = new Socket("localhost", port);
						so.close();
					}
					thisThread.join();
				} catch (Exception ex) {
				}
			}
		}

	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		if (port == this.port)
			return;
		else {
			synchronized (this) {
				boolean wasEnabled = isEnabled;
				setEnabled(false);
				this.port = port;
				setEnabled(wasEnabled);
			}
		}
	}

	public ConnectionListener() {
	}

}
