<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<button type="button" class="ui orange basic button hashtagBtn">
    #<\%= hashName %>
</button>
<input type="hidden" name="hashName[]" value="<\%= hashValue %>"  maxlength="20">

