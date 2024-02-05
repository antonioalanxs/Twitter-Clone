package com.TwitterClone.ProjectBackend.repository;

import com.TwitterClone.ProjectBackend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This Repository is the connection to DB for Notification Entity
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    /**
     * This Query returns a List of Notification of a user ordered by the date when they appeared
     *
     * @param user_id
     * @param init
     * @param size
     * @return
     */
    @Query(value = "SELECT * FROM notification WHERE user_to_notify_id = ?1 ORDER BY date DESC LIMIT ?2,?3", nativeQuery = true)
    List<Notification> findNotifications(long user_id, int init, int size);

    /**
     * This Query returns a List of Notification that are Mentions of a user ordered by the date when they appeared
     *
     * @param user_id
     * @param init
     * @param size
     * @return
     */
    @Query(value = "SELECT * FROM notification WHERE user_to_notify_id = ?1 AND type='MENTION' ORDER BY date DESC LIMIT ?2,?3", nativeQuery = true)
    List<Notification> findMentions(long user_id, int init, int size);

    /**
     * This Query returns the required Notification because when delete we do not have the id
     *
     * @param userWhoNotifiesId
     * @param tweetId
     * @param type
     * @return
     */
    @Query(value = "SELECT * FROM notification WHERE user_who_notifies_id = ?1 AND tweet_trigger_id = ?2 AND type = ?3", nativeQuery = true)
    Optional<Notification> findSpecificNotification(long userWhoNotifiesId, long tweetId, String type);

    /**
     * Find a specific follow notification
     *
     * @param userWhoNotifiesId
     * @param userToNotify
     * @return
     */
    @Query(value = "SELECT * FROM notification WHERE user_who_notifies_id = ?1 AND type = 'FOLLOW' AND user_to_notify_id=?2", nativeQuery = true)
    Optional<Notification> findFollowNotification(long userWhoNotifiesId, long userToNotify);

    /**
     * Counts the notifications associated to a user
     *
     * @param idCurrentUser
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM notification WHERE user_to_notify_id = ?1", nativeQuery = true)
    int countNotifications(Long idCurrentUser);

    /**
     * Counts the mentions associated to a user
     *
     * @param idCurrentUser
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM notification WHERE user_to_notify_id = ?1 AND type='MENTION'", nativeQuery = true)
    int countMentions(Long idCurrentUser);
}