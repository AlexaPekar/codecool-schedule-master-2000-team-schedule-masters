package com.codecool.web.servlet;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.SlotDao;
import com.codecool.web.dao.database.DatabaseColumnDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseSlotDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.exceptions.ServiceException;
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

@WebServlet("/protected/schedule")
public class ScheduleServlet extends AbstractServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())){
            int id = Integer.parseInt(req.getParameter("id"));
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao,columnDao,slotDao);
            Schedule schedule = scheduleService.getById(id);
            sendMessage(resp, HttpServletResponse.SC_OK, schedule);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())){
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao,columnDao,slotDao);
            User user = (User)req.getSession().getAttribute("user");
            String userID = Integer.toString(user.getId());
            String name = req.getParameter("name");
            String amountOfColumns = req.getParameter("amountofcolumns");
            scheduleService.addSchedule(Integer.parseInt(userID),name, Integer.parseInt(amountOfColumns));
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (EmptyFieldException e) {
            sendMessage(resp, HttpServletResponse.SC_NO_CONTENT, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())){
            int id = Integer.parseInt(req.getParameter("id"));
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao,columnDao,slotDao);
            String name = req.getParameter("name");
            scheduleService.editName(id,name);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (EmptyFieldException e) {
            sendMessage(resp, HttpServletResponse.SC_NO_CONTENT, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            int id = Integer.parseInt(req.getParameter("id"));
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao,columnDao,slotDao);
            scheduleService.removeSchedule(id);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
