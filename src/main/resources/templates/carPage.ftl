<html>
<head>
	<title>Palindrome - ${car.number}</title>
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
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome</span>
			<div class="mdl-layout-spacer"></div>
			<a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
		</div>
	</header>
	<#include "menu.ftl">
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
	                        <#if message == "Saved changes">
		                        Зміни збережено
	                        <#else>
                                ${message}
	                        </#if>
                        </#if>
					</p>
				</div>
				<div class="mdl-dialog__actions">
					<button type="button" class="mdl-button close">Закрити</button>
				</div>
			</dialog>

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
					<p>Правопорушень: (КІЛЬКІСТЬ)</p>
					<p>Останнє: (ДАТА)</p>
					<p>Оплачено: (КІЛЬКІСТЬ)</p>
					<p>Очікує опалати: (КІЛЬКІСТЬ)</p>
					<p>
						<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" href="/car/update/${car.id}">
							Редагувати
						</a>
						<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="delete_car()">
							Видалити
						</a>
					</p>
				</div>
			</div>

		</div>
	</main>
</div>
<form action="/logout" method="post" id="logout">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>
<form action="/car/delete/${car.id}" method="post" id="delete-car">
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

    function delete_car() {
        var result = window.confirm('Ви впевнені що бажаєте видалити поточне авто? Ця дія безоборотна');
        if(result) {
            $("#delete-car").submit();
        }
    }

</script>
</body>
</html>