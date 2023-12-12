package org.example.springboottemplate.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final String secret_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsS0WA7NhttzE8c08PH2B" +
            "fwZbBP527WQMi9zNS35of8Lq51jPjl0UvsI3VbPPzpvkcPTHNIWPl5mccrLoNbsy" +
            "UJc4PwmoneKAXSd+a9Tn91izb7hwizsdHi9gG0dAwUsF5lczCi4pC7fQJm/m1acp" +
            "g1MxVO6z6UhDp8YyJpeqd4fp+KuC/J6BSnDJ9QCxg4F/sm5v1vpmADiPru9WmK6R" +
            "HyYu9JXb3kO2yxltI4pi22Eod5Yvbo6zC2+/odFE7JFCI0n8LmQgvGFr0ruEagZG" +
            "DKH1mMQvng8dqMCRRATcwaPjUkcxvYZI3qND5cGa9FhB7BvxwX2nMHMmg6FenC2Z" +
            "5wIDAQAB";

    public String getUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }
    public String generateToken(Map<String, Object> claims,
                                UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUserEmail(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(Date.from(Instant.now()));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
