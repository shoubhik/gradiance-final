<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Select Question</title>
    <style>
        .error {
            color: #ff0000;
            font-weight: bold;
        }
    </style>
</head>
<body>
<c:if test="${empty user.courseSelected.selectedTopic.questions}">
    there are not questions in this topic select another topic <br/>
    <a href="addAnswer">back</a>
</c:if>
<c:if test="${!(empty user.courseSelected.selectedTopic.questions)}">
    Select the question for which you want to add the answers:<br/>
    <form:form method="post" modelAttribute="user">
        <td><form:errors path="" cssClass="error"/></td>

        <table>
            <tr>
                <td>Select Question:</td>
                <td><form:select path="courseSelected.selectedTopic.question"
                                 items="${question}"/></td>
            </tr>
            <tr>
                <td colspan="3">
                    <input type="hidden" value="1" name="_page"/>
                    <input type="submit" value="Next" name="_target2"/>
                    <input type="submit" value="Cancel" name="_cancel"/>
                </td>
            </tr>
        </table>
    </form:form>
</c:if>
</body>
</html>