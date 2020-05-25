<html>
<head>
    <title>Palindrome - Violation Types</title>
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
            min-width: 220px;
            height: 275px;
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
            <span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome <font color="#ffd700">Admin</font></span>
            <a class="mdl-navigation__link" href="/" style="cursor: pointer">Головна</a>
            <a class="mdl-navigation__link" href="/admin/users" style="cursor: pointer">Користувачі</a>
            <a class="mdl-navigation__link" href="/admin/cars" style="cursor: pointer">Автомобілі</a>
            <a class="mdl-navigation__link" href="/change-request" style="cursor: pointer">Запити</a>
            <div class="mdl-layout-spacer"></div>
            <a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
        </div>
        <div class="mdl-layout__tab-bar mdl-js-ripple-effect">
            <a href="/admin/brand" class="mdl-layout__tab">Марки автомобілів</a>
            <a href="/admin/body-type" class="mdl-layout__tab">Типи кузова автомобілів</a>
            <a href="/admin/licence-category" class="mdl-layout__tab">Категорії автомобілів</a>
            <a href="/admin/department" class="mdl-layout__tab">Відділи поліції</a>
            <a href="/admin/violation-type" class="mdl-layout__tab is-active">Типи порушень</a>
            <a href="/admin/engine-type" class="mdl-layout__tab">Типи двигунів</a>
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


            <div style="margin-left: 7%; margin-top: 3%;">
                <a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" href="/admin/violation-type/create">
                    Створити
                </a>
            </div>

            <div style="margin-left: 9%; margin-top: 3%;">
                <#if violationTypes?size gt 0>
                    <ul class="demo-list-item mdl-list">
                        <#list violationTypes as violationType>
                            <div class="demo-card-square mdl-card mdl-shadow--2dp">
                                <div class="mdl-card__supporting-text" style="text-align: right">
                                    <span class="material-icons" onclick="window.location.href='/admin/violation-type/update/${violationType.id}'" style="cursor: pointer">
                                       create
                                   </span>
                                    <span class="material-icons" onclick="delete_violation_type('${violationType.id}', '${violationType.name}')" style="cursor: pointer; margin-left: 2%;">
                                        delete
                                    </span>
                                </div>
                                <div class="mdl-card__actions" style="min-height: 200px; text-align: center; margin-bottom: 15px; overflow-y: auto;">
                                    <h3>${violationType.name}</h3>
                                    <small>Штраф: ${violationType.fee.amount / 100} ${violationType.fee.currency}</small>
                                    <p style="width: 100%; height: 10px;"></p>
                                </div>
                            </div>
                        </#list>
                    </ul>
                <#else>
                    <h3>Тут нічого..</h3>
                </#if>
            </div>


        </div>
    </main>
</div>
<form action="/logout" method="post" id="logout">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>
<form method="post" id="delete-form">
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

    function delete_violation_type(id, name) {
        var result = window.confirm('Ви впевнені, що бажаєте видалити тип порушення \"' + name + "\"?");
        if(result) {
            var delete_uri = '/admin/violation-type/delete/' + id;
            var form = $("#delete-form");
            form.attr('action', delete_uri);
            form.submit();
        }
    }

</script>
</body>
</html>