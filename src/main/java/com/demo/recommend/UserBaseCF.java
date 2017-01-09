package com.demo.recommend;

import com.demo.recommend.read.MysqlModelBuilder;
import com.demo.recommend.write.MysqlRecommendWriter;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.sql.SQLException;

/**
 * 基于用户的协同过滤

 */
public class UserBaseCF {
    public void runRecommendation(int recommender_num) {
        try {
            //String file = "";
            //DataModel model = new HDFSModelBuilder(file).buildRecommendModel();
            String preferenceTable = "t_comment";
            String userIDColumn = "accountID";
            String itemIDColumn = "productID";
            String preferenceColumn = "star";
            String timestampColumn = "";
            DataModel model = new MysqlModelBuilder(preferenceTable, userIDColumn, itemIDColumn, preferenceColumn, timestampColumn)
                    .buildRecommendModel();
            UserSimilarity user = new EuclideanDistanceSimilarity(model);
            //NearestNUserNeighborhood neighbor = new NearestNUserNeighborhood(5, user, model);
            ThresholdUserNeighborhood neighbor = new ThresholdUserNeighborhood(0.5, user, model);
            Recommender recommender = new GenericUserBasedRecommender(model, neighbor, user);
            LongPrimitiveIterator userIDs = model.getUserIDs();
            String sql = "insert into recommend(user_id,item_ids) values(?,?);";
            new MysqlRecommendWriter(sql).write(userIDs, recommender, recommender_num);
        } catch (TasteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
