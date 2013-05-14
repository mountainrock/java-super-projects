/**
 * 
 */
package jdk1_5;


public class EnumDemo {
	public enum PrimaryColours {
		red, blue, green
	};

	
	public static void main(String[] args)
	{
		System.out.println(PrimaryColours.valueOf("red"));
	}

}
