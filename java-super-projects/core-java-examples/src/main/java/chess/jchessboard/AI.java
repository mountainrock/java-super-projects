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

/* $Id: AI.java 7 2009-11-15 18:58:42Z cdivossen $ */

package chess.jchessboard;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class AI implements Runnable {
	// AI Levels
	public static final int EASY = 0; 
	public static final int MEDIUM = 1; 
	public static final int BEST = 2; 
	public static final String[] LEVELS = {"Easy", "Medium", "Best"};
    private static final int[] maxDepthByLevel = { 2, 4, 20 };
    private static final int[] maxTimeByLevel = { 1000, 2000, 5000 };
    private int level = MEDIUM; // Default. But JChessBoard.settings will override this.

	private static Log logger = LogFactory.getLog(AI.class);
	// As suggested by Shannon 1949:
	private final static int[] figureScore = { 
		0,  20000,  900,  500,  350,  300,  100, //White
		-20000, -900, -500, -350, -300, -100 //Black
	};

	// Defensive
	/*
	private final static int[] whiteAttackScore = { 
		1,  0, 20, 15, 10, 10, 4, //White
		   20, 10,  7,  5,  5, 2 //Black
	};
	private final static int[] blackAttackScore = { 
		1, 20, 10,  7,  5,  5, 2, //White
		    0, 20, 15, 10, 10, 4 //Black
	};
	*/
	
	// Neutral
	private final static int[] whiteAttackScore = { 
		1,  0, 15, 10, 8, 7, 3, //White
		   20, 15, 10, 8, 7, 3 //Black
	};
	private final static int[] blackAttackScore = { 
		1, 20, 15, 10, 8, 7, 3, //White
		    0, 15, 10, 8, 7, 3 //Black
	};

	// Aggressive
    /*
	private final static int[] whiteAttackScore = { 
		1,  0, 10,  7,  5,  5, 2, //White
		   50, 20, 15, 10, 10, 4 //Black
	};
	private final static int[] blackAttackScore = { 
		1, 50, 20, 15, 10, 10, 4, //White
	 	    0, 10,  7,  5,  5, 2 //Black
	};
	*/
	boolean isThreaded=false;
	private boolean isEnabled = true;
	private Thread aiThread;
	private JChessBoard jcb;
	private int maxDepth = 9; // Should be global, used static in recursion.
	private Random random = new Random();
	private int evalCounter, posCounter, getAllMovesCounter, getCapturingMovesCounter;
	private int qSearchCounter;

	private static int TRANSTABLESIZE = 0x2000;
	private static long[][] hashBase = new long[13][64];
	private TransTableEntry[] transTable;
	// History Heuristic Table:
	private int[][][] histHeu; //contains scores [from][to][ply]
	// Killer moves:
	private int[] killer1from = new int[20];
	private int[] killer1to = new int[20];
	private int[] killer1value = new int[20];
	private int[] killer2from = new int[20];
	private int[] killer2to = new int[20];
	private int[] killer2value = new int[20];

	private long startTime, endTime;
	private long timeout = 15000; //millisec.
	private static boolean firstAI = true;
	private int transTableHits, transTableMiss, transTableFaults;
	private static String prefix[] = { "          0 ", "        1 ", "      2 ", "    3 ", "  4 ", "5 " };
	private List visitedPositions;

	public static String getVersion() {
		return "$Id: AI.java 7 2009-11-15 18:58:42Z cdivossen $";
	}

	public void prepareMove() {
		makeMove();
	}

	private void makeMove() {
		if(isThreaded)
		   aiThread.interrupt();
		else
			work();
	}

	public void prepareMove(long timeout) {
		if (timeout < 500) // Minimum time: 500ms.
			this.timeout = 500;
		else
			this.timeout = timeout;
		//        this.timeout = 180000;
		makeMove();
	}

	public int getHash(VirtualBoard vb) {
		int hash = 0;
		for (int n = 0; n < 64; n++)
			if (vb.field[n] != VirtualBoard.EM)
				//Empty fields don't need to change the hash.
				hash ^= hashBase[vb.field[n]][n];
		return hash;
	}

	class TransTableEntry {
		int depth = 0;
		int bestFrom = 64;
		int bestTo = 64;
		boolean isSet = false;
		public VirtualBoard virtualBoard;
		int score = 0;

		public TransTableEntry() {
		};
		boolean isWhiteTurn;

		public void set(VirtualBoard vb, int score, int dep, int bF, int bT) {
			virtualBoard = vb.clonedBoard();
			this.score = score;
			depth = dep;
			bestFrom = bF;
			bestTo = bT;
			isSet = true;
			isWhiteTurn = vb.isWhiteTurn;
		}

		public void clear() {
			virtualBoard = null;
			depth = 0;
			bestFrom = 64;
			bestTo = 64;
			isSet = false;
			score = 0;
		}

	}

	public void newGame() {
		// Needed to reset game specific values.
	}

	public static int evaluateMaterial(VirtualBoard vb) {
		int score = 0;
		for (int n = 0; n < 64; n++)
			score += figureScore[vb.field[n]];
		return (vb.isWhiteTurn) ? score : -score;
	}

	private static int whiteAttackScore(VirtualBoard vb) {
		int result = 0;
		long board = vb.getWhiteAttackBoard();
		for (int x = 0; x < 64; x++) {
			if ((board & (1l << x)) != 0)
				result+=whiteAttackScore[vb.field[x]];
		}
		return result;
	}

	private static int blackAttackScore(VirtualBoard vb) {
		int result = 0;
		long board = vb.getBlackAttackBoard();
		for (int x = 0; x < 64; x++) {
			if ((board & (1l << x)) != 0)
				result+=blackAttackScore[vb.field[x]];
		}
		return result;
	}

	public static int countFields(long board) {
		int result=0;
		for (int x = 0; x < 64; x++) {
			result += ( board >> x ) & 1;
		}
		return result;
	}
	
	private static int countCenterFields(long board) {
		int result = 0;
		result += ( board >> 26 ) & 1;
		result += ( board >> 27 ) & 1;
		result += ( board >> 28 ) & 1;
		result += ( board >> 29 ) & 1;
		result += ( board >> 34 ) & 1;
		result += ( board >> 35 ) & 1;
		result += ( board >> 36 ) & 1;
		result += ( board >> 37 ) & 1;
		return result;
	}

	public static int evaluate(VirtualBoard vb) {
		int score = 0;
		int pieceCount = 0;

		// Material evaluation
		for (int n = 0; n < 64; n++) {
			if (vb.field[n] != VirtualBoard.EMPTY_FIELD) {
				score += figureScore[vb.field[n]];
				pieceCount++;
			}
		}

		// Mobility bonus
		score+=vb.movesCount;

		// Board control, attack / defense bonus
		score += whiteAttackScore(vb);
		score -= blackAttackScore(vb);

		// Center control in start and middle game
		if (pieceCount > 12) {
			score += countCenterFields(vb.getWhiteAttackBoard()) * 2;
			score -= countCenterFields(vb.getBlackAttackBoard()) * 2;
		}
	
		// King safety (As suggested by Turing)
		if (pieceCount > 12) {
			score -= countFields(vb.getQueenAttackBoard(vb.whiteKingPos) & ~vb.whitePieces) * 2;
			score += countFields(vb.getQueenAttackBoard(vb.blackKingPos) & ~vb.blackPieces) * 2;
		}
		
		// Ability to castle
		if (pieceCount > 12) {
			if (!vb.whiteKingHasMoved && !vb.leftWhiteRookHasMoved)
				score += 5 ;
			if (!vb.whiteKingHasMoved && !vb.rightWhiteRookHasMoved)
				score += 5 ;
			if (!vb.blackKingHasMoved && !vb.leftBlackRookHasMoved)
				score -= 5 ;
			if (!vb.blackKingHasMoved && !vb.rightBlackRookHasMoved)
				score -= 5 ;
		}
		
		// Pending pawn promotions
		for (int n = 48; n < 56; n++)
			if (vb.field[n] == VirtualBoard.WP)
				score += 20;
		for (int n = 40; n < 48; n++)
			if (vb.field[n] == VirtualBoard.WP)
				score += 10;
		for (int n = 8; n < 16; n++)
			if (vb.field[n] == VirtualBoard.BP)
				score -= 20;
		for (int n = 16; n < 24; n++)
			if (vb.field[n] == VirtualBoard.BP)
				score -= 10;

		// TODO: Pawn formation, e.g. penalty for two pawns in one file
		
		return score;
	}

	public int alphabeta(VirtualBoard vb, int ply, int depth, int qdepth, int alpha, int beta) {

		if (depth == 0) {
			evalCounter++;
			return (vb.isWhiteTurn) ? evaluate(vb) : -evaluate(vb);
		}

		if ((System.currentTimeMillis() >= endTime) || !isEnabled) {
			return Integer.MIN_VALUE;
		}

		VirtualBoard pos, bestPos;
		Move bestMove = null;
		int best_score = -10000000;
		TransTableEntry tbEntry;
		int hash = getHash(vb);

		//        		if (maxDepth - depth >= 2) {
			if ((tbEntry = transTable[hash]).isSet) {
				if (tbEntry.virtualBoard.equals(vb) && tbEntry.depth >= depth) {
					// If this position was considered already, just return the result calculated before.
					transTableHits++;
					return tbEntry.score;
				}
			} else {
				transTableMiss++;
			}
			//        		}

			best_score = -10000000;
			bestPos = null;
			getAllMovesCounter++;
			List moves = vb.getAllMoves();
			java.util.Collections.shuffle(moves);

			if (qdepth > 0) {
				bestPos = vb;
				evalCounter++;
				best_score =  (vb.isWhiteTurn) ? evaluate(vb) : -evaluate(vb);
				if (qdepth > 1)
					return best_score;
			}

			if (moves.size() == 0) {
				// No more moves from this node:
					if (vb.isWhiteTurn) {
						if (vb.isAttackedByBlack(vb.whiteKingPos)) {
							return -20001 - depth * 100; // Mate (Lost)
						} else
							return 10001; // Stalemate. Always avoid this!
					} else {
						if (vb.isAttackedByWhite(vb.blackKingPos)) {
							return -20001 - depth * 100; // Mate (Lost)
						} else
							return 10001; // Stalemate. Always avoid this!
					}
					// TODO Other drawish situations like vb.onlyKingsLeft (slow) should be considered too.
			}

			// Move ordering:
			List positions = new Vector();
			for (int x = 0; x < moves.size(); x++) {
				Move move = (Move) moves.get(x);
				pos = vb.clonedBoard();
				pos.makeAnyMove(move);
				if (depth > 1) {
					pos.score = -evaluateMaterial(pos);
					// Is killer move?
							if (pos.lastFrom == killer1from[depth] && pos.lastTo == killer1to[depth])
								pos.score += 500;
							else if (pos.lastFrom == killer2from[depth] && pos.lastTo == killer2to[depth])
								pos.score += 450;
							pos.score += histHeu[pos.lastFrom][pos.lastTo][ply + 1] * 10; // ply+1?
				}
				positions.add(pos);
			}
			if (depth > 1) {
				java.util.Collections.sort(positions, new java.util.Comparator() {
					public int compare(Object o1, Object o2) {
						return ((VirtualBoard) o1).score - ((VirtualBoard) o2).score;
					}
				});
			}

			//
			// Main loop
			//
			for (int x = positions.size() - 1; x >= 0; x--) {
				pos = (VirtualBoard) positions.get(x);
				boolean isCapturingMove = false;
				if (vb.field[pos.lastTo] != VirtualBoard.EMPTY_FIELD) {
					isCapturingMove = true;
				}
				int score = -10000000;
				if (isCapturingMove && depth == 1) { // Perform a quiescence search
					qSearchCounter++;
					score = -alphabeta(pos, ply + 1, 1, qdepth + 1, -beta, -alpha);
				} else {
					score = -alphabeta(pos, ply + 1, depth - 1, 0, -beta, -alpha);
				}

				if (score > best_score) {
					best_score = score;
					bestPos = pos;
				}
				if (best_score > alpha)
					alpha = best_score;

				if (alpha >= beta) {
					if (score > killer1value[depth]) {
						killer2from[depth] = killer1from[depth];
						killer2to[depth] = killer1to[depth];
						killer2value[depth] = killer1value[depth];
						killer1from[depth] = bestPos.lastFrom;
						killer1to[depth] = bestPos.lastTo;
						killer1value[depth] = score;
					}
					if (!transTable[hash].isSet || transTable[hash].depth < depth)
						transTable[hash].set(vb, best_score, depth, bestPos.lastFrom, bestPos.lastTo);
					histHeu[bestPos.lastFrom][bestPos.lastTo][ply]++;
					return alpha;
				}

			}
			if(bestPos!=null) {
				if (!transTable[hash].isSet || transTable[hash].depth < depth)
					transTable[hash].set(vb, best_score, depth, bestPos.lastFrom, bestPos.lastTo);
				if (best_score > killer1value[depth]) {
					killer2from[depth] = killer1from[depth];
					killer2to[depth] = killer1to[depth];
					killer2value[depth] = killer1value[depth];
					killer1from[depth] = bestPos.lastFrom;
					killer1to[depth] = bestPos.lastTo;
					killer1value[depth] = best_score;
				}
				histHeu[bestPos.lastFrom][bestPos.lastTo][ply]++;
			}
			return best_score;
	}

	public void work() {
		VirtualBoard startBoard;
		startBoard = jcb.getCurrentVirtualBoard();
		System.out.println(startBoard);
		if (!startBoard.gameIsFinished()) {

			startTime = System.currentTimeMillis();
			endTime = startTime + timeout;

			visitedPositions = jcb.getHistory().getAllBoards();
			evalCounter = 0;
			posCounter = 0;
			getAllMovesCounter = 0;
			getCapturingMovesCounter = 0;
			qSearchCounter = 0;
			transTableHits = 0;
			transTableMiss = 0;
			transTableFaults = 0;
			for (int i = 0; i < TRANSTABLESIZE; i++)
				transTable[i].clear();
			for (int i = 0; i < 20; i++) {
				killer1from[i] = 64;
				killer1to[i] = 64;
				killer1value[i] = -1000000;
				killer2from[i] = 64;
				killer2to[i] = 64;
				killer2value[i] = -1000000;
			}
			histHeu = new int[64][64][20];

			maxDepth = maxDepthByLevel[level];
			int depth = 2;
			int ply = 0;
			long time = 0;
			int best_score;

			Move bestMove = null;
			List moves = startBoard.getAllMoves();
			java.util.Collections.shuffle(moves);

			final HashMap moveScores = new HashMap();
			for (int x = 0; x<moves.size(); x++) {
				moveScores.put(moves.get(x), new Integer(Integer.MIN_VALUE));
			}
			jcb.showMessage("AI working...","small");
			if (moves.size() == 1) { // Only one move possible, no need to think about. 
				bestMove = (Move) moves.get(0);
				depth = maxDepth;
			} else
				do {
					logger.debug("\nDepth: " + depth);
					jcb.showMessage("Depth " + depth + "...", "small");
					final int thisPly = ply;
					java.util.Collections.sort(moves, new java.util.Comparator() {
						public int compare(Object o1, Object o2) {
							Move m1 = (Move) o1;
							Move m2 = (Move) o2;
							return ((Integer) moveScores.get(m1)).intValue() - ((Integer) moveScores.get(m2)).intValue(); 
						}
					});
					best_score = -100000000;
					int alpha = -100000000;
					for (int x = moves.size() - 1; x >= 0; x--) {
						Move move = (Move) moves.get(x);
						VirtualBoard position = startBoard.clonedBoard();
						position.makeAnyMove(move);
						int score;
						boolean visited = false;
						for (int n = 0; n < visitedPositions.size(); n++)
							if (((VirtualBoard) visitedPositions.get(n)).isEqualPosition(position))
								visited = true;
						if (visited)
							score = -5000;
						else {
							score = -alphabeta(position, ply + 1, depth - 1, 0, -10000000, -alpha);
							if ((System.currentTimeMillis() >= endTime) || !isEnabled) {
								score=Integer.MIN_VALUE;
								System.out.print("TIMEOUT");
								break;
							}
						}
						if (score > best_score) {
							best_score = score;
							bestMove = move;
						}
						if (best_score > alpha)
							alpha = best_score;
						moveScores.put(move,new Integer(score));
						logger.debug("move : "+move + ", score :" + score + " ");
					}
					depth++;
					time = System.currentTimeMillis();
				} while (
						time < endTime
//						&& (depth <= maxDepth || (time - startTime) < minTime)
						&& (depth <= maxDepth)
						&& ((time - startTime) < (endTime - time))
						&& best_score<20000);
			jcb.showMessage("Evaluated "+evalCounter+" positions in "+(System.currentTimeMillis()-startTime)+" ms",
			"small");
			/*            System.out.println("--------------------------");
                        System.out.println(evalCounter + " evaluations");
                        System.out.println("Transition Table Statistics:");
                        System.out.print("Size: " + TRANSTABLESIZE + " entries");
                        System.out.print("  Hits: " + transTableHits);
                        System.out.print("  Misses: " + transTableMiss);
                        System.out.println("  Faults: " + transTableFaults);
                        int setEntryCounter = 0;
                        for (int n = 0; n < TRANSTABLESIZE; n++)
                            if (transTable[n].isSet)
                                setEntryCounter++;
                        System.out.println(setEntryCounter + " entries set"); */
			final Move aiMove = bestMove;
			logger.info("\n AI Move "+aiMove);
			jcb.makeAIsMove(aiMove);
			// TODO: If there are several equally rated moves, one should be picked by random.
			if (isEnabled) {
				if (startBoard.equals(jcb.getCurrentVirtualBoard())) {
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							synchronized (jcb) {
								jcb.makeAIsMove(aiMove);
							}
						}
					});
				} else {
					jcb.showMessage("AI: Who changed the board?!");
				}
			}
		}else{
			System.out.println("Game has finished");
		}
	}

	public void run() {
		newGame();
		while (isEnabled) {
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException ie) {
			}
			if (isEnabled)
				work(); // Do your job!
		}
	}

	public void perfTest() {
		// Performance-Check
		VirtualBoard testboard = new VirtualBoard();
		testboard.init();

		int count=0;
		// Measure AI.evaluate()
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis() - start < 1000) {
			for (int n = 0; n < 1000; n++)
				AI.evaluate(testboard);
			count+=1000;
		}
		long end = System.currentTimeMillis();
		jcb.showMessage(1000*count / (end - start) + " evaluations/sec.");

		// Measure VirtualBoard.getAllMoves()
		start = System.currentTimeMillis();
		count=0;
		while(System.currentTimeMillis() - start < 1000) {
			for (int n = 0; n < 1000; n++)
				testboard.getAllMoves();
			count+=1000;
		}
		end = System.currentTimeMillis();
		jcb.showMessage(1000*count / (end - start) + " getAllMoves/sec");

		// Measure VirtualBoard.simpleGetAllMoves()
		start = System.currentTimeMillis();
		count=0;
		while(System.currentTimeMillis() - start < 1000) {
			for (int n = 0; n < 1000; n++)
				testboard.simpleGetAllMoves();
			count+=1000;
		}
		end = System.currentTimeMillis();
		jcb.showMessage(1000*count / (end - start) + " simpleGetAllMoves/sec");
	}

	public void shutdown() {
		isEnabled = false;
		aiThread.interrupt();
		try {
			aiThread.join();
		} catch (InterruptedException e) {
		}
	}
	
	public void setLevel(int newLevel) {
		level=newLevel;
		maxDepth = maxDepthByLevel[level];
		timeout = maxTimeByLevel[level];
		jcb.showMessage("AI level: "+LEVELS[level]+" (max depth: "+maxDepth+", timeout: "+timeout+"ms)");
	}

	// The constructor:
	// isThreaded : A threaded AI can think more than a non threaded one. 
	public AI(JChessBoard chessBoard, boolean isThreaded) {
		jcb = chessBoard;
		this.isThreaded = isThreaded;
		if (firstAI) {
			for (int i = 0; i < 13; i++)
				for (int j = 0; j < 64; j++)
					hashBase[i][j] = random.nextInt(TRANSTABLESIZE - 1);
			firstAI = false;
		}
		//Initialize hash base table:
		transTable = new TransTableEntry[TRANSTABLESIZE];
		histHeu = new int[20][64][64];
		// Create transposition table:
		for (int i = 0; i < TRANSTABLESIZE; i++)
			transTable[i] = new TransTableEntry();
		if(isThreaded){
			(aiThread = new Thread(this)).start();
		}
	}
}
