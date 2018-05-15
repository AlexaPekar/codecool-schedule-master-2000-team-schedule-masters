package com.codecool.web.service.simple;

import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Schedule;
import com.codecool.web.service.ScheduleService;

import java.sql.SQLException;
import java.util.List;

public class SimpleScheduleService implements ScheduleService{
    private final ScheduleDao scheduleDao;
    public SimpleScheduleService(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }
    @Override
    public Schedule getById(int id) throws SQLException, ServiceException {
        if(scheduleDao.findById(id)==null){
            throw new ServiceException("No schedules with this id");
        }
        return scheduleDao.findById(id);
    }

    @Override
    public Schedule addSchedule(int userId, String name) throws EmptyFieldException, ServiceException, SQLException {
        if(name.equals("")){
            throw new ServiceException(new EmptyFieldException("Fill all fields"));
        }
        return scheduleDao.insertSchedule(userId,name);
    }

    @Override
    public void editName(int id, String name) throws SQLException, ServiceException, EmptyFieldException {
        if(scheduleDao.findById(id)==null){
            throw new ServiceException("No user with this id");
        }
        if(name.equals("")){
            throw new ServiceException(new EmptyFieldException("Fill all fields"));
        }
        scheduleDao.updateName(id,name);
    }

    @Override
    public List<Schedule> getSchedulesByUserId(int userId) throws SQLException {
        return scheduleDao.findSchedulesByUserId(userId);
    }

    @Override
    public void removeSchedule(int id) throws SQLException, ServiceException {
        if(scheduleDao.findById(id)==null){
            throw new ServiceException("No user with this id");
        }
        scheduleDao.deleteSchedule(id);
    }
}
