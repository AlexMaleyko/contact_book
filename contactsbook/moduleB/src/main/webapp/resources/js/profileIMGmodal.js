//PROFILE IMAGE MODAL

// Get the image and insert it inside the modal - use its "alt" text as a caption
var imgMaxSize = 3145728;//3MB
var initSrc;
document.addEventListener('DOMContentLoaded', function(){
    initSrc = document.getElementById('profileImg').src;
});
function callImgModal(){
    "use strict";
    var modal = document.getElementById('ImgModal');
    var img = document.getElementById('profileImg');
    var modalImg = document.getElementById("mdlImg");
    modal.style.display = "block";
    modalImg.src = img.src;
}

document.addEventListener('DOMContentLoaded', function(){
    document.getElementById('profileImg').addEventListener('click', callImgModal);
});
document.addEventListener('DOMContentLoaded', function(){
    document.getElementById('chooseImg').addEventListener('click', function(){
        document.getElementById('imgInput').click();
    });
});
function show(){
    var reader = new FileReader();
    reader.onload = function (e) {
        document.getElementById("mdlImg").src = e.target.result;
    }
}
function imgPreview(evt) {
    var oFile = document.getElementById('imgInput').files[0];
    var imgFilter = /^(image\/bmp|image\/gif|image\/jpeg|image\/png|image\/tiff)$/i;
    if (!imgFilter.test(oFile.type)) {
        alert("Файл, выбранный вами, не является изображением");
        document.getElementById("imgInput").value = "";
        return;
    }
    if (oFile.size > imgMaxSize) {
        alert("Вы можете выбрать файл размером до 3 мб");
        document.getElementById("imgInput").value = "";
        return;
    }
    totalUploadSize += oFile.size;
    if(totalUploadSize > maxTotalUploadSize){
        alert("Вы не можете загрузить это изображение, так как превышен лимит суммарного размера файлов: 50mb\n" +
            "Для этой загрузки вам доступно еще " +
            ((maxTotalUploadSize -(totalUploadSize - oFile.size))/1048576).toFixed(2) + " mb.");
        document.getElementById(lastAttachId+1+1000).value = '';
        document.getElementById("imgInput").value = "";
        return;
    }

    var files = evt.target.files;

    var f = files[0];
    var reader = new FileReader();
    reader.onload = (function(theFile) {
        return function(e) {
            // Render thumbnail.
            var span = document.createElement('span');
            document.getElementById('mdlImg').src = e.target.result;
        };
    })(f);
    reader.readAsDataURL(f);
}

document.addEventListener('DOMContentLoaded', function(){
    document.getElementById('imgInput').addEventListener('change', imgPreview, false);
});
//save chosen image
document.addEventListener('DOMContentLoaded', function(){
    document.getElementById('saveImg').addEventListener('click', function(){
        if(document.getElementById('imgInput').files[0].size > 0) {
            var img = document.getElementById('profileImg');
            img.src = document.getElementById('mdlImg').src;
            closeModal();
        }
        else{
            closeModal();
        }
    });
});
//cancel action on modal
document.addEventListener('DOMContentLoaded', function(){
    document.getElementById('cancelImg').addEventListener('click', function () {
            document.getElementById("imgInput").value = "";
            document.getElementById('profileImg').src = initSrc;
            closeModal();
        }
    );
});

document.addEventListener('DOMContentLoaded', function(){
    var image = document.getElementById('profileImg');
    var downloadingImage = new Image();
    downloadingImage.onload = function(){
        image.src = this.src;
    };
});