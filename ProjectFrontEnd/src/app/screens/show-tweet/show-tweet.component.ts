import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TweetInformation } from 'src/app/entities/tweet/tweet.model';
import { TweetService } from 'src/app/services/tweet.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-show-tweet',
  templateUrl: './show-tweet.component.html',
  styleUrls: ['./show-tweet.component.css']
})
export class ShowTweetComponent {

  filtersLoaded: Promise<boolean>;
  tweetId: number;
  tweet: TweetInformation;
  isLogged: boolean;
  url:string;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private service: TweetService, private userService: UserService){
    this.tweetId = this.activatedRoute.snapshot.params['id'];
    this.service.getTweet(this.tweetId).subscribe(
      tweet => {
        this.tweet = tweet;
        this.filtersLoaded = Promise.resolve(true);
      },
      error => console.error(error)
    );
    this.userService.getCurrentUser().subscribe(
      user => this.isLogged = user !== undefined,
      error => this.isLogged = false
    )
  }

  returnHome(){
    this.router.navigateByUrl("home");
  }
}
