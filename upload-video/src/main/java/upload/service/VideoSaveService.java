package upload.service;

import org.springframework.web.multipart.MultipartFile;

public interface VideoSaveService {
    Integer insertVideo(MultipartFile file,
                        Integer userId,
                        String professionalName,
                        String title,
                        Integer collectionCount,
                        Integer thumbCount,
                        Integer visitCount,
                        Integer commentCount)throws Exception;
}
