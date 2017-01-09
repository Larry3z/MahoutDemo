package com.demo.utils;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * 其实最好还是把连接池给加上，更好的是直接换成框架，先这么样吧

 */
public class MysqlUtils {
    private static final String jdbcDriver = PropertiesUtils.getPropertyByName("jdbc.driver");
    private static final String jdbcUrl = PropertiesUtils.getPropertyByName("jdbc.url");
    private static final String userName = PropertiesUtils.getPropertyByName("jdbc.username");
    private static final String password = PropertiesUtils.getPropertyByName("jdbc.password");
    private static final Logger log = LoggerFactory.getLogger(MysqlUtils.class);

    static {
        try {
            Class.forName(jdbcDriver);
        } catch (ClassNotFoundException e) {
            log.error("加载Mysql数据库驱动失败！");
        }
    }

    public static void quietClose(ResultSet closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (SQLException sqle) {
                log.warn("Unexpected exception while closing; continuing", sqle);
            }
        }
    }

    public static void quietClose(Statement closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (SQLException sqle) {
                log.warn("Unexpected exception while closing; continuing", sqle);
            }
        }
    }

    public static void quietClose(Connection closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (SQLException sqle) {
                log.warn("Unexpected exception while closing; continuing", sqle);
            }
        }
    }

    public static void quietClose(ResultSet resultSet, Statement statement, Connection connection) {
        quietClose(resultSet);
        quietClose(statement);
        quietClose(connection);
    }

    public static void quietClose(Statement statement, Connection connection) {
        quietClose(statement);
        quietClose(connection);
    }

    public static MysqlDataSource buildDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(jdbcUrl);
        dataSource.setUser(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    public static Connection getConnection(boolean autoCommit) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            if (!autoCommit) {
                connection.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            log.error("获取数据库连接失败！");
        }
        return connection;
    }

    /**
     * 执行一条增删改
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static int executeOne(String sql, Object[] params) throws SQLException {
        Connection conn = getConnection(false);
        PreparedStatement stmt = null;
        int result = -1;
        try {
            stmt = createPreparedStatement(conn, sql, params);
            result = stmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            quietClose(stmt, conn);
        }
        return result;
    }

    /**
     * 批量执行增删改
     *
     * @param sql
     * @param paramList
     * @return
     * @throws SQLException
     */
    public static int executeBatch(String sql, List<Object[]> paramList) throws SQLException {
        int result = 0;
        Connection conn = getConnection(false);//设置autoCommit为false
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < paramList.size(); i++) {
                Object[] param = paramList.get(i);//获取一条sql的参数数组
                for (int j = 0; j < param.length; j++)
                    stmt.setObject(j + 1, param[j]);
                stmt.addBatch();
                if (i % 1000 == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            stmt.executeBatch();
            conn.commit();
            result = paramList.size();//先这么样吧，这样只要是有一条失败都回滚
        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            quietClose(stmt, conn);
        }
        return result;
    }

    public static PreparedStatement createPreparedStatement(Connection conn, String sql, Object[] params) throws
            SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++)
            stmt.setObject(i + 1, params[i]);
        return stmt;
    }
}
