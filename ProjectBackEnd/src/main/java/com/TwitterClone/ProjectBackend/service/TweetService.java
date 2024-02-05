package com.TwitterClone.ProjectBackend.service;

import com.TwitterClone.ProjectBackend.model.Tweet;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.ImageManager;
import com.TwitterClone.ProjectBackend.repository.TweetRepository;
import com.TwitterClone.ProjectBackend.repository.UserRepository;
import com.TwitterClone.ProjectBackend.userManagement.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.util.List;
import java.util.Optional;

/**
 * This class is on charge of implementing all the necessary logic for the Tweets.
 */
@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageManager imageManager;

    /**
     * Obtain some tweets associated to a user
     *
     * @param id
     * @param from
     * @param size
     * @return
     */
    public List<Tweet> findSomeTweetOfUser(Long id, int from, int size) {
        return tweetRepository.findByUser(id, from, size);
    }

    /**
     * Obtain some recent tweets for the current user
     *
     * @param id
     * @param init
     * @param size
     * @return
     */
    public List<Tweet> findSomeRecentForUser(Long id, int init, int size) {
        return tweetRepository.findByUserFollows(id, init, size);
    }

    /**
     * Creates a new tweet
     *
     * @param text
     * @param files
     * @param userId
     * @return
     */
    public Tweet createTweet(String text, Blob[] files, Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        Tweet tweet = new Tweet(text, user, files);
        tweetRepository.save(tweet);
        return tweet;
    }

    /**
     * Deletes a tweet
     *
     * @param tweet
     */
    public void deleteTweet(Tweet tweet) {
        tweetRepository.deleteHashtagOfTweet(tweet.getId());
        tweetRepository.deleteBookmarkOfTweet(tweet.getId());
        tweetRepository.deleteNotificationsOfTweet(tweet.getId());
        tweetRepository.delete(tweet);
    }

    /**
     * Obtain a tweet using an id
     *
     * @param id
     * @return
     */
    public Optional<Tweet> findById(Long id) {
        return tweetRepository.findById(id);
    }

    /**
     * Check the tweet to add or remove a like
     *
     * @param giver
     * @param t
     * @return
     */
    public boolean toggleLike(User giver, Tweet t) {
        boolean liked = false;

        if (this.isLiked(giver, t)) {
            t.removeLike(giver);
        } else {
            t.addLike(giver);
            liked = true;
        }

        this.tweetRepository.save(t);
        return liked;
    }

    /**
     * Checks if the current user had given a like
     *
     * @param user
     * @param tweet
     * @return
     */
    public boolean isLiked(User user, Tweet tweet) {
        if (user == null) {
            return false;
        }

        return tweet.getLikes().contains(user);
    }

    /**
     * Check the tweet to add or remove a retweet
     *
     * @param giver
     * @param t
     * @return
     */
    public boolean toggleRetweet(User giver, Tweet t) {
        boolean retweeted = false;

        if (this.isRetweeted(giver, t)) {
            t.removeRetweet(giver);
        } else {
            t.addRetweet(giver);
            retweeted = true;
        }

        this.tweetRepository.save(t);
        return retweeted;
    }

    /**
     * Checks if the current user had given a retweet
     *
     * @param user
     * @param tweet
     * @return
     */
    public boolean isRetweeted(User user, Tweet tweet) {
        if (user == null) {
            return false;
        }

        return tweet.getRetweets().contains(user);
    }

    /**
     * Check the tweet to add or remove a bookmark
     *
     * @param currentUser
     * @param t
     */
    public boolean toggleBookmark(User currentUser, Tweet t) {
        boolean bookmarked = false;
        List<Tweet> bookmarks = currentUser.getBookmarks();

        if (bookmarks.contains(t)) {
            bookmarks.remove(t);
        } else {
            bookmarks.add(t);
            bookmarked = true;
        }

        currentUser.setBookmarks(bookmarks);
        this.userRepository.save(currentUser);
        return bookmarked;
    }

    /**
     * Checks if the current user had bookmarked
     *
     * @param user
     * @param tweet
     * @return
     */
    public boolean isBookmarked(User user, Tweet tweet) {
        if (user == null) {
            return false;
        }

        return user.getBookmarks().contains(tweet);
    }

    /**
     * Add a comment to a tweet
     *
     * @param tweet
     */
    public void addComment(Long idTweetReplied, Tweet tweet) {
        Tweet t = this.tweetRepository.findById(idTweetReplied).orElse(null);

        if (t == null) {
            return;
        }

        t.addComment(tweet);
        this.tweetRepository.save(t);
    }

    /**
     * Get the number of likes of a tweet
     *
     * @param id
     * @return
     */
    public Long getLikesOfTweet(Long id) {
        Long number = this.tweetRepository.countLikes(id);

        if (number == null) {
            return 0L;
        }

        return number;
    }

    /**
     * Get the number of comments of a tweet
     *
     * @param id
     * @return
     */
    public Long getCommentsOfTweet(Long id) {
        Long number = this.tweetRepository.countComments(id);

        if (number == null) {
            return 0L;
        }

        return number;
    }

    /**
     * Get the number of retweets of a tweet
     *
     * @param id
     * @return
     */
    public Long getRetweetsOfTweet(Long id) {
        Long number = this.tweetRepository.countRetweets(id);

        if (number == null) {
            return 0L;
        }

        return number;
    }

    /**
     * Get some comments associated to a tweet
     *
     * @param id
     * @param offset
     * @param size
     * @return
     */
    public List<Tweet> getComments(Long id, int offset, int size) {
        return this.tweetRepository.findCommentsById(id, offset, size);
    }

    public void addImages(Tweet tweet, MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4) {
        Blob preparedImage;

        if (image1 != null) {
            preparedImage = this.imageManager.prepareImageFile(image1);
            tweet.setMedia1(preparedImage);
        }

        if (image2 != null) {
            preparedImage = this.imageManager.prepareImageFile(image2);
            tweet.setMedia2(preparedImage);
        }

        if (image3 != null) {
            preparedImage = this.imageManager.prepareImageFile(image3);
            tweet.setMedia3(preparedImage);
        }

        if (image4 != null) {
            preparedImage = this.imageManager.prepareImageFile(image4);
            tweet.setMedia4(preparedImage);
        }

        this.tweetRepository.save(tweet);
    }
}