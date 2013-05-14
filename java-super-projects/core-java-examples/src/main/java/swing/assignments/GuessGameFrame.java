package swing.assignments;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Random;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class GuessGameFrame extends javax.swing.JFrame {
	private JButton tooBigButton;
	private JButton tooSmallButton;
	private JButton correctButton;
	private JTextArea statusTextArea;
	private JLabel guessTextLabel;
	private JLabel guessLabel;
	// Create the random number generator
	Random rand = new Random();
	int lower_limit = 0;
	int upper_limit = 1000;

	int guess; // The number guessed by the PC
	int number_of_guesses = 0;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GuessGameFrame inst = new GuessGameFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public GuessGameFrame() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			thisLayout.rowWeights = new double[] { 0.1, 0.0, 0.1, 0.1 };
			thisLayout.rowHeights = new int[] { 7, 116, 7, 7 };
			thisLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
			thisLayout.columnWidths = new int[] { 7, 7, 7, 7 };
			getContentPane().setLayout(thisLayout);
			{
				tooBigButton = new JButton();
				getContentPane().add(tooBigButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				tooBigButton.setText("Too Big");
				tooBigButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						tooBigButtonActionPerformed(evt);
					}
				});
			}
			{
				tooSmallButton = new JButton();
				getContentPane().add(tooSmallButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				tooSmallButton.setText("Too Small");
				tooSmallButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						tooSmallButtonActionPerformed(evt);
					}
				});
			}
			{
				correctButton = new JButton();
				getContentPane().add(correctButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				correctButton.setText("Correct");
				correctButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						correctButtonActionPerformed(evt);
					}
				});
			}
			{
				guessLabel = new JLabel();
				getContentPane().add(guessLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				guessLabel.setText("0");
				guessLabel.setFont(new java.awt.Font("Tahoma", 1, 20));
				guessLabel.setForeground(new java.awt.Color(0, 128, 0));
			}
			{
				guessTextLabel = new JLabel();
				getContentPane().add(guessTextLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				guessTextLabel.setText("The number may be: ");
				guessTextLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
			}
			{
				statusTextArea = new JTextArea();
				getContentPane().add(statusTextArea, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				statusTextArea.setForeground(new java.awt.Color(0, 255, 0));
				statusTextArea.setBackground(new java.awt.Color(0, 0, 0));
				statusTextArea.setFont(new java.awt.Font("Tahoma", 1, 12));
				statusTextArea.setText("Status");
			}
			guess(-1);
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Guess a number based on the user response
	 * 
	 * @param response
	 */
	private void guess(int response) {

		// generate a random number between the lower_limit and the upper limit


		if (response == 2) {// The guessed number by the computer is too big
			upper_limit = guess;
		} else if (response == 1) {
			lower_limit = guess + 1;
		} else if (response == 0) {
			statusTextArea.setText("Correct! The number is: " + guess);
			statusTextArea.setText("The number of guesses is: " + number_of_guesses);
			return;
		}

		// If the different between the lower limit and upper limit is 1,
		// This means that the lower_limit may be the number
		// Ask the user if this is the case, if not, the user must be cheating
		// User must be cheating.
		if (upper_limit - lower_limit <= 1) {
			guessLabel.setText(lower_limit + "");
			number_of_guesses++;

			statusTextArea.setText("Is this correct? ");

			if (response == 0) {
				guessLabel.setText("" + lower_limit);
				statusTextArea.setText("Correct! The number of guesses is: " + number_of_guesses);
			} else {
				statusTextArea.setText("User is cheating");
			}
		}

		guess = rand.nextInt(upper_limit - lower_limit) + lower_limit;
		guessLabel.setText(guess + "");
		number_of_guesses++;
	}

	private void tooBigButtonActionPerformed(ActionEvent evt) {
		guess(2);
	}

	private void tooSmallButtonActionPerformed(ActionEvent evt) {
		guess(1);
	}

	private void correctButtonActionPerformed(ActionEvent evt) {
		guess(0);
	}
}
