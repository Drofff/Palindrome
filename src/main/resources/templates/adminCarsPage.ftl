<html>
<head>
	<title>Palindrome - Cars</title>
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
	<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
	<script
			src="https://code.jquery.com/jquery-3.4.1.js"
			integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
			crossorigin="anonymous"></script>
	<link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
	<style>
		.demo-list-two {
			width: 500px;
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
			<a class="mdl-navigation__link" href="/change-request" style="cursor: pointer">Запити</a>
			<a class="mdl-navigation__link" href="/admin/brand" style="cursor: pointer">Дані</a>
			<div class="mdl-layout-spacer"></div>
			<button id="demo-menu-lower-right" class="mdl-button mdl-js-button mdl-button--icon">
				<i class="material-icons">more_vert</i>
			</button>
			<ul class="mdl-menu mdl-menu--bottom-right mdl-js-menu mdl-js-ripple-effect" for="demo-menu-lower-right">
				<li class="mdl-menu__item" onclick="window.location.href='/change-password'">Змінити пароль</li>
				<li class="mdl-menu__item" onclick="$('#logout').submit()">Вихід</li>
			</ul>
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

			<div class="mdl-grid" style="margin-left: 10%; margin-top: 2%;">
				<div class="mdl-cell mdl-cell--4-col">
					<form action="/admin/cars" method="get" id="search">
						<h4>Фільтри</h4>
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<input class="mdl-textfield__input" type="text" <#if pattern?? && pattern.number??>value="${pattern.number}" </#if> name="number" id="number">
							<label class="mdl-textfield__label" for="number">Номер</label>
						</div>
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<input class="mdl-textfield__input" type="text" <#if pattern?? && pattern.bodyNumber??>value="${pattern.bodyNumber}" </#if> name="bodyNumber" id="bodyNumber">
							<label class="mdl-textfield__label" for="bodyNumber">VIN код</label>
						</div>
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<input class="mdl-textfield__input" type="text" <#if pattern?? && pattern.model??>value="${pattern.model}" </#if> name="model" id="model">
							<label class="mdl-textfield__label" for="model">Модель</label>
						</div>
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<select class="mdl-textfield__input" id="brandId" name="brandId">
								<option value=""></option>
                                <#list brands as brand>
									<option value="${brand.id}" <#if pattern?? && pattern.brandId?? && pattern.brandId == brand.id>selected</#if> >${brand.name}</option>
                                </#list>
							</select>
							<label class="mdl-textfield__label" for="brandId">Марка</label>
						</div>
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<select class="mdl-textfield__input" id="bodyTypeId" name="bodyTypeId">
								<option value=""></option>
                                <#list body_types as body_type>
									<option value="${body_type.id}" <#if pattern?? && pattern.bodyTypeId?? && pattern.bodyTypeId == body_type.id>selected</#if> >${body_type.name}</option>
                                </#list>
							</select>
							<label class="mdl-textfield__label" for="bodyTypeId">Тип кузова</label>
						</div>
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<select class="mdl-textfield__input" id="licenceCategoryId" name="licenceCategoryId">
								<option value=""></option>
                                <#list licence_categories as licence_category>
									<option value="${licence_category.id}" <#if pattern?? && pattern.licenceCategoryId?? && pattern.licenceCategoryId == licence_category.id>selected</#if> >${licence_category.name}</option>
                                </#list>
							</select>
							<label class="mdl-textfield__label" for="licenceCategoryId">Категорія</label>
						</div>
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<select class="mdl-textfield__input" id="engineTypeId" name="engineTypeId">
								<option value=""></option>
                                <#list engine_types as engine_type>
									<option value="${engine_type.id}" <#if pattern?? && pattern.engineTypeId?? && pattern.engineTypeId == engine_type.id>selected</#if> >${engine_type.name}</option>
                                </#list>
							</select>
							<label class="mdl-textfield__label" for="engineTypeId">Тип двигуна</label>
						</div>
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<select class="mdl-textfield__input" id="ownerId" name="ownerId">
								<option value=""></option>
                                <#list drivers as driver>
									<option value="${driver.id}" <#if pattern?? && pattern.ownerId?? && pattern.ownerId == driver.id>selected</#if> >${driver.firstName} ${driver.lastName} (${driver.user.username})</option>
                                </#list>
							</select>
							<label class="mdl-textfield__label" for="ownerId">Власник</label>
						</div>
						<input type="hidden" name="page" id="page_param">
						<div>
							<button class="mdl-button mdl-js-button mdl-button--primary" type="submit">
								Пошук
							</button>
						</div>
					</form>
				</div>
				<div class="mdl-cell mdl-cell--8-col">
                    <#if !cars?? || cars?size == 0>
						<h4 style="margin-top: 2%; margin-left: 20%;">Записи відсутні</h4>
                    </#if>

					<ul class="demo-list-two mdl-list">
                        <#list cars as car>
							<li class="mdl-list__item mdl-list__item--two-line">
					    <span class="mdl-list__item-primary-content">
					      <i class="material-icons mdl-list__item-avatar">directions_car</i>
					      <span onclick="window.location.href='/admin/cars/${car.id}'" style="cursor: pointer">${car.brand.name} ${car.model} (${car.number})</span>
					      <span class="mdl-list__item-sub-title">${car.bodyType.name} (${car.licenceCategory.name})</span>
					    </span>
							</li>
                        </#list>
					</ul>
					<div style="margin-top: 5%;">
                        <#if cars?? && cars?size gt 0>
                            <#include "pagination.ftl">
                        </#if>
					</div>
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

	function next_page() {
		var page = ${page_number};
		var total_pages = ${pages_count};
		if(page < (total_pages - 1)) {
		    var next_page = page + 1;
		    $("#page_param").val(next_page);
		    $("#search").submit();
		}
	}

	function prev_page() {
		var page = ${page_number};
		if(page > 0) {
		    var prev_page = page - 1;
		    $("#page_param").val(prev_page);
		    $("#search").submit();
		}
	}

</script>
</body>
</html>