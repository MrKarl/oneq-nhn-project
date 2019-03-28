package com.toast.oneq.service;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.toast.oneq.dao.HashDao;
import com.toast.oneq.dao.ItemDao;
import com.toast.oneq.dao.QuestionDao;
import com.toast.oneq.exception.FileUploadException;
import com.toast.oneq.vo.HashVo;
import com.toast.oneq.vo.ItemVo;
import com.toast.oneq.vo.QuestionInsertVo;

@RunWith(MockitoJUnitRunner.class)
public class QuestionInsertServiceTest {
    @Mock
    private QuestionDao questionDao;
    @Mock
    private HashDao hashDao;
    @Mock
    private ItemDao itemDao;    
    @Mock
    private FileUploadService fileUploadDao;

    @InjectMocks
    private QuestionInsertService questionInsertService;

    private QuestionInsertVo questionInsertVo;
    private List<String> itemTitleList;
    private List<Integer> itemMediaTypeCodeList;
    private List<String> itemMediaPathList;
    private List<String> hashNameList;
    private List<HashVo> hashList;
    private List<ItemVo> itemList;
    private List<Integer> mediaTypeList;
    private List<String> mediaPathList;
    private List<MultipartFile> itemFiles;

    private List<StringBuffer> fileNames;

    private byte[] content;

    @Before
    public void init() throws FileUploadException, IOException {
        questionInsertVo = new QuestionInsertVo();
        itemTitleList = new ArrayList<String>();
        itemMediaTypeCodeList = new ArrayList<Integer>();
        itemMediaPathList = new ArrayList<String>();
        hashNameList = new ArrayList<String>();
        hashList = new ArrayList<HashVo>();
        itemList = new ArrayList<ItemVo>();
        
        itemFiles = new ArrayList<MultipartFile>();
        fileNames = new ArrayList<StringBuffer>();
        
        setUpQuestionData();
        setUpHashData();
        setUpItemData();
    }
    
    private void setUpQuestionData(){
        fileNames.add(new StringBuffer("questionFile1.jpg"));        fileNames.add(new StringBuffer("itemFile1.jpg"));
        fileNames.add(new StringBuffer("itemFile2.jpg"));        fileNames.add(new StringBuffer("itemFile3.jpg"));
        content = "This is test File Content".getBytes();

        MockMultipartFile questionFile = new MockMultipartFile(fileNames.get(0).toString(), fileNames.get(0).toString(),
                "multipart/mixed", content);
        itemFiles.add(new MockMultipartFile(fileNames.get(1).toString(), fileNames.get(1).toString(), "multipart/mixed", content));
        itemFiles.add(new MockMultipartFile(fileNames.get(2).toString(), fileNames.get(2).toString(), "multipart/mixed", content));
        itemFiles.add(new MockMultipartFile(fileNames.get(3).toString(), fileNames.get(3).toString(), "multipart/mixed", content));
        
        itemTitleList.add("아이템1 제목");
        itemTitleList.add("아이템2 제목");
        itemTitleList.add("아이템3 제목");

        itemMediaTypeCodeList.add(0);
        itemMediaTypeCodeList.add(1);
        itemMediaTypeCodeList.add(2);

        itemMediaPathList.add("");
        itemMediaPathList.add("http://img.naver.net/static/www/u/2013/0731/nmms_224940510.gif");
        itemMediaPathList.add("https://www.youtube.com/embed/rfSGdZwEvtg");

        hashNameList.add("해시1");
        hashNameList.add("해시2");
        hashNameList.add("해시3");
                
        questionInsertVo.setContent("컨텐츠")
            .setHashName(hashNameList)
            .setItemMediaPath(itemMediaPathList)
            .setItemMediaTypeCode(itemMediaTypeCodeList)
            .setItemTitle(itemTitleList)
            .setMediaPath("")
            .setMediaTypeCode((byte)0)
            .setQuestionTypeCode((byte)0)
            .setTitle("제목")
            .setUserId(0)
            .setVoteUserCountMax(200)
            .setQuestionMediaFile(questionFile)
            .setItemMediaFiles((List<MultipartFile>)itemFiles);
    }
    
    private void setUpHashData(){
        for(String hashName : questionInsertVo.getHashName()){
            HashVo hvo = new HashVo();
            hvo.setHashName(hashName);
            hashList.add(hvo);
        }
    }
    
    private void setUpItemData() throws FileUploadException, IOException {
        mediaTypeList = questionInsertVo.getItemMediaTypeCode();
        mediaPathList = questionInsertVo.getItemMediaPath();
        
        int idx_item = 0;
        for (String title : questionInsertVo.getItemTitle()) {
            ItemVo itemVo = new ItemVo();
            itemVo.setTitle(title);
            itemVo.setMediaTypeCode(mediaTypeList.get(idx_item));
            itemVo.setMediaPath(mediaPathList.get(idx_item));
            if (itemFiles != null && itemVo.isMediaTypeImage() && itemFiles.get(idx_item) != null) {
                MultipartFile itemFile = itemFiles.get(idx_item);
                String filePath = fileUploadDao.uploadFile(itemFile);
                itemVo.setMediaPath(filePath);
            } else {
                itemVo.setMediaPath(mediaPathList.get(idx_item));
            }
            itemList.add(itemVo);
            idx_item++;
        }
    }
    
