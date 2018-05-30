package com.codecool.web.service.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codecool.web.dao.ColumnDao;
import com.codecool.web.dao.ScheduleDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Column;
import com.codecool.web.service.ColumnService;

import java.sql.SQLException;
import java.util.List;

public class SimpleColumnService implements ColumnService{

    private static final Logger logger = LoggerFactory.getLogger(SimpleColumnService.class);

    private final ColumnDao columnDao;
    private final ScheduleDao scheduleDao;

    public SimpleColumnService(ColumnDao columnDao, ScheduleDao scheduleDao) {
        this.columnDao = columnDao;
        this.scheduleDao = scheduleDao;
    }

    @Override
    public Column addColumn(int scheduleId, String name) throws ServiceException, SQLException {
        if (name.equals("")) {
            logger.error("Name field left empty");
            throw new ServiceException(new EmptyFieldException("Name is empty"));
        }
        if (scheduleDao.findById(scheduleId) == null) {
            logger.error("No schedule by ID");
            throw new ServiceException("No schedule found with this id");
        }
        logger.info("New column added");
        return columnDao.insert(scheduleId,name);
    }

    @Override
    public void removeColumn(int id) throws ServiceException, NotFoundException, SQLException {
        if (columnDao.findById(id) == null) {
            logger.error("No column by ID");
            throw new ServiceException("No column found with this id");
        }
        logger.info("Column removed");
        columnDao.delete(id);
    }

    @Override
    public void editColumnName(int id, String name) throws ServiceException, NotFoundException, SQLException {
        if (name.equals("")) {
            logger.error("Name field left empty");
            throw new ServiceException(new EmptyFieldException("Name is empty"));
        }
        if (columnDao.findById(id) == null) {
            logger.error("No column by ID");
            throw new ServiceException("No column found with this id");
        }
        logger.info("Column name edited");
        columnDao.updateName(id,name);

    }

    @Override
    public Column getColumnById(int id) throws NotFoundException, SQLException, ServiceException {
        if (columnDao.findById(id) == null) {
            logger.error("No column by ID");
            throw new ServiceException("No column found with this id");
        }
        logger.info("Column returned by ID");
        return columnDao.findById(id);
    }

    @Override
    public List<Column> getColumnsByScheduleId(int scheduleId) throws ServiceException, SQLException {
        if (scheduleDao.findById(scheduleId) == null) {
            logger.error("No column by ID");
            throw new ServiceException("No schedule found with given id");
        }
        logger.info("Columns returned by Schedule ID");
        return columnDao.findColumnsByScheduleID(scheduleId);
    }
}
