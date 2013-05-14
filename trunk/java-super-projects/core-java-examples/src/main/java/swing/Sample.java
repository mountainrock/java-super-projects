/**
 * 
 */
package swing;

import java.awt.BorderLayout;

import javax.swing.JApplet;
import javax.swing.JPanel;

/**
 * 
 * @author Sandeep.Maloth
 * 
 */
public class Sample extends JApplet {

	private JPanel jContentPane = null;

	
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * This is the default constructor
	 */
	public Sample() {
		super();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void init()
	{
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
		}
		return jContentPane;
	}

}
