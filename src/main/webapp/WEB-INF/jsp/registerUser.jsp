<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
Register New User
<form:form method="post" modelAttribute="user">
    <form:errors path="" cssClass="error"/>
    <table>
        <tr>
            <td>User Id<input type="text" name="uid"/></td>
        </tr>
        <tr>
            <td>User Name<input type="text" name="uname"/></td>
        </tr>
        <tr>
            <td>Password <input type="password" name="password"/></td>
        </tr>
        <tr>
            <td>Role
                <form:select name="role" path="roleName" items="${roles}" multiple="false"/>
            </td>
            <td><form:errors path="roleName" cssClass="error"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Register"/></td>
        </tr>
    </table>
</form:form>

</body>
</html>