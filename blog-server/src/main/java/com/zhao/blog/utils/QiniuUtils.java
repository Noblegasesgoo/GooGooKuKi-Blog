package com.zhao.blog.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/26 11:04
 * @description 七牛云oss存储工具类
 */

@Component
public class QiniuUtils {

    /** oss 对象存储临时 url **/
    public static final String URL = "https://img.googookuki.cn";

    @Value("${qiniu.accessKey}")
    private  String accessKey;

    @Value("${qiniu.accessSecretKey}")
    private  String accessSecretKey;

    public  boolean upload(MultipartFile file,String fileName){

        /** 构造一个带指定 Region 对象的配置类 **/
        Configuration cfg = new Configuration(Region.huanan());
        /** ...其他参数参考类注释 **/
        UploadManager uploadManager = new UploadManager(cfg);
        /** ...生成上传凭证，然后准备上传 **/
        String bucket = "googookuki-blog";
        /** 默认不指定key的情况下，以文件内容的hash值作为文件名 **/
        try {

            /** 获取对象字节流 **/
            byte[] uploadBytes = file.getBytes();

            /** 验证权限 **/
            Auth auth = Auth.create(accessKey, accessSecretKey);

            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(uploadBytes, fileName, upToken);

            /** 解析上传成功的结果 **/
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);

            return true;
        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return false;
    }

}
