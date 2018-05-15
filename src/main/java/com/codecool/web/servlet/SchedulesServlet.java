package com.codecool.web.servlet;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.model.Schedule;
import com.codecool.web.model.User;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.simple.SimpleScheduleService;

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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())){
            String url=req.getRequestURI();
            int i = url.lastIndexOf('/');
            String urlID = req.getRequestURI().substring(i + 1);

            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao);
            User user = (User)req.getSession().getAttribute("user");

            int userID = user.getId();
            if(!user.getRole().equals("Admin")&&Integer.parseInt(urlID)!=userID){
                sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, "You don't have access to this Schedule");
                return;
            }
            List<Schedule> schedules = scheduleService.getSchedulesByUserId(userID);
            sendMessage(resp, HttpServletResponse.SC_OK, schedules);
        } catch (SQLException e) {
            handleSqlError(resp, e);e.printStackTrace();
        }
    }
}
