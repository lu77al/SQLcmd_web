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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
        if (action.equals("error")) {
            req.setAttribute("message", req.getParameter("message"));
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }

        DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
        if (dbManager == null) {
            req.getRequestDispatcher("connect.jsp").forward(req, resp);
            return;
        }

        if (action.equals("")) {
            req.setAttribute("tables", service.tables(dbManager));
            req.getRequestDispatcher("tables.jsp").forward(req, resp);
        } else  if (action.equals("find")) {
            String tableName = req.getParameter("table");
            req.setAttribute("name", tableName);
            req.setAttribute("content", service.find(dbManager, tableName));
            req.getRequestDispatcher("find.jsp").forward(req, resp);
        } else  if (action.equals("create")) {
            req.getRequestDispatcher("create.jsp").forward(req, resp);
        }

//        if (action.equals("")) {
//            req.setAttribute("items", service.commandsList());
//            req.getRequestDispatcher("menu.jsp").forward(req, resp);
//        } else if (action.equals("help")) {
//            req.getRequestDispatcher("help.jsp").forward(req, resp);
//        } else if (action.equals("connect")) {
//            req.getRequestDispatcher("connect.jsp").forward(req, resp);
//        } else if (action.equals("find")) {
//            String tableName = req.getParameter("table");
//            req.setAttribute("table", service.find(dbManager, tableName));
//            req.getRequestDispatcher("find.jsp").forward(req, resp);
//        } else {
//            req.setAttribute("message", "unsupported request");
//            req.getRequestDispatcher("error.jsp").forward(req, resp);
//        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        if (action.equals("disconnect")) {
            DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            if (dbManager != null) {
                dbManager.disconnect();
                dbManager = null;
                req.getSession().setAttribute("db_manager", dbManager);
            }
            resp.sendRedirect(resp.encodeRedirectURL(""));
            return;
        }
        if (action.equals("create")) {
            req.getSession().setAttribute("new_table_name", req.getParameter("table_name"));
            req.getSession().setAttribute("new_header", new LinkedHashSet<String>());
            resp.sendRedirect(resp.encodeRedirectURL("create"));
            return;
        }
        if (action.equals("add_column")) {
            Set<String> newHeader = (LinkedHashSet)req.getSession().getAttribute("new_header");
            newHeader.add(req.getParameter("column_name"));
            req.getSession().setAttribute("new_header", newHeader);
            resp.sendRedirect(resp.encodeRedirectURL("create"));
            return;
        }
        if (action.equals("cancel_create")) {
            req.getSession().setAttribute("new_header", null);
            resp.sendRedirect(resp.encodeRedirectURL(""));
            return;
        }
        if (action.equals("create_prepared")) {
            String tableName = (String)req.getSession().getAttribute("new_table_name");
            Set<String> newHeader = (LinkedHashSet)req.getSession().getAttribute("new_header");
            DatabaseManager dbManager = (DatabaseManager) req.getSession().getAttribute("db_manager");
            try {
                service.create(dbManager, tableName, newHeader);
                req.getSession().setAttribute("new_header", null);
                resp.sendRedirect(resp.encodeRedirectURL(""));
            } catch (Exception e) {
                String error =
                        e.getMessage().replaceAll("<", "~bold")
                                .replaceAll(">", "dlob~")
                                .replaceAll("~bold", "<b>")
                                .replaceAll("dlob~", "</b>");
                resp.sendRedirect(resp.encodeRedirectURL("error?message=" + error));
            }
            return;
        }
        if (action.equals("connect")) {
            req.setCharacterEncoding("UTF-8");
            String dbName = req.getParameter("dbname");
            String userName = req.getParameter("username");
            String password = req.getParameter("password");
            req.getSession().setAttribute("db_name", dbName);
            try {
                DatabaseManager dbManager = service.connect(dbName, userName, password);
                req.getSession().setAttribute("db_manager", dbManager);
                resp.sendRedirect(resp.encodeRedirectURL(""));
            } catch (Exception e) {
                String error =
                        e.getMessage().replaceAll("<", "~bold")
                                      .replaceAll(">", "dlob~")
                                      .replaceAll("~bold", "<b>")
                                      .replaceAll("dlob~", "</b>");
                resp.sendRedirect(resp.encodeRedirectURL("error?message=" + error));
            }
        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String action = requestURI.substring(req.getContextPath().length() + 1, requestURI.length());
        return action;
    }
}
