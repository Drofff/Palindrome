<html>
<head>
    <title>Palindrome - Profile</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
    <style>
        .demo-card-image.mdl-card {
            width: 256px;
            height: 256px;
            background: url('data:image/png;base64, ${police.photoUri}') center / cover;
        }
        .demo-card-image {
            height: 52px;
            padding: 16px;
            background: rgba(0, 0, 0, 0.2);
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
                        <a class="mdl-navigation__link" href="/change-request/sent">Запити</a>
                        <a class="mdl-navigation__link" href="">Профіль</a>
                    </nav>
                </div>
                <main class="mdl-layout__content">
                    <div class="page-content">

                        <dialog class="mdl-dialog" style="z-index: 100000">
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

                        <div class="mdl-grid" style="margin-top: 3%;">
                            <div class="mdl-cell mdl-cell--2-col">
                            </div>
                            <div class="mdl-cell mdl-cell--3-col">
                                <div class="demo-card-image mdl-card mdl-shadow--2dp">
                                    <div class="mdl-card__title mdl-card--expand"></div>
                                </div>
                            </div>
                            <div class="mdl-cell mdl-cell--3-col" style="font-size: 20px">
                                <b>${police.firstName} <#if police.middleName??>${police.middleName}</#if> ${police.lastName}</b>
                            </div>
                            <div class="mdl-cell mdl-cell--3-col"></div>
                        </div>
                        <div class="mdl-grid">
                            <div class="mdl-cell mdl-cell--2-col">
                            </div>
                            <div class="mdl-cell mdl-cell--6-col">
                                <div>
                                    <p><b>Електронна пошта:</b> ${email}</p>
                                    <p><b>Посада:</b> ${police.position}</p>
                                    <p><b>Номер жетона:</b> ${police.tokenNumber}</p>
                                    <p><b>Відділення:</b> ${department.name}</p>
                                </div>
                                <div style="margin-top: 2%;">
                                    <a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" href="/police/update">
                                        Редагувати
                                    </a>
                                    <a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" href="/police/update/photo">
                                        Змінити фото
                                    </a>
                                </div>
                            </div>
                        </div>

                    </div>
                </main>
            </div>
        </div>
    </main>
    <form action="/logout" method="post" id="logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    </form>
</div>
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