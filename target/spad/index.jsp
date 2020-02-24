<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="fa">
<head>
    <title>SpadSystem</title>
    <link rel="stylesheet" type="text/css" href="css/materialize.min.css">
    <link rel="stylesheet" type="text/css" href="css/icon.css">
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="blur-background"></div>
<div class="section"></div>
<center>
    <main>
        <div class="section"></div>
        <div class="container">
            <div class="z-depth-1 grey lighten-4 row"
                 style="display: inline-block; border: 1px solid #EEE;width: 50%;">
                <h5 class="indigo-text">به سامانه یکپارچه اسپاد خوش آمدید</h5>
            </div>
        </div>
        <div class="section"></div>
        <div class="container">
            <div class="z-depth-1 grey lighten-4 row"
                 style="display: inline-block; padding: 32px 48px 0px 48px; border: 1px solid #EEE;width: 50%;">
                <form class="col s12" method="post" action="/spadsystem/rest/login">
                    <div class='row'>
                        <div class='col s12'>
                        </div>
                    </div>

                    <div class='row'>
                        <div class='input-field col s12'>
                            <input class='validate' type='text' name='user_code' id='user_code'/>
                            <label for='user_code'>کدپرسنلی</label>
                        </div>
                    </div>

                    <div class='row'>
                        <div class='input-field col s12'>
                            <input class='validate' type='password' name='password' id='password'/>
                            <label for='password'>رمز عبور</label>
                        </div>
                    </div>

                    <br/>
                    <center>
                        <div class='row'>
                            <button type='submit' name='btn_login'
                                    style="background-color: lightsteelblue;color: black;"
                                    class='col s12 btn btn-large waves-effect'>
                                ورود
                            </button>
                        </div>
                    </center>
                </form>
            </div>
        </div>
    </main>
</center>
<script src="js/jquery.min.js"></script>
<script src="js/materialize.min.js"></script>
<script>
    $(document).ready(function () {
        switch (new URLSearchParams(window.location.search).get('text')) {
            case "auth":
                M.toast({html: "نام کاربری یا رمز عبور اشتباه است.", classes: 'rounded red'});
                break;
            case "error":
                M.toast({html: "سرور دچار ایراد شده است. لطفا بعدا امتحان بفرمایید.", classes: 'rounded red'});
                break;
        }
    });
</script>
</body>
</html>
