<html>
<head>
	<title>Palindrome - Request Driver Info Change</title>
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
	<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
	<script
			src="https://code.jquery.com/jquery-3.4.1.js"
			integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
			crossorigin="anonymous"></script>
	<link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
	<style>
		.demo-card-image.mdl-card {
			width: 256px;
			height: 256px;
			background: url('data:image/png;base64, ${photo}') center / cover;
		}
		.demo-card-image {
			height: 52px;
			padding: 16px;
			background: rgba(0, 0, 0, 0.2);
		}
	</style>
</head>
<body>
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
	<header class="mdl-layout__header">
		<div class="mdl-layout__header-row">
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome <font color="#ffd700">Police</font>
			</span>
			<div class="mdl-layout-spacer"></div>
			<a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
		</div>
	</header>
	<main class="mdl-layout__content">
		<form class="page-content" action="/change-request/send/driver/${driver.id}" method="post" id="driver-form">
			<div class="mdl-layout mdl-js-layout mdl-layout--fixed-drawer">
				<div class="mdl-layout__drawer">
                    <span class="mdl-layout-title" style="margin-top: 20px">
                        <img src="/api/resources/img/logo.png" width="150px" height="50px">
                    </span>
					<nav class="mdl-navigation">
						<a class="mdl-navigation__link" href="/">Головна</a>
						<a class="mdl-navigation__link" href="/violation/police">Фіксовані порушення</a>
						<a class="mdl-navigation__link" href="/change-request/sent">Запити</a>
						<a class="mdl-navigation__link" href="">Профіль</a>
					</nav>
				</div>
				<main class="mdl-layout__content">
					<div class="page-content">
						<div class="mdl-grid" style="margin-top: 3%;">
							<div class="mdl-cell mdl-cell--2-col">
							</div>
							<div class="mdl-cell mdl-cell--3-col">
								<div class="demo-card-image mdl-card mdl-shadow--2dp">
									<div class="mdl-card__title mdl-card--expand"></div>
								</div>
							</div>
							<div class="mdl-cell mdl-cell--3-col">
								<div class="mdl-textfield mdl-js-textfield">
									<input class="mdl-textfield__input" value="${driver.firstName}" name="firstName" type="text" id="sample1" required>
									<label class="mdl-textfield__label" for="sample1">Ім'я</label>
								</div>
								<div class="mdl-textfield mdl-js-textfield">
									<input class="mdl-textfield__input" value="${driver.middleName}" name="middleName" type="text" id="sample2">
									<label class="mdl-textfield__label" for="sample2">По батькові</label>
								</div>
								<div class="mdl-textfield mdl-js-textfield">
									<input class="mdl-textfield__input" value="${driver.lastName}" name="lastName" type="text" id="sample3" required>
									<label class="mdl-textfield__label" for="sample3">Прізвище</label>
								</div>
							</div>
							<div class="mdl-cell mdl-cell--3-col"></div>
						</div>
						<div class="mdl-grid">
							<div class="mdl-cell mdl-cell--2-col">
							</div>
							<div class="mdl-cell mdl-cell--6-col">
								<div>
									<p><b>Адреса:</b>
									<div class="mdl-textfield mdl-js-textfield">
										<input class="mdl-textfield__input" value="${driver.address}" name="address" type="text" id="sample4" required>
										<label class="mdl-textfield__label" for="sample4">Адреса</label>
									</div>
									</p>
									<p><b>Номер посвідчення водія:</b>
									<div class="mdl-textfield mdl-js-textfield">
										<input class="mdl-textfield__input" value="${driver.licenceNumber}" name="licenceNumber" type="text" id="sample5" required>
										<label class="mdl-textfield__label" for="sample5">Номер посвідчення водія</label>
									</div>
									</p>
									<p>
									<div class="mdl-textfield mdl-js-textfield">
										<textarea class="mdl-textfield__input" name="comment" rows= "3" id="sample5" form="driver-form"><#if comment??>${comment}</#if></textarea>
										<label class="mdl-textfield__label" for="sample5">Коментар</label>
									</div>
									</p>
								</div>
								<div style="margin-top: 2%;">
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
									<input type="hidden" name="id" value="${driver.id}">
									<button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" type="submit">
										Підтвердити
									</button>
								</div>
							</div>
						</div>
					</div>
				</main>
		</form>
	</main>
	<form action="/logout" method="post" id="logout">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
</div>
</body>
</html>