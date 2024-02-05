import { Component } from "@angular/core";
import { Router } from '@angular/router'; 
import { LoginService } from "src/app/services/login.service";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})

export class LoginComponent {


    constructor(private router: Router, private service: LoginService) { }

    login(event: any, username: string, password: string) {
        event.preventDefault();

        this.service.logIn(username, password).subscribe(
            (response) => {
                this.service.reqIsLogged();
                this.router.navigateByUrl('/home');
            },
            (error) => alert("Wrong credentials")
        );
    }


}