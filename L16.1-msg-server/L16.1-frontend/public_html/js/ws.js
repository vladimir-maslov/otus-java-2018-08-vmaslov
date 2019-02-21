var webSocket;

var host = window.location.host;
var path = window.location.pathname;
var endPointURL = "ws://" + host + "/ws";

init = function (){
    connect();
}

function connect(){
    console.log(endPointURL);
    webSocket = new WebSocket(endPointURL);
    webSocket.onopen = function (event) {}
    webSocket.onmessage = function (event) {
        var resultDiv = document.getElementById("ws_result");
        resultDiv.innerHTML = event.data;
    }
    webSocket.onclose = function (event) {}
}

function sendGetUserByIdMessage(){
    var userId = document.getElementById("userId");
    var j = {"method": "getUserById", "userId": userId.value};

    var message = JSON.stringify(j);
    console.log(message);
    webSocket.send(message);
}

function sendAddUserMessage(){
    var userName = document.getElementById("userName");
    var userAge = document.getElementById("userAge");
    var j = {"method": "addUser", "name": userName.value, "age": userAge.value};

    var message = JSON.stringify(j);
    console.log(message);
    webSocket.send(message);
}