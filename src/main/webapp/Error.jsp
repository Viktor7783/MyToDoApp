<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
</head>
<body style="text-align: center; font-family: Arial;">

<h1>Something went wrong</h1>

<p>
    We’re sorry, but an unexpected error occurred.
</p>

<c:if test="${not empty requestScope['javax.servlet.error.exception']}">
    <p style="color: gray;">
            ${requestScope['javax.servlet.error.exception'].message}
    </p>
</c:if>

<br>
<a href="${pageContext.request.contextPath}/list">Back to Home</a>

</body>
</html>