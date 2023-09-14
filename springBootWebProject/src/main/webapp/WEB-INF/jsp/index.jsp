<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Term Project</title>
    <link rel="stylesheet" href="style1.css">
</head>
<body>
<nav class="nav-bar">
    <section class="nav-container">
        <aside class="menu">
            <div class="menu-content">

                <% if(session.getAttribute("username") != null){ %>
                <a href="#" id="user"><%= session.getAttribute("username")%></a>
                <% }else { %>
                <a href="#" id="login">Login</a>
                <% } %>
                | <a href="#" id="register">Register</a>

<%--                <a href="#" id="user">User Name</a>--%>
<%--                <a href="#" id="login">Login</a>--%>
<%--                | <a href="#" id="register">Register</a>--%>
            </div>
            <div class="arrow-up-login"></div>


            <dif class="login-form">
                <form onsubmit="return false;">
                    <div>
                        <label>Username</label>
                        <input id="usernameLogin" type="text" required>
                    </div>
                    <div>
                        <label>Password</label>
                        <input id="passwordLogin" type="password" required>
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
                        <input type="submit" value="Register" id="registerSubmit">
                    </div>
                </form>
            </dif>
        </aside>
        <%--        <aside class="other-function">--%>
        <%--            <div class="keywordPage">--%>
        <%--                <a href="docs/docID_00000.html">--%>
        <%--                    關鍵字功能--%>
        <%--                </a>--%>
        <%--            </div>--%>

        <%--        </aside>--%>

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
        // if(sessionStorage.getItem("isLogin") == "true"){
        //     $("#login").hide();
        //     $("#user").show();
        //     $("#user").text(sessionStorage.getItem("username"));
        // }else {
        //     $("#login").show();
        //     $("#user").hide();
        // }


        var arrowLogin = $(".arrow-up-login");
        var loginForm = $(".login-form");
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
        $.ajax({
            url:"/account/login",
            type:"POST",
            dataType:"json",
            data:{
                'username': $("#usernameLogin").val(),
                'password': $("#passwordLogin").val(),
            },
            success : function(result) {
                if(result.isLogin == true){
                    // sessionStorage.setItem("isLogin","true");
                    // sessionStorage.setItem("username",result.username);
                    // $("#login").hide();
                    //
                    // $("#user").show();
                    // $("#user").text(sessionStorage.getItem("username"));
                    $("#usernameLogin").val("");
                    $("#passwordLogin").val("");
                    var arrowLogin = $(".arrow-up-login");
                    var loginForm = $(".login-form");
                    arrowLogin.hide();
                    loginForm.hide();
                    $("#login").hide();
                    alert("login successd!");
                    window.location.reload();
                }else {
                    $("#usernameLogin").val("");
                    $("#passwordLogin").val("");
                    var arrowLogin = $(".arrow-up-login");
                    var loginForm = $(".login-form");
                    arrowLogin.hide();
                    loginForm.hide();
                    alert("login failed!");
                }



            }
        })
    }
    function register(){
        $.ajax({
            url:"/account/register",
            type:"POST",
            dataType:"json",
            data:{
                'username': $("#usernameRegsitered").val(),
                'password': $("#passwordRegsitered").val(),
                'email': $("#emailRegsitered").val()
            },
            success : function(result) {
                var arrowRegister = $(".arrow-up-register");
                var registerForm = $(".register-form");
                $("#usernameRegsitered").val("");
                $("#passwordRegsitered").val("");
                $("#emailRegsitered").val("");
                if(result.isSuccessfullyRegistered == true){
                    alert("Success!");
                    arrowRegister.hide();
                    registerForm.hide();
                }else{
                    alert(result.errorMessage);
                }

            }
        })
    }
    function getSearchResult(){
        $.ajax({
            url:"/search",
            type:"POST",
            dataType:"json",
            data:{
                'searchContent': $("#searchContent").val(),
            },
            success : function(result) {

                console.log(result);
                console.log(result[0]);

                $("#resultInNev").empty();
                for(var i = 0; i < result.length; i++) {
                    var wrapper = document.createElement("div");

                    var A = document.createElement("A");
                    A.setAttribute("href",result[i].url);
                    A.appendChild(document.createTextNode(result[i].title));

                    var textDiv = document.createElement("div");
                    var newContent = document.createTextNode(result[i].original_body.slice(0,200)+"...");

                    // add the text node to the newly created div
                    textDiv.appendChild(newContent);

                    wrapper.appendChild(A);
                    wrapper.appendChild(textDiv);

                    document.getElementById("resultInNev").appendChild(wrapper);
                }
            }
        })
    }

</script>
</html>
