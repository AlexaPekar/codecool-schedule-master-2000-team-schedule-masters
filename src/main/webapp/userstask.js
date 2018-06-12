function createUserTaskTable(task) {
    const tableEl = document.createElement('table');
    const tableHeadEl = document.createElement('thead');
    const tableBodyEl = document.createElement('tbody');

    const nameTitleStrongEl = document.createElement('strong');
    nameTitleStrongEl.textContent = "Task title";
    const nameTitleTdEl = document.createElement('td');
    nameTitleTdEl.appendChild(nameTitleStrongEl);

    const nameTdEl = document.createElement('td');
    nameTdEl.textContent = task.name;
    nameTdEl.dataset.taskId = task.id;
    nameTdEl.setAttribute('id', 'task-input-name');

    tableHeadEl.appendChild(nameTitleTdEl);
    tableHeadEl.appendChild(nameTdEl);

    const contentTitleStrongEl = document.createElement('strong');
    contentTitleStrongEl.textContent = "Description";
    const contentTitleTdEl = document.createElement('td');
    contentTitleTdEl.appendChild(contentTitleStrongEl);

    const contentTdEl = document.createElement('td');
    contentTdEl.textContent = task.content;
    nameTdEl.dataset.taskId = task.id;
    contentTdEl.setAttribute('id', 'task-input-content');

    const trEl = document.createElement('tr');
    trEl.appendChild(contentTitleTdEl);
    trEl.appendChild(contentTdEl);

    tableBodyEl.appendChild(trEl);
    tableEl.appendChild(tableHeadEl);
    tableEl.appendChild(tableBodyEl);

    return tableEl;
}

function onOpenUserScheduleButtonClick() {
    const schedulesDivEl = document.getElementById('schedules');
    removeAllChildren(schedulesDivEl);
    const taskId = this.dataset.taskId;
    const link = "/schedule-masters/protected/toScheduleFromTask?taskId=" + taskId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserScheduleButtonRecieved);
    xhr.open('GET', link);
    xhr.send();
}

function onGobackFromTaskButtonClick() {
    showContents(['tasks-content', 'tasks', 'profile-content', 'menu', 'logout-content']);
}

function onUserTaskLoad() {
    showContents(['task-content', 'task', 'profile-content', 'menu', 'logout-content', 'open-tasks-button', 'open-schedule-button']);
    const text = this.responseText;
    const task = JSON.parse(text);
    const taskEl = document.getElementById('task');
    removeAllChildren(taskEl);

    const openTasksButtonEl = document.getElementById('open-tasks-button');

    const openScheduleButtonEl = document.getElementById('open-schedule-button');

    openScheduleButtonEl.dataset.taskId = task.id;

    openTasksButtonEl.addEventListener('click', onGobackFromTaskButtonClick);
    openScheduleButtonEl.addEventListener('click', onOpenUserScheduleButtonClick);

    taskEl.appendChild(createUserTaskTable(task));
}

function onUserTaskClick() {
    const taskId = this.dataset.taskId;
    const link = "/schedule-masters/protected/task?id=" + taskId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserTaskLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', link);
    xhr.send();
}

function createUserTasksTableBody(tasks) {
    tasksTableBodyEl = document.createElement('tbody');

    for (let i = 0; i < tasks.length; i++) {
        const task = tasks[i];

        //creating id cell
        const idTdEl = document.createElement('td');
        idTdEl.textContent = task.id;

        //creating name cell
        const nameTdEl = document.createElement('td');
        nameTdEl.textContent = task.name;
        nameTdEl.dataset.taskId = task.id;
        nameTdEl.style.cursor = "pointer";
        nameTdEl.addEventListener('click', onUserTaskClick);

        //creating row
        const trEl = document.createElement('tr');
        trEl.appendChild(idTdEl);
        trEl.appendChild(nameTdEl);

        tasksTableBodyEl.appendChild(trEl);
    }

    return tasksTableBodyEl;
}

function onGobackFromTasksButtonClick() {
    showContents(['user-content','user','profile-content','menu', 'logout-content', 'users-goback-button']);
}

function onUserTasksButtonRecieved() {
    showContents(['tasks-content', 'tasks', 'profile-content', 'menu', 'logout-content']);

    const text = this.responseText;
    const tasks = JSON.parse(text);

    const tableEl = document.createElement('table');

    const tasksEl = document.getElementById('tasks');
    removeAllChildren(tasksEl);

    const gobackButtonEl = document.createElement('button');
    gobackButtonEl.textContent = "Go back";
    gobackButtonEl.addEventListener('click', onGobackFromTasksButtonClick);

    tableEl.appendChild(createUserTasksTableBody(tasks));

    tasksEl.appendChild(tableEl);
    tasksEl.appendChild(gobackButtonEl)
}

function onUserTasksButtonClick() {
    const tasksDivEl = document.getElementById('tasks');
    removeAllChildren(tasksDivEl);
    const id = this.dataset.userId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserTasksButtonRecieved);
    xhr.open('GET','/schedule-masters/protected/userTasks?userId=' + id);
    xhr.send();
}