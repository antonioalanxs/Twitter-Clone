import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from '@angular/router';
import { TweetService } from "src/app/services/tweet.service";
import { UserInformation } from "src/app/entities/user/user.model";
import SearchService from "src/app/services/search.service";
import { Trend } from "src/app/entities/trend/trend.model";

@Component({
    selector: 'app-search',
    templateUrl: './search.component.html',
    styleUrls: ['./search.component.css']
  })

export class SearchComponent implements OnInit{

    trends: Trend[] = [];
    hashtag: string;
    userInformationList: UserInformation[] = [];
    keyword:string;
    showTweets:boolean = false;

    constructor(private router:Router,activatedRoute: ActivatedRoute, private service: SearchService, private tweetService:TweetService) {
        this.keyword = activatedRoute.snapshot.params['keyword'];
    }

    ngOnInit(): void {
        this.getAndLoadData(this.keyword);
    }

    getAndLoadData(keyword:string){
        this.service.searchProfiles(keyword).subscribe(
            profileList => this.userInformationList = profileList,
            error => this.userInformationList = []
        )
        this.service.searchHashtags(keyword).subscribe(
            hashtagList => this.trends = hashtagList,
            error => this.trends = []
        )
    }

    showHashtag(event){
        this.hashtag = event;
        this.showTweets = true;
    }

}
