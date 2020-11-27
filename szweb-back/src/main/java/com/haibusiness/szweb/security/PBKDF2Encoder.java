package com.haibusiness.szweb.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 *
 * @author Jinyu
 */
@Component
public class PBKDF2Encoder implements PasswordEncoder {
	
	@Value("${springbootjjwt.password.secret}")
	private String secret;

	@Value("${springbootjjwt.password.iteration}")
	private Integer iteration;

	@Value("${springbootjjwt.password.keylength}")
	private Integer keylength;
	
	/**
	 * More info (https://www.owasp.org/index.php/Hashing_Java)
	 * @param cs password
	 * @return encoded password
	 */
	@Override
	public String encode(CharSequence cs) {
		try {
			byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
											.generateSecret(new PBEKeySpec(cs.toString().toCharArray(), secret.getBytes(), iteration, keylength))
											.getEncoded();
			return Base64.getEncoder().encodeToString(result);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
			throw new RuntimeException(ex);
		}
	}
	@Override
	public boolean matches(CharSequence cs, String string) {
		return encode(cs).equals(string);
	}
	public static void main(String[] args) {
		System.out.println(new PBKDF2Encoder().encode("e10adc3949ba59abbe56e057f20f883e"));
		//2lyBWgZlsF9OEORLymlLAL/Wjfie2cEGMM9FIRLqm+M=
	}
}
