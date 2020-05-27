<html>
<head>
    <title>Palindrome - Home</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <link href="https://fonts.googleapis.com/css?family=Nunito&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@200&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/79433ece4b.js" crossorigin="anonymous"></script>
    <style>
        .admin-card {
            width: 220px;
            height: 20px;
            text-align: center;
            color: black;
        }
    </style>
</head>
<body style="background: url('https://images.unsplash.com/photo-1557804506-669a67965ba0?ixlib=rb-1.2.1') center / cover">
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="mdl-layout__header">
        <div class="mdl-layout__header-row">
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome <font color="#ffd700">Admin</font></span>
            <a class="mdl-navigation__link" href="/admin/users" style="cursor: pointer">Користувачі</a>
            <a class="mdl-navigation__link" href="/admin/cars" style="cursor: pointer">Автомобілі</a>
            <a class="mdl-navigation__link" href="/change-request" style="cursor: pointer">Запити</a>
            <a class="mdl-navigation__link" href="/admin/brand" style="cursor: pointer">Дані</a>
            <div class="mdl-layout-spacer"></div>
            <button id="demo-menu-lower-right" class="mdl-button mdl-js-button mdl-button--icon">
                <i class="material-icons">more_vert</i>
            </button>
            <ul class="mdl-menu mdl-menu--bottom-right mdl-js-menu mdl-js-ripple-effect" for="demo-menu-lower-right">
                <li class="mdl-menu__item" onclick="window.location.href='/change-password'">Змінити пароль</li>
                <li class="mdl-menu__item" onclick="$('#logout').submit()">Вихід</li>
            </ul>
        </div>
    </header>
    <main class="mdl-layout__content">
        <div class="page-content" style="display: flex; flex-flow: column; height: 100%;">

            <dialog class="mdl-dialog">
                <div class="mdl-dialog__content">
                    <p style="color: red">
                        <#if error_message??>
                            ${error_message}
                        </#if>
                    </p>
                    <p>
                        <#if message??>
                            ${message}
                        </#if>
                    </p>
                </div>
                <div class="mdl-dialog__actions">
                    <button type="button" class="mdl-button close">Закрити</button>
                </div>
            </dialog>

            <div class="mdl-grid" style="width: 70%; margin-left: auto; margin-right: auto; margin-top: 5%;">
                <div class="mdl-cell mdl-cell--3-col" style="margin-left: 6%;">
                    <div class="demo-card-square mdl-card mdl-shadow--2dp admin-card">
                        <div class="mdl-card__supporting-text" style="margin-top: 12%;">
                            <p><b style="font-size: 30px">${online_counter}</b></p>
                            <p>Користувачів онлайн</p>
                        </div>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--1-col"></div>
                <div class="mdl-cell mdl-cell--3-col">
                    <div class="demo-card-square mdl-card mdl-shadow--2dp admin-card">
                        <div class="mdl-card__supporting-text" style="margin-top: 12%;">
                            <p><b style="font-size: 30px">${users_count}</b></p>
                            <p>Зареєстровано користувачів в системі</p>
                        </div>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--1-col"></div>
                <div class="mdl-cell mdl-cell--3-col">
                    <div class="demo-card-square mdl-card mdl-shadow--2dp admin-card">
                        <div class="mdl-card__supporting-text" style="margin-top: 12%;">
                            <p><b style="font-size: 30px">${blocked_users_count}</b></p>
                            <p>Заблокованих користувачів</p>
                        </div>
                    </div>
                </div>
            </div>
            <div style="width: 100%; min-height: 300px; flex-grow : 1; background: white; margin-top: 13%; font-family: 'Nunito', sans-serif;">
                <br/>
                <br/>
                <p style="font-size: 40px; margin-left: 10%;">Вітаємо, ${user.username}!</p>
                <#if requests?? && requests gt 0>
                    <#if requests == 1>
                        <p style="font-size: 40px; margin-left: 10%;">1 запит очікує рішення</p>
                    <#else>
                        <p style="font-size: 40px; margin-left: 10%;">${requests} запитів очікують рішення</p>
                    </#if>
                <#else>
                    <p style="font-size: 40px; margin-left: 10%;">Наразі запити відсутні</p>
                </#if>
            </div>
        </div>
    </main>
</div>
<form action="/logout" method="post" id="logout">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>
<script>

    var dialog = document.querySelector('dialog');
    dialog.querySelector('.close').addEventListener('click', function() {
        dialog.close();
    });

    $(function() {
        <#if error_message?? || message??>
            dialog.showModal();
        </#if>
    });

</script>
</body>
</html>