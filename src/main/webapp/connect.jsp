<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>SQLCmd</title>
        <base href="${pageContext.request.contextPath}/"/>
    </head>
    <body>
        <form action = "connect" method = "post"><table>
            <tr><td>Database name</td><td><input type = "text" name = "dbname"></td></tr>
            <tr><td>User name</td><td><input type = "text" name = "username"></td></tr>
            <tr><td>Password</td><td><input type = "password" name = "password"></td></tr>
            <tr><td colspan = "2"><input type = "submit" value = "connect"></td></tr>
        </table></form>
    </body>
</html>