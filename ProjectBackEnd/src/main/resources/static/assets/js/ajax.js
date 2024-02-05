const DECODER = new TextDecoder('iso-8859-1');
const NUMBER_ELEMENTS_PER_LOAD = 10;
let FIRST_LOAD = false;
let ACTUAL_HASHTAG;
let ACTUAL_PROFILE;

let counterPetitions = 0;

/**
 * Realize an HTTP petition to request more trends with AJAX
 * @returns {Promise<void>}
 */
async function loadMoreTrends() {
    addSpinner()
    const from = (counterPetitions + 1) * NUMBER_ELEMENTS_PER_LOAD

    const response = await fetch(`/explore/trends?from=${from}&size=${NUMBER_ELEMENTS_PER_LOAD}`);

    if (response.redirected) {
        hideButtons();
        removeSpinner();
        return;
    }
    addNewElements(response, "trend");
    removeSpinner()
}

/**
 * Realize an HTTP petition to request more notifications with AJAX
 * @returns {Promise<void>}
 */
async function loadMoreTweetsForBookmarks() {
    addSpinner()
    const from = (counterPetitions + 1) * NUMBER_ELEMENTS_PER_LOAD

    const response = await fetch(`/bookmarks/tweets?from=${from}&size=${NUMBER_ELEMENTS_PER_LOAD}`);

    if (response.redirected) {
        hideButtons();
        removeSpinner()
        return;
    }

    addNewElements(response, "tweet");
    removeSpinner()
}

/**
 * Realize an HTTP petition to request more tweets with AJAX at home page
 * @returns {Promise<void>}
 */
async function loadMoreTweetsForHome() {
    addSpinner()
    const from = (counterPetitions + 1) * NUMBER_ELEMENTS_PER_LOAD

    const response = await fetch(`/home/tweets?from=${from}&size=${NUMBER_ELEMENTS_PER_LOAD}`);

    if (response.redirected) {
        hideButtons();
        removeSpinner()
        return;
    }

    addNewElements(response, "tweet");
    removeSpinner()
}

/**
 * Realize an HTTP petition to request more tweets with AJAX at home page
 * @returns {Promise<void>}
 */
async function loadMoreTweetsForProfile(userId) {
    addSpinner()
    const from = (counterPetitions + 1) * NUMBER_ELEMENTS_PER_LOAD

    const response = await fetch(`/profile/tweets/${userId}?from=${from}&size=${NUMBER_ELEMENTS_PER_LOAD}`);

    if (response.redirected) {
        hideButtons();
        removeSpinner()
        return;
    }

    addNewElements(response, "tweet");
    removeSpinner()
}

/**
 * Reloads global variable FIRST_LOAD to show only first notifications
 * @returns {Promise<void>}
 */
async function reloadTabs(tab1, tab2){
    changeVisualTab(tab1, tab2);
    let tab2Button = document.getElementById(tab2);
    tab2Button.onclick = function() {reloadTabs(tab2, tab1)};
    let tab1Button = document.getElementById(tab1);
    tab1Button.onclick = ``;
    const container = document.getElementById("notification-container");
    container.innerHTML = "";
    FIRST_LOAD = true;
    if (tab1 === 'mentions-tab') {
        await showMentions();
    } else {
        await showNotifications()
    }
}

/**
 * Update the page to show the notifications associated
 * @returns {Promise<void>}
 */
async function showNotifications() {
    addSpinner();
    showButtons();
    let from = 0;
    if (FIRST_LOAD) {
        counterPetitions = 0;
        FIRST_LOAD = false;
        const load_more = document.getElementById("loadMore");
        load_more.onclick = showNotifications;
        const load_more_mobile = document.getElementById("loadMore-mobile");
        load_more_mobile.onclick = showNotifications;
    } else {
        from = (counterPetitions + 1) * NUMBER_ELEMENTS_PER_LOAD
    }

    const response = await fetch(`/notifications/notification?from=${from}&size=${NUMBER_ELEMENTS_PER_LOAD}`);

    if (response.redirected) {
        removeSpinner();
        hideButtons();
        return;
    }

    addNewElements(response, "notification");
    removeSpinner();
}

/**
 * Update the page to show the mentions associated
 * @returns {Promise<void>}
 */
async function showMentions() {
    addSpinner()
    let from = 0;
    if (FIRST_LOAD) {
        counterPetitions = 0;
        FIRST_LOAD = false;
        const load_more = document.getElementById("loadMore");
        load_more.onclick = showMentions;
        const load_more_mobile = document.getElementById("loadMore-mobile");
        load_more_mobile.onclick = showMentions;
    } else {
        from = (counterPetitions + 1) * NUMBER_ELEMENTS_PER_LOAD
    }

    const response = await fetch(`/mentions/mention?from=${from}&size=${NUMBER_ELEMENTS_PER_LOAD}`);

    if (response.redirected) {
        removeSpinner();
        hideButtons();
        return;
    }

    addNewElements(response, "notification");
    removeSpinner();
}

