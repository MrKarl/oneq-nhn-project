<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<div class="ui tiny image">
    <\% if(mediaTypeCode == 0){ %>
        <img src="./img/default_item.jpg">
    <\% } else if(mediaTypeCode == 1) { %>
        <img class="itemImage" src="<\%= mediaPath %>">
    <\% } else{ 
        var mediaImage = mediaPath.split('/embed/')[1];
        mediaImage = mediaImage.split('&list')[0];  %>
        <img class="itemYoutube" src="http://img.youtube.com/vi/<\%=mediaImage%>/0.jpg" data-youtube="<\%=mediaPath%>">
    <\% } %>
</div>
<div class="middle aligned content">
    <div class="vote">
        <\%= title %>
    </div>
    <div class="result">
        <span class="bar"></span>
        <span class="title">
            <\%= title %>
        </span>
        <span class="count">
            <\%= resultCount %>
        </span>
    </div>
</div>