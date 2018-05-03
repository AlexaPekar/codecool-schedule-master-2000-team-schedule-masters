package com.codecool.web.dao;

import com.codecool.web.model.Column;

import java.util.List;

public interface ColumnDao {
    Column insert(String name);
    void delete(int id);
    Column updateName(int id, String name);
    Column findById(int id);
    List<Column> findColumnsByScheduleID(int scheduleID);
}
