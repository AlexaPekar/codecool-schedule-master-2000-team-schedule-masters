package com.codecool.web.dao;

import com.codecool.web.model.Slot;

import java.sql.SQLException;
import java.util.List;

public interface SlotDao {

    Slot insertNewSlot(int columnId, String timeRange) throws SQLException;
    void deleteSlot(int id) throws SQLException;
    Slot findSlotById(int id) throws SQLException;
    List<Slot> findSlotsByColumnId(int columnId) throws SQLException;
    void joinSlotIdToTaskId(int slotId, int taskId) throws SQLException;
    List<Integer> findSlotIdsByTaskId (int taskId) throws SQLException;
}
