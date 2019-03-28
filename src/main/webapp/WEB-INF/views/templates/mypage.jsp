<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<div id="mypage-button-container" class="ui two column grid">
   <div class="center aligned column">
       <div class="ui red large button writed-question">
            <i class="write icon"></i> Write
        </div>
        <br /> 
        <div class="ui basic top pointing red label"> <\%= writeCount %> </div>
    </div>
    <div class="center aligned column">
        <div class="ui blue large button voted-question">
            <i class="checkmark box icon"></i> Vote
        </div>
        <br /> 
        <div class="ui basic top pointing blue label"> <\%= voteCount %> </div>
    </div>
</div>

<div class="ui main text container mypage-content">
</div>
