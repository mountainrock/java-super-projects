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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.    See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

/* $Id: PGN.java 5 2009-11-10 07:56:47Z cdivossen $ */

package chess.jchessboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

class PGN {

	public static String getVersion() {
		return "$Id: PGN.java 5 2009-11-10 07:56:47Z cdivossen $";
	}

	static final String NAGStrings[] =
	{
		"",
		"!",
		"?",
		"!!",
		"??",
		"!?",
		"?!",
		"forced move (all others lose quickly)",
		"singular move (no reasonable alternatives)",
		"worst move",
		"drawish position",
		"equal chances, quiet position",
		"equal chances, active position",
		"unclear position",
		"White has a slight advantage",
		"Black has a slight advantage",
		"White has a moderate advantage",
		"Black has a moderate advantage",
		"White has a decisive advantage",
		"Black has a decisive advantage",
		"White has a crushing advantage (Black should resign)",
		"Black has a crushing advantage (White should resign)",
		"White is in zugzwang",
		"Black is in zugzwang",
		"White has a slight space advantage",
		"Black has a slight space advantage",
		"White has a moderate space advantage",
		"Black has a moderate space advantage",
		"White has a decisive space advantage",
		"Black has a decisive space advantage",
		"White has a slight time (development) advantage",
		"Black has a slight time (development) advantage",
		"White has a moderate time (development) advantage",
		"Black has a moderate time (development) advantage",
		"White has a decisive time (development) advantage",
		"Black has a decisive time (development) advantage",
		"White has the initiative",
		"Black has the initiative",
		"White has a lasting initiative",
		"Black has a lasting initiative",
		"White has the attack",
		"Black has the attack",
		"White has insufficient compensation for material deficit",
		"Black has insufficient compensation for material deficit",
		"White has sufficient compensation for material deficit",
		"Black has sufficient compensation for material deficit",
		"White has more than adequate compensation for material deficit",
		"Black has more than adequate compensation for material deficit",
		"White has a slight center control advantage",
		"Black has a slight center control advantage",
		"White has a moderate center control advantage",
		"Black has a moderate center control advantage",
		"White has a decisive center control advantage",
		"Black has a decisive center control advantage",
		"White has a slight kingside control advantage",
		"Black has a slight kingside control advantage",
		"White has a moderate kingside control advantage",
		"Black has a moderate kingside control advantage",
		"White has a decisive kingside control advantage",
		"Black has a decisive kingside control advantage",
		"White has a slight queenside control advantage",
		"Black has a slight queenside control advantage",
		"White has a moderate queenside control advantage",
		"Black has a moderate queenside control advantage",
		"White has a decisive queenside control advantage",
		"Black has a decisive queenside control advantage",
		"White has a vulnerable first rank",
		"Black has a vulnerable first rank",
		"White has a well protected first rank",
		"Black has a well protected first rank",
		"White has a poorly protected king",
		"Black has a poorly protected king",
		"White has a well protected king",
		"Black has a well protected king",
		"White has a poorly placed king",
		"Black has a poorly placed king",
		"White has a well placed king",
		"Black has a well placed king",
		"White has a very weak pawn structure",
		"Black has a very weak pawn structure",
		"White has a moderately weak pawn structure",
		"Black has a moderately weak pawn structure",
		"White has a moderately strong pawn structure",
		"Black has a moderately strong pawn structure",
		"White has a very strong pawn structure",
		"Black has a very strong pawn structure",
		"White has poor knight placement",
		"Black has poor knight placement",
		"White has good knight placement",
		"Black has good knight placement",
		"White has poor bishop placement",
		"Black has poor bishop placement",
		"White has good bishop placement",
		"Black has good bishop placement",
		"White has poor rook placement",
		"Black has poor rook placement",
		"White has good rook placement",
		"Black has good rook placement",
		"White has poor queen placement",
		"Black has poor queen placement",
		"White has good queen placement",
		"Black has good queen placement",
		"White has poor piece coordination",
		"Black has poor piece coordination",
		"White has good piece coordination",
		"Black has good piece coordination",
		"White has played the opening very poorly",
		"Black has played the opening very poorly",
		"White has played the opening poorly",
		"Black has played the opening poorly",
		"White has played the opening well",
		"Black has played the opening well",
		"White has played the opening very well",
		"Black has played the opening very well",
		"White has played the middlegame very poorly",
		"Black has played the middlegame very poorly",
		"White has played the middlegame poorly",
		"Black has played the middlegame poorly",
		"White has played the middlegame well",
		"Black has played the middlegame well",
		"White has played the middlegame very well",
		"Black has played the middlegame very well",
		"White has played the ending very poorly",
		"Black has played the ending very poorly",
		"White has played the ending poorly",
		"Black has played the ending poorly",
		"White has played the ending well",
		"Black has played the ending well",
		"White has played the ending very well",
		"Black has played the ending very well",
		"White has slight counterplay",
		"Black has slight counterplay",
		"White has moderate counterplay",
		"Black has moderate counterplay",
		"White has decisive counterplay",
		"Black has decisive counterplay",
		"White has moderate time control pressure",
		"Black has moderate time control pressure",
		"White has severe time control pressure",
	"Black has severe time control pressure" };

