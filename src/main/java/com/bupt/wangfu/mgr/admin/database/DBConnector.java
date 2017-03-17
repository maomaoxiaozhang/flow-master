package com.bupt.wangfu.mgr.admin.database;

/**
 * @ Created by HanB on 2016/7/12 0012.
 */
import java.sql.*;

public class DBConnector {
//    public static final String url = "jdbc:mysql://localhost:3306/odl-info";
    public static final String url = "jdbc:mysql://localhost:3306/test";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "123456";

    public Connection conn = null;
    public PreparedStatement pst = null;

    public DBConnector(String sql) {
        try {
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

