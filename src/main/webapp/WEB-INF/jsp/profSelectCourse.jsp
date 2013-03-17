<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Select course</title>
</head>
<body>
<%@include file='logout.jsp'%>
Please select a course that you are affiliated with as a <c:out value="${user.roleName}"/>
<form:form method="post" modelAttribute="user">
    <form:errors path="" cssClass="error"/>
    <table>
        <tr>
            <td>SelectCourse <form:select path="courseSelected" items="${course}" multiple="false"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Next"/></td>
        </tr>
    </table>
</form:form>
<a href="home">back</a>
</body>
</html>