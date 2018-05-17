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

        getSlotsTask(slot.id);

        slotContentTdEl.appendChild(divEl);

        trEl.appendChild(slotContentTdEl);
        tbodyEl.appendChild(trEl);
    }
    return tbodyEl;
}