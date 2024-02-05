package com.TwitterClone.ProjectBackend.model.mustacheObjects;

import com.TwitterClone.ProjectBackend.model.Trend;
import com.TwitterClone.ProjectBackend.service.HashtagService;
import com.TwitterClone.ProjectBackend.service.ProfileService;
import com.TwitterClone.ProjectBackend.userManagement.User;
import com.TwitterClone.ProjectBackend.userManagement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.persistence.Tuple;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class ModelManager {

    @Autowired
    private HashtagService hashtagService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserService userService;
    @Autowired
    private InformationManager informationManager;

    /**
     * Add to the template the current trends
     *
     * @param model
     */
    public void addCurrentTrends(Model model) {

        List<Trend> trends = this.hashtagService.getCurrentTrends(0, 5);

        model.addAttribute("trends", trends);
    }

    /**
     * Add charts to the dashboard page
     *
     * @param model
     */
    public void addStatistics(Model model) {
        List<Tuple> statics = this.profileService.getStatics();
        String[] amounts = new String[statics.size()];
        String[] dates = new String[statics.size()];

        for (int i = 0; i < statics.size(); i++) {
            dates[i] = statics.get(i).get("join_date").toString();
            amounts[i] = statics.get(i).get("new_people").toString();
        }

        model.addAttribute("amounts", amounts);
        model.addAttribute("dates", dates);
    }

    /**
     * Add recommended users
     *
     * @param model
     * @param request
     */
    public void addRecommended(Model model, HttpServletRequest request) {
        User currentUser = this.informationManager.getCurrentUser(request);

        if (currentUser == null) {
            return;
        }

        List<User> recommended = this.userService.getRecommendedUsers(currentUser.getId());
        model.addAttribute("usersToFollow", recommended);
    }
}
