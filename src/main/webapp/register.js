function onRegisterResponse() {
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
    schedulesButtonEl.addEventListener('click', onLoadSchedules);

    const tasksButtonEl = document.getElementById('tasks-button');
    tasksButtonEl.addEventListener('click', onLoadTasks)

    const usersButtonEl = document.getElementById('users-button');
    usersButtonEl.addEventListener('click', onLoadUsers);

    if (activeUser.role !== "Admin") {
        usersButtonEl.style.display = "none";
    } else {
        usersButtonEl.style.display = "block";
    }
}

function onRegisterButtonClicked() {
    const registerFormEl = document.forms['register-form'];

    const nameInputEl = registerFormEl.querySelector('input[name="name"]');
    const passwordInputEl = registerFormEl.querySelector('input[name="password"]');

    const name = nameInputEl.value;
    const password = passwordInputEl.value;

    const params = new URLSearchParams();
    params.append('name', name);
    params.append('password', password);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onRegisterResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'register');
    xhr.send(params);

    document.getElementById("dimmer").remove();
    document.getElementById('register-lightbox').style.visibility = "hidden";
}

function onRegisterLightBoxLoad() {
    const lightbox = document.getElementById("register-lightbox");
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