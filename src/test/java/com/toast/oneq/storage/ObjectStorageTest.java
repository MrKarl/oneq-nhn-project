package com.toast.oneq.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import com.toast.oneq.exception.ObjectStorageException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
    @ContextConfiguration("/spring/application-context.xml"),
    @ContextConfiguration("/spring/servlet-context.xml")
})
public class ObjectStorageTest {
    @Autowired
    private ObjectStorage objectStorage;
    
   
    @Test
    public void isValidAccessTokenWithExpiresTest() throws ObjectStorageException{
        objectStorage.setExpiresAt(null);
        boolean isValidAccessToken = objectStorage.isValidAccessTokenWithExpires();
        assertEquals(false, isValidAccessToken);
        
        LocalDateTime nowDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        objectStorage.setExpiresAt(nowDateTime);
        isValidAccessToken = objectStorage.isValidAccessTokenWithExpires();
        assertEquals(false, isValidAccessToken);
        
        LocalDateTime oneDayAfterFromNow = LocalDateTime.now().plusDays(1);
        objectStorage.setExpiresAt(oneDayAfterFromNow);
        isValidAccessToken = objectStorage.isValidAccessTokenWithExpires();
        assertEquals(true, isValidAccessToken);

        objectStorage.receiveAccessToken();
        isValidAccessToken = objectStorage.isValidAccessTokenWithExpires();
        assertEquals(true, isValidAccessToken);
    }
    
    @Test
    public void receiveAccessTokenTest() throws ObjectStorageException{        
        objectStorage.receiveAccessToken();
        assertNotNull(objectStorage.getAccessToken());
        assertNotNull(objectStorage.getExpiresAt());
    }
    
    @Test
    public void uploadObjectTest() throws ObjectStorageException, IOException{        
        String fileName = "testFile1.txt";
        byte[] fileBody = {0,1,2,3,4,5,6,7,8,9,
                10,11,12,13,14,15,16,17,18,19,
                20,21,22,23,24,25,26,27,28,29};
        
        MultipartFile file = new MockMultipartFile(fileName, fileName, "text/plain", fileBody);
        String filePath = objectStorage.uploadObject(fileName, file);        
        assertEquals(filePath, fileName);
        
        int httpResponseCode = objectStorage.deleteObject("https://api-storage.cloud.toast.com/v1/AUTH_a1fd1f2df1584962b6a00c01becf228a/oneq_storage/"+fileName);
        assertEquals(httpResponseCode, 204);
    }
    
    @Test
    public void deleteObjectTest() throws ObjectStorageException, IOException{        
        String fileName = "testFile2.txt";
        byte[] fileBody = {0,1,2,3,4,5,6,7,8,9,
                10,11,12,13,14,15,16,17,18,19,
                20,21,22,23,24,25,26,27,28,29};
        
        MultipartFile file = new MockMultipartFile(fileName, fileName, "text/plain", fileBody);
        String filePath = objectStorage.uploadObject(fileName, file);        
        assertEquals(filePath, fileName);
        
        int httpResponseCode = objectStorage.deleteObject("https://api-storage.cloud.toast.com/v1/AUTH_a1fd1f2df1584962b6a00c01becf228a/oneq_storage/"+fileName);
        assertEquals(httpResponseCode, 204);
    }

    @Test
    public void downloadObjectTest() throws ObjectStorageException, IOException{        
        String filePath = "https://api-storage.cloud.toast.com/v1/AUTH_a1fd1f2df1584962b6a00c01becf228a/oneq_storage/noun_58319_cc_20160229110205.png";
        InputStream in = objectStorage.downloadObject(filePath);
        
        assertNotNull(in);
        in.close();
    }
    
    @Test(expected=ObjectStorageException.class)
    public void uploadObjectTest_파일을보내지않을때() throws ObjectStorageException, IOException{        
        String fileName = "testFile2.txt";
        objectStorage.uploadObject(fileName, null);
    }
    
    @Test(expected=ObjectStorageException.class)
    public void deleteObjectTest_잘못된경로() throws ObjectStorageException, IOException{        
        String fileName = "404NotFoundFile.txt";
        objectStorage.deleteObject(fileName);
    }
    
}
