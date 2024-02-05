package com.TwitterClone.ProjectBackend.controller.restController;

import com.TwitterClone.ProjectBackend.model.mustacheObjects.InformationManager;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.TweetInformation;
import com.TwitterClone.ProjectBackend.model.Notification;
import com.TwitterClone.ProjectBackend.model.Tweet;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.UserInformation;
import com.TwitterClone.ProjectBackend.service.NotificationService;
import com.TwitterClone.ProjectBackend.service.ProfileService;
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

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class NotificationRestController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private InformationManager informationManager;
    @Autowired
    private ProfileService profileService;

    interface Basic extends Tweet.Basic, TweetInformation.Basic, User.Basic, Notification.Basic, UserInformation.Basic {
    }


    /**
     * Get some notifications associated to the current user
     *
     * @param from
     * @param size
     * @param request
     * @return
     */
    @Operation(summary = "Get some of the notification of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))
            }),
            @ApiResponse(responseCode = "204", description = "No more notifications not found", content = @Content)
    })
    @GetMapping("/notifications")
    @JsonView(Basic.class)
    public ResponseEntity<List<Notification>> getSomeNotifications(
            @PathParam("from") int from,
            @PathParam("size") int size,
            HttpServletRequest request) {
        User currentUser = this.informationManager.getCurrentUser(request);
        Long idCurrentUser = currentUser.getId();
        List<Notification> newNotifications = this.notificationService.getSomeNotificationsOfUser(idCurrentUser, from, size);

        if (newNotifications.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(newNotifications, HttpStatus.OK);
    }

    /**
     * Get some mentions associated to the current user
     *
     * @param from
     * @param size
     * @param request
     * @return
     */
    @Operation(summary = "Get some of the mentions of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentions obtained", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))
            }),
            @ApiResponse(responseCode = "204", description = "No more mentions not found", content = @Content)
    })
    @GetMapping("/mentions")
    @JsonView(Basic.class)
    public ResponseEntity<List<Notification>> getSomeMentions(
            @PathParam("from") int from,
            @PathParam("size") int size,
            HttpServletRequest request) {
        User currentUser = this.informationManager.getCurrentUser(request);
        Long idCurrentUser = currentUser.getId();
        List<Notification> newMentions = this.notificationService.getSomeMentionsOfUser(idCurrentUser, from, size);

        if (newMentions.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(newMentions, HttpStatus.OK);
    }

    /**
     * Creates a new notification when someone interacts with a tweet or user
     *
     * @param idTweet
     * @param idOwner
     * @param notificationType
     * @param request
     * @return
     */
    @Operation(summary = "Post a new notification to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification Created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))
            }),
            @ApiResponse(responseCode = "202", description = "The user notifies himself or duplicated", content = @Content),
            @ApiResponse(responseCode = "208", description = "Notification duplicated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Cannot create the notification", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not login", content = @Content),
            @ApiResponse(responseCode = "404", description = "User to notify not found", content = @Content)

    })
    @PostMapping("/notifications")
    @JsonView(Basic.class)
    public ResponseEntity<Notification> createNotification(@PathParam("idTweet") Long idTweet,
                                                         @PathParam("idOwner") Long idOwner,
                                                         @PathParam("notificationType") String notificationType,
                                                         HttpServletRequest request) {
        User currentUser = this.informationManager.getCurrentUser(request);

        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<User> owner = this.profileService.findById(idOwner);

        if (owner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Long currentUserId = currentUser.getId();

        if (currentUserId.equals(idOwner)) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        Notification duplicatedNotification =
                this.notificationService.findNotification(currentUserId, idOwner, idTweet, notificationType);

        if (duplicatedNotification != null) {
            return new ResponseEntity<>(duplicatedNotification, HttpStatus.ALREADY_REPORTED);
        }

        Optional<Notification> notificationOptional =
                this.notificationService.createNotification(idTweet, owner.get(), currentUser, notificationType);

        return notificationOptional
                .map(notification -> new ResponseEntity<>(notification, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Deletes a notification when someone interacts with a tweet or user
     *
     * @param idTweet
     * @param notificationType
     * @param request
     * @return
     */
    @Operation(summary = "Deletes a notification to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification Deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))
            }),
            @ApiResponse(responseCode = "404", description = "Notification to delete not found", content = @Content)
    })
    @DeleteMapping("/notifications")
    @JsonView(Basic.class)
    public ResponseEntity<Notification> deleteNotification(@PathParam("idTweet") Long idTweet,
                                                           @PathParam("notificationType") String notificationType,
                                                           @PathParam("idUserToNotify") Long idUserToNotify,
                                                           HttpServletRequest request) {
        User currentUser = this.informationManager.getCurrentUser(request);
        Long currentUserId = currentUser.getId();

        Optional<Notification> notificationOptional =
                this.notificationService.deleteNotification(idTweet, currentUserId, notificationType, idUserToNotify);

        return notificationOptional
                .map(notification -> new ResponseEntity<>(notification, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
