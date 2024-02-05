package com.TwitterClone.ProjectBackend.controller;

import com.TwitterClone.ProjectBackend.model.*;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.InformationManager;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.ModelManager;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.TweetInformation;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.UserInformation;
import com.TwitterClone.ProjectBackend.service.HashtagService;
import com.TwitterClone.ProjectBackend.service.NotificationService;
import com.TwitterClone.ProjectBackend.service.TweetService;
import com.TwitterClone.ProjectBackend.userManagement.UserRoles;
import com.TwitterClone.ProjectBackend.service.ProfileService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This class is on charge of controlling the navigation through the website.
 */
@Controller
public class NavigationController {

    @Autowired
    private HashtagService hashtagService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private TweetService tweetService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private InformationManager informationManager;
    @Autowired
    private ModelManager modelManager;

    /**
     * Change from the current page to login
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request){
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        return "login";
    }

    /**
     * Logout the current user
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logout(Model model, HttpServletRequest request){
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
        return "redirect:/";
    }

    /**
     * Change from the current page to the home page
     * @param model
     * @return
     */
    @GetMapping("/home")
    public String toHome(Model model, HttpServletRequest request) {
        this.informationManager.addNameToThePage(model,"Home");
        this.informationManager.addProfileInfoToLeftBar(model, request);
        this.modelManager.addCurrentTrends(model);

        User currentUser = this.informationManager.getCurrentUser(request);
        List<Tweet> tweetList = this.tweetService.findSomeRecentForUser(currentUser.getId(), 0, 10);

        List<TweetInformation> tweets = this.informationManager.calculateDataOfTweet(tweetList, currentUser);
        model.addAttribute("tweets", tweets);

        if (currentUser!=null){
            model.addAttribute("isLogged", true);
        }

        return "home";
    }


    /**
     * Change from the current page to the explore page
     * @param model
     * @return
     */
    @GetMapping("/explore")
    public String toExplore(Model model, HttpServletRequest request) {

        this.informationManager.addNameToThePage(model, "Explore");
        model.addAttribute("isExplorePage", true);

        this.modelManager.addCurrentTrends(model);
        this.informationManager.addProfileInfoToLeftBar(model, request);

        List<Trend> trends = this.hashtagService.getCurrentTrends(0,10);
        model.addAttribute("trends", trends);

        this.modelManager.addRecommended(model,request);

        return "explore";
    }

    /**
     * Change from the current page to the notifications page
     * @param model
     * @return
     */
    @GetMapping("/notifications")
    public String toNotifications(Model model, HttpServletRequest request) {

        this.informationManager.addNameToThePage(model, "Notifications");

        this.modelManager.addCurrentTrends(model);
        this.informationManager.addProfileInfoToLeftBar(model, request);
        User currentUser = this.informationManager.getCurrentUser(request);

        List<Notification> notifications = this.notificationService.getSomeNotificationsOfUser(currentUser.getId(), 0, 10);
        model.addAttribute("notifications", notifications);

        return "notifications";
    }

    /**
     * Change from the current page to the bookmarks page
     * @param model
     * @return
     */
    @GetMapping("/bookmarks")
    public String toBookmark(Model model, HttpServletRequest request) {

        this.informationManager.addNameToThePage(model, "Bookmarks");

        this.modelManager.addCurrentTrends(model);
        this.informationManager.addProfileInfoToLeftBar(model, request);

        User currentUser = this.informationManager.getCurrentUser(request);
        UserRoles typeUser = currentUser.getRole();
        model.addAttribute("isAdmin", typeUser.equals(UserRoles.ADMIN));

        List<Tweet> bookmarkTweetList = this.profileService.getBookmarks(currentUser.getId(), 0 , 10);
        List<TweetInformation> bookmarks= this.informationManager.calculateDataOfTweet(bookmarkTweetList, currentUser);
        model.addAttribute("tweets", bookmarks);

        if (currentUser!=null){
            model.addAttribute("isLogged", true);
        }

        return "bookmarks";
    }

    /**
     * Change from the current page to the profile page of a user
     * @return
     */
    @GetMapping("/profile/{id}")
    public String toProfile(Model model, HttpServletRequest request, @PathVariable Long id) {
        User profileUser = this.profileService.findById(id).get();
        User currentUser = this.informationManager.getCurrentUser(request);
        model.addAttribute("isYourProfile", (currentUser!=null)&&id.equals(currentUser.getId()));
        model.addAttribute("isFollowed", this.profileService.isFollowedBy(profileUser, currentUser));
        model.addAttribute("isBanned", profileUser.isEnabled());

        String nickname = profileUser.getNickname();
        this.informationManager.addNameToThePage(model, nickname);
        this.informationManager.addProfileInfoToLeftBar(model, request);
        this.modelManager.addCurrentTrends(model);

        UserInformation userToShow = this.informationManager.prepareUserInformation(profileUser, currentUser);
        model.addAttribute("userToShow", userToShow);

        if (currentUser!=null){
            model.addAttribute("isLogged", true);
        }

        List<Tweet> tweets = this.tweetService.findSomeTweetOfUser(id, 0, 10);
        List<TweetInformation> tweetInformationList = this.informationManager.calculateDataOfTweet(tweets, currentUser);
        model.addAttribute("tweets", tweetInformationList);

        //Hide Go To Dashboard button
        model.addAttribute("isAdmin",currentUser!=null && currentUser.getRole() == UserRoles.ADMIN);

        return "profile";
    }

