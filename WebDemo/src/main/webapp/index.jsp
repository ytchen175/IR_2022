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
                <a href="#">Login</a> | <a href="#">Register</a>
            </div>
            <div class="arrow-up"></div>
            <dif class="login-form">
                <form>
                    <div>
                        <label>Username</label>
                        <input type="text" required>
                    </div>
                    <div>
                        <label>Password</label>
                        <input type="password" required>
                    </div>
                    <div>
                        <input type="submit" value="Log In">
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
    $(document).on("click","#searchButton",function(){
        getSearchResult();
    });

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
                    A.setAttribute("href","doc/"+result[i]);
                    A.appendChild(document.createTextNode(result[i]));
                    document.getElementById("resultInNev").appendChild(A);
                }
            }
        })
    }

</script>
</html>
