
function downloadPdfFile() {
  var pdftext = dwr.util.getValue('pdftext');

  UploadDownload.downloadPdfFile(pdftext, function(url) {
    dwr.engine.openInDownload(url);
  });
}

function showPdfFile() {
  var pdftext = dwr.util.getValue('pdftext');

  UploadDownload.downloadPdfFile(pdftext, function(url) {
    window.location = url + "?contentDispositionType=inline";
  });
}
