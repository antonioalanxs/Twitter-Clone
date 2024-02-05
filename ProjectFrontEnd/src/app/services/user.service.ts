import { HttpClient, HttpHandler, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { throwError, Observable, catchError } from 'rxjs';
import { User, UserInformation } from '../entities/user/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUser (username: string): Observable<UserInformation> {
    let url = "/api/users/" + username
    return this.http.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<UserInformation>;
  };

  getCurrentUser (): Observable<UserInformation> {
    let url = "/api/users/me"
    return this.http.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<UserInformation>;
  };

  getFollowedUser(username1: string, username2: string): Observable<boolean> {
    let url = "/api/users/" + username1 + '/followed/' + username2
    return this.http.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<boolean>;
  };

  getFollowed(username: string): Observable<UserInformation[]> {
    let url = '/api/users/' + username + '/followed?from=0&size=10';
    return this.http.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<UserInformation[]>;
  }

  getFollowers(username: string): Observable<UserInformation[]> {
    let url = '/api/users/' + username + '/followers?from=0&size=10';
    return this.http.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<UserInformation[]>;
  }

  putBannerPic(file: any, id: number) {
    let url = '/api/users/' + id + '/banner-image';
    let formData = new FormData();
    formData.append("file", file, file.name);
    let headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');

    return this.http.put(url, formData, { headers: headers}).pipe(
      catchError(error => this.handleError(error))
    ) as any;
  }

  putProfilePic(file: any, id: number) {
    let url = '/api/users/' + id + '/user-image';
    let formData = new FormData();
    formData.append("file", file, file.name);
    let headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');

    return this.http.put(url, formData, { headers: headers}).pipe(
      catchError(error => this.handleError(error))
    ) as any;
  }

  putNickname(nickname: string, id: number) {
    let url = '/api/users/' + id + '/nickname';
    return this.http.put(url, { text: nickname }).pipe(
      catchError(error => this.handleError(error))
    )
  }

  putBiography(biography: string, id: number) {
    let url = '/api/users/' + id + '/biography';
    return this.http.put(url, { text: biography }).pipe(
      catchError(error => this.handleError(error))
    )
  }

  getStatistics(): Observable<object> {
    let url = "/api/users/statistics";
    return this.http.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<object>;
  }

  getUsersToVerify(): Observable<UserInformation[]> {
    let url = "/api/users-to-verify";
    return this.http.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<UserInformation[]>;
  }

  getVerificatedUsers(): Observable<UserInformation[]> {
    let url = "/api/verificated-users";
    return this.http.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<UserInformation[]>;
  }

  getBannedUsers(): Observable<UserInformation[]> {
    let url = "/api/banned-users";
    return this.http.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<UserInformation[]>;
  }

  verifyUser(id: number) {
    let url = '/api/users/' + id + '?type=VERIFY';
    return this.http.put(url, null).pipe(
      catchError(error => this.handleError(error))
    )
  }

  unverifyUser(id: number) {
    let url = '/api/users/' + id + '?type=UNVERIFY';
    return this.http.put(url, null).pipe(
      catchError(error => this.handleError(error))
    )
  }

  banUser(id: number) {
    let url = '/api/users/' + id + '?type=BAN';
    return this.http.put(url, null).pipe(
      catchError(error => this.handleError(error))
    )
  }

  unbannedUser(id: number) {
    let url = '/api/users/' + id + '?type=UNBAN';
    return this.http.put(url, null).pipe(
      catchError(error => this.handleError(error))
    )
  }

  toggleFollow(id: number) {
    let url = '/api/users/' + id + '/followers';
    return this.http.put(url, null).pipe(
      catchError(error => this.handleError(error))
    )
  }

  getRecommendedUsers(): Observable<UserInformation[]> {
    let url = "/api/recommended-users";
    return this.http.get(url).pipe(
      catchError(error => this.handleError(error))
    ) as Observable<UserInformation[]>;
  }

  private handleError(error: any) {
		console.error(error);
		return throwError("Server error (" + error.status + "): " + error.text())
	}

}
