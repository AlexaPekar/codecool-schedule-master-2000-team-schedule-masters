package com.codecool.web.model;

public final class Task extends AbstractModel {

    private String name;
    private String content;

    public Task(int id, String name, String content) {

        super(id);
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
