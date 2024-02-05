package com.TwitterClone.ProjectBackend.model.mustacheObjects;

import com.TwitterClone.ProjectBackend.model.Tweet;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class to store all the information of a tweet to be show at mustache
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TweetInformation {
    public interface Basic {
    }

    @JsonView(Basic.class)
    private Long numComments;
    @JsonView(Basic.class)
    private Long numRetweets;
    @JsonView(Basic.class)
    private Long numLikes;
    @JsonView(Basic.class)
    private boolean isAuthorised;
    @JsonView(Basic.class)
    private String urlToProfilePic;
    @JsonView(Basic.class)
    private String urlToMedia1 = "";
    @JsonView(Basic.class)
    private String urlToMedia2 = "";
    @JsonView(Basic.class)
    private String urlToMedia3 = "";
    @JsonView(Basic.class)
    private String urlToMedia4 = "";

    @JsonView(Basic.class)
    private boolean liked = false;
    @JsonView(Basic.class)
    private boolean retweeted = false;
    @JsonView(Basic.class)
    private boolean bookmarked = false;

    @JsonView(Basic.class)
    private Tweet tweet;

    private String colorLike;
    private String colorRetweet;
    private String colorBookmark;

}
