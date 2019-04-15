package com.biao.reactive.data.mongo.service.impl;

import com.biao.reactive.data.mongo.service.GridFsTemplateService;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

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
        document.append("ContentType", "application/png");
        ObjectId fileId = gridFsTemplate.store(streamToUploadFrom, imageName, document);
        return fileId.toString();
    }

    public void clearPhotosByIds(List<String> imageNames) {
        for (String imageName : imageNames) {
            gridFsTemplate.delete(new Query(new GridFsCriteria("filename").is(imageName)));
        }
    }

    public List<String> selectPhotos(String imageNameRegx) {
        List<String> imageNames = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(new GridFsCriteria("filename").regex(imageNameRegx));
        GridFSFindIterable gridFsResources = gridFsTemplate.find(query);
        if (gridFsResources != null) {
            gridFsResources.skip(0).limit(5000);
            gridFsResources.forEach(new Consumer<GridFSFile>() {
                @Override
                public void accept(GridFSFile gridFsResource) {
                    if (gridFsResource != null) {
                        imageNames.add(gridFsResource.getFilename());
                    }
                }
            });
        }
        return imageNames;
    }

    public GridFsResource imageView(String imageName) {
        return gridFsTemplate.getResource(imageName);
    }
}
