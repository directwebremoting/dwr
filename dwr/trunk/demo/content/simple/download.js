
function downloadPdfFile() {
  var pdftext = dwr.util.getValue('pdftext');

  UploadDownload.downloadPdfFile(pdftext, function(data) {
    dwr.engine.openInDownload(data);
  });
}