	static class STR { // Seven Tag Roster
		Hashtable tags = new Hashtable();
		String tagNames[] = {
				// STR:
				"Event", "Site", "Date", "Round", "White", "Black", "Result",
				// Supplemental tags:
				"WhiteTitle", "BlackTitle", "WhiteElo", "BlackElo", "WhiteUSCF",
				// United States Chess Federation rating
				"BlackUSCF", "WhiteNA", // Network address
				"BlackNA", "WhiteType", // human/program
				"BlackType", "EventDate", // Start date of the event
				"EventSponsor", "Section", // playing section of tournament
				"Stage", // of multistage event, e.g. "preliminary" or "semifinal"
				"Board", // board number in a team event
				"Opening", // traditional opening name
				"Variation", "SubVariation", "ECO",
				// value from Encyclopedia of Chess Openings
				"NIC", // value from New In Chess database
				"Time", // local start of game time
				"UTCTime", "UTCDate", "TimeControl", // !
				"SetUp", "FEN", "Termination",
				// one of: abandoned, adjudication, death, emergency, normal
				//rules infraction, time forfeit, unterminated
				"Annotator", "Mode",
				// OTB: over the board, PM: paper mail, EM: electronic mail, ICS, TC: telco)
		"PlyCount" };

		private boolean isSTRTag(String name) {
			return name.equals("Event")
			|| name.equals("Site")
			|| name.equals("Date")
			|| name.equals("Round")
			|| name.equals("White")
			|| name.equals("Black")
			|| name.equals("Result");
		};
		public String[] tags() {
			String tagNames[] = new String[tags.size()];
			tagNames[0] = "Event"; // Damn standard tags!
			tagNames[1] = "Site";
			tagNames[2] = "Date";
			tagNames[3] = "Round";
			tagNames[4] = "White";
			tagNames[5] = "Black";
			tagNames[6] = "Result";
			int index = 7;
			Enumeration keys = tags.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				if (!isSTRTag(key))
					tagNames[index++] = key;
			}
			return tagNames;
		}
		public String toString() {
			Enumeration keys = tags.keys();
			StringBuffer s = new StringBuffer();
			s.append("[Event \"" + tags.get("Event") + "\"]\n");
			s.append("[Site \"" + tags.get("Site") + "\"]\n");
			s.append("[Date \"" + tags.get("Date") + "\"]\n");
			s.append("[Round \"" + tags.get("Round") + "\"]\n");
			s.append("[White \"" + tags.get("White") + "\"]\n");
			s.append("[Black \"" + tags.get("Black") + "\"]\n");
			s.append("[Result \"" + tags.get("Result") + "\"]\n");
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				if (!isSTRTag(key))
					s.append("[" + key + " \"" + (String) tags.get(key) + "\"]\n");
			}
			return s.toString();
		}

		public STR getCopy() {
			STR newSTR = new STR();
			Enumeration keys = tags.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				newSTR.setTag(key, (String) tags.get(key));
			}
			return newSTR;
		}
		public void setTag(String name, String value) {
			tags.put(name, value);
		}
		public String getTag(String name) {
			return (String) tags.get(name);
		}
		public void removeTag(String name) {
			if (!isSTRTag(name))
				tags.remove(name);
		}
		public int size() {
			return tags.size();
		}
		public boolean hasTag(String name) {
			return tags.containsKey(name);
		}
		public STR() {
			tags.put("Event", "?");
			tags.put("Site", "?");
			tags.put("Date", "????.??.??");
			tags.put("Round", "?");
			tags.put("Black", "?");
			tags.put("White", "?");
			tags.put("Result", "?");
		}
	}

	static class PGNTokenizer {
		String game;
		int gameLength;
		int currentPos = 0;
		boolean foundTag = false;
		boolean foundNAG = false;
		boolean foundMoveNumberIndication = false;
		boolean foundComment = false;
		boolean foundRAV = false;
		String nextToken = null;

		public String next() {
			String token;
			if (nextToken == null)
				hasNext();
			if (nextToken != null) {
				token = nextToken;
				nextToken = null;
				return token;
			} else
				return null;
		}

		public boolean hasNext() {
			boolean endOfToken = false;
			char ch;
			StringBuffer token = new StringBuffer();

			foundNAG = false;
			foundMoveNumberIndication = false;
			foundComment = false;
			foundRAV = false;
			while (currentPos < gameLength && !endOfToken) {
				ch = game.charAt(currentPos++);

				if (ch == '[')
					foundTag = true; // Tag
					else if (ch == ']' && foundTag) {
						foundTag = false;
						if (token.length() > 0)
							endOfToken = true;
					} else if (ch == '.') { // Move number indication
						if (token.length() > 0) {
							endOfToken = true;
							foundMoveNumberIndication = true;
						}
					} else if (ch == '<') { // Future expansion (skip)
						do {
							ch = game.charAt(currentPos++);
						} while (currentPos < gameLength && ch != '>');

					} else if (ch == '(') { // RAV 
						// The whole RAV will be returned as one token.
						int recursionDepth = 1;
						do {
							ch = game.charAt(currentPos++);
							// All the following has to be kept in the RAV.
							if (ch == ';') {
								token.append(ch);
								do { // Read the whole comment, until end of line
									ch = game.charAt(currentPos++);
									token.append(ch);
								} while (currentPos < gameLength && ch != '\n');
							} else if (ch == '%') { // Non-PGN comment (skip)
								do {
									ch = game.charAt(currentPos++);
								} while (currentPos < gameLength && ch != '\n');
							} else if (ch == '{') { //PGN comment
								token.append(ch);
								do { // Read the whole comment
									ch = game.charAt(currentPos++);
									token.append(ch);
								} while (currentPos < gameLength && ch != '}');
							} else if (ch == '(') {
								token.append(ch);
								recursionDepth++;
							} else if (ch == ')') {
								recursionDepth--;
								if (recursionDepth > 0)
									token.append(ch);
							} else
								token.append(ch);
						}
						while (currentPos < gameLength && recursionDepth > 0);

						if (token.length() > 0) {
							endOfToken = true;
							foundRAV = true;
						}

					} else if (ch == '{') { //PGN comment
						foundComment = true;
						do { // Read the whole comment
							ch = game.charAt(currentPos++);
							if (ch == '\n')
								token.append(' ');
							else if (ch != '}')
								token.append(ch);
						} while (currentPos < gameLength && ch != '}');
						if (token.length() > 0)
							endOfToken = true;
					} else if (ch == ';') { // PGN comment
						foundComment = true;
						do { // Read the whole comment, until end of line
							ch = game.charAt(currentPos++);
							if (ch != '\n')
								token.append(ch);
						} while (currentPos < gameLength && ch != '\n');
						if (token.length() > 0)
							endOfToken = true;
					} else if (ch == '%') { // Non-PGN comment (skip)
						do {
							ch = game.charAt(currentPos++);
						} while (currentPos < gameLength && ch != '\n');
					} else if (ch == '$')
						foundNAG = true; // NAG
					else if (ch == '"') { // Quotation
						do {
							ch = game.charAt(currentPos++);
							if (ch != '"')
								token.append(ch);
						} while (currentPos < gameLength && ch != '"');
						if (token.length() > 0)
							endOfToken = true;
					} else if (ch == '\n' || ch == ' ' || ch == '\t') { // End of line, white space
						if (token.length() > 0)
							endOfToken = true;
					} else
						token.append(ch);
			}
			if (token.length() == 0) {
				nextToken = null;
				return false;
			} else {
				nextToken = token.toString();
				return true;
			}
		}

		public PGNTokenizer(String game) {
			this.game = game;
			gameLength = game.length();
		}
	}

	public static STR getSTRfromPGN(String pgn) {
		STR result = new STR();
		int startOfLine=0;
		// A tag line looks line: [White "Fischer"]
		while (startOfLine<pgn.length()) {
			int endOfLine = pgn.indexOf('\n',startOfLine);
			String line = pgn.substring(startOfLine,endOfLine);
			if(line.length()<6) // There should be at least: [x ""]
				break; // We're done.
			if(line.charAt(0)!='[')
				break;
			String tag = line.substring(1,line.indexOf(' '));
			int valueStart = line.indexOf('"');
			int valueEnd= line.lastIndexOf('"');
			String value=line.substring(valueStart+1,valueEnd);
			result.setTag(tag,value);
			startOfLine = endOfLine+1;
		}
		return result;
	}

	public static String getBodyFromPGN(String pgn) {
		int startOfLine=0;
		while (startOfLine<pgn.length()) {
			int endOfLine = pgn.indexOf('\n',startOfLine);
			String line = pgn.substring(startOfLine,endOfLine);
			startOfLine = endOfLine+1;
			if(line.length()<6) // There should be at least: [x ""] for a tag line
				break; // We're done.
		}
		return pgn.substring(startOfLine);
	}

	public static void loadPGN(File file, JChessBoard chessBoard) {
		Reader reader;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			chessBoard.showMessage("File not found: " + file);
			return;
		}
		//        (new Thread(new PGNLoader(jcb, reader, file.getName()))).start();
		(new PGNLoader(chessBoard, reader, file.getName())).run();
	}

	static class PGNLoader implements Runnable {
		private JChessBoard jcb;
		private BufferedReader reader;
		private String filename;

		public void run() {
			final List strList = new ArrayList();
			List games = new ArrayList();

			jcb.showMessage("Loading " + filename + "...");
			// Split the file into separate Strings, each containing one game.
			try {
				String line;
				StringBuffer game = null;
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("[Event ")) {
						game = new StringBuffer();
						games.add(game);
					}
					if (!line.equals("") && game == null) {
						jcb.showMessage("Malformed PGN file.");
						return;
					} else if (game != null) {
						game.append(line);
						game.append("\n");
					}
				}
			} catch (IOException e) {
				jcb.showMessage("IOExcpetion reading PGN file.");
				return;
			}

			int gameCount = games.size();
			if (gameCount == 0) {
				jcb.showMessage("No game was found.");
				jcb.newGame();
				return;
			}
			jcb.showMessage("Found " + gameCount + " game" + ((gameCount > 1) ? "s." : "."));
			// Get the Seven Tag Roster for each of the games.
			for (int n = 0; n < gameCount; n++) {
				/*                PGNTokenizer tokenizer = new PGNTokenizer(((StringBuffer) games.get(n)).toString());
                STR str = new STR();
                boolean setUp = false;
                String fen = null;
                while (tokenizer.hasNext()) {
                    String token = tokenizer.next();
                    if (tokenizer.foundTag && tokenizer.hasNext()) {
                        String nextToken = tokenizer.next();
                        if (token.equals("SetUp") && nextToken.equals("1"))
                            setUp = true;
                        else if (token.equals("FEN"))
                            fen = nextToken;
                        else
                            str.setTag(token, nextToken);
                    }
                } */
				STR str = getSTRfromPGN(((StringBuffer) games.get(n)).toString());
				String body = getBodyFromPGN(((StringBuffer) games.get(n)).toString());
				jcb.getGameTable().addGame(body, str);
			}
			jcb.gotoGame(0);
			jcb.showMessage("Loading finished.");
		}

		public PGNLoader(JChessBoard jcb, Reader reader, String filename) {
			this.jcb = jcb;
			this.reader = new BufferedReader(reader);
			this.filename = filename;
		}
	}

	static public void parseGame(History history, VirtualBoard startBoard, String game, boolean debug)
	throws VirtualBoard.ImpossibleMoveException {
		String token = "";
		VirtualBoard vb = startBoard.clonedBoard();
		VirtualBoard lastvb = null;
		if (game == null)
			return;
		HashMap tags = new HashMap();

		PGNTokenizer tokenizer = new PGNTokenizer(game);

		boolean isFinished = false;
		int expectedMove=1;
		try {
			while (tokenizer.hasNext() && !isFinished) {
				if (vb.isWhiteTurn())
					expectedMove = vb.getFullMoveNumber() + 1;
				else
					expectedMove = vb.getFullMoveNumber();
				token = tokenizer.next();
				if (tokenizer.foundTag && tokenizer.hasNext()) {
					String value = tokenizer.next();
					if (debug)
						System.out.println("Tag: " + token + ": " + value);
					tags.put(token, value);
					if (token.equals("FEN")) { // SetUp-Token is not checked!
						vb.initFromFEN(value);
					history.setBoard(vb);
					}
				} else if (tokenizer.foundNAG) {
					try {
						if (debug)
							System.out.println("NAG: " + token);
						int nag = Integer.parseInt(token);
						if (nag < 0 || nag > 255)
							throw new NumberFormatException();
						if (nag >= NAGStrings.length)
							System.out.println("Unknown NAG: " + token);
						history.addNAG(nag);
					} catch (NumberFormatException e) {
						System.out.println("Illegal NAG: " + token);
					}
				} else if (tokenizer.foundMoveNumberIndication) {
					if (debug)
						System.out.println("MoveNumber: " + token);
					try {
						if (Integer.parseInt(token) != expectedMove)
							throw new NumberFormatException();
					} catch (NumberFormatException e) {
						System.out.println("Wrong move number: " + token + ", expected: " + expectedMove);
					}
				} else if (tokenizer.foundComment) {
					if (debug)
						System.out.println("Comment: " + token);
					history.addComment(token);
				} else if (tokenizer.foundRAV) {
					if (debug)
						System.out.println("RAV: " + token);
					history.addVariation(lastvb.clonedBoard(), token);
				} else if (
						token.equals("1-0") // White won
						|| token.equals("0-1") // Black won
						|| token.equals("1/2-1/2") // Draw
						|| token.equals("*")) { // Aborted
					if (debug)
						System.out.println("Game termination: " + token);
					isFinished = true;
				} else { // Must be a move.
					if (debug)
						System.out.println("Move: " + token);
					lastvb = vb.clonedBoard();
					vb.makeMove(token);
					String algebraicMove = lastvb.algebraic(vb.getLastMove());
					history.addBatchMove(algebraicMove, vb.clonedBoard());
				}
			}
		} catch (VirtualBoard.ImpossibleMoveException e) {
			String moveNumber;
			if (vb.isWhiteTurn())
				moveNumber = "" + expectedMove + ". ";
			else
				moveNumber = "" + expectedMove + ". ... ";
			throw new VirtualBoard.ImpossibleMoveException(moveNumber + token);
		}
	}

	public static void writePGN(File file, GameTable gameTable, boolean append) throws IOException {
		BufferedWriter writer = null;
		String result;

		if (append) {
			writer = new BufferedWriter(new FileWriter(file.getPath(), true));
			writer.write("\n");
		} else
			writer = new BufferedWriter(new FileWriter(file));

		int gameCount = gameTable.getGameCount();
		for (int gameIndex = 0; gameIndex < gameCount; gameIndex++) {
			writer.write(gameTable.getSTR(gameIndex).toString());
			writer.write("\n");
			StringBuffer pgnData = new StringBuffer(gameTable.getPGN(gameIndex));
			int endIndex = 0;
			int startIndex = 0;
			int index = 0;
			char ch;
			while (index < pgnData.length()) {
				ch = pgnData.charAt(index);
				if (ch == '\n') {
					writer.write(pgnData.substring(startIndex, index + 1));
					startIndex = index + 1;
				}
				if (index - startIndex > 78) {
					writer.write(pgnData.substring(startIndex, endIndex));
					writer.write("\n");
					startIndex = endIndex + 1;
				}
				if (ch == ' ')
					endIndex = index;
				index++;
			}
			writer.write("\n");
		}
		writer.flush();
		writer.close();
	}

	public PGN() {
	};
}
