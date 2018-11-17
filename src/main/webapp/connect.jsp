<jsp:include page="app_header.jsp" />

<h4>Please, connect to database:</h4>
<form action = "connect" method = "post"><table>
    <tr><td>Database name</td><td><input type = "text" name = "dbname"></td></tr>
    <tr><td>User name</td><td><input type = "text" name = "username"></td></tr>
    <tr><td>Password</td><td><input type = "password" name = "password"></td></tr>
    <tr><td colspan = "2" align="center"><input type = "submit" value = "connect"></td></tr>
</table></form>

<jsp:include page="app_footer.jsp" />
