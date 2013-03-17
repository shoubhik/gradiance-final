<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Select Homework</title>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>
</head>

<body>
<%@include file='logout.jsp'%>
<c:if test="${empty user.courseSelected.homeworks}">
    this course has no homeworks to edit, select another course<br/>
    <a href="home">back</a>
</c:if>
<c:if test="${!(empty user.courseSelected.homeworks)}">
    <form:form method="post" modelAttribute="user">
        <td><form:errors path="" cssClass="error" /></td>
        <table>
            <tr>
                <td>Select Homework:</td>
            </tr>
            <c:forEach var="homework" items="${user.courseSelected.homeworks}">
                <c:forEach var="attempt" items="${homework.allAttempts}">
                    <tr>
                        <td><input type="radio" name="attempt" value="${homework.id}-${attempt.id}"><c:out value="${homework.name}"/></td>
                        <td>Attempted on: <c:out value="${attempt.time.toString()}"/>   </td>
                        <td>    Score: <c:out value="${attempt.score}"/></td>
                    </tr>
                </c:forEach>
            </c:forEach>
            <tr>
                <td colspan="3">
                    <input type="hidden" value="1" name="_page"/>
                    <input type="submit" value="Next" name="_target2" />
                    <input type="submit" value="Cancel" name="_cancel" />
                </td>
            </tr>
        </table>
    </form:form>
</c:if>
</body>
</html>
