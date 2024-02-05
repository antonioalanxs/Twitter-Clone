import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Tweet, TweetInformation } from 'src/app/entities/tweet/tweet.model';
import { TweetService } from 'src/app/services/tweet.service';

@Component({
  selector: 'app-write-tweet',
  templateUrl: './write-tweet.component.html',
  styleUrls: ['./write-tweet.component.css']
})
export class WriteTweetComponent {
  tweet: Tweet;
  isLogged: boolean;
  urlToProfilePic:string;
  tweetId: string;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private service: TweetService) {
    this.tweetId= this.activatedRoute.snapshot.params['id'];
    if (this.tweetId){
      this.service.getTweet(this.tweetId).subscribe(
        tweet => {
          this.tweet = tweet.tweet;
          this.urlToProfilePic = "/api" + tweet.urlToProfilePic;
        },
        error => console.error(error)
      );
    }
  }

  writeTweet(event, tweetInfo, tweetFiles){
    event.preventDefault();
    if (tweetInfo){
      let validFiles = [];
      for (let i = 0; i < tweetFiles.length; i++){
        let element = tweetFiles[i];
        let fileExtension = element.name.slice((element.name.lastIndexOf(".") - 1 >>> 0) + 2);
        if (fileExtension === "gif" || fileExtension === "png" || fileExtension === "jpg" || fileExtension === "jpeg"){
          validFiles.push(element);
        }
      }
      if (this.tweetId){
        this.service.postComment(tweetInfo, this.tweetId).subscribe(
          tweet => {
            let newTweet = tweet;
            if (validFiles.length > 0){
              this.service.postImages(tweetFiles, newTweet.tweet.id).subscribe();
            }
          },
          error => console.error(error)
        );
      }
      else{
        this.service.postTweet(tweetInfo).subscribe(
          tweet => {
            let newTweet = tweet;
            if (validFiles.length > 0){
              this.service.postImages(tweetFiles, newTweet.tweet.id).subscribe()
            }
          },
          error => console.error(error)
        );
      }
      this.router.navigate(['/home']);
    }
    else{
      alert("You can not publish an empty tweet")
    }
  }
}
