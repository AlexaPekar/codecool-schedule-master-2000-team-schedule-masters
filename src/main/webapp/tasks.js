//Tasks page

let tasksTableBodyEl;

function onLoadTasks() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTasksRecieved);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', '/schedule-masters/protected/tasks');
    xhr.send();
}

function onTasksRecieved() {
    showContents(['tasks-content', 'tasks', 'profile-content', 'menu', 'logout-content', 'create-task-button']);

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
    const link = "/schedule-masters/protected/tasks?id=" + taskId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoadTasks);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('DELETE', link);
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

        //creating delete cell
        const deleteTdEl = document.createElement('td');
        const xDeleteImageTdEl = document.createElement('img');
        xDeleteImageTdEl.setAttribute("src", "/schedule-masters/icons/delete-button.png");
        xDeleteImageTdEl.setAttribute("width", "30");
        xDeleteImageTdEl.setAttribute("height", "30");
        deleteTdEl.appendChild(xDeleteImageTdEl);

        xDeleteImageTdEl.style.cursor = "pointer";
        deleteTdEl.style.cursor = "pointer";
        xDeleteImageTdEl.dataset.taskId = task.id;
        xDeleteImageTdEl.addEventListener('click', onDeleteTaskClick);

        //creating row
        const trEl = document.createElement('tr');
        trEl.appendChild(idTdEl);
        trEl.appendChild(nameTdEl);
        trEl.appendChild(deleteTdEl);

        tasksTableBodyEl.appendChild(trEl);
    }

    return tasksTableBodyEl;
}

function onTaskClick() {
    const taskId = this.dataset.taskId;
    const link = "/schedule-masters/protected/task?id=" + taskId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTaskLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', link);
    xhr.send();
}

function onToScheduleFromTaskLoad() {
    const text = this.responseText;
    if (text !== '{"message":"No schedules with this id"}') {
        const schedules = JSON.parse(text);
        console.log(this.responseText);
        const scheduleId = schedule.id;

        currentSchedule = scheduleId;
        const scheduleDivEl = document.getElementById('schedule');
        removeAllChildren(scheduleDivEl);
        const link = "/schedule-masters/protected/schedule?id=" + scheduleId;
        const xhr = new XMLHttpRequest();
        xhr.addEventListener('load', onScheduleLoad);
        xhr.open('GET', link);
        xhr.send();
    } else {
        alert("This task isn't in any schedule!");
    }

}

//Task page
function onOpenScheduleButtonClick() {
    const schedulesDivEl = document.getElementById('schedules');
    removeAllChildren(schedulesDivEl);
    const taskId = this.dataset.taskId;
    const link = "/schedule-masters/protected/toScheduleFromTask?taskId=" + taskId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSchedulesReceived);
    xhr.open('GET', link);
    xhr.send();
}

function onOpenTasksButtonClick() {
    onLoadTasks();
}

function onTaskLoad() {
    showContents(['task-content', 'task', 'profile-content', 'menu', 'logout-content', 'open-tasks-button', 'open-schedule-button']);
    const text = this.responseText;
    const task = JSON.parse(text);
    const taskEl = document.getElementById('task');
    removeAllChildren(taskEl);

    const openTasksButtonEl = document.getElementById('open-tasks-button');

    const openScheduleButtonEl = document.getElementById('open-schedule-button');

    openScheduleButtonEl.dataset.taskId = task.id;

    openTasksButtonEl.addEventListener('click', onOpenTasksButtonClick);
    openScheduleButtonEl.addEventListener('click', onOpenScheduleButtonClick);

    taskEl.appendChild(createTaskTable(task));
}


function onModifyTaskClick() {
    const taskNameEl = document.getElementById('task-input-name');
    const taskContentEl = document.getElementById('task-input-content');

    const taskName = taskNameEl.textContent;
    const taskContent = taskContentEl.textContent;

    const taskId = taskNameEl.dataset.taskId;

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT', "/schedule-masters/protected/task?id=" + taskId + "&name=" + taskName + "&content=" + taskContent);
    xhr.send();

    alert("Modification saved!");
}

function taskKeyPressed(k) {
    if (k.code == 'Enter') {
        this.contentEditable = false;
        onModifyTaskClick();
    }
}

function createTaskTable(task) {
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
    nameTdEl.setAttribute('title', 'Click here to modify and press enter to save');

    tableHeadEl.appendChild(nameTitleTdEl);
    tableHeadEl.appendChild(nameTdEl);

    //modify name
    nameTdEl.setAttribute('contenteditable', true);
    nameTdEl.addEventListener("keypress", taskKeyPressed);

    const contentTitleStrongEl = document.createElement('strong');
    contentTitleStrongEl.textContent = "Description";
    const contentTitleTdEl = document.createElement('td');
    contentTitleTdEl.appendChild(contentTitleStrongEl);

    const contentTdEl = document.createElement('td');
    contentTdEl.textContent = task.content;
    nameTdEl.dataset.taskId = task.id;
    contentTdEl.setAttribute('id', 'task-input-content');
    contentTdEl.setAttribute('title', 'Click here to modify and press enter to save');

    //modify content
    contentTdEl.setAttribute('contenteditable', true);
    contentTdEl.addEventListener("keypress", taskKeyPressed);

    const trEl = document.createElement('tr');
    trEl.appendChild(contentTitleTdEl);
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

    alert("New task have added successfully!");
}

function onTaskAddResponse() {
    clearMessages();
    if (this.status === OK) {
        appendTask(JSON.parse(this.responseText));
    } else {
        onOtherResponse(tasksContentDivEl, this);
    }
}

function appendTask(task) {
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
    dimmer.style.width = window.innerWidth + 'px';
    dimmer.style.height = window.innerHeight + 'px';
    dimmer.className = 'dimmer';

    dimmer.onclick = function () {
        document.body.removeChild(this);
        lightbox.style.visibility = 'hidden';
    }


    document.body.appendChild(dimmer);

    lightbox.style.visibility = 'visible';
    lightbox.style.top = window.innerHeight / 2 - 50 + 'px';
    lightbox.style.left = window.innerWidth / 2 - 100 + 'px';
}

function getSlotsTask(slotId) {
    const link = "/schedule-masters/protected/slotfillup/" + slotId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSlotsTaskReceived);
    xhr.open('GET', link);
    xhr.send();
}

function onSlotsTaskReceived() {
    const text = this.responseText;
    if (this.status !== 404) {
        const taskDto = JSON.parse(text);

        const taskEl = document.getElementById('slot' + taskDto.slotId);

        const namePEl = document.createElement('p');
        namePEl.textContent = taskDto.task.name;
        namePEl.dataset.taskId = taskDto.task.id;
        namePEl.style.cursor = "pointer";
        namePEl.addEventListener('click', onTaskClick);

        taskEl.appendChild(namePEl);
    }
}

