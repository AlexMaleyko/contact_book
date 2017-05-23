
//contactFORM.jsp
function callPhoneModal(button) {
    "use strict";
    var phoneModal=document.getElementById('phoneModal');
    if(button.id === "createPhoneBtn"){
        var inputs = phoneModal.getElementsByTagName('input');
        for(var i=0; i<inputs.length;i++){
            inputs[i].value = '';
        }
        phoneModal.getElementsByTagName('textarea')[0].value = '';
        document.getElementById("phoneModalName").innerHTML = "Добавить";
        document.getElementById('mdlSavePhBtn').style.display = 'block';
        document.getElementById('mdlUpdatePhBtn').style.display = 'none';
    }
    else {
        document.getElementById("phoneModalName").innerHTML = "Изменить";
        document.getElementById('mdlSavePhBtn').style.display = 'none';
        document.getElementById('mdlUpdatePhBtn').style.display = 'block';
        var phoneTable = document.getElementById("phoneTable");
        var tr = phoneTable.getElementsByTagName('tr');
        for(var i = 0; i<tr.length; i++){
            var rowInputs = tr[i].getElementsByTagName('input');
            for(var j = 0; j<rowInputs.length; j++){
                if(rowInputs[j].type.toLowerCase() === 'checkbox'){
                    if(rowInputs[j].checked){
                        document.getElementById('mdlCountryCode').value =
                            tr[i].getElementsByClassName('countryCode')[0].value;
                        document.getElementById('mdlOperatorCode').value =
                            tr[i].getElementsByClassName('operatorCode')[0].value;
                        document.getElementById('mdlNumber').value =
                            tr[i].getElementsByClassName('number')[0].value;
                        document.getElementById('mdlPhoneComment').value =
                            tr[i].getElementsByClassName('phoneComment')[0].value;
                        if(tr[i].getElementsByClassName('type')[0].value === 'm'){
                            document.getElementById('mdlM').selected = true;
                        }
                        else if(tr[i].getElementsByClassName('type')[0].value === 'h'){
                            document.getElementById('mdlH').selected = true;
                        }
                    }
                    break;
                }
            }
        }
    }
    phoneModal.style.display = "block";
}

function callAttachmentModal(button) {
    "use strict";
    var attachmentModal = document.getElementById('attachmentModal');
    if(button.id === "createAttachBtn"){
        attachmentModal.getElementsByTagName('input')[0].value = '';
        attachmentModal.getElementsByTagName('textarea')[0].value = '';
        document.getElementById("attachmentModalName").innerHTML = "Добавить";
        attachmentModal.getElementsByClassName("container")[0].style.display = "block";
        document.getElementById('mdlSaveAtchBtn').style.display = 'block';
        document.getElementById('mdlUpdateAtchBtn').style.display = 'none';
    }
    else {
        document.getElementById('mdlSaveAtchBtn').style.display = 'none';
        document.getElementById('mdlUpdateAtchBtn').style.display = 'block';
        document.getElementById("attachmentModalName").innerHTML = "Изменить";
        attachmentModal.getElementsByClassName("container")[0].style.display = "none";
        var attachTable = document.getElementById("attachTable");
        var tr = attachTable.getElementsByTagName('tr');
        for(var i = 0; i<tr.length; i++){
            var rowInputs = tr[i].getElementsByTagName('input');
            for(var j = 0; j<rowInputs.length; j++){
                if(rowInputs[j].type.toLowerCase() === 'checkbox'){
                    if(rowInputs[j].checked){
                        document.getElementById('mdlFileName').value =
                            tr[i].getElementsByClassName('fileName')[0].value;
                        document.getElementById('mdlAttachComment').value =
                            tr[i].getElementsByClassName('attachComment')[0].value;
                    }
                    break;
                }
            }
        }
    }
    attachmentModal.style.display = "block";

    if(document.getElementById(+lastAttachId + 1 + 1000) === null) {
        var newFileInput = document.createElement("input");
        newFileInput.setAttribute('type', 'file');
        newFileInput.setAttribute('name', 'uploadFile');
        newFileInput.id = lastAttachId + 1 + 1000;
        document.getElementById('fileInputs').appendChild(newFileInput);
    }
}

