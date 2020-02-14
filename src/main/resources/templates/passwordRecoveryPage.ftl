<html>
<head>
	<title>Palindrome - Password Recovery</title>
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
			background: url('https://www.howtogeek.com/wp-content/uploads/2019/11/img_5dcdb2cfe6171.png') center / cover;
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
			</nav>
		</div>
	</header>
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

			<div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 27%; margin-top: 2%; margin-bottom: 2%;">
				<div class="mdl-card__title">
					<h2 class="mdl-card__title-text">Новий пароль</h2>
				</div>
				<div class="mdl-card__supporting-text">
					<form action="/verify-recovery" method="post" id="recovery-form">
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<input class="mdl-textfield__input" readonly onfocus="this.removeAttribute('readonly');" name="password" type="password" id="password">
							<label class="mdl-textfield__label" for="email">Пароль</label>
						</div>
						<p id="password-msg" style="color: red"></p>
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<input class="mdl-textfield__input" readonly onfocus="this.removeAttribute('readonly');" type="password" id="repeat-password">
							<label class="mdl-textfield__label" for="password">Повторіть пароль</label>
						</div>
						<p id="repeat-password-msg" style="color: red"></p>
						<input type="hidden" name="token" value="${token}">
						<input type="hidden" name="userId" value="${userId}">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
						<div>
							<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" id="save">
								Зберегти
							</a>
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

	    $("#save").click(function() {
	        var password = $("#password").val();
	        var repeat_password = $("#repeat-password").val();
	        if(!password || password.length < 8) {
	            $("#password-msg").html('Пароль повинен містити мінімум 8 символів');
	            return;
	        }
            $("#password-msg").html('');
	        if(!repeat_password) {
	            $("#repeat-password-msg").html("Це обов'язкове поле");
	            return;
	        }
            $("#repeat-password-msg").html('');
	        if(password !== repeat_password) {
                $("#repeat-password-msg").html("Не співпадає з паролем");
	            return;
	        }
	        $("#recovery-form").submit();
	    });
	});

</script>
</body>
</html>