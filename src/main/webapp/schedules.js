let currentSchedule;

function onLoadSchedules() {
    const schedulesDivEl = document.getElementById('schedules');
    removeAllChildren(schedulesDivEl);
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSchedulesReceived);
    xhr.open('GET','/schedule-masters/protected/schedules');
    xhr.send();
}
function onSchedulesReceived(){
    showContents(['schedules-content','profile-content','schedules','menu', 'logout-content', 'create-schedule-button']);
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


        const deleteTdEl = document.createElement('td');
        const xDeleteImageTdEl = document.createElement('img');
        xDeleteImageTdEl.setAttribute("src", "/schedule-masters/icons/delete-button.png");
        xDeleteImageTdEl.setAttribute("width", "30");
        xDeleteImageTdEl.setAttribute("height", "30");
        deleteTdEl.appendChild(xDeleteImageTdEl);

        xDeleteImageTdEl.style.cursor = "pointer";
        xDeleteImageTdEl.dataset.scheduleId = schedule.id;
        xDeleteImageTdEl.addEventListener("click",onScheduleDelete);

        const xUpdateImageTdEl = document.createElement('img');
        xUpdateImageTdEl.setAttribute("src", "/schedule-masters/icons/update-button.png");
        xUpdateImageTdEl.setAttribute("width", "36");
        xUpdateImageTdEl.setAttribute("height", "36");
        deleteTdEl.appendChild(xUpdateImageTdEl);

        xUpdateImageTdEl.style.cursor = "pointer";
        xUpdateImageTdEl.dataset.scheduleId = schedule.id;
        xUpdateImageTdEl.addEventListener("click",onScheduleUpdate);


        const xShareButtonEl = document.createElement('img');
        xShareButtonEl.setAttribute("src", "/schedule-masters/icons/share-button.png");
        xShareButtonEl.setAttribute("width", "30");
        xShareButtonEl.setAttribute("height", "30");
        deleteTdEl.appendChild(xShareButtonEl);

        xShareButtonEl.style.cursor = "pointer";
        xShareButtonEl.dataset.scheduleId = schedule.id;
        //xShareButtonEl.addEventListener("click",onScheduleShare);





        // creating row
        const trEl = document.createElement('tr');
        trEl.appendChild(idTdEl);
        trEl.appendChild(nameTdEl);
        trEl.appendChild(deleteTdEl);

        tbodyEl.appendChild(trEl);
    }

    return tbodyEl;
}

function onScheduleUpdate(){
    const scheduleId =this.dataset.scheduleId;
    const saveScheduleButton = document.getElementById("savebutton");
    saveScheduleButton.dataset.scheduleId = scheduleId;
    const scheduleContentDiv = document.getElementById("schedules-content");
    const lightbox = document.getElementById("scheduleupdate-lightbox");
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

function onScheduleDelete(){
    const scheduleId = this.dataset.scheduleId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoadSchedules);
    xhr.open('DELETE', '/schedule-masters/protected/schedule?id='+scheduleId);
    xhr.send();
}

function onScheduleLoad(){
    showContents(['schedule-content','schedule','schedules-content','profile-content','schedules','menu', 'logout-content', 'create-schedule-button']);
    const text = this.responseText;
    const schedule = JSON.parse(text);
    const scheduleTitle = document.getElementById('schedule-title');
    scheduleTitle.textContent = schedule.name;
    getScheduleColumns(schedule.id);
}

function onScheduleSaveClicked() {
    const scheduleName = document.getElementById('schedule-name').value;
    const saveScheduleButton = document.getElementById("savebutton");

    const scheduleId = saveScheduleButton.dataset.scheduleId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoadSchedules);
    xhr.open('PUT', '/schedule-masters/protected/schedule?id='+scheduleId + '&name=' + scheduleName);
    xhr.send();
    document.getElementById("dimmer").remove();
    document.getElementById("scheduleupdate-lightbox").style.visibility = "hidden";
}

function onScheduleClick(){
    const scheduleId = this.dataset.scheduleId;
    currentSchedule = scheduleId;
    const scheduleDivEl = document.getElementById('schedule');
    removeAllChildren(scheduleDivEl);
    const link = "/schedule-masters/protected/schedule?id="+scheduleId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onScheduleLoad);
    xhr.open('GET',link);
    xhr.send();
}

function onScheduleRefresh(){
    const scheduleId = currentSchedule;
    const scheduleDivEl = document.getElementById('schedule');
    removeAllChildren(scheduleDivEl);
    const link = "/schedule-masters/protected/schedule?id="+scheduleId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onScheduleLoad);
    xhr.open('GET',link);
    xhr.send();
}
function onScheduleLightBoxLoad(){

        const scheduleContentDiv = document.getElementById("schedules-content");
        const lightbox = document.getElementById("schedule-lightbox");
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
    xhr.addEventListener('load', onLoadSchedules);
    xhr.open('POST', '/schedule-masters/protected/schedule');
    xhr.send(params);

    document.getElementById("dimmer").remove();
    document.getElementById("schedule-lightbox").style.visibility = "hidden";

}