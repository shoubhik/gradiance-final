<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
</head>
<body>
<c:if test="${newquestion}">
 Question successfully added!!
</c:if>
<c:if test="${!newquestion}">
    Answers have been success fully added
</c:if>
<br/>
<a href="selectCourse">home</a>
</body>
</html>