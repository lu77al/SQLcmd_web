<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="app_header.jsp" />

<b>Let's create a new table <font color="green" size="+1">[${new_table_name}]<font></b><br>
<form action = "add_column" method = "post">
<table>
<c:set var="count" value="1" scope="page" />
<c:forEach items="${new_header}" var="column">
 <tr>
  <td>column #${count}</td>
  <td>&nbsp&nbsp</td>
  <td>${column}</a></td>
  <td></td>
 </tr>
 <c:set var="count" value="${count + 1}" scope="page"/>
</c:forEach>
 <tr>
  <td>column #${count}</td>
  <td>&nbsp&nbsp</td>
  <td><input type = "text" name = "column_name"></td>
  <td><input type = "submit" value = "add new"></td>
 </tr>
</table>
</form>

<table width="80%"><tr>
 <td align = "right"><form action = "create_prepared" method = "post">
  <input type = "submit" value = "Create table">
 </form></td>
  <td>&nbsp&nbsp</td>
 <td align = "left"><form action = "cancel_create" method = "post">
  <input type = "submit" value = "Cancel">
 </form></td>
</tr></table>


<jsp:include page="app_footer.jsp" />
