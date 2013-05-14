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

/* $Id: VirtualBoard.java 7 2009-11-15 18:58:42Z cdivossen $ */

package chess.jchessboard;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

class VirtualBoard {

	public static String getVersion() {
		return "$Id: VirtualBoard.java 7 2009-11-15 18:58:42Z cdivossen $";
	}

	static final int EMPTY_FIELD = 0;
	static final int EM = 0;
	static final int WHITE_KING = 1;
	static final int WK = 1;
	static final int WHITE_QUEEN = 2;
	static final int WQ = 2;
	static final int WHITE_ROOK = 3;
	static final int WR = 3;
	static final int WHITE_BISHOP = 4;
	static final int WB = 4;
	static final int WHITE_KNIGHT = 5;
	static final int WN = 5;
	static final int WHITE_PAWN = 6;
	static final int WP = 6;
	static final int BLACK_KING = 7;
	static final int BK = 7;
	static final int BLACK_QUEEN = 8;
	static final int BQ = 8;
	static final int BLACK_ROOK = 9;
	static final int BR = 9;
	static final int BLACK_BISHOP = 10;
	static final int BB = 10;
	static final int BLACK_KNIGHT = 11;
	static final int BN = 11;
	static final int BLACK_PAWN = 12;
	static final int BP = 12;
	static final int MAX_FIGURE_VALUE = 12;

	static final int NONE = 0;
	static final int KING = 1;
	static final int QUEEN = 2;
	static final int ROOK = 3;
	static final int BISHOP = 4;
	static final int KNIGHT = 5;
	static final int PAWN = 6;

	private static final int[] startPosition =
	{
		WR,	WN,	WB,	WQ,	WK,	WB,	WN,	WR,
		WP,	WP,	WP,	WP,	WP,	WP,	WP,	WP,
		EM,	EM,	EM,	EM,	EM,	EM, EM,	EM,
		EM,	EM,	EM,	EM,	EM,	EM,	EM,	EM,
		EM,	EM,	EM,	EM,	EM,	EM,	EM,	EM,
		EM,	EM,	EM,	EM,	EM,	EM,	EM,	EM,
		BP,	BP,	BP,	BP,	BP,	BP,	BP,	BP,
		BR,	BN,	BB,	BQ,	BK,	BB,	BN,	BR 
	};
	String[] name = { "-", "K", "Q", "R", "B", "N", "P", "k", "q", "r", "b", "n", "p" };
	protected int[] field = new int[64];
	protected boolean whiteKingHasMoved;
	protected boolean blackKingHasMoved;
	protected boolean leftBlackRookHasMoved;
	protected boolean rightBlackRookHasMoved;
	protected boolean leftWhiteRookHasMoved;
	protected boolean rightWhiteRookHasMoved;
	protected boolean isWhiteTurn;
	protected int blackKingPos, whiteKingPos;
	protected int lastFrom = 64; // 3 bits per rank/file
	protected int lastTo = 64; // (x>>3) gives rank, (x&7) gives file
	protected int pawnPromotion = NONE;
	protected boolean pawnHasPromoted = false;
	protected int enPassantTarget = 64;
	protected int halfmoveClock = 0;
	protected int fullmoveNumber = 0;
	// This is a cache to speed up makeMove(String algebraicMove);
	private boolean doNotConsiderCheckInTryMove = false;
	private static long[] knightMovesBitboards;
	private static long[] kingAttackBitboards=null;
	//	private static long[] pawnMovesBitboards;
	//	private static long[] pawnAttacksBitboards;
	private static long[][] horMoves; // Horizontal moves for the given [field][occupancy];	
	private static long[][] vertMoves; // Vertical oves for the given [field][vertical_occupancy];	
	private static long[][] diag1Moves; // Diagonal moves 	
	private static long[][] diag2Moves; // Diagonal moves 	
	public long whitePieces;
	public long blackPieces;
	private long whitePiecesRot; // Rotated
	private long blackPiecesRot;
	private long whitePiecesDiag1; // Rotated
	private long blackPiecesDiag1;
	private long whitePiecesDiag2; // Rotated
	private long blackPiecesDiag2;
	private long whiteAttackBoard = 0l;
	private long blackAttackBoard = 0l;
    // AI helpers
	public int score;
    public int movesCount=0;


    /*  Field numbering oder:
     * 
     *  8   56, 57, 58, 59, 60, 61, 62, 63,
     *  7   48, 49, 50, 51, 52, 53, 54, 55,
     *  6   40, 41, 42, 43, 44, 45, 46, 47,
     *  5   32, 33, 34, 35, 36, 37, 38, 39, 
     *  4   44, 25, 26, 27, 28, 29, 30, 31, 
     *  3   16, 17, 18, 19, 20, 21, 22, 23,
     *  2    8,  9, 10, 11, 12, 13, 14, 15,
     * 	1    0,  1,  2,  3,  4,  5,  6,  7
	 *
     *       a   b   c   d   e   f   g   h
     */

    private static int[] diag1length =
	{
		1,2,3,4,5,6,7,8,
		2,3,4,5,6,7,8,7,
		3,4,5,6,7,8,7,6,
		4,5,6,7,8,7,6,5,
		5,6,7,8,7,6,5,4,
		6,7,8,7,6,5,4,3,
		7,8,7,6,5,4,3,2,
		8,7,6,5,4,3,2,1,
	};
	
	/* Diagpos
    	0,1,2,3,4,5,6,7
    	0,1,2,3,4,5,6,6
    	0,1,2,3,4,5,5,5
    	0,1,2,3,4,4,4,4
    	0,1,2,3,3,3,3,3
    	0,1,2,2,2,2,2,2
    	0,1,1,1,1,1,1,1
        0,0,0,0,0,0,0,0

    	pos=(rank+file<=7)?rank:7-file;
	 */

	//	  Length:  (rank+file<=7)?rank+file+1:(7-rank)+(7-file)+1;

	private static int[] shift =
	{
		0,1,3,6,10,15,21,28,
		1,3,6,10,15,21,28,36,
		3,6,10,15,21,28,36,43,
		6,10,15,21,28,36,43,49,
		10,15,21,28,36,43,49,54,
		15,21,28,36,43,49,54,58,
		21,28,36,43,49,54,58,61,
		28,36,43,49,54,58,61,63 };

	private static int diag2length[] =
	{
		8,7,6,5,4,3,2,1,
		7,8,7,6,5,4,3,2,
		6,7,8,7,6,5,4,3,
		5,6,7,8,7,6,5,4,
		4,5,6,7,8,7,6,5,
		3,4,5,6,7,8,7,6,
		2,3,4,5,6,7,8,7,
		1,2,3,4,5,6,7,8 
	};

	private static int diag2pos[] =
	{
		0,0,0,0,0,0,0,0,
		0,1,1,1,1,1,1,1,
		0,1,2,2,2,2,2,2,
		0,1,2,3,3,3,3,3,
		0,1,2,3,4,4,4,4,
		0,1,2,3,4,5,5,5,
		0,1,2,3,4,5,6,6,
		0,1,2,3,4,5,6,7,
	};

	//	  

	/*	{
    0:   		0,
    1:   		1,8,
    3:  		2,9,16,
    6:   		3,10,17,24
    10: 		4,11,18,25,32,
    15: 		5,12,19,26,33,40,
    21: 		6,13,20,27,34,41,48,
    28: 		7,14,21,28,35,42,49,56,
    36:		   15,22,29,36,43,50,57
    43: 				23,30,37,44,51,58,
    49: 					 31,38,45,52,59,
    54: 						  39,46,53,60,
    58: 							   47,54,61,
    61: 									55,62,
    63: 										 63}*/

	// diag1Mapping enumerates the fields in the diagonal from the bottom to the left side
	private static int[] diag1Mapping =
	{
		0,
		1,8,
		2,9,16,
		3,10,17,24,
		4,11,18,25,32,
		5,12,19,26,33,40,
		6,13,20,27,34,41,48,
		7,14,21,28,35,42,49,56,
		15,22,29,36,43,50,57,
		23,30,37,44,51,58,
		31,38,45,52,59,
		39,46,53,60,
		47,54,61,
		55,62,
		63 
	};

	private static int[] diag1ReverseMapping; // No defined explicitly, will be derived from diag1Mapping;

	/*	Diag2
    	0:										   56,
    	1:									  48,57,
    	3:								 40,49,58,
    	6:							32,41,50,59,
    	10:				   24,33,42,51,60,
    	15:			  16,25,34,43,52,61,
    	21:		   8,17,26,35,44,53,62,
    	28:		0,9,18,27,36,45,54,63,
    	36:		1,10,19,28,37,46,55,
    	43:		2,11,20,29,38,47,
    	49:		3,12,21,30,39,
    	54:		4,13,22,31,
    	58:		5,14,23,
    	61:		6,15,
    	63:		7
	 */

