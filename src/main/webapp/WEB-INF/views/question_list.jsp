<%@ page contentType = "text/html;charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
    <header>
        <div>OneQ</div>
        <div>
            <div>
                <input type="text" name="hashtag" id="search_field" placeholder="#해쉬태그"/>
            </div>
        </div>
    </header>
    
    <hr/>
    
    <section>
        <div>
            <form action="/oneq/q" method="POST">
            
                <input type="hidden" name="u_id" value="1"/><br/>
                
                <input type="text" name="title" placeholder="투표 제목"/><br/>
                
                <textarea name="content" placeholder="투표 설명"></textarea><br/>
                
                <select name="q_type">
                    <option value="0">단답형</option>
                    <option value="1">복수형</option>
                    <option value="2">순위형</option>
                    <option value="3">VS</option>
                </select><br/>
                
                <select name="media_type">
                    <option value="0">텍스트</option>
                    <option value="1">이미지</option>
                    <option value="2">동영상</option>
                </select><br/>
                
                <input type="text" name="media_path" placeholder="텍스트 or 미디어 path"/><br/>
                
                <input type="text" name="startAt" placeholder="투표 시작일"/><br/>
                <input type="text" name="stopAt" placeholder="투표 마감일"/><br/>
                
                <input type="text" name="voteUserCountMax" placeholder="최대 참여자수"/><br/>
                
                <input type="submit" value="제출"/>
            </form>
        </div>
    </section>
    <footer>
    
    </footer>
</body>
</html>
