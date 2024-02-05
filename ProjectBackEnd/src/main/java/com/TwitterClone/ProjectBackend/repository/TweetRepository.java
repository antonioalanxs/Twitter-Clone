package com.TwitterClone.ProjectBackend.repository;

import com.TwitterClone.ProjectBackend.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * This Repository is the connection to DB for Tweet Entity
 */
@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    /**
     * This Query returns a List of Tweets written by a user ordered by the date when they were published
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT * from tweet WHERE user_id = ?1 ORDER BY publish_date DESC LIMIT ?2, ?3", nativeQuery = true)
    List<Tweet> findByUser(Long id, int from, int size);

    /**
     * Count the user's tweets
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT COUNT(*) from tweet WHERE user_id = ?1", nativeQuery = true)
    int countUserTweets(Long id);

    /**
     * This Query returns a List of Tweets written by the users who the user follows ordered by the date when they were published
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT * from tweet t join (SELECT followed_id from users_followed WHERE user_id = ?1) f on t.user_id = f.followed_id ORDER BY t.publish_date DESC LIMIT ?2, ?3", nativeQuery = true)
    List<Tweet> findByUserFollows(Long id, int init, int size);

    /**
     * Counts the tweets for the current user
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT COUNT(*) from tweet t join (SELECT followed_id from users_followed WHERE user_id = ?1) f on t.user_id = f.followed_id", nativeQuery = true)
    int countTweetsForUser(Long id);

    /**
     * This Query returns a List of Tweets bookmarked by the user
     *
     * @param id
     * @param offset
     * @param size
     * @return
     */
    @Query(value = "SELECT * FROM tweet t JOIN (SELECT bookmarks_id FROM users_bookmarks WHERE user_id = ?1) b ON t.id = b.bookmarks_id ORDER BY publish_date LIMIT ?2,?3", nativeQuery = true)
    List<Tweet> findBookmarksByUserId(Long id, int offset, int size);

    /**
     * Count the bookmarks associated to the current user
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM tweet t JOIN (SELECT bookmarks_id FROM users_bookmarks WHERE user_id = ?1) b ON t.id = b.bookmarks_id", nativeQuery = true)
    int countBookmarks(Long id);

    /**
     * This Query returns a List of Tweets that are comments of another Tweet
     *
     * @param id
     * @param offset
     * @param size
     * @return
     */
    @Query(value = "SELECT * from  tweet WHERE comments_id = ?1 ORDER BY publish_date LIMIT ?2,?3", nativeQuery = true)
    List<Tweet> findCommentsById(Long id, int offset, int size);

    /**
     * This Query returns a List of Tweets retweeted by the user
     *
     * @param id
     * @param offset
     * @param size
     * @return
     */
    @Query(value = "SELECT * FROM tweet_retweets NATURAL JOIN tweet WHERE retweets_id = ?1 ORDER BY publish_date DESC LIMIT ?2,?3", nativeQuery = true)
    List<Tweet> findRetweetsByUser(Long id, int offset, int size);

    /**
     * This Query returns a List of Tweets liked by the user
     *
     * @param id
     * @param offset
     * @param size
     * @return
     */
    @Query(value = "SELECT * FROM tweet_likes NATURAL JOIN tweet WHERE like_id = ?1 ORDER BY publish_date DESC LIMIT ?2,?3", nativeQuery = true)
    List<Tweet> findLikesByUser(Long id, int offset, int size);

    /**
     * This Query returns the amount of likes a Tweet has
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM tweet_likes JOIN tweet ON tweet_id = id WHERE tweet_id = ?1 GROUP BY tweet_id", nativeQuery = true)
    Long countLikes(Long id);

    /**
     * This Query returns the amount of retweets a Tweet has
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM tweet_retweets JOIN tweet ON tweet_id = id WHERE tweet_id = ?1 GROUP BY tweet_id", nativeQuery = true)
    Long countRetweets(Long id);

    /**
     * This Query returns the amount of comments a Tweet has
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM tweet WHERE comments_id = ?1 GROUP BY comments_id", nativeQuery = true)
    Long countComments(Long id);

    /**
     * Get some tweets associated to a trend
     *
     * @param id
     * @param from
     * @param size
     * @return
     */
    @Query(value = "SELECT tweet.* FROM hashtag_tweets JOIN tweet ON tweets_id = id WHERE hashtag_hashtag = ?1 ORDER BY publish_date DESC LIMIT ?2,?3", nativeQuery = true)
    List<Tweet> getTweetsOfTrend(String id, int from, int size);

    /**
     * Deletes a hashtag from a tweet
     *
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM hashtag_tweets WHERE tweets_id=?1", nativeQuery = true)
    void deleteHashtagOfTweet(Long id);

    /**
     * Deletes a Bookmark from a tweet
     *
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM users_bookmarks WHERE bookmarks_id=?1", nativeQuery = true)
    void deleteBookmarkOfTweet(Long id);

    /**
     * Deletes a Notification from a tweet
     *
     * @param id
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM notification WHERE tweet_trigger_id=?1", nativeQuery = true)
    void deleteNotificationsOfTweet(Long id);
}
