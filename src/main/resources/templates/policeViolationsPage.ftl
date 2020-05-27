<html>
<head>
    <title>Palindrome - Violations</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.7.1/jquery.contextMenu.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.7.1/jquery.contextMenu.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.7.1/jquery.ui.position.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
    <style>
        .mdl-card__menu {
            color: #fff;
        }
    </style>
</head>
<body>
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="mdl-layout__header">
        <div class="mdl-layout__header-row">
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome <font color="#ffd700">Police</font>
			</span>
            <div class="mdl-layout-spacer"></div>
            <a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
        </div>
    </header>
    <main class="mdl-layout__content">
        <div class="page-content">

            <dialog class="mdl-dialog">
                <div class="mdl-dialog__content">
                    <p style="color: red">
                        <#if error_message??>
                            ${error_message}
                        </#if>
                    </p>
                </div>
                <div class="mdl-dialog__actions">
                    <button type="button" class="mdl-button close">Закрити</button>
                </div>
            </dialog>

            <div class="mdl-layout mdl-js-layout mdl-layout--fixed-drawer">
                <div class="mdl-layout__drawer">
                    <span class="mdl-layout-title" style="margin-top: 20px">
                        <img src="/api/resources/img/logo.png" width="150px" height="50px">
                    </span>
                    <nav class="mdl-navigation">
                        <a class="mdl-navigation__link" href="/">Головна</a>
                        <a class="mdl-navigation__link" href="">Фіксовані порушення</a>
                        <a class="mdl-navigation__link" href="/change-request/sent">Запити</a>
                        <a class="mdl-navigation__link" href="/change-password">Змінити пароль</a>
                        <a class="mdl-navigation__link" href="/police">Профіль</a>
                    </nav>
                </div>
                <main class="mdl-layout__content">
                    <div class="page-content">

                        <div class="mdl-grid" style="margin-top: 2%;">
                            <div class="mdl-cell mdl-cell--4-col" style="margin-left: 7%;">

                                <h5>Зафіксовані порушення</h5>

                                <hr style="height: 1px; width: 70%;" />

                                <div>
                                    <#list violations as violation>
                                        <div id="${violation.id}" class="demo-card-square contextual mdl-card mdl-shadow--2dp" style="margin-top: 6%; width: 400px; min-height: 115px;">
                                            <div class="mdl-card__supporting-text" style="margin-top: 30px; margin-bottom: 2%; text-align: center;
                                             width: 73%; margin-left: 9%;">
                                                "${violation.violationType.name}" - ${violation.violator.firstName} ${violation.violator.lastName}
                                            </div>
                                            <small style="margin-bottom: 10px; margin-left: 15px; color: gray; font-size: 10px;">
                                                <#if violation.dateTime.hour lt 10>0${violation.dateTime.hour}<#else>${violation.dateTime.hour}</#if>:<#if violation.dateTime.minute lt 10>0${violation.dateTime.minute}<#else>${violation.dateTime.minute}</#if> ${violation.dateTime.dayOfMonth}/${violation.dateTime.month.ordinal() + 1}/${violation.dateTime.year?c}
                                            </small>
                                            <div class="mdl-card__menu">
                                                <button class="mdl-button mdl-button--icon mdl-js-button mdl-js-ripple-effect">
                                                    <#if violation.paid>
                                                        <i class="material-icons" style="color: green;">done</i>
                                                    <#else>
                                                        <i class="material-icons" style="color: gray;">hourglass_empty</i>
                                                    </#if>
                                                </button>
                                            </div>
                                        </div>
                                    </#list>
                                    <div style="margin-left: 9%; margin-top: 2%">
                                        <#include "pagination.ftl">
                                    </div>
                                </div>

                            </div>
                            <div class="mdl-cell mdl-cell--1-col"></div>
                            <div class="mdl-cell mdl-cell--4-col">
                                <div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-top: 2%; width: 600px;">
                                    <div class="mdl-card__title">
                                        <h2 class="mdl-card__title-text">Реєстрація правопорушення</h2>
                                    </div>
                                    <div class="mdl-card__supporting-text">
                                        <form action="/violation/create" method="post">

                                            <p>
                                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                                <input class="mdl-textfield__input" autocomplete="off" id="carNumber" name="carNumber" type="text">
                                                <label class="mdl-textfield__label" for="carNumber">Номер автомобіля</label>
                                            </div>
                                            </p>

                                            <p>
                                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" id="location-field">
                                                <input class="mdl-textfield__input" id="location" name="location" type="text">
                                                <label class="mdl-textfield__label" id="location-label" for="location">Місце фіксування</label>
                                            </div>
                                            <a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" id="geo-button" onclick="get_location()" style="margin-left: 5%;">
                                                Зчитати геолокацію
                                            </a>
                                            </p>

                                            <p>
                                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                                <select class="mdl-textfield__input" id="violationTypeId" name="violationTypeId">
                                                    <option></option>
                                                    <#list violation_types as violation_type>
                                                        <option value="${violation_type.id}">${violation_type.name}</option>
                                                    </#list>
                                                </select>
                                                <label class="mdl-textfield__label" for="violationTypeId">Порушення</label>
                                            </div>
                                            </p>

                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                            <div style="margin-top: 4%;">
                                                <button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" type="submit">
                                                    Підтвердити
                                                </button>
                                            </div>

                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </main>
            </div>

        </div>
    </main>
</div>
<form action="/logout" method="post" id="logout">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>
<script>

    var geo_request_prefix = 'https://reverse.geocoder.ls.hereapi.com/6.2/reversegeocode.json?prox=';
    var geo_request_suffix = '&mode=retrieveAddresses&maxresults=1&gen=9&apiKey=cB0rD3P2fluVKeDwTAIGiRvrnHKhbXOkesVZn4Ht3zQ';

    $(function() {

       $.contextMenu({
           selector : '.contextual',
           callback : function (key, options) {
               if(key === 'export_key') {
                   window.location.href = '/export/violation/' + this[0].id;
               }
           },
           items : {
               export_key : {
                   name : "Export as PDF"
               }
           }
       });

    });

    function get_location() {
        if(navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(resolve_location);
        } else {
            window.alert('Ваш браузер не підтримує геопозиціювання');
        }
    }

    function resolve_location(position) {
        var latitude = position.coords.latitude;
        var longitude = position.coords.longitude;
        var coords = latitude + ',' + longitude;
        var geo_url = geo_request_prefix + coords + geo_request_suffix;
        $.ajax({
            type : 'GET',
            url : geo_url,
            success: parse_geo_response
        });
    }

    function parse_geo_response(data) {
        var address = data.Response.View[0].Result[0].Location.Address;
        var location = address.Label;
        location = add_to_address_if_present(address.PostalCode, location);
        location = add_to_address_if_present(address.Street, location);
        $("#location").val(location);
        $("#location-label").attr('hidden', true);
    }

    function add_to_address_if_present(part, address) {
        if(part) {
            return address + ', ' + part;
        }
        return address;
    }

    function next_page() {
        var page = ${page_number};
        var totalPages = ${pages_count};
        if(page < (totalPages - 1)) {
            var next_page = page + 1;
            window.location.href = "/violation/police?page=" + next_page;
        }
    }

    function prev_page() {
        var page = ${page_number};
        if(page > 0) {
            var prev_page = page - 1;
            window.location.href = "/violation/police?page=" + prev_page;
        }
    }

</script>
</body>
</html>