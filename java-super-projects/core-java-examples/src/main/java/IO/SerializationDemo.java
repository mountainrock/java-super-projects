/**
 * 
 */
package IO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Demonstrates that all the members of a serializable object must implement serializable interface
 * @author Sandeep.Maloth
 * 
 */
public class SerializationDemo {

	
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		SampleObject serializable = new SampleObject();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();// OutputStream(new File(""));

		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(serializable);
		byte[] bites = bos.toByteArray();

		ByteArrayInputStream bis = new ByteArrayInputStream(bites);
		ObjectInputStream ois = new ObjectInputStream(bis);
		SampleObject so = (SampleObject) ois.readObject();

		System.out.println("after de-serialization :" + so);

	}

}

class SampleObject implements Serializable {
	Object obj = new SerializableObject();

	public String toString()
	{

		return "I am just a serializable object";
	}
}

class SerializableObject implements Serializable {

}