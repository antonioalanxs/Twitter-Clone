package com.TwitterClone.ProjectBackend.controller.restController;

import com.TwitterClone.ProjectBackend.model.mustacheObjects.InformationManager;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.TweetInformation;
import com.TwitterClone.ProjectBackend.model.Trend;
import com.TwitterClone.ProjectBackend.model.Tweet;
import com.TwitterClone.ProjectBackend.service.HashtagService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * This REST controller manages all the petitions relation with hashtags
 */
@RestController
@RequestMapping("/api")
public class HashtagRestController {
    @Autowired
    private HashtagService hashtagService;
    @Autowired
    private InformationManager informationManager;

    /**
     * Get some trending hashtags
     *
     * @param from
     * @param size
     */
    @Operation(summary = "Get some of the current trending hashtags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trends obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Trend.class))
            }),
            @ApiResponse(responseCode = "204", description = "No more trending hashtags found", content = @Content)
    })
    @GetMapping("/trends")
    public ResponseEntity<List<Trend>> getSomeTrends(@PathParam("from") int from,
                                                     @PathParam("size") int size) {
        List<Trend> newTrends = this.hashtagService.getCurrentTrends(from, size);

        if (newTrends.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(newTrends, HttpStatus.OK);
    }

    /**
     * Get some tweets associated to a hashtag
     *
     * @param from
     * @param size
     * @param id_hashtag
     * @param request
     * @return
     */
    @Operation(summary = "Get some tweets associated to a hashtag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associated tweets to hashtag obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TweetInformation.class))
            }),
            @ApiResponse(responseCode = "204",
                    description = "No more associated tweets to hashtag found", content = @Content)
    })
    @GetMapping("/hashtag/{id_hashtag}/tweets")
    @JsonView(TweetRestController.Basic.class)
    public ResponseEntity<List<TweetInformation>> getSomeTweetsAssociatedToAHashtag(
            @PathParam("from") int from,
            @PathParam("size") int size,
            @PathVariable String id_hashtag,
            HttpServletRequest request) {

        User currentUser = this.informationManager.getCurrentUser(request);

        List<Tweet> tweetsAssociated =
                this.hashtagService.getTweetsAssociatedTo(id_hashtag, from, size);
        List<TweetInformation> tweets =
                this.informationManager.calculateDataOfTweet(tweetsAssociated, currentUser);

        if (tweets.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tweets, HttpStatus.OK);
    }
}
