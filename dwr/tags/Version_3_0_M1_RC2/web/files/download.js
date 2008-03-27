
function downloadPdfFile() {
  var pdftext = dwr.util.getValue('pdftext');

  UploadDownload.downloadPdfFile(pdftext, function(data) {
    window.open(data);
  });
}
