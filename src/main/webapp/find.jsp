<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="app_header.jsp" />

<table width="100%"><tr>
<th width="1">Table:</th>
<th width="1">${name}</th>
<th align="right"><a href="">return</a></th>
</tr></table>

<table class="inner" border="1" cellpadding = "3">
<c:forEach items="${content}" var="row">
    <tr>
    <c:forEach items="${row}" var="element">
        <td>
            ${element}
        </td>
    </c:forEach>
    </tr>
</c:forEach>
</table>

<jsp:include page="app_footer.jsp" />
