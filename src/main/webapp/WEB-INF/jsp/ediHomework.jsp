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
    <p> Edit Homework</p>

    <form:form method="post" modelAttribute="user">
    <form:errors path="" cssClass="error" />
        <c:set var="homework" value="${user.courseSelected.homework}"/>
        <table>
            <tr>
                <td>Homework Name</td>
                    <td><c:out value="${homework.name}"/></td>
            </tr>
            <tr>
                <td>Start Date</td>
                <td><input type="text" name="homework.startDate" value="${homework.getStartDateString()}"/></td>
                <td>specify date in 'yyyy-MM-dd:hh:mm:ss' format</td>
            </tr>
            <tr>
                <td>End Date</td>
                <td><input type="text" name="homework.endDate" value="${homework.getEndDAteString()}"/></td>
                <td>specify date in 'yyyy-MM-dd:hh:mm:ss' format</td>
            </tr>
            <tr>
                <td>Max Attempts</td>
                <td><input type="text" name="homework.numAttempts" value="${homework.numAttempts}"/></td>
            </tr>
            <tr>
                <td>Enter 0 for infinite attempts</td>
            </tr>
            <tr>
                <td>Score selection scheme</td>
                <td><form:select  path="courseSelected.homework.scoreSelectionScheme" items="${schemes}"  /></td>
            </tr>
            <tr>
                <td>Correct Points for questions</td>
                <td><input type="text" name="homework.correctPts" value="${homework.correctPts}"/></td>
            </tr>
            <tr>
                <td>Incorrect Points for questions</td>
                <td><input type="text" name="homework.incorrectPts" value="${homework.incorrectPts}"/></td>
            </tr>
            <tr>
                <td>Number of questions</td>
                <td><input type="text" name="homework.numQuestions" value="${homework.numQuestions}"/></td>
                <td><b>this number should be less tha or equal to number of questions you select</b></td>
            </tr>
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
            <tr>
                <td colspan="3">
                    <input type="hidden" value="2" name="_page"/>
                    <input type="submit" value="Finish" name="_finish"/>
                    <input type="submit" value="Cancel" name="_cancel"/>
                </td>
            </tr>
        </table>
    </form:form>
</body>
</html>