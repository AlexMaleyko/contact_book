<%--
  Created by IntelliJ IDEA.
  User: Alexey
  Date: 03.04.2017
  Time: 21:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"
           prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
           prefix="fmt" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="resources/stylesheets/error.css" >
    <title>Ошибка</title>
</head>
<body>
<div class="error">
    <div class="error-content">
        <header></header>
        <div id="code">
            <h1>Произошла ошибка</h1>
        </div>
        <div id="message">
            <p>
            <ul>Причина ошибки:<br>
                <li><%=exception.getClass() %></li>
            Сообщение:<br>
                <li><%=exception.getMessage() %></li>
            </ul>
               Вы можете вернуться на <a href="/contact_book/contact_book/ContactList">начальную страницу</a>
            </p>
        </div>
        <footer></footer>
    </div>
</div>

</body>
</html>