/**
 * Realize an HTTP petition to request more tweets associated with AJAX
 * @returns {Promise<void>}
 */
async function loadMoreTweetsAssociated() {
    addSpinner()
    const from = (counterPetitions + 1) * NUMBER_ELEMENTS_PER_LOAD

    const response = await fetch(`/explore_more/${ACTUAL_HASHTAG}?from=${from}&size=${NUMBER_ELEMENTS_PER_LOAD}`);

    if (response.redirected) {
        hideButtons();
        removeSpinner()
        return;
    }

    addNewElements(response, "trend");
    removeSpinner()
}

/**
 * Update the page to show the tweets associated to a hashtag
 * @returns {Promise<void>}
 */
async function showTweetsAssociated(hashtag) {
    counterPetitions = 0;
    const response = await fetch(`/explore_more/${hashtag}?from=${counterPetitions}&size=${NUMBER_ELEMENTS_PER_LOAD}`);
    const newTrends = DECODER.decode(await response.arrayBuffer());

    const container = document.getElementById("trend-container");
    container.innerHTML = newTrends;
    const load_more = document.getElementById("loadMore");
    load_more.classList.remove("hidden")
    load_more.onclick = loadMoreTweetsAssociated;
    const load_more_mobile = document.getElementById("loadMore-mobile");
    load_more_mobile.classList.remove("hidden")
    load_more_mobile.onclick = loadMoreTweetsAssociated;
    ACTUAL_HASHTAG = hashtag;
}

/**
 * Update the page to show the followers associated
 * @returns {Promise<void>}
 */
async function showFollowers(userId) {
    counterPetitions = 0;

    if (userId) {
        ACTUAL_PROFILE = userId;
    }
    const container = document.getElementById("follow-container");

    const response = await fetch(`/followers/${ACTUAL_PROFILE}/${counterPetitions}/${NUMBER_ELEMENTS_PER_LOAD}`);

    if (response.redirected) {
        container.innerHTML = "";
        return;
    }

    container.innerHTML = DECODER.decode(await response.arrayBuffer());
}

/**
 * Update the page to show the mentions associated
 * @returns {Promise<void>}
 */
async function showFollowed() {
    counterPetitions = 0;
    const container = document.getElementById("follow-container");

    const response = await fetch(`/followed/${ACTUAL_PROFILE}/${counterPetitions}/${NUMBER_ELEMENTS_PER_LOAD}`);

    if (response.redirected) {
        container.innerHTML = "";
        return;
    }

    container.innerHTML = DECODER.decode(await response.arrayBuffer());
}

/**
 * Add the new elements obtained in the correspondient container
 * @param {HTTP} response
 * @param {String} container_name
 */
async function addNewElements(response, container_name) {
    const newElements = DECODER.decode(await response.arrayBuffer());
    const container = document.getElementById(container_name + "-container");
    container.innerHTML += newElements;

    if (container_name === "trend") {
        for (i = ((counterPetitions + 1) * NUMBER_ELEMENTS_PER_LOAD); i < container.children.length; i++) {
            let childNode = container.children.item(i).childNodes.item(1);
            let trendNumber = (Number(childNode.innerHTML.charAt(0))) + ((counterPetitions + 1) * NUMBER_ELEMENTS_PER_LOAD);
            childNode.innerHTML = trendNumber + ' - Trending';
        }
    }
    counterPetitions++;
}

/**
 * Shows the load more buttons
 */
function showButtons(){
    show(document.getElementById("loadMore"));
    show(document.getElementById("loadMore-mobile"));
}

/**
 * Shows the current element
 * @param {HTMLElement} element
 */
function show(element){
    element.classList.remove('hidden');
    element.classList.add('llg:block');
}

/**
 * Hide the load more buttons when is not possible to load more elements
 */
function hideButtons() {
    hide(document.getElementById("loadMore"));
    hide(document.getElementById("loadMore-mobile"));
};

/**
 * Hides the current element
 * @param {HTMLElement} element
 */
function hide(element) {
    element.classList.add('hidden');
    element.classList.remove('llg:block');
}

/**
 * Add a spinner animation when the page is loading more elements
 */
function addSpinner() {
    document.getElementById("spinner").innerHTML = `<div class="flex items-center justify-center">
                <div class="inline-block h-8 w-8 animate-spin rounded-full border-4 border-primary border-r-transparent align-[-0.125em] motion-reduce:animate-[spin_1.5s_linear_infinite]" role="status">
                    <span class="!absolute !-m-px !h-px !w-px !overflow-hidden !whitespace-nowrap !border-0 !p-0 ![clip:rect(0,0,0,0)]">
                        Loading...
                    </span>
                </div>
            </div>`
}

/**
 * Remove the spinner animation when the page had load all the elements
 */
function removeSpinner() {
    document.getElementById("spinner").innerHTML = ``
}