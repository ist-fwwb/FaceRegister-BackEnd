package cn.pipipan.meetingroom.meetingroomregister.Util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileManagement {

    private final String OSS_AccessKeyId = "LTAIqMIT5KX4oGAT";
    private final String OSS_AccessKeySecret = "wYwZdNHrnvAiM9GNddiXqaeHcB4xfz";
    private final String OSS_BUCKET = "face-file";
    private final String OSS_endpoint = "http://oss-cn-shanghai.aliyuncs.com";

    public void upload(String name, byte[] bytes){
        OSSClient ossClient = new OSSClient(OSS_endpoint, OSS_AccessKeyId, OSS_AccessKeySecret);
        ossClient.putObject(OSS_BUCKET, name, new ByteArrayInputStream(bytes));
        ossClient.shutdown();
    }

    public void download(String name){
        OSSClient ossClient = new OSSClient(OSS_endpoint, OSS_AccessKeyId, OSS_AccessKeySecret);
        OSSObject ossObject = ossClient.getObject(OSS_BUCKET, name);
        InputStream inputStream = ossObject.getObjectContent();
        if (inputStream != null) {
            try{
                File file = new File(name);
                if (!file.exists()) file.createNewFile();
                OutputStream os = new FileOutputStream(file);
                byte[] buffer = new byte[2048];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.close();
                inputStream.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        ossClient.shutdown();
    }
}
