package com.TwitterClone.ProjectBackend.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static long JWT_EXPIRATION_IN_MS = 5400000;
    private static Long REFRESH_TOKEN_EXPIRATION_MSEC = 10800000l;

    @Autowired
    private UserDetailsService userDetailsService;

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            LOG.debug("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            LOG.debug("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOG.debug("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOG.debug("Unsupported JWT exception");
        } catch (IllegalArgumentException ex) {
            LOG.debug("JWT claims string is empty");
        }

        return false;
    }

    public Token generateToken(UserDetails user) {

        Claims claims = Jwts.claims().setSubject(user.getUsername());

        claims.put("auth", user.getAuthorities().stream().map(s -> new SimpleGrantedAuthority("ROLE_" + s))
                .filter(Objects::nonNull).collect(Collectors.toList()));

        Date now = new Date();
        Long duration = now.getTime() + JWT_EXPIRATION_IN_MS;
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_IN_MS);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, 8);

        String token = Jwts.builder().setClaims(claims).setSubject((user.getUsername())).setIssuedAt(new Date())
                .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS256, jwtSecret).compact();

        return new Token(Token.TokenType.ACCESS, token, duration,
                LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));

    }

    public Token generateRefreshToken(UserDetails user) {

        Claims claims = Jwts.claims().setSubject(user.getUsername());

        claims.put("auth", user.getAuthorities().stream().map(s -> new SimpleGrantedAuthority("ROLE_" + s))
                .filter(Objects::nonNull).collect(Collectors.toList()));
        Date now = new Date();
        Long duration = now.getTime() + REFRESH_TOKEN_EXPIRATION_MSEC;
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_MSEC);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, 8);
        String token = Jwts.builder().setClaims(claims).setSubject((user.getUsername())).setIssuedAt(new Date())
                .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS256, jwtSecret).compact();

        return new Token(Token.TokenType.REFRESH, token, duration,
                LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));

    }
}
