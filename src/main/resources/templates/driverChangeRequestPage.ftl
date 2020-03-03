<html>
<head>
	<title>Palindrome - Change Request</title>
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
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome <font color="#ffd700">Admin</font></span>
			<a class="mdl-navigation__link" href="/" style="cursor: pointer">Головна</a>
			<a class="mdl-navigation__link" href="/admin/users" style="cursor: pointer">Користувачі</a>
			<a class="mdl-navigation__link" href="/admin/cars" style="cursor: pointer">Автомобілі</a>
			<a class="mdl-navigation__link" href="/change-request" style="cursor: pointer">Запити</a>
			<div class="mdl-layout-spacer"></div>
			<a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
		</div>
	</header>
	<main class="mdl-layout__content">
		<div class="page-content">

			<div class="mdl-grid" style="margin-left: 15%; margin-top: 5%;">
				<div class="mdl-cell mdl-cell--4-col">
					<table class="mdl-data-table mdl-js-data-table">
						<thead>
						<tr>
							<th class="mdl-data-table__cell--non-numeric">Назва</th>
							<th>Актуальні дані</th>
							<th>Запропоновані дані</th>
						</tr>
						</thead>
						<tbody>
							<tr <#if !(driver.firstName == changed_driver.firstName)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Ім'я</th>
								<th>${driver.firstName}</th>
								<th>${changed_driver.firstName}</th>
							</tr>
							<tr <#if !(driver.lastName == changed_driver.lastName)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Прізвище</th>
								<th>${driver.lastName}</th>
								<th>${changed_driver.lastName}</th>
							</tr>
							<tr <#if !(driver.middleName == changed_driver.middleName)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">По батькові</th>
								<th>${driver.middleName}</th>
								<th>${changed_driver.middleName}</th>
							</tr>
							<tr <#if !(driver.address == changed_driver.address)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Адреса</th>
								<th>${driver.address}</th>
								<th>${changed_driver.address}</th>
							</tr>
							<tr <#if !(driver.licenceNumber == changed_driver.licenceNumber)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Номер посвідчення водія</th>
								<th>${driver.licenceNumber}</th>
								<th>${changed_driver.licenceNumber}</th>
							</tr>
						</tbody>
					</table>

					<div style="margin-top: 5%;">
						<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" style="margin-right: 5%; background-color: red" onclick="$('#refuse-form').submit();">
							Відхилити
						</a>
						<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="$('#approve-form').submit();">
							Підтвердити
						</a>
					</div>

				</div>
				<div class="mdl-cell mdl-cell--4-col" style="margin-left: 10%;">
					<div>
						<img src="data:img/png;base64, ${photo}" style="border-radius: 50%; margin-right: 3%;" width="40px" height="40px" alt=" ">
                        ${police.firstName} ${police.lastName}
					</div>
					<div style="margin-top: 3%;">
                        <#if change_request.comment?? && change_request.comment?has_content>
							<p>Коментар: ${change_request.comment}</p>
                        </#if>
						<p>
							Отримано: <#if change_request.dateTime.hour lt 10>0${change_request.dateTime.hour}<#else>${change_request.dateTime.hour}</#if>:<#if change_request.dateTime.minute lt 10>0${change_request.dateTime.minute}<#else>${change_request.dateTime.minute}</#if>, ${change_request.dateTime.dayOfMonth} of ${change_request.dateTime.month.name()?capitalize} ${change_request.dateTime.year?c}
						</p>
					</div>
				</div>
			</div>

		</div>
	</main>
	<form action="/logout" method="post" id="logout">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
	<form action="/change-request/refuse" method="post" id="refuse-form">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
		<input type="hidden" name="id" value="${change_request.id}">
	</form>
	<form action="/change-request/approve" method="post" id="approve-form">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
		<input type="hidden" name="id" value="${change_request.id}">
	</form>
</div>
</body>
</html>