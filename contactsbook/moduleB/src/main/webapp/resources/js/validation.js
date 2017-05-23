/**
 * Created by Alexey on 03.04.2017.
 */
function daysInMonth(month, year) {
    return new Date(year, month, 0).getDate();
}

function searchFormValidator() {
    "use strict";
    var generalMsg = document.getElementById("generalMsg");
    var searchForm = document.getElementById("searchFields");
    var inputs = searchForm.getElementsByTagName("input");
    //first, reset style of those boxes which were set to red previously
    for(var i=0; i<inputs.length; i++){
        inputs[i].style.borderColor= '#ccc';
    }
    //second, do validation logic
    var filled = 0;
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].value.replace(/\s/g, "").length > 0) {
            filled++;
        }
    }
    if (filled === 0 && document.getElementById('gender').value === '') {
        generalMsg.innerHTML="Заполните по крайней мере одно поле";
        return false;
    }
    var email = document.getElementById('email');
    if(!ValidateEmail(email) && email.value.replace(/\s/g, "").length > 0){
        generalMsg.innerHTML = "Введен неверный формат email";
        document.getElementById('email').style.borderColor = 'red';
        return false;
    }
    var birthInputs = searchForm.getElementsByClassName("birth");
    filled = 0;
    for (var i = 0; i < birthInputs.length; i++) {
        if (birthInputs[i].value.replace(/\s/g, "").length > 0) {
            filled++;
        }
        else{
            birthInputs[i].style.borderColor = "red";
        }
    }
    if(filled<3 && filled>0){
        generalMsg.innerHTML = "Введите дату полностью";
        return false;
    }
    else if(filled === 3) {
        var mm = document.getElementById("mm");
        var dd = document.getElementById("dd");
        var yyyy = document.getElementById("yyyy");
        if(isNaN(mm.value) || isNaN(dd.value) || isNaN(yyyy.value)){
            generalMsg.innerHTML = "В одном из полей даты используются нечисловые символы";
            return false;
        }
        if (+mm.value > 12 || +mm.value < 1) {
            alert(+mm.value > 12);
            generalMsg.innerHTML = "В поле 'месяц' введено недопустимое число";
            mm.style.borderColor = "red";
            return false;
        }
        if (+yyyy.value > (new Date().getFullYear()) || +yyyy.value < 1900) {
            generalMsg.innerHTML = "В поле 'год' введено недопустимое число";
            yyyy.style.borderColor = "red";
            return false;
        }
        if (+dd.value < 0) {
            generalMsg.innerHTML = "В поле 'день' введено недопустимое число";
            dd.style.borderColor = "red";
            return false;
        }
        if(+dd.value > daysInMonth(mm.value, yyyy.value)){
            generalMsg.innerHTML = "В указанном вами месяце " + daysInMonth(mm.value, yyyy.value) + " дней";
            dd.style.borderColor = "red";
            return false;
        }
    }
    searchForm.submit();
}

function contactFormValidator(){
    "use strict";
    var contactForm = document.getElementById('contactForm');
    var contactInfo = document.getElementById('contactInfo');
    var inputs = contactInfo.getElementsByTagName('input');
    //first, reset style of those boxes which were set to red previously
    for(var i=0; i<inputs.length; i++){
        inputs[i].style.borderColor= '#ccc';
    }
    //second, do validation logic
    var fname = document.getElementById('fname');
    var lname = document.getElementById('lname');
    if( fname.value.replace(/\s/g, "").length === 0 || lname.value.replace(/\s/g, "").length === 0){
        fname.style.borderColor = 'red';
        lname.style.borderColor = 'red';
        alert("Все обязательные поля должны быть заполенеы");
        return false;
    }
    var email = document.getElementById('email');
    if(!ValidateEmail(email) && email.value.replace(/\s/g, "").length > 0){
        alert("Введен неверный формат email");
        document.getElementById('email').style.borderColor = 'red';
        return false;
    }
    var birthInputs = contactInfo.getElementsByClassName("contactBirth");
    var filled = 0;
    for (var i = 0; i < birthInputs.length; i++) {
        if (birthInputs[i].value.replace(/\s/g, "").length > 0) {
            filled++;
        }
        else{
            birthInputs[i].style.borderColor = "red";
        }
    }
    if(filled<3 && filled>0){
        alert("Введите дату полностью");
        return false;
    }
    else if(filled === 3) {
        var mm = document.getElementById("mm");
        var dd = document.getElementById("dd");
        var yyyy = document.getElementById("yyyy");
        if(isNaN(mm.value) || isNaN(dd.value) || isNaN(yyyy.value)){
            alert("В одном из полей даты используются нечисловые символы");
            return false;
        }
        if (+mm.value > 12 || +mm.value < 1) {
            alert("В поле 'месяц' введено недопустимое число");
            mm.style.borderColor = "red";
            return false;
        }
        if (+yyyy.value > (new Date().getFullYear()) || +yyyy.value < 1900) {
            alert("В поле 'год' введено недопустимое число.");
            yyyy.style.borderColor = "red";
            return false;
        }
        if (+dd.value < 0) {
            alert("В поле 'день' введено недопустимое число");
            dd.style.borderColor = "red";
            return false;
        }
        if(+dd.value > daysInMonth(mm.value, yyyy.value)){
            alert("В указанном вами месяце " + daysInMonth(mm.value, yyyy.value) + " дней");
            dd.style.borderColor = "red";
            return false;
        }
    }
    var inputs = document.getElementsByTagName('input');
    var checkboxes = [];
    for(var i = 0; i < inputs.length; i++){
        if((inputs[i].name === 'checkBoxGroup' || inputs[i].name === 'checkBoxGroup1') && inputs[i].value != ""){
            checkboxes.push(inputs[i]);
        }
    }
    for(var i = 0; i < checkboxes.length; i++){
        checkboxes[i].checked = true;
    }
    contactForm.submit();
}

function emailModalValidation(){
    var subject = document.getElementById('subject');
    var mailText = document.getElementById('mailText');
    if(subject.value.replace(/\s/g, "").length === 0){
        alert("Заполните тему сообщения");
        return false;
    }
    if(mailText.value.replace(/\s/g, "").length === 0){
        alert("Введите текст сообщения");
        return false;
    }
    document.getElementById('emailFormMdl').submit();
    invokeLoader();
}
function invokeLoader(){
    closeModal();
    var divBackground = document.createElement('div');
    var divLoader = document.createElement('div');
    divBackground.setAttribute('class', 'loaderBack');
    divLoader.setAttribute('class', 'loader');
    var body = document.getElementsByTagName('body')[0];
    divBackground.appendChild(divLoader);
    body.appendChild(divBackground);
}



function ValidateEmail(input)
{
    var mailformat = /^\w+([\+.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if(input.value.match(mailformat))
    {
        return true;
    }
    else
    {
        return false;
    }
}

