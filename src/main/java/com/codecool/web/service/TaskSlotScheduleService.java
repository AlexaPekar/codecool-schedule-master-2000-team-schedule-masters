package com.codecool.web.service;

import java.sql.SQLException;

public interface TaskSlotScheduleService {

    void assignTaskToSlotAndSchedule(int slotId, int taskId, int scheduleId) throws SQLException;
}
