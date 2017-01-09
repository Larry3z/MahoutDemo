package com.demo.practice;


import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class RecommandItem {

    // 设定输入路径
    private static String filePath = "output/result.txt";

    public static void write(LongPrimitiveIterator userIDs, Recommender recommender, int RECOMMENDER_NUM) throws TasteException, IOException {

        String itemList = "";
        // 判断输出文件是否存在
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();// 不存在创建
        } else {
            file.delete();// 存在删除
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        while (userIDs.hasNext()) {
            itemList = "";
            long uid = userIDs.nextLong();
            List<RecommendedItem> list = recommender.recommend(uid, RECOMMENDER_NUM);

            for (RecommendedItem ritem : list) {
                itemList += String.valueOf(ritem.getItemID()) + ",";
            }
            if (itemList != "" && itemList != null) {
                itemList = itemList.substring(0, itemList.length() - 1);
            }

            writer.write("向用户:" + String.valueOf(uid) + "\t" + "推荐" +itemList + "\r\n");
            System.out.println("向用户:" + String.valueOf(uid) + "\t" + "推荐" +itemList + "\r\n");
        }
        writer.close();
    }


}
