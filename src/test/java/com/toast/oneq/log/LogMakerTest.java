package com.toast.oneq.log;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LogMakerTest {
    
    @Test
    public void infoTest(){
        LogMaker.info("log info");
    }
    
    @Test
    public void warnTest(){
        LogMaker.warn("log warn");
    }
    
    @Test
    public void errorTest(){
        LogMaker.error("log error");
    }
    
    @Test
    public void getLogMassageTest(){
        String logMessageTest = "message=[log message]";
        assertThat(LogMaker.getLogMassage("log message"), equalTo(logMessageTest));
    }
}
