function getColumnSlots(columnId){
    const link = "/schedule-masters/protected/slots/"+columnId;
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSlotsReceived);
    xhr.open('GET',link);
    xhr.send();
}

function onSlotsReceived() {
    const text = this.responseText;
    const slots = JSON.parse(text);

    const tableEl = document.createElement('table');
    tableEl.appendChild(createSlotsTableBody(slots));

    const slEl = document.getElementById('column');
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
        slotContentTdEl.textContent = 'enter task';

        trEl.appendChild(slotContentTdEl);
        tbodyEl.appendChild(trEl);
    }
    return tbodyEl;
}