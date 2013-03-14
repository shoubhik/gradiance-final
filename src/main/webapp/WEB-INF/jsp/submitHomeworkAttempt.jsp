<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Homework</title>
</head>
<body>
<form:form method="post" modelAttribute="user">
    <form:errors path="" cssClass="error" />
    <table>
    <c:forEach var="question" items="${user.courseSelected.homework.currentAttempt.questions}">
        <tr>
            <td>Question:</td>
            <td><c:out value="${question.text}"/><br/></td>
        </tr>
        <c:forEach var="answer" items="${question.answers}">
            <tr>
                <td>Answer:</td>
                <td><input type="radio" name="${question.id}"
                           value="${answer.id}">
                    <c:out value="${answer.text}"/><br/>
                    </input>
                </td>
            </tr>
        </c:forEach>
        <c:set var="exp_id" value="${question.id}-Exp" />
        <tr>
            <td> Explaination </td>
            <td><textarea name="${exp_id}"></textarea> <br/></td>
        </tr>
        </table>
    </c:forEach>
    <tr>
        <td colspan="3">
            <input type="hidden" value="2" name="_page"/>
            <input type="submit" value="Finish" name="_finish"/>
            <input type="submit" value="Cancel" name="_cancel"/>
        </td>
    </tr>
</form:form>
</body>
</html>