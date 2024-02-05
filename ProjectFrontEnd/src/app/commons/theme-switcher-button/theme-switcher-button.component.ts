import { Component, OnInit } from '@angular/core';

/**
 *  HOW TO USE THIS COMPONENT:
 *
 * <app-theme-switcher-button></app-theme-switcher-button>
 */
@Component({
  selector: 'app-theme-switcher-button',
  templateUrl: './theme-switcher-button.component.html',
  styleUrls: ['./theme-switcher-button.component.css']
})
export class ThemeSwitcherButtonComponent implements OnInit{
  dark:boolean;
  loadThemePage() {
    // On page load or when changing themes, best to add inline in `head` to avoid FOUC
    // Change the icons inside the button based on previous settings
    this.dark = localStorage.getItem("color-theme") === "dark" ||
    (!("color-theme" in localStorage) &&
      window.matchMedia("(prefers-color-scheme: dark)").matches);
    if (this.dark) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  };

  changeThemePage() {
    // toggle icons inside button
    this.dark = !this.dark;

    // if set via local storage previously
    if (localStorage.getItem("color-theme")) {
      if (localStorage.getItem("color-theme") === "light") {
        document.documentElement.classList.add("dark");
        localStorage.setItem("color-theme", "dark");
      } else {
        document.documentElement.classList.remove("dark");
        localStorage.setItem("color-theme", "light");
      }

      // if NOT set via local storage previously
    } else {
      if (document.documentElement.classList.contains("dark")) {
        document.documentElement.classList.remove("dark");
        localStorage.setItem("color-theme", "light");
      } else {
        document.documentElement.classList.add("dark");
        localStorage.setItem("color-theme", "dark");
      }
    }
  }

  ngOnInit():void{
    this.loadThemePage();
  }
}


