import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { TweetService } from 'src/app/services/tweet.service';
import { Tweet, TweetInformation } from '../tweet.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-tweet-list',
  templateUrl: './tweet-list.component.html',
  styleUrls: ['./tweet-list.component.css']
})
export class TweetListComponent {
  isLogged: boolean;
  tweets: TweetInformation[] = [];
  id: number;
  size: number = 10;
  offset: number = 0;
  @Input()
  typeElement: string;
  @Input()
  hashtag: string;
  @Input()
  userId: number;
  @Input()
  tweetId: number;
  canMore: boolean = true;

  constructor(private router: Router, private service: TweetService, private userService: UserService) { }

  ngOnInit() {
    this.userService.getCurrentUser().subscribe(
      user => {
        this.isLogged = user !== null;
        this.id = user.user.id;
      },
      error => this.isLogged = false
    );
    this.loadElements();
  }

  loadElements() {
    this.addSpinner();
    this.getTweetsForCurrentPage();
    this.offset += 10;
  }

  addSpinner() {
    document.getElementById("spinner").innerHTML = `<div class="flex items-center justify-center sticky">
                <div class="fixed inline-block h-8 w-8 animate-spin rounded-full border-4 border-primary border-r-transparent align-[-0.125em] motion-reduce:animate-[spin_1.5s_linear_infinite]" role="status">
                    <span class="!absolute !-m-px !h-px !w-px !overflow-hidden !whitespace-nowrap !border-0 !p-0 ![clip:rect(0,0,0,0)]">
                        Loading...
                    </span>
                </div>
            </div>`
  }

  removeSpinner() {
    document.getElementById("spinner").innerHTML = ``
  }

  hideButtons() {
    this.canMore = false;
  }

  getTweetsForCurrentPage() {
    switch (this.typeElement) {
      case "Home":
        this.service.getTweetsForCurrentUser(this.offset, this.size).subscribe(
          tweet => {
            if (tweet !== null) {
              tweet.forEach(t => this.tweets.push(t))
            } else {
              this.hideButtons()
            }
            this.removeSpinner()
          },
          error => console.error(error)
        );
        break;
      case "Profile":
        this.service.getTweetsOfAUser(this.userId, this.offset, this.size).subscribe(
          tweet => {
            if (tweet !== null) {
              tweet.forEach(t => this.tweets.push(t))
            } else {
              this.hideButtons()
            }
            this.removeSpinner()
          },
          error => console.error(error)
        );
        break;
      case "Bookmarks":
        this.service.getBookmarksOfCurrentUser(this.offset, this.size).subscribe(
          tweet => {
            if (tweet !== null) {
              tweet.forEach(t => this.tweets.push(t))
            } else {
              this.hideButtons()
            }
            this.removeSpinner()
          },
          error => console.error(error)
        );
        break;
      case "Hashtag":
        this.service.getTweetsOfAHashtag(this.hashtag, this.offset, this.size).subscribe(
          tweet => {
            if (tweet !== null) {
              tweet.forEach(t => this.tweets.push(t))
            } else {
              this.hideButtons()
            }
            this.removeSpinner()
          },
          error => console.error(error)
        );
        break;
        case "Replies":
        this.service.getRepliesOfATweet(this.tweetId, this.offset, this.size).subscribe(
          tweet => {
            if (tweet !== null) {
              tweet.forEach(t => this.tweets.push(t))
            } else {
              this.hideButtons()
            }
            this.removeSpinner()
          },
          error => console.error(error)
        );
        break;
    }
  }

  deleteElement(item){
    let index = this.tweets.indexOf(item);
    if (index > -1) {
      this.tweets.splice(index, 1);
      this.offset -= 1;
    }
  }

  removeElement(item){
    if (this.typeElement === 'Bookmarks'){
        this.deleteElement(item);
    }
  }
}
