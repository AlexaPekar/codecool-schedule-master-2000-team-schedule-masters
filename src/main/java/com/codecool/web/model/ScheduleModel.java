package com.codecool.web.model;

public final class ScheduleModel extends AbstractModel{

    private final int id;
    private final int userId;
    private final String name;


    public ScheduleModel(int id, int id1, int userId, String name) {
        super(id);
        this.id = id1;
        this.userId = userId;
        this.name = name;
    }


    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
