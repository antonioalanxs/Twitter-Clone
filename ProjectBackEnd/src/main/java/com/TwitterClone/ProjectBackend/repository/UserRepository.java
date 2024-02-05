package com.TwitterClone.ProjectBackend.repository;

import com.TwitterClone.ProjectBackend.userManagement.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * This Repository is the connection to DB for User Entity
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * This Query is the advanced one
     *
     * @param username
     * @return
     */
    List<User> findByUsernameContainingIgnoreCase(String username);

    /**
     * This Query returns a user with the provided id
     *
     * @param id
     * @return
     */
    Optional<User> findById(Long id);

    /**
     * This Query returns a user with the provided username
     *
     * @param username
     * @return
     */
    Optional<User> findByUsername(String username);

    /**
     * This Query returns a user with the provided email
     *
     * @param email
     * @return
     */
    Optional<User> findByEmail(String email);

    /**
     * This Query returns a user with the provided token
     *
     * @param token
     * @return
     */
    Optional<User> findByResetPasswordToken(String token);

    /**
     * This Query returns a user with the provided verification code
     *
     * @param code
     * @return
     */
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    User findByVerificationCode(String code);

    /**
     * This Query returns the amount of followed accounts a user has
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM users_followed WHERE user_id = ?1 GROUP BY user_id", nativeQuery = true)
    Long countFollowed(long id);

    /**
     * Count the followers of a user
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM users_followers WHERE user_id = ?1 GROUP BY user_id", nativeQuery = true)
    Long countFollowers(Long id);

    /**
     * Find all the banned user
     *
     * @return
     */
    @Query(value = "SELECT * FROM users WHERE enabled = false", nativeQuery = true)
    List<User> findBanned();

    /**
     * Get some followers of a user
     *
     * @param id
     * @param from
     * @param size
     * @return
     */
    @Query(value = "SELECT users.* FROM users_followers JOIN users ON followers_id=id WHERE user_id = ?1 LIMIT ?2,?3", nativeQuery = true)
    List<User> findFollowers(Long id, int from, int size);

    /**
     * This Query returns the verified users
     *
     * @return
     */
    @Query(value = "SELECT * FROM users WHERE type = 'VERIFIED'", nativeQuery = true)
    List<User> findVerified();

    /**
     * This Query returns the users that are neither VERIFIED nor BANNED
     *
     * @return
     */
    @Query(value = "SELECT * FROM users WHERE type<>'VERIFIED' AND type<>'BANNED'", nativeQuery = true)
    List<User> findNotVerifiedNotBanned();

    /**
     * Counts the new user in the last 5 days
     *
     * @return
     */
    @Query(value = "SELECT join_date, COUNT(*) AS new_people FROM users GROUP BY join_date ORDER BY join_date DESC LIMIT 0,5", nativeQuery = true)
    List<Tuple> countByLast5JoinDate();

    /**
     * Get some followed user
     *
     * @param id
     * @param from
     * @param size
     * @return
     */
    @Query(value = "SELECT users.* FROM users_followed JOIN users ON followed_id=id WHERE user_id = ?1 LIMIT ?2,?3 ", nativeQuery = true)
    List<User> findFollowed(Long id, int from, int size);

    /**
     * Get user to be recommended
     *
     * @param userId
     * @return
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.followers f JOIN f.followers f2 WHERE f2.id = :userId AND u.id NOT IN (SELECT f2.id FROM User u2 JOIN u2.followed f2 WHERE u2.id = :userId) AND u.id <> :userId")
    List<User> findRecommendedUsers(@Param("userId") Long userId);

    @Query(value = "SELECT users.* FROM users_followed JOIN users ON followed_id = id WHERE user_id = ?1 AND username = ?2 ", nativeQuery = true)
    Optional<User> getUserFollowed(Long username1, String username2);
}

