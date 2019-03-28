package com.toast.oneq.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.toast.oneq.dao.HashDao;
import com.toast.oneq.dao.ItemDao;
import com.toast.oneq.dao.QuestionDao;
import com.toast.oneq.exception.FileUploadException;
import com.toast.oneq.vo.HashVo;
import com.toast.oneq.vo.ItemVo;
import com.toast.oneq.vo.QuestionInsertVo;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
public class QuestionInsertService {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private HashDao hashDao;
    @Autowired
    private FileUploadService fileUploadService;
    
    /**
     * 1. 투표 등록 - Question Meta정보 등록 및 파일 업로드 2. 해시 등록 - Hash name 정보 등록 3. 항목
     * 등록 - Item Meta정보 등록 및 파일 업로드
     * 
     * @param ${question/items/hashs
     *            정보}
     * @return 성공시, 투표 리스트 화면, 실패시, 실패 메시지
     * @throws SQLException 
     * @throws FileUploadException
     * @throws IOException 
     **/
    public int saveQuestionAndItemAndHash(QuestionInsertVo questionInsertVo) throws FileUploadException, IOException {
        int questionId = saveQuestion(questionInsertVo);
        saveHashes(questionId, questionInsertVo);
        saveItems(questionId, questionInsertVo);
        return questionId;
    }
    
    protected int saveQuestion(QuestionInsertVo questionInsertVo) throws FileUploadException, IOException {
        MultipartFile questionFile = questionInsertVo.getQuestionMediaFile();
        if (questionFile != null && questionInsertVo.isMediaTypeImage()) {
            String filePath = fileUploadService.uploadFile(questionFile);
            questionInsertVo.setMediaPath(filePath);
        }
        
        return questionDao.insertQuestion(questionInsertVo);
    }

    protected int saveHashes(int questionId, QuestionInsertVo questionInsertVo) {
        if (questionInsertVo.getHashName() == null) {
            return 0;
        }
        List<String> hashNameList = questionInsertVo.getHashName();
        List<String> hashNameSet = new ArrayList<String>(new HashSet<String>(hashNameList));
        List<HashVo> hashList = new ArrayList<HashVo>();
        for (String hashName : hashNameSet) {
            HashVo hvo = new HashVo();
            hvo.setHashName(hashName);
            hashList.add(hvo);
        }
        return hashDao.insertHashs(questionId, hashList);
    }

    protected int saveItems(int questionId, QuestionInsertVo questionInsertVo) throws FileUploadException, IOException {
        List<ItemVo> itemList = generateItemList(questionInsertVo);
        int itemInsertCount = itemDao.insertItems(questionId, itemList);

        return itemInsertCount;
    }

    protected boolean isExistItem(QuestionInsertVo questionInsertVo) {
        return (questionInsertVo.getItemTitle() != null);
    }
    
    protected List<String> uploadFiles(List<MultipartFile> files, int length) throws FileUploadException, IOException{
        List<String> filePaths = new ArrayList<String>();
        int filesLength = files == null ? 0 : files.size();
        try {
            for (int i=0; i<length; i++) {
                if (i < filesLength && files.get(i) != null) {
                    filePaths.add(fileUploadService.uploadFile(files.get(i)));
                }else {
                    filePaths.add("-1");
                }
            }
            return filePaths;
        } catch (FileUploadException fue) {
            for (int i=0; i<filePaths.size(); i++) {
                if(!filePaths.get(i).equals("-1")){
                    fileUploadService.deleteFile(filePaths.get(i));
                }                
            }
            throw fue;
        }
    }

    protected List<ItemVo> generateItemList(QuestionInsertVo questionInsertVo) throws FileUploadException, IOException {
        if (!isExistItem(questionInsertVo)) {
            return new ArrayList<ItemVo>();
        }

        if (questionInsertVo.getItemVoList() != null) {
            return questionInsertVo.getItemVoList();
        }

        List<String> itemTitleList = questionInsertVo.getItemTitle();
        List<Integer> mediaTypeCodeList = questionInsertVo.getItemMediaTypeCode();
        List<String> mediaPathList = questionInsertVo.getItemMediaPath();
        List<MultipartFile> itemFiles = questionInsertVo.getItemMediaFiles();
        int itemCount = itemTitleList.size();
        List<ItemVo> itemVoList = new ArrayList<ItemVo>();        
        List<String> itemFilePathList = uploadFiles(itemFiles, itemCount);
        int idx = 0;
        for (String title : itemTitleList) {
            ItemVo itemVo = new ItemVo();
            itemVo.setTitle(title);
            itemVo.setMediaTypeCode(mediaTypeCodeList.get(idx));

            if (itemFilePathList.get(idx).equals("-1")) {
                itemVo.setMediaPath(mediaPathList.get(idx));
            } else {
                itemVo.setMediaPath(itemFilePathList.get(idx));
            }
            itemVoList.add(itemVo);
            idx++;
        }        
        return itemVoList;
    }
}
