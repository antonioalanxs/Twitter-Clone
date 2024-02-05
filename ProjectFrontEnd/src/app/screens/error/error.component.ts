import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { LoginService } from "src/app/services/login.service";

@Component({
    selector: 'app-error',
    templateUrl: './error.component.html',
    styleUrls: ['./error.component.css']
  })
export class ErrorComponent {

    constructor(private _router:Router, private _service: LoginService) {}
    refresh(): void {
        window.location.reload();
    }

    logOut(){
        this._service.logOut();
        this._router.navigateByUrl('/')
    }
}
