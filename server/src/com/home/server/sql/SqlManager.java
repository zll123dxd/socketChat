package com.home.server.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.home.server.util.LogUtil;

public class SqlManager {
    private static SqlManager mSqlManager;
    private Connection connection = null;
    private Statement statement = null;

    public SqlManager() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost/ACCOUNT";
        String user = "debian-sys-maint";
        String password = "E8MWBQwLvVhC5bqZ";
        try {
            Class.forName(driver);
            connection = (Connection) DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SqlManager getInstance() {
        if (mSqlManager == null) {
            mSqlManager = new SqlManager();
        }
        return mSqlManager;
    }

    public void runTest() {
        Statement statement = null;
        ResultSet resultSet = null;
        try {

            String sql = "insert into account (user,password) values ('c4a1b2', 'woxingding123')";
            statement = connection.createStatement();
            int result = statement.executeUpdate(sql);
            if (result > 0) {
                printLog("insert successful", LogUtil.INFO);
            } else {
                printLog("insert fail", LogUtil.INFO);
            }

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public ResultSet select(String sql) {
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public int update(String sql) {
        int result = 0;
        try {
            statement = connection.createStatement();
            result = statement.executeUpdate(sql);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            mSqlManager = null;
            connection = null;
            statement = null;
        } catch (Exception e) {

        }
    }

    private static void printLog(String str, int level) {
        LogUtil.printLog("SqlManager : " + str, level);
    }
}

