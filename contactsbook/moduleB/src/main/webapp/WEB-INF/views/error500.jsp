<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/stylesheets/error.css" >
    <title>Ошибка</title>
</head>
<body>
<div class="error">
    <div class="error-content">
        <header></header>
        <div id="code">
            <h1>500: ошибка сервера.</h1>
        </div>
        <div id="message">
            <p>
                Произошел сбой в работе приложения.
            </p>
        </div>
        <footer></footer>
    </div>
</div>
</body>
</html>
