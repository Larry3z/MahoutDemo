package com.demo.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class GeneratorData {
    public static void main(String[] args) {
        MysqlUtils utils = new MysqlUtils();
        //String sql = "insert into t_account(account,nickname) values(?,?)";
        String sql = "delete from t_account where account=? and nickname=?";
        String tmp = "test";
        List list = new ArrayList();
        for (int i = 2; i < 100; i++) {
            String[] arr = new String[2];
            String t = tmp + i;
            arr[0] = t;
            arr[1] = t;
            list.add(arr);
        }
        try {
            utils.executeBatch(sql, list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