	// diag2Mapping enumerates the fields in the diagonal from the left to the top side
	private static int[] diag2Mapping =
	{
		56,
		48,57,
		40,49,58,
		32,41,50,59,
		24,33,42,51,60,
		16,25,34,43,52,61,
		8,17,26,35,44,53,62,
		0,9,18,27,36,45,54,63,
		1,10,19,28,37,46,55,
		2,11,20,29,38,47,
		3,12,21,30,39,
		4,13,22,31,
		5,14,23,
		6,15,
		7 
	};
	private static int[] diag2ReverseMapping;

	private static int diag2shift[] =
	{
		28,36,43,49,54,58,61,63,
		21,28,36,43,49,54,58,61,
		15,21,28,36,43,49,54,58,
		10,15,21,28,36,43,49,54,
		6,10,15,21,28,36,43,49,
		3,6,10,15,21,28,36,43,
		1,3,6,10,15,21,28,36,
		0,1,3,6,10,15,21,28 
	};

	
	static class ImpossibleMoveException extends RuntimeException {
		public ImpossibleMoveException(String move) {
			super(move);
		}
	};

	public Move getLastMove() {
		if (lastFrom == 64 || lastTo == 64)
			return null;
		Move move = new Move(lastFrom, lastTo);
		if (pawnHasPromoted)
			move.setPawnPromotion(pawnPromotion);
		return move;
	}

	public boolean isWhiteTurn() {
		return isWhiteTurn;
	}
	public int getWhiteKingPos() {
		return whiteKingPos;
	}
	public int getBlackKingPos() {
		return blackKingPos;
	}
	public int getFullMoveNumber() {
		return fullmoveNumber;
	}

	public int getField(int rank, int file) {
		return field[(rank << 3) + (file & 7)];
	}

	public String toString() {
		String s = "\n";
		for (int r = 7; r >= 0; r--) {
			for (int f = 0; f < 8; f++)
				s += " " + name[field[(r << 3) + f]];
			s += "\n";
		}
		s += "Last move: " + getLastMove() + "\n";
		return s;
	}

	public static boolean isWhiteFigure(int f) {
		return f > 0 && f < 7;
	}

	public static boolean isBlackFigure(int f) {
		return f > 6 && f < 13;
	}

	public String getFEN() { // Forsyth-Edwards Notation
		String fen = "";
		int emptyFieldNo = 0;
		for (int rank = 7; rank >= 0; rank--) {
			for (int file = 0; file < 8; file++) {
				if (field[(rank << 3) + file] != EM && emptyFieldNo > 0) {
					fen += emptyFieldNo;
					emptyFieldNo = 0;
				}
				switch (field[(rank << 3) + file]) {
				case WP :
					fen += "P";
					break;
				case WB :
					fen += "B";
					break;
				case WN :
					fen += "N";
					break;
				case WR :
					fen += "R";
					break;
				case WQ :
					fen += "Q";
					break;
				case WK :
					fen += "K";
					break;
				case BP :
					fen += "p";
					break;
				case BB :
					fen += "b";
					break;
				case BN :
					fen += "n";
					break;
				case BR :
					fen += "r";
					break;
				case BQ :
					fen += "q";
					break;
				case BK :
					fen += "k";
					break;
				case EM :
					emptyFieldNo++;
				}
			}
			if (emptyFieldNo > 0)
				fen += emptyFieldNo;
			if (rank != 0)
				fen += "/";
			emptyFieldNo = 0;
		}

		fen += " ";

		fen += isWhiteTurn ? "w" : "b";

		fen += " ";

		boolean foundCastlingIndication = false;
		if (!whiteKingHasMoved && !rightWhiteRookHasMoved) {
			fen += "K";
			foundCastlingIndication = true;
		}
		if (!whiteKingHasMoved && !leftWhiteRookHasMoved) {
			fen += "Q";
			foundCastlingIndication = true;
		}
		if (!blackKingHasMoved && !rightBlackRookHasMoved) {
			fen += "k";
			foundCastlingIndication = true;
		}
		if (!blackKingHasMoved && !leftBlackRookHasMoved) {
			fen += "q";
			foundCastlingIndication = true;
		}
		if (!foundCastlingIndication)
			fen += "-";

		fen += " ";

		if (enPassantTarget < 64)
			fen += (char) ((int) 'a' + (enPassantTarget & 7)) + "" + ((enPassantTarget >> 3) + 1);
		else
			fen += "-";

		fen += " ";
		fen += halfmoveClock;
		fen += " ";
		fen += isWhiteTurn ? fullmoveNumber + 1 : fullmoveNumber;

		return fen;
	}

	public boolean initFromFEN(String fen) {
		int rank = 7;
		int file = 0;

		init();
		for (int n = 0; n < 64; n++)
			field[n] = EM;
		whiteKingPos = 64;
		blackKingPos = 64;

		java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(fen);
		if (!tokenizer.hasMoreTokens())
			return false;
		String token = tokenizer.nextToken();

		int index = 0;
		while (index < token.length() && token.charAt(index) != ' ') {
			char ch = token.charAt(index);
			switch (ch) {
			case 'P' :
				field[(rank << 3) + file] = WP;
				file++;
				break;
			case 'B' :
				field[(rank << 3) + file] = WB;
				file++;
				break;
			case 'N' :
				field[(rank << 3) + file] = WN;
				file++;
				break;
			case 'R' :
				field[(rank << 3) + file] = WR;
				file++;
				break;
			case 'Q' :
				field[(rank << 3) + file] = WQ;
				file++;
				break;
			case 'K' :
				field[(rank << 3) + file] = WK;
				whiteKingPos = ((rank << 3) + file);
				file++;
				break;
			case 'p' :
				field[(rank << 3) + file] = BP;
				file++;
				break;
			case 'b' :
				field[(rank << 3) + file] = BB;
				file++;
				break;
			case 'n' :
				field[(rank << 3) + file] = BN;
				file++;
				break;
			case 'r' :
				field[(rank << 3) + file] = BR;
				file++;
				break;
			case 'q' :
				field[(rank << 3) + file] = BQ;
				file++;
				break;
			case 'k' :
				field[(rank << 3) + file] = BK;
				blackKingPos = ((rank << 3) + file);
				file++;
				break;
			case '1' :
			case '2' :
			case '3' :
			case '4' :
			case '5' :
			case '6' :
			case '7' :
			case '8' :
				file += ((int) ch) - (int) '0';
				break;
			case '/' :
				rank--;
				file = 0;
				break;
			default :
				return false;
			}
			//            if(file>=8) return false;
			index++;
		}

		if (whiteKingPos == 64 || blackKingPos == 64)
			return false; // No game without them.

		if (!tokenizer.hasMoreTokens())
			return false;
		token = tokenizer.nextToken();

		if (token.equals("w"))
			isWhiteTurn = true;
		else if (token.equals("b"))
			isWhiteTurn = false;
		else
			return false;

		if (!tokenizer.hasMoreTokens())
			return false;
		token = tokenizer.nextToken();

		whiteKingHasMoved = true;
		rightWhiteRookHasMoved = true;
		leftWhiteRookHasMoved = true;
		blackKingHasMoved = true;
		rightBlackRookHasMoved = true;
		leftBlackRookHasMoved = true;

		index = 0;
		if (index < token.length() && token.charAt(index) == 'K') {
			whiteKingHasMoved = false;
			rightWhiteRookHasMoved = false;
			index++;
		}
		if (index < token.length() && token.charAt(index) == 'Q') {
			whiteKingHasMoved = false;
			leftWhiteRookHasMoved = false;
			index++;
		}
		if (index < token.length() && token.charAt(index) == 'k') {
			blackKingHasMoved = false;
			rightBlackRookHasMoved = false;
			index++;
		}
		if (index < token.length() && token.charAt(index) == 'q') {
			blackKingHasMoved = false;
			leftBlackRookHasMoved = false;
			index++;
		}

		if (index == 0)
			if (!token.equals("-"))
				return false;

		if (!tokenizer.hasMoreTokens())
			return false;
		token = tokenizer.nextToken();

		if (token.equals("-"))
			enPassantTarget = 64;
		else if (token.length() == 2)
			enPassantTarget = ((((int) token.charAt(0)) - (int) 'a') + ((((int) token.charAt(1)) - (int) '1') << 3));
		else
			return false;
		if (enPassantTarget > 64 || enPassantTarget < 0)
			return false;

		if (!tokenizer.hasMoreTokens())
			return false;
		token = tokenizer.nextToken();

		halfmoveClock = Integer.parseInt(token);

		if (!tokenizer.hasMoreTokens())
			return false;
		token = tokenizer.nextToken();

		fullmoveNumber = Integer.parseInt(token);
		if (isWhiteTurn)
			fullmoveNumber--;

		if (tokenizer.hasMoreTokens())
			return false;

		// Init the bitboards
		recalculateBitboards();
		return true;
	}

