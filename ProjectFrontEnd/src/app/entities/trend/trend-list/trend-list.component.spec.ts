import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrendListComponent } from './trend-list.component';

describe('HashtagListComponent', () => {
  let component: TrendListComponent;
  let fixture: ComponentFixture<TrendListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TrendListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrendListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
