<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Homework Summary</title>
</head>
<body>
Homework Name : <c:out value="${homework.name}" /> <br/>
Start Date: <c:out value="${homework.startDate.toString()}" /> <br/>
End Date: <c:out value="${homework.endDate.toString()}" /> <br/>
Score Selection Scheme: <c:out value="${homework.scoreSelectionScheme.name}" /> <br/>
Allowed Attempts: <c:out value="${homework.numAttempts}" /> <br/>
Points For Correct Answer: <c:out value="${homework.correctPts}" /> <br/>
Points For Incorrect Answer: <c:out value="${homework.incorrectPts}" /> <br/>
Number of Questions: <c:out value="${homework.numQuestions}" /> <br/>

Questions selected along with their answers: <br/>
<c:forEach var="question" items="${homework.questions}">
    Question: <c:out value="${question.text}" /><br/>
    <c:forEach var="answer" items="${question.answers}">
        Answer: <c:out value="${answer.text}" /> <br/>
        Correct: <c:out value="${answer.correct}" /> <br/>
        Explaination/Hint: <c:out value="${answer.hint.text}" /> <br/> <br/>
    </c:forEach>
    Explaination: Hint: <c:out value="${question.hint.text}" /><br/>
    <br/><br/>
</c:forEach>
<a href="viewAllHomeworks">view another homework</a>
<a href="home">home</a>

</body>
</html>