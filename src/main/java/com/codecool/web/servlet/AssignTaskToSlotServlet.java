package com.codecool.web.servlet;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.SlotDao;
import com.codecool.web.dao.TaskDao;
import com.codecool.web.dao.database.DatabaseColumnDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseSlotDao;
import com.codecool.web.dao.database.DatabaseTaskDao;
import com.codecool.web.model.User;
import com.codecool.web.service.SlotService;
import com.codecool.web.service.TaskService;
import com.codecool.web.service.simple.SimpleSlotService;
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

@WebServlet("/protected/addtasktoslot")
public class AssignTaskToSlotServlet extends AbstractServlet{

    private final Logger logger = LoggerFactory.getLogger(AssignTaskToSlotServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user =(User) req.getSession().getAttribute("user");
        try (Connection connection = getConnection(req.getServletContext())){
            SlotDao slotDao = new DatabaseSlotDao(connection);
            TaskDao taskDao = new DatabaseTaskDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);

            TaskService taskService = new SimpleTaskService(taskDao);
            SlotService slotService = new SimpleSlotService(columnDao,slotDao);

            int scheduleId = Integer.parseInt(req.getParameter("scheduleId"));
            int slotId = Integer.parseInt(req.getParameter("slotId"));
            int taskId = Integer.parseInt(req.getParameter("taskId"));

            taskService.assignTaskIdToScheduleId(taskId,scheduleId);
            slotService.assignSlotIdToTaskId(slotId,taskId);

            logger.info("User with ID:{} assigned task with ID:{} to slot with ID:{}", user.getId(),taskId, slotId);
        } catch (SQLException e) {
            handleSqlError(resp, e);
            logger.error("{} for User with ID:{}",user.getId(),e.getMessage());
        }
    }
}
