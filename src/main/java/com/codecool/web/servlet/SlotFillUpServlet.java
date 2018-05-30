package com.codecool.web.servlet;

import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.database.DatabaseTaskDao;
import com.codecool.web.dto.TaskDto;
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

@WebServlet("/protected/slotfillup/*")
public class SlotFillUpServlet extends AbstractServlet{

    private final Logger logger = LoggerFactory.getLogger(SlotFillUpServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user =(User) req.getSession().getAttribute("user");
        try (Connection connection = getConnection(req.getServletContext())) {
            String url=req.getRequestURI();
            int i = url.lastIndexOf('/');
            String slotId = req.getRequestURI().substring(i + 1);

            TaskDao taskDao = new DatabaseTaskDao(connection);
            TaskService taskService = new SimpleTaskService(taskDao);

            int taskId = taskService.getTaskIdBySlotId(slotId);
            Task task = taskService.getTaskById(taskId);
            TaskDto taskDto = new TaskDto(Integer.parseInt(slotId),task);

            sendMessage(resp, HttpServletResponse.SC_OK, taskDto);
            logger.info("User with ID:{} opened a schedule and filled its slots with tasks",user.getId());

        } catch (SQLException e) {
            handleSqlError(resp, e);
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        } catch (NotFoundException e) {
            sendMessage(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        }
    }
}
