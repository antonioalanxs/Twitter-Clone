/**
 * Notification types and its texts
 */

notifsText = {
    "LIKE": "liked your Tweet",
    "RETWEET": "Retweeted your tweet",
    "FOLLOW": "followed you",
    "MENTION": "mentioned you in a Tweet"
}

/**
 * Load the notification text
 * @param notificationId
 * @param notificationType
 */
function loadNotificationText(notificationId, notificationType){
    let notificationH2 = document.getElementById(notificationId + "userH2");
    let textToLoad = notifsText[notificationType];
    notificationH2.innerHTML += textToLoad;
}

/**
 * Notification types and its SVGs
 */

notifsSVG = {
    "LIKE": `
    <svg viewBox="0 0 24 24" aria-hidden="true" class="w-8 fill-red-0">
                            <g>
                                <path
                                    d="M20.884 13.19c-1.351 2.48-4.001 5.12-8.379 7.67l-.503.3-.504-.3c-4.379-2.55-7.029-5.19-8.382-7.67-1.36-2.5-1.41-4.86-.514-6.67.887-1.79 2.647-2.91 4.601-3.01 1.651-.09 3.368.56 4.798 2.01 1.429-1.45 3.146-2.1 4.796-2.01 1.954.1 3.714 1.22 4.601 3.01.896 1.81.846 4.17-.514 6.67z">
                                </path>
                            </g>
                        </svg>
    `,
    "RETWEET": `
    <svg viewBox="0 0 24 24" aria-hidden="true" class="w-8 fill-green-0">
                            <g>
                                <path 
                                    d="M4.5 3.88l4.432 4.14-1.364 1.46L5.5 7.55V16c0 1.1.896 2 2 2H13v2H7.5c-2.209 0-4-1.79-4-4V7.55L1.432 9.48.068 8.02 4.5 3.88zM16.5 6H11V4h5.5c2.209 0 4 1.79 4 4v8.45l2.068-1.93 1.364 1.46-4.432 4.14-4.432-4.14 1.364-1.46 2.068 1.93V8c0-1.1-.896-2-2-2z">
                                </path>
                            </g>
                        </svg>
    `,
    "FOLLOW": `
    <svg viewBox="0 0 24 24" aria-hidden="true" class="w-8 fill-primary">
                            <g>
                                <path
                                    d="M17.863 13.44c1.477 1.58 2.366 3.8 2.632 6.46l.11 1.1H3.395l.11-1.1c.266-2.66 1.155-4.88 2.632-6.46C7.627 11.85 9.648 11 12 11s4.373.85 5.863 2.44zM12 2C9.791 2 8 3.79 8 6s1.791 4 4 4 4-1.79 4-4-1.791-4-4-4z">
                                </path>
                            </g>
                        </svg>
    `,
    "MENTION": `
    <svg viewBox="0 0 24 24" aria-hidden="true" class="w-8 fill-violet-0">
                            <g>
                                <path
                                    d="M22.99 11.295l-6.986-2.13-.877-.326-.325-.88L12.67.975c-.092-.303-.372-.51-.688-.51-.316 0-.596.207-.688.51l-2.392 7.84-1.774.657-6.148 1.82c-.306.092-.515.372-.515.69 0 .32.21.6.515.69l7.956 2.358 2.356 7.956c.09.306.37.515.69.515.32 0 .6-.21.69-.514l1.822-6.15.656-1.773 7.84-2.392c.303-.09.51-.37.51-.687 0-.316-.207-.596-.51-.688z">
                                </path>
                            </g>
                        </svg>
    `
}

/**
 * Load the svg associated to the notifications
 * @param notificationId
 * @param notificationType
 */
function loadSVG(notificationId, notificationType){
    let notificationFigure = document.getElementById(notificationId + "svg");
    let svgToLoad = notifsSVG[notificationType];
    notificationFigure.innerHTML = svgToLoad;
}

/**
 * Change the visual of the tab
 * @param tabToShow
 * @param tabToHide
 */
function changeVisualTab(tabToShow, tabToHide){
    let tabSelected = document.getElementById(tabToShow);
    tabSelected.classList.remove("dark:text-gray-5", "font-semibold", "border-transparent");
    tabSelected.classList.add("font-bold", "dark:text-white-0", "text-black-0", "border-primary");
    let tabNotSelected = document.getElementById(tabToHide);
    tabNotSelected.classList.remove("font-bold", "dark:text-white-0", "text-black-0", "border-primary");
    tabNotSelected.classList.add("dark:text-gray-5", "font-semibold", "border-transparent");
}