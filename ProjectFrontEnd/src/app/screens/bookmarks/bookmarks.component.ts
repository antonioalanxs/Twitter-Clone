import { Component } from '@angular/core';

import { TweetService } from 'src/app/services/tweet.service';

import { TweetInformation } from 'src/app/entities/tweet/tweet.model';

@Component({
  selector: 'app-bookmarks',
  templateUrl: './bookmarks.component.html',
  styleUrls: ['./bookmarks.component.css'],
  providers: [TweetService]
})
export class BookmarksComponent {
  public tweets: TweetInformation[]

  constructor(private _tweetService: TweetService) {
    this._tweetService.getBookmarksOfCurrentUser(0, 10).subscribe(
      tweets => this.tweets,
      error => console.log(error))
  }
}
