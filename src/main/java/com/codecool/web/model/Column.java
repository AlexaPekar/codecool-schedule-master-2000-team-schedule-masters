package com.codecool.web.model;

public final class Column extends AbstractModel {

    private String name;
    public Column(int id, int scheduleId, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
