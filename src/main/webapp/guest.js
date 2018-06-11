let guestSelectedScheduleId;
function onGuestPageLoad(){
    const url_string = window.location.href;
    //const id = url.substring(url.lastIndexOf('scheduleid=') + 1);
    const url = new URL(url_string);
    const id = url.searchParams.get("scheduleid");
    guestSelectedScheduleId = id;
    getColumnsForGuest();
}

function getColumnsForGuest(){
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onColumnsReceivedForGuest);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'schedule/columns?scheduleid='+guestSelectedScheduleId);
    xhr.send();
}

function onColumnsReceivedForGuest(){
    const text = this.responseText;
    const columns = JSON.parse(text);

    const tableEl = document.createElement('table');
    tableEl.appendChild(createColumnsTableBodyForGuest(columns));

    const scEl = document.getElementById('guest-schedule');
    scEl.appendChild(tableEl);

}

function createColumnsTableBodyForGuest(columns){
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
        divEl.id = 'columnGuest' + column.id;

        // creating id cell
        const colTdEl = document.createElement('td');
        colTdEl.appendChild(divEl);

        trEl2.appendChild(colTdEl);
        getColumnSlotsForGuest(column.id);
    }
    tbodyEl.appendChild(trEl1);
    tbodyEl.appendChild(trEl2);
    return tbodyEl;
}

function getColumnSlotsForGuest(columnId){
    const link = "/schedule-masters/column/slots?columnid="+columnId + "&scheduleid=" + guestSelectedScheduleId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onGuestSlotsReceived);
    xhr.open('GET',link);
    xhr.send();
}

function onGuestSlotsReceived() {
    const text = this.responseText;
    const slotsDto = JSON.parse(text);

    const tableEl = document.createElement('table');
    tableEl.appendChild(createSlotsTableBody(slotsDto.slots));

    const slEl = document.getElementById('columnGuest' + slotsDto.index);
    slEl.appendChild(tableEl);
}

addEventListener('DOMContentLoaded',onGuestPageLoad);