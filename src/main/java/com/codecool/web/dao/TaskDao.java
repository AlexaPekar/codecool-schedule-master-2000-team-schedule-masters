package com.codecool.web.dao;

import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.model.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskDao {

    Task insertNewTask(int userId, String name, String content) throws SQLException;
    void deleteTask(int id) throws SQLException;
    void updateTaskName(int id, String name) throws SQLException;
    void updateContent(int id, String content) throws SQLException;
    Task findTaskById(int id) throws SQLException, NotFoundException;
    List<Task> findAllTaskByUserId(int id) throws SQLException;
    void insertTaskUniqueness(int taskId, String slotsIds, int columnId, int scheduleId) throws SQLException;
}
