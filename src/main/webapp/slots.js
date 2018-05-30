function getColumnSlots(columnId){
    const link = "/schedule-masters/protected/slots/"+columnId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSlotsReceived);
    xhr.open('GET',link);
    xhr.send();
}

function onSlotsReceived() {
    const text = this.responseText;
    const slotsDto = JSON.parse(text);

    const tableEl = document.createElement('table');
    tableEl.appendChild(createSlotsTableBody(slotsDto.slots));

    const slEl = document.getElementById('column' + slotsDto.index);
    slEl.appendChild(tableEl);
}

function createSlotsTableBody(slots) {
    const tbodyEl = document.createElement('tbody');

    for (let i=0;i < slots.length; i++) {
        const trEl = document.createElement('tr');
        const slot = slots[i];

        const slotTimeRangeTdEl = document.createElement('td');
        slotTimeRangeTdEl.textContent = slot.timeRange;

        trEl.appendChild(slotTimeRangeTdEl);

        const slotContentTdEl = document.createElement('td');
        const divEl = document.createElement('div');
        divEl.id = 'slot' + slot.id;
        slotContentTdEl.addEventListener('click',onSlotClicked);
        slotContentTdEl.id = "slotcontent";

        getSlotsTask(slot.id);

        slotContentTdEl.appendChild(divEl);

        trEl.appendChild(slotContentTdEl);
        tbodyEl.appendChild(trEl);
    }
    return tbodyEl;
}
function onSlotClicked(){

    getTaskForUsers();
    const lightbox = document.getElementById("slot-lightbox");
    const slotForm = document.getElementById("slot-form");
    removeAllChildren(slotForm);
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
function getTaskForUsers(){
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTasksLoaded);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', '/schedule-masters/protected/tasks');
    xhr.send();
}

function onTasksLoaded(){
    const text = this.responseText;
    const tasks = JSON.parse(text);
    createTaskSelectionForElement(tasks);
}

function createTaskSelectionForElement(tasks){
    const el = document.getElementById('slot-form');
    const selectDivEl = document.createElement('div');
    const selectEl = document.createElement('select');
    for (let i=0;i < tasks.length; i++) {
        const task = tasks[i];
        const optionEl = document.createElement('option');
        optionEl.label = task.name;
        optionEl.value = task.id;
        selectEl.appendChild(optionEl);
    }
    selectDivEl.appendChild(selectEl);
    el.appendChild(selectDivEl);

}

function onSlotSaveClicked(){
    console.log("nincskészgec");
}