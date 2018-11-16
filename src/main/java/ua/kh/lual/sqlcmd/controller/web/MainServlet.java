package ua.kh.lual.sqlcmd.controller.web;

import ua.kh.lual.sqlcmd.service.Service;
import ua.kh.lual.sqlcmd.service.ServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    Service service = new ServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        if (action.equals("")) {
            req.setAttribute("items", service.commandsList());
            req.getRequestDispatcher("menu.jsp").forward(req,resp);
        } else if (action.equals("help")) {
            req.getRequestDispatcher("help.jsp").forward(req,resp);
        } else {
            req.getRequestDispatcher("error.jsp").forward(req,resp);
        }

    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String action = requestURI.substring(req.getContextPath().length() + 1, requestURI.length());
        return action;
    }
}
