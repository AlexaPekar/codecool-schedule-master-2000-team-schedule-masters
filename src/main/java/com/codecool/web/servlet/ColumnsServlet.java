package com.codecool.web.servlet;


import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.database.DatabaseColumnDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
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
import java.util.List;

@WebServlet("/protected/columns/*")
public class ColumnsServlet extends AbstractServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())){
            String url=req.getRequestURI();
            int i = url.lastIndexOf('/');
            String urlID = req.getRequestURI().substring(i + 1);

            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);

            ColumnDao columnDao = new DatabaseColumnDao(connection);
            ColumnService columnService = new SimpleColumnService(columnDao,scheduleDao);

            int scheduleId = Integer.parseInt(urlID);

            List<Column> columns = columnService.getColumnsByScheduleId(scheduleId);
            sendMessage(resp, HttpServletResponse.SC_OK, columns);
        } catch (SQLException e) {
            handleSqlError(resp, e);e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
