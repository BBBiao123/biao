package com.biao.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.biao.config.AliYunOOSClientConfig;
import com.biao.util.DateUtil;
import com.biao.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AliyunOOSUtils {

    private static Logger logger = LoggerFactory.getLogger(AliyunOOSUtils.class);

    public static final String endpoint = "oss-cn-shenzhen.aliyuncs.com";

    public static boolean uploadToAliyun(AliYunOOSClientConfig clientConfig, byte[] images, String imageName, ObjectMetadata objectMetadata) {
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录
        // https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = clientConfig.getAccessKeyId();
        String accessKeySecret = clientConfig.getAccessKeySecret();
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(clientConfig.getEndpoint(), accessKeyId, accessKeySecret);
        PutObjectResult objectResult = null;
        if (objectMetadata != null) {
            Map<String, String> userMetadata = objectMetadata.getUserMetadata();
            if (userMetadata == null) {
                userMetadata = new HashMap<>();
            }
            userMetadata.put("createTime", DateUtils.getCurrentDateTime());
            objectMetadata.setUserMetadata(userMetadata);
            objectResult = ossClient.putObject(clientConfig.getBucketName(), imageName, new ByteArrayInputStream(images), objectMetadata);
        } else {
            objectResult = ossClient.putObject(clientConfig.getBucketName(), imageName, new ByteArrayInputStream(images));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("imageName:{},upload aliyun etag:{}", imageName, objectResult.getETag());
        }
        ossClient.shutdown();
        return true;
    }

    public static boolean uploadToAliyun(AliYunOOSClientConfig clientConfig, byte[] images, String imageName) {
        return uploadToAliyun(clientConfig, images, imageName, null);
    }

    public static boolean uploadToAliyun(AliYunOOSClientConfig clientConfig, InputStream inputStream, String imageName, ObjectMetadata objectMetadata) {
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录
        // https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = clientConfig.getAccessKeyId();
        String accessKeySecret = clientConfig.getAccessKeySecret();
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(clientConfig.getEndpoint(), accessKeyId, accessKeySecret);
        PutObjectResult objectResult = null;
        if (objectMetadata != null) {
            Map<String, String> userMetadata = objectMetadata.getUserMetadata();
            if (userMetadata == null) {
                userMetadata = new HashMap<>();
            }
            userMetadata.put("createTime", DateUtils.getCurrentDateTime());
            objectMetadata.setUserMetadata(userMetadata);
            objectResult = ossClient.putObject(clientConfig.getBucketName(), imageName, inputStream, objectMetadata);
        } else {
            objectResult = ossClient.putObject(clientConfig.getBucketName(), imageName, inputStream);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("imageName:{},upload aliyun etag:{}", imageName, objectResult.getETag());
        }
        ossClient.shutdown();
        return true;
    }

    public static boolean uploadToAliyun(AliYunOOSClientConfig clientConfig, InputStream inputStream, String imageName) {
        return uploadToAliyun(clientConfig, inputStream, imageName, null);
    }

    public static byte[] downloadImage(AliYunOOSClientConfig clientConfig, String imageName) {
        // 创建OSSClient实例。
        String accessKeyId = clientConfig.getAccessKeyId();
        String accessKeySecret = clientConfig.getAccessKeySecret();
        OSSClient ossClient = new OSSClient(clientConfig.getEndpoint(), accessKeyId, accessKeySecret);
        try {
            OSSObject ossObject = ossClient.getObject(clientConfig.getBucketName(), imageName);
            return ByteInputStreamUtils.getInputStreamByte(ossObject.getObjectContent());
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return null;
    }

    public static Map<String, Object> downloadImageObject(AliYunOOSClientConfig clientConfig, String imageName) {
        // 创建OSSClient实例。
        String accessKeyId = clientConfig.getAccessKeyId();
        String accessKeySecret = clientConfig.getAccessKeySecret();
        OSSClient ossClient = new OSSClient(clientConfig.getEndpoint(), accessKeyId, accessKeySecret);
        try {
            OSSObject ossObject = ossClient.getObject(clientConfig.getBucketName(), imageName);
            if (ossObject != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("imageData", ByteInputStreamUtils.getInputStreamByte(ossObject.getObjectContent()));
                if (ossObject.getObjectMetadata() != null) {
                    map.put("contentType", ossObject.getObjectMetadata().getContentType());
                    map.put("contentLength", ossObject.getObjectMetadata().getContentLength());
                    map.put("contentEncoding", ossObject.getObjectMetadata().getContentEncoding());
                    map.put("simpleImageName", ossObject.getObjectMetadata().getUserMetadata().get("simpleImageName"));
                }
                return map;
            }
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return null;
    }

    public static ObjectMetadata createObjectMetadata(String contentType, Long contentLength, String contentEncoding) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentEncoding(contentEncoding);
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType);
        return metadata;
    }

    public static ObjectMetadata createObjectMetadata(String contentType, Long contentLength, String contentEncoding, Map<String, String> userMetadata) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentEncoding(contentEncoding);
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType);
        metadata.setUserMetadata(userMetadata);
        return metadata;
    }

    public static String getBucketName(String bucket, String imageName) {
        Date date = new Date();
        return bucket + "/" + DateUtil.getYear(date) + "/" + DateUtil.getMonth(date) + "/" + DateUtil.getDay(date) + "/" + imageName;
    }

    public static String getIdcardBucketName(String imageName) {
        Date date = new Date();
        return "face/" + DateUtil.getYear(date) + "/" + DateUtil.getMonth(date) + "/" + DateUtil.getDay(date) + "/" + imageName;
    }

    public static void main(String[] args) throws IOException {
        AliYunOOSClientConfig clientConfig = new AliYunOOSClientConfig();
        clientConfig.setAccessKeyId("LTAIAMjrKUc1P4PU");
        clientConfig.setAccessKeySecret("f4trSaFwDOBJwufMtWk4iC7IivM3Xq");
        clientConfig.setBucketName("use-images");
        clientConfig.setEndpoint("oss-cn-shenzhen.aliyuncs.com");
        Map<String, Object> map = AliyunOOSUtils.downloadImageObject(clientConfig, "banner1.jpg");
        System.out.println(map.get("contentType"));
        System.out.println(map.get("contentLength"));
        System.out.println(map.get("contentEncoding"));
        FileOutputStream fileOutputStream = new FileOutputStream(new File("e://banner1.jpg"));
        fileOutputStream.write((byte[]) map.get("imageData"));
        fileOutputStream.close();
        System.out.println(map.get("imageData"));
    }

    public void readImage() throws FileNotFoundException {
        File file = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\banner1.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        String imageName = "banner1.jpg";
        AliYunOOSClientConfig clientConfig = new AliYunOOSClientConfig();
        clientConfig.setAccessKeyId("LTAIAMjrKUc1P4PU");
        clientConfig.setAccessKeySecret("f4trSaFwDOBJwufMtWk4iC7IivM3Xq");
        clientConfig.setBucketName("use-images");
        clientConfig.setEndpoint("oss-cn-shenzhen.aliyuncs.com");
        AliyunOOSUtils.uploadToAliyun(clientConfig, inputStream, imageName, createObjectMetadata("application/png", file.length(), "utf-8"));
    }

}
