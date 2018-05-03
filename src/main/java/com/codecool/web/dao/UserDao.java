package com.codecool.web.dao;


import com.codecool.web.model.User;

import java.sql.SQLException;

public interface UserDao {

    User findByName(String name) throws SQLException;
    User insertNewUser(String name, String password, String role) throws SQLException;

}
