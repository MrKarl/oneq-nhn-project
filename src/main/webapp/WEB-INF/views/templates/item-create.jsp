<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<div class="ui small image">
    <div class="ui form">
        <div class="field">
            <div class="ui fluid button media_add" style="background: transparent; border: 1px black dashed; padding: 0px; height: 100px;">
                <div class="mediaContent" style="height: 100px; width: 100%; background-position: 50% 50%; background-repeat: no-repeat;"></div>
                <span style="font-size:30px;position:relative;top:-60px;"><i class="upload icon"></i></span>
            </div>
            <div class="media_info item_media_info">
                <input class="mediaTypeCode" type="hidden" name="itemMediaTypeCode[]" value="0">
                <input class="mediaPath" type="hidden" name="itemMediaPath[]" value="" maxlength="1000">
                <input class="mediaFile" type="file" name="itemMediaFile[]">
            </div>
        </div>
    </div>
</div>
<div class="middle aligned content" style="height: 100px">
    <div class="ui" style="text-align: right;">
        <i class="item_remove remove red outline icon"></i>
    </div>
    <div class="meta">
        <div class="ui form">
            <div class="field">
                <input type="text" class="itemTitle" name="itemTitle[]" placeholder="ex) 금요일엔 당연히 감자탕이지 !!!" maxlength="80">
            </div>
        </div>
    </div>
</div>
