package com.TwitterClone.ProjectBackend.model.mustacheObjects;

import com.TwitterClone.ProjectBackend.model.Tweet;
import com.TwitterClone.ProjectBackend.service.HashtagService;
import com.TwitterClone.ProjectBackend.service.NotificationService;
import com.TwitterClone.ProjectBackend.service.ProfileService;
import com.TwitterClone.ProjectBackend.service.TweetService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import com.TwitterClone.ProjectBackend.userManagement.UserRoles;
import lombok.NoArgsConstructor;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manage all the recurrent information needed by the controllers
 */
@Component
@NoArgsConstructor
public class InformationManager {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private HashtagService hashtagService;
    @Autowired
    private TweetService tweetService;
    @Autowired
    private NotificationService notificationService;

    /**
     * Add the current user to the left-bar
     *
     * @param model
     * @param request
     */
    public void addProfileInfoToLeftBar(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            model.addAttribute("isLogged", false);
            return;
        }

        Optional<User> currentSession = this.profileService.findByUsername(principal.getName());
        User currentUser = currentSession.get();

        if (currentUser == null) {
            return;
        }

        model.addAttribute("isLogged", true);
        model.addAttribute("id", currentUser.getId());
        model.addAttribute("user-image", "/users/" + currentUser.getId() + "/user-image");
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("nickname", currentUser.getNickname());
        model.addAttribute("type", currentUser.getType());

