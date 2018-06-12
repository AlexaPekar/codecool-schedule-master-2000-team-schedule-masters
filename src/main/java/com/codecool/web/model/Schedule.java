package com.codecool.web.model;

public final class Schedule extends AbstractModel{

    private final String name;
    private final boolean isPublished;


    public Schedule(int id, String name,boolean isPublished) {
        super(id);
        this.name = name;
        this.isPublished = isPublished;
    }


    public String getName() {
        return name;
    }

    public boolean isPublished() {
        return isPublished;
    }
}
