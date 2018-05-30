package com.codecool.web.servlet;

import com.codecool.web.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/protected/logout")
public class LogoutServlet extends AbstractServlet {
    private final Logger logger  = LoggerFactory.getLogger(LogoutServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("user");
        req.getSession().invalidate();
        resp.setStatus(HttpServletResponse.SC_OK);
        logger.info("{} logged out", user.getId());

    }
}
