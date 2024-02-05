package com.TwitterClone.ProjectBackend.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = getJwtToken(request, true);

            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

                String username = jwtTokenProvider.getUsername(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            LOG.error("Exception processing JWT Token", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtToken(HttpServletRequest request, boolean fromCookie) {

        if (fromCookie) {
            return getJwtFromCookie(request);
        }

        return getJwtFromRequest(request);
    }

    private String getJwtFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {

            String accessToken = bearerToken.substring(7);

            if (accessToken == null) {
                return null;
            }

            return SecurityCipher.decrypt(accessToken);
        }

        return null;
    }

    private String getJwtFromCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return "";
        }

        for (Cookie cookie : cookies) {

            if (JwtCookieManager.ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                String accessToken = cookie.getValue();

                if (accessToken == null) {
                    return null;
                }

                return SecurityCipher.decrypt(accessToken);
            }
        }

        return null;
    }
}
