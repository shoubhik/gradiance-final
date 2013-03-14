<%@ page import="java.sql.ResultSet" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Report</title>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>
</head><body>
<form:form method="post" modelAttribute="user">
    <td><form:errors path="" cssClass="error" /></td>
    <table>
        <tr>
            <td>Enter query:</td>
            <td><textarea name="report.query" ></textarea></td>
        </tr>
        <tr>
            <td colspan="1">
                <input type="submit" value="Submit" name="Submit" />
            </td>
        </tr>
    </table>
</form:form>

<c:if test="${showQuery}">
    <table border="1">
        <c:forEach var="col" items="${data}">
            <tr>
                <c:forEach var="row" items="${col}">
                    <td><c:out value="${row}"/></td>
                </c:forEach>
            </tr>
        </c:forEach>

    </table>
</c:if>


</body>
</html>