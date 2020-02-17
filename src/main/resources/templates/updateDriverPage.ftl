<html>
<head>
	<title>Palindrome - Update Driver Info</title>
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
	<main class="mdl-layout__content">
		<form class="page-content" action="/driver/update" method="post">
			<div class="mdl-grid" style="margin-top: 3%;">
				<div class="mdl-cell mdl-cell--3-col">
				</div>
				<div class="mdl-cell mdl-cell--3-col">
					<div class="demo-card-image mdl-card mdl-shadow--2dp">
						<div class="mdl-card__title mdl-card--expand"></div>
					</div>
				</div>
				<div class="mdl-cell mdl-cell--3-col">
					<div class="mdl-textfield mdl-js-textfield">
						<input class="mdl-textfield__input" value="${driver.firstName}" name="firstName" type="text" id="sample1" required>
						<label class="mdl-textfield__label" for="sample1">Ім'я</label>
					</div>
					<div class="mdl-textfield mdl-js-textfield">
						<input class="mdl-textfield__input" value="${driver.middleName}" name="middleName" type="text" id="sample2" required>
						<label class="mdl-textfield__label" for="sample2">По батькові</label>
					</div>
					<div class="mdl-textfield mdl-js-textfield">
						<input class="mdl-textfield__input" value="${driver.lastName}" name="lastName" type="text" id="sample3" required>
						<label class="mdl-textfield__label" for="sample3">Прізвище</label>
					</div>
				</div>
				<div class="mdl-cell mdl-cell--3-col"></div>
			</div>
			<div class="mdl-grid">
				<div class="mdl-cell mdl-cell--3-col">
				</div>
				<div class="mdl-cell mdl-cell--6-col">
					<div>
						<p><b>Адреса:</b>
							<div class="mdl-textfield mdl-js-textfield">
								<input class="mdl-textfield__input" value="${driver.address}" name="address" type="text" id="sample4" required>
								<label class="mdl-textfield__label" for="sample4">Адреса</label>
							</div>
						</p>
						<p><b>Номер посвідчення водія:</b>
							<div class="mdl-textfield mdl-js-textfield">
								<input class="mdl-textfield__input" value="${driver.licenceNumber}" name="licenceNumber" type="text" id="sample5" required>
								<label class="mdl-textfield__label" for="sample5">Номер посвідчення водія</label>
							</div>
						</p>
					</div>
					<div style="margin-top: 2%;">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
						<#if driver.id??>
							<input type="hidden" name="id" value="${driver.id}">
						</#if>
						<button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" type="submit">
							Зберегти
						</button>
					</div>
				</div>
			</div>
		</form>
	</main>
	<form action="/logout" method="post" id="logout">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
</div>
</body>
</html>