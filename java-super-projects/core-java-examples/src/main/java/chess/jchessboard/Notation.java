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

/* $Id: Notation.java 5 2009-11-10 07:56:47Z cdivossen $ */

package chess.jchessboard;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Keymap;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * @author cd
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Notation extends JScrollPane {
	private History history;
	private JTextPane notationTextPane;
	private StyledDocument notationDocument;
	private int currentNotationPos = 0;

	public void update() {
		currentNotationPos = 0;
		try {
			notationDocument.remove(0, notationDocument.getLength());
			insertNotation(history.getTopGameNode(), notationDocument, 0);
			notationTextPane.setCaretPosition(currentNotationPos);
		} catch (BadLocationException e) {
		}
	}

	private int insertNotation(GameNode startNode, StyledDocument doc, int pos) {
		String s = "";
		int startPos = pos;
		boolean isContinuous = false;

		Style boldStyle, regularStyle, commentStyle, currentNodeStyle, nagStyle, grayStyle;
		grayStyle = notationTextPane.getStyle("gray");
		GameNode currentNode = history.getCurrentGameNode();
		if ((currentNode.isLeaf() && startNode == currentNode.getParent()) || startNode == currentNode) {
			// Make sure only the current variation is colored.
			boldStyle = notationTextPane.getStyle("bold");
			regularStyle = notationTextPane.getStyle("regular");
			commentStyle = notationTextPane.getStyle("comment");
			currentNodeStyle = notationTextPane.getStyle("currentNode");
			nagStyle = notationTextPane.getStyle("nag");
		} else {
			boldStyle = notationTextPane.getStyle("graybold");
			regularStyle = notationTextPane.getStyle("gray");
			commentStyle = notationTextPane.getStyle("gray");
			currentNodeStyle = notationTextPane.getStyle("gray");
			nagStyle = notationTextPane.getStyle("gray");
		}
		try {
			if (startNode.getComment().length() > 0) {
				s = startNode.getComment() + " ";
				commentStyle.addAttribute("gameNode", startNode);
				doc.insertString(pos, s, commentStyle);
				pos += s.length();
			}
			for (int n = 0; n < startNode.getChildCount(); n++) {
				GameNode node = (GameNode) startNode.getChildAt(n);
				boolean isCurrentNode = (node == currentNode);
				if (node.isLeaf()) {
					if (node.isWhiteMove()) {
						s = node.getFullMoveNumber() + ". ";
						boldStyle.addAttribute("gameNode", node);
						doc.insertString(pos, s, boldStyle);
						pos += s.length();
						s = node.getMove();
						Style style = isCurrentNode ? currentNodeStyle : regularStyle;
						style.addAttribute("gameNode", node);
						doc.insertString(pos, s, style);
						pos += s.length();
						isContinuous = true;
					} else {
						if (isContinuous) {
							s = node.getMove();
							Style style = isCurrentNode ? currentNodeStyle : regularStyle;
							style.addAttribute("gameNode", node);
							doc.insertString(pos, s, style);
							pos += s.length();
						} else {
							isContinuous = true;
							s = node.getFullMoveNumber() + "... ";
							boldStyle.addAttribute("gameNode", node);
							doc.insertString(pos, s, boldStyle);
							pos += s.length();
							s = node.getMove();
							Style style = isCurrentNode ? currentNodeStyle : regularStyle;
							style.addAttribute("gameNode", node);
							doc.insertString(pos, s, style);
							pos += s.length();
						}
					}
					if (node.hasComment()) {
						for (int m = 0; m < node.getNags().length; m++) {
							s = " " + PGN.NAGStrings[node.getNags()[m]];
							nagStyle.addAttribute("gameNode", node);
							doc.insertString(pos, s, nagStyle);
							pos += s.length();
						}
						if (node.getComment().length() > 0) {
							s = " " + node.getComment();
							commentStyle.addAttribute("gameNode", node);
							doc.insertString(pos, s, commentStyle);
							pos += s.length();
						}
					}
					doc.insertString(pos++, " ", regularStyle);
				} else {
					isContinuous = false;
					if (node == currentNode.getParent() || isCurrentNode) {
						doc.insertString(pos++, "(", notationTextPane.getStyle("regular"));
						pos += insertNotation(node, doc, pos);
						doc.insertString((pos++) - 1, ")", notationTextPane.getStyle("regular"));
					} else {
						doc.insertString(pos++, "(", grayStyle);
						pos += insertNotation(node, doc, pos);
						doc.insertString((pos++) - 1, ")", grayStyle);
					}
				}
				if (isCurrentNode) // Required to set the caret position
					currentNotationPos = pos;
			}
		} catch (BadLocationException e) {
		}
		return pos - startPos;
	}

	public Notation(History history) {
		this.history = history;
		notationDocument = new javax.swing.text.DefaultStyledDocument();

		notationTextPane = new JTextPane(notationDocument);
		notationTextPane.setEditable(false);
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		Style regular = notationTextPane.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");
		Style s = notationTextPane.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);
		s = notationTextPane.addStyle("bold", regular);
		StyleConstants.setBold(s, true);
		s = notationTextPane.addStyle("comment", regular);
		StyleConstants.setForeground(s, new Color(50, 50, 255));
		s = notationTextPane.addStyle("nag", regular);
		StyleConstants.setForeground(s, new Color(30, 30, 150));
		s = notationTextPane.addStyle("currentNode", regular);
		StyleConstants.setBackground(s, new Color(210, 210, 230));
		s = notationTextPane.addStyle("gray", regular);
		StyleConstants.setForeground(s, new Color(150, 200, 150));
		s = notationTextPane.addStyle("graybold", regular);
		StyleConstants.setForeground(s, new Color(150, 200, 150));
		StyleConstants.setBold(s, true);

		notationTextPane.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
				AttributeSet attributes =
					notationDocument.getCharacterElement(notationTextPane.viewToModel(e.getPoint())).getAttributes();
				if (attributes.isDefined("gameNode")) {
					Notation.this.history.gotoGameNode((GameNode) attributes.getAttribute("gameNode"));
				}
			}
			public void mouseMoved(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
		});

		Keymap keymap = notationTextPane.getKeymap();
		keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, 0), new AbstractAction() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Notation.this.history.prev();
			}
		});
		keymap
		.addActionForKeyStroke(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, 0), new AbstractAction() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Notation.this.history.next();
			}
		});

		getViewport().add(notationTextPane);

	}

}
