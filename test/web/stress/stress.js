
function init() {
  clientFrenzyEle = dwr.util.byId("clientFrenzy");
  dwr.util.setValue("clientHitDelay", clientHitDelay);
  dwr.util.setValue("clientFrenzy", false);
}

var clientRampInProgress = false;
var clientTimeout;
var clientHitDelay = 1000;
var clientSent = 0;
var clientReply = 0;
var clientOutstanding = 0;
var clientSkipped = 0;
var clientFrenzyEle;

function clientRamp() {
  if (clientRampInProgress) {
    // STOP
    if (clientTimeout) clearTimeout(clientTimeout);
    clientRampInProgress = false;
    dwr.util.setValue("clientRamp", "Start");
  }
  else {
    // START
    dwr.util.setValue("clientRamp", "Stop");
    clientRampInProgress = true;
    clientSent = 0;
    clientReply = 0;
    clientSkipped = 0;
    clientPoll();
  }
}

function clientPoll() {
  if (clientFrenzyEle.checked) {
    if (clientHitDelay > 500) {
      clientHitDelay -= 50;
    }
    else if (clientHitDelay > 400) {
      clientHitDelay -= 10;
    }
    else if (clientHitDelay > 300) {
      clientHitDelay -= 5;
    }
    else if (clientHitDelay > 200) {
      clientHitDelay -= 2;
    }
    else if (clientHitDelay > 0) {
      clientHitDelay -= 1;
    }
    dwr.util.setValue("clientHitDelay", clientHitDelay);
  }
  clientSent++;

  dwr.util.setValue("clientSent", clientSent);
  dwr.util.setValue("clientOutstanding", clientOutstanding);

  if (clientOutstanding > 30) {
    clientSkipped++;
    dwr.util.setValue("clientSkipped", clientSkipped);
  }
  else {
    Stress.ping(clientServerReply);
  }

  clientTimeout = setTimeout(clientPoll, clientHitDelay);
}

function clientServerReply() {
  clientReply++;
  clientOutstanding = clientSent - clientReply;
  dwr.util.setValue("clientReply", clientReply);
  dwr.util.setValue("clientOutstanding", clientOutstanding);
}

function clientAdjustHitDelay() {
  clientHitDelay = dwr.util.getValue("clientHitDelay");
}

function clientIgnoreOutstanding() {
  clientReply = clientSent; 
  clientOutstanding = clientSent - clientReply;
  dwr.util.setValue("clientReply", clientReply);
  dwr.util.setValue("clientOutstanding", clientOutstanding);
}



var serverRampInProgress = false;
var serverTimeout;
var serverReply = 0;
var serverErrors = 0;
var serverFrenzyEle;

function serverRamp() {
  if (serverRampInProgress) {
    // STOP
    serverRampInProgress = false;
    if (serverTimeout) clearTimeout(serverTimeout);
    dwr.engine.setActiveReverseAjax(false);
    Stress.setPublishing(false);
    dwr.util.setValue("serverRamp", "Start");
  }
  else {
    // START
    serverRampInProgress = true;
    Stress.setPublishing(true);
    dwr.engine.setActiveReverseAjax(true);
    dwr.util.setValue("serverRamp", "Stop");

    serverReply = 0;
  }
}

function serverAdjustHitDelay() {
  var hitDelay = dwr.util.getValue("serverHitDelay");
  Stress.setHitDelay(hitDelay);
}

function serverIgnoreErrors() {
}

function serverPing(serverPings) {
  if (serverReply != serverPings) {
    serverErrors++;
    dwr.util.setValue("serverErrors", serverErrors);
  }
  serverReply++;
  dwr.util.setValue("serverReply", serverReply);
}

