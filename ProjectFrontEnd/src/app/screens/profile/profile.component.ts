import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserInformation } from 'src/app/entities/user/user.model';
import { LoginService } from 'src/app/services/login.service';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{

  public showChild: boolean = true;

  userId: number;
  userInformation: UserInformation;
  nickname: string;
  username: string;
  biography: string;
  joinDate: string;
  followedNumber: number;
  followersNumber: number;
  urlToProfilePic: string;
  urlToBannerPic: string;

  isLogged: boolean;
  isYourProfile: boolean;
  isAdmin: boolean;
  isNotBanned: boolean;
  isFollowed: boolean;

  svg:string;
  userTypeSVGMap = {
    "VERIFIED":"M22.25 12c0-1.43-.88-2.67-2.19-3.34.46-1.39.2-2.9-.81-3.91s-2.52-1.27-3.91-.81c-.66-1.31-1.91-2.19-3.34-2.19s-2.67.88-3.33 2.19c-1.4-.46-2.91-.2-3.92.81s-1.26 2.52-.8 3.91c-1.31.67-2.2 1.91-2.2 3.34s.89 2.67 2.2 3.34c-.46 1.39-.21 2.9.8 3.91s2.52 1.26 3.91.81c.67 1.31 1.91 2.19 3.34 2.19s2.68-.88 3.34-2.19c1.39.45 2.9.2 3.91-.81s1.27-2.52.81-3.91c1.31-.67 2.19-1.91 2.19-3.34zm-11.71 4.2L6.8 12.46l1.41-1.42 2.26 2.26 4.8-5.23 1.47 1.36-6.2 6.77z",
    "BANNED":"M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zm0-384c13.3 0 24 10.7 24 24V264c0 13.3-10.7 24-24 24s-24-10.7-24-24V152c0-13.3 10.7-24 24-24zM224 352a32 32 0 1 1 64 0 32 32 0 1 1 -64 0z"
  };

  viewBoxMap = {
    "VERIFIED": "0 0 24 24",
    "BANNED": "0 0 512 512",
  }

  viewBox:string;

  constructor(private userService: UserService,
    private loginService: LoginService,
    private activatedRoute: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      const id = params['id'];

      this.showChild = !this.showChild;
      this.userService.getUser(id).subscribe(
        user => {
          this.userInformation = user;
          this.userId = user.user.id;
          this.nickname = this.userInformation.user.nickname;
          this.username = this.userInformation.user.username;
          this.biography = this.userInformation.user.biography;
          this.joinDate = this.userInformation.user.joinDate;
          this.followedNumber = this.userInformation.followedNumber;
          this.followersNumber = this.userInformation.followersNumber;
          this.urlToProfilePic = "/api" + this.userInformation.urlToProfilePic;
          this.urlToBannerPic = "/api" + this.userInformation.urlToBannerPic;

          this.svg = this.userTypeSVGMap[this.userInformation.user.type]
          this.viewBox = this.viewBoxMap[this.userInformation.user.type]

          this.userService.getCurrentUser().subscribe(
            user => {
              this.isLogged = user != null;
              let currentUser = user;
      
              this.userService.getUser(this.username).subscribe(
                user => {
                  this.isYourProfile = currentUser.user.id === user.user.id;
                  this.isAdmin = currentUser.user.role === "ADMIN";
                  this.isNotBanned = user.user.enabled;
      
                  this.showChild = !this.showChild;
      
                  this.userService.getFollowedUser(currentUser.user.username, this.username).subscribe(
                    find => this.isFollowed = find,
                    error => console.log(error)
                  )
                },
                error => console.log(error)
              );
            }
          );
        }
      );
    });
  }

  banUser() {
    this.userService.banUser(this.userId).subscribe(
      error => console.log(error)
    )
    this.isNotBanned = false
  };

  toggleFollowUser() {
    this.userService.toggleFollow(this.userId).subscribe(
      error => console.log(error)
    );
    this.isFollowed = !this.isFollowed;

    if (this.isFollowed) {
      this.followersNumber += 1;
      return;
    }

    this.followersNumber -= 1;
  };

}
