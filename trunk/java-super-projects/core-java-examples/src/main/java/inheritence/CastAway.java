package inheritence;

/**
 * Paat: Baap ko bete mein convert karnekeliye casting karo.. Rarely used...Will give ClassCastException anyway..??
 * @file name : CastAway.java
 * @author : Sandeep.Maloth
 * @Creation Date :Dec 27, 2006
 */
public class CastAway extends Baap {

	
	public static void main(String[] args)
	{
		CastAway b = new CastAway();
		// b.naam();
		// Beta beta = (Beta)b;
		// beta.naam();

	}

}

abstract class Baap {
	protected String name;

	Baap(String name) {
		this.name = name;
	}

	public Baap() {
		// TODO Auto-generated constructor stub
	}

	void naam()
	{
		System.out.println("Baap ka naam .." + name);
	}
}

class Beta extends Baap {

	Beta(String name) {
		super(name);
	}

	void naam()
	{
		System.out.println("Bete ka naam .." + name);
	}
}