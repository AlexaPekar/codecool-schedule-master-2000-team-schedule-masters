package com.codecool.web.model;

public final class Slot extends AbstractModel {

    private String timeRange;

    public Slot(int id, String timeRange) {
        super(id);
        this.timeRange = timeRange;
    }

    public String getTimeRange() {
        return timeRange;
    }
}
