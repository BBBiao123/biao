package com.biao.reactive.data.mongo.service;

import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.InputStream;
import java.util.List;

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

    /**
     * 根据正则批量查询图片
     *
     * @param imageNameRegx
     * @return
     */
    List<String> selectPhotos(String imageNameRegx);

    /**
     * 批量删除图片
     *
     * @param imageNames
     */
    void clearPhotosByIds(List<String> imageNames);

    /**
     * 查询图片
     *
     * @param imageName
     * @return
     */
    GridFsResource imageView(String imageName);


}