        if (currentUser.getType().equals("PRIVATE")) {
            model.addAttribute("private-acount", currentUser.getType());
        }
    }

    /**
     * Add name to the current page
     *
     * @param model
     * @param namePage
     */
    public void addNameToThePage(Model model, String namePage) {
        model.addAttribute("namePage", namePage);
    }

    /**
     * Prepare the list with the Tweets to show at mustache
     *
     * @param tweets
     */
    public List<TweetInformation> calculateDataOfTweet(List<Tweet> tweets, User currentUser) {
        List<TweetInformation> tweetsInfo = new ArrayList<>();

        for (Tweet tweet : tweets) {
            TweetInformation currentTweetInformation = new TweetInformation();
            currentTweetInformation.setTweet(tweet);
            currentTweetInformation.setNumLikes(this.tweetService.getLikesOfTweet(tweet.getId()));
            currentTweetInformation.setNumComments(this.tweetService.getCommentsOfTweet(tweet.getId()));
            currentTweetInformation.setNumRetweets(this.tweetService.getRetweetsOfTweet(tweet.getId()));

            this.checkUserStateAboutTweets(tweet, currentUser, currentTweetInformation);
            this.createUrlToImages(currentTweetInformation);

            tweetsInfo.add(currentTweetInformation);
        }

        return tweetsInfo;
    }

    private void createUrlToImages(TweetInformation tweetInformation) {
        Long tweetUserId = tweetInformation.getTweet().getUser().getId();
        Long tweetId = tweetInformation.getTweet().getId();
        tweetInformation.setUrlToProfilePic("/users/" + tweetUserId + "/user-image");
        if (tweetInformation.getTweet().getMedia1()!=null){
            tweetInformation.setUrlToMedia1("/tweets/" +  tweetId + "/image1");
        }
        if (tweetInformation.getTweet().getMedia2()!=null){
            tweetInformation.setUrlToMedia2("/tweets/" +  tweetId + "/image2");
        }
        if (tweetInformation.getTweet().getMedia3()!=null){
            tweetInformation.setUrlToMedia3("/tweets/" +  tweetId + "/image3");
        }
        if (tweetInformation.getTweet().getMedia4()!=null){
            tweetInformation.setUrlToMedia4("/tweets/" +  tweetId + "/image4");
        }
    }

    /**
     * Check the user status about the tweets to be showed
     *
     * @param tweet
     * @param currentUser
     * @param currentTweetInformation
     */
    private void checkUserStateAboutTweets(Tweet tweet,
                                           User currentUser, TweetInformation currentTweetInformation) {
        currentTweetInformation.setAuthorised(this.isAuthorised(currentUser, tweet));

        if (this.tweetService.isRetweeted(currentUser, tweet)) {
            currentTweetInformation.setColorRetweet("green-0");
            currentTweetInformation.setRetweeted(true);
        } else {
            currentTweetInformation.setColorRetweet("gray-4");
        }

        if (this.tweetService.isLiked(currentUser, tweet)) {
            currentTweetInformation.setColorLike("red-0");
            currentTweetInformation.setLiked(true);
        } else {
            currentTweetInformation.setColorLike("gray-4");
        }

        if (this.tweetService.isBookmarked(currentUser, tweet)) {
            currentTweetInformation.setColorBookmark("primary");
            currentTweetInformation.setBookmarked(true);
        } else {
            currentTweetInformation.setColorBookmark("gray-4");
        }
    }

    /**
     * Check if the user is authorised
     *
     * @param currentUser
     * @param tweet
     * @return
     */
    private boolean isAuthorised(User currentUser, Tweet tweet) {
        if (currentUser == null || currentUser.getRole().equals(UserRoles.ANONYMOUS)) {
            return false;
        }

        boolean condition1 = currentUser.getId().equals(tweet.getUser().getId());
        boolean condition2 = currentUser.getRole().equals(UserRoles.ADMIN);
        return condition2 || condition1;
    }

    /**
     * Obtain the current User in the session
     *
     * @param request
     * @return
     */
    public User getCurrentUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return null;
        }

        Optional<User> currentSession = this.profileService.findByUsername(principal.getName());
        return currentSession.get();
    }

    /**
     * Process all the image files from the input
     *
     * @param tweet_files
     * @return
     * @throws IOException
     */
    public Blob[] manageImages(MultipartFile[] tweet_files) throws IOException {
        Blob[] files = new Blob[4];

        if (!tweet_files[0].isEmpty()) {

            for (int index = 0; (index < tweet_files.length) && (index < 4); index++) {
                files[index] = BlobProxy
                        .generateProxy(tweet_files[index]
                                .getInputStream(), tweet_files[index]
                                .getSize());
            }
        }

        return files;
    }

    /**
     * Process the text of a tweet to analyze if exist a hashtag or a mention
     *
     * @param text
     * @param tweet
     * @param currentUser
     */
    public void processTextTweet(String text, Tweet tweet, User currentUser) {
        String[] splitText = text.split(" ");

        for (String segment : splitText) {

            if (segment.startsWith("#")) {
                this.processHashtag(segment, tweet);
            }

            if (segment.startsWith("@")) {
                this.processMention(segment, tweet, currentUser);
            }
        }
    }

    /**
     * Process a mention when it appears in a tweet
     *
     * @param segment
     * @param tweet
     * @param currentUser
     */
    public void processMention(String segment, Tweet tweet, User currentUser) {
        String[] splitMention = segment.split("@");

        for (String mention : splitMention) {
            User userToMention = this.profileService.findByUsername(mention).orElse(null);

            if (userToMention != null && !currentUser.getId().equals(userToMention.getId())) {
                this.notificationService
                        .createNotification(
                                tweet.getId(),
                                userToMention,
                                currentUser,
                                "MENTION");
            }
        }
    }

    /**
     * Process a hashtag when it appears in a tweet
     *
     * @param segment
     * @param firstTweet
     */
    public void processHashtag(String segment, Tweet firstTweet) {
        String[] splitHashtags = segment.split("#");

        for (String hashtag : splitHashtags) {

            if (!hashtag.equals("")) {
                hashtagService.add(hashtag, firstTweet);
            }
        }
    }

    public UserInformation prepareUserInformation(User profileUser, User currentUser) {
        UserInformation currentUserInformation = new UserInformation();
        currentUserInformation.setUser(profileUser);

        int followersNumber = profileUser.getFollowersNumber();
        currentUserInformation.setFollowersNumber(followersNumber);

        int followedNumber = profileUser.getFollowedNumber();
        currentUserInformation.setFollowedNumber(followedNumber);

        List<Tweet> tweetList = this.tweetService.findSomeTweetOfUser(profileUser.getId(),0, 10);
        List<TweetInformation> tweets = this.calculateDataOfTweet(tweetList, currentUser);
        currentUserInformation.setTweets(tweets);

        Long id = profileUser.getId();
        currentUserInformation.setUrlToProfilePic("/users/" + id + "/user-image");
        currentUserInformation.setUrlToBannerPic("/users/" + id + "/banner-image");

        return currentUserInformation;
    }

    public List<UserInformation> prepareListUser(List<User> users) {
        List<UserInformation> listUsers = new ArrayList<>();

        for (User user: users) {
            UserInformation currentUser = this.prepareUserInformation(user, null);
            listUsers.add(currentUser);
        }

        return listUsers;
    }
}
