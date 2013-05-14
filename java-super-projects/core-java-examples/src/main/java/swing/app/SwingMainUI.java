package swing.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utils.Constants;

public class SwingMainUI extends javax.swing.JFrame {

	private static Log logger = LogFactory.getLog(SwingMainUI.class.getName());

	private volatile ProcessResult extractionResult;

	private JButton processButton;

	private JTextField excelPathTextBox;

	private JButton browseExcelButton;

	private JPanel jPanel1;

	private JTextArea statusArea = new JTextArea();

	private ServiceImpl serviceImpl;

	public static void main(String[] args)
	{

		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				SwingMainUI inst = new SwingMainUI();
				inst.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent we)
					{
						System.exit(0);
					}
				});
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public SwingMainUI() {
		super();
		serviceImpl = new ServiceImpl();
		extractionResult = serviceImpl.getExtractionResult();
		extractionResult.addObserver(new ResultObserver(extractionResult, statusArea));
		initGUI();
	}

	private void initGUI()
	{
		try {
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				GridBagLayout jPanel1Layout = new GridBagLayout();
				jPanel1.setPreferredSize(new java.awt.Dimension(380, 247));
				jPanel1Layout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
				jPanel1Layout.rowHeights = new int[] { 7, 7, 7, 7 };
				jPanel1Layout.columnWeights = new double[] { 0.1, 0.0, 0.1, 0.1 };
				jPanel1Layout.columnWidths = new int[] { 7, 166, 7, 7 };
				jPanel1.setLayout(jPanel1Layout);
				{
					browseExcelButton = new JButton();
					jPanel1.add(browseExcelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					browseExcelButton.setText("Browse...");
					browseExcelButton.setFont(new java.awt.Font("Tahoma", 1, 12));
					browseExcelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt)
						{
							final JFileChooser fc = new JFileChooser();
							FileFilter filter = new FileFilter() {

								@Override
								public boolean accept(File f)
								{
									return f.isDirectory() || (f.getName().toLowerCase().endsWith(".xls")) ? true : false;
								}

								@Override
								public String getDescription()
								{
									return null;
								}

							};
							fc.setFileFilter(filter);
							// In response to a button click:
							int returnVal = fc.showOpenDialog(SwingMainUI.this);
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File file = fc.getSelectedFile();
								// This is where a real application would open the file.
								excelPathTextBox.setText(file.getAbsolutePath());
							}
						}
					});
				}
				{
					excelPathTextBox = new JTextField();
					jPanel1.add(excelPathTextBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					excelPathTextBox.setText("Please choose a file....");
					excelPathTextBox.setFont(new java.awt.Font("Tahoma", 1, 12));
				}
				{
					processButton = new JButton();
					jPanel1.add(processButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					processButton.setText("Process");
					processButton.setSize(81, 29);
					processButton.setFont(new java.awt.Font("Tahoma", 1, 16));
					processButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt)
						{
							new Thread() {

								@Override
								public void run()
								{
									processButton.setEnabled(false);
									statusArea.setText("Processing..... " + Constants.RETURN_NEW_LINE);
									try {
										serviceImpl.getExtractionResult().reset();
										serviceImpl.doProcess(excelPathTextBox.getText());
									} finally {
										processButton.setEnabled(true);
									}

								}

							}.start();

						}
					});
				}
				{
					JScrollPane scrollPane = new JScrollPane(statusArea);
					jPanel1.add(scrollPane, new GridBagConstraints(0, 2, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane.setPreferredSize(new Dimension(50, 250));
					{

						statusArea.setText("Status...");
						statusArea.setEditable(false);
						statusArea.setLineWrap(true);
						statusArea.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 0)));
						statusArea.setFont(new java.awt.Font("Tahoma", 1, 11));
						statusArea.setForeground(new java.awt.Color(64, 0, 128));
					}
				}
			}

			this.setSize(400, 274);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
