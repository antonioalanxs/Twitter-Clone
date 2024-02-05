function changeButton(buttonId) {
    let button = document.getElementById(buttonId + "action-button");

    if (button.classList.contains("bg-primary")) {
        button.classList.replace("bg-primary", "bg-red-3");
        button.textContent = "Unfollow";
        return;
    }

    if (button.classList.contains("bg-red-3")) {
        button.classList.replace("bg-red-3", "bg-primary");
        button.textContent = "Follow";
    }
}
