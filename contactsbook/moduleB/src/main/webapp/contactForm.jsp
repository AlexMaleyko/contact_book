<%--
  Created by IntelliJ IDEA.
  User: Alexey
  Date: 28.03.2017
  Time: 18:58
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>
        Создание/Редактирование
    </title>
    <link rel="stylesheet" type="text/css" href="resources/stylesheets/contactForm.css" >
    <link rel="stylesheet" type="text/css" href="resources/stylesheets/modal.css" >
    <script src="resources/js/contactForm.js"></script>
    <script src="resources/js/modal.js"></script>
    <script src="resources/js/validation.js"></script>
    <script src="resources/js/profileIMGmodal.js"></script>
    <meta charset="UTF-8">
</head>
<body>
<div id="pgBtns">
<button  type="submit" id="saveAll" form="contactForm">Сохранить все</button>
<button type="submit" form="contactList" formaction="ContactList" id="cancelAll">Отменить изменения</button>
</div>
<section id="container">
    <form id="contactForm" method="post" enctype="multipart/form-data" onsubmit="contactFormValidator();return false" action="${formaction}">
    <section id="contact">
        <div id="profileImgContainer">
            <img id="profileImg" alt="profileImg" src="<c:url value="${contact.profilePictureName}"/>"/>
        </div>
        <%--<button style="display:none;" id='cnslBtn'>Отменить изменения</button>--%>
        <div id="contactInfo">
            <label for="fname">Имя*</label><br>
            <input type="text" id="fname" name="fname" value="<c:out value="${contact.name}"/>"><br>
            <label for="lname">Фамилия*</label><br>
            <input type="text" id="lname" name="lname" value="<c:out value="${contact.surname}"/>"><br>
            <label for="patronymic">Отчество</label><br>
            <input type="text" id="patronymic" name="patronymic" value="<c:out value="${contact.patronymic}"/>"><br>
            <label>Дата рождения</label><br>
            дд:
            <input type="number" class="contactBirth" name="day" id="dd" min="1" value="${dd}">
            мм:
            <input type="number" class="contactBirth" name="month" id="mm" min="1" value="${mm}">
            гггг:
            <input type="number" class="contactBirth" name="year" id="yyyy" value="${yyyy}"><br>
            <label for="gender">Пол</label>
            <c:choose>
                <c:when test = "${fn:toLowerCase(contact.gender) == 'm'}">
                <select id="gender" name="gender">
                    <option value="m" selected>М</option>
                    <option value="f">Ж</option>
                </select><br>
                </c:when>
                <c:when test = "${fn:toLowerCase(contact.gender) == 'f'}">
                    <select id="gender" name="gender">
                        <option value="f">М</option>
                        <option value="m" selected>Ж</option>
                    </select><br>
                </c:when>
                <c:otherwise>
                    <select id="gender" name="gender">
                        <option value="" selected></option>
                        <option value="m">М</option>
                        <option value="f">Ж</option>
                    </select><br>
                </c:otherwise>
            </c:choose>
            <label for="citizenship">Гражданство</label><br>
            <input type="text" id="citizenship" name="citizenship" value="<c:out value="${contact.citizenship}"/>"><br>
            <label for="status">Семейное положение</label><br>
            <input type="text" id="status" name="status" value="<c:out value="${contact.maritalStatus}"/>"><br>
            <label for="email">Email</label><br>
            <input type="text" id="email" name="email" value="<c:out value="${contact.email}"/>"><br>
            <label for="web">Web-сайт</label><br>
            <input type="text" id="web" name="web" value="<c:out value="${contact.website}"/>"><br>
            <label for="job">Место работы</label><br>
            <input type="text" id="job" name="job" value="<c:out value="${contact.job}"/>"><br>
            <label for="country">Страна: </label>
            <input type="text" class="address" name="country" id="country" value="<c:out value="${contact.country}"/>">
            <label for="city">Город: </label>
            <input type="text" class="address" id="city" name="city" value="<c:out value="${contact.city}"/>"><br>
            <label for="street">Улица: </label>
            <input type="text" class="address" id="street" name="street" value="<c:out value="${contact.street}"/>">
            <label for="index">Индекс: </label>
            <input type="text" class="address" id="index" name="index" value="<c:out value="${contact.postalCode}"/>">
        </div>
    </section>


    <section id="associatedData">

        <section id="phones">
            <div>Контактные телефоны</div>
            <input type="button" value="Добавить" class="phoneBtn" id="createPhoneBtn" onclick="callPhoneModal(this)"/>
            <input type="button" value="Изменить" class="phoneBtn" id="updatePhoneBtn" onclick="callPhoneModal(this)" disabled/>
            <input type="button" value="Удалить" class="phoneBtn" id="deletePhoneBtn" onclick="deletePhones()" disabled/>
            <table id="phoneTable">
                <thead>
                <tr>
                    <th><input type="checkbox" id="mainCheckBox" onclick="checkAllPhones(this);manageUpdateButton(this)"/></th>
                    <th>Номер</th>
                    <th>Тип</th>
                    <th>Комментарий</th>
                </tr>
                </thead>
                <tbody>
                <tr style="display: none">
                    <td><input type="checkbox" name="checkBoxGroup" onchange="manageUpdateButton(this)" value=""/></td>
                    <td><span></span>
                        <input type="hidden" name="countryCode" class="countryCode" value=""/>
                        <input type="hidden" name="operatorCode" class="operatorCode" value=""/>
                        <input type="hidden" name="number" class="number" value=""/>
                    </td>
                    <td><span></span>
                        <input type="hidden" name="type" class="type" value=""/>
                    </td>
                    <td><span></span>
                        <input type="hidden" name="phoneComment" class="phoneComment" value=""/>
                    </td>
                </tr>
                <c:forEach items="${contact.numberDTOList}" var="phone">
                    <tr>
                        <td><input type="checkbox" name="checkBoxGroup" onchange="manageUpdateButton(this)" value="${phone.numberId}"/></td>
                        <td><span>${phone.countryCode}${phone.operatorCode}${phone.number}</span>
                            <input type="hidden" name="countryCode" class="countryCode" value="${phone.countryCode}"/>
                            <input type="hidden" name="operatorCode" class="operatorCode" value="${phone.operatorCode}"/>
                            <input type="hidden" name="number" class="number" value="${phone.number}"/>
                        </td>
                        <td><span>${phone.type}</span>
                            <input type="hidden" name="type" class="type" value="${phone.type}"/>
                        </td>
                        <td><span>${phone.comment}</span>
                            <input type="hidden" name="phoneComment" class="phoneComment" value="${phone.comment}"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </section>

        <section id="attachments">
            <div>Прикрепленые файлы</div>
            <input type="button" value="Добавить" class="attachmentBtn" id="createAttachBtn" onclick="callAttachmentModal(this)"/>
            <input type="button" value="Изменить" class="attachmentBtn" id="updateAttachBtn" onclick="callAttachmentModal(this)" disabled/>
            <input type="button" value="Удалить" class="attachmentBtn" id="deleteAttachBtn"  onclick="deleteAttachs()" disabled />
            <table id="attachTable">
                <thead>
                <tr>
                    <th><input type="checkbox" id="mCheckBox" onclick="checkAllAttachs(this);manageUpdateButton1(this)"/></th>
                    <th>Имя файла</th>
                    <th>Дата загрузки</th>
                    <th>Комментарий</th>
                </tr>
                </thead>
                <tbody>
                <tr style="display: none">
                    <td><input type="checkbox" name="checkBoxGroup1" onchange="manageUpdateButton1(this)" value=""/></td>
                    <td><span></span>
                        <input type="hidden" class="fileName" name="fileName" value=""/>
                    </td>
                    <td><span></span></td>
                    <td><span></span>
                        <input type="hidden" class="attachComment" name="attachComment" value=""/>
                    </td>
                </tr>
                <c:forEach items="${contact.attachmentDTOList}" var="attach">
                    <tr>
                        <td><input type="checkbox" name="checkBoxGroup1" onchange="manageUpdateButton1(this)" value="${attach.attachmentId}"/></td>
                        <td><a href="${attach.filePath}" target="_blank"><span>${attach.fileName}</span></a>
                            <input type="hidden" class="fileName" name="fileName" value="${attach.fileName}"/>
                        </td>
                        <td><span><fmt:formatDate value="${attach.uploadDate}" pattern="MM/dd/yyyy HH:mm" /></span></td>
                        <td><span>${attach.comment}</span>
                            <input type="hidden" class="attachComment" name="attachComment" value="${attach.comment}"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </section>
    </section>

        <div id="attachmentModal" class="modal">
            <div class="modal-content">
                <header></header>
                <span id="attachmentModalName"></span><br>
                <span id="attachmentInfo">
            Имя файла:
            <input type="text" size="30" maxlength="30" id="mdlFileName"/>
        </span>
                <br>
                <div class="container" >
                    <%--<div class="contr"><h2>You can select the file (image) and click Upload button</h2></div>--%>
                    <div class="upload_form_cont">
                        <div>
                            <div><h3>Выберите файл или изображение и нажмите на кнопку "Загрузить"</h3></div>
                            <div id="fileInputs">

                            </div>
                        </div>
                        <%--<div>
                            <input type="button" value="Загрузить" onclick="startUploading()" />
                        </div>--%>
                        <div id="fileinfo">
                            <div id="filename"></div>
                            <div id="filesize"></div>
                            <div id="filetype"></div>
                            <div id="filedim"></div>
                        </div>
                        <div id="error">You should select valid image files only!</div>
                        <%--<div id="error2">An error occurred while uploading the file</div>
                        <div id="abort">The upload has been canceled by the user or the browser dropped the connection</div>--%>
                        <div id="warnsize">Your file is very big. We can't accept it. Please select more small file</div>
                        <div id="progress_info">
                            <div id="progress"></div>
                            <div id="progress_percent">&nbsp;</div>
                            <div class="clear_both"></div>
                            <div>
                                <div id="speed">&nbsp;</div>
                                <div id="remaining">&nbsp;</div>
                                <div id="b_transfered">&nbsp;</div>
                                <div class="clear_both"></div>
                            </div>
                            <div id="upload_response"></div>
                        </div>
                        <img id="preview" />
                    </div>
                </div>
                <br>
                <span class="comment">
                <span>Комментарий:</span><br>
                <textarea rows="4" cols="50" maxlength="200" id="mdlAttachComment"></textarea>
                <br>
                <button type="button" id="mdlSaveAtchBtn" onclick="newAttachRecord()">Сохранить</button>
                <button type="button" id="mdlUpdateAtchBtn" onclick="updateAttachRecord();closeModal()">Сохранить</button>
                <button type="button" onclick="closeModal()">Отменить</button>
            </span>
                <footer></footer>
            </div>
        </div>
        <input type="file" name="profileImg" id="imgInput" style="display:none;"/>
        <input type="hidden" name="contactId" id="contactId" value="<c:out value="${contact.contactId}"/>"/>
    </form>
