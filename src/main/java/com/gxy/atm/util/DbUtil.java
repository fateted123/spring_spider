package com.gxy.atm.util;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * 数据库连接工具
 */
public class DbUtil {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("加载数据库驱动成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("加载数据库驱动失败");
        }
    }

    /**
     * 获取连接
     */
    public static Connection getConn() {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBCProUtil.getUrl(), JDBCProUtil.getUser(), JDBCProUtil.getPwd());
            System.out.println("获取连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("获取连接失败");
        }
        return connection;
    }


    /**
     * 接口的好处
     * AutoCloseable... objs  代表是数组，可变参数
     * 参数可有可无，不限制数量
     *
     * @param objs
     */
    public static void closeAll(AutoCloseable... objs) {
        for (AutoCloseable obj : objs) {
            try {
                if (obj != null) {
                    obj.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
