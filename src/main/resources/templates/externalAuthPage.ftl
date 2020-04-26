<html>
<head>
    <title>Palindrome - Two Step Authentication</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/79433ece4b.js" crossorigin="anonymous"></script>
    <style>
        .demo-card-wide.mdl-card {
            width: 700px;
        }
        .demo-card-wide > .mdl-card__title {
            color: #ffffff;
            height: 300px;
            background: url("https://info.nic.ua/wp-content/uploads/2018/05/2fa-infonic-1-1-e1525942921701.png") center / cover;
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

            <dialog class="mdl-dialog">
                <div class="mdl-dialog__content">
                    <p style="color: red" id="messages">
                        <#if error_message??>
                            ${error_message}
                        </#if>
                        <#if message??>
                            ${message}
                        </#if>
                    </p>
                    <div id="modal-text" style="text-align: center"></div>
                </div>
                <div class="mdl-dialog__actions">
                    <button type="button" id="close-modal" class="mdl-button close">Закрити</button>
                </div>
            </dialog>

            <div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 28%; margin-top: 5%;">
                <div class="mdl-card__title">
                    <h2 class="mdl-card__title-text" style="padding-top: 7px">Двох етапна авторизація</h2>
                </div>
                <#if !success??>
                    <form id="form-auth" class="mdl-card__supporting-text" action="/authenticate/external" method="post">
                        Спосіб підтвердження

                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <select class="mdl-textfield__input" id="optionId" name="optionId" required>
                                <option></option>
                                <#list options as option>
                                    <option value="${option.id}"><#if option.type.name() == "EMAIL">лист на пошту <#else>через пристрій </#if>${option.label}</option>
                                </#list>
                            </select>
                        </div>

                        <a onclick="authenticate()" style="margin-left: 70%;" class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored">
                            Авторизуватись
                        </a>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                    </form>
                    <div class="mdl-card__actions mdl-card--border" style="padding: 15px;">
                        Увага! Не закривайте це вікно до закінчення процесу авторизації
                    </div>
                <#else>
                    <div class="mdl-card__supporting-text">
                        <p><i class="fas fa-lock-open" style="color: green"></i> Авторизація успішно завершена</p>
                        <p><a href="/">Перейти до головної сторінки</a></p>
                    </div>
                </#if>
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
        <#if error_message?? || message??>
            dialog.showModal();
        </#if>

    });

    function authenticate() {
        $("#modal-text").html("<p>Підтвердіть вхід використовуючи обраний метод</p><p style='margin-top: 13px;'><i class='fas fa-spinner fa-spin' style='font-size: 40px'></i></p>");
        $("#close-modal").attr('hidden', true);
        $("#messages").html("");
        dialog.showModal();
        $("#form-auth").submit();
    }

</script>
</body>
</html>