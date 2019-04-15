package com.biao.fileserver.service;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service("gridFsTemplateServiceImpl")
public class GridFsTemplateServiceImpl implements GridFsTemplateService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public String uploadFromStream(InputStream streamToUploadFrom) {
        return uploadFromStream(streamToUploadFrom, UUID.randomUUID().toString().replaceAll("-", ""));
    }

    @Override
    public String uploadFromStream(InputStream streamToUploadFrom, String imageName) {
        Document document = new Document("type", "presentation");
        String contentType = "png";
        int index = imageName.indexOf(".");
        if (index != -1) {
            contentType = imageName.substring(index + 1);
        }
        document.append("ContentType", "application/" + contentType);
        ObjectId fileId = gridFsTemplate.store(streamToUploadFrom, imageName, document);
        return fileId.toString();
    }
}
