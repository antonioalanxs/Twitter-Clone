package com.TwitterClone.ProjectBackend.model.mustacheObjects;

import com.TwitterClone.ProjectBackend.userManagement.User;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserInformation {
    public interface Basic {
    }

    @JsonView(Basic.class)
    private User user;
    @JsonView(Basic.class)
    private String urlToProfilePic;
    @JsonView(Basic.class)
    private String urlToBannerPic;
    @JsonView(Basic.class)
    private int followersNumber;
    @JsonView(Basic.class)
    private int followedNumber;
    @JsonView(Basic.class)
    private List<TweetInformation> tweets;


}
