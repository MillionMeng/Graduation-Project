package com.arvin.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * create by Arvin Meng
 * Date: 2019/4/17.
 */
public interface IFileService {

    public String upload(MultipartFile file, String path);
}
