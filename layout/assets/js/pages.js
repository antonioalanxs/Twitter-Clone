const SCREEN_TITLE = document.getElementById("screenTitle");
const SUBTITLE = document.getElementById("subTitle");
const NORMAL_SUBT_TEXT = "by ";
const CONFIG_BUTTON = document.getElementById("configButton");
const RETURN_BUTTON = document.getElementById("returnButton");
const NAV_BAR = document.getElementById("navBar");

let currentPage;
let previousPage;
let prevPageText;

/**
 * Shows on screen a page and hides the page which was being shown before.
 *
 * @param {String} tabToShow represents the tab selected, which contains its id and the id of the page to be shown.
 * @param {String} tabsToHide represents the collection of tabs to be hidden and its pages.
 */
function showPage(tabToShow, tabsToHide) {
  let tabSections = tabToShow.split("-");
  let pageSelected = document.getElementById(tabSections[0]);
  pageSelected.classList.remove("hidden");
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
    let tabToHideSections = tabToHide.split("-");
    let pageToHide = document.getElementById(tabToHideSections[0]);
    pageToHide.classList.add("hidden");
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

function changePage(pageToChange, nextPage) {
  let elementsToChangeDisplay = [
    pageToChange,
    nextPage,
    CONFIG_BUTTON,
    RETURN_BUTTON,
    NAV_BAR,
    SUBTITLE,
  ];

  elementsToChangeDisplay.forEach((element) => {
    element.classList.toggle("hidden");
  });
  currentPage = nextPage;
}

/**
 * @param {String} buttonPressedId codifies between "-": current page, type of button, notification's owner and next page's text.
 */
function showNextPage(buttonPressedId) {
  let pageIdSections = buttonPressedId.split("-");
  let pageToHide = document.getElementById(pageIdSections[0]);
  previousPage = pageToHide;
  prevPageText = document.getElementById("screenTitle").textContent;
  let nextPage = document.getElementById(pageIdSections[4]);
  changePage(pageToHide, nextPage);
  SCREEN_TITLE.textContent = pageIdSections[4];
  SUBTITLE.textContent += pageIdSections[2];
}

function returnToPrevPage() {
  changePage(currentPage, previousPage);
  SCREEN_TITLE.textContent = prevPageText;
  SUBTITLE.textContent = NORMAL_SUBT_TEXT;
}
