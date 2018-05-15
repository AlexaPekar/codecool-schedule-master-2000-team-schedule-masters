package com.codecool.web.servlet;


import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.database.DatabaseColumnDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Column;
import com.codecool.web.service.ColumnService;
import com.codecool.web.service.simple.SimpleColumnService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/protected/column")
public class ColumnServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())){
            int id = Integer.parseInt(req.getParameter("id"));
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnService columnService = new SimpleColumnService(columnDao,scheduleDao);
            Column column = columnService.getColumnById(id);
            sendMessage(resp, HttpServletResponse.SC_OK, column);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        try (Connection connection = getConnection(req.getServletContext())){
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            ColumnService columnService = new SimpleColumnService(columnDao,scheduleDao);
            int scheduleId = Integer.parseInt(req.getParameter("scheduleId"));
            String name = req.getParameter("name");
            columnService.addColumn(scheduleId,name);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())){
            int id = Integer.parseInt(req.getParameter("id"));
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            ColumnService columnService = new SimpleColumnService(columnDao,scheduleDao);
            String name = req.getParameter("name");
            columnService.editColumnName(id,name);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            int id = Integer.parseInt(req.getParameter("id"));
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            ColumnService columnService = new SimpleColumnService(columnDao,scheduleDao);
            columnService.removeColumn(id);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
}
