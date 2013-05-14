/**
 * 
 */
package email;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;


public class ValidateMail {

	
	public static void main(String[] args)
	{
		try {
			final String string = ".maloth!#$%^$%@mphasis.com";
			InternetAddress internetAddress = new InternetAddress(string, true);
			System.out.println(string);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
