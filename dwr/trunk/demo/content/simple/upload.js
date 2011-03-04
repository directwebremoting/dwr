
function uploadFiles() {
  var image = dwr.util.getValue('uploadImage');
  var file = dwr.util.getValue('uploadFile');
  var color = dwr.util.getValue('color');

  UploadDownload.uploadFiles(image, file, color, function(data) {
    dwr.util.setValue('image', data);
  });

  /*
  UploadDownload.uploadFiles(image, file, color, {
    callback:function(data) {
      dwr.util.setValue('image', data);
    },
    exceptionHandler:function(ex) {
      alert("Error decoding input fields"); 
    }
  });
  */
}
