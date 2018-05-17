package com.codecool.web.service;

import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    User getByName(String name) throws NotFoundException, SQLException, ServiceException;

    User addNewUser(String name,String password,String role) throws SQLException, ServiceException;

    User loginUser(String name, String password) throws ServiceException, SQLException, NotFoundException;

    List<User> getAllUsers(User user) throws SQLException, ServiceException;

}
