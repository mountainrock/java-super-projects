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

/* $Id: ConnectionIndicator.java 5 2009-11-10 07:56:47Z cdivossen $ */

package chess.jchessboard;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

/**
 * This class the represents the conntection indicator shown in the lower 
 * right corner.
 */
class ConnectionIndicator extends JLabel {
	public static String getVersion() {
		return "$Id: ConnectionIndicator.java 5 2009-11-10 07:56:47Z cdivossen $";
	}

	private Color noConnectionColor, connectedColor, waitingColor;
	private Color waitingColor2, errorColor;
	private boolean isWaiting = false;
	private boolean i = false; // Toggles waiting color
	private java.awt.event.ActionListener listener =
		new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			toggleColor();
		}
	};
	private javax.swing.Timer toggleTimer =
		new javax.swing.Timer(250, listener);

	private void toggleColor() {
		if (i)
			setBackground(waitingColor);
		else
			setBackground(waitingColor2);
		i = !i;
	}

	/**
	 * Makes the ConnectionIndicator go in the connected state.
	 */
	public void setReady() {
		setBackground(connectedColor);
		setToolTipText("Connected");
		toggleTimer.stop();
		isWaiting = false;
	}

	/**
	 * Sets this ConnectionIndicator to the error state.
	 */
	public void setError() {
		setBackground(errorColor);
		setToolTipText("Error!");
		toggleTimer.stop();
		isWaiting = false;
	}

	/**
	 * Sets this ConnectionIndicator to the waiting state.
	 * The ConnectionIndicator will blink while waiting.
	 */
	public void setWaiting() {
		setToolTipText("Waiting...");
		isWaiting = true;
		toggleTimer.start();
	}

	/**
	 * Sets this ConnectionIndicator to not-connected state.
	 */
	public void setNoConnection() {
		setToolTipText("Not connected");
		isWaiting = false;
		toggleTimer.stop();
		setBackground(noConnectionColor);
	}

	/**
	 * Returns true if this ConnectionIndicator is in the waiting state.
	 */
	public boolean isWaiting() {
		return isWaiting;
	}

	/**
	 * Creates a new instance of ConnectionIndicator.
	 */
	public ConnectionIndicator() {
		setPreferredSize(new Dimension(15, 15));
		setOpaque(true);
		setBorder(javax.swing.border.LineBorder.createBlackLineBorder());
		//setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		noConnectionColor = getBackground();
		connectedColor = new Color(50, 170, 50);
		waitingColor = new Color(255, 255, 80);
		waitingColor2 = new Color(180, 180, 50);
		errorColor = new Color(170, 50, 50);
		setNoConnection();
	}
}
