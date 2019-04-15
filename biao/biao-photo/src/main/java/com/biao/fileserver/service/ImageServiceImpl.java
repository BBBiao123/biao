package com.biao.fileserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public GridFsResource imageView(String imageName) {
        return gridFsTemplate.getResource(imageName);
    }
}
