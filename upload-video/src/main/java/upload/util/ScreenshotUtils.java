package upload.util;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class ScreenshotUtils {

    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param videourl 源视频文件路径
     * @throws Exception
     */
    public static String fetchFrame(String videourl, MultipartFile file) throws Exception {
        //储存截图的文件
        String projectPath = System.getProperty("user.dir");
        //新的文件名
        String pngName = System.currentTimeMillis() + "_" + new Random().nextInt(1000) + ".png";
        //项目地址
        String destDir = projectPath + File.separator + "upload-video" + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator;

        File file1 = new File(destDir);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        String coverimgPath = file1.getPath() + File.separator + pngName;

        File cutpic = new File(coverimgPath);

        //FFmpegFrameGrabb读取时间随机截图类
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videourl);
        ff.start();
        // 表示视频的总图片数量
        int lenght = ff.getLengthInFrames();
        int i = 0;
        Frame f = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            f = ff.grabFrame();
            if ((i > 5) && (f.image != null)) {
                break;
            }
            i++;
        }
        IplImage img = f.image;
        int owidth = img.width();
        int oheight = img.height();
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH),
                0, 0, null);
        ImageIO.write(bi, "jpg", cutpic);
        //ff.flush();
        ff.stop();

        return coverimgPath;
    }


}