    @Test
    public void saveQuestionAndItemAndHashTest() throws FileUploadException, IOException {
        for(int i=0; i<questionInsertVo.getItemMediaFiles().size(); i++){
            MultipartFile file = questionInsertVo.getItemMediaFiles().get(i);
            when(fileUploadDao.uploadFile(file)).thenReturn("filePath-"+i);
        }
        
        int resultQuestionRegist = questionInsertService.saveQuestionAndItemAndHash(questionInsertVo);
        assertThat(resultQuestionRegist, greaterThanOrEqualTo(0));
    }    
    
    @Test
    public void saveQuestionTest() throws FileUploadException, IOException {
         // 투표에 질문 파일이 등록되어있지 않았을 때, 투표 mediapath 검사
        when(questionDao.insertQuestion(questionInsertVo)).thenReturn(1);
        when(questionDao.selectQuestionInsertVo(1)).thenReturn(questionInsertVo);
        int questionId = questionInsertService.saveQuestion(questionInsertVo);
        assertThat(questionDao.selectQuestionInsertVo(questionId), equalTo(questionInsertVo));
        
        // // 투표의 미디어 타입이 이미지가 아닐 때, 투표 mediapath 검사
        questionInsertVo.setMediaTypeCode((byte)0);
        questionId = questionInsertService.saveQuestion(questionInsertVo);
        assertThat(questionDao.selectQuestionInsertVo(questionId), equalTo(questionInsertVo));

        // File OK / Image OK
        MockMultipartFile questionFile = new MockMultipartFile(fileNames.get(0).toString(), fileNames.get(0).toString(),
                "multipart/mixed", content);
        questionInsertVo.setQuestionMediaFile(questionFile);
        questionInsertVo.setMediaTypeCode((byte)1);
        questionId = questionInsertService.saveQuestion(questionInsertVo);
        assertThat(questionDao.selectQuestionInsertVo(questionId), equalTo(questionInsertVo));
        
        // File OK / Image NO
        questionInsertVo.setMediaTypeCode((byte)0);
        questionId = questionInsertService.saveQuestion(questionInsertVo);
        assertThat(questionDao.selectQuestionInsertVo(questionId), equalTo(questionInsertVo));
        
        // File NO / Image OK
        questionInsertVo.setQuestionMediaFile(null);
        questionInsertVo.setMediaTypeCode((byte)1);
        questionId = questionInsertService.saveQuestion(questionInsertVo);
        assertThat(questionDao.selectQuestionInsertVo(questionId), equalTo(questionInsertVo));
        
        // File NO / Image NO
        questionInsertVo.setQuestionMediaFile(questionFile);
        questionInsertVo.setMediaTypeCode((byte)0);
        questionId = questionInsertService.saveQuestion(questionInsertVo);
        assertThat(questionDao.selectQuestionInsertVo(questionId), equalTo(questionInsertVo));
    }

    @Test
    public void saveHashesTest() {
        int questionId = 1;
        questionInsertVo.setHashName(null);
        int insertHashCount = questionInsertService.saveHashes(questionId, questionInsertVo);
        assertThat(insertHashCount, equalTo(0));

        questionInsertVo.setHashName(hashNameList);
        insertHashCount = questionInsertService.saveHashes(questionId,
        questionInsertVo);
        assertThat(insertHashCount, equalTo(0));
    }

    @Test
    public void saveItemsTest() throws FileUploadException, IOException {
        // 항목의 개수가 하나도 없을 때, 0을 리턴한다.
        int questionId = 1;      
        questionInsertVo.setItemTitle(null);
        int insertItemCount = questionInsertService.saveItems(questionId, questionInsertVo);
        assertThat(insertItemCount, equalTo(0));
        
        // 항목이 존재할 때, 삽입된 항목의 개수를 리턴한다.
        questionInsertVo.setItemTitle(itemTitleList);
        questionInsertVo.setItemMediaTypeCode(itemMediaTypeCodeList);
        questionInsertVo.setItemMediaPath(itemMediaPathList);

        for(int i=0; i<questionInsertVo.getItemMediaFiles().size(); i++){
            MultipartFile file = questionInsertVo.getItemMediaFiles().get(i);
            when(fileUploadDao.uploadFile(file)).thenReturn("filePath-"+i);
        }
        
        List<ItemVo> testItemVo = questionInsertService.generateItemList(questionInsertVo);
        questionInsertVo.setItemVoList(testItemVo);
        
        when(itemDao.insertItems(questionId, questionInsertVo.getItemVoList())).thenReturn(3);
        insertItemCount = questionInsertService.saveItems(questionId, questionInsertVo);
        verify(itemDao).insertItems(questionId, testItemVo);
        assertThat(insertItemCount, equalTo(questionInsertVo.getItemTitle().size()));
    }   
    
