<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="app_header.jsp" />

<table>
 <tr>
  <th>Database</th>
  <th width = "5">&nbsp</th>
  <th><font color="green">${db_name}</font></th>
  <th width = "5">&nbsp</th>
  <th><form action = "disconnect" method = "post">
    <input type = "submit" value = "disconnect">
  </form></th>
 </tr>
<c:forEach items="${tables}" var="table">
 <tr>
  <td>table</td>
  <td></td>
  <td><a href="find?table=${table}">${table}</a></td>
  <td></td>
  <td><form action = "delete${table}" method = "post">
    <input type = "submit" value = "delete">
  </form></td>
 </tr>
</c:forEach>
<tr><td colspan="5" align = "center">
<a href="createtable">Create new table</a>
</table>

<jsp:include page="app_footer.jsp" />
