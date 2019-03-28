package com.toast.oneq.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.toast.oneq.vo.QuestionInsertVo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
    @ContextConfiguration("/spring/application-context.xml"),
    @ContextConfiguration("/spring/servlet-context.xml")
})
public class QuestionInsertValidatorTest{
    private QuestionInsertVo questionInsertVoValid;
    
    @Autowired
    QuestionInsertValidator questionInsertValidator;
    
    @Before
    public void setUp(){
        questionInsertVoValid = new QuestionInsertVo();
        String title = "투표제목";
        String content = "투표내용";
        byte questionTypeCode = 0;
        byte questionMediaTypeCode = 0;
        int voteUserCountMax = 500;
        
        List<String> hashNameList = new ArrayList<String>();
        hashNameList.add("해시태그내용1");
        hashNameList.add("해시태그내용2");
        hashNameList.add("해시태그내용3");
        hashNameList.add("해시태그내용4");
        
        
        List<String> itemTitleList = new ArrayList<String>();
        itemTitleList.add("아이템내용1");
        itemTitleList.add("아이템내용2");
        itemTitleList.add("아이템내용3");
        itemTitleList.add("아이템내용4");
        
        List<Integer> itemMediaTypeCode = new ArrayList<Integer>();
        itemMediaTypeCode.add(0);
        itemMediaTypeCode.add(0);
        itemMediaTypeCode.add(0);
        itemMediaTypeCode.add(0);
        
        
        questionInsertVoValid.setTitle(title);
        questionInsertVoValid.setContent(content);
        questionInsertVoValid.setMediaTypeCode(questionMediaTypeCode);
        questionInsertVoValid.setVoteUserCountMax(voteUserCountMax);
        questionInsertVoValid.setQuestionTypeCode(questionTypeCode);
        questionInsertVoValid.setHashName(hashNameList);
        questionInsertVoValid.setItemTitle(itemTitleList);
        questionInsertVoValid.setItemMediaTypeCode(itemMediaTypeCode);
    }
    
    @Test
    public void supportsTest() {
        assertTrue(questionInsertValidator.supports(QuestionInsertVo.class));
        assertFalse(questionInsertValidator.supports(Object.class));
    }

    @Test
    public void validateTest() {
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.validate(questionInsertVoValid, errors);
        assertFalse(errors.hasErrors());
    }
    
    @Test
    public void titleValidateTest() {
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.titleValidate(questionInsertVoValid, errors);
        assertFalse(errors.hasErrors());
    }
    
