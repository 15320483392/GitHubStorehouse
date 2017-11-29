package com.jdbc;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * JDBC链接数据库工具包
 * Created by Administrator on 2017/8/18.
 */
public class JDBCUtils {
    private static String url;
    private static String username;
    private static String password;
    private static String driver_class;

    static {
        Properties pro = new Properties();
        try {
            pro.load(JDBCUtils.class.getClassLoader().getResourceAsStream(
                    "config.properties"));
            driver_class = pro.getProperty("jdbc.driver_class");
            url = pro.getProperty("jdbc.url");
            username = pro.getProperty("jdbc.username");
            password = pro.getProperty("jdbc.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定义获取连接对象的方法
     */
    private static Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName(driver_class);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 定义释放资源的方法
     */
    private static void closeObjects(Object... parameters) {
        if (parameters != null && parameters.length > 0) {
            for (Object obj : parameters) {
                try {
                    if (obj instanceof PreparedStatement) {
                        ((PreparedStatement) obj).close();
                    }
                    if (obj instanceof Connection) {
                        Connection conn = ((Connection) obj);
                        if (!conn.isClosed()) {
                            conn.close();
                            conn = null;
                        }
                    }
                    if (obj instanceof ResultSet) {
                        ((ResultSet) obj).close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 定义执行简单SQL的增，删，改指令的方法
     *
     * @param sql
     *            接收调用传入的SQL指令
     * @param parameters
     *            调用出传入与SQl指令占位符一一对应的值所组成的数组对象
     */
    public static int executeUpdate(String sql, Object... parameters) {
        int row = 0;
        Connection conn = getConnection();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            // 设置参数
            if (parameters != null && parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    pst.setObject(i + 1, parameters[i]);
                }
            }
            row = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放操作资源的方法
            closeObjects(pst, conn);
        }

        return row;
    }

    /**
     * 定义简单SQL查询的方法
     *
     * @param sql
     *            接收调用传入的SQL指令
     * @param parameters
     *            调用出传入与SQl指令占位符一一对应的值所组成的数组对象
     */
    public static List<Map<String, Object>> executeQuery(String sql,
                                                         Object... parameters) {
        List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = conn.prepareStatement(sql);
            // 设置参数
            if (parameters != null && parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    pst.setObject(i + 1, parameters[i]);
                }
            }
            // 执行SQL指令并处理返回结果
            rs = pst.executeQuery();
            if (rs != null) {
                // 把获取的结果集转换为一张虚拟的表
                ResultSetMetaData rsd = rs.getMetaData();
                // 获取当前表中的列数
                int columnCount = rsd.getColumnCount();
                while (rs.next()) { // 循环行
                    // 定义一个Map集合对象存储当前遍历行
                    Map<String, Object> row = new HashMap<String, Object>();
                    // 循环获取每列的值并存储在row集合中
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(rsd.getColumnName(i),
                                rs.getObject(rsd.getColumnName(i)));
                    }
                    // 把每次构建的新行添加到表中
                    table.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeObjects(rs, pst, conn);
        }
        return table;
    }

    /**
     * public static void main(String[] args) { Properties pro = new
     * Properties(); try {
     * pro.load(JDBCUtils.class.getClassLoader().getResourceAsStream(
     * "jdbc.properties")); System.out.println(pro.get("jdbc.url")); } catch
     * (IOException e) { e.printStackTrace(); } }
     */
    public static void main(String[] args) {
        print("name");
    }

    public static void print(String name, Object... objects) {
        if (objects != null && objects.length > 0) {
            for (Object obj : objects) {
                System.out.println(obj);
            }
        }
        System.out.println(objects.length);
    }
}
