package com.codecool.web.service.simple;

import com.codecool.web.dao.UserDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.User;
import com.codecool.web.service.UserService;

import java.sql.SQLException;
import java.util.List;

public class SimpleUserService implements UserService {

    private final UserDao uD;

    public SimpleUserService(UserDao uD) {
        this.uD = uD;
    }

    @Override
    public User getByName(String name) throws NotFoundException, SQLException, ServiceException {
        if (name.equals("")) {
            throw new ServiceException("You didn't enter a name");
        }
        if (uD.findByName(name) == null) {
            throw new ServiceException("User not found");
        }
        return uD.findByName(name);
    }

    @Override
    public User addNewUser(String name, String password, String role) throws SQLException, ServiceException {
        if (name.equals("") || password.equals("") || role.equals("") ) {
            throw new ServiceException(new EmptyFieldException("Fill all fields"));
        }

        return uD.insertNewUser(name,password,role);
    }

    @Override
    public User loginUser(String name, String password) throws ServiceException, SQLException, NotFoundException {
        User user = this.getByName(name);
        if (!user.getPassword().equals(password)) {
            throw new ServiceException("Wrong password!");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers(User user) throws SQLException, ServiceException {
        String userRole = user.getRole();

        if (userRole.equals("Admin")){
            return uD.findAllUsers();
        } else {
            throw new ServiceException("You are not authorized for this movement.");
        }
    }

    public User getUserById(String id) throws SQLException, ServiceException {
        try {
            return uD.findUserById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new ServiceException("Must be a number");
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Must be a number");
        }
    }
}
