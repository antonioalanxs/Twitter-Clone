import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RightBarComponent } from './right-bar.component';

describe('RightBarComponent', () => {
  let component: RightBarComponent;
  let fixture: ComponentFixture<RightBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RightBarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RightBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
