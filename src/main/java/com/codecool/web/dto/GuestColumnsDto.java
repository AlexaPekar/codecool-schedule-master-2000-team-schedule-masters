package com.codecool.web.dto;

import com.codecool.web.model.Column;

import java.util.List;

public final class GuestColumnsDto {

    private final String scheduleName;

    private final List<Column> columns;

    public GuestColumnsDto(String scheduleName,List<Column> columns) {
        this.scheduleName = scheduleName;
        this.columns = columns;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public List<Column> getColumns() {
        return columns;
    }
}
