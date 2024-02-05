package com.TwitterClone.ProjectBackend.controller.restController;

import com.TwitterClone.ProjectBackend.model.mustacheObjects.InformationManager;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.JSONString;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.TweetInformation;
import com.TwitterClone.ProjectBackend.model.Tweet;
import com.TwitterClone.ProjectBackend.service.NotificationService;
import com.TwitterClone.ProjectBackend.service.ProfileService;
import com.TwitterClone.ProjectBackend.service.TweetService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TweetRestController {
    @Autowired
    private TweetService tweetService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private InformationManager informationManager;
    @Autowired
    private NotificationService notificationService;

    interface Basic extends Tweet.Basic, TweetInformation.Basic, User.Basic {
    }

    @Operation(summary = "Get some of the tweets owned by the followed users of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tweets obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "204", description = "No tweets found for current user in this range", content = @Content),
            @ApiResponse(responseCode = "403", description = "Current User can not do that", content = @Content)
    })
    @GetMapping("/tweets")
    @JsonView(Basic.class)
    public ResponseEntity<List<TweetInformation>> getTweetsForAUser(@PathParam("from") int from,
                                                                    @PathParam("size") int size,
                                                                    HttpServletRequest request) {
        User user = this.informationManager.getCurrentUser(request);

        if(user == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Tweet> tweets = this.tweetService.findSomeRecentForUser(user.getId(), from, size);
        List<TweetInformation> tweetsInformation = this.informationManager.calculateDataOfTweet(tweets, user);

        if (tweetsInformation.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tweetsInformation, HttpStatus.OK);
    }

    @Operation(summary = "Get some of the bookmarks of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookmarks obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "204", description = "No bookmarks found for current user in this range", content = @Content),
            @ApiResponse(responseCode = "403", description = "Current User can not do that", content = @Content)
    })
    @GetMapping("/bookmarks")
    @JsonView(Basic.class)
    public ResponseEntity<List<TweetInformation>> getTweetsForBookmarks(@PathParam("from") int from,
                                                                        @PathParam("size") int size,
                                                                        HttpServletRequest request) {
        User user = this.informationManager.getCurrentUser(request);

        if (user == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Tweet> tweets = this.profileService.getBookmarks(user.getId(), from, size);
        List<TweetInformation> tweetsInformation = this.informationManager.calculateDataOfTweet(tweets, user);

        if (tweetsInformation.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tweetsInformation, HttpStatus.OK);
    }

    @Operation(summary = "Get some of the tweets written by the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tweets obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "204", description = "Tweets not found", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("users/{id}/tweets")
    @JsonView(Basic.class)
    public ResponseEntity<List<TweetInformation>> getProfileTweets(@PathVariable Long id,
                                                                   @PathParam("from") int from,
                                                                   @PathParam("size") int size,
                                                                   HttpServletRequest request) {
        if (this.profileService.findById(id).orElse(null) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = this.informationManager.getCurrentUser(request);
        List<Tweet> tweets = this.tweetService.findSomeTweetOfUser(id, from, size);
        List<TweetInformation> tweetsInformation = this.informationManager.calculateDataOfTweet(tweets, user);

        if (tweetsInformation.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tweetsInformation, HttpStatus.OK);
    }


    @Operation(summary = "Post a new tweet by the current user replying a tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment Created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "403", description = "Current User can not do that", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tweet Not Found", content = @Content)
    })
    @PostMapping("/tweets/{idTweetReplied}/comments")
    @JsonView(Basic.class)
    public ResponseEntity<TweetInformation> postComment(@RequestBody JSONString tweet_info,
                                           @PathVariable("idTweetReplied") Long idTweetReplied,
                                           HttpServletRequest request) {
        Tweet tweetReplied = this.tweetService.findById(idTweetReplied).orElse(null);
        User currentUser = this.informationManager.getCurrentUser(request);

        if(currentUser == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (tweetReplied == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user_reply = tweetReplied.getUser();
        Long userId = currentUser.getId();
        Tweet newTweet = this.tweetService.createTweet(tweet_info.getText(), new Blob[]{}, userId);
        this.informationManager.processTextTweet(tweet_info.getText(), newTweet, currentUser);
        tweetService.addComment(idTweetReplied, newTweet);

        if (!userId.equals(user_reply.getId())) {
            this.notificationService
                    .createNotification(newTweet.getId(), user_reply, currentUser, "MENTION");
        }

        List<Tweet> tweets = new ArrayList<>();
        tweets.add(newTweet);
        List<TweetInformation> tweetProcessed= this.informationManager.calculateDataOfTweet(tweets,currentUser);
        return new ResponseEntity<>(tweetProcessed.get(0), HttpStatus.OK);
    }


    @Operation(summary = "Post a new tweet by the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tweet Created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "403", description = "Current User can not do that", content = @Content)
    })
    @PostMapping("/tweets")
    @JsonView(Basic.class)
    public ResponseEntity<TweetInformation> postTweet(@RequestBody JSONString tweet_info,
                                                      HttpServletRequest request) {
        Blob[] files = new Blob[]{};
        User currentUser = this.informationManager.getCurrentUser(request);

        if (currentUser == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Long userId = currentUser.getId();
        Tweet newTweet = this.tweetService.createTweet(tweet_info.getText(), files, userId);
        this.informationManager.processTextTweet(tweet_info.getText(), newTweet, currentUser);

        List<Tweet> tweets = new ArrayList<>();
        tweets.add(newTweet);
        List<TweetInformation> tweetProcessed = this.informationManager.calculateDataOfTweet(tweets,currentUser);
        return new ResponseEntity<>(tweetProcessed.get(0), HttpStatus.OK);
    }

    @Operation(summary = "Delete a tweet of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tweet Created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "403", description = "Current User can not do that", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tweet Not Found", content = @Content),
    })
    @DeleteMapping("/tweets/{id}")
    @JsonView(Basic.class)
    public ResponseEntity<TweetInformation> deleteTweet(@PathVariable("id") Long id,
                                             HttpServletRequest request) {
        Tweet tweet = this.tweetService.findById(id).orElse(null);

        if (tweet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet);
        User currentUser = this.informationManager.getCurrentUser(request);
        List<TweetInformation> tweetInformation = this.informationManager.calculateDataOfTweet(tweets, currentUser);

        if (!tweetInformation.get(0).isAuthorised()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        this.tweetService.deleteTweet(tweet);
        return new ResponseEntity<>(tweetInformation.get(0), HttpStatus.OK);
    }

    @Operation(summary = "Give or remove like of a tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like given", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "202", description = "Like removed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "403", description = "Current User can not do that", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tweet Not Found", content = @Content),
    })
    @PutMapping("/tweets/{id}/likes")
    @JsonView(Basic.class)
    public ResponseEntity<TweetInformation> toggleLike(@PathVariable("id") Long id,
                                                       HttpServletRequest request) {
        User currentUser = this.informationManager.getCurrentUser(request);

        if(currentUser == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Tweet tweet = this.tweetService.findById(id).orElse(null);

        if (tweet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        boolean hasLiked = tweetService.toggleLike(currentUser, tweet);

        List<Tweet> list = new ArrayList<>();
        list.add(tweet);
        List<TweetInformation> tweetInformation = this.informationManager.calculateDataOfTweet(list, currentUser);

        if (hasLiked) {
            this.notificationService.createNotification(id,tweet.getUser(), currentUser, "LIKE");
            return new ResponseEntity<>(tweetInformation.get(0), HttpStatus.OK);
        }
        this.notificationService.deleteNotification(id, currentUser.getId(), "LIKE", tweet.getUser().getId());
        return new ResponseEntity<>(tweetInformation.get(0), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Give or remove retweet of a tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retweet given", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "202", description = "Retweet removed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "403", description = "Current User can not do that", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tweet Not Found", content = @Content)
    })
    @PutMapping("/tweets/{id}/retweets")
    @JsonView(Basic.class)
    public ResponseEntity<TweetInformation> toggleRetweet(@PathVariable("id") Long id,
                                                          HttpServletRequest request) {
        User currentUser = this.informationManager.getCurrentUser(request);

        if(currentUser == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Tweet tweet = this.tweetService.findById(id).orElse(null);

        if (tweet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        boolean hasRetweeted = this.tweetService.toggleRetweet(currentUser, tweet);

        List<Tweet> list = new ArrayList();
        list.add(tweet);
        List<TweetInformation> tweetInformation = this.informationManager.calculateDataOfTweet(list, currentUser);

        if (hasRetweeted) {
            this.notificationService.createNotification(id,tweet.getUser(), currentUser, "RETWEET");
            return new ResponseEntity<>(tweetInformation.get(0), HttpStatus.OK);
        }
        this.notificationService.deleteNotification(id, currentUser.getId(), "RETWEET", tweet.getUser().getId());
        return new ResponseEntity<>(tweetInformation.get(0), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Add or remove a tweet from the bookmark list of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookmarked", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "202", description = "Bookmark Removed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "403", description = "Current User can not do that", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tweet Not Found", content = @Content)
    })
    @PutMapping("/tweets/{id}/bookmarks")
    @JsonView(Basic.class)
    public ResponseEntity<TweetInformation> toggleBookmark(@PathVariable("id") Long id,
                                                           HttpServletRequest request) {
        User currentUser = this.informationManager.getCurrentUser(request);

        if(currentUser == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Tweet tweet = this.tweetService.findById(id).orElse(null);

        if (tweet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        boolean hasBookmarked = this.tweetService.toggleBookmark(currentUser, tweet);

        List<Tweet> bookmarks = new ArrayList<>();
        bookmarks.add(tweet);
        List<TweetInformation> tweetInformation = this.informationManager.calculateDataOfTweet(bookmarks, currentUser);

        if (hasBookmarked) {
            return new ResponseEntity<>(tweetInformation.get(0), HttpStatus.OK);
        }

        return new ResponseEntity<>(tweetInformation.get(0), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get a tweet by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tweet got", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "404", description = "Tweet Not Found", content = @Content)
    })
    @GetMapping("/tweets/{id}")
    @JsonView(Basic.class)
    public ResponseEntity<TweetInformation> getTweet(@PathVariable("id") Long id, HttpServletRequest request) {
        Tweet tweet = this.tweetService.findById(id).orElse(null);
        User currentUser = this.informationManager.getCurrentUser(request);

        if (tweet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Tweet> tList = new LinkedList<>();
        tList.add(tweet);
        List<TweetInformation> tweetInformationList = this.informationManager.calculateDataOfTweet(tList, currentUser);
        return new ResponseEntity<>(tweetInformationList.get(0), HttpStatus.OK);
    }

    @Operation(summary = "Get some of the tweets that are comments of the specified tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tweets obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "204", description = "No tweets found for current user in this range", content = @Content),
            @ApiResponse(responseCode = "403", description = "Current User can not do that", content = @Content)
    })
    @GetMapping("/tweets/{id}/comments")
    @JsonView(Basic.class)
    public ResponseEntity<List<TweetInformation>> getRepliesOfATweet(@PathVariable("id") Long id,
                                                                     @PathParam("from") int from,
                                                                     @PathParam("size") int size,
                                                                     HttpServletRequest request) {
        User user = this.informationManager.getCurrentUser(request);

        List<Tweet> tweets = this.tweetService.getComments(id, from, size);
        List<TweetInformation> tweetsInformation = this.informationManager.calculateDataOfTweet(tweets, user);

        if (tweetsInformation.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tweetsInformation, HttpStatus.OK);
    }
}
