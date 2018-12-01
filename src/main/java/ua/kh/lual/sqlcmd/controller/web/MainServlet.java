package ua.kh.lual.sqlcmd.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ua.kh.lual.sqlcmd.model.DatabaseManager;
import ua.kh.lual.sqlcmd.service.ConnectionService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class MainServlet extends HttpServlet {

    @Autowired
    private ConnectionService connectionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        if (action.equals("error")) {
            showJSP("error", req, resp);
            return;
        }

        DatabaseManager dbManager = getDBManager(req);
        if (dbManager == null) {
            showJSP("connect", req, resp);
            return;
        }

        if (action.equals("")) {
            req.setAttribute("tables", dbManager.getTableNames());
            showJSP("tables", req, resp);

        } else if (action.equals("find")) {
            String tableName = req.getParameter("table");
            req.setAttribute("name", tableName);
            req.setAttribute("content", find(dbManager, tableName));
            showJSP("find", req, resp);

        } else if (action.equals("create")) {
            showJSP("create", req, resp);

        } else {
            redirectError("unsupported request", "", req, resp);
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        if (action.equals("disconnect")) {
            DatabaseManager dbManager = getDBManager(req);
            if (dbManager != null) {
                dbManager.disconnect();
                dbManager = null;
                req.getSession().setAttribute("db_manager", dbManager);
            }
            redirect("", req, resp);
            return;
        }

        if (action.equals("create")) {
            String tableName = req.getParameter("table_name");
            if (tableName.length() == 0) {
                redirectError("Can\'t create table without name", "", req, resp);
            } else {
                req.getSession().setAttribute("new_table_name", req.getParameter("table_name"));
                req.getSession().setAttribute("new_header", new LinkedHashSet<String>());
                redirect("create", req, resp);
            }
            return;
        }

        if (action.equals("add_column")) {
            String columnName = req.getParameter("column_name");
            if (columnName.length() == 0) {
                redirectError("Can\'t create empty column", "create", req, resp);
            } else {
                Set<String> newHeader = (LinkedHashSet) req.getSession().getAttribute("new_header");
                newHeader.add(columnName);
                redirect("create", req, resp);
            }
            return;
        }

        if (action.equals("cancel_create")) {
            req.getSession().setAttribute("new_header", null);
            redirect("", req, resp);
            return;
        }

        if (action.equals("create_prepared")) {
            String tableName = (String)req.getSession().getAttribute("new_table_name");
            Set<String> newHeader = (LinkedHashSet)req.getSession().getAttribute("new_header");
            DatabaseManager dbManager = getDBManager(req);
            try {
                dbManager.createTable(tableName, newHeader);
                req.getSession().setAttribute("new_header", null);
                redirect("", req, resp);
            } catch (Exception e) {
                redirectError(getExceptionMessage(e), "", req, resp);
            }
            return;
        }
        if (action.equals("drop")) {
            String tableName = req.getParameter("table");
            DatabaseManager dbManager = getDBManager(req);
            try {
                dbManager.dropTable(tableName);
                redirect("", req, resp);
            } catch (Exception e) {
                redirectError(getExceptionMessage(e), "", req, resp);
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
                DatabaseManager dbManager = connectionService.connect(dbName, userName, password);
                req.getSession().setAttribute("db_manager", dbManager);
                redirect("", req, resp);
            } catch (Exception e) {
                redirectError(getExceptionMessage(e), "", req, resp);
            }
            return;
        }
    }

    public List<List<String>> find(DatabaseManager dbManager, String tableName) {
        List<List<String>> result = new LinkedList<>();
        List<String> header = new ArrayList<>(dbManager.getTableHeader(tableName));
        result.add(header);
        List<List> content = dbManager.getAllContent(tableName);
        for (List row: content) {
            List<String> nextRow = new ArrayList<>(header.size());
            for (Object cell: row) {
                nextRow.add(cell.toString());
            }
            result.add(nextRow);
        }
        return result;
    }

    private void redirect(String uri, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath()+ "/" + uri);
    }

    private void showJSP(String jspName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(jspName + ".jsp").forward(req, resp);
    }

    private DatabaseManager getDBManager(HttpServletRequest req) {
        return (DatabaseManager) req.getSession().getAttribute("db_manager");
    }

    private void redirectError(String message, String return_uri, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().setAttribute("error_message", message);
        req.getSession().setAttribute("error_return_uri", return_uri);
        redirect("error", req, resp);
    }

    private String getExceptionMessage(Exception e) {
        return e.getMessage()
                .replaceAll("<", "~bold")
                .replaceAll(">", "dlob~")
                .replaceAll("~bold", "<b>")
                .replaceAll("dlob~", "</b>");
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length() + 1, requestURI.length());
   }
}
