import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  public show: boolean;

  constructor(private _router: Router){ }

  ngOnInit(): void {
      this.changePage();
  }

  public changePage(){
      this.show = !(this._router.url.includes("error") || this._router.url.includes("login") || this._router.url.includes("signup") || this._router.url === "/" || this._router.url.includes("verification") || this._router.url.includes("reset-password") || this._router.url.includes("forgot-password") || this._router.url.includes("write-tweet") || this._router.url.includes("edit-profile"))
  }
}
