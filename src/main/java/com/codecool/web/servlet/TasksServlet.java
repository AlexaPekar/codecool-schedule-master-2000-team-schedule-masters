package com.codecool.web.servlet;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.database.DatabaseTaskDao;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Task;
import com.codecool.web.model.User;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.simple.SimpleTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/protected/tasks/*")
public class TasksServlet extends AbstractServlet {

    private final Logger logger = LoggerFactory.getLogger(TasksServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user =(User) req.getSession().getAttribute("user");
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao);

            int userId = user.getId();

            List<Task> tasks = taskService.getAllTasksByUserId(userId);

            sendMessage(resp, HttpServletResponse.SC_OK, tasks);
            logger.info("User with ID:{} requested tasks");
        } catch (SQLException e) {
            handleSqlError(resp, e);
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user =(User) req.getSession().getAttribute("user");
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao);

            int userId = user.getId();

            String name = req.getParameter("name");
            String content = req.getParameter("content");

            taskService.addNewTask(userId, name, content);
            logger.info("User with ID:{} added new task",userId);
        } catch (SQLException e) {
            handleSqlError(resp, e);
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_NO_CONTENT, e.getMessage());
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user =(User) req.getSession().getAttribute("user");
        try (Connection connection = getConnection(req.getServletContext())) {
            TaskDao taskDao = new DatabaseTaskDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao);

            String taskId = req.getParameter("id");

            taskService.removeTask(taskId);

            logger.info("User with ID:{} removed task with ID:{}",user.getId(),taskId);
        } catch (SQLException e) {
            handleSqlError(resp, e);
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        } catch (NotFoundException e) {
            sendMessage(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        }
    }
}
