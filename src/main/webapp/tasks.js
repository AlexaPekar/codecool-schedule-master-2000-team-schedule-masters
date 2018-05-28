//Tasks page

let tasksTableBodyEl;

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

function onDeleteTaskClick() {
    const taskId = this.dataset.taskId;
    const link = "/schedule-masters/protected/tasks?id="+taskId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoadTasks);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('DELETE',link);
    xhr.send();
}

function createTasksTableBody(tasks) {
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
        nameTdEl.addEventListener('click', onTaskClick);

        //creating delete button
        const deleteButtonEl = document.createElement('button');
        deleteButtonEl.textContent = "Delete";
        console.log(task.id);
        deleteButtonEl.dataset.taskId = task.id;
        nameTdEl.style.cursor = "pointer";
        deleteButtonEl.addEventListener('click', onDeleteTaskClick);

        //creating row
        const trEl = document.createElement('tr');
        trEl.appendChild(idTdEl);
        trEl.appendChild(nameTdEl);
        trEl.appendChild(deleteButtonEl);

        tasksTableBodyEl.appendChild(trEl);
    }

    return tasksTableBodyEl;
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
    taskContentDivEl.appendChild(backButtonEl);

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

function onTaskAddClicked() {
    const taskFormEl = document.forms['task-form'];

    const taskNameInputEl = taskFormEl.querySelector('input[name="name"]');
    const taskContentInputEl = taskFormEl.querySelector('textarea[name="content"]');

    const taskName = taskNameInputEl.value;
    const taskContent = taskContentInputEl.value;

    const params = new URLSearchParams();
    params.append('name', taskName);
    params.append('content', taskContent);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('error', onNetworkError);
    xhr.addEventListener('load', onLoadTasks);
    xhr.open('POST', '/schedule-masters/protected/tasks');
    xhr.send(params);

    document.getElementById("dimmer").remove();
    document.getElementById('task-lightbox').style.visibility = "hidden";
}

function onTaskAddResponse(){
    clearMessages();
    if (this.status === OK) {
        appendTask(JSON.parse(this.responseText));
    } else {
        onOtherResponse(tasksContentDivEl, this);
    }
}

function appendTask(task){
    const idTdEl = document.createElement('td');
    idTdEl.textContent = task.id;

    const aEl = document.createElement('a');
    aEl.textContent = task.name;
    aEl.href = 'javascript:void(0);';
    aEl.dataset.taskId = task.id;
    aEl.addEventListener('click', onTaskClick);

    const nameTdEl = document.createElement('td');
    nameTdEl.appendChild(aEl);

    const trEl = document.createElement('tr');
    trEl.appendChild(idTdEl);
    trEl.appendChild(nameTdEl);
    tasksTableBodyEl.appendChild(trEl);
}

function onTaskLightBoxLoad() {
    const lightbox = document.getElementById("task-lightbox");
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

