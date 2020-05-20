<html>
<head>
    <title>Palindrome - Find <#if name??>${name}<#else>Driver</#if></title>
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
    <style>
        .demo-list-three {
            width: 650px;
        }
    </style>
</head>
<body style="background: url('https://images.unsplash.com/photo-1502240868472-18259bc0f863?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80') center / cover">
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
            <div class="mdl-layout mdl-js-layout mdl-layout--fixed-drawer">
                <div class="mdl-layout__drawer">
                    <span class="mdl-layout-title" style="margin-top: 20px">
                        <img src="/api/resources/img/logo.png" width="150px" height="50px">
                    </span>
                    <nav class="mdl-navigation">
                        <a class="mdl-navigation__link" href="/">Головна</a>
                        <a class="mdl-navigation__link" href="/violation/police">Фіксовані порушення</a>
                        <a class="mdl-navigation__link" href="/change-request/sent">Запити</a>
                        <a class="mdl-navigation__link" href="/police">Профіль</a>
                    </nav>
                </div>
                <main class="mdl-layout__content">
                    <div class="page-content">
                        <div style="margin: 2% 5% 0 5%; background-color: white; width: 90%; min-height: 80%;">
                            <p style="width: 100%; height: 5%;"></p>
                            <form action="" method="get" style="width: 70%; margin-left: auto; margin-right: auto;">

                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                    <input class="mdl-textfield__input" <#if name??>value="${name}"</#if> name="name" type="text" id="name">
                                    <label class="mdl-textfield__label" for="name">ПІБ водія</label>
                                </div>

                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" style="margin-left: 3%;">
                                    <input class="mdl-textfield__input" <#if pattern.address??>value="${pattern.address}"</#if> name="address" type="text" id="address">
                                    <label class="mdl-textfield__label" for="address">Адреса</label>
                                </div>

                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" style="margin-left: 3%;">
                                    <input class="mdl-textfield__input" <#if pattern.licenceNumber??>value="${pattern.licenceNumber}"</#if> name="licenceNumber" type="text" id="licenceNumber">
                                    <label class="mdl-textfield__label" for="licenceNumber">Номер посвідчення водія</label>
                                </div>

                                <button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored"
                                        style="margin-left: 90%;" type="submit">
                                    Пошук
                                </button>

                            </form>

                            <#if drivers?size == 0>
                                <h4 style="margin-left: 15%;">Нічого не знайдено</h4>
                            </#if>

                            <ul class="demo-list-three mdl-list" style="margin-left: 15%; margin-top: 5%;">
                                <#list drivers as driver>
                                    <li class="mdl-list__item mdl-list__item--three-line">
                                        <span class="mdl-list__item-primary-content">
                                          <img src="data:img/png;base64, ${driver.photoUri}" alt="" width="40px" height="40px" class="material-icons mdl-list__item-avatar">
                                          <span>${driver.firstName} <#if driver.middleName??>${driver.middleName}</#if> ${driver.lastName}</span>
                                          <span class="mdl-list__item-text-body">
                                            Адреса: ${driver.address} <br/>
                                            Номер посвідчення водія: ${driver.licenceNumber}
                                          </span>
                                        </span>
                                        <span class="mdl-list__item-secondary-content">
                                          <a class="mdl-list__item-secondary-action" href="/driver/${driver.id}/statistic"><i class="material-icons" style="color: black">assessment</i></a>
                                        </span>
                                    </li>
                                </#list>
                            </ul>

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