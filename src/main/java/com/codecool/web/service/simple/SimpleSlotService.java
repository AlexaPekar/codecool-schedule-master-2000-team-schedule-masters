package com.codecool.web.service.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.SlotDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Slot;
import com.codecool.web.service.SlotService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleSlotService implements SlotService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleSlotService.class);

    private final ColumnDao columnDao;
    private final SlotDao slotDao;

    public SimpleSlotService(ColumnDao columnDao, SlotDao slotDao) {
        this.columnDao = columnDao;
        this.slotDao = slotDao;
    }

    @Override
    public Slot addNewSlot(String columnID, String timeRange) throws NotFoundException, SQLException, ServiceException {
        try {
            if (columnDao.findById(Integer.parseInt(columnID)) == null) {
                logger.error("No column found by ID");
                throw new ServiceException("No column found");
            }
            if (timeRange.equals("")) {
                logger.error("No time range defined");
                throw new ServiceException(new EmptyFieldException("Time range not defined"));
            }
            logger.info("New slot added");
            return slotDao.insertNewSlot(Integer.parseInt(columnID), timeRange);
        } catch (NumberFormatException e) {
            logger.error("Parameter to 'insertNewSlot' method must be int");
            throw new ServiceException("Must be a number");
        } catch (IllegalArgumentException e) {
            logger.error("Parameter to 'insertNewSlot' method must be int");
            throw new ServiceException("Illegal argument, must be number");
        }
    }

    @Override
    public void removeSlot(String id) throws SQLException, ServiceException {
        try {
            if (slotDao.findSlotById(Integer.parseInt(id)) == null) {
                logger.error("No slot found by ID");
                throw new ServiceException("Slot not found");
            }
            logger.info("Slot removed");
            slotDao.deleteSlot(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            logger.error("Parameter to 'deleteSlot' method must be int");
            throw new ServiceException("Must be a number");
        } catch (IllegalArgumentException e) {
            logger.error("Parameter to 'deleteSlot' method must be int");
            throw new ServiceException("Illegal argument, must be number");
        }
    }

    @Override
    public Slot getSlotByID(int id) throws SQLException, ServiceException {
        if (slotDao.findSlotById(id) == null) {
            logger.error("No slot found by ID");
            throw new ServiceException("Slot not found");
        }
        logger.info("Slot returned by ID");
        return slotDao.findSlotById(id);
    }

    @Override
    public List<Slot> getSlotsByColumnID(String columnID) throws NotFoundException, SQLException, ServiceException {
        try {
            if (columnDao.findById(Integer.parseInt(columnID)) == null) {
                logger.error("No column found by ID");
                throw new ServiceException("Column not found");
            }
            logger.info("Slots returned by Column ID");
            return slotDao.findSlotsByColumnId(Integer.parseInt(columnID));
        } catch (NumberFormatException e) {
            logger.error("Parameter to 'findSlotsByColumnId' method must be int");
            throw new ServiceException("Must be a number");
        } catch (IllegalArgumentException e) {
            logger.error("Parameter to 'findSlotsByColumnId' method must be int");
            throw new ServiceException("Illegal argument, must be number");
        }
    }

    @Override
    public void assignSlotIdToTaskId(int slotId, int taskId) throws SQLException {
        logger.info("Slot ID assigned to Task ID");
        slotDao.insertSlotIdToTaskId(slotId, taskId);
    }

    @Override
    public List<Slot> getSlotsByTaskId(int taskId) throws SQLException {
        List<Slot> slots = new ArrayList<>();
        List<Integer> slotIds = slotDao.findSlotIdsByTaskId(taskId);
        for (int i = 0; i < slotIds.size(); i++) {
            int slotId = slotIds.get(i);
            Slot tempSlot = slotDao.findSlotById(slotId);
            slots.add(tempSlot);
        }
        logger.info("Slots returned by Task ID");
        return slots;
    }

    @Override
    public boolean checkSlotsConnectedByTaskId(int taskId) throws SQLException {
        List<Integer> slotIds = slotDao.findSlotIdsByTaskId(taskId);
        for (int i = 0; i < slotIds.size(); i++) {
            if (i != (slotIds.size() - 1)) {
                int slotId = slotIds.get(i);
                int slotNextId = slotIds.get(i + 1);
                if (slotId + 1 == slotNextId) {
                    return true;
                }
            }
        }
        logger.info("Slots checked if connected by Task ID");
        return false;
    }
}
