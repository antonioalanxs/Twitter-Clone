package com.TwitterClone.ProjectBackend.service;

import com.TwitterClone.ProjectBackend.userManagement.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Send an email with the necessary information to reset the password
     *
     * @param recipientEmail
     * @param link
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendResetPasswordMail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("twitterclone027@gmail.com\n", "Twitter Clone Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    /**
     * Send a verification email to the new user
     *
     * @param user
     * @param userMail
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendVerificationEmail(User user, String userMail)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = userMail;
        String fromAddress = "twitterclone027@gmail.com";
        String senderName = "Twitter clone";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Twitter Clone.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = "https://localhost:8443/verification?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public void sendBanMail(String recipientEmail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("twitterclone027@gmail.com\n", "Twitter Clone Support");
        helper.setTo(recipientEmail);

        String subject = "Your Account has been banned!";

        String content = "<p>Hello,</p>"
                + "<p>Due to illegal actions against the app, the administrator has decided to ban your account until further notice." +
                "If you believe that a mistake has been made, please contact our personnel though this email address.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void sendUnBanMail(String recipientEmail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("twitterclone027@gmail.com\n", "Twitter Clone Support");
        helper.setTo(recipientEmail);

        String subject = "Your Account has been Unbanned!";

        String content = "<p>Hello,</p>"
                + "<p>After reviewing the status of your account, our staff have decided to unbanned your account. " +
                "From now on, you are free to enter your TwitterClone account again.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

}
