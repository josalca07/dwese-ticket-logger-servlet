package org.iesalixar.daw2.jorge.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);
    private String userName = "Jorge";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entering the doGet method of UserServlet.");
        request.setAttribute("userName", userName);
        request.getRequestDispatcher("/user.jsp").forward(request, response);
        logger.info("Exiting the doGet method of UserServlet.");
    }
}