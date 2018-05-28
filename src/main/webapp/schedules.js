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
function onScheduleLightBoxLoad(){

        const scheduleContentDiv = document.getElementById("schedules-content");
        const lightbox = document.getElementById("schedule-lightbox");
        const dimmer = document.createElement("div");
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
function onScheduleAddClicked(){
    const scheduleFormEl = document.forms['schedule-form'];

    const nameInputEl = scheduleFormEl.querySelector('input[name="name"]');
    const select = document.getElementById("amountofcolumns");
    const currOpt = select.options[select.selectedIndex].value;
    //const columnsNumberEl = scheduleFormEl.querySelector('option[name="amountofcolumns"]');

    const name = nameInputEl.value;
    const columns = currOpt;

    const params = new URLSearchParams();
    params.append('name', name);
    params.append('amountofcolumns', columns);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', '/schedule-masters/protected/schedule');
    xhr.send(params);
}