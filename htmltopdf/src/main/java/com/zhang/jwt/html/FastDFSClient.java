package com.zhang.jwt.html;

import org.csource.common.IniFileReader;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FastDFSClient {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(FastDFSClient.class);
    private static final String FASTDFS_CLIENT_PROPERTIES = "fastdfs-client.properties";
    public static sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    public static sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    public static String viewHost;

    static {
        try {
            Properties props = new Properties();
            InputStream in = IniFileReader.loadFromOsFileSystemOrClasspathAsStream(FASTDFS_CLIENT_PROPERTIES);
            if (in != null) {
                props.load(in);
            }
            viewHost = props.getProperty("fastdfs.view.host.address");
            ClientGlobal.initByProperties(FASTDFS_CLIENT_PROPERTIES);
        } catch (Exception e) {
            logger.error("FastDFS Client Init Fail!", e);
        }
    }

    public static String upload(byte[] file_buff, String fileName) throws Exception {

        TrackerServer trackerServer = null;
        try {

            TrackerClient tracker = new TrackerClient();
            trackerServer = tracker.getConnection();

            StorageClient storageClient = new StorageClient(trackerServer, null);
            NameValuePair valuePair = new NameValuePair();
            valuePair.setName("name");
            valuePair.setValue("tenmao test");
            NameValuePair[] pairs = {valuePair};

            String extName = null;
            if (fileName.contains(".")) {
                extName = fileName.substring(fileName.lastIndexOf(".") + 1);
            } else {
                return "";
            }

            String[] fileIds = storageClient.upload_file(file_buff, extName, pairs);
            return viewHost + fileIds[0] + "/" + fileIds[1];
        } finally {
            try {
                if (null != trackerServer) {
                    trackerServer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String upload(String base64Str, String fileName) throws Exception {
        byte[] data = decoder.decodeBuffer(base64Str);
        String url = upload(data, fileName);
        return url;

    }

}