function updatePhoneRecord(){
    "use strict";
    //Validation
    var number = document.getElementById('mdlNumber');
    if(number.value.replace(/\s/g, "").length === 0){
        alert("Вы не ввели номер телефона");
        return;
    }
    if(!number.value.match(/^[0-9]*$/)){
        alert("Номер телефона должен состоять только из цифр");
        return;
    }
    if(!document.getElementById('mdlCountryCode').value.match(/^\+?\d*\s*$/)){
        alert("Код страны может состоять только из цифр и знака +.");
        return;
    }
    if(!document.getElementById('mdlOperatorCode').value.match(/^\s*\d*\s*$/)){
        alert("Код оператора может состоять только из цифр");
        return;
    }
    var phoneTable = document.getElementById("phoneTable");
    var tr = phoneTable.getElementsByTagName('tr');
    for(var i = 0; i<tr.length; i++){
        var rowInputs = tr[i].getElementsByTagName('input');
        for(var j = 0; j<rowInputs.length; j++){
            if(rowInputs[j].type.toLowerCase() === 'checkbox'){
                if(rowInputs[j].checked){
                    var td = tr[i].getElementsByTagName('td');
                    var v1 = document.getElementById('mdlCountryCode').value;
                    tr[i].getElementsByClassName('countryCode')[0].value = v1;

                    var v2 = document.getElementById('mdlOperatorCode').value;
                    tr[i].getElementsByClassName('operatorCode')[0].value = v2;

                    var v3 = document.getElementById('mdlNumber').value;
                    tr[i].getElementsByClassName('number')[0].value = v3;

                    td[1].getElementsByTagName('span')[0].innerHTML = v1 + v2 + v3;

                    var v4 = document.getElementById('mdlPhoneComment').value;
                    tr[i].getElementsByClassName('phoneComment')[0].value = v4;
                    td[3].getElementsByTagName('span')[0].innerHTML = v4;

                    var select = document.getElementById('mdlPhoneSel');
                    if(select.options[select.selectedIndex].value === 'm'){
                        tr[i].getElementsByClassName('type')[0].value = 'm';
                        td[2].getElementsByTagName('span')[0].innerHTML = 'm';
                    }
                    else{
                        tr[i].getElementsByClassName('type')[0].value = 'h';
                        td[2].getElementsByTagName('span')[0].innerHTML = 'h';
                    }
                }
                break;
            }
        }
    }
    closeModal();
}

function updateAttachRecord() {
    "use strict";
    //validation
    var fileName = document.getElementById('mdlFileName');
    if(fileName.value.replace(/\s/g, "").length === 0){
        alert('Имя файла не может быть пустым');
        return;
    }
    var attachTable = document.getElementById("attachTable");
    var tr = attachTable.getElementsByTagName('tr');
    for(var i = 0; i<tr.length; i++){
        var rowInputs = tr[i].getElementsByTagName('input');
        for(var j = 0; j<rowInputs.length; j++){
            if(rowInputs[j].type.toLowerCase() === 'checkbox'){
                if(rowInputs[j].checked){
                    var td = tr[i].getElementsByTagName('td');
                    var v1 = document.getElementById('mdlFileName').value;
                    tr[i].getElementsByClassName('fileName')[0].value = v1;
                    td[1].getElementsByTagName('span')[0].innerHTML = v1;

                    var v2 = document.getElementById('mdlAttachComment').value;
                    tr[i].getElementsByClassName('attachComment')[0].value = v2;
                    td[3].getElementsByTagName('span')[0].innerHTML = v2;
                }
                break;
            }
        }
    }
}

