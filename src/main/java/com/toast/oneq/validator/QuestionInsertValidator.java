package com.toast.oneq.validator;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.toast.oneq.vo.QuestionInsertVo;

public class QuestionInsertValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return (QuestionInsertVo.class.isAssignableFrom(clazz));
    }

    @Override
    public void validate(Object target, Errors errors) {
        QuestionInsertVo questionInsertVo = (QuestionInsertVo) target;
        titleValidate(questionInsertVo, errors);
        contentValidate(questionInsertVo, errors);
        questionTypeCodeValidate(questionInsertVo, errors);
        questionMediaTypeCodeValidate(questionInsertVo, errors);
        voteUserCountMaxValidate(questionInsertVo, errors);
        hashListValidate(questionInsertVo, errors);
        itemListValidate(questionInsertVo, errors);
    }

    protected void titleValidate(QuestionInsertVo questionInsertVo, Errors errors) {
        if (questionInsertVo.getTitle().length() == 0) {
            errors.rejectValue("title", "투표 제목을 입력하시지 않으셨어요.");
        }
        
        if (questionInsertVo.getTitle().length() > 80) {
            errors.rejectValue("title", "투표 제목이 너무 길어요. 최대 80자에요.");
        }
    }
    
    protected void contentValidate(QuestionInsertVo questionInsertVo, Errors errors) {
        if (questionInsertVo.getContent().length() > 500) {
            errors.rejectValue("title", "투표 내용이 너무 길어요. 최대 500자에요.");
        }
    }

    protected void questionTypeCodeValidate(QuestionInsertVo questionInsertVo, Errors errors) {
        if (questionInsertVo.getQuestionTypeCode() < 0 || questionInsertVo.getQuestionTypeCode() >  1) {
            errors.rejectValue("questionTypeCode", "투표 종류가 올바르지 않아요.");
        }
    }

    protected void questionMediaTypeCodeValidate(QuestionInsertVo questionInsertVo, Errors errors) {
        if (questionInsertVo.getMediaTypeCode() < 0 || questionInsertVo.getMediaTypeCode() > 2) {
            errors.rejectValue("mediaTypeCode", "투표 미디어 타입이 올바르지 않습니다.");
        }
    }

    protected void voteUserCountMaxValidate(QuestionInsertVo questionInsertVo, Errors errors) {
        if (questionInsertVo.getVoteUserCountMax() < 0) {
            errors.rejectValue("voteUserCountMax", "최대 투표자수는 음수가 될 수 없습니다.");
        }
    }

    protected void hashListValidate(QuestionInsertVo questionInsertVo, Errors errors) {
        List<String> hashNameList = questionInsertVo.getHashName();        
        hashNameValidate(hashNameList, errors);
    }

    protected void hashNameValidate(List<String> hashNameList, Errors errors) {
        if(hashNameList == null){
            return;
        }
        
        for(String hashName : hashNameList){
            if(hashName != null && hashName.length() > 20){
                errors.rejectValue("hashName", "해시태그 최대 길이는 20자입니다.");
                break;
            }
        }
    }

    protected void itemListValidate(QuestionInsertVo questionInsertVo, Errors errors) {
        List<String> itemTitleList = questionInsertVo.getItemTitle();
        List<Integer> itemMediaTypeCode = questionInsertVo.getItemMediaTypeCode();
        
        itemTitleValidate(itemTitleList, errors);
        itemMediaTypeCodeValidate(itemMediaTypeCode, errors);
    }

    protected void itemTitleValidate(List<String> itemTitleList, Errors errors) {
        for(String title : itemTitleList){
            if (title.length() == 0) {
                errors.rejectValue("itemTitle", "투표 항목 내용을 입력하시지 않으셨습니다.");
                break;
            } else if (title.length() > 80) {
                errors.rejectValue("itemTitle", "투표 항목이 너무 길어요. 최대 80자에요.");
                break;
            }
        }
    }
    
    protected void itemMediaTypeCodeValidate(List<Integer> itemMediaTypeCode, Errors errors) {
        for(int mediaTypeCode : itemMediaTypeCode){
            if (mediaTypeCode < 0 || mediaTypeCode >  2) {
                errors.rejectValue("itemMediaTypeCode", "투표 항목 미디어 타입이 올바르지 않습니다.");
                break;
            }
        }
    }
}
