package swing.assignments;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


public class SpellCheckerFrame extends javax.swing.JFrame {
	private JButton spellCheckButton;
	private JLabel jLabel1;
	private JTextField inputTextField;
	String[] dictionary;
	private JTextField dictionaryTextField;
	private JLabel jLabel2;
	private JLabel jLabel3;
	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SpellCheckerFrame inst = new SpellCheckerFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public SpellCheckerFrame() {
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
			getContentPane().setForeground(new java.awt.Color(0,128,0));
			getContentPane().setBackground(new java.awt.Color(255,255,255));
			{
				spellCheckButton = new JButton();
				getContentPane().add(spellCheckButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				spellCheckButton.setText("Spell Check");
				spellCheckButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						spellCheckButtonActionPerformed(evt);
					}
				});
			}
			{
				inputTextField = new JTextField();
				getContentPane().add(inputTextField, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				inputTextField.setText(" ");
				inputTextField.setFont(new java.awt.Font("Tahoma",1,16));
				inputTextField.setPreferredSize(new java.awt.Dimension(156, 35));
				inputTextField.setForeground(new java.awt.Color(128,0,128));
				inputTextField.setBackground(new java.awt.Color(255,255,128));
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jLabel1.setForeground(new java.awt.Color(0,128,0));
				jLabel1.setText("Status :");
			}
			{
				dictionaryTextField = new JTextField();
				getContentPane().add(dictionaryTextField, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				dictionaryTextField.setText("c:\\dictionary.txt");
			}
			{
				jLabel2 = new JLabel();
				getContentPane().add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel2.setText("Please enter a word to search : ");
			}
			{
				jLabel3 = new JLabel();
				getContentPane().add(jLabel3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel3.setText("Dictionary file path :                   ");
			}
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void spellCheckButtonActionPerformed(ActionEvent evt) {
		String dictionary ;
		try {
			dictionary= readFileAsString(dictionaryTextField.getText());
		} catch (IOException e) {
			jLabel1.setText("Dictionary not found");
			e.printStackTrace();
			return;
		}
		String[] words = dictionary.split("\r\n");
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			System.out.println(word);
			if(word.equalsIgnoreCase(inputTextField.getText().trim())){
				jLabel1.setText("Word found in dictionary");
				return;
			}
		}
		jLabel1.setText("Word not found");

	}

	private String readFileAsString(String filePath) throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

}
