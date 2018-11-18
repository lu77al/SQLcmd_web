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
<c:set var="count" value="1" scope="page" />
<c:forEach items="${tables}" var="table">
 <tr>
  <td>table #${count}</td>
  <td></td>
  <td><a href="find?table=${table}">${table}</a></td>
  <td></td>
  <td><form action = "drop?table=${table}" method = "post">
    <input type = "submit" value = "delete">
  </form></td>
 </tr>
 <c:set var="count" value="${count + 1}" scope="page"/>
</c:forEach>
</td></tr></table>
<form action = "create" method = "post">
<table></tr>
 <td>#${count}</td>
 <td><input type = "text" name = "table_name"></td>
 <td><input type = "submit" value = "Create new"></td>
</tr></tabe>
</form>

<jsp:include page="app_footer.jsp" />
