/**
 * Give o remove a like to a tweet
 * @param {String} tweet_id 
 * @returns {Promise<void>}
 */
async function giveLike(tweet_id, tweet_owner_id) {
    const like_container = document.querySelector('#like-' + tweet_id);
    const like_svg = document.querySelector('#like-svg-' + tweet_id);
    let text = like_container.textContent;
    let value = parseInt(text);

    const response = await fetch(`/tweet/like/${tweet_id}`);
    const state = response.redirected;
    let notificationType = "LIKE";

    if (!state) {
        value++;
        await fetch(`/newNotification?idTweet=${tweet_id}&idOwner=${tweet_owner_id}&notificationType=${notificationType}`);
    } else {
        value--;
        await fetch(`/deleteNotification?idTweet=${tweet_id}&notificationType=${notificationType}`);
    }

    like_svg.classList.toggle('fill-red-0');
    like_svg.classList.toggle('fill-gray-4');
    like_container.textContent = value;
};

/**
 * Give o remove a retweet to a tweet
 * @param {String} tweet_id 
 * @returns {Promise<void>}
 */
async function giveRetweet(tweet_id, tweet_owner_id) {
    const retweet_container = document.querySelector('#retweet-' + tweet_id);
    const retweet_svg = document.querySelector('#retweet-svg-' + tweet_id);
    let text = retweet_container.textContent;
    let value = parseInt(text);
    let notificationType = "RETWEET";

    const response = await fetch(`/tweet/retweet/${tweet_id}`);
    const state = response.redirected;

    if (!state) {
        value++;
        await fetch(`/newNotification?idTweet=${tweet_id}&idOwner=${tweet_owner_id}&notificationType=${notificationType}`);
    } else {
        value--;
        await fetch(`/deleteNotification?idTweet=${tweet_id}&notificationType=${notificationType}`);
    }

    retweet_svg.classList.toggle('fill-green-0');
    retweet_svg.classList.toggle('fill-gray-4');
    retweet_container.textContent = value;
};

/**
 * Give or remove a bookmark of a tweet
 * @param {String} tweet_id 
 * @returns {Promise<void>}
 */
async function giveBookmark(tweet_id) {
    const bookmark_svg = document.querySelector('#bookmark-svg-' + tweet_id);

    await fetch(`/tweet/bookmark/${tweet_id}`);

    bookmark_svg.classList.toggle('fill-primary');
    bookmark_svg.classList.toggle('fill-gray-4');
};

/**
 * Deletes a tweet
 * @param {String} tweet_id 
 * @returns {Promise<void>}
 */
async function deleteTweet(tweet_id) {
    const tweet_container = document.querySelector('#tweet-' + tweet_id);

    await fetch(`/tweet/delete/${tweet_id}`);

    tweet_container.classList.toggle('hidden');
};