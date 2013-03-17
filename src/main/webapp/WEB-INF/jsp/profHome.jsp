<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
</head>
<body>
<%@include file='logout.jsp'%>
        hello <c:out value="${user.roleName}"/> <c:out value="${user.userName}"/><br/>
    <a href="selectCourse">select course</a>        <br/>
    <a href="addCourse">Add course</a>             <br/>
    <a href="home">Back</a>                         <br/>
</body>
</html>