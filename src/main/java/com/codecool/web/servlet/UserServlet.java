package com.codecool.web.servlet;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.User;
import com.codecool.web.service.UserService;
import com.codecool.web.service.simple.SimpleUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/protected/user")
public class UserServlet extends AbstractServlet {

    private final Logger logger = LoggerFactory.getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser =(User) req.getSession().getAttribute("user");
        try (Connection connection = getConnection(req.getServletContext())) {
            UserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new SimpleUserService(userDao);

            String userId = req.getParameter("id");

            User user = userService.getUserById(userId);

            sendMessage(resp, HttpServletResponse.SC_OK, user);
            logger.info("User with ID:{} requested data of user with ID:{}",user.getId(),currentUser.getId());


        } catch (SQLException e) {
            handleSqlError(resp, e);
            logger.error("{} for user ID: {}",e.getMessage(),currentUser.getId());
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            logger.error("{} for user ID: {}",e.getMessage(),currentUser.getId());
        }
    }
}
