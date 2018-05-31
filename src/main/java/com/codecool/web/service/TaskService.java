package com.codecool.web.service;

import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskService {

    Task addNewTask(int userId, String name, String content) throws ServiceException, SQLException;

    void removeTask(String id) throws ServiceException, NotFoundException, SQLException;

    void editTaskName(String id, String name) throws NotFoundException, SQLException, ServiceException;

    void editTaskContent(String id, String content) throws NotFoundException, SQLException, ServiceException;

    Task getTaskById(String id) throws NotFoundException, SQLException, ServiceException;

    List<Task> getAllTasksByUserId(int id) throws SQLException;

    void assignTaskIdToScheduleId(int taskId, int scheduleId) throws SQLException;

    int getTaskIdBySlotId(String slotId) throws SQLException, ServiceException;

    Task getTaskById(int id) throws NotFoundException, SQLException, ServiceException;

    List<Task> getAllTasksByUserId(String id) throws SQLException, ServiceException;
}

