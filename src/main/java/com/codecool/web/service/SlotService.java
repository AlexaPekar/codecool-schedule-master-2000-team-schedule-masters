package com.codecool.web.service;

import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Slot;

import java.sql.SQLException;
import java.util.List;

public interface SlotService {
    Slot addNewSlot(int columnID, String timeRange) throws NotFoundException, SQLException, ServiceException;
    void removeSlot(int id) throws SQLException, ServiceException;
    Slot getSlotByID(int id) throws SQLException, ServiceException;
    List<Slot> getSlotsByColumnID(String columnID) throws NotFoundException, SQLException, ServiceException;
    void assignSlotIdToTaskId(int slotId, int taskId) throws SQLException;
    List<Slot> getSlotsByTaskId(int taskId) throws SQLException;
    boolean checkSlotsConnectedByTaskId(int taskId) throws SQLException;
}