import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserService } from './user.service';
import { UserInformation } from '../entities/user/user.model';


@Injectable({ providedIn: 'root' })
export class LoginService {

    logged: boolean = false;
    currentUser: UserInformation;

    getCurrentUser(){
        return this.currentUser;
    }

    constructor(private http: HttpClient, private service: UserService) {
        this.reqIsLogged();
    }

    reqIsLogged() {
        let url = '/api/users/me';
        this.http.get(url, { withCredentials: true }).subscribe(
            response => {
                this.currentUser = response as UserInformation;
                this.logged = true;
            },
            error => {
                if (error.status != 404) {
                    this.logged = false;
                }
            }
        );

    }

    logIn(user: string, pass: string) {
        let url = "/api/login"
        return this.http.post(url, { username: user, password: pass }, { withCredentials: true });
    }

    logOut() {
        let url = "/api/logout"
        return this.http.post(url, { withCredentials: true })
            .subscribe((resp: any) => {
                console.log("LOGOUT: Successfully");
                this.logged = false;
            });

    }

    isLogged() {
        return this.logged;
    }

    isAdmin() {
        return; //TODO fix roles Bug!!!
    }



}