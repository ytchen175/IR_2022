<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Term Project</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<nav class="nav-bar">
    <section class="nav-container">
        <aside class="menu">
            <div class="menu-content">
                <a href="#" id="login">Login</a> | <a href="#" id="register">Register</a>
            </div>
            <div class="arrow-up-login"></div>
            <dif class="login-form">
                <form onsubmit="return false;">
                    <div>
                        <label>Username</label>
                        <input type="text" required>
                    </div>
                    <div>
                        <label>Password</label>
                        <input type="password" required>
                    </div>
                    <div>
                        <input type="submit" value="Log In" id="loginSubmit">
                    </div>
                </form>
            </dif>
            <div class="arrow-up-register"></div>
            <dif class="register-form">
                <form onsubmit="return false;">
                    <div>
                        <label>Username</label>
                        <input id="usernameRegsitered" type="text" required>
                    </div>
                    <div>
                        <label>Password</label>
                        <input id="passwordRegsitered" type="text" required>
                    </div>
                    <div>
                        <label>email</label>
                        <input id="emailRegsitered" type="email" required>
                    </div>
                    <div>
                        <input type="submit" value="Register">
                    </div>
                </form>
            </dif>
        </aside>
        <aside class="other-function">
            <div class="keywordPage">
                <a href="#">
                    關鍵字功能
                </a>
            </div>

        </aside>

    </section>
</nav>
<div class="box">
    <form onsubmit="return false;">
        <input id="searchContent" type="text" name="" placeholder="Type...">
        <input id="searchButton" type="submit" name="" value="Search">
    </form>
    <div class="result">
        <nav id="resultInNev">
        </nav>
    </div>
</div>


<br/>
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    $(document).ready(function (){
        var arrowLogin = $(".arrow-up-login");
        var loginForm = $(".login-form")
        var isLoginDropdownOpened = false;
        $("#login").click(function (event){
            event.preventDefault();
            if(isLoginDropdownOpened == false){
                arrowLogin.show();
                loginForm.show();
                isLoginDropdownOpened = true;
                arrowRegister.hide();
                registerForm.hide();
                isRegisterDropdownOpened = false;
            }
            else{
                arrowLogin.hide();
                loginForm.hide();
                isLoginDropdownOpened = false;
            }
        })
        var arrowRegister = $(".arrow-up-register");
        var registerForm = $(".register-form")
        var isRegisterDropdownOpened = false;
        $("#register").click(function (event){
            event.preventDefault();
            if(isRegisterDropdownOpened == false){
                arrowRegister.show();
                registerForm.show();
                isRegisterDropdownOpened = true;
                arrowLogin.hide();
                loginForm.hide();
                isLoginDropdownOpened = false;
            }
            else{
                arrowRegister.hide();
                registerForm.hide();
                isRegisterDropdownOpened = false;
            }
        })
    })

    $(document).on("click","#searchButton",function(){
        getSearchResult();
    });
    $(document).on("click","#loginSubmit",function(){
        login();
    });
    $(document).on("click","#registerSubmit",function(){
        register();
    });
    function login(){

    }
    function register(){
        $.ajax({
            url:"RegisterServlet",
            type:"POST",
            dataType:"json",
            data:{
                'username': $("#usernameRegsitered").val(),
                'password': $("#passwordRegsitered").val(),
                'email': $("#emailRegsitered").val()
            },
            success : function(result) {

            }
        })
    }
    function getSearchResult(){
        $.ajax({
            url:"search-servlet",
            type:"POST",
            dataType:"json",
            data:{
                'searchContent': $("#searchContent").val(),
            },
            success : function(result) {
                $("#resultInNev").empty();
                for(var i = 0; i < result.length; i++) {
                    var A = document.createElement("A");
                    A.setAttribute("href","docs/"+result[i]);
                    A.appendChild(document.createTextNode(result[i]));
                    document.getElementById("resultInNev").appendChild(A);
                }
            }
        })
    }

</script>
</html>
