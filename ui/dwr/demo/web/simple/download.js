
function downloadPdfFile() {
  var pdftext = dwr.util.getValue('pdftext');

  UploadDownload.downloadPdfFile(pdftext, {
    callback: function(url) { dwr.engine.openInDownload(url); },
    async: false // workaround IE7/8's aggressive popup blocker
  });
}

function showPdfFile() {
  var pdftext = dwr.util.getValue('pdftext');

  UploadDownload.downloadPdfFile(pdftext, {
    callback: function(url) { window.location = url + "?contentDispositionType=inline"; },
    async: false // workaround IE7/8's aggressive popup blocker
  });
}
