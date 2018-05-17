

function getScheduleColumns(scheduleId){
    const link = "/schedule-masters/protected/columns/"+scheduleId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onColumnsReceived);
    xhr.open('GET',link);
    xhr.send();
}

function onColumnsReceived() {
    const text = this.responseText;
    const columns = JSON.parse(text);

    const tableEl = document.createElement('table');
    tableEl.appendChild(createColumnsTableBody(columns));

    const scEl = document.getElementById('schedule');
    scEl.appendChild(tableEl);
}

function createColumnsTableBody(columns) {
    const tbodyEl = document.createElement('tbody');
    const trEl1 = document.createElement('tr');

    for (let i=0;i < columns.length; i++) {
        const column = columns[i];

        const colNameTdEl = document.createElement('td');
        colNameTdEl.textContent = column.name;

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
        getColumnSlots(column.id);

    }

    tbodyEl.appendChild(trEl1);
    tbodyEl.appendChild(trEl2);

    return tbodyEl;
}