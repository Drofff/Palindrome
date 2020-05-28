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
</head>
<body>
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="mdl-layout__header">
        <div class="mdl-layout__header-row">
            <span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome</span>
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

            <div class="mdl-grid" style="margin-top: 2%;">
                <div class="mdl-cell mdl-cell--4-col" style="margin-left: 7%;">

                    <h5>Інформація про застосунок</h5>

                    <hr style="height: 1px; width: 70%;" />

                    <#if userApp?? && userApp.id??><p>Ідентифікатор застосунку: ${userApp.id}</p></#if>

                    <form  method="post" action="/user-app/<#if userApp?? && userApp.id??>update/${userApp.id}"<#else>create"</#if>>

                        <p>
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <input class="mdl-textfield__input" autocomplete="off" <#if userApp?? && userApp.name??>value="${userApp.name}"</#if> id="name" name="name" type="text">
                            <label class="mdl-textfield__label" for="name">Назва застосунку</label>
                        </div>
                        </p>

                        <#if nameError??>
                            <p style="color: red">${nameError}</p>
                        </#if>

                        <p>
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <input class="mdl-textfield__input" autocomplete="off" <#if userApp?? && userApp.baseUrl??>value="${userApp.baseUrl}"</#if> id="baseUrl" name="baseUrl" type="text">
                            <label class="mdl-textfield__label" for="baseUrl">Базова адреса (example: http://example.com)</label>
                        </div>
                        </p>

                        <#if baseUrlError??>
                            <p style="color: red">${baseUrlError}</p>
                        </#if>

                        <p>
                        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                            <input class="mdl-textfield__input" autocomplete="off" <#if userApp?? && userApp.redirectUri??>value="${userApp.redirectUri}"</#if> id="redirectUri" name="redirectUri" type="text">
                            <label class="mdl-textfield__label" for="redirectUri">Відносний шлях переадресації (example: /exa/mple)</label>
                        </div>
                        </p>

                        <#if baseUrlError??>
                            <p style="color: red">${redirectUriError}</p>
                        </#if>


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