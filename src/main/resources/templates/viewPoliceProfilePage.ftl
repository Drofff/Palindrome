<html>
<head>
	<title>Palindrome - Police Info</title>
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

			<dialog class="mdl-dialog">
				<div class="mdl-dialog__content">
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

			<dialog class="mdl-dialog" id="block-dialog">
				<h4 class="mdl-dialog__title">Вкажіть причину блокування</h4>
				<div class="mdl-dialog__content">
					<form action="/admin/users/block" method="post" id="block-user">
						<input type="hidden" name="userId" id="block-user-id">
						<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
							<input class="mdl-textfield__input" type="text" id="reason" name="reason">
							<label class="mdl-textfield__label" for="reason">Причина</label>
						</div>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
					</form>
				</div>
				<div class="mdl-dialog__actions">
					<button type="button" class="mdl-button" onclick="$('#block-user').submit()">Заблокувати</button>
					<button type="button" class="mdl-button close">Закрити</button>
				</div>
			</dialog>

			<div class="mdl-grid" style="margin-top: 3%;">
				<div class="mdl-cell mdl-cell--3-col">
				</div>
				<div class="mdl-cell mdl-cell--3-col">
					<div class="demo-card-image mdl-card mdl-shadow--2dp">
						<div class="mdl-card__title mdl-card--expand"></div>
					</div>
				</div>
				<div class="mdl-cell mdl-cell--3-col" style="font-size: 20px">
					<b>${police.firstName} <#if police.middleName??>${police.middleName}</#if> ${police.lastName}</b>
				</div>
				<div class="mdl-cell mdl-cell--3-col"></div>
			</div>
			<div class="mdl-grid">
				<div class="mdl-cell mdl-cell--3-col">
				</div>
				<div class="mdl-cell mdl-cell--6-col">
					<div>
						<p><b>Посада:</b> ${police.position}</p>
						<p><b>Номер жетона:</b> ${police.tokenNumber}</p>
						<p><b>Департамент:</b> ${police.department.name}</p>
					</div>
					<div style="margin-top: 2%;">
                        <#if blocked?? && blocked>
							<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="unblock_user()" style="background-color: green">
								Розблокувати
							</a>
                        <#else>
							<a class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="block_user()" style="background: red">
								Заблокувати
							</a>
                        </#if>
					</div>
				</div>
			</div>
		</div>
	</main>
	<form action="/logout" method="post" id="logout">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
	<form action="/admin/users/unblock" method="post" id="unblock-user">
		<input type="hidden" name="id" id="unblock-user-id">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	</form>
</div>
<script>
    var dialog = document.querySelector('dialog');
    dialog.querySelector('.close').addEventListener('click', function() {
        dialog.close();
    });

    var form_dialog = document.getElementById('block-dialog');
    form_dialog.querySelector('.close').addEventListener('click', function() {
        form_dialog.close();
    });

    $(function() {
        <#if message??>
        dialog.showModal();
        </#if>
    });

    function block_user() {
        $('#block-user-id').val('${police.userId}');
        $('#reason').val('');
        form_dialog.showModal();
    }

    function unblock_user() {
        var result = window.confirm("Доступ користувачу ${police.firstName} буде відновлено. Ви впевнені?");
        if(result) {
            $("#unblock-user-id").val('${police.userId}');
            $("#unblock-user").submit();
        }
    }

</script>
</body>
</html>