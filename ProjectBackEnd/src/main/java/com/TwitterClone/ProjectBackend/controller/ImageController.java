package com.TwitterClone.ProjectBackend.controller;

import com.TwitterClone.ProjectBackend.model.Tweet;
import com.TwitterClone.ProjectBackend.service.ProfileService;
import com.TwitterClone.ProjectBackend.service.TweetService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Manage all the petitions that needs images
 */
@Controller
public class ImageController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private TweetService tweetService;

    /**
     * Load the profile pic of a user
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @GetMapping("/users/{id}/user-image")
    public ResponseEntity<Object> downloadProfilePic(@PathVariable long id) throws SQLException {
        Optional<User> user = profileService.findById(id);

        if (user.isPresent() && user.get().getProfilePicture() != null) {

            Resource file = new InputStreamResource(user.get().getProfilePicture().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(user.get().getProfilePicture().length()).body(file);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Load the banner of a user
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @GetMapping("/users/{id}/banner-image")
    public ResponseEntity<Object> downloadBannerPic(@PathVariable long id) throws SQLException {
        Optional<User> user = profileService.findById(id);

        if (user.isPresent() && user.get().getProfileBanner() != null) {

            Resource file = new InputStreamResource(user.get().getProfileBanner().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(user.get().getProfileBanner().length()).body(file);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Load the first image of a tweet
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @GetMapping("tweets/{id}/image1")
    public ResponseEntity<Object> downloadTweetImage1(@PathVariable long id) throws SQLException {
        Optional<Tweet> tweet = this.tweetService.findById(id);

        if (tweet.isPresent() && tweet.get().getMedia1() != null) {
            Resource file = new InputStreamResource(tweet.get().getMedia1().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(tweet.get().getMedia1().length()).body(file);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Load the second image of a tweet
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @GetMapping("tweets/{id}/image2")
    public ResponseEntity<Object> downloadTweetImage2(@PathVariable long id) throws SQLException {
        Optional<Tweet> tweet = this.tweetService.findById(id);

        if (tweet.isPresent() && tweet.get().getMedia2() != null) {
            Resource file = new InputStreamResource(tweet.get().getMedia2().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(tweet.get().getMedia2().length()).body(file);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Load the thirst image of a tweet
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @GetMapping("tweets/{id}/image3")
    public ResponseEntity<Object> downloadTweetImage3(@PathVariable long id) throws SQLException {
        Optional<Tweet> tweet = this.tweetService.findById(id);

        if (tweet.isPresent() && tweet.get().getMedia3() != null) {
            Resource file = new InputStreamResource(tweet.get().getMedia3().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(tweet.get().getMedia3().length()).body(file);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Load the fourth image of a tweet
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @GetMapping("tweets/{id}/image4")
    public ResponseEntity<Object> downloadTweetImage4(@PathVariable long id) throws SQLException {
        Optional<Tweet> tweet = this.tweetService.findById(id);

        if (tweet.isPresent() && tweet.get().getMedia4() != null) {
            Resource file = new InputStreamResource(tweet.get().getMedia4().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(tweet.get().getMedia4().length()).body(file);
        }

        return ResponseEntity.notFound().build();
    }
}
