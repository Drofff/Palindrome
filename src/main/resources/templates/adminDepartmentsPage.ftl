<html>
<head>
    <title>Palindrome - Departments</title>
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
            width: 220px;
            min-height: 220px;
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
            <a class="mdl-navigation__link" href="/change-request" style="cursor: pointer">Запити</a>
            <div class="mdl-layout-spacer"></div>
            <a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
        </div>
        <div class="mdl-layout__tab-bar mdl-js-ripple-effect">
            <a href="/admin/brand" class="mdl-layout__tab">Марки автомобілів</a>
            <a href="/admin/body-type" class="mdl-layout__tab">Типи кузова автомобілів</a>
            <a href="/admin/licence-category" class="mdl-layout__tab">Категорії автомобілів</a>
            <a href="/admin/department" class="mdl-layout__tab is-active">Відділи поліції</a>
            <a href="/admin/violation-type" class="mdl-layout__tab">Типи порушень</a>
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
                <a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" href="/admin/department/create">
                    Створити
                </a>
            </div>

            <div style="margin-left: 9%; margin-top: 3%;">
                <#if departments?size gt 0>
                    <ul class="demo-list-item mdl-list">
                        <#list departments as department>
                            <div class="demo-card-square mdl-card mdl-shadow--2dp">
                                <div class="mdl-card__supporting-text" style="text-align: right">
                                    <span class="material-icons" onclick="window.location.href='/admin/department/update/${department.id}'" style="cursor: pointer">
                                       create
                                   </span>
                                    <span class="material-icons" onclick="delete_department('${department.id}', '${department.name}')" style="cursor: pointer; margin-left: 2%;">
                                        delete
                                    </span>
                                </div>
                                <div class="mdl-card__actions" style="min-height: 200px; text-align: center;">
                                    <h3>${department.name}</h3>
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

    function delete_department(id, name) {
        var result = window.confirm('Ви впевнені, що бажаєте видалити відділ \"' + name + "\"?");
        if(result) {
            var delete_uri = '/admin/department/delete/' + id;
            var form = $("#delete-form");
            form.attr('action', delete_uri);
            form.submit();
        }
    }

</script>
</body>
</html>