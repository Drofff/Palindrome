<html>
<head>
	<title>Palindrome - Violation</title>
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
	<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
	<script
			src="https://code.jquery.com/jquery-3.4.1.js"
			integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
			crossorigin="anonymous"></script>
	<link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
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
    <#include "menu.ftl">
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

			<div style="margin-top: 5%; margin-left: 5%; margin-right: 5%;">
               <p>Порушення: ${violation.violationType.name}</p>
			   <p>Статус: <#if violation.paid><b style="color: green;">Оплачено</b><#else><b style="color: red;">Очікує оплати</b></#if></p>
			   <p>Місце фіксування: ${violation.location}</p>
				<p>Дата: <#if violation.dateTime.hour lt 10>0${violation.dateTime.hour}<#else>${violation.dateTime.hour}</#if>:<#if violation.dateTime.minute lt 10>0${violation.dateTime.minute}<#else>${violation.dateTime.minute}</#if> ${violation.dateTime.dayOfMonth}/${violation.dateTime.month.ordinal() + 1}/${violation.dateTime.year?c}</p>
				<p>Розмір штрафу: ${violation.violationType.fee.amount / 100} ${violation.violationType.fee.currency}</p>
				<p>Зафіксував: <font style="cursor: pointer" onclick="window.location.href='/police/${violation.officer.id}'">${violation.officer.position} ${violation.officer.lastName} ${violation.officer.firstName}</font></p>
				<#if !violation.paid>
				    <p>
					    <a class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect" href="/pay/${violation.id}">
						    Оплатити
					    </a>
				    </p>
				<#else>
					<p style="margin-top: 2%;">
						<a href="/pay/ticket/${violation.id}">Переглянути квитанцію</a>
					</p>
				</#if>
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