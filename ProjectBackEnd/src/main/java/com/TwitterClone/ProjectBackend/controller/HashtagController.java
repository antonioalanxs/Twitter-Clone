package com.TwitterClone.ProjectBackend.controller;

import com.TwitterClone.ProjectBackend.model.mustacheObjects.InformationManager;
import com.TwitterClone.ProjectBackend.model.mustacheObjects.TweetInformation;
import com.TwitterClone.ProjectBackend.model.Trend;
import com.TwitterClone.ProjectBackend.model.Tweet;
import com.TwitterClone.ProjectBackend.service.HashtagService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * This controller manages all the petitions relation with hashtags
 */
@Controller
public class HashtagController {

    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private InformationManager informationManager;

    /**
     * Load more trends using AJAX
     *
     * @param model
     * @param from
     * @param size
     * @return
     */
    @GetMapping("/explore/trends")
    public String loadMoreTrends(Model model,
                                 @Param("from") int from,
                                 @Param("size") int size) {
        int numTrends = this.hashtagService.countTrends();

        if (numTrends <= from) {
            return "redirect:/";
        }

        List<Trend> newTrends = this.hashtagService.getCurrentTrends(from, size);
        model.addAttribute("trends", newTrends);

        model.addAttribute("isExplorePage", true);

        return "explore_element";
    }

    /**
     * Load more tweets associated to a hashtag using AJAX
     *
     * @param model
     * @param hashtag
     * @param from
     * @param size
     * @param request
     * @return
     */
    @GetMapping("/explore_more/{hashtag}")
    public String loadMoreTweetsAssociated(Model model,
                                           @PathVariable String hashtag,
                                           @PathParam("from") int from,
                                           @PathParam("size") int size,
                                           HttpServletRequest request) {
        User currentUser = this.informationManager.getCurrentUser(request);
        int numTweetsAssociated = this.hashtagService.countTweetsAssociated(hashtag);

        if (numTweetsAssociated <= from) {
            return "redirect:/";
        }

        List<Tweet> tweetsAssociated = this.hashtagService.getTweetsAssociatedTo(hashtag, from, size);
        List<TweetInformation> tweets = this.informationManager.calculateDataOfTweet(tweetsAssociated, currentUser);
        model.addAttribute("tweets", tweets);

        if (currentUser != null) {
            model.addAttribute("isLogged", true);
        }

        return "tweet";
    }
}
