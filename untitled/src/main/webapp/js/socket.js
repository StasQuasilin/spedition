let protocol = 'ws://';
let home = window.location.host;
let address = '/socket';
let subscribes = {};
let connection = new WebSocket(protocol + home + context + address, );
connection.onopen = function(){
    console.log('Connection open')
};
connection.onerror = function () {
    console.log('Connection err')
};
connection.onmessage = function(event){
    let json = JSON.parse(event.data);
    let type = json['type'];
    let data = json['data'];
    let subscribe = subscribes[type];
    if (typeof subscribe === 'function') {
        subscribe(data, type);
    } else{
        console.log('Subscribe \'' + type + '\' is ' + typeof subscribes[type]);
    }
};
function subscribe(subscribe, handler) {
    subscribes[subscribe] = handler;
    send(JSON.stringify({
        action:'subscribe',
        subscribe:subscribe,
        user:userId
    }))
}
function unsubscribe(subscribe) {
    delete subscribes[subscribe];
    send(JSON.stringify({
        action:'unsubscribe',
        subscribe:subscribe
    }))
}
function send(message) {
    if (connection.readyState === WebSocket.OPEN){
        connection.send(message);
    } else if (connection.readyState === WebSocket.CONNECTING){
        setTimeout(function () {
            send(message)
        }, 500);
    }
}