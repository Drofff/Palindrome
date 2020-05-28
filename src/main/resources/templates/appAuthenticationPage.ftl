<html>
<head>
    <title>Palindrome - Authentication into ${userApp.name}</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
    <style>
        .demo-card-wide.mdl-card {
            width: 600px;
        }
        .demo-card-wide > .mdl-card__title {
            height: 300px;
            background: url("/api/resources/img/logo.png") center no-repeat;
        }
    </style>
</head>
<body>
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="mdl-layout__header">
        <div class="mdl-layout__header-row">
            <span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome</span>
            <div class="mdl-layout-spacer"></div>
        </div>
    </header>
    <main class="mdl-layout__content">
        <div class="page-content">

            <dialog class="mdl-dialog" style="z-index: 100000">
                <div class="mdl-dialog__content">
                        ${message}
                </div>
                <div class="mdl-dialog__actions">
                    <button type="button" class="mdl-button close">Закрити</button>
                </div>
            </dialog>

            <div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 27%; margin-top: 2%; margin-bottom: 2%;">
                <div class="mdl-card__title">
                    <h2 class="mdl-card__title-text">Авторизація в застосунок&nbsp;<b>${userApp.name}</b></h2>
                </div>
                <div class="mdl-card__supporting-text">
                    <p>${userApp.name} отримає інформацію про адресу вашої електронної скринька, ваше ПІБ та фото</p>
                    <form action="/user-app/${userApp.id}/authenticate" method="post">
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <input class="mdl-textfield__input" readonly onfocus="this.removeAttribute('readonly');" name="username" type="text" id="email">
                            <label class="mdl-textfield__label" for="email">Електронна пошта</label>
                        </div>
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <input class="mdl-textfield__input" readonly onfocus="this.removeAttribute('readonly');" name="password" type="password" id="password">
                            <label class="mdl-textfield__label" for="password">Пароль</label>
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <div>
                            <button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" type="submit">
                                Вхід
                            </button>
                            <a href="/forgot-password" style="color: cornflowerblue; margin-left: 10%;">Забули пароль?</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>
</div>
<script>
    var dialog = document.querySelector('dialog');
    dialog.querySelector('.close').addEventListener('click', function() {
        dialog.close();
    });
    $(function() {
        <#if message??>
            dialog.showModal();
        </#if>
    });
</script>
</body>
</html>