<html>
<head>
    <title>Palindrome - ${driver.firstName} ${driver.lastName} Statistics</title>
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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.css">
    <script src="https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.js"></script>
    <style>
        .ct-series-a .ct-line {
            stroke: dodgerblue;
        }
        .ct-series-a .ct-point {
            stroke: dodgerblue;
        }
        .ct-series-a .ct-bar {
            stroke: dodgerblue;
            stroke-width: 30px;
        }
        .ct-series-b .ct-bar {
            stroke: dodgerblue;
            stroke-width: 30px;
        }
        .ct-series-c .ct-bar {
            stroke: dodgerblue;
            stroke-width: 30px;
        }
        .ct-series-d .ct-bar {
            stroke: dodgerblue;
            stroke-width: 30px;
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
                        <a class="mdl-navigation__link" href="/change-password">Змінити пароль</a>
                        <a class="mdl-navigation__link" href="/police">Профіль</a>
                    </nav>
                </div>
                <main class="mdl-layout__content">
                    <div class="page-content" style="background: white; margin-top: 3%; margin-left: 4%; width: 70%; padding: 4% 4% 6% 4%;">

                        <h3 style="margin-left: 9%; margin-bottom: 3%;">Статистика ${driver.firstName} ${driver.lastName} (${driver.licenceNumber}) <hr style="width: 60%;"></h3>
                        <div class="mdl-grid">
                            <div class="mdl-cell mdl-cell--1-col"></div>
                            <div class="mdl-cell mdl-cell--3-col">
                                <div>Порушень: ${statistic.violationsCount}</div>
                                <div>Очікують оплати: ${statistic.unpaidViolationsCount}</div>
                            </div>
                            <div class="mdl-cell mdl-cell--5-col">
                                <#if statistic.longestUnpaidViolationDateTime??>
                                    <div>Найдовше очікує оплати порушення зафіксоване:
                                        <#if statistic.longestUnpaidViolationDateTime.hour lt 10>0${statistic.longestUnpaidViolationDateTime.hour}<#else>${statistic.longestUnpaidViolationDateTime.hour}</#if>:<#if statistic.longestUnpaidViolationDateTime.minute lt 10>0${statistic.longestUnpaidViolationDateTime.minute}<#else>${statistic.longestUnpaidViolationDateTime.minute}</#if> ${statistic.longestUnpaidViolationDateTime.dayOfMonth}/${statistic.longestUnpaidViolationDateTime.month.ordinal() + 1}/${statistic.longestUnpaidViolationDateTime.year?c}
                                    </div>
                                </#if>
                            </div>
                            <div class="mdl-cell mdl-cell--1-col"></div>
                        </div>

                        <#if statistic.violationsCount gt 0>
                            <div class="mdl-grid" style="width: 90%; margin-left: auto; margin-right: auto; margin-top: 5%;">
                                <div id="violations-chart" class="mdl-cell mdl-cell--5-col" style="max-width: 60%; max-height: 60%;">
                                    <div class="violations-chart ct-golden-section"></div>
                                </div>
                                <div class="mdl-cell mdl-cell--1-col">
                                </div>
                                <div id="violation-types-chart" class="mdl-cell mdl-cell--5-col" style="max-width: 60%; max-height: 60%; margin-left: 3%;">
                                    <div class="violation-types-chart ct-golden-section"></div>
                                </div>
                                <div class="mdl-cell mdl-cell--1-col">
                                </div>
                            </div>
                            <div class="mdl-tooltip" data-mdl-for="violations-chart" style="font-family: Ubuntu;">
                                Кількість порушень в місяць на протязі останніх 7 місяців
                            </div>
                            <div class="mdl-tooltip" data-mdl-for="violation-types-chart" style="font-family: Ubuntu;">
                                Найбільш часто фіксовані типи порушень
                            </div>
                        </#if>

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
        showPoint: true,
        lineSmooth: true
    };

    var dialog = document.querySelector('dialog');
    dialog.querySelector('.close').addEventListener('click', function() {
        dialog.close();
    });

    $(function() {
        <#if statistic.violationsCount gt 0>
            initCharts();
        </#if>
        <#if error_message?? || message??>
            dialog.showModal();
        </#if>
    });

    function initCharts() {
        <#if statistic.violationsCountPerLastMonths??>
            initViolationsPerMonthsChart();
        </#if>
        initViolationTypesChart();
    }

    function initViolationsPerMonthsChart() {
        var labels = [];
        var series = [];
        <#list statistic.violationsCountPerLastMonths as monthDate, count>
            labels.push('${monthDate.month?capitalize}');
            series.push(${count});
        </#list>
        var data = {
            labels : labels,
            series : [ series ]
        };
        new Chartist.Line('.violations-chart', data, chart_options);
    }

    function initViolationTypesChart() {
        var labels = [];
        var series = [];

        <#list statistic.mostFrequentViolationTypes as type, count>
            labels.push('${type.name}');
            series.push(${count});
        </#list>

        var othersCount = ${statistic.violationsCount} - series.reduce((ac, cV) => ac + cV);
        labels.push('Інші типи');
        series.push(othersCount);

        var data = {
            labels : labels,
            series : [ series ]
        };

        new Chartist.Bar('.violation-types-chart', data);
    }

</script>
</body>
</html>