function onLoginResponse() {
    if (this.status === OK) {
        const user = JSON.parse(this.responseText);
        activeUser = user;
        setAuthorization(user);
        onProfileLoad(user);
        onLoadMenu();
        showContents(['menu', 'profile-content', 'logout-content']);
        onLoadSchedules();
    } else {
        onOtherResponse(loginContentDivEl, this);
    }
}

function onLoadMenu() {
    menuDivEl = document.getElementById('menu');
    const schedulesButtonEl = document.getElementById('schedules-button');
    schedulesButtonEl.style.cursor = "pointer";
    schedulesButtonEl.addEventListener('click', onLoadSchedules);

    const tasksButtonEl = document.getElementById('tasks-button');
    tasksButtonEl.style.cursor = "pointer";
    tasksButtonEl.addEventListener('click', onLoadTasks);

    const usersButtonEl = document.getElementById('users-button');
    usersButtonEl.style.cursor = "pointer";
    usersButtonEl.addEventListener('click', onLoadUsers);

    if (activeUser.role !== "Admin") {
        usersButtonEl.style.display = "none";
    } else {
        usersButtonEl.removeAttribute("style");
        usersButtonEl.style.cursor = "pointer";
    }
}

function onLoginButtonClicked() {
    const loginFormEl = document.forms['login-form'];

    const nameInputEl = loginFormEl.querySelector('input[name="name"]');
    const passwordInputEl = loginFormEl.querySelector('input[name="password"]');

    const name = nameInputEl.value;
    const password = passwordInputEl.value;

    const params = new URLSearchParams();
    params.append('name', name);
    params.append('password', password);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoginResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'login');
    xhr.send(params);

    document.getElementById("dimmer").remove();
    document.getElementById('login-lightbox').style.visibility = "hidden";
}

function onLoginLightBoxLoad() {
    const lightbox = document.getElementById("login-lightbox");
    const dimmer = document.createElement("div");
    dimmer.id = "dimmer";
    dimmer.style.width =  window.innerWidth + 'px';
    dimmer.style.height = window.innerHeight + 'px';
    dimmer.className = 'dimmer';

    dimmer.onclick = function(){
        document.body.removeChild(this);
        lightbox.style.visibility = 'hidden';
    }


    document.body.appendChild(dimmer);

    lightbox.style.visibility = 'visible';
    lightbox.style.top = window.innerHeight/2 - 50 + 'px';
    lightbox.style.left = window.innerWidth/2 - 100 + 'px';
}