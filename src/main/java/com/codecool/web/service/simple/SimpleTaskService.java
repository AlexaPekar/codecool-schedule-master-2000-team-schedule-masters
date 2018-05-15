package com.codecool.web.service.simple;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Task;
import com.codecool.web.service.TaskService;

import java.sql.SQLException;
import java.util.List;

public class SimpleTaskService implements TaskService{

    private final TaskDao taskDao;

    public SimpleTaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public Task addNewTask(int userId, String name, String content) throws ServiceException, SQLException {
        if (name.equals("") || content.equals("")) {
            throw new ServiceException(new EmptyFieldException("Name or content field empty"));
        }
        return taskDao.insertNewTask(userId,name,content);
    }

    @Override
    public void removeTask(int id) throws ServiceException, NotFoundException, SQLException {
        if (taskDao.findTaskById(id) == null) {
            throw new ServiceException("Task not found with given id");
        }
        taskDao.deleteTask(id);
    }

    @Override
    public void editTaskName(int id, String name) throws NotFoundException, SQLException, ServiceException {
        if (taskDao.findTaskById(id) == null) {
            throw new ServiceException("Task not found with given id");
        }
        if (name.equals("")) {
            throw new ServiceException(new EmptyFieldException("Name is empty"));
        }
        taskDao.updateTaskName(id,name);
    }

    @Override
    public void editTaskContent(int id, String content) throws NotFoundException, SQLException, ServiceException {
        if (taskDao.findTaskById(id) == null) {
            throw new ServiceException("Task not found with given id");
        }
        if (content.equals("")) {
            throw new ServiceException(new EmptyFieldException("Content field is empty"));
        }
        taskDao.updateContent(id,content);
    }

    @Override
    public Task getTaskById(int id) throws NotFoundException, SQLException, ServiceException {
        if (taskDao.findTaskById(id) == null) {
            throw new ServiceException("Task not found with given id");
        }
        return taskDao.findTaskById(id);
    }

    @Override
    public List<Task> getAllTasksByUserId(int id) throws SQLException {
        return taskDao.findAllTaskByUserId(id);
    }

    @Override
    public void assignTaskIdToScheduleId(int taskId, int scheduleId) throws SQLException {
        taskDao.insertTaskIdToScheduleId(taskId, scheduleId);
    }
}
