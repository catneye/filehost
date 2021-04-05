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
var onmessagefuncs = [];
function socketinit(uri, onopen, onmessage, onerror, onclose) {

    console.log('Opening WebSocket: ' + uri);
    websocket = new WebSocket(uri);

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

}

function socketclose() {
    websocket.close();
}

function socketonmessage(onmessage) {
    websocket.onmessage = function (event) {
        onmessage(event);
    };
}

function socketsend(message) {
    console.log('WebSocket doSend');
    if (websocket.readyState === 1) {
        websocket.send(message);
    } else {
        console.log('WebSocket is ' + websocket.readyState);
    }
}
function addOnMessage(response, funcptr) {
    var callback = {};
    callback.response = response;
    callback.funcptr = funcptr;
    onmessagefuncs.push(callback);
}
