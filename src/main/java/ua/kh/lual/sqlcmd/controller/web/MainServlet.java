package ua.kh.lual.sqlcmd.controller.web;

import ua.kh.lual.sqlcmd.model.DBManagerException;
import ua.kh.lual.sqlcmd.model.DatabaseManager;
import ua.kh.lual.sqlcmd.service.Service;
import ua.kh.lual.sqlcmd.service.ServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private Service service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = new ServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db-manager");

        if (action.equals("")) {
            req.setAttribute("items", service.commandsList());
            req.getRequestDispatcher("menu.jsp").forward(req, resp);
        } else if (action.equals("help")) {
            req.getRequestDispatcher("help.jsp").forward(req, resp);
        } else if (action.equals("connect")) {
            req.getRequestDispatcher("connect.jsp").forward(req, resp);
        } else if (action.equals("find")) {
            String tableName = req.getParameter("table");
            req.setAttribute("table", service.find(dbManager, tableName));
            req.getRequestDispatcher("find.jsp").forward(req, resp);
        } else {
            req.setAttribute("message", "unsupported request");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        if (action.equals("connect")) {
            req.setCharacterEncoding("UTF-8");
            String dbName = req.getParameter("dbname");
            String userName = req.getParameter("username");
            String password = req.getParameter("password");
            try {
                DatabaseManager dbManager = service.connect(dbName, userName, password);
                req.getSession().setAttribute("db-manager", dbManager);
                resp.sendRedirect(resp.encodeRedirectURL(""));
            } catch (Exception e) {
                req.setAttribute("message",
                        e.getMessage().replaceAll("<", "~bold")
                                      .replaceAll(">", "dlob~")
                                      .replaceAll("~bold", "<b>")
                                      .replaceAll("dlob~", "</b>"));
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String action = requestURI.substring(req.getContextPath().length() + 1, requestURI.length());
        return action;
    }
}
