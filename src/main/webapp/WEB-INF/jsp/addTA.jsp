<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Course</title>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>
</head>
<body>
<%@include file='logout.jsp'%>
Only students who are not registered as student for the course can be selected as TS<br/>
<form:form method="post" modelAttribute="user">
    <form:errors path="" cssClass="error"/>
    <table>
        <tr>
            <td>Enter StudentId  <input type="text" name="ta" /></td>
        </tr>
        <tr>
            <td><input type="submit" value="Add TA"/></td>
        </tr>
    </table>
</form:form>
<br/>
<a href="home">back</a>
</body>
</html>