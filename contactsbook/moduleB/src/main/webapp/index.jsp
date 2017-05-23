<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Список контактов</title>
    <link rel="stylesheet" type="text/css" href="resources/stylesheets/indexStylesheet.css" >
    <link rel="stylesheet" type="text/css" href="resources/stylesheets/modal.css" >
    <script src="resources/js/index.js"></script>
    <script src="resources/js/modal.js"></script>
    <script src="resources/js/validation.js"></script>
    <style>
        button[type='submit']:disabled{
            cursor: not-allowed;
        }
    </style>
    <meta charset="UTF-8">
</head>
<body>
<button id="backToList" type="submit" form="backToListForm"><<</button>
<section>
    <div>
        <button type="submit" id="createContact" value="createContact" form="contactForm" formmethod="get" formaction="CreateContactForm">
            Создать
        </button>
        <button type="submit" id="updateContact" value="updateContact" form="contactForm" formmethod="get" formaction="UpdateContact" disabled>
            Изменить
        </button>
        <button type="submit" id="deleteContact" value="deleteContact" form="contactForm" formmethod="post" formaction="DeleteContact" disabled>
            Удалить
        </button>
        Показывать
        <select name="pageNum" id="pageNum">
            <option value="10">10</option>
            <option value="20">20</option>
        </select>
        записей на странице
        <input type="text" name="search" placeholder="Search.." id="searchInput" onkeyup="search()"/>
    </div>

    <form method="post" id="contactForm">
        <table id="contactTable">
            <thead>
            <tr>
                <th><input type="checkbox" onchange="checkAll(this);manageUpdateDeleteButtons(this)" id="mainCheckBox"/></th>
                <th>Полное имя</th>
                <th>Дата рождения</th>
                <th>Адрес</th>
                <th>Место работы</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${contacts}" var="contact">
                <tr>
                    <td>
                        <input type="checkbox" name="checkBoxGroup" value="${contact.contactId}" onchange="manageUpdateDeleteButtons(this)"/>
                        <input type="hidden" class='hasEmail' value="${contact.email}"/>
                    </td>
                    <td><a href="ContactForm?contactId=${contact.contactId}"><span>${contact.surname} ${contact.name} ${contact.patronymic}</span></a></td>
                    <td>${contact.birth}</td>
                    <td>${contact.country} ${contact.city} ${contact.street} ${contact.postalCode}</td>
                    <td>${contact.job}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </form>
    <button onclick="callEmailForm()">Отправить email</button>
    <button onclick="callSearchModal()">Форма поиска</button>
    <form id="backToListForm" method="get" action="ContactList">
    </form>

    <div id="pagination"></div>
    <form id="paginationForm" method="get" action="${paginationFormAction}">
        <input type="hidden" id="skipTotal" name="skipTotal" value="${skipTotal}"/>
        <input type="hidden" id="currentPage" name="currentPage" value="${currentPage}"/>
        <input type="hidden" id="clientLimit" name="clientLimit" value="${clientLimit}"/>
        <input type="hidden" id="clickedPage" name="clickedPage" value="${currentPage}"/>
        <input type="hidden" id="pageTotal" name="pageTotal" value="${pageTotal}"/>
        <input type="hidden" class="isSameSearch" name="isSameSearch" value="1"/>
    </form>

    <div id="searchModal" class="modal">
        <div class="modal-content">
            <header></header>
            <span id="searchModalName">Форма поиска</span><br>
            <div id="search">
            <form id="searchFields" method="get" action="Search" onsubmit="searchFormValidator();return false">
                <label for="fname">Имя</label><br>
                <input type="text" id="fname" name="fname"><br>
                <label for="lname">Фамилия</label><br>
                <input type="text" id="lname" name="lname"><br>
                <label for="patronymic">Отчество</label><br>
                <input type="text" id="patronymic" name="patronymic"><br>
                <label>Дата рождения</label>
                <select id="comparator" name="comparator">
                    <option id="equal" value="equals" selected>Равно</option>
                    <option id="greater" value="greater">Больше</option>
                    <option id="less" value="less">Меньше</option>
                </select><br>
                дд:
                <input type="number" class="birth" name="day" id="dd" min="1">
                мм:
                <input type="number" class="birth" name="month" id="mm" min="1">
                гггг:
                <input type="number" class="birth" name="year" id="yyyy"><br>
                <label for="gender">Пол</label>
                        <select id="gender" name="gender">
                            <option value="">choose</option>
                            <option value="m">М</option>
                            <option value="f">Ж</option>
                        </select><br>
                <label for="citizenship">Гражданство</label><br>
                <input type="text" id="citizenship" name="citizenship"><br>
                <label for="status">Семейное положение</label><br>
                <input type="text" id="status" name="status"><br>
                <label for="email">Email</label><br>
                <input type="text" id="email" name="email"><br>
                <label for="web">Web-сайт</label><br>
                <input type="text" id="web" name="web"><br>
                <label for="job">Место работы</label><br>
                <input type="text" id="job" name="job"><br>
                <label for="country">Страна: </label><br>
                <input type="text" class="address" id="country" name="country">
                <label for="city">Город: </label>
                <input type="text" class="address" id="city" name="city"><br>
                <label for="street" >Улица: </label><br>
                <input type="text" class="address" id="street" name="street">
                <label for="index">Индекс: </label><br>
                <input type="text" class="address" id="index" name="postalCode">
                <button type="submit">Найти</button>
                <button onclick="closeModal()">Отменить</button>
                <input type="hidden" class="isSameSearch" name="isSameSearch" value="0"/>
            </form>
                <span id="generalMsg"></span>
            </div>

        </span>
            <footer></footer>
        </div>
    </div>

</section>
<div id="emailForm" class="modal">
    <div class="modal-content">
        <header></header>
        <form method="post" id="emailFormMdl" action="SendEmail" onsubmit="emailModalValidation();return false">
            <div id="subjContainer">
            Тема:
            <input type="text" name="subject" id="subject"><br>
            </div>
                Получатели
            <div id="mailBoxList"></div>
            <select id="pattern">
                    <option label="choose" value="" name="" selected>Шаблон</option>
                <c:forEach items="${msgTmpl}" var="template">
                    <option name="${template.name}" value="${template.template}">${template.name}</option>
                </c:forEach>
            </select>
            <textarea name="mailText" id="mailText" placeholder="Сообщение"></textarea>
            <button type = "submit">Отправить</button>
            <button type="button" onclick="closeModal()">Отменить</button>
            <input type="hidden" name="pattern" id="hiddenPattern" value=""/>
        </form>
        <footer></footer>
    </div>
</div>
<input type="hidden" id="isSearchPage" value="${isSearchPage}"/>

</body>
</html>
