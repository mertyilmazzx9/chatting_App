package technologies.proven.myilmaz;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Hashing {

	public static String generateSHA256Hash(String input) {
		try {
			// Create a MessageDigest instance for SHA-256
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// Perform the hash computation
			byte[] encodedhash = digest.digest(input.getBytes());

			// Convert byte array into a hexadecimal string
			StringBuilder hexString = new StringBuilder();
			for (byte b : encodedhash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
			
		}
	}
}
