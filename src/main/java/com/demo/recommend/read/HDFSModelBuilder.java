package com.demo.recommend.read;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

import java.io.File;
import java.io.IOException;


public class HDFSModelBuilder implements IModelBuilder {

    private String fileHdfsPath;

    public HDFSModelBuilder(String fileHdfsPath) {
        this.fileHdfsPath = fileHdfsPath;
    }

    @Override
    public DataModel buildRecommendModel() {
        DataModel model = null;
        try {
            model = new FileDataModel(new File(fileHdfsPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }
}
