package com.codecool.web.service.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(SimpleScheduleService.class);

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
            logger.error("No schedule found by ID");
            throw new ServiceException("No schedules with this id");
        }
        logger.info("Schedule returned by ID");
        return scheduleDao.findById(id);
    }

    @Override
    public Schedule addSchedule(int userId, String name,int amountOfColumns) throws EmptyFieldException, ServiceException, SQLException {
        if(name.equals("")){
            logger.error("Field(s) left empty");
            throw new ServiceException(new EmptyFieldException("Fill all fields"));
        }
        if (amountOfColumns < 1 || amountOfColumns > 7) {
            logger.error("Column number not between 1 and 7");
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
        logger.info("New schedule added");
        return schedule;
    }

    @Override
    public void editName(int id, String name) throws SQLException, ServiceException, EmptyFieldException {
        if(scheduleDao.findById(id)==null){
            logger.error("No schedule by ID");
            throw new ServiceException("No schedule with this id");
        }
        if(name.equals("")){
            logger.error("Field(s) left empty");
            throw new ServiceException(new EmptyFieldException("Fill all fields"));
        }
        logger.info("Schedule name edited");
        scheduleDao.updateName(id,name);
    }

    @Override
    public List<Schedule> getSchedulesByUserId(int userId) throws SQLException {
        logger.info("Schedules returned by User ID");
        return scheduleDao.findSchedulesByUserId(userId);
    }

    @Override
    public void removeSchedule(int id) throws SQLException, ServiceException {
        if(scheduleDao.findById(id)==null){
            logger.error("No schedule found by ID");
            throw new ServiceException("No schedule with this id");
        }
        logger.info("Schedule removed");
        scheduleDao.deleteSchedule(id);
    }

    @Override
    public int getColumnNumber(int id) throws SQLException {
        logger.info("Column number returned by ID");
        return scheduleDao.getColumnNumber(id);
    }
}
