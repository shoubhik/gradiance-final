<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Login Screen</title>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>

</head>
<body>
<form:form method="post" modelAttribute="user">
    <form:errors path="" cssClass="error"/>
    <table>
        <tr>
            <td>Name <input type="text" name="uname"/></td>
            <td><form:errors path="userName" cssClass="error"/></td>
        </tr>
        <tr>
            <td>Password <input type="password" name="password"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Login"/></td>
        </tr>
    </table>
</form:form>
<a href="register">Register new user</a>
</body>
</html>
