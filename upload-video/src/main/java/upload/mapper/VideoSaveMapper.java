package upload.mapper;

import org.apache.ibatis.annotations.Param;

public interface VideoSaveMapper {
    int insertVideoUrl(@Param("videoUrl") String videoUrl,
                       @Param("userId") Integer userId,
                       @Param("professionalName") String professionalName,
                       @Param("imageUrl") String imageUrl,
                       @Param("title") String title,
                       @Param("createTime") String createTime,
                       @Param("collectionCount") Integer collectionCount,
                       @Param("thumbCount") Integer thumbCount,
                       @Param("visitCount") Integer visitCount,
                       @Param("commentCount") Integer commentCount);
}
