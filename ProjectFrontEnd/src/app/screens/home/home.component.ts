import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  
  isCharged: boolean;

  constructor(private userService: UserService){};
  
  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(
      response => this.isCharged = true
    );
  }
}
