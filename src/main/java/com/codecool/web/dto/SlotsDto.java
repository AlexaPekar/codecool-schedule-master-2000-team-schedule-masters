package com.codecool.web.dto;

import com.codecool.web.model.Slot;

import java.util.List;

public final class SlotsDto {

    private final int index;
    private final List<Slot> slots;

    public SlotsDto(int index, List<Slot> slots) {
        this.index = index;
        this.slots = slots;
    }

    public int getIndex() {
        return index;
    }

    public List<Slot> getSlots() {
        return slots;
    }
}
