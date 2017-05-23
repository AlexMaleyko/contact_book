
function checkAll(mainCheckbox){
    var checkboxes=document.getElementsByName("checkBoxGroup");
    for (var i=0; i<checkboxes.length;i++) {
        checkboxes[i].checked=mainCheckbox.checked;
    }
}

var checkedBoxNumber = 0;
function manageUpdateDeleteButtons(checkBox){
    if(checkBox.id =="mainCheckBox" && checkBox.checked==true){
        checkedBoxNumber=document.getElementsByName("checkBoxGroup").length;
    }
    if(checkBox.id =="mainCheckBox" && checkBox.checked==false){
        checkedBoxNumber=0;
    }
    if(checkBox.id !="mainCheckBox") {
        if (checkBox.checked) {
            checkedBoxNumber++;
        }
        else {
            checkedBoxNumber = checkedBoxNumber - 1;
            document.getElementById("mainCheckBox").checked=false;// SWITCHES OFF THE MAIN CHECKBOX when one of checkboxes is unchecked
        }
    }
        if (checkedBoxNumber !== 1) {
            document.getElementById("updateContact").disabled = true;
        }
        else {
            document.getElementById("updateContact").disabled = false;
        }
        if(checkedBoxNumber==0){
            document.getElementById("deleteContact").disabled = true;
        }
        else{
            document.getElementById("deleteContact").disabled = false;
        }
}


function search() {
    var input, filter, table,tbody, tr, a, i;
    input = document.getElementById("searchInput");
    filter = input.value.toUpperCase();
    table = document.getElementById("contactTable");
    tbody=table.getElementsByTagName("tbody")[0];
    tr = tbody.getElementsByTagName("tr");

    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
        a = tr[i].getElementsByTagName("a")[0];
        if (a) {
            if (a.innerHTML.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}
document.addEventListener('DOMContentLoaded', function(){
    document.getElementById('pattern').addEventListener('change', function(){
        var select = document.getElementById('pattern');
        document.getElementById('mailText').value = select.options[select.selectedIndex].value;
        document.getElementById('hiddenPattern').value = select.options[select.selectedIndex].getAttribute('name');
        if(select.options[select.selectedIndex].getAttribute('name') != ""){
            document.getElementById('mailText').disabled = true;
        }
        else{
            document.getElementById('mailText').disabled = false;
        }
    });
});
function newPage (event) {
    document.getElementById('clickedPage').value
        = event.target.name;
    document.getElementById('paginationForm').submit();
}
document.addEventListener('DOMContentLoaded', function() {
    "use strict";
    var pageTotal = document.getElementById('pageTotal').value;
    var currentPage = document.getElementById('currentPage').value;
    var clientLimit = document.getElementById('clientLimit').value;
    var pagination = document.getElementById('pagination');
    var button;

    if(pageTotal == 0){
        pagination.innerHTML = "Данные отсутствуют";
    }
    else {
        for (var i = 0; i < pageTotal; i++) {
            button = document.createElement('button');
            button.setAttribute('type', 'submit');
            button.setAttribute('name', i);
            if (i == currentPage) {
                button.setAttribute('class', 'active');
            }
            else {
                button.setAttribute('class', 'paginationBtn');
            }
            button.value = i + 1;
            button.innerHTML = i + 1;
            pagination.appendChild(button);
        }
    }
    var paginationButtons = document.getElementsByClassName('paginationBtn');
    for(var i = 0; i < paginationButtons.length; i++){
        paginationButtons[i].addEventListener('click', newPage, false);
    }
    document.getElementById('pageNum').value = clientLimit;
    document.getElementById('pageNum').addEventListener('change', function () {
        document.getElementById('clientLimit').value
            = document.getElementsByName('pageNum')[0].value;
        document.getElementById('paginationForm').submit();
    })
});
document.addEventListener('DOMContentLoaded', function(){
    if(document.getElementById("isSearchPage").value == 1){
        document.getElementById("backToList").style.display = "block";
    }
});
/*
function addCheckBoxListener(){
    alert("dfd");
    var checkboxes=document.getElementsByName("checkBoxGroup");
    for (var i=0; i<checkboxes.length;i){
        checkboxes[i].addEventListener("change",manageUpdateButton);
    }
}

document.onload = addCheckBoxListener();
*/
