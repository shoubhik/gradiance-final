<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>TA Added</title>
</head>
<body>
<%@include file='logout.jsp'%>
TA <b><c:out value="${ta}"/></b> has bees successfully added for the course <c:out value="${course}"/> <br/>
<a href="selectCourse">back</a>

</body>
</html>