function newPhoneRecord(){
    "use strict";
    //Validation
    var number = document.getElementById('mdlNumber');
    if(number.value.replace(/\s/g, "").length === 0){
        alert("Вы не ввели номер телефона");
        return;
    }
    if(!number.value.match(/^[0-9]*$/)){
        alert("Номер телефона должен состоять только из цифр");
        return;
    }
    if(!document.getElementById('mdlCountryCode').value.match(/^\+?\d*\s*$/)){
        alert("Код страны может состоять только из цифр и знака +.");
        return;
    }
    if(!document.getElementById('mdlOperatorCode').value.match(/^\s*\d*\s*$/)){
        alert("Код оператора может состоять только из цифр");
        return;
    }
    var phoneTable = document.getElementById("phoneTable");
    var newTr = phoneTable.getElementsByTagName('tr')[1].cloneNode(true);//1st row is invisible and therefore never going to be deleted
    var v1 = document.getElementById('mdlCountryCode').value;
    var v2 = document.getElementById('mdlOperatorCode').value;
    var v3 = document.getElementById('mdlNumber').value;
    var td = newTr.getElementsByTagName('td');
    td[0].getElementsByTagName('input')[0].value = 0;

    td[1].getElementsByClassName('countryCode')[0].value = v1;
    td[1].getElementsByClassName('operatorCode')[0].value = v2;
    td[1].getElementsByClassName('number')[0].value = v3;
    td[1].getElementsByTagName('span')[0].innerHTML = v1 + v2 + +v3;

    var select = document.getElementById('mdlPhoneSel');
    if(select.options[select.selectedIndex].value === 'm'){
        td[2].getElementsByClassName('type')[0].value = 'm';
        td[2].getElementsByTagName('span')[0].innerHTML = 'm';
    }
    else if(select.options[select.selectedIndex].value === 'h'){
        td[2].getElementsByClassName('type')[0].value = 'h';
        td[2].getElementsByTagName('span')[0].innerHTML = 'h';
    }
    td[3].getElementsByTagName('span')[0].innerHTML =
        document.getElementById('mdlPhoneComment').value;
    td[3].getElementsByClassName('phoneComment')[0].value =
        document.getElementById('mdlPhoneComment').value;
    newTr.style.display = "table-row";
    phoneTable.getElementsByTagName('tbody')[0].appendChild(newTr);
    closeModal();
}

var lastAttachId = 1000;
var totalUploadSize = 0;
var maxTotalUploadSize = 52428800;
var maxFileUploadSize = 31457280;
function newAttachRecord(){
    "use strict";
    //VALIDATION
    var oFile = document.getElementById(lastAttachId+1+1000).files[0];
    if(oFile.size > maxFileUploadSize){
        alert("Превышен максимальный размер файла: 30mb.\n " +
            "Размер загружаемого вами файла: " + (oFile.size/1048576).toFixed(2) + " mb.");
        document.getElementById(lastAttachId+1+1000).value = '';
        return false;
    }
    totalUploadSize += oFile.size;
    if(totalUploadSize > maxTotalUploadSize){
        alert("Вы не можете загрузить этот файл, так как превышен лимит суммарного размера файлов: 50mb\n" +
            "Для этой загрузки вам доступно еще " +
            ((maxTotalUploadSize -(totalUploadSize - oFile.size))/1048576).toFixed(2) + " mb.");
        document.getElementById(lastAttachId+1+1000).value = '';
        totalUploadSize -= oFile.size;
        return false;
    }
    var fileName = document.getElementById('mdlFileName');
    if(fileName.value.replace(/\s/g, "").length === 0){
        fileName.value = oFile.name;
        alert('Будет использовано имя загружаемого файла');
    }
    var attachTable = document.getElementById("attachTable");
    var newTr = attachTable.getElementsByTagName('tr')[1].cloneNode(true);//1st row is invisible and therefore never going to be deleted
    var v1 = document.getElementById('mdlFileName').value;
    var v2 = document.getElementById('mdlAttachComment').value;
    var td = newTr.getElementsByTagName('td');
    td[0].getElementsByTagName('input')[0].value = 0;

    td[1].getElementsByClassName('fileName')[0].value = v1;
    td[1].getElementsByTagName('span')[0].innerHTML = v1;

    td[2].getElementsByTagName('span')[0].innerHTML = "Just now";

    td[3].getElementsByClassName('attachComment')[0].value = v2;
    td[3].getElementsByTagName('span')[0].innerHTML = v2;
    newTr.style.display = 'table-row';

    newTr.id = lastAttachId + 1;
    document.getElementById(+lastAttachId+1+1000).style.display = 'none';
    lastAttachId++;
    attachTable.getElementsByTagName('tbody')[0].appendChild(newTr);
    closeModal();

}



