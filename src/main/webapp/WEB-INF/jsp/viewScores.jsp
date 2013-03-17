<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Scores</title>
</head>
<body>
<%@include file='logout.jsp'%>
<table border="1">
    <tr>
        <th>Homework Name</th>
        <th>Score</th>
    </tr>
    <c:forEach var="homework" items="${homeworks}">
        <tr>
            <td><c:out value="${homework.name}"></c:out></td>
            <td>Score: <c:out value="${homework.aggregateScore}"/></td>
        </tr>
    </c:forEach>
</table>
<a href="selectCourse">back</a>

</body>
</html>