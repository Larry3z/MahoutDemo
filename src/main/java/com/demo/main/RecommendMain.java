package com.demo.main;

import com.demo.recommend.UserBaseCF;
import org.apache.mahout.cf.taste.common.TasteException;


public class RecommendMain {
    public static void main(String[] args) throws TasteException {
        UserBaseCF userBaseCF = new UserBaseCF();
        userBaseCF.runRecommendation(5);
    }
}
