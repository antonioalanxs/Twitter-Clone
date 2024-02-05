import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserInformation } from 'src/app/entities/user/user.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-followers',
  templateUrl: './followers.component.html',
  styleUrls: ['./followers.component.css']
})
export class FollowersComponent implements OnInit{

  users: UserInformation[];
  username: string;

  constructor(private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private router: Router) {
    const userId = activatedRoute.snapshot.params['id'];
    this.username = userId;
  }

  ngOnInit():void{
    this.activatedRoute.params.subscribe(params => {
      const typeList = params['users-list'];

      if (typeList === 'followers') {
        this.userService.getFollowers(this.username).subscribe(
          users => this.users = users,
          error => console.log(error)
        );
        this.changeVisualTab('followers-tab', 'following-tab');
        return;
      }

      if (typeList === 'followed') {
        this.userService.getFollowed(this.username).subscribe(
          users => this.users = users,
          error => console.log(error)
        );
        this.changeVisualTab('following-tab', 'followers-tab');
        return;
      }
    });
  }

  goToFollowed() {
    this.router.navigate(['profile/', this.username, 'followed']);
    this.changeVisualTab('following-tab', 'followers-tab');
  }

  goToFollowers() {
    this.router.navigate(['profile/', this.username, 'followers']);
    this.changeVisualTab('followers-tab', 'following-tab');
  }

  changeVisualTab(tabToShow: string, tabToHide: string){
    let tabSelected = document.getElementById(tabToShow);
    tabSelected.classList.remove("dark:text-gray-5", "font-semibold", "border-transparent");
    tabSelected.classList.add("font-bold", "dark:text-white-0", "text-black-0", "border-primary");
    let tabNotSelected = document.getElementById(tabToHide);
    tabNotSelected.classList.remove("font-bold", "dark:text-white-0", "text-black-0", "border-primary");
    tabNotSelected.classList.add("dark:text-gray-5", "font-semibold", "border-transparent");
}

}
