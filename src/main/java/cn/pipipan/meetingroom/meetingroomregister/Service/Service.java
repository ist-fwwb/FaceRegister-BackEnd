package cn.pipipan.meetingroom.meetingroomregister.Service;

import cn.pipipan.meetingroom.meetingroomregister.Util.FileManagement;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.ImageFormat;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@org.springframework.stereotype.Service
public class Service {
    @Autowired
    FileManagement fileManagement;


    public void doProcess(String fileName){
        FaceEngine faceEngine = new FaceEngine();
        faceEngine.active("3G4cuARsSzRB1yqCdb81k9BYg9REisXrUEvpa9f2Mhz8", "2NLXmxWqt6FGT1v79DWcJaGorM3qxZu4H3ezWXt34r9M");
        EngineConfiguration engineConfiguration = EngineConfiguration.builder().functionConfiguration(
                FunctionConfiguration.builder()
                        .supportAge(true)
                        .supportFace3dAngle(true)
                        .supportFaceDetect(true)
                        .supportFaceRecognition(true)
                        .supportGender(true)
                        .build()).build();
        faceEngine.init(engineConfiguration);
        fileManagement.download(fileName+".jpg");
        File file = new File("/tmp/"+fileName+".jpg");
        if (!file.exists()) {
            System.out.println("file not exits");
            faceEngine.unInit();
            return;
        }
        try{
            ImageInfo imageInfo = getRGBData(file);
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
            faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);

            FaceFeature faceFeature = new FaceFeature();
            faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
            fileManagement.upload(fileName, faceFeature.getFeatureData());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //人脸检测
        faceEngine.unInit();
    }

    private ImageInfo getRGBData(File file) {
        if (file == null)
            return null;
        ImageInfo imageInfo;
        try {
            //将图片文件加载到内存缓冲区
            BufferedImage image = ImageIO.read(file);
            imageInfo = bufferedImage2ImageInfo(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageInfo;
    }

    public ImageInfo getRGBData(InputStream input) {
        if (input == null)
            return null;
        ImageInfo imageInfo;
        try {
            BufferedImage image = ImageIO.read(input);
            imageInfo=bufferedImage2ImageInfo(image);
        } catch (IOException e) {
            return null;
        }finally {
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return imageInfo;
    }

    private ImageInfo bufferedImage2ImageInfo(BufferedImage image) {
        ImageInfo imageInfo = new ImageInfo();
        int width = image.getWidth();
        int height = image.getHeight();
        // 使图片居中
        width = width & (~3);
        height = height & (~3);
        imageInfo.width = width;
        imageInfo.height = height;
        //根据原图片信息新建一个图片缓冲区
        BufferedImage resultImage = new BufferedImage(width, height, image.getType());
        //得到原图的rgb像素矩阵
        int[] rgb = image.getRGB(0, 0, width, height, null, 0, width);
        //将像素矩阵 绘制到新的图片缓冲区中
        resultImage.setRGB(0, 0, width, height, rgb, 0, width);
        //进行数据格式化为可用数据
        BufferedImage dstImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        if (resultImage.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
            ColorConvertOp colorConvertOp = new ColorConvertOp(cs, dstImage.createGraphics().getRenderingHints());
            colorConvertOp.filter(resultImage, dstImage);
        } else {
            dstImage = resultImage;
        }

        //获取rgb数据
        imageInfo.rgbData = ((DataBufferByte) (dstImage.getRaster().getDataBuffer())).getData();
        return imageInfo;
    }


    class ImageInfo {
        public byte[] rgbData;
        public int width;
        public int height;

        public byte[] getRgbData() {
            return rgbData;
        }

        public void setRgbData(byte[] rgbData) {
            this.rgbData = rgbData;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
