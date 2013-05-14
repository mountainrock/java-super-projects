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

/* $Id: ChessClock.java 7 2009-11-15 18:58:42Z cdivossen $ */

package chess.jchessboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * ChessClock keeps track of the time each player has to make moves.
 * It also holds the selectors for who is playing which side.
 */
class ChessClock extends JPanel {
	private JChessBoard jcb;
	private boolean isEnabled = true;
	private boolean whiteTimeIsRunning = false;
	private boolean blackTimeIsRunning = false;
	private long whiteTime = 600000, blackTime = 600000; //milliseconds
	private JLabel whiteTimeLabel, blackTimeLabel;
	private boolean isWhiteTurn = true;
	private boolean newPlayerIsWhite = true;
	private long lastSysTime;
	private long newGameTime;
//	private ImageIcon aiIcon, humanIcon, netIcon, unknownIcon;
	private java.text.DecimalFormat timeFormater = new java.text.DecimalFormat("00");
	private java.awt.event.ActionListener listener = new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			updateClocks();
		}
	};
	private Timer clockTimer = new Timer(250, listener);
	final static Color WHITE = new Color(255, 255, 255);
	final static Color BLACK = new Color(0, 0, 0);
	final static Color GRAY = new Color(128, 128, 128);
	JComboBox whitePlayerSelector;
	JComboBox blackPlayerSelector;
	private java.awt.event.ActionListener whitePlayerSelectorListener;
	private java.awt.event.ActionListener blackPlayerSelectorListener;


	public static String getVersion() {
		return "$Id: ChessClock.java 7 2009-11-15 18:58:42Z cdivossen $";
	}

	private void updateClocks() {
		if (jcb.isConnected() && jcb.getConnectionIndicator().isWaiting()) {
			// If playing a network game, halt the clock until the peer 
			// acknowledged the move.
			lastSysTime = System.currentTimeMillis();
			return;
		}
		long thisSysTime = System.currentTimeMillis();
		if (whiteTimeIsRunning) {
			whiteTime -= thisSysTime - lastSysTime;
			if (whiteTime <= 0) {
				whiteTime = 0;
				whiteTimeIsRunning = false;
				blackTimeIsRunning = false;
				if (jcb.getWhitePlayer().equals(JChessBoardImpl.HUMAN)) {
					// Stop game only if own time is over.
					// Otherwise a move of the opponent made in the last
					// moment received delayed due to network delays could
					// be refused.
					synchronized (jcb) {
						jcb.timeForfeit();
					}
				}
			}
		} else if (blackTimeIsRunning) {
			blackTime -= thisSysTime - lastSysTime;
			if (blackTime <= 0) {
				blackTime = 0;
				blackTimeIsRunning = false;
				whiteTimeIsRunning = false;
				if (jcb.getBlackPlayer() == JChessBoardImpl.HUMAN) {
					synchronized (jcb) {
						jcb.timeForfeit();
					}
				}
			}
		}
		showTimes();
		lastSysTime = System.currentTimeMillis();
	}
	private void showTimes() {
		if (isEnabled) {
			whiteTimeLabel.setText(formatTime(whiteTime));
			blackTimeLabel.setText(formatTime(blackTime));
		} else {
			whiteTimeLabel.setText("--:--");
			blackTimeLabel.setText("--:--");
		}
	}
	/**
	 * Converts the time given in milliseconds to an minutes:seconds format,
	 * e.g. formatTime(60000) would return "01:00".
	 */
	public String formatTime(long time) {
		long t = time / 1000;
		if (t * 1000 < time)
			t++;
		return timeFormater.format(t / 60) + ":" + timeFormater.format(t % 60);
	}
	public void setEnabled(boolean enable) {
		if (isEnabled && !enable)
			stopClock();
		if (!isEnabled && enable)
			resetClocks();
		isEnabled = enable;
		showTimes();
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	/**
	 * Stops both clocks.
	 */
	public void stopClock() {
		blackTimeIsRunning = false;
		whiteTimeIsRunning = false;
		clockTimer.stop();
		updateClocks();
	}

	public boolean isRunning() {
		return blackTimeIsRunning || whiteTimeIsRunning;
	}

	/**
	 * Starts the appropriate clock.
	 */
	public void startClock() {
		if (!isEnabled)
			return;
		if (jcb.isWhiteTurn()) {
			blackTimeIsRunning = false;
			whiteTimeIsRunning = true;
		} else {
			blackTimeIsRunning = true;
			whiteTimeIsRunning = false;
		}
		lastSysTime = System.currentTimeMillis();
		showTimes();
		clockTimer.start();
	}
	/**
	 * Resets the clocks to the initial time.
	 */
	public void resetClocks() {
		whiteTime = jcb.getSettings().whiteTime;
		blackTime = jcb.getSettings().blackTime;
		showTimes();
	}
	/**
	 * Sets the clock of the white player.
	 * @param time The new time in ms.
	 */
	public void setWhitePlayersTime(long time) {
		whiteTime = time;
		showTimes();
	}
	/**
	 * Sets the clock of the black player.
	 * @param time The new time in ms.
	 */
	public void setBlackPlayersTime(long time) {
		blackTime = time;
		showTimes();
	}
	/**
	 * Returns the time on the clock of the white player in milliseconds.
	 */
	public long getWhiteTime() {
		return whiteTime;
	}
	/**
	 * Returns the time on the clock of the black player in milliseconds.
	 */
	public long getBlackTime() {
		return blackTime;
	}

	public void updatePlayerSelectors() {
		whitePlayerSelector.removeActionListener(whitePlayerSelectorListener);
		blackPlayerSelector.removeActionListener(blackPlayerSelectorListener);
		if (jcb.isConnected()) {
			if(whitePlayerSelector.getItemCount()<4) {
				whitePlayerSelector.addItem(JChessBoardImpl.UNKNOWN);
				whitePlayerSelector.addItem(JChessBoardImpl.PEER);
			}
			if(blackPlayerSelector.getItemCount()<4) {
				blackPlayerSelector.addItem(JChessBoardImpl.UNKNOWN);
				blackPlayerSelector.addItem(JChessBoardImpl.PEER);
			}
			whitePlayerSelector.setEnabled(false);
			blackPlayerSelector.setEnabled(false);
		} else {
			if(whitePlayerSelector.getItemCount()>2) {
				whitePlayerSelector.removeItem(JChessBoardImpl.PEER);
				whitePlayerSelector.removeItem(JChessBoardImpl.UNKNOWN);
			}
			if(blackPlayerSelector.getItemCount()>2) {
				blackPlayerSelector.removeItem(JChessBoardImpl.UNKNOWN);
				blackPlayerSelector.removeItem(JChessBoardImpl.PEER);
			}
			whitePlayerSelector.setEnabled(true);
			blackPlayerSelector.setEnabled(true);
		}
		whitePlayerSelector.setSelectedItem(jcb.getWhitePlayer());
		blackPlayerSelector.setSelectedItem(jcb.getBlackPlayer());
		whitePlayerSelector.addActionListener(whitePlayerSelectorListener);
		blackPlayerSelector.addActionListener(blackPlayerSelectorListener);
	}

	/** 
	 * Creates a new instance of ChessClock.
	 * @param chessBoard The JChessBoard that will be coupled with this clock.
	 */
	public ChessClock(JChessBoard chessBoard) {
		jcb = chessBoard;

// Icons not used anymore
//		java.net.URL url = JChessBoard.class.getResource("/chess/jchessboard/images/ai.gif");
//		aiIcon = url != null ? new ImageIcon(url) : new ImageIcon("null");
//
//		url = JChessBoard.class.getResource("/chess/jchessboard/images/human.gif");
//		humanIcon = url != null ? new ImageIcon(url) : new ImageIcon("null");
//
//		url = JChessBoard.class.getResource("/chess/jchessboard/images/net.gif");
//		netIcon = url != null ? new ImageIcon(url) : new ImageIcon("null");
//
//		url = JChessBoard.class.getResource("/chess/jchessboard/images/unknown.gif");
//		unknownIcon = url != null ? new ImageIcon(url) : new ImageIcon("null");

		//        whiteTimeLabel = new JLabel(humanIcon);
		//        blackTimeLabel = new JLabel(humanIcon);
		whiteTimeLabel = new JLabel();
		blackTimeLabel = new JLabel();
		Font clockFont = new Font("SansSerif", Font.BOLD, 18);
		whiteTimeLabel.setFont(clockFont);
		javax.swing.border.Border clockBorder = new javax.swing.border.EtchedBorder();
		setBorder(clockBorder);
		javax.swing.border.Border labelBorder =
			new javax.swing.border.BevelBorder(
					javax.swing.border.BevelBorder.LOWERED,
					new Color(220, 220, 220),
					new Color(100, 100, 100));
		blackTimeLabel.setFont(clockFont);
		whiteTimeLabel.setForeground(GRAY);
		whiteTimeLabel.setBackground(WHITE);
		whiteTimeLabel.setOpaque(true);
		whiteTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		whiteTimeLabel.setBorder(labelBorder);
		blackTimeLabel.setForeground(GRAY);
		blackTimeLabel.setBackground(BLACK);
		blackTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		blackTimeLabel.setOpaque(true);
		blackTimeLabel.setBorder(labelBorder);

		resetClocks();

		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridBag);

		c.fill = GridBagConstraints.NONE;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(3, 3, 3, 3);
		Dimension labelSize = new Dimension(75, 30);
		whiteTimeLabel.setPreferredSize(labelSize);
		whiteTimeLabel.setMinimumSize(labelSize);
		gridBag.setConstraints(whiteTimeLabel, c);
		add(whiteTimeLabel);

		c.gridx = 1;
		whitePlayerSelector = new JComboBox();
		whitePlayerSelector.addItem(JChessBoardImpl.HUMAN);
		whitePlayerSelector.addItem(JChessBoardImpl.COMPUTER);
		whitePlayerSelector.setFont(new Font("SanSerif", Font.PLAIN, 10));
		gridBag.setConstraints(whitePlayerSelector, c);
		add(whitePlayerSelector);
		whitePlayerSelectorListener = new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String value = (String) whitePlayerSelector.getSelectedItem();
				jcb.setWhitePlayer(value);
				jcb.updateSTR();
				jcb.triggerAI();
			}
		};
		whitePlayerSelector.addActionListener(whitePlayerSelectorListener);

		c.gridx = 2;
		blackTimeLabel.setPreferredSize(labelSize);
		blackTimeLabel.setMinimumSize(labelSize);
		gridBag.setConstraints(blackTimeLabel, c);
		add(blackTimeLabel);

		c.gridx = 3;
		blackPlayerSelector = new JComboBox();
		blackPlayerSelector.addItem(JChessBoardImpl.HUMAN);
		blackPlayerSelector.addItem(JChessBoardImpl.COMPUTER);
		blackPlayerSelector.setFont(new Font("SanSerif", Font.PLAIN, 10));
		gridBag.setConstraints(blackPlayerSelector, c);
		add(blackPlayerSelector);
		blackPlayerSelectorListener = new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String value = (String) blackPlayerSelector.getSelectedItem();
				jcb.setBlackPlayer(value);
				jcb.updateSTR();
				jcb.triggerAI();
			}
		};
		blackPlayerSelector.addActionListener(blackPlayerSelectorListener);

	}
}
