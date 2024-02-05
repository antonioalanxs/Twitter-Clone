import { Component } from '@angular/core';


/**
 * How to use this component:
 * <app-notifications></app-notifications>
 */
@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent{

  tabSelected: string = "all";

  showPage(tabToShow: string, tabsToHide: string[]) {
    this.tabSelected = tabToShow.split("-")[0];
    let tabSelected = document.getElementById(tabToShow);
    tabSelected.classList.remove(
      "dark:text-gray-5",
      "font-semibold",
      "border-transparent"
    );
    tabSelected.classList.add(
      "font-bold",
      "dark:text-white-0",
      "text-black-0",
      "border-primary"
    );

    for (let tabToHide of tabsToHide) {
      let tabHidden = document.getElementById(tabToHide);
      tabHidden.classList.remove(
        "font-bold",
        "dark:text-white-0",
        "text-black-0",
        "border-primary"
      );
      tabHidden.classList.add(
        "dark:text-gray-5",
        "font-semibold",
        "border-transparent"
      );
    }
  }

}
