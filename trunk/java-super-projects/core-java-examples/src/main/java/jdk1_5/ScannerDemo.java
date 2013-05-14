/**
 * 
 */
package jdk1_5;

import java.util.Scanner;


public class ScannerDemo {

	
	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		while (s.hasNext()) {
			String value = s.next();
			System.out.println(value);
			System.out.println("Found " + s.findInLine(("[0-9]+")));
			System.out.println("Delimiter is  " + s.delimiter());
		}
		s.close();

	}

}
