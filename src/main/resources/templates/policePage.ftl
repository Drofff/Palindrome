<html>
<head>
	<title>Palindrome - Police Info</title>
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
	<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
	<script
			src="https://code.jquery.com/jquery-3.4.1.js"
			integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
			crossorigin="anonymous"></script>
	<link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
	<style>
		.demo-card-image.mdl-card {
			width: 256px;
			height: 256px;
			background: url('data:image/png;base64, ${photo}') center / cover;
		}
		.demo-card-image {
			height: 52px;
			padding: 16px;
			background: rgba(0, 0, 0, 0.2);
		}
	</style>
</head>
<body>
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
	<header class="mdl-layout__header">
		<div class="mdl-layout__header-row">
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome</span>
			<div class="mdl-layout-spacer"></div>
			<nav class="mdl-navigation mdl-layout--large-screen-only">
				<a class="mdl-navigation__link" href="/">Головна</a>
				<a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
			</nav>
		</div>
	</header>
    <#include "menu.ftl">
	<main class="mdl-layout__content">
		<div class="page-content">

			<dialog class="mdl-dialog">
				<div class="mdl-dialog__content">
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

			<div class="mdl-grid" style="margin-top: 3%;">
				<div class="mdl-cell mdl-cell--3-col">
				</div>
				<div class="mdl-cell mdl-cell--3-col">
					<div class="demo-card-image mdl-card mdl-shadow--2dp">
						<div class="mdl-card__title mdl-card--expand"></div>
					</div>
				</div>
				<div class="mdl-cell mdl-cell--3-col" style="font-size: 20px">
					<b>${police.firstName} <#if police.middleName??>${police.middleName}</#if> ${police.lastName}</b>
				</div>
				<div class="mdl-cell mdl-cell--3-col"></div>
			</div>
			<div class="mdl-grid">
				<div class="mdl-cell mdl-cell--3-col">
				</div>
				<div class="mdl-cell mdl-cell--6-col">
					<div>
						<p><b>Посада:</b> ${police.position}</p>
						<p><b>Номер жетона:</b> ${police.tokenNumber}</p>
						<p><b>Департамент:</b> ${police.department.name}</p>
					</div>
				</div>
			</div>
		</div>
	</main>
	<form action="/logout" method="post" id="logout">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
</div>
<script>
    var dialog = document.querySelector('dialog');
    dialog.querySelector('.close').addEventListener('click', function() {
        dialog.close();
    });
    $(function() {
        <#if message??>
        dialog.showModal();
        </#if>
    });
</script>
</body>
</html>