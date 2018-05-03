package com.codecool.web.dao;

import com.codecool.web.model.Schedule;

import java.sql.SQLException;
import java.util.List;

public interface ScheduleDao {

    Schedule findById(int id) throws SQLException;
    Schedule insertSchedule(int userId,String name);
    Schedule updateName(int id,String name);
    List<Schedule> findSchedulesByUserId(int userId) throws SQLException;
    void deleteSchedule(int id);
}
