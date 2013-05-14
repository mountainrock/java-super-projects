package miscellaneous;

public class XORSample {
	static int ONE = 1;

	static int ZERO = 0;

	static int arr[] = { ONE, ZERO };

	
	public static void main(String[] args)
	{

		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i] + "^" + arr[i] + "=" + (arr[i] ^ arr[i]));
			for (int j = i; j < arr.length; j++) {

				System.out.println(arr[i] + "^" + arr[j] + "=" + (arr[i] ^ arr[j]));

			}

		}

	}

}
