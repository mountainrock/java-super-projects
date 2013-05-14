package utils;

public class LogUtil {

	public static void logConsole(Object obj)
	{
		System.out.print(new Throwable().getStackTrace()[1] + " : ");
		System.out.println(obj);
	}
}
