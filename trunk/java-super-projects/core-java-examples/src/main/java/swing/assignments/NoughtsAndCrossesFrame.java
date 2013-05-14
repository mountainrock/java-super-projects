package swing.assignments;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Scanner;

import javax.net.ssl.SSLEngineResult.Status;
import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;


public class NoughtsAndCrossesFrame extends javax.swing.JFrame {
	protected JButton jButton1;
	protected JButton jButton2;
	protected JButton jButton8;
	protected JLabel statusLabel;
	protected JButton jButton9;
	protected JButton jButton7;
	protected JButton jButton6;
	protected JButton jButton5;
	protected JButton jButton4;
	protected JButton jButton3;
	private JButton resetButton;

	protected JButton[][] grid = new JButton[2][2];
	private int boardSize = 3;
	private int letter;
	private int count = 0;
	private int[][] board;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				NoughtsAndCrossesFrame inst = new NoughtsAndCrossesFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);

			}
		});
	}

	public NoughtsAndCrossesFrame() {
		super();
		initGUI();
		JButton tempGrid[][] = { { jButton1, jButton2, jButton3 }, { jButton4, jButton5, jButton6 }, { jButton7, jButton8, jButton9 } };
		grid = tempGrid;
		initBoard();
	}

	private void initBoard() {
		boardSize = 3;
		count = 0;
		board = new int[boardSize][boardSize];
		statusLabel.setText("Please start");
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				board[i][j] = 2;
				grid[i][j].setText(".");
			}
		}
	}

	protected void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
			thisLayout.rowHeights = new int[] { 7, 7, 7, 7 };
			thisLayout.columnWeights = new double[] { 0.1, 0.0, 0.1, 0.1 };
			thisLayout.columnWidths = new int[] { 7, 106, 7, 7 };
			getContentPane().setLayout(thisLayout);
			getContentPane().setBackground(new java.awt.Color(255,255,255));
			{
				jButton1 = new JButton();
				getContentPane().add(jButton1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton1.setText(" ");
				jButton1.setBackground(new java.awt.Color(255,255,255));
				jButton1.setForeground(new java.awt.Color(255,128,0));
				jButton1.setFont(new java.awt.Font("Tahoma",1,22));
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton1ActionPerformed(evt);
					}
				});
			}
			{
				jButton2 = new JButton();
				getContentPane().add(jButton2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton2.setText(" ");
				jButton2.setBackground(new java.awt.Color(255,255,255));
				jButton2.setForeground(new java.awt.Color(255,128,0));
				jButton2.setFont(new java.awt.Font("Tahoma",1,22));
				jButton2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton2ActionPerformed(evt);
					}
				});
			}
			{
				jButton3 = new JButton();
				getContentPane().add(jButton3, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton3.setText(" ");
				jButton3.setBackground(new java.awt.Color(255,255,255));
				jButton3.setForeground(new java.awt.Color(255,128,0));
				jButton3.setFont(new java.awt.Font("Tahoma",1,22));
				jButton3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton3ActionPerformed(evt);
					}
				});
			}
			{
				jButton4 = new JButton();
				getContentPane().add(jButton4, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton4.setText(" ");
				jButton4.setBackground(new java.awt.Color(255,255,255));
				jButton4.setForeground(new java.awt.Color(255,128,0));
				jButton4.setFont(new java.awt.Font("Tahoma",1,22));
				jButton4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton4ActionPerformed(evt);
					}
				});
			}
			{
				jButton5 = new JButton();
				getContentPane().add(jButton5, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton5.setText(" ");
				jButton5.setBackground(new java.awt.Color(255,255,255));
				jButton5.setForeground(new java.awt.Color(255,128,0));
				jButton5.setFont(new java.awt.Font("Tahoma",1,22));
				jButton5.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton5ActionPerformed(evt);
					}
				});
			}
			{
				jButton6 = new JButton();
				getContentPane().add(jButton6, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton6.setText(" ");
				jButton6.setBackground(new java.awt.Color(255,255,255));
				jButton6.setForeground(new java.awt.Color(255,128,0));
				jButton6.setFont(new java.awt.Font("Tahoma",1,22));
				jButton6.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton6ActionPerformed(evt);
					}
				});
			}
			{
				jButton7 = new JButton();
				getContentPane().add(jButton7, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton7.setText(" ");
				jButton7.setBackground(new java.awt.Color(255,255,255));
				jButton7.setForeground(new java.awt.Color(255,128,0));
				jButton7.setFont(new java.awt.Font("Tahoma",1,22));
				jButton7.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton7ActionPerformed(evt);
					}
				});
			}
			{
				jButton8 = new JButton();
				getContentPane().add(jButton8, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton8.setText(" ");
				jButton8.setBackground(new java.awt.Color(255,255,255));
				jButton8.setForeground(new java.awt.Color(255,128,0));
				jButton8.setFont(new java.awt.Font("Tahoma",1,22));
				jButton8.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton8ActionPerformed(evt);
					}
				});
			}
			{
				jButton9 = new JButton();
				getContentPane().add(jButton9, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton9.setText(" ");
				jButton9.setBackground(new java.awt.Color(255,255,255));
				jButton9.setForeground(new java.awt.Color(255,128,0));
				jButton9.setFont(new java.awt.Font("Tahoma",1,22));
				jButton9.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton9ActionPerformed(evt);
					}
				});
			}
			{
				statusLabel = new JLabel();
				getContentPane().add(statusLabel, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				statusLabel.setFont(new java.awt.Font("Tahoma",1,16));
				statusLabel.setForeground(new java.awt.Color(0,128,0));
				statusLabel.setText("Status");
			}
			{
				resetButton = new JButton();
				getContentPane().add(resetButton, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				resetButton.setText("Reset");
				resetButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						resetButtonActionPerformed(evt);
					}
				});
			}
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void jButton1ActionPerformed(ActionEvent evt) {
		play(0, 0);
	}

	protected void jButton2ActionPerformed(ActionEvent evt) {
		play(0, 1);
	}

	protected void jButton3ActionPerformed(ActionEvent evt) {
		play(0, 2);
	}

	protected void jButton4ActionPerformed(ActionEvent evt) {
		play(1, 0);
	}

	protected void jButton5ActionPerformed(ActionEvent evt) {
		play(1, 1);
	}

	protected void jButton6ActionPerformed(ActionEvent evt) {
		play(1, 2);
	}

	protected void jButton7ActionPerformed(ActionEvent evt) {
		play(2, 0);
	}

	protected void jButton8ActionPerformed(ActionEvent evt) {
		play(2, 1);
	}

	protected void jButton9ActionPerformed(ActionEvent evt) {
		play(2, 2);
	}

	String convert(int s) {
		if (s == 0)
			return "O";
		if (s == 1)
			return "X";
		return ".";
	}

	boolean rowAllSame(int i, int s) {
		boolean same = true;
		for (int j = 0; j < boardSize && same; j++) {
			same = (board[i][j] == s);
		}
		return same;
	}

	boolean colAllSame(int i, int s) {
		boolean same = true;
		for (int j = 0; j < boardSize && same; j++) {
			same = (board[j][i] == s);
		}
		return same;
	}

	boolean leftDiagAllSame(int s) {
		boolean same = true;
		for (int j = 0; j < boardSize && same; j++) {
			same = (board[j][j] == s);
		}
		return same;
	}

	boolean rightDiagAllSame(int s) {
		boolean same = true;
		for (int j = 0; j < boardSize && same; j++) {
			same = (board[j][boardSize - j - 1] == s);
		}
		return same;
	}

	void drawGrid() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++)
				grid[i][j].setText(convert(board[i][j]));
		}
	}

	boolean win(int s) {
		boolean win = false;
		for (int j = 0; j < boardSize && !win; j++)
			win = rowAllSame(j, s);
		if (win)
			return true;
		for (int j = 0; j < boardSize && !win; j++)
			win = colAllSame(j, s);
		if (win)
			return true;
		win = leftDiagAllSame(s);
		if (win)
			return true;
		else
			return rightDiagAllSame(s);
	}

	void computerRandomMove(int s) {
		int freeSpaces = boardSize * boardSize - count;
		if (freeSpaces != 0) {
			Random r = new Random();
			int pos = r.nextInt(freeSpaces) + 1;
			boolean found = false;
			int k = 0;
			for (int i = 0; i < boardSize && !found; i++)

				for (int j = 0; j < boardSize && !found; j++) {
					if (board[i][j] == 2)
						k++;
					if (k == pos) {

						board[i][j] = s;
						found = true;
					}

				}
		}
	}

	boolean checkWin(int s) {
		boolean b = win(s);
		if (b) {
			String convert = convert(s);
			if (convert.equals("O")) {
				statusLabel.setText("You win! Well done!!");
			} else {
				statusLabel.setText("Computer wins!");
			}

		}
		return b;
	}

	boolean checkDraw() {
		boolean b = count == boardSize * boardSize;
		if (b)
			statusLabel.setText("DRAW");
		return b;
	}

	public void play(int x, int y) {
		drawGrid();

		boolean finished = false;

		letter = 0;
		board[x][y] = letter;
		drawGrid();
		count++;
		finished = checkWin(letter);
		if (!finished) {
			finished = checkDraw();
			if (!finished)
				;
			{
				letter = 1;
				computerRandomMove(letter);
				drawGrid();
				count++;
				finished = checkWin(letter);
				if (!finished)
					finished = checkDraw();
			}

		}
		if (!finished) {
			statusLabel.setText("Your move!");
		}

	}

	private void resetButtonActionPerformed(ActionEvent evt) {
		initBoard();
	}

}
