package miscellaneous;

import java.security.Provider;
import java.security.Security;
import java.security.Provider.Service;

public class SunJCEDemo {
	public static void main(String[] args) throws Exception
	{

		Provider p = Security.getProvider("SunJCE");
		System.out.println(p.getInfo());
		Service service = sun.security.jca.GetInstance.getService("SunJCE", "SunJCE");
		System.out.println(service);
	}
}
