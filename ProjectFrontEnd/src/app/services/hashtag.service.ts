
import { Injectable } from '@angular/core';
import { throwError, Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { TweetInformation } from '../entities/tweet/tweet.model';
import { Trend} from '../entities/trend/trend.model';

@Injectable({
    providedIn: 'root'
})
export class HashtagService{
    constructor(
        private http: HttpClient,
    ){}

    getSomeTrends(offset:number, size:number): Observable<Trend[]> {
        let url = "/api/trends?from="+ offset + "&size=" + size;
        return this.http.get(url).pipe(
          catchError(error => this.handleError(error)
        )) as Observable<Trend[]>
      }

    private handleError(error: any) {
		console.error(error);
		return throwError("Server error (" + error.status + "): " + error.text())
	}
}
