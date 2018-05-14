package com.codecool.web.service;

import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.Column;

import java.sql.SQLException;
import java.util.List;

public interface ColumnService {

    Column addColumn(int scheduleId,String name) throws ServiceException, SQLException;
    void removeColumn(int id) throws ServiceException, NotFoundException, SQLException;
    void editColumnName(int id,String name) throws ServiceException, NotFoundException, SQLException;
    Column getColumnById(int id) throws NotFoundException, SQLException, ServiceException;
    List<Column> getColumnsByScheduleId(int scheduleId) throws ServiceException, SQLException;
}
