package com.toast.oneq.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.toast.oneq.dao.ItemDao;
import com.toast.oneq.dao.QuestionDao;
import com.toast.oneq.exception.FileUploadException;
import com.toast.oneq.exception.ObjectStorageException;
import com.toast.oneq.storage.ObjectStorage;

@Service
public class FileUploadService {
    @Autowired
    private ObjectStorage objectStorage;

    public String uploadFile(MultipartFile file) throws FileUploadException, IOException{        
        if(file == null){
            throw new FileUploadException("Null Exception Error. File is Null.");
        }        
        try {
            String fileName = objectStorage.uploadObject(makeFileNewName(file), file);
            String filePath = "/file/image/"+fileName;
            return filePath;
        } catch (ObjectStorageException e) {
            throw new FileUploadException(e.getMessage());
        }
    }
    
    public InputStream downloadFile(String fileName) throws ObjectStorageException, IOException{
        String fullFilePath = objectStorage.getObjectStoreUrl() + objectStorage.getDefaultContainer() + fileName;

        return objectStorage.downloadObject(fullFilePath);
    }
    
    public void deleteFile(String filePath) throws FileUploadException, IOException{
        try{            
            objectStorage.deleteObject(filePath);
        }catch(ObjectStorageException e){
            throw new FileUploadException(e.getMessage());
        }
    }
    
    protected String makeFileNewName(MultipartFile file) throws FileUploadException{
        String nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHMMss"));
        String timeStampString = nowDateTime;
        
        if(file.getOriginalFilename().length() <= 0){
            throw new FileUploadException("Null Exception Error. File is invalid");
        }
        
        StringBuffer fileName = new StringBuffer(file.getOriginalFilename());
        String fileExt = fileName.toString().substring(fileName.lastIndexOf(".")+1);
        String fileTitle = fileName.substring(0, fileName.lastIndexOf(".")-1);
        byte[] encoded = Base64.encodeBase64(fileTitle.getBytes());
        fileTitle = new String(encoded);
        String fileNewName = fileTitle + "_" + timeStampString + "." + fileExt;

        return fileNewName;
    }
}