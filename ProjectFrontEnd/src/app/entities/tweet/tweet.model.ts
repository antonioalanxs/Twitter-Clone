import { User } from "../user/user.model";

export interface TweetInformation{
    numComments:number;
    numLikes:number;
    numRetweets:number;
    retweeted:boolean;
    liked:boolean;
    bookmarked:boolean;
    urlToProfilePic:string;
    urlToMedia1:string;
    urlToMedia2:string;
    urlToMedia3:string;
    urlToMedia4:string;
    isAuthorised:boolean;
    tweet:Tweet;
    authorised:boolean;
}

export interface Tweet{
    id: number;
    user: User;
    publishDate:Date;
    text:string;
}


