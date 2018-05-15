let schedulesContentDivEl;

function onLoad() {
    schedulesContentDivEl = document.getElementById('schedules-content');


    onLoadSchedules();
}
function onLoadSchedules() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSchedulesReceived);
    xhr.open('GET','/schedule-masters/protected/schedules');
    xhr.send();
}
function onSchedulesReceived(){
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
        nameTdEl.textContent = schedule.name;

        // creating row
        const trEl = document.createElement('tr');
        trEl.appendChild(idTdEl);
        trEl.appendChild(nameTdEl);

        tbodyEl.appendChild(trEl);
    }

    return tbodyEl;
}

document.addEventListener('DOMContentLoaded', onLoad);