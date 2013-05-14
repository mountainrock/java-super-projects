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

/* $Id: GameNode.java 5 2009-11-10 07:56:47Z cdivossen $ */

package chess.jchessboard;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author cd
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class GameNode extends DefaultMutableTreeNode {
	private String comment = "";
	private String output = null;
	private int[] nags = new int[0];
	private String move = "";
	private VirtualBoard board; // The board this move lead to.

	public boolean isWhiteMove() {
		return !board.isWhiteTurn();
	}
	public int getFullMoveNumber() {
		return board.getFullMoveNumber();
	}
	public String getMove() {
		return move;
	}
	public VirtualBoard getBoard() {
		return board;
	}
	public void setBoard(VirtualBoard vb) {
		board = vb;
	}
//	public void initBoard() {
//	board.init();
//	treeModel.nodeChanged(this);
//	}
	public Object getUserObject() {
		return toString();
	}
	public String toString() {
		//            if(output==null) 
		output = makeOutput();
		return output;
	}
	private String makeShortOutput() {
		if (getParent()==null)
			return "Start";
		if (isLeaf()) {
			StringBuffer newOutput = new StringBuffer();
			newOutput.append(board.getFullMoveNumber());
			if (board.isWhiteTurn())
				newOutput.append("... ");
			else
				newOutput.append(". ");
			newOutput.append(move);
			return new String(newOutput.toString());
		} else
			return "Variation";
	}
	private String makeOutput() {
		StringBuffer newOutput = new StringBuffer(makeShortOutput());
		if (comment.length() > 0 || nags.length > 0) {
			newOutput.append("  [");
			for (int n = 0; n < nags.length; n++) {
				if (nags[n] > 0 && nags[n] < PGN.NAGStrings.length) {
					newOutput.append(PGN.NAGStrings[nags[n]]);
					newOutput.append(" ");
				}
			}
			if (comment.length() > 0) {
				newOutput.append("\"");
				if (comment.length() + newOutput.length() < 50)
					newOutput.append(comment);
				else if (newOutput.length() < 46) {
					newOutput.append(
							comment.substring(0, 46 - newOutput.length()));
					newOutput.append("...");
				}
				newOutput.append("\"");
			}
			newOutput.append("]");
		}
		return newOutput.toString();
	}
	public void setComment(String comment) {
		this.comment = comment;
		output = null;
	}
	public void addComment(String comment) {
		if (this.comment.equals(""))
			this.comment = comment;
		else
			this.comment = this.comment + " " + comment;
		output = null;
	}
	public boolean hasComment() {
		return comment.length() > 0 || nags.length > 0;
	}
	public String getComment() {
		return new String(comment);
	}
	public String getAnnotation() {
		if (comment.length() == 0 && nags.length == 0)
			return "";
		String anno = new String();
		anno =
			"Annotation for move "
			+ board.getFullMoveNumber()
			+ ". "
			+ move.toString()
			+ "\n";
		if (nags.length > 0) {
			for (int n = 0; n < nags.length; n++) {
				if (nags[n] > 0 && nags[n] < PGN.NAGStrings.length)
					anno += "NAG $"
						+ nags[n]
						       + ": "
						       + PGN.NAGStrings[nags[n]]
						                        + "\n";
			}
		}
		if (comment.length() > 0)
			anno += "\"" + comment + "\"";
		return anno;
	}
	public void addNAG(int nag) {
		int[] newNags = new int[nags.length + 1];
		for (int n = 0; n < nags.length; n++)
			newNags[n] = nags[n];
		newNags[nags.length] = nag;
		nags = newNags;
		output = null;
	}
	public int[] getNags() {
		return nags;
	}
	public void setNags(int[] newNags) {
		nags = newNags;
		output = null;
	}
	public boolean whiteMoveExpected() {
		return board.isWhiteTurn();
	}

	public String getPGN() {
		StringBuffer pgn = new StringBuffer();
		boolean isContinuous = false;

		if (comment.length() > 0) {
			pgn.append("{");
			pgn.append(comment);
			pgn.append("} ");
		}
		GameNode node = this;
		for (int n = 0; n < getChildCount(); n++) {
			node = (GameNode) getChildAt(n);
			if (node.isLeaf()) {
				if (node.isWhiteMove()) {
					pgn.append(node.getFullMoveNumber());
					pgn.append(". ");
					pgn.append(node.getMove());
					isContinuous = true;
				} else {
					if (isContinuous) {
						pgn.append(node.getMove());
					} else {
						isContinuous = true;
						pgn.append(node.getFullMoveNumber());
						pgn.append("... ");
						pgn.append(node.getMove());
					}
				}
				pgn.append(" ");
				if (node.hasComment()) {
					for (int m = 0; m < node.getNags().length; m++) {
						pgn.append("$");
						pgn.append(Integer.toString(node.getNags()[m]));
						pgn.append(" ");
					}
					if (node.comment.length() > 0) {
						pgn.append("{");
						pgn.append(node.comment);
						pgn.append("} ");
					}
				}
			} else {
				isContinuous = false;
				pgn.append("(");
				pgn.append(node.getPGN());
				pgn.append(") ");
			}
		}
		return pgn.toString();
	}

	public void reset() {
		removeAllChildren();
		board.init();
		output = null;
		nags = new int[0];
		move = "";
		comment = "";
	}
	public GameNode(String move, VirtualBoard board) {
		super();
		this.move = move;
		this.board = board;
	}
}
