package com.zhang.word;

import org.csource.common.IniFileReader;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
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
            logger.error("FastDFS Client Init Fail!",e);
        }
    }

    public static String upload(byte[] file_buff,String fileName) throws Exception  {

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

            String[] fileIds = storageClient.upload_file(file_buff,extName,pairs);
            return viewHost+fileIds[0]+"/"+fileIds[1];
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

    public static String upload(String base64Str,String fileName) throws Exception {
        byte[] data = decoder.decodeBuffer(base64Str);
        String url = upload(data,fileName);
        return url;

    }

    public static void main(String[] args) {
        String svgStr = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4NCjwhRE9DVFlQRSBzdmcgUFVC\n" +
                "TElDICItLy9XM0MvL0RURCBTVkcgMS4wLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL1RSLzIwMDEv\n" +
                "UkVDLVNWRy0yMDAxMDkwNC9EVEQvc3ZnMTAuZHRkIj4NCjxzdmcgeG1sbnM9Imh0dHA6Ly93d3cu\n" +
                "dzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGlu\n" +
                "ayIgZmlsbC1ydWxlPSJldmVub2RkIiBoZWlnaHQ9IjAuNjM4ODg4ODg4ODg4ODg4OGluIiBwcmVz\n" +
                "ZXJ2ZUFzcGVjdFJhdGlvPSJub25lIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHZpZXdCb3g9IjAg\n" +
                "MCA1MTg0IDE0NzIiIHdpZHRoPSIyLjI1aW4iPg0KPHN0eWxlIHR5cGU9InRleHQvY3NzIj4NCi5i\n" +
                "cnVzaDAgeyBmaWxsOiByZ2IoMjU1LDI1NSwyNTUpOyB9DQoucGVuMCB7IHN0cm9rZTogcmdiKDAs\n" +
                "MCwwKTsgc3Ryb2tlLXdpZHRoOiAxOyBzdHJva2UtbGluZWpvaW46IHJvdW5kOyB9DQoucGVuMSB7\n" +
                "IHN0cm9rZTogcmdiKDAsMCwwKTsgc3Ryb2tlLXdpZHRoOiAxNjsgc3Ryb2tlLWxpbmVqb2luOiBy\n" +
                "b3VuZDsgfQ0KLnBlbjIgeyBzdHJva2U6IHJnYigwLDAsMCk7IHN0cm9rZS13aWR0aDogODsgc3Ry\n" +
                "b2tlLWxpbmVqb2luOiByb3VuZDsgfQ0KLmZvbnQwIHsgZm9udC1zaXplOiAyMDJweDsgZm9udC1m\n" +
                "YW1pbHk6ICJUaW1lcyBOZXcgUm9tYW4iLCBzZXJpZjsgfQ0KLmZvbnQxIHsgZm9udC1zaXplOiAz\n" +
                "NDZweDsgZm9udC1mYW1pbHk6ICJUaW1lcyBOZXcgUm9tYW4iLCBzZXJpZjsgfQ0KLmZvbnQyIHsg\n" +
                "Zm9udC1zdHlsZTogaXRhbGljOyBmb250LXNpemU6IDM0NnB4OyBmb250LWZhbWlseTogIlRpbWVz\n" +
                "IE5ldyBSb21hbiIsIHNlcmlmOyB9DQouZm9udDMgeyBmb250LXNpemU6IDMxM3B4OyBmb250LWZh\n" +
                "bWlseTogU3ltYm9sLCBzZXJpZjsgfQ0KLmZvbnQ0IHsgZm9udC1zaXplOiA0NzBweDsgZm9udC1m\n" +
                "YW1pbHk6IFN5bWJvbCwgc2VyaWY7IH0NCi5mb250NSB7IGZvbnQtd2VpZ2h0OiBib2xkOyBmb250\n" +
                "LXNpemU6IDE2cHg7IGZvbnQtZmFtaWx5OiBTeXN0ZW0sIHNhbnMtc2VyaWY7IH0NCjwvc3R5bGU+\n" +
                "DQo8Zz4NCjxsaW5lIGNsYXNzPSJwZW4xIiBmaWxsPSJub25lIiB4MT0iNjQiIHgyPSIyOTYiIHkx\n" +
                "PSI2MDgiIHkyPSI2MDgiLz4NCjxsaW5lIGNsYXNzPSJwZW4yIiBmaWxsPSJub25lIiB4MT0iMTA5\n" +
                "MSIgeDI9IjEyMjUiIHkxPSIxMTc1IiB5Mj0iMTE3NSIvPg0KPGxpbmUgY2xhc3M9InBlbjEiIGZp\n" +
                "bGw9Im5vbmUiIHgxPSIxMzA3IiB4Mj0iMTU0NSIgeTE9IjYwOCIgeTI9IjYwOCIvPg0KPGxpbmUg\n" +
                "Y2xhc3M9InBlbjEiIGZpbGw9Im5vbmUiIHgxPSIyMzI1IiB4Mj0iMjU1NyIgeTE9IjYwOCIgeTI9\n" +
                "IjYwOCIvPg0KPGxpbmUgY2xhc3M9InBlbjIiIGZpbGw9Im5vbmUiIHgxPSI0MDY5IiB4Mj0iNDIw\n" +
                "MyIgeTE9Ijg4NyIgeTI9Ijg4NyIvPg0KPHRleHQgY2xhc3M9ImZvbnQwIiBmaWxsPSJyZ2IoMCww\n" +
                "LDApIiBzdHJva2U9Im5vbmUiIHN0eWxlPSJkb21pbmFudC1iYXNlbGluZTogYWxwaGFiZXRpYzsi\n" +
                "IHg9IjExMDIiIHhtbDpsYW5nPSJlbiIgeG1sOnNwYWNlPSJwcmVzZXJ2ZSIgeT0iMjY0Ij4xPC90\n" +
                "ZXh0Pg0KPHRleHQgY2xhc3M9ImZvbnQwIiBmaWxsPSJyZ2IoMCwwLDApIiBzdHJva2U9Im5vbmUi\n" +
                "IHN0eWxlPSJkb21pbmFudC1iYXNlbGluZTogYWxwaGFiZXRpYzsiIHg9IjQwNDEiIHhtbDpsYW5n\n" +
                "PSJlbiIgeG1sOnNwYWNlPSJwcmVzZXJ2ZSIgeT0iNTMyIj4xPC90ZXh0Pg0KPHRleHQgY2xhc3M9\n" +
                "ImZvbnQwIiBmaWxsPSJyZ2IoMCwwLDApIiBzdHJva2U9Im5vbmUiIHN0eWxlPSJkb21pbmFudC1i\n" +
                "YXNlbGluZTogYWxwaGFiZXRpYzsiIHg9IjQwODAiIHhtbDpsYW5nPSJlbiIgeG1sOnNwYWNlPSJw\n" +
                "cmVzZXJ2ZSIgeT0iODAzIj4xPC90ZXh0Pg0KPHRleHQgY2xhc3M9ImZvbnQwIiBmaWxsPSJyZ2Io\n" +
                "MCwwLDApIiBzdHJva2U9Im5vbmUiIHN0eWxlPSJkb21pbmFudC1iYXNlbGluZTogYWxwaGFiZXRp\n" +
                "YzsiIHg9IjExMDIiIHhtbDpsYW5nPSJlbiIgeG1sOnNwYWNlPSJwcmVzZXJ2ZSIgeT0iMTA5MSI+\n" +
                "MTwvdGV4dD4NCjx0ZXh0IGNsYXNzPSJmb250MCIgZmlsbD0icmdiKDAsMCwwKSIgc3Ryb2tlPSJu\n" +
                "b25lIiBzdHlsZT0iZG9taW5hbnQtYmFzZWxpbmU6IGFscGhhYmV0aWM7IiB4PSI0MDg0IiB4bWw6\n" +
                "bGFuZz0iZW4iIHhtbDpzcGFjZT0icHJlc2VydmUiIHk9IjExMTciPjI8L3RleHQ+DQo8dGV4dCBj\n" +
                "bGFzcz0iZm9udDAiIGZpbGw9InJnYigwLDAsMCkiIHN0cm9rZT0ibm9uZSIgc3R5bGU9ImRvbWlu\n" +
                "YW50LWJhc2VsaW5lOiBhbHBoYWJldGljOyIgeD0iMTEwNiIgeG1sOmxhbmc9ImVuIiB4bWw6c3Bh\n" +
                "Y2U9InByZXNlcnZlIiB5PSIxNDA1Ij4yPC90ZXh0Pg0KPHRleHQgY2xhc3M9ImZvbnQxIiBmaWxs\n" +
                "PSJyZ2IoMCwwLDApIiBzdHJva2U9Im5vbmUiIHN0eWxlPSJkb21pbmFudC1iYXNlbGluZTogYWxw\n" +
                "aGFiZXRpYzsiIHg9Ijg0IDEzMzAgMjM0NSIgeG1sOmxhbmc9ImVuIiB4bWw6c3BhY2U9InByZXNl\n" +
                "cnZlIiB5PSI0NjIiPjExMTwvdGV4dD4NCjx0ZXh0IGNsYXNzPSJmb250MSIgZmlsbD0icmdiKDAs\n" +
                "MCwwKSIgc3Ryb2tlPSJub25lIiBzdHlsZT0iZG9taW5hbnQtYmFzZWxpbmU6IGFscGhhYmV0aWM7\n" +
                "IiB4PSI1NzIgMjgzMyAzMzczIDM0NzUgMzk5MSA0NTU3IDQ2NTkgNDkxNyIgeG1sOmxhbmc9ImVu\n" +
                "IiB4bWw6c3BhY2U9InByZXNlcnZlIiB5PSI3MDQiPjExbG58bG4yPC90ZXh0Pg0KPHRleHQgY2xh\n" +
                "c3M9ImZvbnQxIiBmaWxsPSJyZ2IoMCwwLDApIiBzdHJva2U9Im5vbmUiIHN0eWxlPSJkb21pbmFu\n" +
                "dC1iYXNlbGluZTogYWxwaGFiZXRpYzsiIHg9IjkwIDIzNTEiIHhtbDpsYW5nPSJlbiIgeG1sOnNw\n" +
                "YWNlPSJwcmVzZXJ2ZSIgeT0iMTAwNCI+MjI8L3RleHQ+DQo8dGV4dCBjbGFzcz0iZm9udDIiIGZp\n" +
                "bGw9InJnYigwLDAsMCkiIHN0cm9rZT0ibm9uZSIgc3R5bGU9ImRvbWluYW50LWJhc2VsaW5lOiBh\n" +
                "bHBoYWJldGljOyIgeD0iMTYxMSAxODAzIDM3NTEiIHhtbDpsYW5nPSJlbiIgeG1sOnNwYWNlPSJw\n" +
                "cmVzZXJ2ZSIgeT0iNzA0Ij5keHg8L3RleHQ+DQo8dGV4dCBjbGFzcz0iZm9udDIiIGZpbGw9InJn\n" +
                "YigwLDAsMCkiIHN0cm9rZT0ibm9uZSIgc3R5bGU9ImRvbWluYW50LWJhc2VsaW5lOiBhbHBoYWJl\n" +
                "dGljOyIgeD0iMTM1MSIgeG1sOmxhbmc9ImVuIiB4bWw6c3BhY2U9InByZXNlcnZlIiB5PSIxMDA0\n" +
                "Ij54PC90ZXh0Pg0KPHRleHQgY2xhc3M9ImZvbnQzIiBmaWxsPSJyZ2IoMCwwLDApIiBzdHJva2U9\n" +
                "Im5vbmUiIHN0eWxlPSJkb21pbmFudC1iYXNlbGluZTogYWxwaGFiZXRpYzsiIHg9IjM1MCA3OTQg\n" +
                "MjA0MyAyNjExIDMwNzkgNDI2MyIgeG1sOnNwYWNlPSJwcmVzZXJ2ZSIgeT0iNzA0Ij7CtCstwrQ9\n" +
                "PTwvdGV4dD4NCjx0ZXh0IGNsYXNzPSJmb250NCIgZmlsbD0icmdiKDAsMCwwKSIgc3Ryb2tlPSJu\n" +
                "b25lIiBzdHlsZT0iZG9taW5hbnQtYmFzZWxpbmU6IGFscGhhYmV0aWM7IiB4PSIxMDgyIiB4bWw6\n" +
                "c3BhY2U9InByZXNlcnZlIiB5PSI4NDIiPsOyPC90ZXh0Pg0KPC9nPg0KPC9zdmc+DQo=";
        try {
            byte[] data = decoder.decodeBuffer(svgStr);
            String url = FastDFSClient.upload(data,"test-svg.svg");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}