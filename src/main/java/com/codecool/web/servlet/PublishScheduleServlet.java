package com.codecool.web.servlet;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.SlotDao;
import com.codecool.web.dao.database.DatabaseColumnDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseSlotDao;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.simple.SimpleScheduleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/protected/schedule/publish")
public class PublishScheduleServlet extends AbstractServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao, columnDao, slotDao);
            int scheduleId = Integer.parseInt(req.getParameter("scheduleid"));
            if(scheduleService.isSchedulePublished(scheduleId)){
                scheduleService.unPublishSchedule(scheduleId);
                sendMessage(resp,HttpServletResponse.SC_OK,"Schedule unpublished");
            }
            else {
                scheduleService.publishSchedule(scheduleId);
                sendMessage(resp,HttpServletResponse.SC_OK,"Schedule published");

            }
        } catch (SQLException e) {
            sendMessage(resp,HttpServletResponse.SC_NOT_FOUND,e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao, columnDao, slotDao);
            int scheduleId = Integer.parseInt(req.getParameter("scheduleid"));

            sendMessage(resp,HttpServletResponse.SC_OK,scheduleService.isSchedulePublished(scheduleId));

        } catch (SQLException e) {
            sendMessage(resp,HttpServletResponse.SC_NOT_FOUND,e.getMessage());
        }
    }
}
