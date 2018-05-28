package com.codecool.web.dto;

import com.codecool.web.model.Task;


public final class TaskDto {

    private final int slotId;
    private final Task task;

    public TaskDto(int slotId, Task task) {
        this.slotId = slotId;
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public int getSlotId() {
        return slotId;
    }
}
