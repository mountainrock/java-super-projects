/**
 * 
 */
package encyption;

import java.io.File;

import IO.IOUtility;


public class XOR // extends Cryptographer
{
	static String FILE_LOCATION = "C:\\Program Files\\Yahoo!\\Messenger\\Profiles\\gudy2dudy\\Archive\\Messages\\mahendra_tele\\20060203-talk2sandeepm.dat";
	static String USER = "talk2sandeepm";

	/** Initialize xor cryptographer using given string as the encryption and decryption private key. */

	public XOR(String privateKey) {
		int len = privateKey.length(); // length of the private key string

		key = new byte[len]; // create array of bytes for the key

		privateKey.getBytes(0, len, key, 0); // convert string into array of bytes
	}

	/** Initialize xor cryptographer using standard private key. */

	public XOR() {
		this("R1@t5&*43,.:{=zDy)_y+}]|y&7k5/#3gt,{szq!`:+"); // use default key
	}

	/** Encrypts the given block of data. */

	public void encrypt(byte[] data, int offset, int length)
	{
		for (int i = offset, j = offset + length; i < j; i++) // encrypt each byte in the data
		{
			byte b = data[i];

			data[i] ^= key[count++ % key.length]; // xor current byte with key and previous byte

			previous = b; // remember current uncrypted byte (will be used to encrypt the next)
		}
	}

	/** Decrypts the given block of data. */

	public void decrypt(byte[] data, int offset, int length)
	{
		for (int i = offset, j = offset + length; i < j; i++) // encrypt each byte in the data
		{
			data[i] ^= key[count++ % key.length]; // xor current byte with key and previous byte

			previous = data[i]; // remember current byte (will be used to encrypt the next)
		}
	}

	/** The key used for encryption. */

	private byte[] key = null;

	/** Index in key of the byte that will be used to encrypt the next byte. */

	private int count = 0;

	/** The last byte encrypted (used to encrypt following byte). */

	private byte previous = (byte) 0xE3; // this is just some random number (so that the first byte also looks like it's encrypted)

	public static void main(String[] args)
	{
		XOR xor = new XOR(USER);
		StringBuffer sbuf = IOUtility.readFile(new File(FILE_LOCATION));
		final byte[] inputBytes = sbuf.toString().getBytes();
		xor.decrypt(inputBytes, 0, inputBytes.length);
		System.out.println(new String(inputBytes));
	}
}