    /**
     * Change from the current page to the user related people page
     * @return
     */
    @GetMapping("/follow/{id}")
    public String toRelatedPeople(@PathVariable("id") Long profileUserId,
                                  Model model,
                                  HttpServletRequest request) {
        User profileUser = this.profileService.findById(profileUserId).get();

        String nickname = profileUser.getNickname();
        String namePage = "People related to " + nickname;
        this.informationManager.addNameToThePage(model, namePage);

        this.informationManager.addProfileInfoToLeftBar(model, request);
        this.modelManager.addCurrentTrends(model);

        List<User> followed = profileService.getFollowed(profileUserId, 0, 10);
        model.addAttribute("follows", followed);

        model.addAttribute("user", profileUser);

        return "follow";
    }

    /**
     * Change from the current page to the edit profile page
     * @return
     */
    @GetMapping("/edit-profile")
    public String toEditProfile(Model model, HttpServletRequest request) {
        User currentUser = this.informationManager.getCurrentUser(request);
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());

        this.informationManager.addNameToThePage(model, "Edit profile");
        model.addAttribute("user", currentUser);

        return "edit-profile";
    }

    /**
     * Change from the current page to the write tweet page
     * @return
     */
    @GetMapping("/write-tweet")
    public String toWriteTweet(Model model) {
        model.addAttribute("type", "new");
        return "write-tweet";
    }

    /**
     * Change the current page to the reply a tweet
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/write-tweet/comment/{id}")
    public String toReplyTweet(Model model,
                               @PathVariable("id") Long id,
                               HttpServletRequest request)  {
        Optional<Tweet> tweet = this.tweetService.findById(id);
        Tweet tweetToReply = tweet.get();
        List<Tweet> t= (new ArrayList<>());
        t.add(tweetToReply);
        TweetInformation tweetInformation = this.informationManager.calculateDataOfTweet(t, this.informationManager.getCurrentUser(request)).get(0);
        model.addAttribute("tweet", tweetInformation);
        model.addAttribute("type", "reply");

        return "write-tweet";
    }

    /**
     * Change from the current page to the error page
     * @return
     */
    @GetMapping("/error")
    public String toError() {
        return "error";
    }

    /**
     * Change from the current page to the dashboard page
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/dashboard")
    public String toDashboard(Model model, HttpServletRequest request){
        this.informationManager.addNameToThePage(model,"Dashboard");
        this.informationManager.addProfileInfoToLeftBar(model,request);
        this.modelManager.addCurrentTrends(model);
        List<User> users = this.profileService.getVerified();
        model.addAttribute("verified", users);
        users = this.profileService.getBanned();
        model.addAttribute("banned", users);
        users = this.profileService.getToVerified();
        model.addAttribute("toVerify",users);
        this.modelManager.addStatistics(model);
        return "admin-dashboard";
    }

    /**
     * Change the type of user to "VERIFIED"
     * @param id
     * @return
     */
    @GetMapping("/verify/{id}")
    public String verify(@PathVariable Long id,
                         HttpServletRequest request){
        User currentUser = this.informationManager.getCurrentUser(request);

        if(currentUser.getRole().toString().equals("ADMIN")) {
            User user = this.profileService.findById(id).get();
            user.setType("VERIFIED");
            this.profileService.updateUserBan(user);
        }

        return "redirect:/dashboard";
    }

    /**
     * Change the type of user to "PUBLIC"
     * @param id
     * @return
     */
    @GetMapping("/unverify/{id}")
    public String unverify(@PathVariable Long id,
                           HttpServletRequest request){
        User currentUser = this.informationManager.getCurrentUser(request);

        if(currentUser.getRole().toString().equals("ADMIN")) {
            User user = this.profileService.findById(id).get();
            user.setType("PUBLIC");
            this.profileService.updateUserBan(user);
        }

        return "redirect:/dashboard";
    }

    /**
     * Shows a tweet with yours comments
     * @param id
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/tweet/{id}")
    public String toShowTweet(@PathVariable Long id, Model model, HttpServletRequest request){
        this.modelManager.addCurrentTrends(model);
        this.informationManager.addNameToThePage(model,"Tweet");
        this.informationManager.addProfileInfoToLeftBar(model,request);

        Tweet tweet = this.tweetService.findById(id).get();
        List<Tweet> tweets = new LinkedList<>();
        tweets.add(tweet);
        User currentUser = this.informationManager.getCurrentUser(request);
        List<TweetInformation> tweetInformation = this.informationManager.calculateDataOfTweet(tweets,currentUser);
        model.addAttribute("tweet", tweetInformation);

        List<Tweet> replies = this.tweetService.getComments(id,0,10);
        tweetInformation = this.informationManager.calculateDataOfTweet(replies,currentUser);
        model.addAttribute("tweets",tweetInformation);

        return "show-tweet";
    }
}
