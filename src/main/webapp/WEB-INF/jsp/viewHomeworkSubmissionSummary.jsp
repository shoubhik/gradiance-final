<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Submission Summary</title>
</head>
<body>
<%@include file='logout.jsp'%>
Score : <c:out value="${summary.score}" /> <br/>
<c:forEach var="question" items="${summary.questions}">
    Question: <c:out value="${question.text}" /><br/>
    Your Response: <c:out value="${question.response.text}" /><br/>
    <c:if test="${question.response.correct }">
        your response is correct<br/>
        Explaination: <c:out value="${question.response.hint.text}" /><br/>
    </c:if>
    <c:if test="${!question.response.correct }">
        your response is incorrect<br/>
        Hint: <c:out value="${question.response.hint.text}" /><br/>
    </c:if>
    <br/><br/>
</c:forEach>
<a href="home">home</a>     | <a href="attemptHomework">attempt another homework</a>

</body>
</html>