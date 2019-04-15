package com.biao.fileserver.service;

import org.springframework.data.mongodb.gridfs.GridFsResource;

public interface ImageService {

    GridFsResource imageView(String imageName);
}
