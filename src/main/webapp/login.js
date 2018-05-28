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
}