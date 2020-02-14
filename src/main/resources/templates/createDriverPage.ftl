<html>
<head>
	<title>Palindrome - Driver Info</title>
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
			background: url('https://www.cisco.com/c/en/us/products/security/what-is-information-security-infosec/_jcr_content/Grid/subcategory_atl_8acc/layout-subcategory-atl/anchor_info_127c/image.img.png/1531154998451.png') center / cover;
		}
	</style>
</head>
<body>
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
	<header class="mdl-layout__header">
		<div class="mdl-layout__header-row">
			<span class="mdl-layout-title" style="font-family: 'Josefin Sans', sans-serif;">Palindrome</span>
			<div class="mdl-layout-spacer"></div>
			<nav class="mdl-navigation mdl-layout--large-screen-only">
			</nav>
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
					<h2 class="mdl-card__title-text">Інформація про водія</h2>
				</div>
				<div class="mdl-card__supporting-text">
					<form action="/driver/create" method="post" enctype="multipart/form-data">

						<div>
							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<input class="mdl-textfield__input" <#if driver?? && driver.firstName??>value="${driver.firstName}"</#if> autocomplete="off" name="firstName" type="text">
								<label class="mdl-textfield__label" for="password">Ім'я</label>
							</div>
							<#if firstNameError??>
								<p style="color: red">${firstNameError}</p>
							</#if>

							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<input class="mdl-textfield__input" <#if driver?? && driver.middleName??>value="${driver.middleName}"</#if> autocomplete="off" name="middleName" type="text">
								<label class="mdl-textfield__label" for="password">По батькові</label>
							</div>
	                        <#if middleNameError??>
								<p style="color: red">${middleNameError}</p>
	                        </#if>

							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<input class="mdl-textfield__input" <#if driver?? && driver.lastName??>value="${driver.lastName}"</#if> autocomplete="off" name="lastName" type="text">
								<label class="mdl-textfield__label" for="password">Прізвище</label>
							</div>
                            <#if lastNameError??>
								<p style="color: red">${lastNameError}</p>
                            </#if>
						</div>

						<div>
							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<input class="mdl-textfield__input" <#if driver?? && driver.address??>value="${driver.address}"</#if> autocomplete="off" name="address" type="text">
								<label class="mdl-textfield__label" for="password">Адреса</label>
							</div>
		                    <#if addressError??>
								<p style="color: red">${addressError}</p>
		                    </#if>
						</div>

						<div>
							<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
								<input class="mdl-textfield__input" <#if driver?? && driver.licenceNumber??>value="${driver.licenceNumber}"</#if> autocomplete="off" name="licenceNumber" type="text">
								<label class="mdl-textfield__label" for="password">Номер водійського посвідчення</label>
							</div>
	                        <#if licenceNumberError??>
								<p style="color: red">${licenceNumberError}</p>
	                        </#if>
						</div>

						<div style="margin-top: 2%;">
							<label>Фото</label>
							<input type="file" name="photo" required>
						</div>

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
</script>
</body>
</html>