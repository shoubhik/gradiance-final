<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>

    <title>Add Question</title>
</head>
<body>
<%@include file='logout.jsp'%>
<p> Add question to question bank</p>
<form:form method="post" modelAttribute="user">
    <form:errors path="" cssClass="error" />
    <table>
        <c:if test="${newquestion}">
        <tr>
            <td>Enter Question:</td>
            <td><textarea name="question.text" ></textarea></td>
        </tr>
        </c:if>
        <c:if test="${!newquestion}">
            <tr>
                <td>Question:</td>
                <td><c:out value="${user.courseSelected.selectedTopic.question.text}"/></td>
            </tr>
        </c:if>
        <c:if test="${newquestion}">
        <tr>
            <td>Difficulty Level:</td>
            <td><input type="text" name="difficultyLevel" /></td>
        </tr>
        <tr> <td>The difficulty level can be between 1 and 5 and is mandatory</td></tr>
        <tr>
            <td>Points awarded for correct answer:</td>
            <td><input type="text"  name="pointCorrect" /></td>
        </tr>
        <tr>
            <td>Points deducted for incorrect answer:</td>
            <td><input type="text" name="pointIncorrect" /></td>
        </tr>
        <tr>
            <td>Explanation for correct answer:</td>
            <td><textarea name="question.hint" ></textarea></td>
        </tr>
        </c:if>
        <tr>
            <td>Fill in the correct answers:</td>
        </tr>
        <c:forEach var="i" begin="1" end="${user.courseSelected.selectedTopic.question.numCorrectAnswers}">
            <c:set var="txt_name" value="${'correct_ans'}${i}"/>
            <c:set var="hnt_name" value="${'correct_hint'}${i}"/>
            <tr>
                <td>Correct Answer:</td>
                <td><textarea name="${txt_name}"></textarea></td>
            </tr>
            <tr>
                <td>Answer explaination:</td>
                <td><textarea name="${hnt_name}"></textarea></td>
            </tr>
        </c:forEach>
        <tr>
            <td>Fill in the Incorrect answers:</td>
        </tr>
        <c:forEach var="i" begin="1" end="${user.courseSelected.selectedTopic.question.numIncorrectAnswers}">
            <c:set var="txt_name" value="${'incorrect_ans'}${i}"/>
            <c:set var="hnt_name" value="${'incorrect_hint'}${i}"/>
            <tr>
                <td>Incorrect Answer:</td>
                <td><textarea name="${txt_name}"></textarea></td>
            </tr>
            <tr>
                <td>Answer hint:</td>
                <td><textarea name="${hnt_name}"></textarea></td>
            </tr>
        </c:forEach>
        <tr>
            <c:if test="${newquestion}">
            <td colspan="3">
                <input type="hidden" value="2" name="_page"/>
                <input type="submit" value="Finish" name="_finish" />
                <input type="submit" value="Cancel" name="_cancel" />
            </td>
            </c:if>
            <c:if test="${!newquestion}">
            <td colspan="3">
                <input type="hidden" value="3" name="_page"/>
                <input type="submit" value="Finish" name="_finish" />
                <input type="submit" value="Cancel" name="_cancel" />
            </td>
            </c:if>
        </tr>
    </table>
</form:form>

</body>
</html>