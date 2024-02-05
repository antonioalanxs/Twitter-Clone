package com.TwitterClone.ProjectBackend.controller.restController;

import com.TwitterClone.ProjectBackend.model.Tweet;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.InformationManager;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.TweetInformation;
import com.TwitterClone.ProjectBackend.service.ProfileService;
import com.TwitterClone.ProjectBackend.service.TweetService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Manage all the petitions that needs images
 */
@RestController
@RequestMapping("/api/")
public class ImageRestController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private TweetService tweetService;
    @Autowired
    private InformationManager informationManager;

    /**
     * Load the profile pic of a user
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Operation(summary = "Get the profile pic associated to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile pic obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "404", description = "Profile pic not found", content = @Content)
    })
    @GetMapping("users/{id}/user-image")
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
    @Operation(summary = "Get the banner pic associated to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Banner pic obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "404", description = "Banner pic not found", content = @Content)
    })
    @GetMapping("users/{id}/banner-image")
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
    @Operation(summary = "Get the first media associated to a tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "First media obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "202", description = "First media not found", content = @Content)
    })
    @GetMapping("tweets/{id}/image1")
    public ResponseEntity<Object> downloadTweetImage1(@PathVariable long id) throws SQLException {
        Optional<Tweet> tweet = this.tweetService.findById(id);

        if (tweet.isPresent() && tweet.get().getMedia1() != null) {
            Resource file = new InputStreamResource(tweet.get().getMedia1().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(tweet.get().getMedia1().length()).body(file);
        }

        return ResponseEntity.accepted().build();
    }

    /**
     * Load the second image of a tweet
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Operation(summary = "Get the second media associated to a tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Second media obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "202", description = "Second media not found", content = @Content)
    })
    @GetMapping("tweets/{id}/image2")
    public ResponseEntity<Object> downloadTweetImage2(@PathVariable long id) throws SQLException {
        Optional<Tweet> tweet = this.tweetService.findById(id);

        if (tweet.isPresent() && tweet.get().getMedia2() != null) {
            Resource file = new InputStreamResource(tweet.get().getMedia2().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(tweet.get().getMedia2().length()).body(file);
        }

        return ResponseEntity.accepted().build();
    }

    /**
     * Load the thirst image of a tweet
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Operation(summary = "Get the third media associated to a tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Third media obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "202", description = "Third media not found", content = @Content)
    })
    @GetMapping("tweets/{id}/image3")
    public ResponseEntity<Object> downloadTweetImage3(@PathVariable long id) throws SQLException {
        Optional<Tweet> tweet = this.tweetService.findById(id);

        if (tweet.isPresent() && tweet.get().getMedia3() != null) {
            Resource file = new InputStreamResource(tweet.get().getMedia3().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(tweet.get().getMedia3().length()).body(file);
        }

        return ResponseEntity.accepted().build();
    }

    /**
     * Load the fourth image of a tweet
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Operation(summary = "Get the fourth media associated to a tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fourth media obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "202", description = "Fourth media not found", content = @Content)
    })
    @GetMapping("tweets/{id}/image4")
    public ResponseEntity<Object> downloadTweetImage4(@PathVariable long id) throws SQLException {
        Optional<Tweet> tweet = this.tweetService.findById(id);

        if (tweet.isPresent() && tweet.get().getMedia4() != null) {
            Resource file = new InputStreamResource(tweet.get().getMedia4().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(tweet.get().getMedia4().length()).body(file);
        }

        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Put some images to a tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images saved", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tweet not found", content = @Content)
    })
    @PutMapping("tweets/{id}/images")
    public ResponseEntity<TweetInformation> uploadImages(@PathVariable Long id,
                                                         @RequestParam("image1") MultipartFile image1,
                                                         @Param("image2") MultipartFile image2,
                                                         @Param("image3") MultipartFile image3,
                                                         @Param("image4") MultipartFile image4,
                                                         HttpServletRequest request){
        User currentUser = this.informationManager.getCurrentUser(request);

        if (currentUser == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Tweet> tweet = this.tweetService.findById(id);

        if (tweet.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User userOwnerOfTweet = tweet.get().getUser();

        if (!Objects.equals(currentUser.getId(), userOwnerOfTweet.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        this.tweetService.addImages(tweet.get(), image1, image2, image3, image4);
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
