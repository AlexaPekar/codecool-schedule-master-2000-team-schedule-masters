package com.codecool.web.service;

import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.User;

import java.sql.SQLException;

public interface UserService {

    User getByName(String name) throws NotFoundException, SQLException, ServiceException;

    User addNewUser(String name,String password,String role) throws SQLException, ServiceException;

}
