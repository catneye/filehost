/* 
 * Copyright (C) 2017 plintus
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

var websocket = null;
var runing = false;
var onmessagefuncs = [];
function socketinit(uri, onopen, onmessage, onerror, onclose) {

    websocket = {};
    console.log('Opening Ajax:' + uri);
    websocket.uri = uri;

    websocket.onerror = function (event) {
        onerror(event);
    };

    websocket.onopen = function (event) {
        onopen(event);
    };

    websocket.onmessage = function (event) {
        onmessage(event);
    };
    websocket.onclose = function (event) {
        onclose(event);
    };
    onopen();
}

function socketclose() {
}

function socketonmessage(onmessage) {
    websocket.onmessage = function (event) {
        onmessage(event);
    };
}

function socketsend(message) {
    console.log('WebSocket doSend');
    //console.log(message);
    var pars = "object=" + encodeURIComponent(message);
    //console.log(JSON.stringify(message));contentType:'text/html',
    var rq = new Ajax.Request(websocket.uri, {method: 'POST', parameters: pars,
        asynchronous: true, 
        onFailure: websocket.onerror,
        onComplete: websocket.onmessage});
}
function addOnMessage(response, funcptr) {
    console.log('addOnMessage for ' + response);
    var callback = {};
    callback.response = response;
    callback.funcptr = funcptr;
    onmessagefuncs.push(callback);
}
