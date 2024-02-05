import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowTweetComponent } from './show-tweet.component';

describe('ShowTweetComponent', () => {
  let component: ShowTweetComponent;
  let fixture: ComponentFixture<ShowTweetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowTweetComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowTweetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
