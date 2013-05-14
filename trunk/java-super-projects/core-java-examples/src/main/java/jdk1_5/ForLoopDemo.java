package jdk1_5;

import java.util.ArrayList;

public class ForLoopDemo {

	
	public static void main(String[] args)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(1);
		// simpler than iterator.
		// Autoboxing and Auto-Unboxing
		for (Integer i : list) {
			System.out.println(i);
		}

	}

}