</section>

<!--modal windows-->
<div id="phoneModal" class="modal">
    <div class="modal-content">
        <header></header>
        <span id="phoneModalName"></span><br>
        <span id="numberInfo">
            Тел.
            <input type="text" size="4" maxlength="4" id="mdlCountryCode">
            <input type="text" size="4" maxlength="4" id="mdlOperatorCode">
            <input type="text" size="8" maxlength="8" id="mdlNumber">
            <br>
            Тип:
            <select id="mdlPhoneSel">
                <option id="mdlEmpty" value=""></option>
                <option id="mdlM" value="m">Мобильный</option>
                <option id="mdlH" value="hf">Домашний</option>
            </select>
        </span>
        <br>
        <span class="comment">
            <span>Комментарий:</span><br>
            <textarea rows="4" cols="50" maxlength="200" id="mdlPhoneComment"></textarea>
            <br>
            <button id="mdlSavePhBtn" onclick="newPhoneRecord()">Сохранить</button>
            <button id="mdlUpdatePhBtn" onclick="updatePhoneRecord()">Сохранить</button>
            <button onclick="closeModal()">Отменить</button>
        </span>
        <footer></footer>
    </div>
</div>

<form id="contactList" action="ContactList" method="get"></form>

<div id="ImgModal" class="modal">
<div class="modal-content">
    <!-- Modal Content (The Image) -->
    <img id="mdlImg" alt="profile picture">

    <div id="buttons">
        <button id="chooseImg">Изменить</button>
        <button id="saveImg">Сохранить</button>
        <button id="cancelImg">Отменить все</button>
    </div>
</div>
</div>

</body>
</html>
