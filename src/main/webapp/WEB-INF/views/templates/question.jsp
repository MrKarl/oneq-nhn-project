<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
    <span class="ui attached grey finished label">Closed</span>
    <div class="content">
        <div class="left floated header">
            <\%= title %>
        </div>
        <a class="right floated">
            <i class="share icon dimmer-show"></i>
        </a>
        <div class="description">
            <div class="left floated meta">
                <\%= startAt %> ~ <\%= stopAt %>
            </div>
            <div class="right floated meta">
                <i class="users icon"></i>
                <span class="vote-num">
                <\%= voteUserCount %>
                </span>
                <!--  if statemend  -->
                <\% if(voteUserCountMax > 0) { %>
                    (<\%=voteUserCountMax %>)
                <!--  End if  -->                    
                <\% }  %> 
                   Votes
            </div>
        </div>
    </div>
    <div class="image">
    <\% if(mediaTypeCode == 1 && mediaPath != "") {  %>
        <img src="<\%= mediaPath %>">
    <\% } else if(mediaTypeCode == 2){ %> 
        <iframe width="100%" height="400" src= "<\%= mediaPath %>" frameborder="0" allowfullscreen></iframe>
    <\% } %>
    </div>
    <div class="content">
        <div class="description">
            <p><\%= content %></p>
            <div class="hashlist content">
            </div>
        </div>
    </div>
    <div class="itemlist content">
    </div>
    <div class="extra content">
        <div class="ui very relaxed horizontal list">
            <span class="hitNum item">
                <i class="check icon"></i>
                <span class="value"><\%= hit %></span> hits
            </span>
            
            <\% if(questionTypeCode == 1){ %>
            <span class="ui item">
                <i class="sidebar icon"></i>
                복수투표
            </span>            
            <\% } %>
        
        </div>
        <button class="ui right floated basic teal vote-button button">
                        투표하기
        </button>
        <button class="ui right floated basic teal result-button button">
                        결과보기
        </button>
    </div>
    
    <div class="ui share dimmer">
        <div class="content">
            <div class="center">
                <i class="close icon dimmer-hide"></i>
                
                <img class="dimmerImage image" src="">
                
                <iframe class="dimmerYoutube youtube" src="" allowfullscreen></iframe>
                
                <div class="dimmerShare shareDiv">
                    <div class="ui top attached tabular menu">
                        <div data-tab="first" class="active item">링크</div>
                        <div data-tab="second" class="item">소스 코드</div>
                        <!-- <div data-tab="third" class="item">이미지</div> -->
                    </div>
                    <div class="ui bottom attached active tab segment" data-tab="first">
                        <div class="ui form">
                            <div class="field">
                                <label>Link</label>
                                <input type="text" class="link-source">
                            </div>
                        </div>
                    </div>
                    <div class="ui bottom attached tab segment" data-tab="second">
                        <div class="ui form">
                            <div class="field">
                                <label>Source Code</label>
                                <input type="text" class="iframe-source">
                            </div>
                            <div class="inline fields">
                                <div class="field">
                                    <div class="ui radio iframe-mode checkbox">
                                        <input type="radio" name="iframe-mode" value="0" checked="checked">
                                        <label>투표부터 보기</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui radio iframe-mode checkbox">
                                        <input type="radio" name="iframe-mode" value="1">
                                        <label>결과부터 보기</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    