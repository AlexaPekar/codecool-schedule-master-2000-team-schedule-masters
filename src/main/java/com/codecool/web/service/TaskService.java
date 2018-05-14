package com.codecool.web.service;

import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskService {

    Task addNewTask(int userId,String name,String content) throws ServiceException, SQLException;
    void removeTask(int id) throws ServiceException, NotFoundException, SQLException;
    void editTaskName(int id,String name) throws NotFoundException, SQLException, ServiceException;
    void editTaskContent(int id,String content) throws NotFoundException, SQLException, ServiceException;
    Task getTaskById(int id) throws NotFoundException, SQLException, ServiceException;
    List<Task> getAllTasksByUserId(int id) throws SQLException;
    void storeTaskUniqueness(int taskId, String slotsIds, int columnId, int scheduleId);
    String createSlotsString(String[] slotsIds);
}

