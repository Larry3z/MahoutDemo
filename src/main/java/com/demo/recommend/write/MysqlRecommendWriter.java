package com.demo.recommend.write;

import com.demo.utils.MysqlUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MysqlRecommendWriter implements IRecommendWriter {
    private String sql = "";

    public MysqlRecommendWriter(String sql) {
        this.sql = sql;
    }

    @Override
    public void write(LongPrimitiveIterator userIDs, Recommender recommender, int RECOMMENDER_NUM)
            throws TasteException, SQLException {
        List<Object[]> resultList = new ArrayList<Object[]>();
        while (userIDs.hasNext()) {
            StringBuffer itemBuffer = new StringBuffer();
            long uid = userIDs.nextLong();
            List<RecommendedItem> list = recommender.recommend(uid, RECOMMENDER_NUM);

            for (RecommendedItem item : list) {
                itemBuffer.append(item.getItemID());
                itemBuffer.append(",");
            }
            if(!list.isEmpty()){
                itemBuffer.deleteCharAt(itemBuffer.length()-1);
            }
            List<Object> params = new ArrayList<Object>();
            params.add(uid);
            params.add(itemBuffer.toString());
            Object[] par = params.toArray();
            resultList.add(par);
        }
        MysqlUtils.executeBatch(sql, resultList);
    }
}
