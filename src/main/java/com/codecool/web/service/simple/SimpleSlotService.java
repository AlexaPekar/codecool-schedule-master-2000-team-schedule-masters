package com.codecool.web.service.simple;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.SlotDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Slot;
import com.codecool.web.service.SlotService;

import java.sql.SQLException;
import java.util.List;

public class SimpleSlotService implements SlotService {
    private final ColumnDao columnDao;
    private final SlotDao slotDao;

    public SimpleSlotService(ColumnDao columnDao, SlotDao slotDao) {
        this.columnDao = columnDao;
        this.slotDao = slotDao;
    }

    @Override
    public Slot addNewSlot(int columnID, String timeRange) throws NotFoundException, SQLException, ServiceException {
        if(columnDao.findById(columnID)== null){
            throw new ServiceException("No column found");
        }
        if(timeRange.equals("")) {
            throw new ServiceException(new EmptyFieldException("Time range not defined"));
        }
        return slotDao.insertNewSlot(columnID,timeRange);
    }

    @Override
    public void removeSlot(int id) throws SQLException, ServiceException {
        if(slotDao.findSlotById(id)== null){
            throw new ServiceException("Slot not found");
        }
        slotDao.deleteSlot(id);
    }

    @Override
    public Slot getSlotByID(int id) throws SQLException, ServiceException {
        if(slotDao.findSlotById(id)== null){
            throw new ServiceException("Slot not found");
        }
        return slotDao.findSlotById(id);
    }

    @Override
    public List<Slot> getSlotsByColumnID(int columnID) throws NotFoundException, SQLException, ServiceException {
        if(columnDao.findById(columnID)== null){
            throw new ServiceException("Column not found");
        }
        return slotDao.findSlotsByColumnId(columnID);
    }

    @Override
    public void assignSlotIdToTaskId(int slotId, int taskId) throws SQLException {
        slotDao.joinSlotIdToTaskId(slotId, taskId);
    }
}
