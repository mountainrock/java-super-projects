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

/* $Id: Settings.java 7 2009-11-15 18:58:42Z cdivossen $ */

package chess.jchessboard;

import java.awt.Color;
import java.io.IOException;

/**
 * @author cd
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Settings {
	public int networkPort = 8189;
	public boolean enableServer = true;
	public boolean enableClock = true;
	public boolean enableBeep = true;
	//public boolean debugDumpTraffic = true;
	public boolean debugDumpTraffic = false;
	public boolean debugPGNReader = false;
	public boolean showPossibleMoves = true;
	public long blackTime = 600000; //Milliseconds
	public long whiteTime = 600000; //Milliseconds
	public Color boardColor1 = new Color(0, 150, 0);
	public Color boardColor2 = new Color(125, 255, 125);
	public Color boardHighlightColor = new Color(40, 200, 200);
	public Color boardLabelColor = new Color(180, 180, 180);
	public Color boardBackgroundColor = new Color(0, 0, 0);
	public Color chatColorYou = new Color(0, 150, 0);
	public Color chatColorOpponent = new Color(50, 50, 200);
	public Color chatColorDebug = new Color(255, 100, 100);
	public int windowWidth = 710;
	public int windowHeight = 580;
	public String userName = ""; 
	public int aiLevel = AI.MEDIUM; 

	private String settingsFileName=null;

	public static String getVersion() {
		return "$Id: Settings.java 7 2009-11-15 18:58:42Z cdivossen $";
	}

	public void save() throws IOException {
		java.io.BufferedWriter writer;
		String fsep = System.getProperty("file.separator");
		String fileName =
			System.getProperty("user.dir") + fsep + settingsFileName;
		writer = new java.io.BufferedWriter(new java.io.FileWriter(fileName));
		try {
			java.lang.reflect.Field[] fields = this.getClass().getFields();
			for (int n = 0; n < fields.length; n++) {
				Object fieldObj = fields[n].get(this);
				writer.write(fields[n].getName() + "=");
				if (fieldObj instanceof Integer
						|| fieldObj instanceof Boolean
						|| fieldObj instanceof Long
						|| fieldObj instanceof String)
					writer.write("" + fieldObj);
				else if (fieldObj instanceof Color) {
					writer.write(
							((Color) fieldObj).getRed()
							+ ","
							+ ((Color) fieldObj).getGreen()
							+ ","
							+ ((Color) fieldObj).getBlue());
				}
				writer.write("\n");
			}
		} catch (IllegalAccessException e) {
		}
		writer.flush();
		writer.close();
	}

	public void parseLine(String line) {
		int index = line.indexOf('=');
		if (index == -1)
			return;
		String fieldName = line.substring(0, index);
		String value = line.substring(index + 1, line.length());
		java.lang.reflect.Field field;
		try {
			try {
				field = this.getClass().getField(fieldName);
				Object fieldObj = field.get(this);
				if (fieldObj instanceof Integer)
					field.setInt(this, Integer.parseInt(value));
				else if (fieldObj instanceof Long)
					field.setLong(this, Long.parseLong(value));
				else if (fieldObj instanceof String)
					field.set(this, value);
				else if (fieldObj instanceof Boolean) {
					if (value.equals("true"))
						field.setBoolean(this, true);
					else
						field.setBoolean(this, false);
				} else if (fieldObj instanceof Color) {
					try {
						int sindex = 0;
						index = value.indexOf(',', sindex);
						int red =
							Integer.parseInt(value.substring(sindex, index));
						sindex = index + 1;
						index = value.indexOf(',', sindex);
						int green =
							Integer.parseInt(value.substring(sindex, index));
						sindex = index + 1;
						index = value.length();
						int blue =
							Integer.parseInt(value.substring(sindex, index));
						field.set(this, new Color(red, green, blue));
					} catch (NumberFormatException e) {
					}
				}
			} catch (IllegalAccessException e) {
			}
		} catch (java.lang.NoSuchFieldException e) {
		}
	}

	public boolean read() {
		java.io.BufferedReader reader;
		String fsep = System.getProperty("file.separator");
		String fileName =
			System.getProperty("user.dir") + fsep + settingsFileName;
		try {
			reader =
				new java.io.BufferedReader(new java.io.FileReader(fileName));
			String line = reader.readLine();
			while (line != null) {
				parseLine(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public Settings (String filename) {
		userName = System.getProperty("user.name");
		settingsFileName = filename;
	}

}
