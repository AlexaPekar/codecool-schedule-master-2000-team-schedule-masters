package com.codecool.web.servlet;


import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.SlotDao;
import com.codecool.web.dao.database.DatabaseColumnDao;
import com.codecool.web.dao.database.DatabaseScheduleDao;
import com.codecool.web.dao.database.DatabaseSlotDao;
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

@WebServlet("/protected/encrypt")
public class EncryptScheduleLinkServlet extends AbstractServlet {
    private final Logger logger  = LoggerFactory.getLogger(ColumnServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())){
            int id = Integer.parseInt(req.getParameter("scheduleid"));
            ScheduleDao scheduleDao = new DatabaseScheduleDao(connection);
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            ScheduleService scheduleService = new SimpleScheduleService(scheduleDao,columnDao,slotDao);
            String encryptedId = scheduleService.encrypt(Long.valueOf(id));
            sendMessage(resp, HttpServletResponse.SC_OK, encryptedId);

            logger.info("Encrypted id created");
        } catch (SQLException e) {
            sendMessage(resp,HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
        }

    }
}
