//Tasks page

function onLoadUsers(){
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUsersRecieved);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', '/schedule-masters/protected/users');
    xhr.send();
}

function onUsersRecieved() {
    showContents(['users-content', 'users', 'profile-content', 'menu', 'logout-content']);

    const text = this.responseText;
    const users = JSON.parse(text);

    const tableEl = document.createElement('table');
    tableEl.setAttribute('class', 'datatable');

    const usersEl = document.getElementById('users');
    removeAllChildren(usersEl);

    tableEl.appendChild(createUsersTableBody(users));

    usersEl.appendChild(tableEl);
}

function createUsersTableBody(users) {
    const tableBodyEl = document.createElement('tbody');

    for (let i = 0; i < users.length; i++) {
        const user = users[i];

        //creating id cell
        const idTdEl = document.createElement('td');
        idTdEl.textContent = user.id + ".";
        idTdEl.setAttribute('id', 'idtdelements');

        //creating name cell
        const nameTdEl = document.createElement('td');
        nameTdEl.textContent = user.name;
        nameTdEl.dataset.userId = user.id;
        nameTdEl.style.cursor = "pointer";
        nameTdEl.addEventListener('click', onUserClick);

        //creating row
        const trEl = document.createElement('tr');
        trEl.appendChild(idTdEl);
        trEl.appendChild(nameTdEl);

        tableBodyEl.appendChild(trEl);
    }

    return tableBodyEl;
}

function onUserClick(){
    const userId = this.dataset.userId;
    const link = "/schedule-masters/protected/user?id="+userId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET',link);
    xhr.send();
}

//Task page

function onUsersBackButtonClick() {
    onLoadUsers();
}

function onUserLoad(){
    showContents(['user-content', 'user', 'profile-content', 'menu', 'logout-content', 'users-goback-button']);
    const text = this.responseText;
    const user = JSON.parse(text);
    const userEl = document.getElementById('user');
    removeAllChildren(userEl);

    const backButtonEl  = document.getElementById('users-goback-button');
    backButtonEl.addEventListener('click', onUsersBackButtonClick);

    const scheduleButtonEl = document.getElementById('user-schedules-button');
    scheduleButtonEl.dataset.userId = user.id;
    scheduleButtonEl.addEventListener('click', onUserSchedulesButtonClick);

    const taskButtonEl = document.getElementById('user-tasks-button');
    taskButtonEl.dataset.userId = user.id;
    taskButtonEl.addEventListener('click', onUserTasksButtonClick);

    userEl.appendChild(createUserTable(user));
}

function createUserTable(user) {
    const tableEl = document.createElement('table');
    tableEl.setAttribute('class', 'datatable');

    const tableHeadEl = document.createElement('thead');
    const tableBodyEl = document.createElement('tbody');

    const nameTdEl = document.createElement('td');
    nameTdEl.textContent = "Name";
    tableHeadEl.appendChild(nameTdEl);

    const passwordTdEl = document.createElement('td');
    passwordTdEl.textContent = "Password";
    tableHeadEl.appendChild(passwordTdEl);

    const roleTdEl = document.createElement('td');
    roleTdEl.textContent = "Role";
    tableHeadEl.appendChild(roleTdEl);

    const nameContentTdEl = document.createElement('td');
    nameContentTdEl.textContent = user.name;

    const passwordContentTdEl = document.createElement('td');
    passwordContentTdEl.textContent = user.password;

    const roleContentTdEl = document.createElement('td');
    roleContentTdEl.textContent = user.role;

    const trEl = document.createElement('tr');
    trEl.appendChild(nameContentTdEl);
    trEl.appendChild(passwordContentTdEl);
    trEl.appendChild(roleContentTdEl);

    tableBodyEl.appendChild(trEl);
    tableEl.appendChild(tableHeadEl);
    tableEl.appendChild(tableBodyEl);

    return tableEl;
}