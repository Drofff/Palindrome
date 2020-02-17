<html>
<head>
	<title>Palindrome - Login</title>
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
	<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
	<script
			src="https://code.jquery.com/jquery-3.4.1.js"
			integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
			crossorigin="anonymous"></script>
	<link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
	<style>
		.demo-card-wide.mdl-card {
			width: 600px;
		}
		.demo-card-wide > .mdl-card__title {
			color: #fff;
			height: 300px;
			background: url('https://securionpay.com/wp-content/uploads/2016/05/Things-You-Need-To-Know-About-Card-Authorization.svg') center / cover;
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
				<a class="mdl-navigation__link" href="/registration">Реєстрація</a>
			</nav>
		</div>
	</header>
	<main class="mdl-layout__content">
		<div class="page-content">

			<dialog class="mdl-dialog">
				<div class="mdl-dialog__content">
					<p style="color: red">
						Неправильні авторизаційні дані
					</p>
				</div>
				<div class="mdl-dialog__actions">
					<button type="button" class="mdl-button close">Закрити</button>
				</div>
			</dialog>

			<div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 27%; margin-top: 2%; margin-bottom: 2%;">
				<div class="mdl-card__title">
					<h2 class="mdl-card__title-text">Авторизація</h2>
				</div>
				<div class="mdl-card__supporting-text">
					<form action="/login" method="post">
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<input class="mdl-textfield__input" readonly onfocus="this.removeAttribute('readonly');" name="username" type="text" id="email">
							<label class="mdl-textfield__label" for="email">Електронна пошта</label>
						</div>
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<input class="mdl-textfield__input" readonly onfocus="this.removeAttribute('readonly');" name="password" type="password" id="password">
							<label class="mdl-textfield__label" for="password">Пароль</label>
						</div>
						<div style="margin-bottom: 5%;">
							<label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect" for="checkbox-1">
								<input type="checkbox" id="checkbox-1" class="mdl-checkbox__input" name="remember-me">
								<span class="mdl-checkbox__label">Залишатись в системі</span>
							</label>
						</div>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
						<div>
							<button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" type="submit">
								Вхід
							</button>
							<a href="/forgot-password" style="color: cornflowerblue; margin-left: 10%;">Забули пароль?</a>
						</div>
					</form>
				</div>
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
	    <#if error_message??>
            dialog.showModal();
	    </#if>
	});
</script>
</body>
</html>