package com.TwitterClone.ProjectBackend.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This is Hashtag Entity. A Hashtag is a String that starts with the symbol '#' and has any whitespace.
 * A Hashtag is a phrase that represents a topic which a group of people are interested into.
 * When a User publish a Tweet with a Hashtag this Tweet would appear in the explore page related to that Hashtag.
 * Publishing Tweet with Hashtag will help your account to be more findable.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hashtag {
    public interface Basic {
    }

    @Id
    @JsonView(Basic.class)
    private String hashtag;
    @ManyToMany
    @JsonView(Basic.class)
    private Set<Tweet> tweets = new HashSet<>();

    /**
     * Add a tweet to this hashtag
     *
     * @param tweet
     */
    public void addTweet(Tweet tweet) {
        this.tweets.add(tweet);
    }
}