    @Test
    public void titleValidateTest_에러_빈문자열() {
        questionInsertVoValid.setTitle("");
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.titleValidate(questionInsertVoValid, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void titleValidateTest_에러_너무긴문자열() {
        StringBuffer str = new StringBuffer();
        for(int i=0; i<50; i++){
            str.append("This is so long string. This number is "+ (i+1)+ ".");
        }
        questionInsertVoValid.setTitle(str.toString());
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.titleValidate(questionInsertVoValid, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void contentValidateTest() {
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.contentValidate(questionInsertVoValid, errors);
        assertFalse(errors.hasErrors());
    }
    
    @Test
    public void contentValidateTest_에러() {
        StringBuffer str = new StringBuffer();
        for(int i=0; i<50; i++){
            str.append("This is so long string. This number is "+ (i+1)+ ".");
        }
        questionInsertVoValid.setContent(str.toString());
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.contentValidate(questionInsertVoValid, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void questionTypeCodeValidateTest() {
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.questionTypeCodeValidate(questionInsertVoValid, errors);
        assertFalse(errors.hasErrors());
    }
    
    @Test
    public void questionTypeCodeValidateTest_에러_음수() {
        questionInsertVoValid.setQuestionTypeCode((byte)-1);
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.questionTypeCodeValidate(questionInsertVoValid, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void questionTypeCodeValidateTest_에러_너무큰수() {
        questionInsertVoValid.setQuestionTypeCode((byte)10);
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.questionTypeCodeValidate(questionInsertVoValid, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void questionMediaTypeCodeValidateTest() {
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.questionMediaTypeCodeValidate(questionInsertVoValid, errors);
        assertFalse(errors.hasErrors());
    }
    
    @Test
    public void questionMediaTypeCodeValidateTest_에러_음수() {
        questionInsertVoValid.setMediaTypeCode((byte)-1);
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.questionMediaTypeCodeValidate(questionInsertVoValid, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void questionMediaTypeCodeValidateTest_에러_너무큰수() {
        questionInsertVoValid.setMediaTypeCode((byte)10);
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.questionMediaTypeCodeValidate(questionInsertVoValid, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void voteUserCountMaxValidateTest() {
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.voteUserCountMaxValidate(questionInsertVoValid, errors);
        assertFalse(errors.hasErrors());
    }
    
    @Test
    public void voteUserCountMaxValidateTest_에러() {
        questionInsertVoValid.setVoteUserCountMax(-1);
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.voteUserCountMaxValidate(questionInsertVoValid, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void hashListValidateTest() {
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.hashListValidate(questionInsertVoValid, errors);
        assertFalse(errors.hasErrors());
    }
    
    @Test
    public void hashNameValidateTest() {
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.hashNameValidate(questionInsertVoValid.getHashName(), errors);
        assertFalse(errors.hasErrors());
    }
    
    @Test
    public void hashNameValidateTest_에러() {
        List<String> hashNameList = new ArrayList<String>();
        StringBuffer str = new StringBuffer();
        for(int i=0; i<50; i++){
            str.append("This is so long string. This number is "+ (i+1)+ ".");
        }
        hashNameList.add(str.toString());
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.hashNameValidate(hashNameList, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void hashNameValidateTest_에러_NULL() {
        List<String> hashNameList = new ArrayList<String>();
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.hashNameValidate(null, errors);
    }
    
    @Test
    public void itemListValidateTest() {
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.itemListValidate(questionInsertVoValid, errors);
        assertFalse(errors.hasErrors());
    }
    
    @Test
    public void itemTitleValidateTest() {
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.itemTitleValidate(questionInsertVoValid.getItemTitle(), errors);
        assertFalse(errors.hasErrors());
    }
    
    @Test
    public void itemTitleValidateTest_에러_빈문자열() {
        List<String> itemTitleList = new ArrayList<String>();
        itemTitleList.add("");
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.itemTitleValidate(itemTitleList, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test
    public void itemTitleValidateTest_에러_너무긴문자열() {
        List<String> itemTitleList = new ArrayList<String>();
        StringBuffer str = new StringBuffer();
        for(int i=0; i<50; i++){
            str.append("This is so long string. This number is "+ (i+1)+ ".");
        }
        itemTitleList.add(str.toString());
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.itemTitleValidate(itemTitleList, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test    
    public void itemMediaTypeCodeValidateTest() {
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.itemMediaTypeCodeValidate(questionInsertVoValid.getItemMediaTypeCode(), errors);
        assertFalse(errors.hasErrors());

    }
    
    @Test    
    public void itemMediaTypeCodeValidateTest_에러_너무큰수() {
        List<Integer> itemMediaTypeCode = new ArrayList<Integer>();
        itemMediaTypeCode.add(10);
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.itemMediaTypeCodeValidate(itemMediaTypeCode, errors);
        assertTrue(errors.hasErrors());
    }
    
    @Test    
    public void itemMediaTypeCodeValidateTest_에러_음수() {
        List<Integer> itemMediaTypeCode = new ArrayList<Integer>();
        itemMediaTypeCode.add(-1);
        Errors errors = new BeanPropertyBindingResult(questionInsertVoValid, "validForm");
        questionInsertValidator.itemMediaTypeCodeValidate(itemMediaTypeCode, errors);
        assertTrue(errors.hasErrors());
    }
}
