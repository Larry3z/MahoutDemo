package com.demo.practice;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;


public class JuranDemo {
    final static int RECOMMENDER_NUM = 2;

    public static void main(String[] args) throws IOException,TasteException {

        // 设定文件路径
        String file = "input/tt2.txt";
        // 从文件加载数据
        DataModel model = new FileDataModel(new File(file));
        // 指定用户相似度计算方法，这里采用欧几里德距离
        UserSimilarity user = new EuclideanDistanceSimilarity(model);
        // 指定用户邻居数量
        UserNeighborhood neighbor = new NearestNUserNeighborhood(10,user,model);
        // 构建基于用户的推荐系统
        Recommender recommender = new GenericUserBasedRecommender(model, neighbor, user);

        LongPrimitiveIterator userIDs = model.getUserIDs();
        RecommandItem.write(userIDs, recommender, RECOMMENDER_NUM);

    }
}
