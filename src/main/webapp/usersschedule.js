function onUserSlotsTaskReceived() {
    const text = this.responseText;
    if (this.status !== 404) {
        const taskDto = JSON.parse(text);

        const taskEl = document.getElementById('slot' + taskDto.slotId);

        const namePEl = document.createElement('p');
        namePEl.textContent = taskDto.task.name;
        namePEl.dataset.taskId = taskDto.task.id;
        namePEl.style.cursor = "pointer";
        namePEl.addEventListener('click', onUserTaskClick);

        taskEl.appendChild(namePEl);
    }
}

function getUserSlotsTask(slotId) {
    const link = "/schedule-masters/protected/slotfillup/" + slotId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserSlotsTaskReceived);
    xhr.open('GET', link);
    xhr.send();
}

function createUserSlotsTableBody(slots) {
    const tbodyEl = document.createElement('tbody');

    for (let i=0;i < slots.length; i++) {
        const trEl = document.createElement('tr');
        const slot = slots[i];

        const slotTimeRangeTdEl = document.createElement('td');
        slotTimeRangeTdEl.textContent = slot.timeRange;

        trEl.appendChild(slotTimeRangeTdEl);

        const slotContentTdEl = document.createElement('td');
        const divEl = document.createElement('div');
        divEl.id = 'slot'+slot.id;
        slotContentTdEl.id = "slotcontent";

        getUserSlotsTask(slot.id);

        slotContentTdEl.appendChild(divEl);

        trEl.appendChild(slotContentTdEl);
        tbodyEl.appendChild(trEl);
    }
    return tbodyEl;
}

function onUserSlotsReceived() {
    const text = this.responseText;
    const slotsDto = JSON.parse(text);

    const tableEl = document.createElement('table');
    tableEl.appendChild(createUserSlotsTableBody(slotsDto.slots));

    const slEl = document.getElementById('column' + slotsDto.index);
    slEl.appendChild(tableEl);
}

function getUserColumnSlots(columnId){
    const link = "/schedule-masters/protected/slots/"+columnId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserSlotsReceived);
    xhr.open('GET',link);
    xhr.send();
}

function createUserColumnsTableBody(columns) {
    const tbodyEl = document.createElement('tbody');
    const trEl1 = document.createElement('tr');

    for (let i=0;i < columns.length; i++) {
        const column = columns[i];

        const colNameTdEl = document.createElement('td');
        colNameTdEl.textContent = column.name;
        colNameTdEl.dataset.columnId = column.id;

        trEl1.appendChild(colNameTdEl);
    }

    const trEl2 = document.createElement('tr');

    for (let i = 0; i < columns.length; i++) {
        const column = columns[i];

        const divEl = document.createElement('div');
        //divEl.textContent = 'insert slot';
        divEl.id = 'column' + column.id;

        // creating id cell
        const colTdEl = document.createElement('td');
        colTdEl.appendChild(divEl);


        trEl2.appendChild(colTdEl);
        getUserColumnSlots(column.id);

    }

    tbodyEl.appendChild(trEl1);
    tbodyEl.appendChild(trEl2);

    return tbodyEl;
}

function onUserColumnsReceived() {
    const text = this.responseText;
    const columns = JSON.parse(text);

    const tableEl = document.createElement('table');
    tableEl.appendChild(createUserColumnsTableBody(columns));

    const scEl = document.getElementById('schedule');
    scEl.appendChild(tableEl);
}

function getUserScheduleColumns(scheduleId){
    const link = "/schedule-masters/protected/columns/"+scheduleId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserColumnsReceived);
    xhr.open('GET',link);
    xhr.send();
}

function onUserScheduleLoad(){
    showContents(['schedule-content','schedule','schedules-content','profile-content','schedules','menu', 'logout-content']);
    const text = this.responseText;
    const schedule = JSON.parse(text);
    const scheduleTitle = document.getElementById('schedule-title');
    scheduleTitle.textContent = schedule.name;
    getUserScheduleColumns(schedule.id);
}

function onUserScheduleClick(){
    const scheduleId = this.dataset.scheduleId;
    currentSchedule = scheduleId;
    const scheduleDivEl = document.getElementById('schedule');
    removeAllChildren(scheduleDivEl);
    const link = "/schedule-masters/protected/schedule?id="+scheduleId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserScheduleLoad);
    xhr.open('GET',link);
    xhr.send();
}

function createUserSchedulesTableBody(schedules){
    const tbodyEl = document.createElement('tbody');

    for (let i = 0; i < schedules.length; i++) {
        const schedule = schedules[i];

        // creating id cell
        const idTdEl = document.createElement('td');
        idTdEl.textContent = schedule.id;

        // creating name cell
        const nameTdEl = document.createElement('td');
        nameTdEl.dataset.scheduleId = schedule.id;
        nameTdEl.style.cursor = "pointer";
        nameTdEl.textContent = schedule.name;
        nameTdEl.addEventListener("click", onUserScheduleClick);

        // creating row
        const trEl = document.createElement('tr');
        trEl.appendChild(idTdEl);
        trEl.appendChild(nameTdEl);

        tbodyEl.appendChild(trEl);
    }

    return tbodyEl;
}

function onGobackFromScheduleButtonClick() {
    showContents(['user-content','user','profile-content','menu', 'logout-content', 'users-goback-button']);
}

function onUserScheduleButtonRecieved(){
    showContents(['schedules-content','profile-content','schedules','menu', 'logout-content']);
    const text = this.responseText;
    const schedules = JSON.parse(text);

    const tableEl = document.createElement('table');
    tableEl.appendChild(createUserSchedulesTableBody(schedules));

    const gobackButtonEl = document.createElement('button');
    gobackButtonEl.textContent = "Go back"
    gobackButtonEl.addEventListener('click', onGobackFromScheduleButtonClick);

    const scEl = document.getElementById('schedules');
    scEl.appendChild(tableEl);
    scEl.appendChild(gobackButtonEl);
}

function onUserSchedulesButtonClick(){
    const schedulesDivEl = document.getElementById('schedules');
    removeAllChildren(schedulesDivEl);
    const id = this.dataset.userId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserScheduleButtonRecieved);
    xhr.open('GET','/schedule-masters/protected/userSchedules?userId=' + id);
    xhr.send();
}