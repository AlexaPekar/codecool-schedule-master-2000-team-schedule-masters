package com.codecool.web.dao;

import com.codecool.web.model.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskDao {

    Task insertNewTask(int userId, String name, String content) throws SQLException;
    void deleteTask(int id) throws SQLException;
    void updateTaskName(int id, String name) throws SQLException;
    void updateContent(int id, String content) throws SQLException;
    Task findTaskById(int id) throws SQLException;
    List<Task> findAllTaskByUserId(int id) throws SQLException;
}
