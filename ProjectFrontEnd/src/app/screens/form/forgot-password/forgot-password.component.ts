import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import ResetPasswordService from "src/app/services/reset-password.service";

@Component({
    selector: 'app-forgot-password',
    templateUrl: './forgot-password.component.html',
    styleUrls: ['./forgot-password.component.css']
  })
export class ForgotPassword {
    private res: number;

    constructor(private router:Router,activatedRoute: ActivatedRoute, private service: ResetPasswordService) {}
   
    forgotPasswordForm(event:any, email:string){
        event.preventDefault();
        this.service.processForgotPassword(email).subscribe(
            response => {
                this.res = response.status;
                if (this.res == 200){
                    this.router.navigate(['/forgot-password-confirmation'])
                }
            },
            error =>  this.router.navigate(['/error'])
        )
    }
}
