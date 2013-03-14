<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Options</title>
</head>
<body>
Selected Options: <c:out value="${user.courseSelected.courseId}"/> | <c:out value="${user.courseSelected.courseName}"/>
<br/>
<a href="viewAllHomeworks">View All Homeworks</a><br/>
<a href="selectCourse">select another course</a><br/>

</body>
</html>