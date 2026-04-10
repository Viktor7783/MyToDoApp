<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Registration</title>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>


<body>
<jsp:include page="/common/header.jsp" />

<c:if test="${not empty ERROR}">
    <div style="color:red; margin-bottom:15px;">
        <c:out value="${ERROR}"/>
    </div>
</c:if>

<div class="container">

    <h2>User Register Form</h2>
    <div class="col-md-6 offset-md-3">

        <c:if test="${not empty NOTIFICATION}">
            <div class="alert alert-success center" role="alert">
                <p><c:out value="${NOTIFICATION}"/></p>
            </div>
        </c:if>
        <!-- Форма используется для отправки данных на сервер -->
        <form action="<c:url value='/register'/>" method="post">
            <div class="form-group">
                <!-- label связывается с input через атрибут for, значение for должно совпадать с id у input -->
                <label for="firstName">First Name:</label> <%--label for="firstName" & input id="firstName"--%>
                <input type="text"
                       class="form-control"
                       id="firstName"
                       placeholder="First Name"
                       name="firstName"
                       value="<c:out value='${firstName}' />"
                       required>
                <!-- placeholder — это текст-подсказка внутри поля ввода, он отображается, пока пользователь ничего не ввёл -->
            </div>

            <div class="form-group">
                <label for="lastName">Last Name:</label>
                <input type="text"
                       class="form-control"
                       id="lastName"
                       placeholder="Last Name"
                       name="lastName"
                       value="<c:out value='${lastName}' />"
                       required>
            </div>

            <div class="form-group">
                <label for="username">User Name:</label>
                <input type="text"
                       class="form-control"
                       id="username"
                       placeholder="User Name"
                       name="username"
                       value="<c:out value='${username}' />"
                       required>
            </div>

            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password"
                       class="form-control"
                       id="password"
                       placeholder="Password"
                       name="password"
                       required>
            </div>

            <button type="submit" class="btn btn-primary">Submit</button>

        </form>
    </div>
</div>
<jsp:include page="/common/footer.jsp" />
</body>

</html>
