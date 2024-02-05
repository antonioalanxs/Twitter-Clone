package com.TwitterClone.ProjectBackend.userManagement;

import com.TwitterClone.ProjectBackend.DTO.RegisteredRequest;
import com.TwitterClone.ProjectBackend.repository.UserRepository;
import com.TwitterClone.ProjectBackend.security.EmailValidator;
import com.TwitterClone.ProjectBackend.service.MailService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * This class is on charge of implementing all the model part of the signUp process and contains all its logic.
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserService {

    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;


    /**
     * It takes the parameters of the request, test if they are valid and them registered a new user
     *
     * @param request
     * @return
     */
    @Transactional
    public boolean signup(RegisteredRequest request) throws MessagingException, IOException {
        User user = new User(request.getUsername(), request.getPassword(), request.getEmail(), "USER");

        if (!emailValidator.test(request.getEmail())) {
            return false;
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return false;
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        user.setImages(new String[]{"example_data/Default_profilepic.jpg", "example_data/Default_profilebanner.jpg"});
        userRepository.save(user);

        mailService.sendVerificationEmail(user, user.getEmail());

        return true;
    }



    /**
     * Verify the new user
     *
     * @param verificationCode
     * @return
     */
    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isLoggedIn()) {
            return false;
        }

        user.setVerificationCode(null);
        user.setEnabled(true);
        userRepository.save(user);

        return true;
    }

    /**
     * Creates a token for the reset password form
     *
     * @param token
     * @param email
     */
    public void updateResetPasswordToken(String token, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        }
    }

    /**
     * Obtain a user using the token associated with
     *
     * @param token
     * @return
     */
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token).orElseThrow();
    }

    /**
     * Updates the current password to the new one
     *
     * @param user
     * @param newPassword
     */
    public void updatePassword(User user, String newPassword) {

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    /**
     * Obtain recommended users
     *
     * @param userId
     * @return
     */
    public List<User> getRecommendedUsers(Long userId) {
        return userRepository.findRecommendedUsers(userId);
    }

    /**
     * Obtains user that has a relation with the keyword
     *
     * @param keyword
     * @return
     */
    public List<User> findByUsernameContainingIgnoreCase(String keyword) {
        return this.userRepository.findByUsernameContainingIgnoreCase(keyword);
    }

    public User findByMail(String mail){
        return this.userRepository.findByEmail(mail).orElse(null);
    }
}