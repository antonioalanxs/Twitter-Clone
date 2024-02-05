import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { UserService } from "./user.service";
import { Observable, catchError, throwError } from "rxjs";

@Injectable({ providedIn: 'root' })
export class Signup {

    constructor(private http: HttpClient, private service: UserService) {}

    signUp(user:string, pass: string, mail:string): Observable<Response>{
        let url = "/api/signup"
        return this.http.post(url, {username: user, password: pass, email: mail}, {observe: 'response'})
        .pipe(
            catchError(error => this.handleError(error)
          )) as Observable<Response>
    }

    verify(code:string):Observable<Response>{
      let url = "/api/verification?code=" + code
      return this.http.get(url, {observe: 'response'}).pipe(
        catchError(error => this.handleError(error))
      )as Observable<Response>
    }

    private handleError(error: any) {
		console.error(error);
		return throwError("Server error (" + error.status + "): " + error.text())
    }
}
