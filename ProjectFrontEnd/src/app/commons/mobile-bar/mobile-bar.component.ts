import { Component } from '@angular/core';

import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-mobile-bar',
  templateUrl: './mobile-bar.component.html',
  styleUrls: ['./mobile-bar.component.css'],
  providers: [UserService]
})
export class MobileBarComponent {
  public username: string;
  public urlToProfilePicture: string;

  public isLogged: boolean;
  public isCharged: boolean;

  constructor(private _userService: UserService) { }

  ngOnInit(): void {
    this._loadUserInformation();
  }

  private _loadUserInformation(): void {
    this._userService.getCurrentUser().subscribe(userInformation => {
      this.isLogged = userInformation !== null;
      this.urlToProfilePicture = "/api" + userInformation.urlToProfilePic;
      this.username = userInformation.user.username;
      this.isCharged = true;
    },
      error => {
        this.isLogged = false
        this.isCharged = true;
      });
  }
}
