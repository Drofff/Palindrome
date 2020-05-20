<html>
<head>
    <title>Palindrome - New Change Request</title>
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

            <div class="mdl-layout mdl-js-layout mdl-layout--fixed-drawer">
                <div class="mdl-layout__drawer">
                    <span class="mdl-layout-title" style="margin-top: 20px">
                        <img src="/api/resources/img/logo.png" width="150px" height="50px">
                    </span>
                    <nav class="mdl-navigation">
                        <a class="mdl-navigation__link" href="/">Головна</a>
                        <a class="mdl-navigation__link" href="/violation/police">Фіксовані порушення</a>
                        <a class="mdl-navigation__link" href="/change-request/sent">Запити</a>
                        <a class="mdl-navigation__link" href="/police">Профіль</a>
                    </nav>
                </div>
                <main class="mdl-layout__content">
                    <div class="page-content">
                        <div class="demo-card-wide mdl-card mdl-shadow--2dp" style="margin-left: 10%; margin-top: 2%;">
                            <div class="mdl-card__title">
                                <h2 class="mdl-card__title-text">Новий запит</h2>
                            </div>
                            <div class="mdl-card__supporting-text">
                                <div style="margin-left: 7%; margin-bottom: 6%;">
                                    <p>
                                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                        <select class="mdl-textfield__input" id="targetType">
                                            <option></option>
                                            <option value="driver">Дані водія</option>
                                            <option value="car">Інформація про автомобіль</option>
                                        </select>
                                        <label class="mdl-textfield__label" for="targetType">Тип змінюваної інформації</label>
                                    </div>
                                    </p>
                                    <p>
                                    <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" id="targetIdContainer" hidden>
                                        <select class="mdl-textfield__input" id="targetId">

                                        </select>
                                        <label class="mdl-textfield__label" for="targetId">Ціль зміни</label>
                                    </div>
                                    </p>
                                    <button type="button" id="next_btn" class="mdl-button mdl-js-button mdl-button--raised mdl-button--colored" onclick="submit()" disabled>
                                        Далі
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>

        </div>
    </main>
</div>
<form action="/logout" method="post" id="logout">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>
<script>

    var targetType;
    var targetId;

    $(function() {
        $("#targetType").change(function() {
            targetType = $("#targetType").val();
            if(!targetType) {
                return;
            }
            $.ajax({
                type : 'GET',
                url : '/' + targetType + '/list',
                headers : {
                    'Accept' : 'application/json'
                }
            }).done(function(data) {
                var options = "<option></option>";
                for(var i = 0; i < data.length; i++) {
                    var label;
                    if(data[i].hasOwnProperty('number')) {
                        label = data[i].number;
                    } else {
                        label = data[i].firstName + " " + data[i].lastName + " (" + data[i].licenceNumber + ")";
                    }
                    options += "<option value='" + data[i].id + "'>" + label + "</option>";
                }
                $("#targetId").html(options);
            });
            $("#targetIdContainer").removeAttr('hidden');
        });
        $("#targetId").change(function() {
            targetId = $("#targetId").val();
            $("#next_btn").removeAttr('disabled');
        });
    });

    function submit() {
        if(!targetType) {
            return;
        }
        if(!targetId) {
            return;
        }
        window.location.href = "/change-request/send/" + targetType + "/" + targetId;
    }

</script>
</body>
</html>