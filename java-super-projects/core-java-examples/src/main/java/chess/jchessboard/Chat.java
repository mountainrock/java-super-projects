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

/* $Id: Chat.java 7 2009-11-15 18:58:42Z cdivossen $ */

package chess.jchessboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Keymap;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Position;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

class Chat extends JPanel {
	private DefaultListModel visibleList;
	private JList chatJList;
	private JViewport chatViewport;
	private JScrollPane scrollPane;
	private JChessBoard jcb;
	private JTextField textfield;
	private JScrollBar scrollBar;
	private JTextPane textPane;
	private Component view;
	private DefaultStyledDocument document;
	private Position endPosition;
	private int replaceOffset = -1;
	private java.util.List inputHistory = new java.util.Vector();
	private int inputHistoryIndex = 0;

	public static String getVersion() {
		return "$Id: Chat.java 7 2009-11-15 18:58:42Z cdivossen $";
	}

	public void showMessage(String message) {
		showMessagePart(message + "\n");
	}

	public void showMessagePart(String message) {
		showMessagePart(message, "regular", false);
	}

	public void showMessage(String message, String style) {
		if (message.length() > 0)
			message += "\n";
		showMessagePart(message, style, false);
	}

	public void showReplaceableMessage(String message) {
		showReplaceableMessage(message, "PGNComment");
	}

	public void showReplaceableMessage(String message, String style) {
		if (message.length() > 0)
			message += "\n";
		showMessagePart(message, style, true);
	}

