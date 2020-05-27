<html>
<head>
	<title>Palindrome - Create User</title>
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
			width: 512px;
		}
		.demo-card-wide > .mdl-card__title {
			color: #fff;
			height: 176px;
			background: url('https://images.unsplash.com/photo-1533745848184-3db07256e163?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1489&q=80') center / cover;
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
			<a class="mdl-navigation__link" href="/admin/cars" style="cursor: pointer">Автомобілі</a>
			<a class="mdl-navigation__link" href="/change-request" style="cursor: pointer">Запити</a>
			<a class="mdl-navigation__link" href="/admin/brand" style="cursor: pointer">Дані</a>
			<div class="mdl-layout-spacer"></div>
			<button id="demo-menu-lower-right" class="mdl-button mdl-js-button mdl-button--icon">
				<i class="material-icons">more_vert</i>
			</button>
			<ul class="mdl-menu mdl-menu--bottom-right mdl-js-menu mdl-js-ripple-effect" for="demo-menu-lower-right">
				<li class="mdl-menu__item" onclick="window.location.href='/change-password'">Змінити пароль</li>
				<li class="mdl-menu__item" onclick="$('#logout').submit()">Вихід</li>
			</ul>
		</div>
	</header>
	<main class="mdl-layout__content">
		<div class="page-content">

			<dialog class="mdl-dialog" id="msg-dialog">
				<div class="mdl-dialog__content">
					<p style="color: red">
                        <#if error_message??>
                            ${error_message}
                        </#if>
					</p>
					<p>
                        <#if message??>
                            <#if message == "Successfully blocked user">
								Успішно заблоковано доступ користувачу
                            <#else>
                                <#if message == "Successfully unblocked user">
									Успішно відновлено доступ користувачу
                                <#else>
                                    ${message}
                                </#if>
                            </#if>
                        </#if>
					</p>
				</div>
				<div class="mdl-dialog__actions">
					<button type="button" class="mdl-button close">Закрити</button>
				</div>
			</dialog>

			<div class="mdl-grid" style="margin-top: 5%; margin-left: 15%;">
				<div class="mdl-cell mdl-cell--4-col">
					<form action="/admin/users/create" method="post">
						<div>
							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<input class="mdl-textfield__input" <#if user?? && user.username??>value="${user.username}"</#if> type="text" id="sample3" name="username">
								<label class="mdl-textfield__label" for="sample3">Електронна пошта</label>
							</div>
                            <#if usernameError??>
								<p style="color: red">${usernameError}</p>
                            </#if>
						</div>
						<div>
							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<select class="mdl-textfield__input" id="role" name="role">
									<option value=""></option>
                                    <#list roles as role>
										<option <#if user?? && user.role?? && user.role.name() == role.name()>selected</#if> value="${role.name()}">${role.name()?capitalize}</option>
                                    </#list>
								</select>
								<label class="mdl-textfield__label" for="role">Роль</label>
							</div>
						</div>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
						<button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" type="submit">
							Створити
						</button>
					</form>
				</div>
				<div class="mdl-cell mdl-cell--6-col">
					<div class="demo-card-wide mdl-card mdl-shadow--2dp">
						<div class="mdl-card__title">
							<h2 class="mdl-card__title-text">Створення нового аккаунта</h2>
						</div>
						<div class="mdl-card__supporting-text">
							Створивши аккаунт на пошту користувача буде відправлено лист з даними для входу. Аккаунт не вимагає додаткової активації.
						</div>
					</div>
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