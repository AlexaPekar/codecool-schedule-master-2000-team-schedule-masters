package com.codecool.web.dao;

import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.model.Column;

import java.sql.SQLException;
import java.util.List;

public interface ColumnDao {
    Column insert(int scheduleId, String name) throws SQLException;
    void delete(int id) throws SQLException;
    void updateName(int id, String name) throws SQLException;
    Column findById(int id) throws SQLException, NotFoundException;
    List<Column> findColumnsByScheduleID(int scheduleID) throws SQLException;
}
