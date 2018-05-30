package com.codecool.web.service.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Task;
import com.codecool.web.service.TaskService;

import java.sql.SQLException;
import java.util.List;

public class SimpleTaskService implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTaskService.class);

    private final TaskDao taskDao;

    public SimpleTaskService(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public Task addNewTask(int userId, String name, String content) throws ServiceException, SQLException {
        if (name.equals("")) {
            logger.error("Name field left empty");
            throw new ServiceException(new EmptyFieldException("Name field is empty"));
        }
        logger.info("New task added");
        return taskDao.insertNewTask(userId, name, content);
    }

    @Override
    public void removeTask(String id) throws ServiceException, NotFoundException, SQLException {
        try {
            if (taskDao.findTaskById(Integer.parseInt(id)) == null) {
                logger.error("No task found by ID");
                throw new ServiceException("Task not found with given id");
            }
            logger.info("Task removed");
            taskDao.deleteTask(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            logger.error("Parameter to 'deleteTask' method must be int");
            throw new ServiceException("Must be a number");
        } catch (IllegalArgumentException e) {
            logger.error("Parameter to 'deleteTask' method must be int");
            throw new ServiceException("Illegal argument, must be number");
        }
    }

    @Override
    public void editTaskName(String id, String name) throws NotFoundException, SQLException, ServiceException {
        try {
            if (taskDao.findTaskById(Integer.parseInt(id)) == null) {
                logger.error("No task by given ID");
                throw new ServiceException("Task not found with given id");
            }
            if (name.equals("")) {
                logger.error("Name field left empty");
                throw new ServiceException(new EmptyFieldException("Name is empty"));
            }
            logger.info("Task name edited");
            taskDao.updateTaskName(Integer.parseInt(id), name);
        } catch (NumberFormatException e) {
            logger.error("Parameter to 'updateTaskName' method must be int");
            throw new ServiceException("Must be a number");
        } catch (IllegalArgumentException e) {
            logger.error("Parameter to 'updateTaskName' method must be int");
            throw new ServiceException("Illegal argument, must be number");
        } catch (EmptyFieldException e) {
            logger.error("Name shouldn't be empty");
        }
    }

    @Override
    public void editTaskContent(String id, String content) throws NotFoundException, SQLException, ServiceException {
        try {
            if (taskDao.findTaskById(Integer.parseInt(id)) == null) {
                logger.error("No task by given ID");
                throw new ServiceException("Task not found with given id");
            }
            logger.info("Task content edited");
            taskDao.updateContent(Integer.parseInt(id), content);
        } catch (NumberFormatException e) {
            logger.error("Parameter to 'updateContent' method must be int");
            throw new ServiceException("Must be a number");
        } catch (IllegalArgumentException e) {
            logger.error("Parameter to 'updateContent' method must be int");
            throw new ServiceException("Illegal argument, must be number");
        } catch (EmptyFieldException e) {
            logger.error("Field shouldn't be empty");
        }
    }

    @Override
    public Task getTaskById(String id) throws NotFoundException, SQLException, ServiceException {
        try {
            if (taskDao.findTaskById(Integer.parseInt(id)) == null) {
                logger.error("No task by given ID");
                throw new ServiceException("Task not found with given id");
            }
            logger.info("Task returned by ID");
            return taskDao.findTaskById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            logger.error("Parameter to 'findTaskById' method must be int");
            throw new ServiceException("Must be a number");
        } catch (IllegalArgumentException e) {
            logger.error("Parameter to 'findTaskById' method must be int");
            throw new ServiceException("Illegal argument, must be number");
        }
    }

    @Override
    public Task getTaskById(int id) throws NotFoundException, SQLException, ServiceException {
            if (taskDao.findTaskById(id) == null) {
                logger.error("No task by given ID");
                throw new ServiceException("Task not found with given id");
            }
            logger.info("Task returned by ID");
            return taskDao.findTaskById(id);
    }

    @Override
    public List<Task> getAllTasksByUserId(int id) throws SQLException {
        logger.info("All tasks returned");
        return taskDao.findAllTaskByUserId(id);
    }

    public int getTaskIdBySlotId(String slotId) throws SQLException, ServiceException {
        try {
            logger.info("Task ID returned by Slot ID");
            return taskDao.findTaskIdBySlotId(Integer.parseInt(slotId));
        } catch (NumberFormatException e) {
            logger.error("Parameter to 'findTaskIdBySlotId' method must be int");
            throw new ServiceException("Must be a number");
        } catch (IllegalArgumentException e) {
            logger.error("Parameter to 'findTaskIdBySlotId' method must be int");
            throw new ServiceException("Illegal argument, must be number");
        }
    }

    @Override
    public void assignTaskIdToScheduleId(int taskId, int scheduleId) throws SQLException {
        logger.info("Task ID assigned to Schedule ID");
        taskDao.insertTaskIdToScheduleId(taskId, scheduleId);
    }
}
