<html>
<head>
	<title>Palindrome - Account Blocked</title>
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
			background: url('https://images.unsplash.com/photo-1496368077930-c1e31b4e5b44?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80') center / cover;
		}
	</style>

</head>
<body style="background: url('https://images.unsplash.com/photo-1480944657103-7fed22359e1d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1489&q=80') center / cover">
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
	<header class="mdl-layout__header">
		<div class="mdl-layout__header-row">
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome</span>
			<div class="mdl-layout-spacer"></div>
			<a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
		</div>
	</header>
	<main class="mdl-layout__content">
		<div class="page-content">

			<div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 30%; margin-top: 5%;">
				<div class="mdl-card__title">
					<h2 class="mdl-card__title-text">Ваш аккаунт заблоковано!</h2>
				</div>
				<div class="mdl-card__supporting-text" style="font-size: 16px;">
					<p><b>${dateTime}</b> адміністрація сервісу заблокувала ваш аккаунт</p>
					<p><b>Причина:</b> ${reason}</p>
				</div>
			</div>

		</div>
	</main>
</div>
<form action="/logout" method="post" id="logout">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>
</body>
</html>