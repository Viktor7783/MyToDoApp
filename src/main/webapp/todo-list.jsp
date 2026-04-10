<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Management Application</title>

    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
          crossorigin="anonymous">
</head>

<body>

<c:if test="${not empty ERROR}">
    <div style="color:red; margin-bottom:15px;">
        <c:out value="${ERROR}"/>
    </div>
</c:if>

<header>
    <nav class="navbar navbar-expand-md navbar-dark" style="background-color: tomato">
        <ul class="navbar-nav">
            <li><a href="<c:url value='/list'/>" class="nav-link">Todos</a></li>
        </ul>

        <ul class="navbar-nav navbar-collapse justify-content-end">
            <li><a href="<c:url value='/logout'/>" class="nav-link">Logout</a></li>
        </ul>
    </nav>
</header>

<div class="row">

    <div class="container">
        <h3 class="text-center">List of Todos</h3>
        <hr>
        <div class="container text-left">
            <a href="<c:url value='/new'/>" class="btn btn-success">Add Todo</a>
        </div>
        <br>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Title</th>
                <th>Description</th>
                <th>Target Date</th>
                <th>Todo Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="todo" items="${listToDo}">

                <tr>
                    <td><c:out value="${todo.title}"/></td>
                    <td title="${todo.description}">
                        <c:out value="${todo.description}"/>
                    </td>
                    <td><c:out value="${todo.targetDate}"/></td>
                    <td><c:out value="${todo.done}"/></td>

                    <c:url var="editUrl" value="/edit">
                        <c:param name="id" value="${todo.id}"/>
                    </c:url>

                    <c:url var="deleteUrl" value="/delete">
                        <c:param name="id" value="${todo.id}"/>
                    </c:url>

                    <td style="display: flex; gap: 15px; align-items: center;">
                        <a href="${editUrl}">Edit</a>
                        <form action="<c:url value='/delete'/>"
                              method="post"
                              style="margin: 0;"
                              onsubmit="return confirm('Delete this todo?');">
                            <input type="hidden" name="id" value="${todo.id}">
                            <button type="submit" class="btn btn-link p-0">Delete</button>
                        </form>
                    </td>

                </tr>
            </c:forEach>
            </tbody>

        </table>
    </div>
</div>

<jsp:include page="/common/footer.jsp"/>
</body>
</html>
