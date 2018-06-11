package com.codecool.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/guest")
public class GuestAccessServlet extends AbstractServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //int scheduleId = Integer.parseInt(req.getParameter("scheduleid"));
        req.setAttribute("guest",true);
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}
