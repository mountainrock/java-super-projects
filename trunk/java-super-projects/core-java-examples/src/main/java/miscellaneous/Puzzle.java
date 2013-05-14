package miscellaneous;

import java.util.ArrayList;
import java.util.List;

/**
 * In a town a rice merchant used to sell his rice with weights, which are of diff kgs. The merchant never sell more than 47kgs. Merchant used to weigh what ever Kilogram of rice asked by the customer
 * in single weighing. The merchant used sell in rice in integers only (NO 4and half kg, 3 kg 200 grams and all). So the question is what is the minimum number of weighs the merchant nee to attain
 * this and what are they?
 * @author Sandeep.Maloth
 * 
 */
public class Puzzle {
	public static int MAX = 47;

	
	public static void main(String[] args)
	{
		List answer = new ArrayList();
		for (int i = 1; i < MAX - 1; i++) {
			if (isRequiredWeight(i, answer)) {
				answer.add(new Integer(i));
			}
		}
		System.out.println("The required weights are : " + answer);

	}

	// TODO: the weights may be balanced ....eg 5 kg weight can be got from 7-2=5
	private static boolean isRequiredWeight(int inputNumber, List list)
	{
		for (int i = 0; i < list.size(); i++) {
			Integer intr = (Integer) list.get(i);
			if (list.size() >= 2) {
				for (int j = i + 1; j < list.size(); j++) {
					Integer intrNext = (Integer) list.get(j);
					int tempSum = intr.intValue() + intrNext.intValue();
					if (tempSum == inputNumber) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
