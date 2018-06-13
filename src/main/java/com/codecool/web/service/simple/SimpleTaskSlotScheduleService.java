package com.codecool.web.service.simple;

import com.codecool.web.dao.TaskSlotScheduleDao;
import com.codecool.web.service.TaskSlotScheduleService;

import java.sql.SQLException;

public class SimpleTaskSlotScheduleService implements TaskSlotScheduleService {

    private final TaskSlotScheduleDao taskSlotScheduleDao;

    public SimpleTaskSlotScheduleService(TaskSlotScheduleDao taskSlotScheduleDao) {
        this.taskSlotScheduleDao = taskSlotScheduleDao;
    }

    @Override
    public void assignTaskToSlotAndSchedule(int slotId, int taskId, int scheduleId) throws SQLException {
        taskSlotScheduleDao.insertTaskToSlot(slotId, taskId, scheduleId);
    }
}
