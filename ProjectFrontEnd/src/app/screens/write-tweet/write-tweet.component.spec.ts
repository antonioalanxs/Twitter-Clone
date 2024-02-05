import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WriteTweetComponent } from './write-tweet.component';

describe('WriteTweetComponent', () => {
  let component: WriteTweetComponent;
  let fixture: ComponentFixture<WriteTweetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WriteTweetComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WriteTweetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
