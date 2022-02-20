package com.zhao.blog.controller;

import com.zhao.blog.common.annotations.MyLogger;
import com.zhao.blog.common.enums.StatusCode;
import com.zhao.blog.controller.response.Response;
import com.zhao.blog.utils.QiniuUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author noblegasesgoo
 * @version 0.0.1
 * @date 2022/1/25 21:53
 * @description
 */

@Slf4j
@RestController
@RequestMapping("/upload")
@Api(value = "上传管理", tags = "上传管理")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;

    @MyLogger(module = "上传管理", operation = "上传图片请求")
    @ApiOperation(value = "上传图片请求")
    @PostMapping("/private/image")
    public Response uploadImage(@RequestParam("image") MultipartFile file) {

        /** 获取原始文件名 **/
        String originalFilename = file.getOriginalFilename();

        /** 校验是否后缀文件格式为空 **/
        int index = originalFilename.lastIndexOf(".");
        String suffix = null;
        if (index == -1 || (suffix = originalFilename.substring(index + 1)).isEmpty()) {
            /** 如果文件末尾不是文件格式名称的话出错 **/
            log.info("[goo-blog|UploadController|uploadImage] 文件后缀不能为空！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "文件后缀不能为空！", originalFilename);
        }

        /** 判断是否是合法的后缀 **/
        Set<String> allowSuffix = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif"));
        /** 这里的 suffix 前缀在上一个 if 时做赋值了 **/
        if (!allowSuffix.contains(suffix.toLowerCase())) {
            /** 非法文件格式 **/
            log.info("[goo-blog|UploadController|uploadImage] 非法文件格式！");
            return new Response(StatusCode.STATUS_CODEC400.getCode(), "非法文件格式！", suffix);
        }

        /** 设置独一无二的名称 **/
        String onlyFileName = new StringBuffer()
                .append(UUID.randomUUID().toString())
                .append(".")
                .append(StringUtils.substringAfterLast(originalFilename, ".")).toString();
        boolean upload = qiniuUtils.upload(file, onlyFileName);
        if (!upload) {
            log.info("[goo-blog|UploadController|uploadImage] 图片上传失败！");
            return new Response(StatusCode.STATUS_CODEC500.getCode(), "图片上传失败！", QiniuUtils.URL + "/" + onlyFileName);
        }

        return new Response(StatusCode.STATUS_CODEC200.getCode(), "图片上传成功！", QiniuUtils.URL + "/" + onlyFileName);

    }
}
