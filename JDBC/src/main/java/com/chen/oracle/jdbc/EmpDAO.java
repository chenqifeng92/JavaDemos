package com.chen.oracle.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmpDAO {
    public static void main(String[] args){
        EmpDAO dao = new EmpDAO();
        dao.findAll();
    }
    public void findAll(){
        //初始化连接
        Connection con = null;
        //初始化语句对象
        Statement stmt = null;
        //初始化结果集
        ResultSet rs = null;

        try{
            //加载Oracle驱动
            Class.forName("oracle.jdbc.OracleDriver");
            //配置Oracle数据库连接的端口号账号密码，建立连接
            con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.199.114:1521:ORCL","C##oracle_user","Orcl_12c");
            //创建语句对象，执行SQL语句
            stmt = con.createStatement();
            rs = stmt.executeQuery("select id,name,salary,age from workers");
            //循环打印查询结果到console
            while(rs.next()){
                System.out.println(rs.getInt("id")+","+rs.getString("name")+","+rs.getDouble("salary")+","
                        +rs.getInt("age"));
            }
        }catch(SQLException e){
            System.out.println("数据库访问异常");
        }catch(ClassNotFoundException e) {
            System.out.println("驱动类无法找到");
            e.printStackTrace();
        }finally{
            //释放资源
            try{
                if(stmt!=null){
                    stmt.close();
                }
                if(con!=null){
                    con.close();
                }
            }catch(SQLException e){
                System.out.println("关闭连接发生异常");
            }
        }
    }
}
