/* PHONE NUMBER TABLE*/
function checkAllPhones(mainCheckbox){
    var checkboxes = document.getElementsByName("checkBoxGroup");
    for (var i=0; i<checkboxes.length;i++) {
        checkboxes[i].checked = mainCheckbox.checked;
    }
}


var checkedBoxNumber = 0;

function doUpdate () {
    if (checkedBoxNumber != 1) {
        document.getElementById("updatePhoneBtn").disabled = true;
    }
    else {
        document.getElementById("updatePhoneBtn").disabled = false;
    }
    if(checkedBoxNumber === 0){
        document.getElementById("deletePhoneBtn").disabled = true;
    }
    else{
        document.getElementById("deletePhoneBtn").disabled = false;
    }
}
function manageUpdateButton(checkBox){
    if(checkBox.id === "mainCheckBox" && checkBox.checked === true){
        checkedBoxNumber = document.getElementsByName("checkBoxGroup").length - 1;//-1 for permanently hidden button
    }
    if(checkBox.id === "mainCheckBox" && checkBox.checked === false){
        checkedBoxNumber = 0;
    }
    if(checkBox.id != "mainCheckBox") {
        if (checkBox.checked) {
            checkedBoxNumber++;
        }
        else {
            checkedBoxNumber = checkedBoxNumber - 1;
            document.getElementById("mainCheckBox").checked=false;
        }
    }
    if (checkedBoxNumber != 1) {
        document.getElementById("updatePhoneBtn").disabled = true;
    }
    else {
        document.getElementById("updatePhoneBtn").disabled = false;
    }
    if(checkedBoxNumber === 0){
        document.getElementById("deletePhoneBtn").disabled = true;
    }
    else{
        document.getElementById("deletePhoneBtn").disabled = false;
    }
}


/*ATTACHMENT TABLE*/
function checkAllAttachs(mainCheckbox){
    var checkboxes = document.getElementsByName("checkBoxGroup1");
    for (var i=0; i<checkboxes.length;i++) {
        checkboxes[i].checked = mainCheckbox.checked;
    }
}

var checkedBoxNumber1 = 0;
function doUpdate1(){
    if (checkedBoxNumber1 != 1) {
        document.getElementById("updateAttachBtn").disabled = true;
    }
    else {
        document.getElementById("updateAttachBtn").disabled = false;
    }
    if(checkedBoxNumber1==0){
        document.getElementById("deleteAttachBtn").disabled = true;
    }
    else{
        document.getElementById("deleteAttachBtn").disabled = false;
    }
}
function manageUpdateButton1(checkBox){
    if(checkBox.id === "mCheckBox" && checkBox.checked === true){
        checkedBoxNumber1 = document.getElementsByName("checkBoxGroup1").length-1;
    }
    if(checkBox.id === "mCheckBox" && checkBox.checked === false){
        checkedBoxNumber1 = 0;
    }
    if(checkBox.id != "mCheckBox") {
        if (checkBox.checked) {
            checkedBoxNumber1++;
        }
        else {
            checkedBoxNumber1 = checkedBoxNumber1 - 1;
            document.getElementById("mCheckBox").checked = false;
        }
    }
    if (checkedBoxNumber1 != 1) {
        document.getElementById("updateAttachBtn").disabled = true;
    }
    else {
        document.getElementById("updateAttachBtn").disabled = false;
    }
    if(checkedBoxNumber1==0){
        document.getElementById("deleteAttachBtn").disabled = true;
    }
    else{
        document.getElementById("deleteAttachBtn").disabled = false;
    }
}

function deletePhones(){
    "use strict";
    var phoneTable = document.getElementById("phoneTable");
    var tableRows = phoneTable.getElementsByTagName('tr');
    var phonesContainer = document.getElementById('phones');
    for(var i = 0; i<tableRows.length; i++){
        var rowInputs = tableRows[i].getElementsByTagName('input');
        for(var j = 0; j<rowInputs.length; j++){
            if(rowInputs[j].type.toLowerCase() === 'checkbox'){
                if(rowInputs[j].checked){
                    tableRows[i].style.display = 'none';
                    rowInputs[j].checked = false;
                    var input = document.createElement('input');
                    input.setAttribute('type', 'hidden');
                    input.setAttribute('name', 'deletePhone');
                    input.setAttribute('value', rowInputs[j].value);
                    phonesContainer.appendChild(input);
                    //delete child nodes (hidden inputs are the target)
                    while(tableRows[i].firstChild){
                        tableRows[i].removeChild(tableRows[i].firstChild)
                    }
                    checkedBoxNumber--;
                    doUpdate();
                }
                break;
            }
        }
    }
    document.getElementById('deletePhoneBtn').disabled = true;
}

function deleteAttachs(){
    "use strict";
    var attachTable = document.getElementById("attachTable");
    var tableRows = attachTable.getElementsByTagName('tr');
    var attachContainer = document.getElementById('attachments');
    for(var i = 0; i<tableRows.length; i++){
        var rowInputs = tableRows[i].getElementsByTagName('input');
        for(var j = 0; j<rowInputs.length; j++){
            if(rowInputs[j].type.toLowerCase() === 'checkbox'){
                if(rowInputs[j].checked){
                    tableRows[i].style.display = 'none';
                    rowInputs[j].checked = false;
                    //create input which contains info about file checked for deletion
                    var input = document.createElement('input');
                    input.setAttribute('type', 'hidden');
                    input.setAttribute('name', 'deleteAttach');
                    input.setAttribute('value', rowInputs[j].value);
                    attachContainer.appendChild(input);
                    //delete hidded inputs with files
                    if(+rowInputs[j].value === 0){
                        document.getElementById('fileInputs').
                        removeChild(document.getElementById(+tableRows[i].id+1000));
                    }
                    //delete child nodes (hidden inputs)
                    while(tableRows[i].firstChild){
                        tableRows[i].removeChild(tableRows[i].firstChild)
                    }
                    checkedBoxNumber1--;
                    doUpdate1();
                }
                break;
            }
        }
    }
    document.getElementById('deleteAttachBtn').disabled = true;
}

/*
function callModal() {
    alert('hi');
    var modal = document.getElementById('myModal');
    modal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    var modal = document.getElementById('myModal');
    if (event.target == modal) {
        modal.style.display = "none";
    }
}*/
