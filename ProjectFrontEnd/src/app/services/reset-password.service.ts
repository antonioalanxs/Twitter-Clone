import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { UserService } from "./user.service";
import { Observable, catchError, throwError } from "rxjs";

@Injectable({ providedIn: 'root' })
export default class ResetPasswordService {
    constructor(private http: HttpClient, private service: UserService) {}

    processForgotPassword(mail:string):Observable<Response>{
        let url = "/api/forgot-password"
        return this.http.post(url, {email: mail}, {observe: 'response'})
        .pipe(
            catchError(error => this.handleError(error)
          )) as Observable<Response>
    }

    processResetPassword(passwordToken: String, newPassword: String):Observable<Response>{
      let url = "/api/reset-password?passwordToken="+passwordToken;
      return this.http.put(url, {password:newPassword}, {observe: 'response'})
        .pipe(
          catchError(error => this.handleError(error))
        ) as Observable<Response>
    }

    private handleError(error: any) {
		console.error(error);
		return throwError("Server error (" + error.status + "): " + error.text())
    }
}
