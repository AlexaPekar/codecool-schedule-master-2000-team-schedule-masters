package com.codecool.web.dao;

import com.codecool.web.model.Schedule;

import java.util.List;

public interface ScheduleDao {

    Schedule findById(int id);
    Schedule insertSchedule(int userId,String name);
    Schedule updateName(int id,String name);
    List<Schedule> findSchedulesByUserId(int userId);
    void deleteSchedule(int id);
}
