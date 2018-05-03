package com.codecool.web.model;

public final class Schedule extends AbstractModel{

    private final String name;


    public Schedule(int id, String name) {
        super(id);
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
