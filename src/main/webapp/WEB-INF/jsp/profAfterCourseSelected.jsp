<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Course Options</title>
</head>
<body>
<%@include file='logout.jsp'%>
Selected Course: <c:out value="${user.courseSelected}"/> | <c:out value="${user.courseSelected.courseName}"/>
<br/>
<a href="addHomework">add homework</a><br/>
<a href="editHomework">edit homework</a><br/>
<a href="addQuestion">add question</a><br/>
<a href="addAnswer">add answer</a><br/>
<a href="reports">reports</a><br/>
<a href="selectCourse">select another course</a><br/>


</body>
</html>