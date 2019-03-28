<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    
    <title>OneQ</title>
    
    <link rel="shortcut icon" type="image/png" href="/img/favicon.png">
    <link rel="stylesheet" type="text/css" href="/css/calendar.css">
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/main.css">
</head>
<body>
    <header id="header" class="ui fixed three item borderless menu">
        <div class="ui container">
            <div class="item">
                <a href="./" class="header item">
                    OneQ
                </a>
            </div>
            <div class="ui search item">
                <div class="ui icon input">
                    <input type="text" id="searchForm" class="searchForm" placeholder="해쉬태그를 입력해주세요.">
                    <i class="search link icon"></i>
                </div>
            </div>
            <div class="menu item">
                <a class="login item"><i class="grey sign in large icon"></i></a>
                <a class="create-question">
                    <i class="write grey large icon"></i>
                </a>
                <div class="ui item">
                  <div class="ui simple dropdown">
                    <i class="grey user large icon"></i>
                    <div class="menu">
                      <div class="item"><a href="/#my/questions" class="myPage">마이페이지</a></div>
                      <div class="item"><a href="/logout" class="logout">로그아웃</a></div>
                    </div>
                  </div>
                </div>
            </div>
        </div>
    </header>
    
    <section id="question-create" class="ui main text container">
    </section>
    <section id="overlay">
        <img src="/img/ajax-loader.gif" alt="Saving Loader"/>
    </section>
    <script data-main="/js/config/conf-create" src="/js/lib/require.js"></script>
    
</body>
</html>