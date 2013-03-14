<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
<c:if test="${empty user.courseSelected.topics}">
    there are not topics in this course select another course <br/>
    <a href="selectCourse">back</a>
</c:if>
<c:if test="${!(empty user.courseSelected.topics)}">
    Select the topic for which you want to add the question:<br/>
    <form:form method="post" modelAttribute="user">
        <td><form:errors path="" cssClass="error"/></td>
        <table>
            <tr>
                <td>Select Topic:</td>
                <td><form:select path="courseSelected.topics" items="${topic}" multiple="false"/></td>
            </tr>
            <tr>
                <td colspan="3">
                    <input type="hidden" value="0" name="_page"/>
                    <input type="submit" value="Next" name="_target1"/>
                    <input type="submit" value="Cancel" name="_cancel"/>
                </td>
            </tr>
        </table>
    </form:form>
</c:if>
</body>
</html>
