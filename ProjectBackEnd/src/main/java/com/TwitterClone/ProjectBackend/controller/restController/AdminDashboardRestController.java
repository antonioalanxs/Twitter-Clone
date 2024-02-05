package com.TwitterClone.ProjectBackend.controller.restController;


import com.TwitterClone.ProjectBackend.DTO.StatisticRequest;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.InformationManager;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.UserInformation;
import com.TwitterClone.ProjectBackend.service.MailService;
import com.TwitterClone.ProjectBackend.service.ProfileService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.persistence.Tuple;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdminDashboardRestController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private InformationManager informationManager;
    @Autowired
    private MailService mailService;

    interface Basic extends User.Basic, UserInformation.Basic{}

    @Operation(summary = "If the user is a admin, he can get the statistics of new accounts created in the last 5 days whit new accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics shown", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "403", description = "No permission", content = @Content)
    })
    @GetMapping("users/statistics")
    public ResponseEntity<List<StatisticRequest>> getNewAccountStatistics(HttpServletRequest request) {
        User currentUser = this.informationManager.getCurrentUser(request);
        List<Tuple> statics = this.profileService.getStatics();
        List<StatisticRequest> statistics = new ArrayList<>();

        for (Tuple aStatic : statics) {
            String name = aStatic.get("join_date").toString();
            int value = Integer.parseInt(aStatic.get("new_people").toString());

            statistics.add(new StatisticRequest(name, value));
        }

        if (currentUser.getRole().toString().equals("ADMIN")) {
            return ResponseEntity.ok(statistics);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "If the user is a admin, he can update user type user according to his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User type updated", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "Type not valid", content = @Content),
            @ApiResponse(responseCode = "403", description = "User can not do that", content = @Content),
            @ApiResponse(responseCode = "404", description = "User ID not found", content = @Content)
    })
    @PutMapping("users/{id}")
    @JsonView(Basic.class)
    public ResponseEntity<Object> toggleType(@PathVariable Long id,
                                           @RequestParam("type") String type,
                                           HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {
        User currentUser = this.informationManager.getCurrentUser(request);

        if (currentUser == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (profileService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = this.profileService.findById(id).get();

        if (!currentUser.getRole().toString().equals("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        switch (type) {
            case "VERIFY" -> user.setType("VERIFIED");
            case "UNVERIFY" -> user.setType("PUBLIC");
            case "BAN" -> {
                user.setType("BANNED");
                user.setEnabled(false);
                mailService.sendBanMail(user.getEmail());
            }
            case "UNBAN" -> {
                user.setType("PUBLIC");
                user.setEnabled(true);
                mailService.sendUnBanMail(user.getEmail());
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        this.profileService.updateUserBan(user);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get all the users that can be verified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users obtained", content = {
                    @Content(mediaType = "application/json")
            })})
    @GetMapping("users-to-verify")
    @JsonView(Basic.class)
    public ResponseEntity<List<UserInformation>> getUsersToVerify() {
        List<User> userToVerify = this.profileService.getToVerified();
        List<UserInformation> users = this.informationManager.prepareListUser(userToVerify);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Get all the verificated users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users obtained", content = {
                    @Content(mediaType = "application/json")
            })})
    @GetMapping("verificated-users")
    @JsonView(Basic.class)
    public ResponseEntity<List<UserInformation>> getVerificatedUsers() {
        List<User> userToVerify = this.profileService.getVerified();
        List<UserInformation> users = this.informationManager.prepareListUser(userToVerify);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Get all the banned users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users obtained", content = {
                    @Content(mediaType = "application/json")
            })})
    @GetMapping("banned-users")
    @JsonView(Basic.class)
    public ResponseEntity<List<UserInformation>> getBannedUsers() {
        List<User> userToVerify = this.profileService.getBanned();
        List<UserInformation> users = this.informationManager.prepareListUser(userToVerify);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
