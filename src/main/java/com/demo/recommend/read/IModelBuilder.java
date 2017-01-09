package com.demo.recommend.read;

import org.apache.mahout.cf.taste.model.DataModel;


public interface IModelBuilder {
    public DataModel buildRecommendModel();
}
