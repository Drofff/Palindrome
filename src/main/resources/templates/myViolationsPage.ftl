<html>
<head>
	<title>Palindrome - My Violations</title>
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
	<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
	<script
			src="https://code.jquery.com/jquery-3.4.1.js"
			integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
			crossorigin="anonymous"></script>
	<link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
	<style>
		.demo-card-event.mdl-card {
			width: 256px;
			height: 256px;
			float: left;
			margin: 1rem;
			position: relative;
		}
		.demo-card-event > .mdl-card__title {
			align-items: flex-start;
		}
		.demo-card-event > .mdl-card__title > h4 {
			margin-top: 0;
		}
		.demo-card-event > .mdl-card__actions {
			display: flex;
			box-sizing:border-box;
			align-items: center;
		}
	</style>
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
                            ${message}
                        </#if>
					</p>
				</div>
				<div class="mdl-dialog__actions">
					<button type="button" class="mdl-button close">Закрити</button>
				</div>
			</dialog>

			<div style="margin-top: 2%; margin-left: 5%; margin-right: 5%;">

				<form action="/violation" method="get" id="filter-form" style="margin-bottom: 3%;">
					<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
						<select class="mdl-textfield__input" id="carId" name="carId">
							<option value="">Всі автомобілі</option>
                            <#list cars as car>
								<option value="${car.id}" <#if car_id?? && car.id == car_id>selected</#if> >${car.brand.name} ${car.model} (${car.number})</option>
                            </#list>
						</select>
					</div>
				</form>


                <#if !violations?? || violations?size == 0>
					<h4 style="margin-top: 2%; margin-left: 20%;">Записи відсутні</h4>
                </#if>

				<#list violations as violation>
					<div class="demo-card-event mdl-card mdl-shadow--2dp" style="<#if violation.paid>background: green;</#if>">
						<div class="mdl-card__title mdl-card--expand" style="background: rgb(255,255,255); margin-top: 1%; margin-left: 1%; margin-right: 1%; cursor: pointer"
						     onclick="window.location.href='/violation/${violation.id}'">
							<h4>${violation.violationType.name}<br/><small>
							<#if violation.dateTime.hour lt 10>0${violation.dateTime.hour}<#else>${violation.dateTime.hour}</#if>:<#if violation.dateTime.minute lt 10>0${violation.dateTime.minute}<#else>${violation.dateTime.minute}</#if> ${violation.dateTime.dayOfMonth}/${violation.dateTime.month.ordinal() + 1}/${violation.dateTime.year?c}</small></h4>
						</div>
						<div class="mdl-card__actions mdl-card--border" style="background: rgb(255,255,255);
						 margin-bottom: 1%; margin-left: 1%; margin-right: 1%; width: 98%; <#if violation.paid>border-color: green;<#else>border-color: #3E4EB8;</#if>">
							<#if violation.paid>
								<a class="mdl-button mdl-button--colored" style="color: green">
									Оплачено
								</a>
							<#else>
							    <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" href="/pay/${violation.id}">
								    Оплатити
							    </a>
							</#if>
						</div>
					</div>
				</#list>
			</div>
			<div style="margin-left: 37%; margin-top: 2%; bottom: 20%; position: absolute">
                <#include "pagination.ftl">
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

	    $("#carId").change(function() {
	        $("#filter-form").submit();
	    });

    });

	function next_page() {
	    var page = ${page_number};
	    var total_pages = ${pages_count};
	    if(page < (total_pages - 1)) {
	        var next_page = page + 1;
	        window.location.href = '/violation?page=' + next_page;
	    }
	}

	function prev_page() {
	    var page = ${page_number};
	    if(page > 0) {
	        var prev_page = page - 1;
	        window.location.href = '/violation?page=' + prev_page;
	    }
	}

</script>
</body>
</html>