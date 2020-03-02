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
							<tr <#if !(car.number == changed_car.number)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Номер</th>
								<th>${car.number}</th>
								<th>${changed_car.number}</th>
							</tr>
							<tr <#if !(car.bodyNumber == changed_car.bodyNumber)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">VIN код</th>
								<th>${car.bodyNumber}</th>
								<th>${changed_car.bodyNumber}</th>
							</tr>
							<tr <#if !(car.brand.id == changed_car.brand.id)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Марка</th>
								<th>${car.brand.name}</th>
								<th>${changed_car.brand.name}</th>
							</tr>
							<tr <#if !(car.model == changed_car.model)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Модель</th>
								<th>${car.model}</th>
								<th>${changed_car.model}</th>
							</tr>
							<tr <#if !(car.bodyType.id == changed_car.bodyType.id)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Тип кузова</th>
								<th>${car.bodyType.name}</th>
								<th>${changed_car.bodyType.name}</th>
							</tr>
							<tr <#if !(car.color == changed_car.color)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Колір</th>
								<th>${car.color}</th>
								<th>${changed_car.color}</th>
							</tr>
							<tr <#if !(car.weight == changed_car.weight)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Вага</th>
								<th>${car.weight}</th>
								<th>${changed_car.weight}</th>
							</tr>
							<tr <#if !(car.licenceCategory.id == changed_car.licenceCategory.id)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Категорія</th>
								<th>${car.licenceCategory.name}</th>
								<th>${changed_car.licenceCategory.name}</th>
							</tr>
							<tr <#if !(car.engineVolume == changed_car.engineVolume)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Об'єм двигуна</th>
								<th>${car.engineVolume}</th>
								<th>${changed_car.engineVolume}</th>
							</tr>
							<tr <#if !(car.engineType.id == changed_car.engineType.id)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Тип двигуна</th>
								<th>${car.engineType.name}</th>
								<th>${changed_car.engineType.name}</th>
							</tr>
							<tr <#if !(car.registrationDate == changed_car.registrationDate)>style="background: rgb(139, 195, 74);"</#if>>
								<th class="mdl-data-table__cell--non-numeric">Дата реєстрації</th>
								<th>${car.registrationDate}</th>
								<th>${changed_car.registrationDate}</th>
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
			</div>

		</div>
	</main>
	<form action="/logout" method="post" id="logout">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
	<form action="/change-request/refuse" method="post" id="refuse-form">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
		<input type="hidden" name="id" value="${request_id}">
	</form>
	<form action="/change-request/approve" method="post" id="approve-form">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
		<input type="hidden" name="id" value="${request_id}">
	</form>
</div>
</body>
</html>