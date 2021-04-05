<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>Файловое хранилище</title>
        <meta charset="UTF-8">
        <meta name="keywords" content="Файловое хранилище" />
        <meta name="description" content="Файловое хранилище" />
        <script type="text/javascript" src="./js/libs/prototype/prototype.js"></script>
        <script type="text/javascript" src="./js/libs/scriptaculous/scriptaculous.js"></script>
        <script type="text/javascript" src="./js/funcs.js"></script>
        <script type="text/javascript" src="./js/sockets.js"></script>
        <link rel="icon" href="images/favicon.png" type="image/x-icon">
        <link href='./css/site.css' rel='stylesheet' type='text/css'>
        <link href="https://fonts.googleapis.com/css?family=PT+Sans:400,400i,700,700i&subset=cyrillic" rel="stylesheet"> 
        <meta name="viewport" content="width=1200, initial-scale=2.0">
        <meta name="openstat-verification" content="b3e8f7a25324add067d649cca029483168a48d9c" />
    </head>
    <body onload="socketinit(getWsUri() + '/' + wsName, onOpen, onMessage, onError, onClose);" id="body">
        <%@include file="/jspf/vars.jspf" %> 
        <%@include file="/jspf/sockets.jspf" %>
        <div class="main">
            <div class="header">
            </div>
            <div class="topmenu">
            </div>
            <div class="workspace">
                <jsp:include page="/Router" />
            </div>
            <div class="footer">
            </div>
        </div>
        <jsp:include page="/SetIp" />
        <!--Openstat-->
        <span id="openstat1"></span>
        <script type="text/javascript">
            var openstat = {counter: 1, next: openstat};
            (function (d, t, p) {
                var j = d.createElement(t);
                j.async = true;
                j.type = "text/javascript";
                j.src = ("https:" == p ? "https:" : "http:") + "//openstat.net/cnt.js";
                var s = d.getElementsByTagName(t)[0];
                s.parentNode.insertBefore(j, s);
            })(document, "script", document.location.protocol);
        </script>
        <!--/Openstat-->
        <!-- Rating@Mail.ru counter -->
        <script type="text/javascript">
            var _tmr = window._tmr || (window._tmr = []);
            _tmr.push({id: "2914666", type: "pageView", start: (new Date()).getTime()});
            (function (d, w, id) {
                if (d.getElementById(id))
                    return;
                var ts = d.createElement("script");
                ts.type = "text/javascript";
                ts.async = true;
                ts.id = id;
                ts.src = (d.location.protocol == "https:" ? "https:" : "http:") + "//top-fwz1.mail.ru/js/code.js";
                var f = function () {
                    var s = d.getElementsByTagName("script")[0];
                    s.parentNode.insertBefore(ts, s);
                };
                if (w.opera == "[object Opera]") {
                    d.addEventListener("DOMContentLoaded", f, false);
                } else {
                    f();
                }
            })(document, window, "topmailru-code");
        </script>
        <noscript>
        <div>
            <img src="//top-fwz1.mail.ru/counter?id=2914666;js=na" style="border:0;position:absolute;left:-9999px;" alt="" />
        </div>
        </noscript>
        <!-- //Rating@Mail.ru counter -->
        <!-- Top100 (Kraken) Counter -->
        <script>
            (function (w, d, c) {
                (w[c] = w[c] || []).push(function () {
                    var options = {
                        project: 4498620
                    };
                    try {
                        w.top100Counter = new top100(options);
                    } catch (e) {
                    }
                });
                var n = d.getElementsByTagName("script")[0],
                        s = d.createElement("script"),
                        f = function () {
                            n.parentNode.insertBefore(s, n);
                        };
                s.type = "text/javascript";
                s.async = true;
                s.src =
                        (d.location.protocol == "https:" ? "https:" : "http:") +
                        "//st.top100.ru/top100/top100.js";

                if (w.opera == "[object Opera]") {
                    d.addEventListener("DOMContentLoaded", f, false);
                } else {
                    f();
                }
            })(window, document, "_top100q");
        </script>
        <noscript>
        <img src="//counter.rambler.ru/top100.cnt?pid=4498620" alt="Топ-100" />
        </noscript>
        <!-- END Top100 (Kraken) Counter -->
    </body>
</html>
