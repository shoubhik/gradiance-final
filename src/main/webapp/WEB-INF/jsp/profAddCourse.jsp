<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>

    <title>Add Course</title>
</head>
<body>
Only the courses that have not expired and not previously added can be added.<br/>
<form:form method="post" modelAttribute="user">
    <form:errors path="" cssClass="error"/>
    <table>
        <tr>
            <td>SelectCourse <form:select path="courseSelected" items="${course}" multiple="false"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Add"/></td>
        </tr>
    </table>
</form:form>
<br/>
<a href="home">back</a>
</body>
</html>