package utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UniqueNumberUtil {

	private static Log logger = LogFactory.getLog(UniqueNumberUtil.class);

	public static String getUniqueId(int length) {
		try {
			Thread.sleep(10); // just to get a unique milli sec.???
		} catch (InterruptedException e) {
			logger.warn(e, e);
		}
		long currentTimeMillis = System.currentTimeMillis();
		String now = currentTimeMillis + "";
		 System.out.println(new Date(currentTimeMillis));
		String branchId = now.substring(now.length() - length, now.length());
		return branchId;
	}
	
	public static void main(String[] args) throws InterruptedException {
		List list=new ArrayList();
		while(true){
		 int length = 5; //higher the length the greater the no. of unique no. you get.
		String uniqueId = UniqueNumberUtil.getUniqueId(length);
		 if(list.contains(uniqueId)){
			 throw new RuntimeException("Id not unique"+ uniqueId);
		 }
		 list.add(uniqueId);
		 System.out.println(uniqueId);
		 Thread.sleep(10);
		}
		
	}
}
