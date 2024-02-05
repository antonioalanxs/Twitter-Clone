import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import ResetPasswordService from "src/app/services/reset-password.service";

@Component({
    selector: 'app-reset-password',
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.css']
  })
export class ResetPassword {
    
    constructor(private router:Router,activatedRoute: ActivatedRoute, private service: ResetPasswordService) {
        this.passwordToken = activatedRoute.snapshot.params['passwordToken'];
    }
    private res: number;
    private passwordToken: string; 

    resetPasswordForm(event: any, newPassword:String){
        event.preventDefault();
        
        this.service.processResetPassword(this.passwordToken, newPassword).subscribe(
            response => {
                this.res = response.status;
                if (this.res == 200){
                    this.router.navigate(['/reset-password-confirmation'])
                }
            },
            error =>  this.router.navigate(['/error'])
        )
    }
}
