package com.TwitterClone.ProjectBackend.userManagement;

import com.TwitterClone.ProjectBackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This service is dedicated to the management of the entity user. Specially whn loading
 * then by usiung the UserDetailsService interface form spring security
 */
@Service
@AllArgsConstructor

public class RepositoryUserDetailsService implements UserDetailsService {
    private final static String USER_NOT_FOUND = "User with username %username not found";
    @Autowired
    private UserRepository userRepository;
    /**
     * It searchs on the repository to see if it can find a user by the username. If not, it throws an
     * exception
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                roles);

    }


}
