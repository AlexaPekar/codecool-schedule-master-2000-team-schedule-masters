package com.codecool.web.dao;


import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    User findByName(String name) throws SQLException, NotFoundException;
    User insertNewUser(String name, String password, String role) throws SQLException;
    List<User> findAllUsers() throws SQLException;
    User findUserById(int id) throws SQLException;
    User insertNewUser(String name, String role) throws SQLException;
}
