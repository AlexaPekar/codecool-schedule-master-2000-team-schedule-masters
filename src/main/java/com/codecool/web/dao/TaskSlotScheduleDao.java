package com.codecool.web.dao;

import java.sql.SQLException;

public interface TaskSlotScheduleDao {

    void insertTaskToSlot(int slotId, int taskId, int scheduleId) throws SQLException;
}
