<html>
<head>
	<title>Palindrome - Change Password</title>
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
		.demo-card-square.mdl-card {
			width: 600px;
			max-height: 700px;
		}
		.demo-card-square > .mdl-card__title {
			height: 350px;
			color: #fff;
			background:
					url('https://cikavoinfo.com/uploads/images/default/password.jpg') center / cover;
		}
	</style>
</head>
<body>
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
	<#if role.name() == "DRIVER">
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
	</#if>
	<#if role.name() == "POLICE">
		<header class="mdl-layout__header">
			<div class="mdl-layout__header-row">
				<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome <font color="#ffd700">Police</font></span>
				<div class="mdl-layout-spacer"></div>
				<a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
			</div>
		</header>
		<main class="mdl-layout__content">
			<div class="page-content">
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
	</#if>
	<#if role.name() == "ADMIN">
		<header class="mdl-layout__header">
			<div class="mdl-layout__header-row">
				<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome <font color="#ffd700">Admin</font></span>
				<a class="mdl-navigation__link" href="/admin/home" style="cursor: pointer">Головна</a>
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
	</#if>
	<main class="mdl-layout__content">
		<div class="page-content">

			<dialog class="mdl-dialog" style="z-index: 10000;">
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

			<div class="demo-card-square mdl-card mdl-shadow--2dp" style="<#if role.name() == "POLICE">margin-left: 11%;<#else>margin-left: 27%;</#if> <#if role.name() == "ADMIN">margin-top: 2%;<#else>margin-top: 5%;</#if>">
				<div class="mdl-card__title mdl-card--expand">
					<h2 class="mdl-card__title-text">Зміна паролю</h2>
				</div>
				<div class="mdl-card__supporting-text">
					<#if message??>
						<i class="fas fa-check-circle" style="color: green"></i> ${message}
				    <#else>
					    <form action="/change-password" method="post" id="chng-pwd">
						    <div>
							    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" id="password-field">
								    <input class="mdl-textfield__input" type="password" <#if passwords?? && passwords.password??>value="${passwords.password}"</#if> name="password" id="password">
								    <label class="mdl-textfield__label" for="password">Пароль</label>
							    </div>
						    </div>
						    <div>
							    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								    <input class="mdl-textfield__input" type="password" <#if passwords?? && passwords.newPassword??>value="${passwords.newPassword}"</#if> name="newPassword" id="newPassword">
								    <label class="mdl-textfield__label" for="newPassword">Новий пароль</label>
							    </div>
                                <#if passwordError??>
								    <p style="color: red">
                                        ${passwordError}
								    </p>
                                </#if>
						    </div>
						    <div>
							    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								    <input class="mdl-textfield__input" type="password" <#if passwords?? && passwords.newPassword??>value="${passwords.newPassword}"</#if> id="re-newPassword">
								    <label class="mdl-textfield__label" for="re-newPassword">Повторіть новий пароль</label>
							    </div>
						    </div>
						    <div style="margin-top: 3%;">
							    <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect" for="byMail">
								    <input type="checkbox" id="byMail" name="byMail" class="mdl-checkbox__input" <#if passwords?? && passwords.byMail?? && passwords.byMail>checked</#if> >
								    <span class="mdl-checkbox__label">Підтвердження через пошту</span>
							    </label>
						    </div>
						    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
						    <div style="margin-top: 5%;">
							    <a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="submit()">
								    Змінити
							    </a>
						    </div>
					    </form>
					</#if>
				</div>
			</div>

		</div>
	</main>
	<#if role.name() == "POLICE">
				</div>
			</div>
		</main>
	</#if>
	<form action="/logout" method="post" id="logout">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
</div>
<script>

	var password = '';

	var DISABLED = 'disabled';
	var DISABLED_CLASS = 'is-disabled';

    var dialog = document.querySelector('dialog');
    dialog.querySelector('.close').addEventListener('click', function() {
        dialog.close();
    });
    $(function() {

        <#if error_message??>
            dialog.showModal();
        </#if>

	    var password_input = $("#password");
	    var password_field = $("#password-field");
	    var by_mail_input = $("#byMail");

        by_mail_input.change(function() {
	        var checked = by_mail_input[0].checked;
	        if(checked === true) {
                password_field.addClass(DISABLED_CLASS);
                password_input.attr(DISABLED, true);
                password = password_input.val();
                password_input.val('');
	        } else {
                password_field.removeClass(DISABLED_CLASS);
                password_input.removeAttr(DISABLED);
                password_input.val(password);
	        }
	    });

    });

    function submit() {
        var password = $("#newPassword").val();
        var re_password = $("#re-newPassword").val();
        if(password === re_password) {
            $("#chng-pwd").submit();
        } else {
            window.alert('Новий пароль не співпадає з повтореним');
        }
    }

</script>
</body>
</html>