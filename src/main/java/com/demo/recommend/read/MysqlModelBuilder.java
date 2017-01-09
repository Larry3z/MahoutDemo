package com.demo.recommend.read;

import com.demo.utils.MysqlUtils;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;


public class MysqlModelBuilder implements IModelBuilder {

    private static MysqlDataSource dataSource = null;

    static {
        dataSource = MysqlUtils.buildDataSource();
    }

    private String preferenceTable;
    private String userIDColumn;
    private String itemIDColumn;
    private String preferenceColumn;
    private String timestampColumn;

    public MysqlModelBuilder(String preferenceTable, String userIDColumn, String itemIDColumn,
                             String preferenceColumn, String timestampColumn) {
        this.preferenceTable = preferenceTable;
        this.userIDColumn = userIDColumn;
        this.itemIDColumn = itemIDColumn;
        this.preferenceColumn = preferenceColumn;
        this.timestampColumn = timestampColumn;
    }

    @Override
    public DataModel buildRecommendModel() {
        DataModel model = new MySQLJDBCDataModel(dataSource, preferenceTable, userIDColumn, itemIDColumn,
                preferenceColumn, timestampColumn);
        return model;
    }


}
