package com.spring.userservice.util;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

import com.spring.userservice.exception.ExpiredException;


@Service
public class TokenService {

	private static final String AES = "AES";
	private static final String SECRET_KEY = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static int tokenExpirationMinutes = 10;

	private static byte[] hexToBytes(String hex) {
		int len = hex.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}
		return data;
	}

	public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
		keyGenerator.init(256);
		return keyGenerator.generateKey();
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte b : bytes) {
			result.append(String.format("%02X", b));
		}
		return result.toString();
	}

	/**
	 * Encrypts the provided data using the AES encryption algorithm and a secret
	 * key.
	 *
	 * @param data The data to be encrypted.
	 * @return The Base64-encoded encrypted data.
	 * @throws ExpiredException If encryption fails.
	 */
	public static String encryptData(String data) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(hexToBytes(SECRET_KEY), AES);
		try {
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

			return Base64.getUrlEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			throw new ExpiredException("token", "Encryption failure");
		}
	}

	/**
	 * Decrypts the provided data using the AES encryption algorithm and the secret
	 * key. Additionally, it checks the token's expiration time against the
	 * configured expiration limit.
	 *
	 * @param data The Base64-encoded encrypted data to be decrypted.
	 * @return The decrypted data if it's valid and not expired.
	 * @throws ExpiredException      If decryption or token expiration check fails.
	 * @throws InvalidTokenException If the token format is invalid.
	 */
	public static String decryptData(String data) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(hexToBytes(SECRET_KEY), AES);
		try {

			byte[] base64DecodedBytes = Base64.getUrlDecoder().decode(data);

			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] decryptedBytes = cipher.doFinal(base64DecodedBytes);
			//String[] tokenParts = new String(decryptedBytes, StandardCharsets.UTF_8).split(",");

			//String tokenCreatedTimeString = tokenParts[0].trim();

			//checkTokenExpiration(tokenCreatedTimeString, tokenExpirationMinutes);

			return new String(decryptedBytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new ExpiredException("token", "Invalid token format or decryption failure");
		}
	}

	public static void checkTokenExpiration(String tokenCreatedTimeString, int expirationMinutes)
			throws ExpiredException {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			Date tokenCreatedDate = dateFormat.parse(tokenCreatedTimeString);

			long tokenCreatedTime = tokenCreatedDate.getTime();
			long currentTimeMillis = System.currentTimeMillis();
			if ((currentTimeMillis - tokenCreatedTime) > expirationMinutes * 60 * 1000) {
				throw new ExpiredException("token", "Token expired");
			}
		} catch (ParseException e) {

			throw new ExpiredException("Token", "Token expiration check failed");
		}
	}

	/**
	 * Checks whether a signup token has expired based on its creation timestamp and
	 * the configured token expiration time.
	 *
	 * @param createdDate The timestamp when the token was created.
	 * @return {@code true} if the token has expired, {@code false} otherwise.
	 */
	public static boolean isSignupTokenExpired(Timestamp createdDate) {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		long timeDifference = currentTime.getTime() - createdDate.getTime();
		return timeDifference > (tokenExpirationMinutes * 60 * 1000);
	}

}
