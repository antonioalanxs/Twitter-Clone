package com.TwitterClone.ProjectBackend.controller;

import com.TwitterClone.ProjectBackend.model.mustacheObjects.InformationManager;
import com.TwitterClone.ProjectBackend.service.MailService;
import com.TwitterClone.ProjectBackend.service.NotificationService;
import com.TwitterClone.ProjectBackend.service.ProfileService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Manages all the petitions relation with the profile
 */
@Controller
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private InformationManager informationManager;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MailService mailService;

    /**
     * Update the user profile
     *
     * @param request
     * @param banner
     * @param profile
     * @param nickname
     * @param biography
     * @return
     * @throws IOException
     */
    @PostMapping("/profile/edit-profile")
    public String editProfile(HttpServletRequest request,
                              @RequestParam MultipartFile banner,
                              @RequestParam MultipartFile profile,
                              @RequestParam String nickname,
                              @RequestParam String biography) throws IOException {
        User currentUser = this.informationManager.getCurrentUser(request);
        this.profileService.uploadProfile(currentUser, banner, profile, nickname, biography);
        String id = currentUser.getId().toString();
        return "redirect:/profile/" + id;
    }

    /**
     * Obtains some followed user with AJAX
     *
     * @param model
     * @param from
     * @param size
     * @param username
     * @return
     */
    @GetMapping("/followed/{username}/{from}/{size}")
    public String getFollowed(Model model,
                              @PathVariable int from,
                              @PathVariable int size,
                              @PathVariable String username) {
        User user = this.profileService.findByUsername(username).get();
        Long countFollowed = this.profileService.countFollowed(user.getId());

        if (countFollowed <= from) {
            return "redirect:/";
        }

        List<User> followed = profileService.getFollowed(user.getId(), from, size);
        model.addAttribute("follows", followed);

        return "follow-content";
    }

    /**
     * Obtain some follower user with AJAX
     *
     * @param model
     * @param from
     * @param size
     * @param username
     * @return
     */
    @GetMapping("/followers/{username}/{from}/{size}")
    public String getFollowers(Model model,
                               @PathVariable int from,
                               @PathVariable int size,
                               @PathVariable String username) {
        User user = this.profileService.findByUsername(username).get();
        Long countFollowers = this.profileService.countFollowers(user.getId());

        if (countFollowers <= from) {
            return "redirect:/";
        }

        List<User> followed = profileService.getFollowers(user.getId(), from, size);
        model.addAttribute("follows", followed);

        return "follow-content";
    }

    /**
     * Change the type of user to "BANNED"
     *
     * @param id
     * @return
     */
    @GetMapping("/ban/{id}")
    public String ban(@PathVariable Long id,
                      HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        User currentUser = this.informationManager.getCurrentUser(request);
        if (currentUser.getRole().toString().equals("ADMIN")) {
            User user = this.profileService.findById(id).get();
            user.setType("BANNED");
            user.setEnabled(false);
            mailService.sendBanMail(user.getEmail());
            this.profileService.updateUserBan(user);
        }
        return "redirect:/profile/" + id.toString();
    }

    /**
     * Change the type of user to "PUBLIC"
     *
     * @param id
     * @return
     */
    @GetMapping("/unban/{id}")
    public String unban(@PathVariable Long id,
                        HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        User currentUser = this.informationManager.getCurrentUser(request);
        if (currentUser.getRole().toString().equals("ADMIN")) {
            User user = this.profileService.findById(id).get();
            user.setType("PUBLIC");
            user.setEnabled(true);
            mailService.sendUnBanMail(user.getEmail());
            this.profileService.updateUserBan(user);
        }
        return "redirect:/dashboard";
    }

    /**
     * Manage when the current user follow or unfollow another user
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/toggleFollow/{id}")
    public String toggleFollow(@PathVariable Long id,
                               HttpServletRequest request) {
        User profileUser = this.profileService.findById(id).get();
        User currentUser = this.informationManager.getCurrentUser(request);
        boolean hasFollowed = this.profileService.toggleFollow(profileUser, currentUser);

        if (hasFollowed) {
            this.notificationService.createNotification(null, profileUser, currentUser, "FOLLOW");
        } else {
            this.notificationService.deleteNotification(null,
                    currentUser.getId(), "FOLLOW", profileUser.getId());
        }

        return "redirect:/profile/" + id;
    }

}
