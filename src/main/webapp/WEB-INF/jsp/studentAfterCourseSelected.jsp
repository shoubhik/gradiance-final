<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Options</title>
</head>
<body>
Selected Options: <c:out value="${user.courseSelected.courseId}"/> | <c:out value="${user.courseSelected.courseName}"/>
<br/>
<a href="attemptHomework">Attempt homework</a><br/>
<a href="viewPastSubmissions">View past homeworks</a><br/>
<a href="viewScores">View Scores</a><br/>
<a href="selectCourse">select another course</a><br/>

</body>
</html>