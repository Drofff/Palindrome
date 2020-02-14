<html>
<head>
	<title>Palindrome - Registration</title>
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
			<nav class="mdl-navigation mdl-layout--large-screen-only">
				<a class="mdl-navigation__link" href="/">Головна</a>
				<a class="mdl-navigation__link" href="/login">Авторизація</a>
			</nav>
		</div>
	</header>
	<main class="mdl-layout__content">
		<div class="page-content">
			<h4 style="margin-top: 10%; margin-left: 35%;">Реєстрація</h4>
			<form action="/registration" method="post" id="registration-form" style="margin-left: 35%;">
				<div>
					<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
						<input class="mdl-textfield__input" <#if user?? && user.username??>value="${user.username}" </#if> autocomplete="off" readonly onfocus="this.removeAttribute('readonly');" name="username" type="text" id="email">
						<label class="mdl-textfield__label" for="email">Електронна пошта</label>
					</div>
					<p id="email-msg" style="color: red"></p>
					<#if usernameError??>
						<p style="color: red">Невірний формат пошти</p>
					</#if>
					<#if error_message??>
						<p style="color: red">Ця пошта вже зареєстрована в системі</p>
					</#if>
				</div>
				<div>
					<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
						<input class="mdl-textfield__input" <#if user?? && user.password??>value="${user.password}" </#if> autocomplete="off" readonly onfocus="this.removeAttribute('readonly');" name="password" type="password" id="password">
						<label class="mdl-textfield__label" for="password">Пароль</label>
					</div>
					<p id="password-msg" style="color: red"></p>
				</div>
				<div>
					<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
						<input class="mdl-textfield__input" <#if user?? && user.password??>value="${user.password}" </#if> autocomplete="off" readonly onfocus="this.removeAttribute('readonly');" type="password" id="repeat-password">
						<label class="mdl-textfield__label" for="repeat-password">Повторіть пароль</label>
					</div>
					<p id="repeat-password-msg" style="color: red"></p>
				</div>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
					<div>
					<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="register()">
						Створити аккаунт
					</a>
				</div>
			</form>
		</div>
	</main>
</div>
<script>

    $(function() {
        $("#email").keyup(function() {
            var email = $("#email").val();
            if(is_empty(email)) {
                $("#email-msg").html("Будь ласка, вкажіть електронну пошту");
            } else {
                $("#email-msg").html("");
            }
        });
        $("#password").keyup(function() {
            var password = $("#password").val();
            if(is_empty(password)) {
                $("#password-msg").html("Оберіть пароль");
            } else {
                $("#password-msg").html("");
            }
        });
        $("#repeat-password").keyup(function() {
            var repeat_password = $("#repeat-password").val();
            if(is_empty(repeat_password)) {
                $("#repeat-password-msg").html("Обов'язкове поле");
            } else {
                $("#repeat-password-msg").html("");
            }
        });
	});

	function register() {
	    var is_valid = true;

	    var email = $("#email").val();
	    var password = $("#password").val();
	    var repeat_password = $("#repeat-password").val();

	    if(is_empty(email)) {
	        $("#email-msg").html("Будь ласка, вкажіть електронну пошту");
            is_valid = false;
	    } else {
	        $("#email-msg").html("");
	    }

	    if(is_empty(password)) {
            $("#password-msg").html("Оберіть пароль");
            is_valid = false;
        } else if(password.length < 8) {
            $("#password-msg").html("Пароль повинен мати мінімум 8 символів");
            is_valid = false;
	    } else {
            $("#password-msg").html("");
	    }

	    if(is_empty(repeat_password)) {
            $("#repeat-password-msg").html("Обов'язкове поле");
            is_valid = false;
	    } else if(password !== repeat_password) {
	        $("#repeat-password-msg").html("Повинен співпадати з паролем");
            is_valid = false;
	    } else {
            $("#repeat-password-msg").html("");
	    }

	    if(is_valid) {
		    $("#registration-form").submit();
	    }

	}

	function is_empty(str) {
	    return !str || str.length === 0;
	}

</script>
</body>
</html>