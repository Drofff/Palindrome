<html>
<head>
	<title>Palindrome - Change Driver Photo</title>
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
			width: 512px;
		}
		.demo-card-wide > .mdl-card__title {
			color: #fff;
			height: 176px;
			background: url('https://images.unsplash.com/photo-1511367461989-f85a21fda167?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80') center / cover;
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
		<form class="page-content" action="/driver/update/photo" method="post" enctype="multipart/form-data">

			<dialog class="mdl-dialog">
				<div class="mdl-dialog__content">
					<#if error_message??>
						<p style="color: red">
                            ${error_message}
						</p>
					</#if>
				</div>
				<div class="mdl-dialog__actions">
					<button type="button" class="mdl-button close">Закрити</button>
				</div>
			</dialog>

            <#if success?? && success>
				<div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 30%; margin-top: 5%;">
					<div class="mdl-card__title">
						<h2 class="mdl-card__title-text">Оберіть нове фото</h2>
					</div>
					<div class="mdl-card__supporting-text">
						<i class="fas fa-check-circle" style="color: green"></i> Фото успішно оновлено
					</div>
				</div>
			<#else>
				<div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 30%; margin-top: 5%;">
					<div class="mdl-card__title">
						<h2 class="mdl-card__title-text">Оберіть нове фото</h2>
					</div>
					<div class="mdl-card__supporting-text">
						<input type="file" name="photo">
					</div>
					<div class="mdl-card__actions mdl-card--border">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
						<button class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" type="submit">
							Зберегти
						</button>
					</div>
				</div>
			</#if>

		</form>
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
        <#if error_message??>
        dialog.showModal();
        </#if>
    });
</script>
</body>
</html>