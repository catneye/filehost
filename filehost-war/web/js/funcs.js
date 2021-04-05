function randomString(string_length) {
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
    var randomstring = '';
    for (var i = 0; i < string_length; i++) {
        var rnum = Math.floor(Math.random() * chars.length);
        randomstring += chars.substring(rnum, rnum + 1);
    }
    return randomstring;
}
function dateToYMD(date)
{
    var d = date.getDate();
    var m = date.getMonth() + 1;
    var y = date.getFullYear();
    return '' + y + '-' + (m <= 9 ? '0' + m : m) + '-' + (d <= 9 ? '0' + d : d);
}

function posMeCenter(element) {
    var h = window.height;
    var w = window.width;
    element.style.width = (innerWidth - 40) + 'px';
    element.style.height = (innerHeight - 40) + 'px';
    element.style.marginTop = "-" + (innerHeight / 2 - 20) + 'px';
    element.style.marginLeft = "-" + (innerWidth / 2 - 20) + 'px';
}

function noop() {
}

function newcaptcha(obj) {
    obj.src = './Captcha/captcha.png?rand=' + Math.random();
}

function removeOptions(selectbox)
{
    var i;
    for (i = selectbox.options.length - 1; i >= 0; i--)
    {
        selectbox.remove(i);
    }
}

function sortSelect(selElem) {
    var tmpAry = new Array();
    for (var i = 0; i < selElem.options.length; i++) {
        tmpAry[i] = new Array();
        tmpAry[i][0] = selElem.options[i].text;
        tmpAry[i][1] = selElem.options[i].value;
    }
    tmpAry.sort();
    while (selElem.options.length > 0) {
        selElem.options[0] = null;
    }
    for (var i = 0; i < tmpAry.length; i++) {
        var op = new Option(tmpAry[i][0], tmpAry[i][1]);
        selElem.options[i] = op;
    }
    return;
}

function showMenu(idmenu) {
    arrmenus = $$('div[id^="floatmenu"]');
    //arrmenus.forEach(function (element, index, array) {
    //    element.hide();
    //});

    arrmenus.each(function (element, index, array) {
        element.hide();
    });

    if (idmenu) {
        $(idmenu).show();
    }
}

function FileSlicer(file) {
    this.sliceSize = 1024 * 10;
    this.slices = Math.ceil(file.size / this.sliceSize);
    this.currentSlice = 0;
    this.getNextSlice = function () {
        var start = this.currentSlice * this.sliceSize;
        var end = Math.min((this.currentSlice + 1) * this.sliceSize, file.size);
        ++this.currentSlice;

        return file.slice(start, end);
    };
}

var selectedrow = null;
var selectedstyle = null;

function selectrow(obj) {
    if (touchedrow === null) {
        if (selectedrow !== null) {
            selectedrow.style.backgroundColor = selectedstyle;
            selectedrow = null;
            selectedstyle = null;
        }
        selectedstyle = obj.style.backgroundColor;
        selectedrow = obj;
        obj.style.backgroundColor = 'lightsalmon';
    }
}

function touchrow(id, obj) {
    if ((obj.parentNode.touchedrow) && (obj.parentNode.touchedrow !== null)) {
        obj.parentNode.touchedrow.style.backgroundColor = obj.parentNode.touchedstyle;
        obj.parentNode.touchedrow = null;
        obj.parentNode.touchedstyle = null;
    }
    obj.parentNode.touched = id;
    obj.parentNode.touchedstyle = obj.style.backgroundColor;
    obj.parentNode.touchedrow = obj;
    obj.style.backgroundColor = 'salmon';
}

function showHint(text, color) {
    var div = $("hintframe");
    while (div.firstChild) {
        div.removeChild(div.firstChild);
    }
    var div0 = document.createElement("div");
    div.appendChild(div0);
    div0.innerHTML = text;
    div0.setStyle({'color': color, width: '100%', 'text-align': 'center', opacity: '1'});
    div.show();
    new Effect.Shake(div, {duration: 0.5});
    hideHintTimeout();
}

