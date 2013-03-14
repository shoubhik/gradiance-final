<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>
    <c:if test="${newhomework}">
    <title>Add Homework</title>
    </c:if>
    <c:if test="${!newhomework}">
        <title>Edit Homework</title>
    </c:if>
</head>
<body>
<c:if test="${newhomework}">
<p> Add Homework</p>
</c:if>
<c:if test="${!newhomework}">
    <p> Edit Homework</p>
</c:if>

<c:if test="${empty questions && newhomework}">
    there are no questions in this course select another course <br/>
    <a href="home">back</a>
</c:if>
<c:if test="${!(empty questions && newhomework)}">
<form:form method="post" modelAttribute="user">
    <form:errors path="" cssClass="error" />
    <table>
        <tr>
            <td>Homework Name</td>
            <c:if test="${newhomework}">
            <td><input type="text" name="homework.name" /></td>
            </c:if>
            <c:if test="${!newhomework}">
                <td><c:out value="${user.courseSelected.homework.name}"/></td>
            </c:if>
        </tr>
        <tr>
            <td>Start Date</td>
            <td><input type="text" name="homework.startDate" /></td>
            <td>specify date in 'yyyy-MM-dd:hh:mm:ss' format</td>
        </tr>
        <tr>
            <td>End Date</td>
            <td><input type="text" name="homework.endDate" /></td>
            <td>specify date in 'yyyy-MM-dd:hh:mm:ss' format</td>
        </tr>
        <tr>
            <td>Max Attempts</td>
            <td><input type="text" name="homework.numAttempts" /></td>
        </tr>
        <tr>
            <td>Enter 0 for infinite attempts</td>
        </tr>
        <tr>
            <td>Score selection scheme</td>
            <td><form:select  path="courseSelected.homework.scoreSelectionScheme" items="${schemes}"/></td>
        </tr>
        <tr>
            <td>Correct Points for questions</td>
            <td><input type="text" name="homework.correctPts" /></td>
        </tr>
        <tr>
            <td>Incorrect Points for questions</td>
            <td><input type="text" name="homework.incorrectPts" /></td>
        </tr>
        <tr>
            <td>Number of questions</td>
            <td><input type="text" name="homework.numQuestions" /></td>
            <td><b>this number should be less tha or equal to number of questions you select</b></td>
        </tr>
        <c:if test="${newhomework}">
        <tr>
            <td>Select Questions from question bank</td>
        </tr>
        <c:forEach var="question" items="${questions}">
            <tr>
                <td><input type="checkbox" name="question" value="${question.id}"><c:out value="${question.text}"/></td>
            </tr>
        </c:forEach>
        </c:if>
        <c:if test="${!newhomework}">
            <tr>
                <td>Select questions to delete (these questions are added to the homework)</td>
            </tr>
            <c:forEach var="question" items="${addedQuestions}">
                <tr>
                    <td><input type="checkbox" name="delete" value="${question.id}"><c:out value="${question.text}"/></td>
                </tr>
            </c:forEach>
            <tr>
                <td>Select questions to add (these questions are not yet added to the homework)</td>
            </tr>
            <c:forEach var="question" items="${notAddedQuestions}">
                <tr>
                    <td><input type="checkbox" name="add" value="${question.id}"><c:out value="${question.text}"/></td>
                </tr>
            </c:forEach>
        </c:if>
        <tr>
            <td colspan="3">
                <c:if test="${newhomework}">
                    <input type="hidden" value="1" name="_page"/>
                </c:if>
                <c:if test="${!newhomework}">
                    <input type="hidden" value="2" name="_page"/>
                </c:if>
                <input type="submit" value="Finish" name="_finish"/>
                <input type="submit" value="Cancel" name="_cancel"/>
            </td>
        </tr>
    </table>
</form:form>
</c:if>
</body>
</html>