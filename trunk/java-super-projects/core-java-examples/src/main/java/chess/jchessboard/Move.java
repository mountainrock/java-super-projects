/* JChessBoard -- a chess game
 * Copyright (C) 2000 Claus Divossen <claus.divossen@gmx.de>
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

/* $Id: Move.java 5 2009-11-10 07:56:47Z cdivossen $ */

package chess.jchessboard;

/**
 * This class represents a chess move.
 */
public class Move {
	private int from;
	private int to;
	private boolean malformedString = false;
	private int pawnPromotion = VirtualBoard.NONE;

	public static String getVersion() {
		return "$Id: Move.java 5 2009-11-10 07:56:47Z cdivossen $";
	}

	/**
	 * Returns a string representation of this Move.
	 */
	public String toString() {
		String moveString =
			""
			+ (char) ((int) 'a' + (from & 7))
			+ ""
			+ ((from >> 3) + 1)
			+ (char) ((int) 'a' + (to & 7))
			+ ""
			+ ((to >> 3) + 1);
		if (pawnPromotion != VirtualBoard.NONE) {
			if (pawnPromotion == VirtualBoard.QUEEN)
				moveString += "Q";
			else if (pawnPromotion == VirtualBoard.ROOK)
				moveString += "R";
			else if (pawnPromotion == VirtualBoard.BISHOP)
				moveString += "B";
			else if (pawnPromotion == VirtualBoard.KNIGHT)
				moveString += "N";
		}
		return moveString;
	}

	/**
	 * Returns true if this move is valid. The from an to fields will be checked 
	 * to be inside of the board and the selected promotion has to be a valid choice.
	 * If this move was construction from an malformed string, it will return false.
	 */
	public boolean isValid() {
		return !malformedString
		&& from > 0
		&& from < 64
		&& to > 0
		&& to < 64
		&& (pawnPromotion == VirtualBoard.NONE
				|| pawnPromotion == VirtualBoard.QUEEN
				|| pawnPromotion == VirtualBoard.ROOK
				|| pawnPromotion == VirtualBoard.BISHOP
				|| pawnPromotion == VirtualBoard.KNIGHT);
	}

	/**
	 * Returns the number of the field this move starts from.
	 */
	public int fromField() {
		return from;
	}
	/**
	 * Returns the number of the field this move starts from.
	 */
	public int toField() {
		return to;
	}
	/**
	 * Returns the file of the field this move starts from.
	 */
	public int fromFile() {
		return (int) (from & 7);
	}
	/**
	 * Returns the rank of the field this move starts from.
	 */
	public int fromRank() {
		return (int) (from >> 3);
	}
	/**
	 * Returns the file of the field this move goes to.
	 */
	public int toFile() {
		return (int) (to & 7);
	}
	/**
	 * Returns the rank of the field this move goes to.
	 */
	public int toRank() {
		return (int) (to >> 3);
	}
	/**
	 * Returns the figure that a pawn would promote to.
	 */
	public int pawnPromotion() {
		return pawnPromotion;
	}
	/**
	 * Sets the figure a pawn would promote to.
	 */
	public void setPawnPromotion(int prom) {
		pawnPromotion = prom;
	}

	/**
	 * Creates an integer hash of this move.
	 */
	public int hashCode() {
		// Note: pawnPromotion is not considered here.
		return from | (to << 6);
	}

	/**
	 * Compares this move to the given move.
	 */
	public boolean equals(Move m) {
		return m.fromField() == from
		&& m.toField() == to
		&& m.pawnPromotion() == pawnPromotion
		&& m.isValid() == isValid();
	}

	/**
	 * Creates a new move from the given String representation. Only the simple form
	 * "a2a3" is accepted, not the algebraic notation.
	 * @param move
	 */
	public Move(String moveString) {
		if (moveString.length() == 4 || moveString.length() == 5) {
			from =
				((int) (((int) moveString.charAt(0) - (int) 'a')
						+ (((int) (Character.getNumericValue(moveString.charAt(1))
								- 1))
								<< 3)));
			to =
				((int) (((int) moveString.charAt(2) - (int) 'a')
						+ (((int) (Character.getNumericValue(moveString.charAt(3))
								- 1))
								<< 3)));
			if (moveString.length() == 5) {
				if (moveString.substring(4, 5).equals("Q"))
					pawnPromotion = VirtualBoard.QUEEN;
				else if (moveString.substring(4, 5).equals("R"))
					pawnPromotion = VirtualBoard.ROOK;
				else if (moveString.substring(4, 5).equals("B"))
					pawnPromotion = VirtualBoard.BISHOP;
				else if (moveString.substring(4, 5).equals("N"))
					pawnPromotion = VirtualBoard.KNIGHT;
				else
					malformedString = true;
			}
		} else
			malformedString = true;
	}

	/** 
	 * Creates a move, from field number to field number
	 * @param f from-field number
	 * @param t to-field number
	 */
	public Move(int f, int t) {
		from = f;
		to = t;
	}

	/** 
	 * Creates a move, from field number to field number
	 * @param f from-field number
	 * @param t to-field number
	 * @param _pawnPromotion what a promoted pawn shall become
	 */
	public Move(int f, int t, int _pawnPromotion) {
		from = f;
		to = t;
		pawnPromotion = _pawnPromotion;
	}

	/**
	 * Creates a move rank/file to rank/file.
	 */
	public Move(int fromRank, int fromFile, int toRank, int toFile) {
		from = (int) ((fromRank << 3) + fromFile);
		to = (int) ((toRank << 3) + toFile);
	}
}
