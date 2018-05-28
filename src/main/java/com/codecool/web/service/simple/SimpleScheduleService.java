package com.codecool.web.service.simple;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.dao.SlotDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Column;
import com.codecool.web.model.Schedule;
import com.codecool.web.service.ScheduleService;

import java.sql.SQLException;
import java.util.List;

public class SimpleScheduleService implements ScheduleService{
    private final ScheduleDao scheduleDao;
    private final ColumnDao columnDao;
    private final SlotDao slotDao;

    public SimpleScheduleService(ScheduleDao scheduleDao,ColumnDao columnDao,SlotDao slotDao) {
        this.scheduleDao = scheduleDao;
        this.columnDao = columnDao;
        this.slotDao = slotDao;
    }
    @Override
    public Schedule getById(int id) throws SQLException, ServiceException {
        if(scheduleDao.findById(id)==null){
            throw new ServiceException("No schedules with this id");
        }
        return scheduleDao.findById(id);
    }

    @Override
    public Schedule addSchedule(int userId, String name,int amountOfColumns) throws EmptyFieldException, ServiceException, SQLException {
        if(name.equals("")){
            throw new ServiceException(new EmptyFieldException("Fill all fields"));
        }
        if (amountOfColumns < 1 || amountOfColumns > 7) {
            throw new ServiceException("Invalid amount of columns.Enter number between 1 and 7");
        }
        Schedule schedule = scheduleDao.insertSchedule(userId,name);

        for (int i = 0;i < amountOfColumns;i++) {
            Column column =columnDao.insert(schedule.getId(),"Enter text");
            for (int j= 7;j < 19;j++) {
                String timeRange = j + "-" + (j+1);
                slotDao.insertNewSlot(column.getId(),timeRange);
            }
        }
        return schedule;
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

    @Override
    public int getColumnNumber(int id) throws SQLException {
        return scheduleDao.getColumnNumber(id);
    }
}
