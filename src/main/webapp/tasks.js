//Tasks page

function onLoadTasks(){
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTasksRecieved);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', '/schedule-masters/protected/tasks');
    xhr.send();
}

function onTasksRecieved(){
    showContents(['tasks-content']);

    const text = this.responseText;
    const tasks = JSON.parse(text);

    const tableEl = document.createElement('table');
    tableEl.appendChild(createTasksTableBody(tasks));

    const tasksEl = document.getElementById('tasks');
    tasksEl.appendChild(tableEl);
}

function createTasksTableBody(tasks) {
    const tableBodyEl = document.createElement('tbody');

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
    removeAllChildren(tasksContentDivEl);
    const link = "/schedule-masters/protected/task?id="+taskId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTaskLoad);
    xhr.open('GET',link);
    xhr.send();
}

//Task page

function onTaskLoad(){
    const text = this.responseText;
    const task = JSON.parse(text);
    tasksContentDivEl.textContent = task.id + " " + task.name;
}

function createTaskTableBody(task) {
    const tableHeadEl = document.createElement('thead');
    const tableBodyEl = document.createElement('tbody');

    const nameTdEl = document.createElement('td');
    nameTdEl.textContent = task.name;
    tableHeadEl.appendChild(nameTdEl);


}