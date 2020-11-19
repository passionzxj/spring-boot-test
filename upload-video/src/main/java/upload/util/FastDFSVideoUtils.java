package upload.util;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FastDFSVideoUtils {
    private static StorageClient1 client1;

    private static StorageServer storeStorage;

    private static StorageServer storageServer;

    static{
        try {
            ClientGlobal.initByProperties("fastdfs_client.conf");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            storeStorage = trackerClient.getStoreStorage(trackerServer);
            String storageIp = storeStorage.getSocket().getInetAddress().getHostAddress();
            Integer port = storeStorage.getSocket().getPort();
            //0表示上传到图片目录，1表示上传到视频目录
            storageServer = new StorageServer(storageIp, port, 1);
            client1 = new StorageClient1(trackerServer, storageServer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    public static String upload(MultipartFile file){
        String oldName = file.getOriginalFilename();
        try {
            return client1.upload_file1(file.getBytes(), oldName.substring(oldName.lastIndexOf(".")+1),null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return null;
    }
}