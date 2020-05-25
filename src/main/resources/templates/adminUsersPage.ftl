<html>
<head>
	<title>Palindrome - Users</title>
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
			<a class="mdl-navigation__link" href="/admin/cars" style="cursor: pointer">Автомобілі</a>
			<a class="mdl-navigation__link" href="/change-request" style="cursor: pointer">Запити</a>
			<a class="mdl-navigation__link" href="/admin/brand" style="cursor: pointer">Дані</a>
			<div class="mdl-layout-spacer"></div>
			<a class="mdl-navigation__link" onclick="$('#logout').submit()">Вихід</a>
		</div>
	</header>
	<main class="mdl-layout__content">
		<div class="page-content">

			<dialog class="mdl-dialog" id="msg-dialog">
				<div class="mdl-dialog__content">
					<p style="color: red">
                        <#if error_message??>
                            ${error_message}
                        </#if>
					</p>
					<p>
                        <#if message??>
	                        <#if message == "Successfully blocked user">
		                        Успішно заблоковано доступ користувачу
	                        <#else>
		                        <#if message == "Successfully unblocked user">
			                        Успішно відновлено доступ користувачу
	                            <#else>
                                    ${message}
		                        </#if>
	                        </#if>
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

			<div class="mdl-grid" style="margin-left: 2%; margin-top: 2%;">
				<div class="mdl-cell mdl-cell--2-col">
					<a class="mdl-button mdl-js-button mdl-button--primary" href="/admin/users/create" style="margin-top: 10%;">
						Створити користувача
					</a>
				</div>
				<div class="mdl-cell mdl-cell--10-col">
					<form action="/admin/users" method="get" id="search">
						<div>
							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<input class="mdl-textfield__input" <#if pattern?? && pattern.username??>value="${pattern.username}"</#if> type="text" id="email" name="username">
								<label class="mdl-textfield__label" for="email">Пошта</label>
							</div>

							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<select class="mdl-textfield__input" id="role" name="role">
									<option value=""></option>
                                    <#list roles as role>
										<option <#if pattern?? && pattern.role?? && pattern.role.name() == role.name()>selected</#if> value="${role.name()}">${role.name()?capitalize}</option>
                                    </#list>
								</select>
								<label class="mdl-textfield__label" for="role">Роль</label>
							</div>

							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<select class="mdl-textfield__input" id="active" name="active">
									<option value=""></option>
									<option <#if pattern?? && pattern.active?? && pattern.active>selected</#if> value="true">Так</option>
									<option <#if pattern?? && pattern.active?? && !pattern.active>selected</#if> value="false">Ні</option>
								</select>
								<label class="mdl-textfield__label" for="active">Активований</label>
							</div>

							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<select class="mdl-textfield__input" id="blocked" name="blocked">
									<option value=""></option>
									<option <#if pattern?? && pattern.blocked?? && pattern.blocked>selected</#if> value="true">Так</option>
									<option <#if pattern?? && pattern.blocked?? && !pattern.blocked>selected</#if> value="false">Ні</option>
								</select>
								<label class="mdl-textfield__label" for="active">Заблокований</label>
							</div>
							<input type="hidden" name="page" id="page_param">

						</div>

						<div>
							<button class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" type="submit">
								Пошук
							</button>
						</div>

					</form>
				</div>
			</div>

            <#if !users?? || users?size == 0>
				<h4 style="margin-top: 2%; margin-left: 20%;">Записи відсутні</h4>
            </#if>

			<ul class="demo-list-two mdl-list" style="margin-left: 30%; margin-top: 2%;">
                <#list users as user>
					<li class="mdl-list__item mdl-list__item--two-line">
					    <span class="mdl-list__item-primary-content">
					      <i class="material-icons mdl-list__item-avatar">person</i>
					      <span <#if user.isDriver()>onclick="window.location.href='/admin/users/driver/${user.id}?type=USER_ID'"</#if>
							      <#if user.isPolice()>onclick="window.location.href='/admin/users/police/${user.id}'"</#if>
							    style="cursor: pointer">${user.username}</span>
						    <#if !user.enabled>
							    <span class="mdl-list__item-sub-title">Не активований</span>
						    <#else>
							    <#if user.blocked>
								    <span class="mdl-list__item-sub-title" style="color: red">Заблокований</span>
						        <#else>
							        <span class="mdl-list__item-sub-title">
									    <#if user.isDriver()>
										    Водій
	                                    <#else>
	                                        <#if user.isPolice()>
											    Поліцейський
	                                        </#if>
	                                    </#if>
								        <#if user.isAdmin()>Адміністратор</#if>
							        </span>
							    </#if>
						    </#if>
					    </span>
						<span class="mdl-list__item-secondary-content">
							<#if !user.blocked>
								<a class="mdl-list__item-secondary-action" onclick="block_user('${user.id}')" style="cursor: pointer"><i class="material-icons" style="color: red">cancel</i></a>
						    <#else>
							    <a class="mdl-list__item-secondary-action" onclick="unblock_user('${user.id}', '${user.username}')" style="cursor: pointer"><i class="material-icons" style="color: green">autorenew</i></a>
							</#if>
					    </span>
					</li>
				</#list>
			</ul>
			<#if users?size gt 0>
				<div style="margin-left: 37%; margin-top: 5%;">
                    <#include "pagination.ftl">
				</div>
			</#if>
		</div>
	</main>
</div>
<form action="/logout" method="post" id="logout">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>
<form action="/admin/users/unblock" method="post" id="unblock-user">
	<input type="hidden" name="id" id="unblock-user-id">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>
<script>

    var dialog = document.getElementById('msg-dialog');
    dialog.querySelector('.close').addEventListener('click', function() {
        dialog.close();
    });

    var form_dialog = document.getElementById('block-dialog');
    form_dialog.querySelector('.close').addEventListener('click', function() {
        form_dialog.close();
    });

    $(function() {
        <#if error_message?? || message??>
            dialog.showModal();
        </#if>
    });

	function block_user(id) {
	    $('#block-user-id').val(id);
	    $('#reason').val('');
        form_dialog.showModal();
	}

	function unblock_user(id, username) {
        var result = window.confirm("Доступ користувачу " + username + " буде відновлено. Ви впевнені?");
        if(result) {
            $("#unblock-user-id").val(id);
            $("#unblock-user").submit();
        }
	}

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