	/**
	 * Tries to give an algebraic representation of the move.
	 * Results might be ambiguous.
	 */
	private String algebraicDraft(Move move) {
		boolean capturing = false;
		//        boolean enPassant=false;
		String to = (char) ((int) 'a' + (move.toFile())) + "" + ((move.toRank()) + 1);
		String from = "";
		String promotion = "";

		if (field[move.fromField()] == WHITE_KING || field[move.fromField()] == BLACK_KING) {
			if (move.fromFile() == 4 && move.toFile() == 2)
				return "O-O-O";
			else if (move.fromFile() == 4 && move.toFile() == 6)
				return "O-O";
		}
		if ((field[move.fromField()] == WP && move.toRank() == 7)
				|| (field[move.fromField()] == BP && move.toRank() == 0)) {
			promotion = "=Q";
			if (move.pawnPromotion() == VirtualBoard.ROOK)
				promotion = "=R";
			else if (move.pawnPromotion() == VirtualBoard.BISHOP)
				promotion = "=B";
			else if (move.pawnPromotion() == VirtualBoard.KNIGHT)
				promotion = "=N";
		}

		if (field[move.toField()] != EMPTY_FIELD
				|| ((field[move.fromField()] == WP || field[move.fromField()] == BP) && move.toField() == enPassantTarget))
			capturing = true;
		//        if ((move.fromFile()!= move.toFile()) && 
		//            ((field[move.toField()]==EM && field[move.fromField()]==WP)  
		//            || (field[move.toField()]==EM && field[move.fromField()]==BP))) {
		//            enPassant=true;
		//        }

		if (field[move.fromField()] == WHITE_ROOK || field[move.fromField()] == BLACK_ROOK)
			from = "R";
		else if (field[move.fromField()] == WHITE_BISHOP || field[move.fromField()] == BLACK_BISHOP)
			from = "B";
		else if (field[move.fromField()] == WHITE_KNIGHT || field[move.fromField()] == BLACK_KNIGHT)
			from = "N";
		else if (field[move.fromField()] == WHITE_QUEEN || field[move.fromField()] == BLACK_QUEEN)
			from = "Q";
		else if (field[move.fromField()] == WHITE_KING || field[move.fromField()] == BLACK_KING)
			from = "K";
		// else if it's a pawn, it stays empty.
		if (capturing && (field[move.fromField()] == WHITE_PAWN || field[move.fromField()] == BLACK_PAWN)) {
			from = (char) ((int) 'a' + (move.fromField() & 7)) + "";
		}
		return from + ((capturing) ? "x" : "") + to +
		/*((enPassant)?"e.p.":"")+*/
		promotion;
	}

	private Hashtable getAllAlgebraicDrafts() {
		Hashtable table = new Hashtable();
		Iterator moveIter;
		synchronized (this) {
			//			doNotConsiderCheckInTryMove = true;
			// utest.pgn: Qe4 oder Q3e4? (Q5e4 ist eigentlich nicht moeglich.)
			moveIter = getAllMoves().iterator();
			//			doNotConsiderCheckInTryMove = false;
		}
		while (moveIter.hasNext()) {
			Move move = (Move) moveIter.next();
			table.put(move, algebraicDraft(move));
		}
		return table;
	}

	public String algebraic(Move move) {
		return algebraic(move, true);
	}

	public String algebraic(Move move, boolean indicateCheck) {
		if (!isPossibleMove(move))
			return null;

		Hashtable allDrafts = getAllAlgebraicDrafts();
		// Check uniqueness
		Move m = null;
		String alg = algebraicDraft(move);
		Enumeration keys = allDrafts.keys();
		boolean ambiguousRank = false;
		boolean ambiguousFile = false;
		boolean isAmbiguous = false;

		while (keys.hasMoreElements()) {
			m = (Move) keys.nextElement();
			if (alg.equals((String) allDrafts.get(m)) && !move.equals(m)) {
				isAmbiguous = true;
				if (move.fromFile() == m.fromFile())
					ambiguousFile = true;
				if (move.fromRank() == m.fromRank())
					ambiguousRank = true;
			}
		}
		if (isAmbiguous) {
			boolean addRank = false;
			boolean addFile = false;
			if (ambiguousRank && ambiguousFile) {
				addRank = true;
				addFile = true;
			} else if (ambiguousFile)
				addRank = true;
			else
				addFile = true;
			if (addRank) {
				String alg2 = alg.charAt(0) + "" + (move.fromRank() + 1) + alg.substring(1, alg.length());
				alg = alg2;
			}
			if (addFile) {
				String alg2 =
					alg.charAt(0) + "" + (char) ((int) 'a' + move.fromFile()) + "" + alg.substring(1, alg.length());
				alg = alg2;
			}
		}
		if (indicateCheck) {
			VirtualBoard vb = clonedBoard();
			try {
				vb.makeMove(move);
			} catch (ImpossibleMoveException e) {
			}
			// Check or checkmate indicator
			if ((vb.isWhiteTurn && vb.isAttackedByBlack(vb.whiteKingPos))
					|| (!vb.isWhiteTurn && vb.isAttackedByWhite(vb.blackKingPos))) {
				if (vb.gameIsFinished())
					alg += "#";
				else
					alg += "+";
			}
		}
		return alg;
	}

	public void makeMove(String algebraicMove) throws ImpossibleMoveException {
		String moveToMake;
		// Remove check indication
		if (algebraicMove.endsWith("+") || algebraicMove.endsWith("#"))
			moveToMake = algebraicMove.substring(0, algebraicMove.length() - 1);
		else
			moveToMake = algebraicMove;
		// Castling shortcut
		if (isWhiteTurn) {
			if (moveToMake.equals("O-O")) {
				makeMove(new Move(4, 6));
				return;
			} else if (moveToMake.equals("O-O-O")) {
				makeMove(new Move(4, 2));
				return;
			}
		} else {
			if (moveToMake.equals("O-O")) {
				makeMove(new Move(60, 62));
				return;
			} else if (moveToMake.equals("O-O-O")) {
				makeMove(new Move(60, 58));
				return;
			}
		}
		// Promotion
		pawnPromotion = NONE;
		String move2;
		if (moveToMake.length() < 2)
			throw new ImpossibleMoveException(algebraicMove);
		char lastChar = moveToMake.charAt(moveToMake.length() - 1);
		if (lastChar == 'Q' || lastChar == 'R' || lastChar == 'B' || lastChar == 'N') {
			if (lastChar == 'Q')
				pawnPromotion = QUEEN;
			else if (lastChar == 'R')
				pawnPromotion = ROOK;
			else if (lastChar == 'B')
				pawnPromotion = BISHOP;
			else if (lastChar == 'N')
				pawnPromotion = KNIGHT;
			// Remove promotion indicator and optional '=' :
			if (moveToMake.charAt(moveToMake.length() - 2) == '=')
				move2 = moveToMake.substring(0, moveToMake.length() - 2);
			else
				move2 = moveToMake.substring(0, moveToMake.length() - 1);
		} else
			move2 = moveToMake;

		int to =
			((int) (((int) move2.charAt(move2.length() - 2) - (int) 'a')
					+ (((int) (Character.getNumericValue(move2.charAt(move2.length() - 1)) - 1)) << 3)));
		char firstChar = move2.charAt(0);
		int figure;
		if (firstChar == 'N')
			figure = WN;
		else if (firstChar == 'B')
			figure = WB;
		else if (firstChar == 'R')
			figure = WR;
		else if (firstChar == 'Q')
			figure = WQ;
		else if (firstChar == 'K')
			figure = WK;
		else
			figure = WP;
		if (!isWhiteTurn)
			figure += 6;
		boolean capturing = (move2.indexOf('x') != -1);
		int fromFile = -1;
		int fromRank = -1;
		if (figure == WP || figure == BP) {
			if (move2.length() > 2)
				fromFile = (int) ((int) move2.charAt(0) - (int) 'a');
		} else {
			if (move2.length() > 3 + (capturing ? 1 : 0)) {
				fromFile = (int) ((int) move2.charAt(1) - (int) 'a');
				if (fromFile < 0 || fromFile > 7) {
					fromFile = -1;
					fromRank = Character.getNumericValue(move2.charAt(1)) - 1;
				}
			}
			if (move2.length() > 4 + (capturing ? 1 : 0))
				fromRank = Character.getNumericValue(move2.charAt(2)) - 1;
		}

		if (fromRank == -1 && fromFile == -1) {
			for (int from = 0; from < 64; from++) {
				if (field[from] == figure) {
					//    Move m = new Move(from,to);
					//    m.setPawnPromotion(pawnPromotion);
					if (isPossibleMove(from, to)) {
						move(from, to);
						return;
					}
				}
			}
		} else if (fromRank == -1) {
			if (fromFile < 0 || fromFile > 7)
				throw new ImpossibleMoveException(algebraicMove);
			for (int from = fromFile; from < 64; from += 8) {
				if (field[from] == figure) {
					//                    Move m = new Move(from,to);
					//                    m.setPawnPromotion(pawnPromotion);
					if (isPossibleMove(from, to)) {
						move(from, to);
						return;
					}
				}
			}
		} else if (fromFile == -1) {
			for (int from = fromRank * 8; from < (fromRank * 8) + 8; from++) {
				if (field[from] == figure) {
					//                    Move m = new Move(from,to);
					//                    m.setPawnPromotion(pawnPromotion);
					if (isPossibleMove(from, to)) {
						move(from, to);
						return;
					}
				}
			}
		} else {
			int from = (fromRank << 3) + fromFile;
			if (field[from] == figure) {
				//                Move m = new Move(from,to);
				//                m.setPawnPromotion(pawnPromotion);
				if (isPossibleMove(from, to)) {
					move(from, to);
					return;
				}
			}
		}
		throw new ImpossibleMoveException(algebraicMove);
	}

