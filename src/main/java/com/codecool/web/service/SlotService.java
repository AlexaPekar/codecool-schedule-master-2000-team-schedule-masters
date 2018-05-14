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
    List<Slot> getSlotsByColumnID(int columnID) throws NotFoundException, SQLException, ServiceException;
}
