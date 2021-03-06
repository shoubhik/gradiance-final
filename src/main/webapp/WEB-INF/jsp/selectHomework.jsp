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
<c:if test="${editHomework}">
Select the homework  which you want to edit: Only homeworks that have not yet started are shown here and can be edited<br/>
</c:if>
<c:if test="${attemptHomework}">
    Select homework you want to attempt:<br/>
</c:if>
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
            <tr>
                <td><input type="radio" name="homework" value="${homework.id}"><c:out value="${homework.name}"/></td>
            </tr>
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
