<html>
<head>
    <title>Palindrome - Update Driver Info</title>
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
            width: 60%;
        }
        .demo-card-wide > .mdl-card__title {
            color: #fff;
            height: 200px;
            background: url('https://robbreportedit.files.wordpress.com/2019/04/icon-a5-b.jpg') center / cover;
        }
    </style>
</head>
<body>
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="mdl-layout__header">
        <div class="mdl-layout__header-row">
            <span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome <font color="#ffd700">Admin</font></span>
            <a class="mdl-navigation__link" href="/" style="cursor: pointer">Головна</a>
            <a class="mdl-navigation__link" href="/admin/users" style="cursor: pointer">Користувачі</a>
            <a class="mdl-navigation__link" href="/admin/cars" style="cursor: pointer">Автомобілі</a>
            <a class="mdl-navigation__link" href="/change-request" style="cursor: pointer">Запити</a>
            <a class="mdl-navigation__link" href="/admin/brand" style="cursor: pointer">Дані</a>
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

            <div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 22%; margin-top: 5%;">
                <div class="mdl-card__title">
                    <h2 class="mdl-card__title-text">Оновлення даних водія</h2>
                </div>
                <div class="mdl-card__supporting-text">
                    <form action="/admin/users/driver/${driver.id}/update" method="post">

                        <div>
                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" <#if driver.firstName??>value="${driver.firstName}"</#if> autocomplete="off" name="firstName" type="text">
                                <label class="mdl-textfield__label" <#if firstNameError??>style="color: red;"</#if> for="password">
                                    <#if firstNameError??>${firstNameError}<#else>Ім'я</#if>
                                </label>
                            </div>

                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" <#if driver.middleName??>value="${driver.middleName}"</#if> autocomplete="off" name="middleName" type="text">
                                <label class="mdl-textfield__label" for="password">По батькові</label>
                            </div>

                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" <#if driver.lastName??>value="${driver.lastName}"</#if> autocomplete="off" name="lastName" type="text">
                                <label class="mdl-textfield__label" <#if lastNameError??>style="color: red;"</#if> for="password">
                                    <#if lastNameError??>${lastNameError}<#else>Прізвище</#if>
                                </label>
                            </div>
                        </div>

                        <div>

                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" <#if driver.address??>value="${driver.address}"</#if> autocomplete="off" name="address" type="text">
                                <label class="mdl-textfield__label" <#if addressError??>style="color: red;" </#if> for="password">
                                    <#if addressError??>${addressError}<#else>Адреса</#if>
                                </label>
                            </div>

                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" <#if driver.licenceNumber??>value="${driver.licenceNumber}"</#if> autocomplete="off" name="licenceNumber" type="text">
                                <label <#if licenceNumberError??>style="color: red;" </#if> class="mdl-textfield__label" for="password">
                                    <#if licenceNumberError??>${licenceNumberError}<#else>Номер посвідчення водія</#if>
                                </label>
                            </div>

                        </div>

                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <div style="margin-top: 4%;">
                            <button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" type="submit">
                                Зберегти
                            </button>
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
        <#if error_message??>
        dialog.showModal();
        </#if>
    });
</script>
</body>
</html>