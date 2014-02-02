package com.bri8.supermag.util;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;


public class DesEncryptionHelper {

	byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };
	int iterationCount = 19;
	SecretKey key;
	Cipher ecipher;
	Cipher dcipher;

	public DesEncryptionHelper(String passPhrase) {
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
		KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);

		try {
			this.key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			ecipher = Cipher.getInstance(key.getAlgorithm());
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher = Cipher.getInstance(key.getAlgorithm());
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public String encrypt(String str) {
		try {
			byte[] encryptedString = ecipher.doFinal(str.getBytes("UTF8"));
			return new String(Base64.encodeBase64(encryptedString));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String decrypt(String str) {
		try {
			byte[] dec = Base64.decodeBase64(str.getBytes());
			byte[] utf8 = dcipher.doFinal(dec);
			return new String(utf8, "UTF8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}