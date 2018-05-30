package com.codecool.web.servlet;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.SlotDao;
import com.codecool.web.dao.database.DatabaseColumnDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseSlotDao;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.simple.SimpleScheduleService;
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

@WebServlet("/protected/schedules/*")
public class SchedulesServlet extends AbstractServlet {

    private final Logger logger = LoggerFactory.getLogger(SchedulesServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("user");
        try (Connection connection = getConnection(req.getServletContext())){
            String url=req.getRequestURI();
            int i = url.lastIndexOf('/');
            String urlID = req.getRequestURI().substring(i + 1);

            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao,columnDao,slotDao);

            int userID = user.getId();

            List<Schedule> schedules = scheduleService.getSchedulesByUserId(userID);
            sendMessage(resp, HttpServletResponse.SC_OK, schedules);
            logger.info("User with ID: {} requested the schedule list",userID);
        } catch (SQLException e) {
            handleSqlError(resp, e);e.printStackTrace();
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        }
    }
}
