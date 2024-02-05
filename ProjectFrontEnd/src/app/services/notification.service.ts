import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { Notification } from "../entities/notification/notification.model";

@Injectable({
    providedIn: 'root'
})
export class NotificationService {
    constructor(
        private httpClient: HttpClient,
    ){}

    getSomeNotifications(from: number, size: number): Observable<Notification[]> {
        let url = "/api/notifications?from=" + from + "&size=" + size;
        return this.httpClient.get(url).pipe(
			catchError(error => this.handleError(error))
		) as Observable<Notification[]>;
    }

    getSomeMentions(from: number, size: number): Observable<Notification[]> {
        let url = "/api/mentions?from=" + from + "&size=" + size;
        return this.httpClient.get(url).pipe(
			catchError(error => this.handleError(error))
		) as Observable<Notification[]>;
    }

    private handleError(error: any) {
		console.error(error);
		return throwError("Server error (" + error.status + "): " + error.text())
	}
}