import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { UserService } from "./user.service";
import { Observable, catchError, throwError } from "rxjs";
import { TweetService } from "./tweet.service";
import { UserInformation } from "../entities/user/user.model";
import { Trend } from "../entities/trend/trend.model";

@Injectable({ providedIn: 'root' })
export default class SearchService {
    constructor(private http: HttpClient, private userService: UserService, private tweetService: TweetService) {}

    searchProfiles(keyword:string): Observable<UserInformation[]>{
        let url = "/api/users/" + keyword + "/found-users"
        return this.http.get(url).pipe(
            catchError(error => this.handleError(error)
            )) as Observable<UserInformation[]>
    }

    searchHashtags(keyword:string):Observable<Trend[]>{
        let url = "/api/hashtags/" + keyword + "/found-hashtags"
        return this.http.get(url).pipe(
            catchError(error => this.handleError(error)
            )) as Observable<Trend[]>
    }

    private handleError(error: any) {
		console.error(error);
		return throwError("Server error (" + error.status + "): " + error.text())
	}
}
