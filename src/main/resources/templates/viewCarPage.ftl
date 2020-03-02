<html>
<head>
	<title>Palindrome - Car Info</title>
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
						<tr>
							<th class="mdl-data-table__cell--non-numeric">Номер</th>
							<th>${car.number}</th>
						</tr>
						<tr>
							<th class="mdl-data-table__cell--non-numeric">VIN код</th>
							<th>${car.bodyNumber}</th>
						</tr>
						<tr>
							<th class="mdl-data-table__cell--non-numeric">Марка</th>
							<th>${car.brand.name}</th>
						</tr>
						<tr>
							<th class="mdl-data-table__cell--non-numeric">Модель</th>
							<th>${car.model}</th>
						</tr>
						<tr>
							<th class="mdl-data-table__cell--non-numeric">Тип кузова</th>
							<th>${car.bodyType.name}</th>
						</tr>
						<tr>
							<th class="mdl-data-table__cell--non-numeric">Колір</th>
							<th>${car.color}</th>
						</tr>
						<tr>
							<th class="mdl-data-table__cell--non-numeric">Вага</th>
							<th>${car.weight}</th>
						</tr>
						<tr>
							<th class="mdl-data-table__cell--non-numeric">Категорія</th>
							<th>${car.licenceCategory.name}</th>
						</tr>
						<tr>
							<th class="mdl-data-table__cell--non-numeric">Об'єм двигуна</th>
							<th>${car.engineVolume}</th>
						</tr>
						<tr>
							<th class="mdl-data-table__cell--non-numeric">Тип двигуна</th>
							<th>${car.engineType.name}</th>
						</tr>
						<tr>
							<th class="mdl-data-table__cell--non-numeric">Дата реєстрації</th>
							<th>${car.registrationDate}</th>
						</tr>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="mdl-cell mdl-cell--4-col">
					<p style="margin-bottom: 10%;">
						<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" href="/admin/cars/update/${car.id}">
							Редагувати
						</a>
					</p>
					<p>Власник:</p>
					<p>
						<img src="data:img/png;base64, ${driver.photoUri}" style="border-radius: 50%; margin-right: 3%;" width="40px" height="40px" alt=" ">
						<span onclick="window.location.href='/admin/users/driver/${driver.id}'" style="cursor: pointer">${driver.firstName} ${driver.lastName}</span>
					</p>
				</div>
			</div>

		</div>
	</main>
	<form action="/logout" method="post" id="logout">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
</div>
</body>
</html>