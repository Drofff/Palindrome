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
		 .demo-card-square.mdl-card {
			 width: 140px;
			 height: 60px;
		 }
		.demo-card-square > .mdl-card__title {
			color: #fff;
			background:
					url('https://imageog.flaticon.com/icons/png/512/55/55283.png?size=1200x630f&pad=10,10,10,10&ext=png&bg=FFFFFFFF') center / cover;
		}
	</style>
</head>
<body>
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
	<header class="mdl-layout__header">
		<div class="mdl-layout__header-row">
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome</span>
			<div class="mdl-layout-spacer"></div>
			<#if authenticated?? && authenticated>
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

			<#if authenticated?? && authenticated>
				<div class="mdl-grid" style="margin-top: 3%;">
					<div class="mdl-cell mdl-cell--1-col"></div>

					<div class="mdl-cell mdl-cell--4-col">
						<div class="demo-card-wide mdl-card mdl-shadow--2dp">
							<div class="mdl-card__title">
								<h2 class="mdl-card__title-text">Мої автомобілі</h2>
							</div>
							<div class="mdl-card__supporting-text">
								<div class="mdl-grid" style="margin-left: -10px">
                                    <#list cars as car>
										<div class="mdl-cell mdl-cell--4-col" style="margin-right: 20px; cursor: pointer">
											<div class="demo-card-square mdl-card mdl-shadow--2dp" onclick="window.location.href='/car/${car.id}'">
												<div class="mdl-card__title mdl-card--expand">
												</div>
												<div class="mdl-card__supporting-text" style="text-align: center; font-size: 20px; margin-left: -10px">
                                                    <#if car.model?length gt 7>
														<b>${car.model?substring(0, 7)}..</b>
                                                    <#else>
														<b>${car.model}</b>
                                                    </#if>
												</div>
											</div>
										</div>
                                    </#list>
									<div class="mdl-cell mdl-cell--1-col" style="cursor: pointer">
										<div class="demo-card-square mdl-card mdl-shadow--2dp" onclick="window.location.href='/car/create'">
											<div class="mdl-card__title mdl-card--expand" style="background: url('https://img.icons8.com/pastel-glyph/2x/plus.png') center / cover">
											</div>
											<div class="mdl-card__supporting-text" style="text-align: center; font-size: 20px; margin-left: -10px">
												Додати авто
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="mdl-card__actions mdl-card--border">
								<a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" href="/car">
									Переглянути всі
								</a>
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
							<div class="mdl-card__actions mdl-card--border">
								<a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect">
									Переглянути всі
								</a>
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
							<div class="mdl-card__actions mdl-card--border">
								<a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect">
									Переглянути всі
								</a>
							</div>
						</div>
					</div>
					<div class="mdl-cell mdl-cell--1-col"></div>
				</div>
			<#else>

			</#if>

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