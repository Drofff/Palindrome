<html>
<head>
	<title>Palindrome - Requests</title>
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
	<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
	<script
			src="https://code.jquery.com/jquery-3.4.1.js"
			integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
			crossorigin="anonymous"></script>
	<link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
	<style>
		.demo-card-square.mdl-card {
			width: 320px;
			height: 320px;
			float: left;
			margin: 1rem;
			position: relative;
		}
		.driver {
			background:
					url('https://i.pinimg.com/originals/36/f9/76/36f97663a6a4a6e7a9547e1b5f9d4087.gif') center / cover;

		}
		.car {
			background:
					url('https://cdn.dribbble.com/users/992933/screenshots/4608688/car_loop.gif') center / cover;
		}
		.demo-card-square > .mdl-card__title {
			color: #fff;
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
			<div class="mdl-layout-spacer"></div>
			<a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
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

			<div style="margin-top: 5%; margin-left: 5%; margin-right: 5%;">

				<#if !requests?? || requests?size == 0>
				    <h4>Записи відсутні..</h4>
				</#if>

                <#list requests as request>
					<div class="demo-card-square mdl-card mdl-shadow--2dp">
                        <#if request.isForDriver()>
							<div class="mdl-card__title mdl-card--expand driver">
								<h2 class="mdl-card__title-text">Запит на водія</h2>
							</div>
							<div class="mdl-card__supporting-text">
								<a href="/admin/users/driver/${request.targetValue.id}" target="_blank">Переглянути водія</a>
                                <#if request.comment?? && request.comment?has_content>
									<p>Коментар: ${request.comment}</p>
                                <#else>
	                                <p>Коментар відсутній</p>
                                </#if>
								<p>Отримано: <#if request.dateTime.hour lt 10>0${request.dateTime.hour}<#else>${request.dateTime.hour}</#if>:<#if request.dateTime.minute lt 10>0${request.dateTime.minute}<#else>${request.dateTime.minute}</#if>, ${request.dateTime.dayOfMonth} of ${request.dateTime.month.name()?capitalize} ${request.dateTime.year?c}</p>
							</div>
							<div class="mdl-card__actions mdl-card--border">
								<a href="/change-request/driver/${request.id}" class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect">
									Перейти до запиту
								</a>
							</div>
                        </#if>
                        <#if request.isForCar()>
							<div class="mdl-card__title mdl-card--expand car">
								<h2 class="mdl-card__title-text">Запит на автомобіль</h2>
							</div>
							<div class="mdl-card__supporting-text">
								<a href="/admin/cars/${request.targetValue.id}" target="_blank">Переглянути автомобіль</a>
                                <#if request.comment?? && request.comment?has_content>
									<p>Коментар: ${request.comment}</p>
                                <#else>
	                                <p>Коментар відсутній</p>
                                </#if>
								<p>Отримано: <#if request.dateTime.hour lt 10>0${request.dateTime.hour}<#else>${request.dateTime.hour}</#if>:<#if request.dateTime.minute lt 10>0${request.dateTime.minute}<#else>${request.dateTime.minute}</#if>, ${request.dateTime.dayOfMonth} of ${request.dateTime.month.name()?capitalize} ${request.dateTime.year?c}</p>
							</div>
							<div class="mdl-card__actions mdl-card--border">
								<a href="/change-request/car/${request.id}" class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect">
									Перейти до запиту
								</a>
							</div>
                        </#if>
					</div>
                </#list>
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