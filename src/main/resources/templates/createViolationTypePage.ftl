<html>
<head>
    <title>Palindrome - Add Violation Type</title>
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
            color: #000000;
            height: 200px;
            background: url('https://images.unsplash.com/photo-1532101780307-8f873ece858f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80') center / cover;
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
            <div class="mdl-layout-spacer"></div>
            <button id="demo-menu-lower-right" class="mdl-button mdl-js-button mdl-button--icon">
                <i class="material-icons">more_vert</i>
            </button>
            <ul class="mdl-menu mdl-menu--bottom-right mdl-js-menu mdl-js-ripple-effect" for="demo-menu-lower-right">
                <li class="mdl-menu__item" onclick="window.location.href='/change-password'">Змінити пароль</li>
                <li class="mdl-menu__item" onclick="$('#logout').submit()">Вихід</li>
            </ul>
        </div>
        <div class="mdl-layout__tab-bar mdl-js-ripple-effect">
            <a href="/admin/brand" class="mdl-layout__tab">Марки автомобілів</a>
            <a href="/admin/body-type" class="mdl-layout__tab">Типи кузова автомобілів</a>
            <a href="/admin/licence-category" class="mdl-layout__tab">Категорії автомобілів</a>
            <a href="/admin/department" class="mdl-layout__tab">Відділи поліції</a>
            <a href="/admin/violation-type" class="mdl-layout__tab">Типи порушень</a>
            <a href="/admin/engine-type" class="mdl-layout__tab">Типи двигунів</a>
        </div>
    </header>
    <main class="mdl-layout__content">
        <div class="page-content">
            <div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 10%; margin-top: 2%;">
                <div class="mdl-card__title">
                    <h2 class="mdl-card__title-text">Додати тип порушення</h2>
                </div>
                <div class="mdl-card__supporting-text">
                    <form id="violation-type-form" action="/admin/violation-type/create" method="post">

                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <input class="mdl-textfield__input" <#if violationType?? && violationType.name??>value="${violationType.name}" </#if> autocomplete="off" id="name" name="name" type="text">
                            <label class="mdl-textfield__label" for="name">Назва типу порушення</label>
                        </div>

                        <p style="color: red;">
                            <#if nameError??>${nameError}</#if>
                            <#if error_message??>${error_message}</#if>
                        </p>

                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <input class="mdl-textfield__input" <#if violationType?? && violationType.feeAmount??>value="${(violationType.feeAmount / 100)?c}"
                                    </#if> autocomplete="off" id="feeAmount" name="feeAmount" type="number" step="0.01">
                            <label class="mdl-textfield__label" for="feeAmount">Розмір штрафу</label>
                        </div>

                        <p style="color: red;">
                            <#if amountError??>${amountError}</#if>
                        </p>

                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <select class="mdl-textfield__input" id="feeCurrency" name="feeCurrency">
                                <#list availableCurrencies as currency>
                                    <option <#if violationType?? && violationType.feeCurrency?? && violationType.feeCurrency == currency>selected</#if> value="${currency}">${currency}</option>
                                </#list>
                            </select>
                            <label class="mdl-textfield__label" for="feeCurrency">Валюта штрафу</label>
                        </div>

                        <#if currencyError??>
                            <p style="color: red">${currencyError}</p>
                        </#if>

                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <div style="margin-top: 4%;">
                            <a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="submit_form()">
                                Додати
                            </a>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </main>
</div>
<script>

    function submit_form() {
        var amountField = $("#feeAmount");
        if(amountField.val()) {
            var amount = amountField.val() * 100;
            amountField.val(amount);
        }
        $("#violation-type-form").submit();
    }

</script>
</body>
</html>