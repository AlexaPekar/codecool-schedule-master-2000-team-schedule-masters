//Tasks page

function onLoadTasks(){
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTasksRecieved);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', '/schedule-masters/protected/tasks');
    xhr.send();
}

function onTasksRecieved(){
    showContents(['tasks-content', 'tasks', 'profile-content', 'menu', 'logout-content']);

    const text = this.responseText;
    const tasks = JSON.parse(text);

    const tableEl = document.createElement('table');

    const tasksEl = document.getElementById('tasks');
    removeAllChildren(tasksEl);

    tableEl.appendChild(createTasksTableBody(tasks));

    tasksEl.appendChild(tableEl);
}

function createTasksTableBody(tasks) {
    const tableBodyEl = document.createElement('tbody');

    for (let i = 0; i < tasks.length; i++) {
        const task = tasks[i];

        //creating id cell
        const idTdEl = document.createElement('td');
        idTdEl.textContent = task.id + ".";

        //creating name cell
        const nameTdEl = document.createElement('td');
        nameTdEl.textContent = task.name;
        nameTdEl.dataset.taskId = task.id;
        nameTdEl.style.cursor = "pointer";
        nameTdEl.addEventListener('click', onTaskClick);

        //creating row
        const trEl = document.createElement('tr');
        trEl.appendChild(idTdEl);
        trEl.appendChild(nameTdEl);

        tableBodyEl.appendChild(trEl);
    }

    return tableBodyEl;
}

function onTaskClick(){
    const taskId = this.dataset.taskId;
    const link = "/schedule-masters/protected/task?id="+taskId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTaskLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET',link);
    xhr.send();
}

//Task page

function getSlotsTask(slotId){
    const link = "/schedule-masters/protected/slotfillup/"+slotId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSlotsTaskReceived);
    xhr.open('GET',link);
    xhr.send();
}

function onSlotsTaskReceived() {
    const text = this.responseText;
    const taskDto = JSON.parse(text);

    const taskEl = document.getElementById('slot' + taskDto.slotId);

    const namePEl = document.createElement('p');
    namePEl.textContent = taskDto.task.name;
    namePEl.dataset.taskId = taskDto.task.id;
    namePEl.style.cursor = "pointer";
    namePEl.addEventListener('click', onTaskClick);

    taskEl.appendChild(namePEl);
}

function onBackButtonClick() {
    showContents(['tasks-content', 'tasks', 'profile-content', 'menu', 'logout-content']);
}

function onTaskLoad(){
    showContents(['task-content', 'task', 'profile-content', 'menu', 'logout-content', 'tasks-goback-button']);
    const text = this.responseText;
    const task = JSON.parse(text);
    const taskEl = document.getElementById('task');
    removeAllChildren(taskEl);

    const backButtonEl  = document.getElementById('tasks-goback-button');

    backButtonEl.addEventListener('click', onBackButtonClick);

    taskEl.appendChild(createTaskTable(task));
}

function createTaskTable(task) {
    const tableEl = document.createElement('table');
    const tableHeadEl = document.createElement('thead');
    const tableBodyEl = document.createElement('tbody');

    const nameTdEl = document.createElement('td');
    nameTdEl.textContent = task.name;
    tableHeadEl.appendChild(nameTdEl);

    const contentTdEl = document.createElement('td');
    contentTdEl.textContent = task.content;

    const trEl = document.createElement('tr');
    trEl.appendChild(contentTdEl);

    tableBodyEl.appendChild(trEl);
    tableEl.appendChild(tableHeadEl);
    tableEl.appendChild(tableBodyEl);

    return tableEl;
}