/**
 * 
 */
package performance;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;


public class OSCacheDemo {

	
	public static void main(String[] args) throws Throwable
	{
		GeneralCacheAdministrator admin = new GeneralCacheAdministrator();
		admin.putInCache("abc", new Object());
		System.out.println(admin.getFromCache("abc"));
		admin.flushEntry("abc");
		// admin.getCache().
		try {
			System.out.println("Flushed ...." + admin.getFromCache("abc"));

		} catch (NeedsRefreshException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			admin.putInCache("abc", new Object());
			System.out.println(admin.getFromCache("abc"));
		}

	}

}
