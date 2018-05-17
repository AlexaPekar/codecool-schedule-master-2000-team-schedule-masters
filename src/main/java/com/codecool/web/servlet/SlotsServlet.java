package com.codecool.web.servlet;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.SlotDao;
import com.codecool.web.dao.database.DatabaseColumnDao;
import com.codecool.web.dao.database.DatabaseSlotDao;
import com.codecool.web.dto.SlotsDto;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Slot;
import com.codecool.web.service.SlotService;
import com.codecool.web.service.simple.SimpleSlotService;


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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            SlotService slotService = new SimpleSlotService(columnDao, slotDao);

            String newColumnId = req.getParameter("newColumnId");
            String newTimeRange = req.getParameter("newTimeRange");

            slotService.addNewSlot(newColumnId, newTimeRange);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_NO_CONTENT, e.getMessage());
        } catch (NotFoundException e) {
            sendMessage(resp, HttpServletResponse.SC_NO_CONTENT, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())) {
            ColumnDao columnDao = new DatabaseColumnDao(connection);
            SlotDao slotDao = new DatabaseSlotDao(connection);
            SlotService slotService = new SimpleSlotService(columnDao, slotDao);

            String slotId = req.getParameter("slotId");

            slotService.removeSlot(slotId);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_NO_CONTENT, e.getMessage());
        }
    }
}
