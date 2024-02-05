package com.TwitterClone.ProjectBackend.model;

import com.TwitterClone.ProjectBackend.userManagement.User;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * This is Notification Entity. When a user interacts with either your profile or your tweets, a notification will be
 * generated in order to report you about your influence in other users.
 */
@Entity
@Getter
public class Notification {
    public interface Basic {
    }

    ;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Basic.class)
    private Long id;
    @ManyToOne
    @JsonView(Basic.class)
    private Tweet tweetTrigger;
    @ManyToOne
    @JsonView(Basic.class)
    private User userToNotify;
    @ManyToOne
    @JsonView(Basic.class)
    private User userWhoNotifies;
    @JsonView(Basic.class)
    private LocalDateTime date;
    @JsonView(Basic.class)
    private String type;

    /**
     * Deafult constructor
     */
    public Notification() {
        id = new Random().nextLong();
    }

    /**
     * Constructor
     *
     * @param tweet
     * @param userToNotify
     * @param userWhoNotifies
     */
    public Notification(Tweet tweet, User userToNotify, User userWhoNotifies, String type) {
        this.userToNotify = userToNotify;
        this.userWhoNotifies = userWhoNotifies;
        this.tweetTrigger = tweet;
        this.date = LocalDateTime.now();
        this.type = type;
    }

    /**
     * This constructor is for sample data
     *
     * @param tweet
     * @param userToNotify
     * @param userWhoNotifies
     * @param date
     */
    public Notification(Tweet tweet, User userToNotify, User userWhoNotifies, LocalDateTime date, String type) {
        this.userToNotify = userToNotify;
        this.userWhoNotifies = userWhoNotifies;
        this.tweetTrigger = tweet;
        this.date = date;
        this.type = type;
    }
}
