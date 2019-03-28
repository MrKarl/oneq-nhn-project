<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <title>OneQ : 간편한 투표의 종결자</title>
    <style>
        body{
            background-color:#eee;
            text-align: center;
            color:black;
            font-family: Lato,'Helvetica Neue',Arial,Helvetica,sans-serif;
        }
        
        .date{
            display:inline-block;
            width:260px;
            background-color:deepskyblue;
            color:white;
            font-weight: bold;
            padding:20px 10px;
            font-size:30px;
            margin:30px 0 10px;;
        }
        
        .time{
            width:280px;
            margin: 0 auto;
        }
        
        .time div{
            display:inline-block;
            font-size:50px;
        }
        
        .time div:not(.colon){
            background-color: darkslategray;
            color:white;
            font-weight: bold;
            padding:10px;
        }
        
        .oops{
            font-size:100px;
            font-weight: bold;
        }
        
        .description{
            font-size:13px;         
            line-height: 20px;
        }
        
        .goback a{
            text-decoration: none;
            color:red;
            font-weight: bold;
            font-size: 20px;
        }
        
        .pause a{
            text-decoration: none;
            color:blue;
            font-weight: bold;
            font-size: 20px;
        }
        .clock{
            margin-bottom:40px;
        }
        .redirect{
            margin-top:20px;
            font-size:15px;
        }
    </style>
</head>
<body>
    <header>
        <img src="/img/errorIcon.png" alt="errorIcon.png" style="width:200px; margin-top:50px;"/>
    </header>
    <section>
        <div class="error-message">
            <div class="oops">
                <span>OOOOOPS !</span>
            </div>
            <div class="description">
                <span>So Sorry !!</span><br/>
                <span>문제가 발생하였습니다 ! 다시 시도해주세요 !</span><br/>
                <span>계속 문제가 발생하신다면, 저희 엔지니어에게 연락 부탁드려요 !</span><br/><br/>
                
                <strong>oneq.nhnent@gmail.com</strong>
            </div>
        </div>
        
        <div class="clock">
            <div class="date">
                201x-xx-xx
            </div>
            
            <div class="time">
                <div class="hour">00</div>
                <div class="colon">:</div>
                <div class="minute">00</div>
                <div class="colon">:</div>
                <div class="second">00</div>
            </div>
        </div>
        
        <div class="redirect">
            Redirect to home in <span class="left"></span> seconds.
        </div>
        
        <div class="func">
            <span class="goback"><a href="/">Back</a></span>&nbsp;
            <span class="pause"><a href="#">Pause</a></span>
        </div>
    </section>
    
    
    <footer>
    </footer>
    
    <script src="https://code.jquery.com/jquery-2.2.0.min.js"></script>
    <script type="text/javascript">
        $(document).on('ready', function(){
            var redirectTime = 8;
            var leftInterval = setInterval(function(){
                $('.left').text(redirectTime);
                redirectTime = redirectTime - 1;
                if(redirectTime < 0){
                    document.location = "/";
                }
            }, 1000);
            
            $('.pause').on('click', function(){
                clearInterval(leftInterval);
                $('.redirect').hide();
                $('.pause').hide();
            });
            
            var clockInterval = setInterval(function(){
                var $date = $('.date');
                var $hour = $('.hour');
                var $minute = $('.minute');
                var $second = $('.second');
                
                var dateTime = new Date();
                
                var fullYear = dateTime.getFullYear();
                var month = dateTime.getMonth() + 1;
                var day = dateTime.getDate();
                var hour = dateTime.getHours();
                var minute = dateTime.getMinutes();
                var second = dateTime.getSeconds();
                
                month = month < 10 ? "0"+month : month;
                day = day < 10 ? "0"+day : day;
                hour = hour < 10 ? "0"+hour : hour;
                minute = minute < 10 ? "0"+minute : minute;
                second = second < 10 ? "0"+second : second;
                
                var dateString = fullYear+"-"+month+"-"+day;
                
                $date.text(dateString);
                $hour.text(hour);
                $minute.text(minute);
                $second.text(second);
            }, 1000);
        });
        
    </script>
</body>
</html>
