import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TweetInformation } from './tweet.model';
import { Router } from '@angular/router';
import { TweetService } from 'src/app/services/tweet.service';

@Component({
  selector: 'app-tweet',
  templateUrl: './tweet.component.html',
  styleUrls: ['./tweet.component.css']
})
export class TweetComponent {
  @Input()
  tweet:TweetInformation;
  @Input()
  isLogged:boolean;
  @Output()
  delete = new EventEmitter<boolean>();
  @Output()
  bookmark = new EventEmitter<boolean>();
  constructor(private router: Router, private service: TweetService) {}

  urlToComment:string;
  profileIdURL:string;

  isMedia1:boolean;
  isMedia2:boolean;
  isMedia3:boolean;
  isMedia4:boolean;
  threeImages: boolean;

  svg:string;
  userTypeSVGMap = {
    "VERIFIED":"M22.25 12c0-1.43-.88-2.67-2.19-3.34.46-1.39.2-2.9-.81-3.91s-2.52-1.27-3.91-.81c-.66-1.31-1.91-2.19-3.34-2.19s-2.67.88-3.33 2.19c-1.4-.46-2.91-.2-3.92.81s-1.26 2.52-.8 3.91c-1.31.67-2.2 1.91-2.2 3.34s.89 2.67 2.2 3.34c-.46 1.39-.21 2.9.8 3.91s2.52 1.26 3.91.81c.67 1.31 1.91 2.19 3.34 2.19s2.68-.88 3.34-2.19c1.39.45 2.9.2 3.91-.81s1.27-2.52.81-3.91c1.31-.67 2.19-1.91 2.19-3.34zm-11.71 4.2L6.8 12.46l1.41-1.42 2.26 2.26 4.8-5.23 1.47 1.36-6.2 6.77z",
    "BANNED":"M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zm0-384c13.3 0 24 10.7 24 24V264c0 13.3-10.7 24-24 24s-24-10.7-24-24V152c0-13.3 10.7-24 24-24zM224 352a32 32 0 1 1 64 0 32 32 0 1 1 -64 0z"
  };

  viewBoxMap = {
    "VERIFIED": "0 0 24 24",
    "BANNED": "0 0 512 512",
  }

  viewBox:string;

  imageClass:string = "max-h-[500px] border bg-gray-4 dark:bg-twitter-dark-gray border-gray-4 rounded-xl overflow-hidden";

  ngOnInit(): void{
    this.tweet.urlToProfilePic = "/api" + this.tweet.urlToProfilePic

    this.isMedia1 = !(this.tweet.urlToMedia1 === "");
    this.isMedia2 = !(this.tweet.urlToMedia2 === "");
    this.isMedia3 = !(this.tweet.urlToMedia3 === "");
    this.isMedia4 = !(this.tweet.urlToMedia4 === "");
    this.tweet.urlToMedia1 = "/api" + this.tweet.urlToMedia1
    this.tweet.urlToMedia2 = "/api" + this.tweet.urlToMedia2
    this.tweet.urlToMedia3 = "/api" + this.tweet.urlToMedia3
    this.tweet.urlToMedia4 = "/api" + this.tweet.urlToMedia4
    this.threeImages = this.isMedia3 && !this.isMedia4;
    if (this.isMedia2){
      this.imageClass += " grid grid-cols-2";
    }
    if (this.isMedia4){
      this.imageClass += " grid grid-rows-2";
    }
    this.svg = this.userTypeSVGMap[this.tweet.tweet.user.type]
    this.viewBox = this.viewBoxMap[this.tweet.tweet.user.type]
  }

  giveRetweet(){
    if (! this.isLogged){
      return
    }
    this.service.toggleRetweet(this.tweet.tweet.id).subscribe(
      tweet => {
        this.tweet.retweeted = tweet.retweeted;
        this.tweet.numRetweets = tweet.numRetweets;
      },
      error => console.error(error)
    );

  }

  giveLike(){
    if (! this.isLogged){
      return
    }
    this.service.toggleLike(this.tweet.tweet.id).subscribe(
      tweet => {
        this.tweet.liked = tweet.liked;
        this.tweet.numLikes = tweet.numLikes;
      },
      error => console.error(error)
    );
  }

  giveBookmark(){
    if (! this.isLogged){
      return
    }
    this.service.toggleBookmark(this.tweet.tweet.id).subscribe(
      tweet => {
        this.tweet.bookmarked = tweet.bookmarked;
        this.bookmark.emit(true)
      },
      error => console.error(error)
    );
  }

  deleteElement() {
    let okResponse = window.confirm("Do you want to remove this tweet?");
        if (okResponse) {
          this.service.deleteTweet(this.tweet.tweet.id).subscribe(
                tweet => this.delete.emit(true),
                error => console.error(error)
            )
        }
  }

}
