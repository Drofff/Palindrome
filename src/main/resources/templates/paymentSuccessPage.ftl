<html>
<head>
	<title>Palindrome - Successfully paid</title>
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
			width: 60%;
		}
		.demo-card-wide > .mdl-card__title {
			color: #fff;
			height: 200px;
			background: url('https://images.unsplash.com/photo-1509017174183-0b7e0278f1ec?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1351&q=80') center / cover;
		}
	</style>
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
				</div>
				<div class="mdl-dialog__actions">
					<button type="button" class="mdl-button close">Закрити</button>
				</div>
			</dialog>

			<div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 22%; margin-top: 5%;">
				<div class="mdl-card__title">
					<h2 class="mdl-card__title-text">Оплата штрафу</h2>
				</div>
				<div class="mdl-card__supporting-text">
					<i class="fas fa-check-circle" style="color: green"></i> Оплата успішно завершена
				</div>
			</div>

		</div>
	</main>
	<form action="/logout" method="post" id="logout">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
</div>
</body>
</html>