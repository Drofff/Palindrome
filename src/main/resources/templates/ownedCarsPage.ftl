<html>
<head>
	<title>Palindrome - My Cars</title>
	<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
	<script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
	<script
			src="https://code.jquery.com/jquery-3.4.1.js"
			integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
			crossorigin="anonymous"></script>
	<link href="https://fonts.googleapis.com/css?family=Josefin+Sans&display=swap" rel="stylesheet">
	<style>
		.demo-card-wide > .mdl-card__title {
			color: #fff;
			height: 60px;
			background: #3E4EB8;
		}
		.demo-card-square.mdl-card {
			width: 180px;
			height: 60px;
		}
		.demo-card-square > .mdl-card__title {
			color: #fff;
			background:
					url('https://imageog.flaticon.com/icons/png/512/55/55283.png?size=1200x630f&pad=10,10,10,10&ext=png&bg=FFFFFFFF') center / cover;
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

			<div style="margin-left: 28%; margin-top: 2%;">

				<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" href="/car/create" style="margin-bottom: 3%;">
					Додати
				</a>

				<#assign counter = page_payload?size - 1>
	            <#list page_payload as car>
		            <div class="mdl-grid">
			            <div class="mdl-cell mdl-cell--4-col">
				            <div class="mdl-cell mdl-cell--4-col">
					            <div class="demo-card-square mdl-card mdl-shadow--2dp">
						            <div class="mdl-card__title mdl-card--expand">
						            </div>
					            </div>
				            </div>
			            </div>
			            <div class="mdl-cell mdl-cell--4-col">
				            <p><b>Марка:</b> ${car.brand.name}</p>
				            <p><b>Модель:</b> ${car.model}</p>
				            <p><b>Номер:</b> ${car.number}</p>
				            <p>
					            <a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" href="/car/${car.id}">
						            Переглянути
					            </a>
				            </p>
			            </div>
		            </div>
		            <#if counter gt 0>
		                <hr style="width: 50%; margin-left: 20px;"/>
		            </#if>
                    <#assign counter -= 1>
	            </#list>
				<div style="margin-left: 13%; margin-bottom: 2%; margin-top: 3%;">
				<#include "pagination.ftl">
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
            window.location.href = '/car?page=' + next_page;
        }
	}

	function prev_page() {
        var page = ${page_number};
        if(page > 0) {
            var previous_page = page - 1;
            window.location.href = '/car?page=' + previous_page;
        }
	}

</script>
</body>
</html>