package com.codecool.web.servlet;

import com.codecool.web.dao.UserDao;
import com.codecool.web.dao.database.DatabaseUserDao;
import com.codecool.web.exceptions.NotFoundException;
import com.codecool.web.exceptions.ServiceException;
import com.codecool.web.model.User;
import com.codecool.web.service.UserService;
import com.codecool.web.service.simple.SimpleUserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login/google")
public class GoogleLoginServlet extends AbstractServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        try (Connection connection = getConnection(req.getServletContext())) {
            UserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new SimpleUserService(userDao);

            //String password = req.getParameter("password");

            User user = userService.getByName(email);
            req.getSession().setAttribute("user", user);

            sendMessage(resp, HttpServletResponse.SC_OK, user);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (NotFoundException e) {
            try (Connection connection = getConnection(req.getServletContext())) {
                UserDao userDao = new DatabaseUserDao(connection);
                UserService userService = new SimpleUserService(userDao);
                User user = userService.addNewUser(email,"User");
                req.getSession().setAttribute("user", user);
                sendMessage(resp, HttpServletResponse.SC_OK, user);
            } catch (SQLException e1) {
                handleSqlError(resp, e1);
            }
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
