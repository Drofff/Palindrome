<html>
<head>
	<title>Palindrome - Edit Car</title>
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
	<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
	<script
			src="https://code.jquery.com/jquery-3.4.1.js"
			integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
			crossorigin="anonymous"></script>
	<link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
	<script src="https://kit.fontawesome.com/79433ece4b.js" crossorigin="anonymous"></script>
	<style>
		.demo-card-wide.mdl-card {
			width: 60%;
		}
		.demo-card-wide > .mdl-card__title {
			color: #fff;
			height: 200px;
			background: url('/api/resources/img/background_wireframe.jpg') center / cover;
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
			<a class="mdl-navigation__link" href="/change-request" style="cursor: pointer">Запити</a>
			<a class="mdl-navigation__link" href="/admin/brand" style="cursor: pointer">Дані</a>
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
				</div>
				<div class="mdl-dialog__actions">
					<button type="button" class="mdl-button close">Закрити</button>
				</div>
			</dialog>

			<div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 22%; margin-top: 5%;">
				<div class="mdl-card__title">
					<h2 class="mdl-card__title-text">Редагування даних автомобіля</h2>
				</div>
				<div class="mdl-card__supporting-text">
					<form action="/admin/cars/update/${car.id}" method="post" style="margin-left: 5%;">

						<div id="general-info">
							<div>
								<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
									<input class="mdl-textfield__input" <#if car.number??>value="${car.number}"</#if> type="text" name="number" id="number">
									<label class="mdl-textfield__label" for="number">Номер автомобіля</label>
								</div>
                                <#if numberError??>
									<p style="color: red">${numberError}</p>
                                </#if>

								<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
									<input class="mdl-textfield__input" <#if car.bodyNumber??>value="${car.bodyNumber}"</#if> type="text" name="bodyNumber" id="bodyNumber">
									<label class="mdl-textfield__label" for="bodyNumber">VIN код</label>
								</div>
                                <#if bodyNumberError??>
									<p style="color: red">${bodyNumberError}</p>
                                </#if>
							</div>

							<div>
								<label><b>Дата першої реєстрації</b></label><br/>
								<div class="mdl-textfield mdl-js-textfield" style="margin-top: -1%;">
									<input class="mdl-textfield__input" <#if car.registrationDate??>value="${car.registrationDate}"</#if> type="date" id="registrationDate" name="registrationDate">
								</div>
							</div>
                            <#if registrationDateError??>
								<p style="color: red">${registrationDateError}</p>
                            </#if>

							<div>
								<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
									<select class="mdl-textfield__input" id="licenceCategoryId" name="licenceCategoryId">
										<option></option>
                                        <#list licence_categories as licence_category>
											<option <#if car.licenceCategoryId?? && car.licenceCategoryId == licence_category.id>selected</#if> value="${licence_category.id}">${licence_category.name}</option>
                                        </#list>
									</select>
									<label class="mdl-textfield__label" for="licenceCategoryId">Категорія автомобіля</label>
								</div>
                                <#if licenceCategoryIdError??>
									<p style="color: red">${licenceCategoryIdError}</p>
                                </#if>
								<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
									<input class="mdl-textfield__input" <#if car.color??>value="${car.color}"</#if> type="text" name="color" id="color">
									<label class="mdl-textfield__label" for="color">Колір</label>
								</div>
                                <#if colorError??>
									<p style="color: red">${colorError}</p>
                                </#if>
							</div>
							<div style="margin-top: 4%;">
								<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="to_tech_info()">
									Далі
								</a>
							</div>
						</div>

						<div id="tech-info" hidden>
							<div>
								<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
									<select class="mdl-textfield__input" id="bodyTypeId" name="bodyTypeId">
										<option></option>
                                        <#list body_types as body_type>
											<option <#if car.bodyTypeId?? && car.bodyTypeId == body_type.id>selected</#if> value="${body_type.id}">${body_type.name}</option>
                                        </#list>
									</select>
									<label class="mdl-textfield__label" for="bodyTypeId">Тип кузова</label>
								</div>
                                <#if bodyTypeIdError??>
									<p style="color: red">${bodyTypeIdError}</p>
                                </#if>

								<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
									<input class="mdl-textfield__input" <#if car.weight??>value="${car.weight}"</#if> type="number" min="0" name="weight" id="weight">
									<label class="mdl-textfield__label" for="weight">Повна вага</label>
								</div>
                                <#if weightError??>
									<p style="color: red">${weightError}</p>
                                </#if>
							</div>

							<div>
								<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
									<select class="mdl-textfield__input" id="engineTypeId" name="engineTypeId">
										<option></option>
                                        <#list engine_types as engine_type>
											<option <#if car.engineTypeId?? && car.engineTypeId == engine_type.id>selected</#if> value="${engine_type.id}">${engine_type.name}</option>
                                        </#list>
									</select>
									<label class="mdl-textfield__label" for="engineTypeId">Тип двигуна</label>
								</div>
                                <#if engineTypeIdError??>
									<p style="color: red">${engineTypeIdError}</p>
                                </#if>

								<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
									<input class="mdl-textfield__input" step="0.1" <#if car.engineVolume??>value="${car.engineVolume}"</#if> type="number" min="0" name="engineVolume" id="engineVolume">
									<label class="mdl-textfield__label" for="engineVolume">Об'єм двигуна</label>
								</div>
                                <#if engineVolumeError??>
									<p style="color: red">${engineVolumeError}</p>
                                </#if>
							</div>

							<div style="margin-top: 4%;">
								<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="back_to_general_info()">
									Назад
								</a>
								<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="to_vendor_info()">
									Далі
								</a>
							</div>

						</div>

						<div id="vendor-info" hidden>

							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<select class="mdl-textfield__input" id="brandId" name="brandId">
									<option></option>
                                    <#list brands as brand>
										<option <#if car.brandId?? && car.brandId == brand.id>selected</#if> value="${brand.id}">${brand.name}</option>
                                    </#list>
								</select>
								<label class="mdl-textfield__label" for="brandId">Марка виробника</label>
							</div>
                            <#if brandError??>
								<p style="color: red">${brandError}</p>
                            </#if>

							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<input class="mdl-textfield__input" <#if car.model??>value="${car.model}" </#if> type="text" name="model" id="model">
								<label class="mdl-textfield__label" for="model">Модельний ряд</label>
							</div>
                            <#if modelError??>
								<p style="color: red">${modelError}</p>
                            </#if>

							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
							<div style="margin-top: 4%;">
								<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="back_to_tech_info()">
									Назад
								</a>
								<button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" type="submit">
									Зберегти
								</button>
							</div>
						</div>

					</form>
				</div>
			</div>

		</div>
	</main>
	<form action="/logout" method="post" id="logout">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
</div>
<script>
    var dialog = document.querySelector('dialog');
    dialog.querySelector('.close').addEventListener('click', function() {
        dialog.close();
    });

    $(function() {
        <#if error_message??>
        dialog.showModal();
        </#if>
    });

    function to_tech_info() {
        var number = $("#number").val();
        var bodyNumber = $("#bodyNumber").val();
        var regDate = $("#registrationDate").val();
        var licenceCategory = $("#licenceCategoryId").val();
        var color = $("#color").val();
        var args = [ number, bodyNumber, regDate, licenceCategory, color ];
        if(!is_any_blank(args)) {
            $("#general-info").attr('hidden', true);
            $("#tech-info").removeAttr('hidden');
        } else {
            window.alert('Будь ласка, заповніть всі поля аби продовжити');
        }
    }

    function back_to_general_info() {
        $("#tech-info").attr('hidden', true);
        $("#general-info").removeAttr('hidden');
    }

    function to_vendor_info() {
        var bodyType = $("#bodyTypeId").val();
        var weight = $("#weight").val();
        var engineType = $("#engineTypeId").val();
        var engineVol = $("#engineVolume").val();
        var args = [bodyType, weight, engineType, engineVol];
        if(!is_any_blank(args)) {
            $("#tech-info").attr('hidden', true);
            $("#vendor-info").removeAttr('hidden');
        } else {
            window.alert('Будь ласка, заповніть всі поля аби продовжити');
        }
    }

    function back_to_tech_info() {
        $("#vendor-info").attr('hidden', true);
        $("#tech-info").removeAttr('hidden');
    }

    function is_any_blank(args) {
        var has_blank = false;
        args.forEach(function(arg) {
            if(!arg || arg.length === 0) {
                has_blank = true;
            }
        });
        return has_blank;
    }

</script>
</body>
</html>