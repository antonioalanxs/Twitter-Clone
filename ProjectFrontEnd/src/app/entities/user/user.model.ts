import { TweetInformation } from "../tweet/tweet.model";

export interface User {
  id: number;
  username: string;
  nickname: string;
  type: string;
  enabled: boolean;
  biography: string;
  joinDate: string;
  role: string;
}

export interface UserInformation {
  user: User;
  urlToProfilePic: string;
  urlToBannerPic: string;
  followersNumber: number;
  followedNumber: number;
  tweets: Array<TweetInformation>;
}

