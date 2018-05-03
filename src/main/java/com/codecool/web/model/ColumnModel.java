package com.codecool.web.model;

public final class ColumnModel extends AbstractModel {

    private int scheduleId;
    private String name;
    public ColumnModel(int id,int scheduleId,String name) {
        super(id);
        this.scheduleId = scheduleId;
        this.name = name;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public String getName() {
        return name;
    }
}
