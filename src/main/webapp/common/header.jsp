<%--Это часть для вставки в register.jsp (partials никогда не содержат <html>, <head>, <body>)--%>

<header> <%--Семантический HTML-тег.cОбозначает шапку страницы (верхний блок сайта)--%>
    <nav class="navbar navbar-expand-md navbar-dark" style="background-color: tomato">
        <%--nav — навигационный блок.Здесь находится меню и ссылки по сайту.
          Классы относятся к Bootstrap и отвечают за внешний вид.--%>

        <div> <%-- div — универсальный контейнер.Нужен просто для группировки элементов,
              никакого смысла сам по себе не несёт.--%>
            <a class="navbar-brand" href="<%= request.getContextPath() %>">Todo App</a>
            <%-- a — ссылка. request.getContextPath() возвращает корень веб-приложения (например /todo-app)--%>
        </div>

        <ul class="navbar-nav navbar-collapse justify-content-end">
            <%-- ul — список пунктов меню.Меню логически является списком ссылок.--%>

            <li> <%-- li — один пункт списка (один пункт меню)--%>
                <a href="<%= request.getContextPath() %>/login" class="nav-link">Login</a>
            </li>

            <li>
                <a href="<%= request.getContextPath() %>/register" class="nav-link">Signup</a>
            </li>

        </ul>

    </nav>
</header>
