package com.biao.fileserver.service;

import java.io.InputStream;

public interface GridFsTemplateService {

    /**
     * 存储图片
     *
     * @param streamToUploadFrom
     * @return
     */
    String uploadFromStream(InputStream streamToUploadFrom);

    /**
     * 存储图片
     *
     * @param streamToUploadFrom
     * @param imageName
     * @return
     */
    String uploadFromStream(InputStream streamToUploadFrom, String imageName);
}
