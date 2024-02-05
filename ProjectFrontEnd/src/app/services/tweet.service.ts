
import { Injectable } from '@angular/core';
import { throwError, Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Tweet, TweetInformation } from '../entities/tweet/tweet.model';

@Injectable({
  providedIn: 'root'
})
export class TweetService {
  constructor(
    private httpClient: HttpClient,
  ) { }

	getRepliesOfATweet(tweetId: number, from: number, size: number) {
		let url = "/api/tweets/" + tweetId + "/comments?from=" + from + "&size=" + size
			return this.httpClient.get(url).pipe(
				catchError(error => this.handleError(error))
			) as Observable<TweetInformation[]>;
		}

    getTweetsOfAUser(userId, from, size): Observable<TweetInformation[]> {
		let url = "/api/users/" + userId + "/tweets?from=" + from + "&size=" + size
		return this.httpClient.get(url).pipe(
			catchError(error => this.handleError(error))
		) as Observable<TweetInformation[]>;
	}

  getBookmarksOfCurrentUser(from, size): Observable<TweetInformation[]> {
    let url = "/api/bookmarks?from=" + from + "&size=" + size
    return this.httpClient.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<TweetInformation[]>;
  }

  getTweetsOfAHashtag(hashtag, from, size): Observable<TweetInformation[]> {
    let url = "/api/hashtag/" + hashtag + "/tweets?from=" + from + "&size=" + size
    return this.httpClient.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<TweetInformation[]>;
  }

  getTweetsForCurrentUser(from, size): Observable<TweetInformation[]> {
    let url = "/api/tweets?from=" + from + "&size=" + size
    return this.httpClient.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<TweetInformation[]>;
  }

  toggleBookmark(id) {
    let url = "/api/tweets/" + id + "/bookmarks"
    return this.httpClient.put(url, "").pipe(
      catchError(error => this.handleError(error))
    ) as Observable<TweetInformation>;
  }

  toggleLike(id) {
    let url = "/api/tweets/" + id + "/likes"
    return this.httpClient.put(url, "").pipe(
      catchError(error => this.handleError(error))
    ) as Observable<TweetInformation>;
  }

  toggleRetweet(id) {
    let url = "/api/tweets/" + id + "/retweets"
    return this.httpClient.put(url, "").pipe(
      catchError(error => this.handleError(error))
    ) as Observable<TweetInformation>;
  }

  deleteTweet(id) {
    let url = "/api/tweets/" + id
    return this.httpClient.delete(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<TweetInformation>;
  }

	getTweet(id){
		let url = "/api/tweets/" + id
		return this.httpClient.get(url).pipe(
			catchError(error => this.handleError(error))
		) as Observable<TweetInformation>;
	}

  postTweet(text) {
    let url = "/api/tweets/"
    let object = { "text": text }
    let httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    }
    return this.httpClient.post(url, object, httpOptions).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<TweetInformation>;
  }

  postComment(text, id) {
    let url = "/api/tweets/" + id + "/comments"
    let object = { "text": text }
    let httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    }
    return this.httpClient.post(url, object, httpOptions).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<TweetInformation>;
  }

  postImages(files, id) {
    let url = "/api/tweets/" + id + "/images";

    let formData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append("image" + (i + 1), files[i], files[i].name);
    }
    let headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    return this.httpClient.put(url, formData, { headers: headers }).pipe(
      catchError(error => this.handleError(error))
    ) as any;
  }

  private handleError(error: any) {
    console.error(error);
    return throwError("Server error (" + error.status + "): " + error.text())
  }
}
