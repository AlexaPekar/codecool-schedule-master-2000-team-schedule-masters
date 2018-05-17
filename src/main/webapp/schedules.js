function onLoadSchedules() {
    const schedulesDivEl = document.getElementById('schedules');
    removeAllChildren(schedulesDivEl);
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSchedulesReceived);
    xhr.open('GET','/schedule-masters/protected/schedules');
    xhr.send();
}
function onSchedulesReceived(){
    showContents(['schedules-content','profile-content','schedules','menu', 'logout-content']);
    const text = this.responseText;
    const schedules = JSON.parse(text);

    const tableEl = document.createElement('table');
    tableEl.appendChild(createSchedulesTableBody(schedules));

    const scEl = document.getElementById('schedules');
    scEl.appendChild(tableEl);
}

function createSchedulesTableBody(schedules) {
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
        nameTdEl.addEventListener("click", onScheduleClick);

        // creating row
        const trEl = document.createElement('tr');
        trEl.appendChild(idTdEl);
        trEl.appendChild(nameTdEl);

        tbodyEl.appendChild(trEl);
    }

    return tbodyEl;
}

function onScheduleClick(){
    const scheduleId = this.dataset.scheduleId;
    const scheduleDivEl = document.getElementById('schedule');
    removeAllChildren(scheduleDivEl);
    const link = "/schedule-masters/protected/schedule?id="+scheduleId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onScheduleLoad);
    xhr.open('GET',link);
    xhr.send();
    //getScheduleColumns(scheduleId);
}
function onScheduleLoad(){
    showContents(['schedule-content','schedule','schedules-content','profile-content','schedules','menu', 'logout-content']);
    const text = this.responseText;
    const schedule = JSON.parse(text);
    const scheduleTitle = document.getElementById('schedule-title');
    scheduleTitle.textContent = schedule.name;
    getScheduleColumns(schedule.id);
}