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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.css">
    <script src="https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.js"></script>
    <style>
        .ct-series-a .ct-line {
            stroke: dodgerblue;
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
                            <p style="height: 7%; width: 100%;"></p>
                            <div class="mdl-grid" style="width: 90%; margin-left: auto; margin-right: auto;">
                                <div class="mdl-cell mdl-cell--1-col">
                                </div>
                                <div id="violations-chart" class="mdl-cell mdl-cell--4-col" style="max-width: 40%; max-height: 40%;">
                                    <div class="violations-chart ct-golden-section"></div>
                                </div>
                                <div class="mdl-cell mdl-cell--1-col">
                                </div>
                                <div id="change-requests-chart" class="mdl-cell mdl-cell--4-col" style="max-width: 40%; max-height: 40%; margin-left: 3%;">
                                    <div class="change-requests-chart ct-golden-section"></div>
                                </div>
                                <div class="mdl-cell mdl-cell--1-col">
                                </div>
                            </div>
                            <div class="mdl-tooltip" data-mdl-for="violations-chart" style="font-family: Ubuntu;">
                                Кількість порушень зафіксованих за останні 7 днів
                            </div>
                            <div class="mdl-tooltip" data-mdl-for="change-requests-chart" style="font-family: Ubuntu;">
                                Кількість прийнятих запитів на зміну за останні 7 днів
                            </div>

                            <div style="width: 30%; margin-left: 15%; margin-top: 5%; color: darkgray; font-size: 14px">
                                Palindrome - це команда, яка наполегливо йде до однієї мети - забезпечити ваш комфорт. Наш сервіс завжди турбується про вас та вашу думку!
                                Якщо у вас виникають будь-які проблеми, запитання чи пропозиції, звертайтесь до розробників системи за <a href="https://github.com/Drofff" target="_blank">посиланням.</a>
                            </div>

                            <div class="mdl-grid" style="width: 90%; margin: 5% auto 7% auto;">
                                <div class="mdl-cell mdl-cell--1-col">
                                </div>
                                <div class="mdl-cell mdl-cell--4-col" style="margin-left: 3%;">
                                    <div class="demo-card-square mdl-card mdl-shadow--2dp" style="width: 400px;">
                                        <div class="mdl-card__supporting-text" style="width: 300px; margin-left: auto; margin-right: auto;">
                                            <h4>Додати порушення</h4>
                                            <form action="/violation/create" method="get">
                                                <div class="mdl-textfield mdl-js-textfield">
                                                    <input class="mdl-textfield__input" name="number" type="text" id="sample1">
                                                    <label class="mdl-textfield__label" for="sample1">Номер автомобіля</label>
                                                </div>
                                                <button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored"
                                                        style="margin-left: 165px;" type="submit">
                                                    Продовжити
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <div class="mdl-cell mdl-cell--1-col">
                                </div>
                                <div class="mdl-cell mdl-cell--4-col" style="margin-right: 3%; margin-left: 3%;">
                                    <div class="demo-card-square mdl-card mdl-shadow--2dp" style="width: 400px;">
                                        <div class="mdl-card__supporting-text" style="width: 300px; margin-left: auto; margin-right: auto;">
                                            <h4>Знайти водія</h4>
                                            <form action="/driver/find" method="get">
                                                <div class="mdl-textfield mdl-js-textfield">
                                                    <input class="mdl-textfield__input" name="name" type="text" id="sample1">
                                                    <label class="mdl-textfield__label" for="sample1">ПІБ водія</label>
                                                </div>
                                                <button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored"
                                                        style="margin-left: 200px;" type="submit">
                                                    Знайти
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <div class="mdl-cell mdl-cell--1-col">
                                </div>
                            </div>
                            <p style="height: 1px; width: 90%;"></p>
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

    var chart_options = {
        fullWidth: true,
        showPoint: false,
        lineSmooth: false
    };

    var dialog = document.querySelector('dialog');
    dialog.querySelector('.close').addEventListener('click', function() {
        dialog.close();
    });

    $(function() {
        initCharts();
        <#if error_message?? || message??>
        dialog.showModal();
        </#if>
    });

    function initCharts() {
        initViolationsChart();
        initChangeRequestsChart();
    }

    function initViolationsChart() {
        var labels = [];
        var series = [];
        <#list violations as date, count>
            labels.push('${date.dayOfWeek?substring(0, 3)}');
            series.push(${count});
        </#list>
        var data = {
            labels : labels,
            series : [ series ]
        };
        new Chartist.Line('.violations-chart', data, chart_options);
    }

    function initChangeRequestsChart() {
        var labels = [];
        var series = [];
        <#list requests as date, count>
            labels.push('${date.dayOfWeek?substring(0, 3)}');
            series.push(${count});
        </#list>
        var data = {
            labels : labels,
            series : [ series ]
        };
        new Chartist.Line('.change-requests-chart', data, chart_options);
    }

</script>
</body>
</html>