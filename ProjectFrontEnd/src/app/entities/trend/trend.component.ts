import { Component, Input, OnInit, Output, EventEmitter } from "@angular/core";
import { Router } from '@angular/router';


import { HashtagService } from "src/app/services/hashtag.service";
import { TweetService } from "src/app/services/tweet.service";
import { Trend } from "./trend.model";

@Component({
    selector: 'app-trend',
    templateUrl: './trend.component.html',
    styleUrls: ['./trend.component.css'],
  })

  export class TrendComponent{


    @Input()
    trend: Trend;
    @Input()
    index: number;
    @Output()
    showHashtag = new EventEmitter<String>();


    constructor( private service: HashtagService, private router:Router, private tweetService:TweetService) {}

    sendShowHashtag(){
      this.showHashtag.emit(this.trend.hashtag);
    }

  }
