package com.codecool.web.servlet;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.SlotDao;
import com.codecool.web.dao.database.DatabaseColumnDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseSlotDao;
import com.codecool.web.dto.GuestColumnsDto;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Column;
import com.codecool.web.model.Schedule;
import com.codecool.web.service.ColumnService;
import com.codecool.web.service.ScheduleService;
import com.codecool.web.service.simple.SimpleColumnService;
import com.codecool.web.service.simple.SimpleScheduleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/schedule/columns")
public class GuestScheduleServlet extends AbstractServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())){
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao,columnDao,slotDao);
            ColumnService columnService = new SimpleColumnService(columnDao,scheduleDao);
            String scheduleIdString = req.getParameter("scheduleid");
            int scheduleId = scheduleService.decrypt(scheduleIdString);
            if(!scheduleService.isSchedulePublished(scheduleId)){
                sendMessage(resp,HttpServletResponse.SC_FORBIDDEN,"Schedule isn't public.");
                return;
            }
            List<Column> columns = columnService.getColumnsByScheduleId(scheduleId);
            Schedule schedule = scheduleService.getById(scheduleId);
            GuestColumnsDto guestColumnsDto = new GuestColumnsDto(schedule.getName(),columns);
            sendMessage(resp,HttpServletResponse.SC_OK,guestColumnsDto);

        } catch (SQLException e) {
           handleSqlError(resp,e);
        } catch (ServiceException e) {
            sendMessage(resp,HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
        }
    }
}
