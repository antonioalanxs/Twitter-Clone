import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ThemeSwitcherButtonComponent } from './theme-switcher-button.component';

describe('ThemeSwitcherButtonComponent', () => {
  let component: ThemeSwitcherButtonComponent;
  let fixture: ComponentFixture<ThemeSwitcherButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ThemeSwitcherButtonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ThemeSwitcherButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
