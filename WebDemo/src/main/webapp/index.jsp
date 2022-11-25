<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

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
                for(var i = 0; i < result.length; i++) {
                    var A = document.createElement("A");
                    A.setAttribute("href",result[i]);
                    A.appendChild(document.createTextNode(result[i]));
                    document.getElementById("resultInNev").appendChild(A);
                }
            }
        })
    }

</script>
</html>
