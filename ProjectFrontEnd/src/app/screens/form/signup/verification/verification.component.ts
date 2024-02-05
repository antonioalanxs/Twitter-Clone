import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Signup } from "src/app/services/signup.service";

@Component({
  selector: 'app-verification',
  templateUrl: './verification.html',
  styleUrls: ['./verification.component.css']
})
export class VerificationComponent {
  constructor(private router: Router, activatedRoute: ActivatedRoute, private service: Signup) {
    this.code = activatedRoute.snapshot.params['code'];
  }
  private res: number;
  private code: string;

  verify() {
    this.service.verify(this.code).subscribe(
      response => {
        this.res = response.status;
        if (this.res == 200) {
          this.router.navigate(['/verify'])
        }
      },
      error => this.router.navigate(['/error'])
    )
  }
}
