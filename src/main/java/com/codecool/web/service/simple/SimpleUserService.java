package com.codecool.web.service.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codecool.web.dao.UserDao;
import com.codecool.web.exceptions.EmptyFieldException;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.User;
import com.codecool.web.service.UserService;

import java.sql.SQLException;
import java.util.List;

public class SimpleUserService implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleUserService.class);

    private final UserDao uD;

    public SimpleUserService(UserDao uD) {
        this.uD = uD;
    }

    @Override
    public User getByName(String name) throws NotFoundException, SQLException, ServiceException {
        if (name.equals("")) {
            logger.error("No user name entered");
            throw new ServiceException("You didn't enter a name");
        }
        if (uD.findByName(name) == null) {
            logger.error("No user found by name");
            throw new ServiceException("User not found by this name");
        }
        logger.info("User returned by name");
        return uD.findByName(name);
    }

    @Override
    public User addNewUser(String name, String password, String passwordAgain, String role) throws SQLException, ServiceException {
        try {
            if (name.equals("") || password.equals("") || passwordAgain.equals("") || role.equals("")) {
                logger.error("Field(s) left empty");
                throw new ServiceException(new EmptyFieldException("Fill all fields!"));
            }
            logger.info("New user added");
            if (password.equals(passwordAgain)) {
                return uD.insertNewUser(name, password, role);
            } else {
                throw new ServiceException("Passwords don't match!");
            }
        }catch (SQLException ex) {
            throw new ServiceException("Username is already taken.");
        }
    }

    @Override
    public User loginUser(String name, String password) throws ServiceException, SQLException, NotFoundException {
        User user = this.getByName(name);
        if (!user.getPassword().equals(password)) {
            logger.error("Wrong password entered");
            throw new ServiceException("Wrong password!");
        }
        logger.info("User logged in");
        return user;
    }

    @Override
    public List<User> getAllUsers(User user) throws SQLException, ServiceException {
        String userRole = user.getRole();

        if (userRole.equals("Admin")){
            logger.info("All users returned");
            return uD.findAllUsers();
        } else {
            logger.error("No authorization");
            throw new ServiceException("You are not authorized for this movement.");
        }
    }

    public User getUserById(String id) throws SQLException, ServiceException {
        try {
            logger.info("User returned by ID");
            return uD.findUserById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            logger.error("Parameter to 'findUserById' method must be int");
            throw new ServiceException("Must be a number");
        } catch (IllegalArgumentException e) {
            logger.error("Parameter to 'findUserById' method must be int");
            throw new ServiceException("Illegal argument, must be number");
        }
    }

    @Override
    public User addNewUser(String name, String role) throws SQLException {
        return uD.insertNewUser(name,role);
    }
}
