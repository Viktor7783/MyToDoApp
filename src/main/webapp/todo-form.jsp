<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Management Application</title>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>

<body>

<c:if test="${not empty ERROR}">
    <div style="color:red; margin-bottom:15px;">
        <c:out value="${ERROR}"/>
    </div>
</c:if>

<header>
    <!-- nav - Это навигационная панель (меню), стилизованная через Bootstrap. Это семантический HTML-тег для навигации-->
    <nav class="navbar navbar-expand-md navbar-dark" style="background-color: tomato">
        <!-- ul = unordered list (неупорядоченный список). Он создаёт список с маркерами (точками).-->
        <ul class="navbar-nav">
            <!-- li = list item (элемент списка). это пункт меню (Todos, Logout).-->
            <li><a href="<c:url value='/list'/>" class="nav-link">Todos</a></li>
        </ul>

        <ul class="navbar-nav navbar-collapse justify-content-end">
            <li><a href="<c:url value='/logout'/>" class="nav-link">Logout</a></li>
        </ul>
    </nav>
</header>
<div class="container col-md-5">
    <div class="card">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/${empty todo ? 'insert' : 'update'}" method="post">

                <h2>
                    <c:if test="${not empty todo}">
                        Edit Todo
                    </c:if>
                    <c:if test="${empty todo}">
                        Add New Todo
                    </c:if>
                </h2>

                <c:if test="${not empty todo}">
                    <input type="hidden" name="id" value="<c:out value='${todo.id}' />"/>
                </c:if>

                <!--fieldset — это группировка элементов формы. Он логически объединяет поля. -->
                <fieldset class="form-group">
                    <label for="title">Todo Title</label>
                    <input type="text" id="title"
                           value="<c:out value='${title != null ? title : (todo != null ? todo.title : "")}' />"
                           class="form-control"
                           name="title"
                           required="required"
                           minlength="5">
                </fieldset>

                <fieldset class="form-group">
                    <label for="description">Todo Description</label>
                    <textarea id="description"
                              class="form-control"
                              name="description"
                              minlength="5"
                              rows="4"><c:out
                            value="${description !=null ? description : (todo != null ? todo.description : '')}"/></textarea>

                </fieldset>

                <fieldset class="form-group">
                    <label for="status">Todo Status</label>
                    <select class="form-control" id="status" name="isDone">
                        <option value="false" ${not empty todo and todo.done == false ? "selected" : ""}>
                            In Progress
                        </option>

                        <option value="true" ${not empty todo and todo.done == true ? "selected" : ""}>
                            Complete
                        </option>
                    </select>
                </fieldset>

                <fieldset class="form-group">
                    <label for="target_date">Todo Target Date</label>
                    <input type="date" id="target_date"
                           value="<c:out value='${todo.targetDate}' />"
                           class="form-control"
                           name="targetDate" required="required">
                </fieldset>

                <button type="submit" class="btn btn-success">Save</button>
                <a href="<c:url value='/list'/>" class="btn btn-secondary">Cancel</a>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/common/footer.jsp"/>
</body>
</html>