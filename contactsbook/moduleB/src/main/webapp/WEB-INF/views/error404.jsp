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
            <h1>404: страница не найдена.</h1>
        </div>
        <div id="message">
            <p>
                Ресурс к которому вы обращаетесь мог быть удален или переименован.<br>
                Проверьте правильность введенного Вами адреса. Если ничего не выйдет, вы можете вернуться на
                <a href="${pageContext.request.contextPath}">начальную страницу</a>
            </p>
        </div>
        <footer></footer>
    </div>
</div>
</body>
</html>