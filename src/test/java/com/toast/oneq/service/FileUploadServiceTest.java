package com.toast.oneq.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import com.toast.oneq.exception.FileUploadException;
import com.toast.oneq.exception.ObjectStorageException;
import com.toast.oneq.storage.ObjectStorage;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextHierarchy({
//    @ContextConfiguration("/spring/application-context.xml"),
//    @ContextConfiguration("/spring/servlet-context.xml")
//})
@RunWith(MockitoJUnitRunner.class)
public class FileUploadServiceTest {
    private static final String FILE_SAVE_REAL_PATH = "/home1/irteam/files/"; // TODO: 코드리뷰
    private static final String FILE_SAVE_PROJECT_PATH = "https://api-storage.cloud.toast.com/v1/AUTH_a1fd1f2df1584962b6a00c01becf228a/oneq_storage/";
    
    private static final String FILE_PATH_IN_PROJECT = "/file/image/";
    
    private StringBuilder fileName = new StringBuilder("test.jpg");
    private byte[] content = "hello world".getBytes();
    
    @Mock
    ObjectStorage objectStorage;
    
    @InjectMocks
    private FileUploadService fileUploadService;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);        
    }
    
    @Test
    public void uploadFileTest() throws Exception {        
        MockMultipartFile file = new MockMultipartFile(fileName.toString(), fileName.toString(), "multipart/mixed", content);
        
        String nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHMMss"));
        String timeStampString = nowDateTime;
        
        StringBuffer fileName = new StringBuffer(file.getOriginalFilename());
        String fileExt = fileName.toString().substring(fileName.lastIndexOf(".")+1);
        String fileTitle = fileName.substring(0, fileName.lastIndexOf(".")-1);
        byte[] encoded = Base64.encodeBase64(fileTitle.getBytes());
        fileTitle = new String(encoded);
        String fileNewName = fileTitle + "_" + timeStampString + "." + fileExt;
        
        
        when(objectStorage.uploadObject(fileNewName, file)).thenReturn(fileNewName);
        String uploadSuccessFilePath = fileUploadService.uploadFile(file);
        assertEquals(FILE_PATH_IN_PROJECT + fileNewName, uploadSuccessFilePath);
    }
    
    @Test(expected=FileUploadException.class)
    public void uploadFileTest_ObjectStorage예외() throws ObjectStorageException, FileUploadException, IOException {        
        MockMultipartFile file = new MockMultipartFile(fileName.toString(), fileName.toString(), "multipart/mixed", content);
        
        String nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHMMss"));
        String timeStampString = nowDateTime;
        
        StringBuffer fileName = new StringBuffer(file.getOriginalFilename());
        String fileExt = fileName.toString().substring(fileName.lastIndexOf(".")+1);
        String fileTitle = fileName.substring(0, fileName.lastIndexOf(".")-1);
        byte[] encoded = Base64.encodeBase64(fileTitle.getBytes());
        fileTitle = new String(encoded);
        String fileNewName = fileTitle + "_" + timeStampString + "." + fileExt;
        
        when(objectStorage.uploadObject(fileNewName, file)).thenThrow(new ObjectStorageException("Upload Fail"));
        fileUploadService.uploadFile(file);
    }
    
    @Test(expected=FileUploadException.class)
    public void uploadFileTest_파일NULL() throws Exception {        
        fileUploadService.uploadFile(null);
    }
    
    @Test
    public void deleteFileTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("content", fileName.toString(), "multipart/mixed", content);
        
        String nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHMMss"));
        String timeStampString = nowDateTime;
        
        String fileNewName = fileName.insert(fileName.indexOf("."), "_" + timeStampString).toString();
        fileUploadService.deleteFile(FILE_SAVE_PROJECT_PATH + fileNewName);
        verify(objectStorage).deleteObject(FILE_SAVE_PROJECT_PATH + fileNewName);
    }
    
    @Test(expected=FileUploadException.class)
    public void deleteFileTest_유효하지않은파일() throws FileUploadException, ObjectStorageException, IOException {
        when(objectStorage.deleteObject("ThereIsNoFile.txt")).thenThrow(new ObjectStorageException("Upload Fail"));
        fileUploadService.deleteFile("ThereIsNoFile.txt");
    }
    
    public void makeFileNewName() throws FileUploadException {
        MockMultipartFile file = new MockMultipartFile(fileName.toString(), fileName.toString(), "multipart/mixed", content);
        fileUploadService.makeFileNewName(file);
    }
    
    @Test(expected=FileUploadException.class)
    public void makeFileNewName_유효하지않은파일() throws FileUploadException {
        MockMultipartFile file = new MockMultipartFile(fileName.toString(), "", "multipart/mixed", content);
        fileUploadService.makeFileNewName(file);
    }
}
