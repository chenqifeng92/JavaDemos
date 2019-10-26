package com.chen.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 提供打开和关闭数据库连接的工具类
 *
 */
public class DBUtil {
    public static Connection getConnection() throws Exception{
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection("jdbc:mysql://45.32.49.15:3306/test","root","Tokyo#190908");
            //conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.199.114:1521:ORCL","C##oracle_user","Orcl_12c");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return conn;
    }
    public static void close(Connection conn) throws Exception{
        if(conn != null){
            try {
                conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}

