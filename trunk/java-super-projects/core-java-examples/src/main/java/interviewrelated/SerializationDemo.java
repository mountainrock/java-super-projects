/**
 * 
 */
package interviewrelated;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class SerializationDemo {

	
	public static void main(String[] args)
	{
		MySerializable object = new MySerializable();

		// Serialize to a file
		ObjectOutput out;
		try {
			out = new ObjectOutputStream(new FileOutputStream("d://filename.ser"));
			out.writeObject(object);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Serialize to a byte array
		ByteArrayOutputStream bos;
		try {
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.close();
			// Get the bytes of the serialized object
			byte[] buf = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

/**
 * Learning : All the members of a serializable class must also be serializable
 * 
 * @author Sandeep.Maloth
 * 
 */
class MySerializable implements Serializable {
	int p = 3;

	/**
	 * NonSerializable n = new NonSerializable();//Give non serializable exception.
	 */
	Shape s = new Shape() {
	};
}

interface Shape extends Serializable {

}

class NonSerializable {

}