	public void showMessagePart(
			String message,
			String style,
			boolean replaceable) {
		final String lmessage = message;
		final String lstyle = style;
		final boolean lreplaceable = replaceable;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					synchronized (document) {
						if (replaceOffset != -1) {
							document.remove(
									replaceOffset,
									endPosition.getOffset() - replaceOffset - 1);
						}
						if (lreplaceable)
							replaceOffset = endPosition.getOffset() - 1;
						else
							replaceOffset = -1;
						document.insertString(
								endPosition.getOffset() - 1,
								lmessage,
								textPane.getStyle(lstyle));
					}
					textPane.setCaretPosition(document.getLength());
				} catch (BadLocationException e) {
					System.out.println(e);
				}
				scrollBar.setValue(Integer.MAX_VALUE);
			}
		});
	}

	public void showActionMessagePart(String message, String action) {
		final MutableAttributeSet attributes = textPane.getStyle("action");
		final String lmessage = message;
		attributes.addAttribute("action", action);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				synchronized (document) {
					try {
						document.insertString(
								endPosition.getOffset() - 1,
								lmessage,
								attributes);
					} catch (BadLocationException e) {
					}
				}
			}
		});
	}



	public void parseInput(String message) {
		if (message.equals(""))
			return;

		inputHistory.add(new String(message));
		inputHistoryIndex = inputHistory.size();

		if (message.startsWith("/")) {
			int index = message.indexOf(' ');
			String command = null;
			if (index > 0)
				command = message.substring(1, index);
			else
				command = message.substring(1, message.length());
			String argument = null;
			if (index > 0 && message.length() > index + 1)
				argument = message.substring(index + 1, message.length());

			if (command.equals("set")) {
				if (argument == null)
					showMessage("Missing argument");
				else
					jcb.getSettings().parseLine(argument);
			} else if (command.equals("getfen")) {
				String fen = jcb.getCurrentVirtualBoard().getFEN();
				showMessage("FEN: " + fen);
			} else if (command.equals("fen")) {
				if (argument == null) {
					showMessage("Missing argument");
					return;
				}
				/* --JAM: We can too init from FEN in a network game.  Almost all the code 
				 * is there, just commented out.
                        if(jcb.isConnected()) {
                            showMessage("Initialization from FEN not supported in network games.");
                            return;
                        }
				 */
				VirtualBoard newBoard = new VirtualBoard();
				boolean success = newBoard.initFromFEN(argument);
				if (success) {
					showMessage("Initialization from FEN OK.");
					if (jcb.isConnected()) {
						// Opponent will need the new board, too
						// --JAM: maybe we should ask him first?
						jcb.getProtocol().sendMessage(
								Protocol.INIT_FROM_FEN_MESSAGE,
								argument);
					}
					jcb.newGame(newBoard);
				} else {
					showMessage("Initialization from FEN failed.");
				}
				showMessage("FEN was: "+argument);
			} else if (command.equals("exit")) {
				jcb.exit();
			} else if (command.equals("name")) {
				if (argument == null) {
					showMessage("Missing argument");
					return;
				}
				jcb.getSettings().userName=argument;
				jcb.showMessage("Set name to \""+argument+"\".");
				jcb.updateSTR();
				if(jcb.isConnected())
					jcb.getProtocol().sendPlayerName(argument);
			} else if (command.equals("undo")) {
				jcb.undoMove();
			} else if (command.equals("perf")) {
				jcb.getAi().perfTest();
			} else if (command.equals("m")) {
				try {
					VirtualBoard newVB =
						jcb.getCurrentVirtualBoard().clonedBoard();
					newVB.makeMove(argument);
					Move thisMove = newVB.getLastMove();
					jcb.makeUsersMove(thisMove);
				} catch (VirtualBoard.ImpossibleMoveException e) {
					showMessage("Impossible move: " + argument);
				}
			} else if (command.equals("nag")) {
				if (argument == null) {
					showMessage("Missing argument");
					return;
				}
				try {
					int nag = Integer.parseInt(argument);
					if (nag < 0 || nag >= PGN.NAGStrings.length)
						throw new NumberFormatException();
					jcb.getHistory().addNAG(nag);
					jcb.update();
					//                        jcb.getHistory().gotoIndex(jcb.getHistory().getCurrentIndex());
				} catch (NumberFormatException e) {
					showMessage("Illegal NAG: " + argument);
				}
			} else if (command.equals("c")) {
				jcb.getHistory().addComment(argument);
				jcb.update();
			} else if (command.equals("lsnags")) {
				showMessage("Available Numeric Annotation Glyphs:");
				for (int n = 0; n < PGN.NAGStrings.length; n++)
					showMessage(n + ". " + PGN.NAGStrings[n]);
			} else if (command.equals("getpgn")) {
				showMessage(jcb.getHistory().getPGNBody());
			} else if (command.equals("eval")) {
				showMessage(AI.evaluate(jcb.getCurrentVirtualBoard())+"");
			} else if (command.equals("ver")) {
				showMessage(
						AI.getVersion()
						+ "\n"
						+ BoardConnector.getVersion()
						+ "\n"
						+ Chat.getVersion()
						+ "\n"
						+ ChessClock.getVersion()
						+ "\n"
						+ ConnectionListener.getVersion()
						+ "\n"
						+ ConnectionIndicator.getVersion()
						+ "\n"
						+ History.getVersion()
						+ "\n"
						+ JChessBoardImpl.getVersion()
						+ "\n"
						+ Move.getVersion()
						+ "\n"
						+ PGN.getVersion()
						+ "\n"
						+ VirtualBoard.getVersion()
						+ "\n"
						+ VisualBoard.getVersion());
			} else if (command.equals("help")) {
				showMessage("---- Available commands: ----", "help");
				showMessage(
						"/getfen       Give Forsyth-Edwards Notation for the current board.",
				"help");
				showMessage("/fen <FEN>    Initialize board from FEN.", "help");
				//                    showMessage("/set <variable>=<value> \tSet <variable> to <value>.","help");
				showMessage("/help         Show this.", "help");
				showMessage(
						"/m <move>     Make <move>, <move> must be in algebraic notation.",
				"help");
				showMessage("/undo         Undo the last move.", "help");
				showMessage(
						"/nag <nag>    Add a Numeric Annotation Glyph to the last move.",
				"help");
				showMessage(
						"/c <comment>        Add <comment> to the last move.",
				"help");
				showMessage(
						"/lsnags       Show the list of available NAGs.",
				"help");
				showMessage(
						"/eval         Evaluate the current position.",
				"help");
				showMessage(
						"/ver          Show version of each module.",
				"help");
				showMessage(
						"/name <name>  Set your name.",
				"help");
				showMessage("/exit         Exit JChessBoard.", "help");
			} else
				showMessage("Unknown command: " + command);
		} else {
			if (jcb.isConnected()) {
				jcb.getProtocol().sendMessage(Protocol.USER_MESSAGE, message);
			}
			showMessage("You: " + message, "chatOut");
		}
	}

	public Chat(JChessBoard chessBoard) {
		super(new BorderLayout());
		this.jcb = chessBoard;
		setBorder(new javax.swing.border.EtchedBorder());
		//document = new DefaultStyledDocument();
		document = new DefaultStyledDocument();
		endPosition = document.getEndPosition();
		textPane = new JTextPane(document);
		textPane.setEditable(false);
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//scrollPane.setPreferredSize(new Dimension(1,jcb.getSettings().chatSize));
		scrollBar = scrollPane.getVerticalScrollBar();
		chatViewport = scrollPane.getViewport();
		chatViewport.add(textPane);
		//    chatViewport.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		view = chatViewport.getView();
		add("Center", scrollPane);

		//Initialize some styles.
		Style def =
			StyleContext.getDefaultStyleContext().getStyle(
					StyleContext.DEFAULT_STYLE);
		Style regular = textPane.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");

		Style s = textPane.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = textPane.addStyle("annotation", regular);
		StyleConstants.setFontFamily(s, "Serif");
		StyleConstants.setBold(s, true);

		s = textPane.addStyle("action", regular);
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, new Color(0, 0, 255));

		s = textPane.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

		s = textPane.addStyle("help", regular);
		StyleConstants.setBold(s, true);
		StyleConstants.setFontFamily(s, "Monospaced");

		s = textPane.addStyle("chatIn", regular);
		StyleConstants.setBold(s, true);
		StyleConstants.setForeground(s, chessBoard.getSettings().chatColorOpponent);

		s = textPane.addStyle("chatOut", regular);
		StyleConstants.setBold(s, true);
		StyleConstants.setForeground(s, chessBoard.getSettings().chatColorYou);

		s = textPane.addStyle("small", regular);
		StyleConstants.setFontSize(s, 8);

		s = textPane.addStyle("debug", regular);
		StyleConstants.setFontSize(s, 8);
		StyleConstants.setForeground(s, chessBoard.getSettings().chatColorDebug);

		s = textPane.addStyle("large", regular);
		StyleConstants.setFontSize(s, 16);

		textfield = new JTextField();
		textfield.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String message = textfield.getText();
				textfield.setText("");
				parseInput(message);
			}
		});

		Keymap keymap = textfield.getKeymap();
		keymap
		.addActionForKeyStroke(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, 0),
				new AbstractAction() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						if (inputHistoryIndex > 0)
							inputHistoryIndex--;
						if (inputHistory.size() > 0)
							textfield.setText(
									(String) inputHistory.get(inputHistoryIndex));
					}
				});
		keymap
		.addActionForKeyStroke(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, 0),
				new AbstractAction() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						if (inputHistoryIndex < inputHistory.size())
							inputHistoryIndex++;
						if (inputHistoryIndex == inputHistory.size())
							textfield.setText("");
						else
							textfield.setText(
									(String) inputHistory.get(inputHistoryIndex));
					}
				});
		keymap
		.addActionForKeyStroke(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
				new AbstractAction() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						inputHistoryIndex = inputHistory.size();
						textfield.setText("");
					}
				});
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		messagePanel.setBorder(
				javax.swing.border.LineBorder.createBlackLineBorder());
		messagePanel.add("Center", textfield);
		messagePanel.add("East", chessBoard.getConnectionIndicator());
		add("South", messagePanel);

		textPane.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
				AttributeSet attributes =
					document
					.getCharacterElement(textPane.viewToModel(e.getPoint()))
					.getAttributes();
				if (attributes.isDefined("action")) {
					Object action = attributes.getAttribute("action");
					if (action instanceof String)
						parseInput((String) action);
				}
			}
			public void mouseMoved(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
		});
	}
}