	public void inithorMoves() {
		horMoves = new long[64][256];
		int moves[][] = new int[8][256];
		for (int file = 0; file < 8; file++) {
			for (int occ = 0; occ < 256; occ++) {
				int n = file + 1;
				while (n < 8) {
					moves[file][occ] |= (1 << n);
					if ((occ & (1 << n)) != 0)
						break;
					n++;
				}
				n = file - 1;
				while (n >= 0) {
					moves[file][occ] |= (1 << n);
					if ((occ & (1 << n)) != 0)
						break;
					n--;
				}
			}
		}
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				for (int occ = 0; occ < 256; occ++) {
					horMoves[(rank << 3) + file][occ] = ((long) moves[file][occ]) << (rank * 8);
				}
			}
		}
	}

	public void initVertMoves() {
		vertMoves = new long[64][256];
		long moves[][] = new long[8][256];
		for (int rank = 0; rank < 8; rank++) {
			for (int occ = 0; occ < 256; occ++) {
				int n = rank + 1;
				while (n < 8) {
					moves[rank][occ] |= (1l << (n * 8));
					if ((occ & (1 << n)) != 0)
						break;
					n++;
				}
				n = rank - 1;
				while (n >= 0) {
					moves[rank][occ] |= (1l << (n * 8));
					if ((occ & (1 << n)) != 0)
						break;
					n--;
				}
			}
		}
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				for (int occ = 0; occ < 256; occ++) {
					vertMoves[(rank << 3) + file][occ] = moves[rank][occ] << file;
				}
			}
		}
	}

	public void initDiag1Moves() {
		diag1Moves = new long[64][256];
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				int diagLength = (rank + file <= 7) ? rank + file + 1 : (7 - rank) + (7 - file) + 1;
				int sh = shift[(rank << 3) + file];
				for (int occ = 0; occ < (1 << diagLength); occ++) {
					int n = ((rank + file <= 7) ? rank : 7 - file) + 1; // Position on diagonal
					while (n < diagLength) {
						diag1Moves[(rank << 3) + file][occ] |= 1l << diag1Mapping[sh + n];
						if ((occ & (1 << n)) != 0)
							break;
						n++;
					}
					n = ((rank + file <= 7) ? rank : 7 - file) - 1; // Position on diagonal
					while (n >= 0) {
						diag1Moves[(rank << 3) + file][occ] |= 1l << diag1Mapping[sh + n];
						if ((occ & (1 << n)) != 0)
							break;
						n--;
					}
					/*                    if (rank == 0 && file == 5)
                                            System.out.println(
                                                "Diag1moves rank "
                                                    + rank
                                                    + " file "
                                                    + file
                                                    + ", lenght "
                                                    + diagLength
                                                    + ", occ: "
                                                    + binaryPresentation((byte) occ)
                                                    + ":\n"
                                                    + binaryPresentation(diag1Moves[(rank << 3) + file][occ]));
					 */
				}
			}
		}
	}

	public void initDiag2Moves() {
		diag2Moves = new long[64][256];
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				int diagLength = diag2length[(rank << 3) + file];
				int sh = diag2shift[(rank << 3) + file];
				for (int occ = 0; occ < (1 << diagLength); occ++) {
					int n = diag2pos[(rank << 3) + file] + 1;
					while (n < diagLength) {
						diag2Moves[(rank << 3) + file][occ] |= 1l << diag2Mapping[sh + n];
						if ((occ & (1 << n)) != 0)
							break;
						n++;
					}
					n = diag2pos[(rank << 3) + file] - 1;
					while (n >= 0) {
						diag2Moves[(rank << 3) + file][occ] |= 1l << diag2Mapping[sh + n];
						if ((occ & (1 << n)) != 0)
							break;
						n--;
					}
				}
			}
		}
	}

	public void clearBitboardField(int n) {
		whitePieces &= ~(1l << n);
		blackPieces &= ~(1l << n);
		whitePiecesRot &= ~(1l << ( ((n&7)<<3) + ((n&56)>>3)) );
		blackPiecesRot &= ~(1l << ( ((n&7)<<3) + ((n&56)>>3)) );
		whitePiecesDiag1 &= ~(1l << diag1ReverseMapping[n]);
		blackPiecesDiag1 &= ~(1l << diag1ReverseMapping[n]);
		whitePiecesDiag2 &= ~(1l << diag2ReverseMapping[n]);
		blackPiecesDiag2 &= ~(1l << diag2ReverseMapping[n]);
	}

	public void setWhiteBitboardField(int n) {
		whitePieces |= (1l << n);
		whitePiecesRot |= 1l << ( ((n&7)<<3) + ((n&56)>>3) );
		whitePiecesDiag1 |= 1l << diag1ReverseMapping[n];
		whitePiecesDiag2 |= 1l << diag2ReverseMapping[n];
	}

	public void setBlackBitboardField(int n) {
		blackPieces |= (1l << n);
		blackPiecesRot |= 1l << ( ((n&7)<<3) + ((n&56)>>3) );
		blackPiecesDiag1 |= 1l << diag1ReverseMapping[n];
		blackPiecesDiag2 |= 1l << diag2ReverseMapping[n];
	}

	public void recalculateBitboards() {
		whitePieces = 0;
		whitePiecesRot = 0;
		whitePiecesDiag1 = 0;
		whitePiecesDiag2 = 0;
		blackPieces = 0;
		blackPiecesRot = 0;
		blackPiecesDiag1 = 0;
		blackPiecesDiag2 = 0;
		for (int x = 0; x < 64; x++) {
			if (isWhiteFigure(field[x])) {
				setWhiteBitboardField(x);
			}
			if (isBlackFigure(field[x])) {
				setBlackBitboardField(x);
			}
		}
	}

	public VirtualBoard() {
		if (knightMovesBitboards == null) {
			initKnightMoves();
			initKingAttackBitboards();
			inithorMoves();
			initVertMoves();
			initDiag1Moves();
			diag1ReverseMapping = new int[64];
			for (int x = 0; x < 64; x++)
				diag1ReverseMapping[diag1Mapping[x]] = x;
			initDiag2Moves();
			diag2ReverseMapping = new int[64];
			for (int x = 0; x < 64; x++)
				diag2ReverseMapping[diag2Mapping[x]] = x;
		}
	}

	public List getAllMoves() {
		List list = new Vector();

		for (int n = 0; n < 64; n++) {
			if ((isWhiteTurn && isWhiteFigure(field[n]) || (!isWhiteTurn && isBlackFigure(field[n])))) {
				if (field[n] == WP) {
					if ((n >> 3) == 6) { // A white pawn is going to promote
						if (isPossibleMove(n, n + 8)) {
							list.add(new Move(n, n + 8, QUEEN));
							list.add(new Move(n, n + 8, ROOK));
							list.add(new Move(n, n + 8, BISHOP));
							list.add(new Move(n, n + 8, KNIGHT));
						}
						if (isPossibleMove(n, n + 7)) { // It might capture something doing so
							list.add(new Move(n, n + 7, QUEEN));
							list.add(new Move(n, n + 7, ROOK));
							list.add(new Move(n, n + 7, BISHOP));
							list.add(new Move(n, n + 7, KNIGHT));
						}
						if (isPossibleMove(n, n + 9)) { // To left and to right
							list.add(new Move(n, n + 9, QUEEN));
							list.add(new Move(n, n + 9, ROOK));
							list.add(new Move(n, n + 9, BISHOP));
							list.add(new Move(n, n + 9, KNIGHT));
						}
					} else {
						if (((n >> 3) == 1) && (isPossibleMove(n, n + 16))) {
							list.add(new Move(n, n + 16));
						}
						if (isPossibleMove(n, n + 8)) {
							list.add(new Move(n, n + 8));
						}
						if (isPossibleMove(n, n + 7)) {
							list.add(new Move(n, n + 7));
						}
						if (isPossibleMove(n, n + 9)) {
							list.add(new Move(n, n + 9));
						}
					}
				} else if (field[n] == BP) {
					if ((n >> 3) == 1) { // A black pawn is going to promote
						if (isPossibleMove(n, n - 8)) {
							list.add(new Move(n, n - 8, QUEEN));
							list.add(new Move(n, n - 8, ROOK));
							list.add(new Move(n, n - 8, BISHOP));
							list.add(new Move(n, n - 8, KNIGHT));
						}
						if (isPossibleMove(n, n - 7)) { // It might capture something doing so
							list.add(new Move(n, n - 7, QUEEN));
							list.add(new Move(n, n - 7, ROOK));
							list.add(new Move(n, n - 7, BISHOP));
							list.add(new Move(n, n - 7, KNIGHT));
						}
						if (isPossibleMove(n, n - 9)) { // To left and to right
							list.add(new Move(n, n - 9, QUEEN));
							list.add(new Move(n, n - 9, ROOK));
							list.add(new Move(n, n - 9, BISHOP));
							list.add(new Move(n, n - 9, KNIGHT));
						}
					} else {
						if ((n >> 3 == 6) && (isPossibleMove(n, n - 16))) {
							list.add(new Move(n, n - 16));
						}
						if (isPossibleMove(n, n - 8)) {
							list.add(new Move(n, n - 8));
						}
						if (isPossibleMove(n, n - 7)) {
							list.add(new Move(n, n - 7));
						}
						if (isPossibleMove(n, n - 9)) {
							list.add(new Move(n, n - 9));
						}
					}
				} else { 
					for (int to = 0; to < 64; to++) {
						if (isPossibleMove(n, to)) {
							list.add(new Move(n, to));
						}
					}
				}
			}
		}
		movesCount=list.size();
		return list;
	}

	public List simpleGetAllMoves() {
		List list = new Vector();
		for (int n = 0; n < 64; n++) {
			for (int m = 0; m < 64; m++) {
				if (isPossibleMove(n, m)) {
					list.add(new Move(n, m));
				}
			}
		}
		movesCount=list.size();
		return list;
	}

	public int abs(int a) {
		return (int) ((a < 0) ? -a : a);
	}

	public void clear() {
		for (int n = 0; n < 64; n++)
			field[n] = EMPTY_FIELD;
		isWhiteTurn = true;
		whiteKingHasMoved = false;
		blackKingHasMoved = false;
		leftBlackRookHasMoved = false;
		rightBlackRookHasMoved = false;
		leftWhiteRookHasMoved = false;
		rightWhiteRookHasMoved = false;
		lastFrom = 64; // not a valid Position!
		lastTo = 64;
		enPassantTarget = 64;
		halfmoveClock = 0;
		fullmoveNumber = 0;
		whitePieces = 0;
		whitePiecesRot = 0;
		whitePiecesDiag1 = 0;
		whitePiecesDiag2 = 0;
		blackPieces = 0;
		blackPiecesRot = 0;
		blackPiecesDiag1 = 0;
		blackPiecesDiag2 = 0;
		movesCount = 0;
	}

	public void init() {
		// Initializes the board ("new game")
		clear();
		for (int n = 0; n < 64; n++)
			field[n] = startPosition[n];
		blackKingPos = (7 << 3) + 4;
		whiteKingPos = (0 << 3) + 4;
		recalculateBitboards();
	}

	public VirtualBoard clonedBoard() {
		VirtualBoard newBoard = new VirtualBoard();
		//for(int n=0; n<64; n++)
		//    newBoard.field[n]=field[n];
		System.arraycopy(field, 0, newBoard.field, 0, 64);
		newBoard.isWhiteTurn = isWhiteTurn;
		newBoard.whiteKingHasMoved = whiteKingHasMoved;
		newBoard.blackKingHasMoved = blackKingHasMoved;
		newBoard.leftBlackRookHasMoved = leftBlackRookHasMoved;
		newBoard.rightBlackRookHasMoved = rightBlackRookHasMoved;
		newBoard.leftWhiteRookHasMoved = leftWhiteRookHasMoved;
		newBoard.rightWhiteRookHasMoved = rightWhiteRookHasMoved;
		newBoard.lastFrom = lastFrom;
		newBoard.lastTo = lastTo;
		newBoard.enPassantTarget = enPassantTarget;
		newBoard.blackKingPos = blackKingPos;
		newBoard.whiteKingPos = whiteKingPos;
		newBoard.pawnPromotion = pawnPromotion;
		newBoard.pawnHasPromoted = pawnHasPromoted;
		newBoard.fullmoveNumber = fullmoveNumber;
		newBoard.halfmoveClock = halfmoveClock;
		newBoard.whitePieces = whitePieces;
		newBoard.blackPieces = blackPieces;
		newBoard.whitePiecesRot = whitePiecesRot;
		newBoard.blackPiecesRot = blackPiecesRot;
		newBoard.whitePiecesDiag1 = whitePiecesDiag1;
		newBoard.blackPiecesDiag1 = blackPiecesDiag1;
		newBoard.whitePiecesDiag2 = whitePiecesDiag2;
		newBoard.blackPiecesDiag2 = blackPiecesDiag2;
		newBoard.whiteAttackBoard = whiteAttackBoard;
		newBoard.blackAttackBoard = blackAttackBoard;
		return newBoard;
	}

	public boolean equals(VirtualBoard vb) {
		for (int n = 0; n < 64; n++)
			if (field[n] != vb.field[n])
				return false;
		return isWhiteTurn == vb.isWhiteTurn
		&& whiteKingHasMoved == vb.whiteKingHasMoved
		&& blackKingHasMoved == vb.blackKingHasMoved
		&& leftBlackRookHasMoved == vb.leftBlackRookHasMoved
		&& rightBlackRookHasMoved == vb.rightBlackRookHasMoved
		&& leftWhiteRookHasMoved == vb.leftWhiteRookHasMoved
		&& rightWhiteRookHasMoved == vb.rightWhiteRookHasMoved
		&& lastFrom == vb.lastFrom
		&& lastTo == vb.lastTo
		&& enPassantTarget == vb.enPassantTarget
		&& blackKingPos == vb.blackKingPos
		&& whiteKingPos == vb.whiteKingPos
		&& pawnPromotion == vb.pawnPromotion
		&& pawnHasPromoted == vb.pawnHasPromoted
		&& fullmoveNumber == vb.fullmoveNumber
		&& halfmoveClock == vb.halfmoveClock;
	}
	public boolean isEqualPosition(VirtualBoard vb) {
		// Whether rooks or kings have moved is not considered here.
		if (whitePieces != vb.whitePieces || blackPieces != vb.blackPieces)
			return false;
		for (int n = 0; n < 64; n++)
			if (field[n] != vb.field[n])
				return false;
		return true;
	}

	public void makeMove(Move move) throws ImpossibleMoveException {
		pawnPromotion = move.pawnPromotion();
		if (isPossibleMove(move))
			move(move.fromField(), move.toField());
		else
			throw new ImpossibleMoveException(move + "");
	}

	public void makeAnyMove(Move move) {
		pawnPromotion = move.pawnPromotion();
		move(move.fromField(), move.toField());
	}

	public boolean isAttackedByWhite(int f) {
		// A field is attacked, even if the attacker would leave his king in check.
		if (whiteAttackBoard != 0) {
			// Haha, this is faster. But only if it is present, already.
			return (whiteAttackBoard & (1l << f)) != 0;
		}
		// Otherwise, it is faster to check the how the existing black pieces could move.
		long toBoard = 1l << f;
		for (int x = 0; x < 64; x++) {
			if (field[x] != EM) {
				switch (field[x]) {
				case WP :
					if (WPmightAttack(x >> 3, x & 7, f >> 3, f & 7))
						return true;
					break;
				case WK :
					if ((kingAttackBitboards[x] & toBoard) != 0)
						return true;
					break;
				case WN :
					if ((knightMovesBitboards[x] & toBoard) != 0)
						return true;
					break;
				case WQ :
					if (WQmightMove(x >> 3, x & 7, f >> 3, f & 7))
						return true;
					break;
				case WR :
					if (WRmightMove(x >> 3, x & 7, f >> 3, f & 7))
						return true;
					break;
				case WB :
					if (WBmightMove(x >> 3, x & 7, f >> 3, f & 7))
						return true;
				}
			}
		}
		return false;
	}

	public boolean isAttackedByBlack(int f) {
		if (blackAttackBoard != 0) {
			// Haha, this is faster. But only if it is present, already.
			return (blackAttackBoard & (1l << f)) != 0;
		}
		long toBoard = 1l << f;
		for (int x = 0; x < 64; x++) {
			if (x != f && field[x] != EM) {
				switch (field[x]) {
				case BP :
					if (BPmightAttack(x >> 3, x & 7, f >> 3, f & 7))
						return true;
					break;
				case BK :
					if ((kingAttackBitboards[x] & toBoard) != 0)
						return true;
					break;
				case BN :
					if ((knightMovesBitboards[x] & toBoard) != 0)
						return true;
					break;
				case BQ :
					if (BQmightMove(x >> 3, x & 7, f >> 3, f & 7))
						return true;
					break;
				case BR :
					if (BRmightMove(x >> 3, x & 7, f >> 3, f & 7))
						return true;
					break;
				case BB :
					if (BBmightMove(x >> 3, x & 7, f >> 3, f & 7))
						return true;
				}
			}
		}
		return false;
	}

	/*
	 * Which fields could a queen move to or attack, from the given field, regardless of colors
	 */
	public long getQueenAttackBoard(int field) {
		long attackBoard = 0;
		int vert_occupancy, diag1occupancy, diag2occupancy, occupancy;
		occupancy = (int) (((whitePieces | blackPieces) >> (field & 56))) & 255;
		attackBoard |= horMoves[field][occupancy];
		vert_occupancy = (int) (((whitePiecesRot | blackPiecesRot) >> ((field & 7) * 8))) & 255;
		attackBoard |= vertMoves[field][vert_occupancy];
		diag1occupancy =
			(int) ((whitePiecesDiag1 | blackPiecesDiag1) >> shift[field]) & ((1 << diag1length[field]) - 1);
		attackBoard |= diag1Moves[field][diag1occupancy];
		diag2occupancy =
			(int) ((whitePiecesDiag2 | blackPiecesDiag2) >> diag2shift[field])
			& ((1 << diag2length[field]) - 1);
		attackBoard |= diag2Moves[field][diag2occupancy];
		return attackBoard;
	}

	public long getWhiteAttackBoard() {
		if (whiteAttackBoard != 0)
			return whiteAttackBoard; // Shortcut, it's already calculated.

		long attackBoard = 0;
		int vert_occupancy, diag1occupancy, diag2occupancy, occupancy;
		for (int x = 0; x < 64; x++) {

			if (field[x] != EM) {
				switch (field[x]) {
				case WP :
					int rank = x >> 3;
					int file = x & 7;
					if ((file < 7) && (rank < 7))
						attackBoard |= 1l << ((rank + 1) * 8 + file + 1);
					if ((file > 0) && (rank < 7))
						attackBoard |= 1l << ((rank + 1) * 8 + file - 1);
					break;
				case WK :
					attackBoard |= kingAttackBitboards[x];
					break;
				case WN :
					attackBoard |= knightMovesBitboards[x];
					break;
				case WQ :
					attackBoard |= getQueenAttackBoard(x);
					break;
				case WR :
					occupancy = (int) (((whitePieces | blackPieces) >> (x & 56))) & 255;
					attackBoard |= horMoves[x][occupancy];
					vert_occupancy = (int) (((whitePiecesRot | blackPiecesRot) >> ((x & 7) * 8))) & 255;
					attackBoard |= vertMoves[x][vert_occupancy];
					break;
				case WB :
					diag1occupancy =
						(int) ((whitePiecesDiag1 | blackPiecesDiag1) >> shift[x]) & ((1 << diag1length[x]) - 1);
					attackBoard |= diag1Moves[x][diag1occupancy];
					diag2occupancy =
						(int) ((whitePiecesDiag2 | blackPiecesDiag2) >> diag2shift[x])
						& ((1 << diag2length[x]) - 1);
					attackBoard |= diag2Moves[x][diag2occupancy];
				}
			}
		}
		//		System.out.println("WhitePieces:\n"+binaryPresentation(whitePieces)+"\n");
		//		System.out.println("WhitePiecesRot:\n"+binaryPresentation(whitePiecesRot)+"\n");
		//        System.out.println("WhiteAttacks:\n" + binaryPresentation(attackBoard & ~whitePieces));
		whiteAttackBoard = attackBoard ; // & ~whitePieces;
		return whiteAttackBoard;
	}

	public long getBlackAttackBoard() {
		if (blackAttackBoard != 0)
			return blackAttackBoard; // Shortcut, it's already calculated.
		int vert_occupancy, diag1occupancy, diag2occupancy, occupancy;
		long attackBoard = 0;
		for (int x = 0; x < 64; x++) {
			if (field[x] != EM) {
				switch (field[x]) {
				case BP :
					int rank = x >> 3;
					int file = x & 7;
					if ((file < 7) && (rank > 0))
						attackBoard |= 1l << ((rank - 1) * 8 + file + 1);
					if (file > 0 && (rank > 0))
						attackBoard |= 1l << ((rank - 1) * 8 + file - 1);
					break;
				case BK :
					attackBoard |= kingAttackBitboards[x];
					break;
				case BN :
					attackBoard |= knightMovesBitboards[x];
					break;
				case BQ :
					attackBoard |= getQueenAttackBoard(x);
					break;
				case BR :
					occupancy = (int) (((whitePieces | blackPieces) >> (x & 56))) & 255;
					attackBoard |= horMoves[x][occupancy];	
					vert_occupancy = (int) (((whitePiecesRot | blackPiecesRot) >> ((x & 7) * 8))) & 255;
					attackBoard |= vertMoves[x][vert_occupancy];
					break;
				case BB :
					diag1occupancy =
						(int) ((whitePiecesDiag1 | blackPiecesDiag1) >> shift[x]) & ((1 << diag1length[x]) - 1);
					attackBoard |= diag1Moves[x][diag1occupancy];
					diag2occupancy =
						(int) ((whitePiecesDiag2 | blackPiecesDiag2) >> diag2shift[x])
						& ((1 << diag2length[x]) - 1);
					attackBoard |= diag2Moves[x][diag2occupancy];
				}
			}
		}
/* Used for bitboard verifcation
		long simpleAttackBoard = 0;
		// A very simple way:
		for (int x = 0; x < 64; x++) {
			if (isAttackedByBlack(x))
				simpleAttackBoard|= 1l << x;
		}
		if(attackBoard != simpleAttackBoard) {
			System.out.println("Simple:\n"+binaryPresentation(simpleAttackBoard));
			System.out.println("Fast:\n"+binaryPresentation(attackBoard));
			System.out.println("Board was:\n"+this);
			System.out.println("BlackPieces:\n"+binaryPresentation(blackPieces)+"\n");
			System.out.println("BlackPiecesRot:\n"+binaryPresentation(blackPiecesRot)+"\n");
			System.out.println("BlackPiecesDiag1:\n"+binaryPresentation(blackPiecesDiag1)+"\n");
			System.out.println("BlackPiecesDiag2:\n"+binaryPresentation(blackPiecesDiag2)+"\n");
			System.out.println("WhitePieces:\n"+binaryPresentation(whitePieces)+"\n");
		}
*/
		blackAttackBoard = attackBoard ; //& ~blackPieces;
		return blackAttackBoard;
	}

	public boolean isPossibleMove(Move move) {
		return isPossibleMove(move.fromField(), move.toField());
	}

	public boolean isPossibleMove(int from, int to) {
		if (field[from] != EM && (isWhiteTurn == isWhiteFigure(field[from])) && mightBePossibleMove(from, to)) {
			if (doNotConsiderCheckInTryMove) {
				return true;
			}
			VirtualBoard nb = VirtualBoard.this.clonedBoard();
			//nb= new board
			nb.move(from, to);
			if (isWhiteTurn) {
				if (nb.whiteKingPos < 64 && nb.whiteKingPos >= 0 && !nb.isAttackedByBlack(nb.whiteKingPos))
					return true;
			} else if (nb.blackKingPos < 64 && nb.blackKingPos >= 0 && !nb.isAttackedByWhite(nb.blackKingPos))
				return true;
		}
		return false;
	}

	public VirtualBoard tryMove(int from, int to) {
		if (field[from] != EM && (isWhiteTurn == isWhiteFigure(field[from])) && mightBePossibleMove(from, to)) {
			VirtualBoard nb = VirtualBoard.this.clonedBoard();
			//nb= new board
			nb.move(from, to);
			if (doNotConsiderCheckInTryMove)
				return nb;
			if (isWhiteTurn) {
				if (nb.whiteKingPos < 64 && nb.whiteKingPos >= 0 && !nb.isAttackedByBlack(nb.whiteKingPos))
					return nb;
			} else if (nb.blackKingPos < 64 && nb.blackKingPos >= 0 && !nb.isAttackedByWhite(nb.blackKingPos))
				return nb;
		}
		return null;
	}

	public boolean onlyKingsLeft() {
		for (int n = 0; n < 64; n++)
			if (field[n] != EM && field[n] != WK && field[n] != BK)
				return false;
		return true;
	}

	public boolean gameIsFinished() {
		// Looks for possible moves for current player. Returns immediatly
		// if a move was found, and thus the game cannot be finished.
		// If there are only the kings left, the game is inevitable
		// drawn.
		if (onlyKingsLeft())
			return true;
		for (int from = 0; from < 64; from++)
			if (field[from] > 0 && (isWhiteTurn == (field[from] < 7)))
				for (int to = 0; to < 64; to++)
					if (isPossibleMove(new Move(from, to)))
						return false;
		return true;
	}

	/*
	 * move
	 * 
	 * This in the core method, where the moves finally are processed.
	 * 
	 * @author cd
	 *
	 */
	private void move(int from, int to) {
		// Moves that arrive this point have been checked 
		// to be valid.
		int file = from & 7;
		int rank = from >> 3;
		int newFile = to & 7;
		int newRank = to >> 3;
		pawnHasPromoted = false;
		boolean capturing = false;

		if (isWhiteTurn)
			fullmoveNumber++;

		if (file == 4) {
			if ((field[from] == WK) && file == 4 && newFile == 2) {
				// White castles queen side:
				move(0, 3);
				isWhiteTurn = !isWhiteTurn;
				if (isWhiteTurn)
					fullmoveNumber--;
			} else if ((field[from] == WK) && file == 4 && newFile == 6) {
				// White castles king side:
				move(7, 5);
				isWhiteTurn = !isWhiteTurn;
				if (isWhiteTurn)
					fullmoveNumber--;
			} else if ((field[from] == BK) && file == 4 && newFile == 2) {
				// Black castles queen side:
				move(((7 << 3) + 0), ((7 << 3) + 3));
				isWhiteTurn = !isWhiteTurn;
				if (isWhiteTurn)
					fullmoveNumber--;
			} else if ((field[from] == BK) && file == 4 && newFile == 6) {
				// Black castles king side:
				move(((7 << 3) + 7), ((7 << 3) + 5));
				isWhiteTurn = !isWhiteTurn;
				if (isWhiteTurn)
					fullmoveNumber--;
			}
		}
		if ((field[to] == EM && field[from] == WP) && file != newFile) {
			// Capturing en passant (white):
			field[(4 << 3) + newFile] = EM;
			clearBitboardField((4 << 3) + newFile);
			capturing = true;
		} else if ((field[to] == EM && field[from] == BP) && file != newFile) {
			// Capturing en passant (black):
			field[(3 << 3) + newFile] = EM;
			clearBitboardField((3 << 3) + newFile);
			capturing = true;
		}
		if (field[to] == WK) //Oops!
			whiteKingPos = 64; // Illegal field!
		else if (field[to] == BK)
			blackKingPos = 64;
		// No, thats not a joke. Its required for move checking.
		if (field[from] == WR && from == 0)
			leftWhiteRookHasMoved = true;
		else if (field[from] == WR && from == 7)
			rightWhiteRookHasMoved = true;
		else if (field[from] == BR && from == (7 << 3))
			leftBlackRookHasMoved = true;
		else if (field[from] == BR && from == (7 << 3) + 7)
			rightBlackRookHasMoved = true;
		else if (field[from] == WK) {
			whiteKingHasMoved = true;
			whiteKingPos = to;
		} else if (field[from] == BK) {
			blackKingHasMoved = true;
			blackKingPos = to;
		}
		if (to == 0)
			leftWhiteRookHasMoved = true;
		else if (to == 7)
			rightWhiteRookHasMoved = true;
		else if (to == 56)
			leftBlackRookHasMoved = true;
		else if (to == 63)
			rightBlackRookHasMoved = true;

		if (field[to] != EM)
			capturing = true;

		// Halfmove clock
		if (field[from] == WP || field[from] == BP || capturing)
			halfmoveClock = 0;
		else
			halfmoveClock++;

		//Set up enPassantTarget:
		if (field[from] == WP && rank == 1 && newRank == 3)
			enPassantTarget = ((2 << 3) + file);
		else if (field[from] == BP && rank == 6 && newRank == 4)
			enPassantTarget = ((5 << 3) + file);
		else
			enPassantTarget = 64;

		// Update the bitboards
		clearBitboardField(from);
		clearBitboardField(to);
		if (isWhiteFigure(field[from])) {
			setWhiteBitboardField(to);
		}
		if (isBlackFigure(field[from])) {
			setBlackBitboardField(to);
		}

		// Clear the attack boards, they have to be recalculated when needed.
		whiteAttackBoard = 0;
		blackAttackBoard = 0;

		// Update the array board
		field[to] = field[from];
		field[from] = EM;
		lastFrom = from;
		lastTo = to;
		isWhiteTurn = !isWhiteTurn;

		// Pawn promotion:
		if (field[to] == WP && newRank == 7) {
			if (pawnPromotion == NONE) {
				pawnPromotion = QUEEN;
				field[to] = WHITE_QUEEN;
			} else if (pawnPromotion == QUEEN)
				field[to] = WHITE_QUEEN;
			else if (pawnPromotion == ROOK)
				field[to] = WHITE_ROOK;
			else if (pawnPromotion == BISHOP)
				field[to] = WHITE_BISHOP;
			else if (pawnPromotion == KNIGHT)
				field[to] = WHITE_KNIGHT;
			pawnHasPromoted = true;
		} else if (field[to] == BP && newRank == 0) {
			if (pawnPromotion == NONE) {
				pawnPromotion = QUEEN;
				field[to] = BLACK_QUEEN;
			} else if (pawnPromotion == QUEEN)
				field[to] = BLACK_QUEEN;
			else if (pawnPromotion == ROOK)
				field[to] = BLACK_ROOK;
			else if (pawnPromotion == BISHOP)
				field[to] = BLACK_BISHOP;
			else if (pawnPromotion == KNIGHT)
				field[to] = BLACK_KNIGHT;
			pawnHasPromoted = true;
		}
		//            		System.out.println("From: "+from+", To: "+to);
		//            		System.out.println("WhitePiecesDiag1:\n"+binaryPresentation(whitePiecesDiag1));
		//            		System.out.println("BlackPiecesDiag1:\n"+binaryPresentation(blackPiecesDiag1)+"\n");
	}

	//
	// Move possibilities:
	//
	public boolean mightBePossibleMove(int from, int to) {
		return mightBePossibleMove(from >> 3, from & 7, to >> 3, to & 7);
	}

	private void initKnightMoves() {
		knightMovesBitboards = new long[64];
		for (int from = 0; from < 64; from++) {
			for (int to = 0; to < 64; to++) {
				if (from != to && WNmightMove(from >> 3, from & 7, to >> 3, to & 7)) {
					knightMovesBitboards[from] |= 1l << to;
				}
			}
		}
	}

	private void initKingAttackBitboards() {
		kingAttackBitboards = new long[64];
		for (int from = 0; from < 64; from++) {
			for (int to = 0; to < 64; to++) {
				if (from != to && WKmightAttack(from >> 3, from & 7, to >> 3, to & 7)) {
					kingAttackBitboards[from] |= 1l << to;
				}
			}
		}
	}

	public boolean mightBePossibleMove(int fromRank, int fromFile, int toRank, int toFile) {
		// Might return true even if the King is left in check.
		if (fromRank < 0
				|| fromFile < 0
				|| fromRank > 7
				|| fromFile > 7
				|| toRank < 0
				|| toFile < 0
				|| toRank > 7
				|| toFile > 7)
			return false;

		int fromField = getField(fromRank, fromFile);
		if (fromField == EM || (fromRank == toRank && fromFile == toFile))
			return false;

		int toField = getField(toRank, toFile);
		// No capturing of own figures:
		if (isWhiteFigure(fromField) && isWhiteFigure(toField))
			return false;
		if (isBlackFigure(fromField) && isBlackFigure(toField))
			return false;
		switch (fromField) {
		case WK :
			return WKmightMove(fromRank, fromFile, toRank, toFile);
		case WQ :
			return WQmightMove(fromRank, fromFile, toRank, toFile);
		case WR :
			return WRmightMove(fromRank, fromFile, toRank, toFile);
		case WB :
			return WBmightMove(fromRank, fromFile, toRank, toFile);
		case WN :
			return WNmightMove(fromRank, fromFile, toRank, toFile);
		case WP :
			return WPmightMove(fromRank, fromFile, toRank, toFile);
		case BK :
			return BKmightMove(fromRank, fromFile, toRank, toFile);
		case BQ :
			return BQmightMove(fromRank, fromFile, toRank, toFile);
		case BR :
			return BRmightMove(fromRank, fromFile, toRank, toFile);
		case BB :
			return BBmightMove(fromRank, fromFile, toRank, toFile);
		case BN :
			return BNmightMove(fromRank, fromFile, toRank, toFile);
		case BP :
			return BPmightMove(fromRank, fromFile, toRank, toFile);
		}
		return false; // Should never be reached.
	}

	// Black knight
	private boolean BNmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return ((fromFile)) != (toFile)
		&& (fromRank) != (toRank)
		&& abs(((fromFile)) - (toFile)) + abs((fromRank) - (toRank)) == 3;
	}

	// white knight
	private boolean WNmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return ((fromFile)) != (toFile)
		&& (fromRank) != (toRank)
		&& abs(((fromFile)) - (toFile)) + abs((fromRank) - (toRank)) == 3;
	}

	// black queen
	public boolean BQmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return checkLineOfSight(fromRank, fromFile, toRank, toFile);
	}

	// white queen
	public boolean WQmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return checkLineOfSight(fromRank, fromFile, toRank, toFile);
	}

	// black bishop
	private boolean BBmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return (abs(toRank - fromRank) == abs(toFile - (fromFile)))
		&& checkLineOfSight(fromRank, fromFile, toRank, toFile);
	}

	// white bishop
	private boolean WBmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return (abs(toRank - fromRank) == abs(toFile - (fromFile)))
		&& checkLineOfSight(fromRank, fromFile, toRank, toFile);
	}

	// black rook
	private boolean BRmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return (toRank == fromRank || toFile == (fromFile)) && checkLineOfSight(fromRank, fromFile, toRank, toFile);
	}

	// white rook    
	private boolean WRmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return (toRank == fromRank || toFile == fromFile) && checkLineOfSight(fromRank, fromFile, toRank, toFile);
	}

	// black pawn
	private boolean BPmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return (
				(toRank == fromRank - 1 && toFile == fromFile && getField(toRank, toFile) == EM)
				|| (toRank == fromRank - 1
						&& abs(toFile - fromFile) == 1
						&& (isWhiteFigure(getField(toRank, toFile)) || ((toRank << 3) + toFile) == enPassantTarget))
						|| ((fromRank) == 6
								&& (toRank) == 4
								&& toFile == fromFile
								&& getField(4, toFile) == EM
								&& getField(5, toFile) == EM));
	}

	public boolean BPmightAttack(int fromRank, int fromFile, int toRank, int toFile) {
		return toRank == fromRank - 1 && abs(toFile - (fromFile)) == 1;
	}

	// white pawn
	public boolean WPmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return (
				(toRank == fromRank + 1 && toFile == fromFile && getField(toRank, toFile) == EM)
				|| (toRank == fromRank + 1
						&& abs(toFile - fromFile) == 1
						&& (isBlackFigure(getField(toRank, toFile)) || ((toRank << 3) + toFile) == enPassantTarget))
						|| ((fromRank) == 1
								&& (toRank) == 3
								&& toFile == fromFile
								&& getField(3, toFile) == EM
								&& getField(2, toFile) == EM));
	}

	public boolean WPmightAttack(int fromRank, int fromFile, int toRank, int toFile) {
		return toRank == fromRank + 1 && abs(toFile - (fromFile)) == 1;
	}

	// black king
	public boolean BKmightAttack(int fromRank, int fromFile, int toRank, int toFile) {
		return (abs(toRank - fromRank) <= 1 && abs(toFile - fromFile) <= 1);
	}

	private boolean BKmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return (
				(abs(toRank - fromRank) <= 1 && abs(toFile - fromFile) <= 1)
				|| (fromRank == 7 && fromFile == 4 && toRank == 7 && toFile == 2 && BKcouldCastleQueenSide())
				|| (fromRank == 7 && fromFile == 4 && toRank == 7 && toFile == 6 && BKcouldCastleKingSide()));
	}

	private boolean BKcouldCastleQueenSide() {
		return !blackKingHasMoved
		&& !leftBlackRookHasMoved
		&& checkLineOfSight(7, 0, 7, 4)
		&& !isAttackedByWhite((7 << 3) + 4)
		&& !isAttackedByWhite((7 << 3) + 3);
	}

	private boolean BKcouldCastleKingSide() {
		return !blackKingHasMoved
		&& !rightBlackRookHasMoved
		&& checkLineOfSight(7, 7, 7, 4)
		&& !isAttackedByWhite((7 << 3) + 4)
		&& !isAttackedByWhite((7 << 3) + 5);
	}

	// white king
	public boolean WKmightAttack(int fromRank, int fromFile, int toRank, int toFile) {
		return (abs(toRank - fromRank) <= 1 && abs(toFile - fromFile) <= 1);
	}

	private boolean WKmightMove(int fromRank, int fromFile, int toRank, int toFile) {
		return (
				(abs(toRank - fromRank) <= 1 && abs(toFile - fromFile) <= 1)
				|| (fromRank == 0 && fromFile == 4 && toRank == 0 && toFile == 2 && WKcouldCastleQueenSide())
				|| (fromRank == 0 && fromFile == 4 && toRank == 0 && toFile == 6 && WKcouldCastleKingSide()));
	}

	public boolean WKcouldCastleQueenSide() {
		return !whiteKingHasMoved 
		&& !leftWhiteRookHasMoved 
		&& checkLineOfSight(0, 0, 0, 4)
		&& !isAttackedByBlack(4)
		&& !isAttackedByBlack(3);
	}
	public boolean WKcouldCastleKingSide() {
		return !whiteKingHasMoved 
		&& !rightWhiteRookHasMoved 
		&& checkLineOfSight(0, 7, 0, 4)
		&& !isAttackedByBlack(4)
		&& !isAttackedByBlack(5);
	}

	public String getResult() {
		if (gameIsFinished()) {
			if (onlyKingsLeft()) {
				return "1/2-1/2";
			} else if (isWhiteTurn()) {
				if (isAttackedByBlack(getWhiteKingPos())) {
					return "0-1";
				} else {
					return "1/2-1/2";
				}
			} else {
				if (isAttackedByWhite(getBlackKingPos())) {
					return "1-0";
				} else {
					return "1/2-1/2";
				}
			}
		} else
			return "*";
	}

	// 
	// private methods
	//

	private boolean checkLineOfSight(int fromRank, int fromFile, int toRank, int toFile) {
		// Checks wether there is any piece on the line between the two given fields.
		if (fromRank != toRank && fromFile != toFile && abs(toRank - fromRank) != abs(toFile - fromFile))
			return false; // Only horizontal, vertical, or diagonal lines
		// are allowed.
		int rdiff = (toRank >= fromRank) ? ((toRank > fromRank) ? 1 : 0) : -1;
		int fdiff = (toFile >= fromFile) ? ((toFile > fromFile) ? 1 : 0) : -1;
		int rank = fromRank + rdiff;
		int file = fromFile + fdiff;
		while (rank != toRank || file != toFile) {
			if (field[(rank << 3) + file] != EM)
				return false;
			rank += rdiff;
			file += fdiff;
		}
		return true;
	}

	public static String binaryPresentation(long b) {
		String result = "";
		for (int rank = 7; rank >= 0; rank--) {
			for (int file = 0; file < 8; file++) {
				if ((b & (1l << ((rank << 3) + file))) != 0) {
					result += "1 ";
				} else {
					result += "0 ";
				}
			}
			result += "\n";
		}
		return result;
	}

	public static String binaryPresentation(byte b) {
		String result = "";
		for (int file = 7; file >= 0; file--) {
			if ((b & (1l << file)) != 0) {
				result += "1";
			} else {
				result += "0";
			}
		}
		return result;
	}

}
