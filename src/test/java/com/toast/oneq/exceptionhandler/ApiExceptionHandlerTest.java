package com.toast.oneq.exceptionhandler;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.toast.oneq.exception.QuestionInsertFormException;
import com.toast.oneq.exception.ServiceFailException;
import com.toast.oneq.exception.UnauthorizedUserException;
import com.toast.oneq.util.ResponseHeaderUtil;
import com.toast.oneq.vo.ResponseVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy ({
    @ContextConfiguration("/spring/application-context.xml"),
    @ContextConfiguration("/spring/servlet-context.xml")
})
public class ApiExceptionHandlerTest {
    private ApiExceptionHandler apiExceptionHandler;
    private ResponseVo responseVo;
    
    @Before
    public void setup() {
        apiExceptionHandler = new ApiExceptionHandler();
        responseVo = new ResponseVo();
    }
    
    @Test
    public void handleUnauthorizedUserExceptionTest() throws Exception {
        Map<String,Object> header = ResponseHeaderUtil.createHeader(500, "유효하지 않은 유저입니다.", false);
        responseVo.setHeader(header);
        assertThat(apiExceptionHandler.handleUnauthorizedUserException(new UnauthorizedUserException()).getHeader(), equalTo(responseVo.getHeader()));
    }
    
    @Test
    public void handleServiceFailExceptionTest() {
        Map<String,Object> header = ResponseHeaderUtil.createHeader(500, new ServiceFailException("실패").getMessage(), false);
        responseVo.setHeader(header);
        assertThat(apiExceptionHandler.handleServiceFailException(new ServiceFailException("실패")).getHeader(), equalTo(responseVo.getHeader()));
    }
    
    @Test
    public void handleQuestionInsertFormExceptionTest() throws Exception {
        String body = "{\"exception\":\"THIS IS EXCEPTION BODY\"}";
        Map<String,Object> header = ResponseHeaderUtil.createHeader(406, body, false);
        responseVo.setHeader(header);
        assertThat(apiExceptionHandler.handleQuestionInsertFormException(new QuestionInsertFormException(body)).getHeader().toString(), equalTo(responseVo.getHeader().toString()));
    }
    
    @Test
    public void handleExceptionTest() throws Exception {
        Map<String,Object> header = ResponseHeaderUtil.createHeader(500,"서버 에러 입니다.", false);
        responseVo.setHeader(header);
        assertThat(apiExceptionHandler.handleException(new Exception()).getHeader(), equalTo(responseVo.getHeader()));
    }
}
