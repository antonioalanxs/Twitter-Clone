import { Tweet } from "../tweet/tweet.model";
import { User } from "../user/user.model";

export interface Notification {

    id: number;
    tweetTrigger: Tweet;
    userToNotify: User;
    userWhoNotifies: User;
    type: string;
}
