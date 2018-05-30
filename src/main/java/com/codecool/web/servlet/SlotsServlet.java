package com.codecool.web.servlet;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.SlotDao;
import com.codecool.web.dao.database.DatabaseColumnDao;
import com.codecool.web.dao.database.DatabaseSlotDao;
import com.codecool.web.dto.SlotsDto;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Slot;
import com.codecool.web.model.User;
import com.codecool.web.service.SlotService;
import com.codecool.web.service.simple.SimpleSlotService;
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

@WebServlet("/protected/slots/*")
public class SlotsServlet extends AbstractServlet {

    private final Logger logger = LoggerFactory.getLogger(SlotsServlet.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user =(User) req.getSession().getAttribute("user");
        try (Connection connection = getConnection(req.getServletContext())){
            String url=req.getRequestURI();
            int i = url.lastIndexOf('/');
            String columnId = req.getRequestURI().substring(i + 1);

            ColumnDao columnDao = new DatabaseColumnDao(connection);

            SlotDao slotDao = new DatabaseSlotDao(connection);
            SlotService slotService = new SimpleSlotService(columnDao, slotDao);

            List<Slot> slots = slotService.getSlotsByColumnID(columnId);
            SlotsDto slotsDto = new SlotsDto(Integer.parseInt(columnId),slots);
            sendMessage(resp, HttpServletResponse.SC_OK, slotsDto);
            logger.info("User with ID:{} requested slots for column with ID:{}",user.getId(),columnId);
        } catch (SQLException e) {
            handleSqlError(resp, e);
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        } catch (NotFoundException e) {
            sendMessage(resp,HttpServletResponse.SC_NOT_FOUND,e.getMessage());
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user =(User) req.getSession().getAttribute("user");
        try (Connection connection = getConnection(req.getServletContext())) {
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            SlotService slotService = new SimpleSlotService(columnDao, slotDao);

            String newColumnId = req.getParameter("newColumnId");
            String newTimeRange = req.getParameter("newTimeRange");

            slotService.addNewSlot(newColumnId, newTimeRange);
            logger.info("User with ID:{} added slots",user.getId());
        } catch (SQLException e) {
            handleSqlError(resp, e);
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_NO_CONTENT, e.getMessage());
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        } catch (NotFoundException e) {
            sendMessage(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user =(User) req.getSession().getAttribute("user");
        try (Connection connection = getConnection(req.getServletContext())) {
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            SlotService slotService = new SimpleSlotService(columnDao, slotDao);

            String slotId = req.getParameter("slotId");

            slotService.removeSlot(slotId);

            logger.info("User with ID:{} removed slot with ID:{}",user.getId(),slotId);
        } catch (SQLException e) {
            handleSqlError(resp, e);
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_NO_CONTENT, e.getMessage());
            logger.error("{} for user ID: {}",e.getMessage(),user.getId());
        }
    }
}
