package com.TwitterClone.ProjectBackend.security;

import com.TwitterClone.ProjectBackend.security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(1)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    //Expose AuthenticationManager as a Bean to be used in other services
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher("/api/**");

        // URLs that need authentication to access to it
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/tweets/write-tweet").hasAnyRole("USER","ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/tweets/reply-tweet/**").hasAnyRole("USER", "ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/images/**").hasAnyRole("USER", "ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/profile/**").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/profile/followers/**").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/profile/followed/**").hasAnyRole("USER", "ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/bookmarks/**").hasAnyRole("USER", "ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/notifications/**").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/notifications/**").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/notifications/**").hasAnyRole("USER", "ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/tweet/**").hasAnyRole("USER","ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/tweet/**").hasAnyRole("USER","ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/tweet/**").hasAnyRole("USER","ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/tweet/**").hasAnyRole("USER","ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/verify/**").hasAnyRole("USER","ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/ban/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/unban/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/verify/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/unverify/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/statistics").hasRole("ADMIN");
        // Other URLs can be accessed without authentication
        http.authorizeRequests().anyRequest().permitAll();

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf().disable();

        // Disable Http Basic Authentication
        http.httpBasic().disable();

        // Disable Form login Authentication
        http.formLogin().disable();

        // Avoid creating session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add JWT Token filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