//INDEX.jsp

function callSearchModal(){
    "use strict";
    //when user calls the searchForm, reset its styles and fields to initial
    var searchModal = document.getElementById("searchModal");
    var inputs = searchModal.getElementsByTagName('input');
    for(var i = 0; i < inputs.length; i++){
        if(inputs[i].name == "isSameSearch"){
            continue;
        }
        inputs[i].value = '';
        inputs[i].style.borderColor = '#ccc';
    }
    document.getElementById("generalMsg").innerHTML = '';
    searchModal.style.display = "block";
}

function callEmailForm(){
    "use strict";
    var checkboxes = document.getElementsByName('checkBoxGroup');
    var checked = [];
    for(var i = 0; i < checkboxes.length; i++){
        if(checkboxes[i].checked){
            checked.push(checkboxes[i]);
        }
    }
    if(checked.length === 0){
        alert("Вы не выбрали ни одного получателя");
        return;
    }
    var withoutEmail = [];
    for(var i = 0; i < checked.length; i++){
        if(checked[i].parentElement.getElementsByClassName('hasEmail')[0].value === ""){
            withoutEmail.push(checked[i]);
        }
    }
    if(withoutEmail.length != 0){
        var message = "У следующих контактов не указан email:\n";
        for(var i = 0; i < withoutEmail.length; i++){
            //go to <tr> level and get span with name
            message +=
            withoutEmail[i].parentElement.parentElement.getElementsByTagName('span')[0].innerHTML +"\n";
        }
        alert(message);
        return;
    }
    var mailBoxList = document.getElementById('mailBoxList');
    var emailForm = document.getElementById('emailForm');
    var inputs = emailForm.getElementsByTagName('input');
    var textarea = emailForm.getElementsByTagName('textarea')[0];
    for(var i = 0; i < inputs.length; i++){
        inputs[i].value = '';
    }
    textarea.value = '';
    document.getElementById('pattern').value = '';
    while(mailBoxList.firstChild){
        mailBoxList.removeChild(mailBoxList.firstChild);
    }
    for(var i = 0; i < checked.length; i++){
        var input = document.createElement('input');
        input.setAttribute('type', 'hidden');
        input.setAttribute('name', 'recipient');
        input.value = checked[i].value;
        var div = document.createElement('div');
        div.appendChild(checked[i].parentElement.parentElement.
        getElementsByTagName('span')[0].cloneNode(true));
        mailBoxList.appendChild(input);
        mailBoxList.appendChild(div);
    }
    emailForm.style.display = 'block';
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    var modals = document.getElementsByClassName('modal');
    for(var j=0; j<modals.length;j++) {
        if (event.target === modals[j]) {
            for (var i = 0; i < modals.length; i++) {
                modals[i].style.display = "none";
            }
        }
    }
};

function closeModal() {
    var modals = document.getElementsByClassName('modal');
    for(var i=0; i<modals.length; i++){
        modals[i].style.display = "none";
    }
}
