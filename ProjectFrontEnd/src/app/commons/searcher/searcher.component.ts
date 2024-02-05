import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

/**
 *  HOW TO USE THIS COMPONENT:
 *
 * <app-searcher></app-searcher>
 */
@Component({
  selector: 'app-searcher',
  templateUrl: './searcher.component.html',
  styleUrls: ['./searcher.component.css']
})
export class SearcherComponent {
  
  constructor(private router:Router){}
  
  search(keyword:string){
    this.router.navigate(['/search/'+keyword]);
  }

}
