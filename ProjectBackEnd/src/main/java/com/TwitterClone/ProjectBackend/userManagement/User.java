package com.TwitterClone.ProjectBackend.userManagement;

import com.TwitterClone.ProjectBackend.model.Tweet;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.*;

/**
 * This is User Entity. A User is a person who has already registered in the application.
 * They register with a mail account and a password which is cypher for security.
 * A user has a unique username which will appear in the application with an @. It also has a customizable nickname.
 * The User can write a description about they in order to tell other Users about them.
 * The User can customize its own profile uploading a profile banner and a profile picture that depicts them
 * The User can also keep Tweets as Bookmarks in order to see them later.
 * The User has different roles. It could be a normal User or an Admin.
 * The Admin is in charge of moderating the interactions between Users by
 * banning Users that are not respecting the community or by verify famous Users.
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {
    public interface Basic {
    }
    

    public interface Profile extends Basic {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Basic.class)
    private Long id;
    @Column(unique = true)
    @JsonView(Basic.class)
    private String username;
    @Column(unique = true)
    private String email;
    @JsonView(Basic.class)
    private String nickname;
    @JsonView(Profile.class)
    private String biography = "";
    private String password;
    @Lob
    private Blob profilePicture;
    @Lob
    private Blob profileBanner;
    @Enumerated(EnumType.STRING)
    @JsonView(Profile.class)
    private UserRoles role;
    @ManyToMany
    private List<User> followers = new ArrayList<>();
    @ManyToMany
    private List<User> followed = new ArrayList<>();
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Tweet> bookmarks = new ArrayList<>();
    @JsonView(Basic.class)
    private final LocalDate joinDate;
    @JsonView(Basic.class)
    private String type;
    @Column(name = "verification_code", length = 64)
    private String verificationCode;
    @Column(name = "reset_password_token")
    private String resetPasswordToken;
    private boolean loggedIn;
    @JsonView(Basic.class)
    private boolean enabled = true;

    /**
     * Main constructor.
     */

    public User(@NotNull @NotBlank String username,
                @NotNull @NotBlank String password,
                @NotNull @NotBlank String email,
                String role) throws IOException {
        super();
        this.username = username;
        this.nickname = username;
        this.password = password;
        this.email = email;
        if (role.isBlank()) this.role = UserRoles.valueOf("USER");
        else this.role = UserRoles.valueOf(role);
        this.joinDate = java.time.LocalDate.now();
        this.loggedIn = false;
        this.type = "PUBLIC";
    }

    /**
     * Empty constructor for DB.
     */
    public User() {
        this.joinDate = java.time.LocalDate.now();
    }

    /**
     * This constructor is for example data.
     */
    public User(String username, String nickname, String biography, String mail, String password, LocalDate time, String type) {
        this.username = username;
        this.nickname = nickname;
        this.biography = biography;
        this.joinDate = time;
        this.role = UserRoles.valueOf("USER");
        this.email = mail;
        this.password = password;
        this.type = type;
    }

    /**
     * This is for adding the profilePicture and the profileBanner directly from the path
     *
     * @param files
     * @throws IOException
     */
    public void setImages(String[] files) throws IOException {
        Resource image = new ClassPathResource(files[0]);
        this.profilePicture = BlobProxy.generateProxy(image.getInputStream(), image.contentLength());
        image = new ClassPathResource(files[1]);
        this.profileBanner = BlobProxy.generateProxy(image.getInputStream(), image.contentLength());
    }

    /**
     * This is for adding a follower user to the user
     *
     * @param user
     */
    public void addFollower(User user) {
        this.followers.add(user);
    }

    /**
     * This is for adding a followed user to the user
     *
     * @param user
     */
    public void addFollowed(User user) {
        this.followed.add(user);
    }

    /**
     * Get the number of followers
     *
     * @return
     */
    public int getFollowersNumber() {
        return this.followers.size();
    }

    /**
     * Get the number of followed
     *
     * @return
     */
    public int getFollowedNumber() {
        return this.followed.size();
    }

    /**
     * Remove a follower user
     *
     * @param user
     */
    public void removeFollower(User user) {
        this.followers.remove(user);
    }

    /**
     * Remove a followed user
     *
     * @param user
     */
    public void removeFollowed(User user) {
        this.followed.remove(user);
    }
}