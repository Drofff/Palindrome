<html>
<head>
    <title>Palindrome - Change Requests</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
    <style>
        .demo-card-square.mdl-card {
            width: 300px;
            height: 250px;
            float: left;
            margin: 1rem;
            position: relative;
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

            <div class="mdl-layout mdl-js-layout mdl-layout--fixed-drawer">
                <div class="mdl-layout__drawer">
                    <span class="mdl-layout-title" style="margin-top: 20px">
                        <img src="/api/resources/img/logo.png" width="150px" height="50px">
                    </span>
                    <nav class="mdl-navigation">
                        <a class="mdl-navigation__link" href="/">Головна</a>
                        <a class="mdl-navigation__link" href="/violation/police">Фіксовані порушення</a>
                        <a class="mdl-navigation__link" href="">Запити</a>
                        <a class="mdl-navigation__link" href="">Профіль</a>
                    </nav>
                </div>
                <main class="mdl-layout__content">
                    <div class="page-content">
                        <div style="margin-left: 7%; margin-top: 5%;">
                            <div class="demo-card-square mdl-card mdl-shadow--2dp">
                                <div onclick="window.location.href='/change-request/send'" class="mdl-card__supporting-text" style="cursor: pointer; text-align: center;">
                                    <h5>Новий запит</h5>
                                    <span class="material-icons" style="font-size: 100px; margin-top: 10px;">post_add</span>
                                </div>
                            </div>

                            <#list requests as request>
                                <div class="demo-card-square mdl-card mdl-shadow--2dp">
                                    <div class="mdl-card__supporting-text">
                                        <p>
                                            <img width="50px" height="50px" alt="" src="data:img/png;base64, ${request.targetOwner.photoUri}" style="border-radius: 50%; margin-right: 4%;" />
                                            ${request.targetOwner.firstName} ${request.targetOwner.lastName}
                                        </p>
                                        <p style="width: 70%; margin-top: 1%; height: 90px; margin-top: 30px;">
                                        <#if request.comment?? && request.comment?has_content>
                                            ${request.comment}
                                        <#else>
                                            Коментар відсутній
                                        </#if>
                                        </p>
                                    </div>
                                    <small style="margin-bottom: 10px; margin-left: 15px; color: gray; font-size: 10px;">
                                        <#if request.dateTime.hour lt 10>0${request.dateTime.hour}<#else>${request.dateTime.hour}</#if>:<#if request.dateTime.minute lt 10>0${request.dateTime.minute}<#else>${request.dateTime.minute}</#if> ${request.dateTime.dayOfMonth}/${request.dateTime.month.ordinal() + 1}/${request.dateTime.year?c}
                                    </small>
                                    <div class="mdl-card__menu">
                                        <button class="mdl-button mdl-button--icon mdl-js-button mdl-js-ripple-effect">
                                            <#if request.approved>
                                                <i class="material-icons" style="color: green;">done</i>
                                            <#else>
                                                <i class="material-icons" style="color: gray;">hourglass_empty</i>
                                            </#if>
                                        </button>
                                    </div>
                                </div>
                            </#list>
                        </div>
                        <div style="width: 100%; position: absolute; bottom: 60px;">
                            <div style="width: 320px; margin-left: auto; margin-right: auto; padding-top: 350px;">
                                <#include "pagination.ftl">
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

    var SENT_REQUESTS_URI = "/change-request/sent?page=";

    function next_page() {
        var page = ${page_number};
        var total_pages = ${pages_count};
        if(page < (total_pages - 1)) {
            var next_page = page + 1;
            window.location.href = SENT_REQUESTS_URI + next_page;
        }
    }

    function prev_page() {
        var page = ${page_number};
        if(page > 0) {
            var prev_page = page - 1;
            window.location.href = SENT_REQUESTS_URI + prev_page;
        }
    }

</script>
</body>
</html>