package com.arvin.service.Impl;

import com.arvin.service.IFileService;
import com.arvin.util.FTPUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * create by Arvin Meng
 * Date: 2019/4/17.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService{

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path){
        //获取原始文件名
        String fileName = file.getOriginalFilename();
        //获取扩展名  +1(不要.)
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        //修改文件名
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);

        try {
            //文件上传到本地服务器
            file.transferTo(targetFile);

            //上传到ftp服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            //上传完后，删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件上传异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
