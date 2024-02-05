package com.TwitterClone.ProjectBackend.service;

import com.TwitterClone.ProjectBackend.model.Hashtag;
import com.TwitterClone.ProjectBackend.model.Trend;
import com.TwitterClone.ProjectBackend.model.Tweet;
import com.TwitterClone.ProjectBackend.repository.HashtagRepository;
import com.TwitterClone.ProjectBackend.repository.TweetRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.util.*;

/**
 * Connect the hashtag controller with the hashtag repository
 */
@NoArgsConstructor
@AllArgsConstructor
@Service
public class HashtagService {

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private TweetRepository tweetRepository;

    /**
     * Obtain hashtag relation with the keyword
     *
     * @param keyword
     * @return
     */
    public List<Hashtag> findByHashtagIsContainingIgnoreCase(String keyword) {
        List<Hashtag> someHashtags = this.hashtagRepository.findByHashtagIsContainingIgnoreCase(keyword);
        return someHashtags;
    }

    /**
     * Get some trends
     *
     * @param init
     * @param size
     * @return
     */
    public List<Trend> getCurrentTrends(int init, int size) {
        List<Tuple> trends = this.hashtagRepository.find(init, size);
        List<Tuple> trendsList = trends.stream().toList();
        return this.converterToTrend(trendsList);
    }

    /**
     * Add the tweet to a hashtag
     *
     * @param hashtag
     * @param tweet
     */
    public void add(String hashtag, Tweet tweet) {
        Optional<Hashtag> h = this.hashtagRepository.findById(hashtag);

        if (h.isPresent()) {
            Hashtag h2 = h.get();
            h2.addTweet(tweet);
            this.hashtagRepository.save(h2);
            return;
        }

        Set<Tweet> set = new HashSet<>();
        set.add(tweet);
        Hashtag h2 = new Hashtag(hashtag, set);
        this.hashtagRepository.save(h2);
    }

    /**
     * Convert a list of Tuples to a list of Trends
     *
     * @param trends
     * @return
     */
    private List<Trend> converterToTrend(List<Tuple> trends) {
        List<Trend> list = new LinkedList<>();

        for (int i = 0; i < trends.size(); i++) {
            String numTweets = trends.get(i).get("numtweets").toString();
            String hashtag = trends.get(i).get("hashtag").toString();
            list.add(new Trend(hashtag, Integer.parseInt(numTweets)));
        }

        return list;
    }

    /**
     * Get some tweets associated to a trend
     *
     * @param hashtag
     * @param from
     * @param size
     * @return
     */
    public List<Tweet> getTweetsAssociatedTo(String hashtag, int from, int size) {
        return this.tweetRepository.getTweetsOfTrend(hashtag, from, size);
    }

    /**
     * Obtains the amount of trends
     *
     * @return
     */
    public int countTrends() {
        return this.hashtagRepository.countTrends();
    }

    /**
     * Obtains the amount of tweets associated to a hashtag
     *
     * @param hashtag
     * @return
     */
    public int countTweetsAssociated(String hashtag) {
        return this.hashtagRepository.countTweetsAssociated(hashtag);
    }
}
