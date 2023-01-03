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
                <c:if test="condition"></c:if>
                <% if(session.getAttribute("userName") != null){ %>
                <a href="#" id="user"><%= session.getAttribute("userName")%></a>
                <% }else { %>
                <a href="#" id="login">Login</a>
                <% } %>
                | <a href="#" id="register">Register</a>
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
                        <input type="submit" value="Register">
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
            url:"LoginServlet",
            type:"POST",
            dataType:"json",
            data:{
                'username': $("#usernameLogin").val(),
                'password': $("#passwordLogin").val(),
            },
            success : function() {
                $("#usernameLogin").empty();
                $("#passwordLogin").empty();
                var arrowLogin = $(".arrow-up-login");
                var loginForm = $(".login-form");
                arrowLogin.hide();
                loginForm.hide();
                $("#login").hide();
                window.location.reload();
            }
        })
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

                console.log("result:");

                $("#resultInNev").empty();
                for(var i = 0; i < result.length; i++) {


                    var wrapper = document.createElement("div");

                    var A = document.createElement("A");
                    A.setAttribute("href",result[i].url);
                    A.appendChild(document.createTextNode(result[i].title));
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
