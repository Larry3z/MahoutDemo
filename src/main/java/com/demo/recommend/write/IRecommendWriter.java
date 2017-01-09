package com.demo.recommend.write;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.recommender.Recommender;

import java.sql.SQLException;


public interface IRecommendWriter {
    public void write(LongPrimitiveIterator userIDs, Recommender recommender, int RECOMMENDER_NUM) throws TasteException, SQLException;
}
