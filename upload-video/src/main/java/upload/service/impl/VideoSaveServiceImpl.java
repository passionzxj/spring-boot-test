package upload.service.impl;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import upload.mapper.VideoSaveMapper;
import upload.service.VideoSaveService;
import upload.util.FastDFSClient;
import upload.util.FastDFSFile;
import upload.util.ScreenshotUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class VideoSaveServiceImpl implements VideoSaveService {

    private static final String VIEW_HOST = "http://10.0.0.118:90/file-view/";

    @Autowired
    private VideoSaveMapper videoSaveMapper;

    @Override
    public Integer insertVideo(MultipartFile file,
                               Integer userId,
                               String professionalName,
                               String title,
                               Integer collectionCount,
                               Integer thumbCount,
                               Integer visitCount,
                               Integer commentCount) throws Exception {
        FastDFSFile fastDFSFile = getFastDFSFile(file);
        String[] strings = FastDFSClient.upload(fastDFSFile);
        final String fileId = VIEW_HOST + strings[0] + "/" + strings[1];
        Date date = new Date();
        //HH为24小时制，hh为12小时制
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
        String createTime = dateFormat.format(date);

        //将截图转化为file对象，再将file对象转化为MockMultipartFile 对象
        String cutpicPath = ScreenshotUtils.fetchFrame(fileId, file);
        File cutpic = new File(cutpicPath);
        FileInputStream fileInputStream = new FileInputStream(cutpic);
        MockMultipartFile cutPicFile = new MockMultipartFile(cutpic.getName(), cutpic.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

        //FastDFSUtils将上面转化的MockMultipartFile 对象上传
        FastDFSFile fastDFSFile1 = getFastDFSFile(cutPicFile);
        String[] strings1 = FastDFSClient.upload(fastDFSFile1);
        final String cutPicfileId = VIEW_HOST + strings1[0] + "/" + strings1[1];
        FileUtil.del(cutpicPath);
        return videoSaveMapper.insertVideoUrl(fileId, userId, professionalName, cutPicfileId, title, createTime, collectionCount, thumbCount, visitCount, commentCount);
    }


    private FastDFSFile getFastDFSFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) return new FastDFSFile();
        String ext = filename.substring(filename.lastIndexOf(".") + 1);
        byte[] file_buff = null;
        InputStream inputStream = file.getInputStream();
        if (inputStream != null) {
            int len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        inputStream.close();

        return new FastDFSFile(filename, file_buff, ext);
    }


}