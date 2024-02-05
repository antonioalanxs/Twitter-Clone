package com.TwitterClone.ProjectBackend.controller.restController;

import com.TwitterClone.ProjectBackend.model.Hashtag;
import com.TwitterClone.ProjectBackend.model.Trend;
import com.TwitterClone.ProjectBackend.model.Tweet;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.InformationManager;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.TweetInformation;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.UserInformation;
import com.TwitterClone.ProjectBackend.service.HashtagService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import com.TwitterClone.ProjectBackend.userManagement.UserService;
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

import java.util.List;

@RestController
@RequestMapping("/api")
public class SearchRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private InformationManager informationManager;

    interface Basic extends User.Basic,Tweet.Basic,UserInformation.Basic,Hashtag.Basic{}

    ;

    @Operation(summary = "Find profiles which contains the keyword in their username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserInformation.class))
            }),
            @ApiResponse(responseCode = "404", description = "No profiles found that match the keyword", content = @Content)
    })
    @GetMapping("/users/{keyword}/found-users")
    @JsonView(Basic.class)
    public ResponseEntity<List<UserInformation>> searchProfiles(@PathVariable String keyword) {
        List<User> usersList = userService.findByUsernameContainingIgnoreCase(keyword);

        if (usersList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<UserInformation> userInformationList = this.informationManager.prepareListUser(usersList);
        return new ResponseEntity<>(userInformationList, HttpStatus.OK);
    }

    @Operation(summary = "Find Hashtags which contains the keyword in their username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Trend.class))
            }),
            @ApiResponse(responseCode = "404", description = "No hashtags found that match the keyword", content = @Content)
    })
    @GetMapping("/hashtags/{keyword}/found-hashtags")
    @JsonView(Basic.class)
    public ResponseEntity<Object> searchHashtags(@PathVariable String keyword) {
        List<Hashtag> hashtagsList = hashtagService.findByHashtagIsContainingIgnoreCase(keyword);

        if (hashtagsList.size() == 0) {
            return new ResponseEntity<>(hashtagsList, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(hashtagsList);
    }

}
