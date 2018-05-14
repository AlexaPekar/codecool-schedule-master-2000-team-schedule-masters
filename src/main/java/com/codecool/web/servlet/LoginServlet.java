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

@WebServlet("/login")
public class LoginServlet extends AbstractServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = getConnection(req.getServletContext())){
            UserDao userDao = new DatabaseUserDao(connection);
            UserService userService = new SimpleUserService(userDao);

            String name = req.getParameter("name");
            String password = req.getParameter("password");

            User user = userService.loginUser(name, password);
            req.getSession().setAttribute("user", user);

            sendMessage(resp, HttpServletResponse.SC_OK, user);
        } catch (SQLException e) {
            handleSqlError(resp, e);
        } catch (NotFoundException e) {
            sendMessage(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (ServiceException e) {
            sendMessage(resp, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
