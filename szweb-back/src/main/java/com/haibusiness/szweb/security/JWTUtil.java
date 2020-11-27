package com.haibusiness.szweb.security;

import com.haibusiness.szweb.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jinyu
 */
@Component
public class JWTUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Value("${springbootjjwt.jjwt.secret}")
	private String secret;
	
	@Value("${springbootjjwt.jjwt.expiration}")
	private String expirationTime;
	
	public Claims getAllClaimsFromToken(String token) {
		System.out.println("token= "+token);
		return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes())).parseClaimsJws(token).getBody();
	}
	public String getUsernameFromToken(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}
	public Date getIssuedAtDateFromToken(String token) {
		return getAllClaimsFromToken(token).getIssuedAt();
	}
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordUpdate) {
		return (lastPasswordUpdate != null && created.before(lastPasswordUpdate));
	}
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String username) {
		Long expirationTimeLong = Long.parseLong(expirationTime); //in second
		SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(createdDate)
				.setExpiration(expirationDate)
				.signWith(key,SignatureAlgorithm.HS512)
				.compact();
	}
	public Boolean validateToken(String token, UserDetails userDetails) {
		User user = (User) userDetails;
		final String username = getUsernameFromToken(token);
		final Date created = getIssuedAtDateFromToken(token);
//        final Date expiration = getExpirationDateFromToken(token);
//        如果token存在，且token创建日期 > 最后修改密码的日期 则代表token有效
		return (
				username.equals(user.getUsername())
						&& !isTokenExpired(token)
						&& !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetTime())
		);
	}
	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

}