    @Test
    public void isExistItemTest() {
        boolean isItemExisted = questionInsertService.isExistItem(questionInsertVo);
        assertThat(isItemExisted, equalTo(true));
        
        questionInsertVo.setItemTitle(null);
        isItemExisted = questionInsertService.isExistItem(questionInsertVo);
        assertThat(isItemExisted, equalTo(false));
    }
    
    @Test
    public void uploadFilesTest() throws FileUploadException, IOException {
        List<MultipartFile> files = new ArrayList<MultipartFile>();
        int length = 0;
        
        String tempFileName = "temp_";
        for(int i=0; i<5; i++){
            MockMultipartFile file = new MockMultipartFile(tempFileName+i, tempFileName+i, "multipart/mixed", content);
            files.add(file);
        }
        for(int i=0; i<files.size(); i++){
            when(fileUploadDao.uploadFile(files.get(i))).thenThrow(new FileUploadException(""));
        }        
        questionInsertService.uploadFiles(files, length);
    }

    @Test(expected=FileUploadException.class)
    public void uploadFilesTest_파일업로드예외() throws FileUploadException, IOException {
        List<MultipartFile> files = new ArrayList<MultipartFile>();
        int length = 5;
        
        String tempFileName = "temp_";
        for(int i=0; i<length; i++){
            if(i==3){
                files.add(null);
            }else{
                MockMultipartFile file = new MockMultipartFile(tempFileName+i, tempFileName+i, "multipart/mixed", content);
                files.add(file);
            }
        }
        
        for(int i=0; i<length; i++){
            if(i==2){
                when(fileUploadDao.uploadFile(files.get(i))).thenThrow(new FileUploadException(""));
            }else{
                when(fileUploadDao.uploadFile(files.get(i))).thenReturn("filePath-"+i);
            }
        }

        questionInsertService.uploadFiles(files, length);
    }
    
    @Test
    public void generateItemListTest_파일이꽉찼을때() throws FileUploadException, IOException {
        List<ItemVo> itemList = new ArrayList<ItemVo>();
        List<String>  titleList = questionInsertVo.getItemTitle();
        List<Integer> mediaTypeList = questionInsertVo.getItemMediaTypeCode();
        List<String>  mediaPathList = questionInsertVo.getItemMediaPath();
        List<MultipartFile>    itemFiles = questionInsertVo.getItemMediaFiles();
        int idx_item = 0;
        for(String title : titleList){
            ItemVo ivo = new ItemVo();
            ivo.setTitle(title);
            ivo.setMediaTypeCode(mediaTypeList.get(idx_item));
            
            if(itemFiles != null && ivo.isMediaTypeImage() && itemFiles.get(idx_item) != null){
                MultipartFile itemFile = itemFiles.get(idx_item);
                String filePath = fileUploadDao.uploadFile(itemFile);
                ivo.setMediaPath(filePath);
            }else{
                ivo.setMediaPath(mediaPathList.get(idx_item));
            }
            itemList.add(ivo);
            idx_item++;
        }

        for(int i=0; i<questionInsertVo.getItemMediaFiles().size(); i++){
            MultipartFile file = questionInsertVo.getItemMediaFiles().get(i);
            when(fileUploadDao.uploadFile(file)).thenReturn("filePath-"+i);
        }
        
        List<ItemVo> generatedItemList = questionInsertService.generateItemList(questionInsertVo);
        for(int i=0; i<itemList.size(); i++){
            assertEquals(itemList.get(i).getTitle(), generatedItemList.get(i).getTitle());
            assertEquals(itemList.get(i).getMediaTypeCode(), generatedItemList.get(i).getMediaTypeCode());
        }
        
        questionInsertVo.setItemTitle(null);
        generatedItemList = questionInsertService.generateItemList(questionInsertVo);
        assertEquals(generatedItemList.size(), 0);
    }

    @Test
    public void generateItemListTest_파일이듬성듬성일때() throws FileUploadException, IOException {
        List<MultipartFile> files = new ArrayList<MultipartFile>();
        int length = 5;
        
        String tempFileName = "temp_";
        for(int i=0; i<length; i++){
            if(i==1){
                files.add(null);
            }else{
                MockMultipartFile file = new MockMultipartFile(tempFileName+i, tempFileName+i, "multipart/mixed", content);
                files.add(file);
                when(fileUploadDao.uploadFile(file)).thenReturn("filePath-"+i);
            }
        }
        questionInsertVo.setItemMediaFiles(files);
        List<ItemVo> generatedItemList = questionInsertService.generateItemList(questionInsertVo);        
        for(int i=0; i<itemList.size(); i++){
            assertEquals(itemList.get(i).getTitle(), generatedItemList.get(i).getTitle());
            assertEquals(itemList.get(i).getMediaTypeCode(), generatedItemList.get(i).getMediaTypeCode());
        }
    }


}
