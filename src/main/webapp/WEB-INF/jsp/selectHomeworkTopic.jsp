<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
    <title>Select Topic</title>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>
</head>

<body>
Select the topic for which you want to add the question:<br/>
<form:form method="post" modelAttribute="selectedCourse">
    <td><form:errors path="" cssClass="error" /></td>
    <table>
        <tr>
            <td>Select Topic:</td>
            <td><form:select path="homework.topic" items="${topics}" /></td>
            <td><form:errors path="homework.topic" cssClass="error" /></td>
        </tr>
        <tr>
            <td colspan="3">
                <input type="hidden" value="0" name="_page"/>
                <input type="submit" value="Next" name="_target1" />
                <input type="submit" value="Cancel" name="_cancel" />
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>
