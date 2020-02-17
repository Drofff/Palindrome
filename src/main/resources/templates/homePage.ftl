<html>
<head>
	<title>Palindrome - Home</title>
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
			height: 60px;
			background: #3E4EB8;
		}
	</style>
</head>
<body>
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
	<header class="mdl-layout__header">
		<div class="mdl-layout__header-row">
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome</span>
			<div class="mdl-layout-spacer"></div>
			<#if is_authenticated?? && is_authenticated>
				<a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
		    <#else>
			    <nav class="mdl-navigation mdl-layout--large-screen-only">
				    <a class="mdl-navigation__link" href="/registration">Реєстрація</a>
				    <a class="mdl-navigation__link" href="/login">Авторизація</a>
			    </nav>
			</#if>
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
				<div class="mdl-cell mdl-cell--1-col"></div>

				<div class="mdl-cell mdl-cell--4-col">
					<div class="demo-card-wide mdl-card mdl-shadow--2dp">
						<div class="mdl-card__title">
							<h2 class="mdl-card__title-text">Мої автомобілі</h2>
						</div>
						<div class="mdl-card__supporting-text">
							<#list cars as car>
								${car.model}
							</#list>
						</div>
					</div>

					<div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-top: 10%;">
						<div class="mdl-card__title">
							<h2 class="mdl-card__title-text">Квитанції до оплати</h2>
						</div>
						<div class="mdl-card__supporting-text">
							Lorem ipsum dolor sit amet, consectetur adipiscing elit.
							Mauris sagittis pellentesque lacus eleifend lacinia...
						</div>
					</div>
				</div>
				<div class="mdl-cell mdl-cell--1-col"></div>
				<div class="mdl-cell mdl-cell--4-col">
					<div class="demo-card-wide mdl-card mdl-shadow--2dp">
						<div class="mdl-card__title">
							<h2 class="mdl-card__title-text">Мої порушення</h2>
						</div>
						<div class="mdl-card__supporting-text">
							Lorem ipsum dolor sit amet, consectetur adipiscing elit.
							Mauris sagittis pellentesque lacus eleifend lacinia...
						</div>
					</div>
				</div>
				<div class="mdl-cell mdl-cell--1-col"></div>
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