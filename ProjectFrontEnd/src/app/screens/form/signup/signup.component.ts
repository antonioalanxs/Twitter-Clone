import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from '@angular/router'; // To route the page when needed
import { Signup } from "src/app/services/signup.service";
import { UserService } from "src/app/services/user.service";

@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css']
  })

export class SignUpComponent{

    private res: number;

    constructor(private router:Router,activatedRoute: ActivatedRoute, private service: Signup) {}
       
        signUpUser(event: any, username:string, password: string, email:string){
            event.preventDefault();
            
            this.service.signUp(username,password, email).subscribe(
                response => {this.res = response.status; 
                    if (this.res == 200){
                        this.router.navigate(['/verification'])
                    }
                },
                error =>  this.router.navigate(['/error'])
            )
        }
    }
