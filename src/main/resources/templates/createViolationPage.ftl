<html>
<head>
	<title>Palindrome - Add Violation</title>
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
			width: 60%;
		}
		.demo-card-wide > .mdl-card__title {
			color: #fff;
			height: 200px;
			background: url('https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80') center / cover;
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
		<div class="page-content">

			<dialog class="mdl-dialog" style="z-index: 100000">
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
						<div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 10%; margin-top: 2%;">
							<div class="mdl-card__title">
								<h2 class="mdl-card__title-text">Реєстрація правопорушення</h2>
							</div>
							<div class="mdl-card__supporting-text">
								<form action="/violation/create" method="post">

									<p>
									<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
										<input class="mdl-textfield__input"
												<#if violation?? && violation.carNumber??>
													value="${violation.carNumber}"
												<#else>
													<#if number??>value="${number}"</#if>
												</#if> autocomplete="off" id="carNumber" name="carNumber" type="text">
										<label class="mdl-textfield__label" for="carNumber">Номер автомобіля</label>
									</div>
									</p>

									<p>
									<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" id="location-field">
										<input class="mdl-textfield__input" <#if violation?? && violation.location??>value="${violation.location}"</#if> id="location" name="location" type="text">
										<label class="mdl-textfield__label" id="location-label" for="location">Місце фіксування</label>
									</div>
									<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" id="geo-button" onclick="get_location()" style="margin-left: 5%;">
										Зчитати геолокацію
									</a>
									<#if locationError??>
										<p style="color: red">${locationError}</p>
									</#if>
									</p>

									<p>
									<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
										<select class="mdl-textfield__input" id="violationTypeId" name="violationTypeId">
											<option></option>
											<#list violation_types as violation_type>
												<option <#if violation?? && violation.violationTypeId?? && violation.violationTypeId == violation_type.id>selected</#if> value="${violation_type.id}">${violation_type.name}</option>
											</#list>
										</select>
										<label class="mdl-textfield__label" for="violationTypeId">Порушення</label>
									</div>
									<#if violationTypeIdError??>
										<p style="color: red">${violationTypeIdError}</p>
									</#if>
									</p>

									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
									<div style="margin-top: 4%;">
										<button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" type="submit">
											Підтвердити
										</button>
									</div>

								</form>
							</div>
						</div>
					</div>
				</main>
			</div>

		</div>
	</main>
</div>
<script>

	var geo_request_prefix = 'https://reverse.geocoder.ls.hereapi.com/6.2/reversegeocode.json?prox=';
	var geo_request_suffix = '&mode=retrieveAddresses&maxresults=1&gen=9&apiKey=cB0rD3P2fluVKeDwTAIGiRvrnHKhbXOkesVZn4Ht3zQ';

    var dialog = document.querySelector('dialog');
    dialog.querySelector('.close').addEventListener('click', function() {
        dialog.close();
    });

    $(function() {

        <#if error_message??>
            dialog.showModal();
        </#if>

        $("#location").keydown(function() {
            $("#location-label").removeAttr('hidden');
        });

    });

    function get_location() {
        if(navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(resolve_location);
        } else {
            window.alert('Ваш браузер не підтримує геопозиціювання');
        }
    }

    function resolve_location(position) {
        var latitude = position.coords.latitude;
        var longitude = position.coords.longitude;
        var coords = latitude + ',' + longitude;
        var geo_url = geo_request_prefix + coords + geo_request_suffix;
        $.ajax({
	        type : 'GET',
	        url : geo_url,
	        success: parse_geo_response
        });
    }

    function parse_geo_response(data) {
        var address = data.Response.View[0].Result[0].Location.Address;
        var location = address.Label;
        location = add_to_address_if_present(address.PostalCode, location);
        location = add_to_address_if_present(address.Street, location);
        $("#location").val(location);
        $("#location-label").attr('hidden', true);
    }

    function add_to_address_if_present(part, address) {
        if(part) {
            return address + ', ' + part;
        }
        return address;
    }

</script>
</body>
</html>