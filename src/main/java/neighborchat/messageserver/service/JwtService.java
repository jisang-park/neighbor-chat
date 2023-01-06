package neighborchat.messageserver.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtService {

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;
    private final Key key;

    public JwtService(@Value("${jwt.access-token-expire-time}") long accessTokenExpireTime,
                      @Value("${jwt.refresh-token-expire-time}") long refreshTokenExpireTime,
                      @Value("${jwt.secret-key}") String jwtSecretKey) {
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpireTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpireTime;
        this.key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Authentication getAuthentication(String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            return null;
        }

        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
        Claims claims = parser.parseClaimsJws(accessToken).getBody();

        String role = (String) claims.get("role");
        Set<GrantedAuthority> authorities = Arrays.stream(role.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());

        Map<String, Object> attributes = Map.of("id", claims.get("id"));

        DefaultOAuth2User principal = new DefaultOAuth2User(authorities, attributes, "id");
        return new OAuth2AuthenticationToken(principal, authorities, (String) claims.get("provider"));
    }

    public String createAccessToken(Long id, String provider, String... role) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("id", id);
        claims.put("role", String.join(",", role));
        claims.put("provider", provider);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key)
                .compact();
    }

    public String reissueAccessToken(String token) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("id", getAttribute(token, "id", Long.class));
        claims.put("role", getAttribute(token, "role", String.class));
        claims.put("provider", getAttribute(token, "provider", String.class));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long id) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("id", id);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key)
                .compact();
    }

    public String reissueRefreshToken(String token) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("id", getAttribute(token, "id", Long.class));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key)
                .compact();
    }

    public boolean isExpired(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }

    private <T> T getAttribute(String token, String attribute, Class<T> clazz) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
        return parser.parseClaimsJws(token).getBody().get(attribute, clazz);
    }

}
