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
	<style>
		.demo-card-wide.mdl-card {
			width: 512px;
		}
		.demo-card-wide > .mdl-card__title {
			color: #fff;
			height: 60px;
			background: #3E4EB8;
		}
		 .car-card {
			 width: 140px;
			 height: 60px;
		 }
		.demo-card-square > .mdl-card__title {
			color: #fff;
			background:
					url('https://imageog.flaticon.com/icons/png/512/55/55283.png?size=1200x630f&pad=10,10,10,10&ext=png&bg=FFFFFFFF') center / cover;
		}
		 .admin-card {
			 width: 220px;
			 height: 20px;
			 text-align: center;
			 color: black;
		 }
	</style>
</head>
<body <#if user?? && user.isAdmin()>
    style="background: url('https://images.unsplash.com/photo-1557804506-669a67965ba0?ixlib=rb-1.2.1') center / cover"
</#if>
<#if !user??>
    style="background: url('https://images.unsplash.com/photo-1526666361175-e3595627c376?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1049&q=80') center / cover"
</#if>
>
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
	<header class="mdl-layout__header">
		<div class="mdl-layout__header-row">
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome <#if user?? && user.isAdmin()><font color="#ffd700">Admin</font></#if></span>
            <#if user?? && user.isAdmin()>
				<a class="mdl-navigation__link" href="/admin/users" style="cursor: pointer">Користувачі</a>
	            <a class="mdl-navigation__link" href="/admin/cars" style="cursor: pointer">Автомобілі</a>
	            <a class="mdl-navigation__link" href="/change-request" style="cursor: pointer">Запити</a>
            </#if>
			<div class="mdl-layout-spacer"></div>
			<#if user??>
				<a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
		    <#else>
			    <nav class="mdl-navigation mdl-layout--large-screen-only">
				    <a class="mdl-navigation__link" href="/registration">Реєстрація</a>
				    <a class="mdl-navigation__link" href="/login">Авторизація</a>
			    </nav>
			</#if>
		</div>
	</header>
	<#if user?? && user.isDriver()>
        <#include "menu.ftl">
	</#if>
	<main class="mdl-layout__content">
		<div class="page-content" <#if user?? && user.isAdmin()>style="display: flex; flex-flow: column; height: 100%;"</#if> >

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

			<#if !user??>
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

					<div class="mdl-cell mdl-cell--4-col" style="margin-left: 17%;">
						<div class="demo-card-square mdl-card mdl-shadow--2dp">
							<div class="mdl-card__supporting-text" style="text-align: center">
								Моніторте власні порушення
								<img width="160px" style="margin-top: 28px;" height="160px" alt="" src="https://getdrawings.com/free-icon/monitor-icon-png-53.png">
							</div>
						</div>
					</div>
					<div class="mdl-cell mdl-cell--4-col" style="margin-left: 5%; margin-right: 13%;">
						<div class="demo-card-square mdl-card mdl-shadow--2dp">
							<div class="mdl-card__supporting-text" style="text-align: center">
								Оплачуйте штрафи онлайн
								<img width="180px" style="margin-top: 8px;" height="180px" alt="" src="https://www.selectivepay.com/wp-content/uploads/2019/09/icon-payment-gateway.png">
							</div>
						</div>
					</div>

					<div style="font-family: 'Josefin Sans', sans-serif; margin-left: -10px; font-size: 18px; text-align: center; margin-top: 5%; margin-bottom: 3%; width: 100%;">
						<b>Palindrome</b>
					</div>

				</div>
			</#if>

			<#if user?? && user.isDriver()>
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
											<div class="demo-card-square mdl-card mdl-shadow--2dp car-card" onclick="window.location.href='/car/${car.id}'">
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
										<div class="demo-card-square mdl-card mdl-shadow--2dp car-card" onclick="window.location.href='/car/create'">
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
					</div>
					<div class="mdl-cell mdl-cell--1-col"></div>
					<div class="mdl-cell mdl-cell--4-col">
						<div class="demo-card-wide mdl-card mdl-shadow--2dp">
							<div class="mdl-card__title">
								<h2 class="mdl-card__title-text">Мої порушення</h2>
							</div>
							<#if !violations?? || violations?size == 0>
								<div class="mdl-card__supporting-text" style="padding: 20px; min-height: 100px; width: 512px;">
									<b>Немає порушень</b>
								</div>
							<#else>
								<div class="mdl-card__supporting-text" style="padding: 0; min-height: 100px; width: 512px;">
									<table class="mdl-data-table mdl-js-data-table mdl-data-table--selectable mdl-shadow--2dp" style="height: 100%; width: 100%;">
										<tbody>
										<#list violations as violation>
											<tr style="cursor: pointer" onclick="window.location.href='/violation/${violation.id}'">
												<td class="mdl-data-table__cell--non-numeric">${violation.violationType.name}</td>
												<td class="mdl-data-table__cell--non-numeric"><#if violation.dateTime.hour lt 10>0${violation.dateTime.hour}<#else>${violation.dateTime.hour}</#if>:<#if violation.dateTime.minute lt 10>0${violation.dateTime.minute}<#else>${violation.dateTime.minute}</#if> ${violation.dateTime.dayOfMonth}/${violation.dateTime.month.ordinal() + 1}/${violation.dateTime.year?c}</small></h4></td>
												<td class="mdl-data-table__cell--non-numeric"><#if violation.paid><b style="color: green;">Оплачено</b><#else><b style="color: red;">Очікує оплати</b></#if></td>
											</tr>
										</#list>
										</tbody>
									</table>
								</div>
							</#if>
							<div class="mdl-card__actions mdl-card--border">
								<a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" href="/violation">
									Переглянути всі
								</a>
							</div>
						</div>
					</div>
					<div class="mdl-cell mdl-cell--1-col"></div>
				</div>
			</#if>

			<#if user?? && user.isAdmin()>

				<div class="mdl-grid" style="margin-left: 10%; margin-right: 10%; margin-top: 5%;">
					<div class="mdl-cell mdl-cell--4-col">
						<div class="demo-card-square mdl-card mdl-shadow--2dp admin-card">
							<div class="mdl-card__supporting-text" style="margin-top: 12%;">
								<p><b style="font-size: 30px">${online_counter}</b></p>
								<p>Користувачів онлайн</p>
							</div>
						</div>
					</div>
					<div class="mdl-cell mdl-cell--4-col">
						<div class="demo-card-square mdl-card mdl-shadow--2dp admin-card">
							<div class="mdl-card__supporting-text" style="margin-top: 12%;">
								<p><b style="font-size: 30px">${users_count}</b></p>
								<p>Зареєстровано користувачів в системі</p>
							</div>
						</div>
					</div>
					<div class="mdl-cell mdl-cell--4-col">
						<div class="demo-card-square mdl-card mdl-shadow--2dp admin-card">
							<div class="mdl-card__supporting-text" style="margin-top: 12%;">
								<p><b style="font-size: 30px">${blocked_users_count}</b></p>
								<p>Заблокованих користувачів</p>
							</div>
						</div>
					</div>
				</div>

				<div style="width: 100%; min-height: 300px; flex-grow : 1; background: white; margin-top: 13%; font-family: 'Nunito', sans-serif;">
					<br/>
					<br/>
					<p style="font-size: 40px; margin-left: 10%;">Вітаємо, ${user.username}!</p>
                    <#if requests?? && requests gt 0>
	                    <#if requests == 1>
		                    <p style="font-size: 40px; margin-left: 10%;">1 запит очікує рішення</p>
	                    <#else>
		                    <p style="font-size: 40px; margin-left: 10%;">${requests} запитів очікують рішення</p>
	                    </#if>
                    <#else>
						<p style="font-size: 40px; margin-left: 10%;">Наразі запити відсутні</p>
                    </#if>
				</div>

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