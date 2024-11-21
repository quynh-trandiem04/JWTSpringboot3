package vn.iostart.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${security.jwt.secret-key}")
	private String secretKey;
	
	@Value("${security.jwt.expiration-time}")
	private long jwtExpiration;
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}
	public long getExpirationTime() {
		return jwtExpiration;
	}
	
	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
	    return Jwts.builder()
	        .claims(extraClaims) // Thêm các claims tùy chỉnh
	        .subject(userDetails.getUsername()) // Lưu thông tin username vào subject
	        .issuedAt(new Date(System.currentTimeMillis())) // Thời điểm phát hành
	        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 30)) // Hạn sử dụng (30 giờ)
	        .signWith(getSignInKey(), Jwts.SIG.HS256) // Ký bằng thuật toán và khóa bí mật
	        .compact(); // Tạo JWT
	}
	public boolean isTokenValid(String token, UserDetails userDetails) {
	    final String username = extractUsername(token); // Trích xuất username từ token
	    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Xác thực token hợp lệ
	}
	private boolean isTokenExpired(String token) {
		
		return extractEpiration(token).before(new Date());
	}
	private Date extractEpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	private Claims extractAllClaims(String token) {
		return Jwts
				.parser()
				.verifyWith(getSignInKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
