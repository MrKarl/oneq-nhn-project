package com.toast.oneq.util;


import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UuidUtilTest {

    @Test
    public void generatedUuid() {
        Assert.assertThat(UuidUtil.generatedUuid().getClass(), Matchers.equalTo(String.class));
    }
    
}
