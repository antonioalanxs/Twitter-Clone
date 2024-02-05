import { Component } from '@angular/core';

import { Router } from '@angular/router';

import { UserService } from 'src/app/services/user.service';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-left-bar',
  templateUrl: './left-bar.component.html',
  styleUrls: ['./left-bar.component.css'],
  providers: [UserService, LoginService]
})
export class LeftBarComponent {

  public nickname: string;
  public username: string;
  public urlToProfilePicture: string;
  public type: string;

  public svg: string;
  private _userTypeSVGMap = {
    "VERIFIED": "M22.25 12c0-1.43-.88-2.67-2.19-3.34.46-1.39.2-2.9-.81-3.91s-2.52-1.27-3.91-.81c-.66-1.31-1.91-2.19-3.34-2.19s-2.67.88-3.33 2.19c-1.4-.46-2.91-.2-3.92.81s-1.26 2.52-.8 3.91c-1.31.67-2.2 1.91-2.2 3.34s.89 2.67 2.2 3.34c-.46 1.39-.21 2.9.8 3.91s2.52 1.26 3.91.81c.67 1.31 1.91 2.19 3.34 2.19s2.68-.88 3.34-2.19c1.39.45 2.9.2 3.91-.81s1.27-2.52.81-3.91c1.31-.67 2.19-1.91 2.19-3.34zm-11.71 4.2L6.8 12.46l1.41-1.42 2.26 2.26 4.8-5.23 1.47 1.36-6.2 6.77z",
    "BANNED": "M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zm0-384c13.3 0 24 10.7 24 24V264c0 13.3-10.7 24-24 24s-24-10.7-24-24V152c0-13.3 10.7-24 24-24zM224 352a32 32 0 1 1 64 0 32 32 0 1 1 -64 0z"
  };
  public viewBox: string;
  private _viewBoxMap = {
    "VERIFIED": "0 0 24 24",
    "BANNED": "0 0 512 512",
  }

  public isLogged: boolean;
  public navigation: string = '';

  finish: boolean = false;

  constructor(private _userService: UserService,
    private _loginService: LoginService,
    private router: Router) { }

  ngOnInit(): void {
    this._loadUserInformation();
  }

  private _loadUserInformation(): void {
    this._userService.getCurrentUser().subscribe(userInformation => {
      this.isLogged = userInformation !== null;
      this.finish = true;
      if (!this.isLogged) {
        return
      }
      this.navigation = '/home'
      this.urlToProfilePicture = "/api" + userInformation.urlToProfilePic
      this.nickname = userInformation.user.nickname;
      this.username = userInformation.user.username;
      this.type = userInformation.user.type;
      this._loadSVG();
    },
      error => this.finish = true);
  }

  private _loadSVG(): void {
    this.svg = this._userTypeSVGMap[this.type];
    this.viewBox = this._viewBoxMap[this.type];
  }

  public logOut(): void {
    this._loginService.logOut();
    this.router.navigateByUrl("/")
  }
}