function hideHintTimeout() {
    var div = $("hintframe");
    setTimeout(function () {
        new Effect.Fade(div, {duration: 1.0});
    }, 5000);
}

function bindDomElemToObjProp(element, obj, propertyName, after, before) {
    element.observe('change', function (event) {
        if (before) {
            before(element, obj, propertyName, event);
        }
        if (element.tagName.toUpperCase() === "SELECT") {
            obj[propertyName] = element.options[element.selectedIndex].value;
        } else if (element.tagName.toUpperCase() === "TEXTAREA") {
            obj[propertyName] = element.value;
        } else if (element.tagName.toUpperCase() === "INPUT") {
            if (element.readAttribute('type').toUpperCase() === 'CHECKBOX') {
                obj[propertyName] = element.checked;
            } else {
                obj[propertyName] = element.value;
            }
        }
        if (after) {
            after(element, obj, propertyName, event);
        }
    });
}

function createFormElement(form, val, desc, sels) {
    var div = document.createElement("div");
    div.addClassName('editformline');
    var title = document.createElement("div");
    title.innerHTML = desc.title;
    title.addClassName('editformtitle');
    div.appendChild(title);
    form.appendChild(div);
    var field = null;

    //if (Array.isArray(sels)) {
    if (!sels) {
        sels = {};
    }
    if (sels.hasOwnProperty(desc.name)) {
        var sel = sels[desc.name];
        field = document.createElement("select");
        for (var i = 0; i < sel.length; i++) {
            var option = document.createElement("option");
            option.value = sel[i].id;
            option.text = sel[i].name;
            if (sel[i].id === 0) {
                //option.disabled=true;
            }
            field.appendChild(option);
        }
        div.appendChild(field);
        field.value = val;
    } else {
        switch (desc.type) {
            case 'boolean':
                field = document.createElement("input");
                field.setAttribute("type", "checkbox");
                div.appendChild(field);
                field.checked = val;
                break;
            case 'integer':
                field = document.createElement("input");
                field.setAttribute("type", "text");
                div.appendChild(field);
                field.value = val;
                break;
            case 'string':
                field = document.createElement("input");
                field.setAttribute("type", "text");
                div.appendChild(field);
                field.value = val;
                break;
            case 'text':
                field = document.createElement("textarea");
                div.appendChild(field);
                field.cols = "40";
                field.rows = "10";
                field.setStyle({'height': '150px'});
                field.value = val;
                break;
            case 'date':
                field = document.createElement("input");
                field.setAttribute("type", "text");
                div.appendChild(field);
                field.value = val;
                //field.observe('click', function (event) {
                //    NewCssCal(field.id, "yyyyMMdd", "dropdown", true, "24", true);
                //    return false;
                //});
                var calbtn = document.createElement("span");
                calbtn.setStyle({'width': '14px', 'height': '14px', 'border': 'solid lightgray 1px'});
                calbtn.innerHTML = "&hellip;";
                div.appendChild(calbtn);
                new Calendar({
                    outputFields: [field],
                    onHideCallback: function (date, e) {
                        //field.value=date;
                        var event = new Event('change');
                        field.dispatchEvent(event);
                    },
                    //dateField: field,
                    parentElement: div,
                    popupTriggerElement: calbtn,
                    withTime: true,
                    hideOnClickElsewhere: true,
                    hideOnClickOnDay: true,
                    language: 'ru'
                });
                break;
        }
    }
    if (field !== null) {
        field.id = randomString(10);
        field.addClassName('editformfield');
        if (!desc.iseditable) {
            field.disabled = true;
        } else {
        }
        return field.id;
    }
}

if (typeof console == "undefined") {
    this.console = {
        log: function (msg) {
            return;
        }
    };
}