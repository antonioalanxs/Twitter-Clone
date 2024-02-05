package com.TwitterClone.ProjectBackend.security;

import com.TwitterClone.ProjectBackend.userManagement.RepositoryUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * In this method we disable the CSRF protection for know so our app can accept any request from any user.
     * @param http
     * @throws Exception
     */

    @Autowired
    private RepositoryUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/signup").permitAll();
        http.authorizeRequests().antMatchers("/error").permitAll();
        http.authorizeRequests().antMatchers("/logout").permitAll();


        // Login form
        http.formLogin().loginPage("/login");
        http.formLogin().usernameParameter("username");
        http.formLogin().passwordParameter("password");
        http.formLogin().defaultSuccessUrl("/home");
        http.formLogin().failureUrl("/error");

        // Private pages
        http.authorizeRequests().antMatchers("/home").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/notifications").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/bookmarks").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/write-tweet").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/edit-profile").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/follow/**").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/write-tweet/comment/**").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/tweets/reply-tweet/**").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/tweets/delete/**").hasAnyRole("USER", "ADMIN");
        //ADMIN Page
        http.authorizeRequests().antMatchers("/dashboard").hasAnyRole("ADMIN");
        http.authorizeRequests().antMatchers("/ban/**").hasAnyRole("ADMIN");
        http.authorizeRequests().antMatchers("/unbanned/**").hasAnyRole("ADMIN");
        http.authorizeRequests().antMatchers("/verify/**").hasAnyRole("ADMIN");
        http.authorizeRequests().antMatchers("/unverify/**").hasAnyRole("ADMIN");

        // Logout
        http.logout().logoutUrl("/logout");
        http.logout().logoutSuccessUrl("/");
        //http.csrf().disable();
        // Allow H2 console
        // http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.headers().frameOptions().sameOrigin();

    }
}