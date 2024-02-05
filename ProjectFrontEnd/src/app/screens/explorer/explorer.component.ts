import { Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';

@Component({
    selector: 'app-explorer',
    templateUrl: './explorer.component.html',
    styleUrls: ['./explorer.component.css']
  })

export class ExplorerComponent implements OnInit{

    showTrends: boolean;
    hashtag: string;


    constructor(private router:Router) {}

    ngOnInit(): void {
        this.showTrends = true;
      }

    showTweets(hashtag){
      this.hashtag = hashtag;
      this.showTrends = false;
    }
}
