<html>
<head>
    <title>Palindrome - Update Police Info</title>
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
            background: url('https://dzdqvx0schpv8.cloudfront.net/wp-content/uploads/2019/06/employer-with-magnifying-glass-exploring-application-papers-vector-id1073831676-1200x565.jpg') center / cover;
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
                    <h2 class="mdl-card__title-text" style="color: black;">Оновлення даних поліцейського</h2>
                </div>
                <div class="mdl-card__supporting-text">
                    <form action="/admin/users/police/${police.id}/update" method="post">

                        <div>
                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" <#if police.firstName??>value="${police.firstName}"</#if> autocomplete="off" name="firstName" type="text">
                                <label class="mdl-textfield__label" <#if firstNameError??>style="color: red;"</#if> for="password">
                                    <#if firstNameError??>${firstNameError}<#else>Ім'я</#if>
                                </label>
                            </div>

                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" <#if police.middleName??>value="${police.middleName}"</#if> autocomplete="off" name="middleName" type="text">
                                <label class="mdl-textfield__label" for="password">По батькові</label>
                            </div>

                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" <#if police.lastName??>value="${police.lastName}"</#if> autocomplete="off" name="lastName" type="text">
                                <label class="mdl-textfield__label" <#if lastNameError??>style="color: red;"</#if> for="password">
                                    <#if lastNameError??>${lastNameError}<#else>Прізвище</#if>
                                </label>
                            </div>
                        </div>

                        <div>

                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" <#if police.position??>value="${police.position}"</#if> autocomplete="off" name="position" type="text">
                                <label class="mdl-textfield__label" <#if positionError??>style="color: red;" </#if> for="password">
                                    <#if positionError??>${positionError}<#else>Посада</#if>
                                </label>
                            </div>

                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" <#if police.tokenNumber??>value="${police.tokenNumber}"</#if> autocomplete="off" name="tokenNumber" type="text">
                                <label <#if tokenNumberError??>style="color: red;" </#if> class="mdl-textfield__label" for="password">
                                    <#if tokenNumberError??>${tokenNumberError}<#else>Номер жетона</#if>
                                </label>
                            </div>

                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <select class="mdl-textfield__input" id="departmentId" name="departmentId">
                                    <option></option>
                                    <#list departments as department>
                                        <option <#if police.departmentId?? && police.departmentId == department.id>selected</#if> value="${department.id}">${department.name}</option>
                                    </#list>
                                </select>
                                <label class="mdl-textfield__label" <#if departmentIdError??>style="color: red;"</#if> for="licenceCategoryId">
                                    <#if departmentIdError??>${departmentIdError}<#else>Відділення</#if>
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