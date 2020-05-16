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
	<link href="https://fonts.googleapis.com/css?family=Nunito&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=Manrope:wght@200&display=swap" rel="stylesheet">
	<script src="https://kit.fontawesome.com/79433ece4b.js" crossorigin="anonymous"></script>
</head>
<body style="background: url('https://images.unsplash.com/photo-1526666361175-e3595627c376?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1049&q=80') center / cover">
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
	<header class="mdl-layout__header">
		<div class="mdl-layout__header-row">
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome</span>
			<div class="mdl-layout-spacer"></div>
			<nav class="mdl-navigation mdl-layout--large-screen-only">
				<a class="mdl-navigation__link" href="/registration">Реєстрація</a>
				<a class="mdl-navigation__link" href="/login">Авторизація</a>
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

			<div style="background: #fff; margin-left: 20%; margin-right: 10%; margin-top: 200px; font-family: 'Manrope', sans-serif;
				padding: 50px;">
				<p style="font-size: 15px;">Вітаємо в Palindrome! Ми - сервіс, що забезпечує вашу вза'ємодію з державою та органами правового порядку задля
				менеджменту вашої активності на дорозі. Приєднуйся, з нами простіше!</p>
				<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" style="margin-top: 2%;" href="/registration">
					Приєднатися
				</a>
			</div>
			<div class="mdl-grid" style="background: #fff; width: 100%; flex-grow: 1; min-height: 300px; margin-top: 70px;
			 padding-top: 40px;">

				<div class="mdl-cell mdl-cell--5-col" style="margin-left: auto; width: 330px;">
					<div class="demo-card-square mdl-card mdl-shadow--2dp">
						<div class="mdl-card__supporting-text" style="text-align: center">
							Моніторте власні порушення
							<img width="160px" style="margin-top: 28px;" height="160px" alt="" src="https://getdrawings.com/free-icon/monitor-icon-png-53.png">
						</div>
					</div>
				</div>
				<div class="mdl-cell mdl-cell--2-col">
				</div>
				<div class="mdl-cell mdl-cell--5-col" style="margin-right: auto; width: 330px;">
					<div class="demo-card-square mdl-card mdl-shadow--2dp">
						<div class="mdl-card__supporting-text" style="text-align: center">
							Оплачуйте штрафи онлайн
							<img width="180px" style="margin-top: 8px;" height="180px" alt="" src="https://www.selectivepay.com/wp-content/uploads/2019/09/icon-payment-gateway.png">
						</div>
					</div>
				</div>

				<div style="font-family: 'Josefin Sans', sans-serif; font-size: 18px; text-align: center; margin-top: 5%; margin-bottom: 3%; width: 100%;">
					<b>Palindrome</b>
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