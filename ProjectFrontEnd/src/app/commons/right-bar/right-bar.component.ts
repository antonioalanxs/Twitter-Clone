import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Trend } from 'src/app/entities/trend/trend.model';
import { UserInformation } from 'src/app/entities/user/user.model';
import { HashtagService } from 'src/app/services/hashtag.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-right-bar',
  templateUrl: './right-bar.component.html',
  styleUrls: ['./right-bar.component.css']
})
export class RightBarComponent implements OnInit {

  recommendedUsers: UserInformation[] = [];
  trends:Trend[] = [];

  offset:number = 0;
  size:number = 5;
  isExplorePage:boolean;
  isLogged:boolean;
  showRecommended:boolean;
  finish:boolean=false;

  constructor(private router:Router, private userService: UserService, private hashtagService:HashtagService) {}

  ngOnInit(): void {
    this.isExplorePage = this.router.url.includes("explore");
    this.showRecommended = this.isLogged && this.isExplorePage;
    this.userService.getCurrentUser().subscribe(
      response => this.isLogged = response !== null,
      error => this.isLogged = false
    );
    this.getTrends();
    this.getRecommendedUsers();
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.finish = true;
        this.isExplorePage = this.router.url.includes("explore");
        this.showRecommended = this.isLogged && this.isExplorePage;
      }
    })
  }
  getTrends() {
    this.hashtagService.getSomeTrends(this.offset, this.size).subscribe(
      response => {response.forEach(h => this.trends.push(h)); this.finish = true;},
      error => this.trends = []
    );
  }
  getRecommendedUsers() {
    this.userService.getRecommendedUsers().subscribe(
      users => { users.forEach(u => this.recommendedUsers.push(u)); this.finish = true; },
      error => this.isLogged = false
    );
  }

}
