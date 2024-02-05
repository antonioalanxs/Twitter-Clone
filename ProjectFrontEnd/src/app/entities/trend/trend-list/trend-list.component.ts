import { Component, EventEmitter, Output } from '@angular/core';
import { Trend } from '../trend.model';
import { Router } from '@angular/router';
import { HashtagService } from 'src/app/services/hashtag.service';
import { TweetService } from 'src/app/services/tweet.service';

@Component({
  selector: 'app-trend-list',
  templateUrl: './trend-list.component.html',
  styleUrls: ['./trend-list.component.css']
})
export class TrendListComponent {

  trends:Trend[] = [];

  offset:number = 0;
  size:number = 10;

  canMore:boolean = true;

  @Output()
  selectedHashtag = new EventEmitter<String>();

  constructor(private router:Router, private service: HashtagService, private tweetService:TweetService) {}

  ngOnInit():void{
    this.loadMoreTrends();
  }

  showHashtag(event){
    this.selectedHashtag.emit(event);
  }

  loadMoreTrends() {
    this.addSpinner();
    this.getTrends();
    this.removeSpinner();
  }

  getTrends() {
    this.service.getSomeTrends(this.offset, this.size).subscribe(
      response => {
        if (response !== null) {
          response.forEach(trend=> this.trends.push(trend))
        } else {
          this.hideButtons()
        }
        this.removeSpinner()
      },
      error => console.log(error)
    );
    this.offset += 10;
  }

  addSpinner() {
    document.getElementById("spinner").innerHTML = `<div class="flex items-center justify-center sticky">
                <div class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-primary border-r-transparent align-[-0.125em] motion-reduce:animate-[spin_1.5s_linear_infinite]" role="status">
                    <span class="!absolute !-m-px !h-px !w-px !overflow-hidden !whitespace-nowrap !border-0 !p-0 ![clip:rect(0,0,0,0)]">
                        Loading...
                    </span>
                </div>
            </div>`
  }

  removeSpinner() {
    document.getElementById("spinner").innerHTML = ``
  }

  hideButtons() {
    this.canMore = false;
  }

}
