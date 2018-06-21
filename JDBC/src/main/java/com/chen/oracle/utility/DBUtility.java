package com.chen.oracle.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtility{
    //初始化连接数据
    private static Properties properties = new Properties();
    private static String driver = null;
    private static String url = null;
    private static String user = null;
    private static String pwd = null;

    static{
        try{
            //加载配置文件
            properties.load(DBUtility.class.getClassLoader().getResourceAsStream(
                    "com/huawei/oracle/utility/db.properties"));
            driver = properties.getProperty("jdbc.driver");
            url = properties.getProperty("jdbc.url");
            user = properties.getProperty("jdbc.user");
            pwd = properties.getProperty("jdbc.password");
            //加载数据库驱动
            Class.forName(driver);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static Connection openConnection() throws SQLException{
        return DriverManager.getConnection(url,user,pwd);
    }
    public static void closeConnection(Connection con){
        if(con!=null){
            try{
                con.close();
            }catch(SQLException e){
                System.out.println("关闭连接时发生异常");
            }
        }
    }
}
