/**
 * 
 */
package interviewrelated;


public class ClassInInterfaceTest {

	
	public static void main(String[] args)
	{
		System.out.println("Non static Class within interface :" + new Face.DogFace().x);
		System.out.println("Static Class within interface :" + Face.CatFace.y);
		System.out.println("Abstract Class within interface :" + Face.HumanFace.z);
		System.out.println("Interface with class : " + new ClassInInterfaceTest.InnerFace() {
		}.x);
		System.out.println("Conclusion - It is possible to have class/interface within interface/class");
	}

	interface InnerFace {
		int x = 4;
	}

}

interface Face {
	class DogFace {
		int x = 10;
	}

	static class CatFace {
		static int y = 20;
	}

	abstract class HumanFace {
		static int z = 30;
	}

	interface EyeBall {

	}

}
