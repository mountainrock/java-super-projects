package swing.assignments;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;


public class HangManFrame extends javax.swing.JFrame {
	private JTextField inputTextField;
	private JButton goButton;
	String dictionaryWord = "";
	int tries = 7;
	String letter = "";
	private JLabel messageLabel;
	String userAnswer = null;
	String[] inviDictionaryWord = null;
	String dictionary[] = { "liverpool", "panathinaikos", "blackburn", "gladbach", "valencia", "barcelona", "bolton", "chelsea", "arsenal", "everton", "wigan", "fulham", "portsmouth", "tottenham",
			"sunderland", "newcastle", "fiorentina", "palermo", "udinese", "villarreal", "mallorca" };
	private JLabel hangmanLabel;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				HangManFrame inst = new HangManFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public HangManFrame() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
			thisLayout.rowHeights = new int[] { 7, 7, 7, 7 };
			thisLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
			thisLayout.columnWidths = new int[] { 7, 7, 7, 7 };
			getContentPane().setLayout(thisLayout);
			{
				inputTextField = new JTextField();
				getContentPane().add(inputTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				inputTextField.setText("");
				inputTextField.setFont(new java.awt.Font("Tahoma", 1, 16));
			}
			{
				goButton = new JButton();
				getContentPane().add(goButton, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				goButton.setText("Go");
				goButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						goButtonActionPerformed(evt);
					}
				});
			}
			{
				hangmanLabel = new JLabel();
				getContentPane().add(hangmanLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				hangmanLabel.setFont(new java.awt.Font("Arial",1,22));
				hangmanLabel.setText("------");
			}
			{
				messageLabel = new JLabel();
				getContentPane().add(messageLabel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				messageLabel.setText("You will have 7 chances to play");
			}

			Random r = new Random();
			dictionaryWord = dictionary[r.nextInt(dictionary.length)];
			pack();
			inviDictionaryWord = new String[dictionaryWord.length()];
			play();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void play() {

		String hiddenWord = "";

		String inputAlphabet = inputTextField.getText();
		inputTextField.setText("");

		if (tries > 0) {
			if (dictionaryWord.indexOf(inputAlphabet) != (-1)) {
				for (int i = 0; i < dictionaryWord.length(); i++) {
					char charAt = dictionaryWord.charAt(i);
					if ((charAt + "").equalsIgnoreCase(inputAlphabet)) {
						inviDictionaryWord[i] = inputAlphabet;
					}
				}
			} else {
				tries--;
			}
			messageLabel.setText("You have " + tries + " wrong guesses left." );
		} else {
			messageLabel.setText("You Loose as you have exceeded 7 tries");
		}

		for (int i = 0; i < dictionaryWord.length(); i++) {
			if (inviDictionaryWord[i] == null) {
				inviDictionaryWord[i] = "_ ";
			}
			hiddenWord += inviDictionaryWord[i] + "";

		}
		if (hiddenWord.equalsIgnoreCase(dictionaryWord)) {
			messageLabel.setText("You have won. Congratulations!!");
		}
		hangmanLabel.setText(hiddenWord);
	}

	private void goButtonActionPerformed(ActionEvent evt) {
		play();
	}
}
