package com.TwitterClone.ProjectBackend.model;

import lombok.*;

/**
 * Trend is a Model for showing the info related to a hashtag in the explore page.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trend {

    private String hashtag;
    private int numTweets;

}
