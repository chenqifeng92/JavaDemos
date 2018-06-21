package com.chen.oracle.uservalidate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    public static void main(String[] args){
        //login
        UserDAO dao = new UserDAO();
        //dao.login1("huawei","huawei01");
        //dao.login1("huitong","huitong01");
        dao.login("huawei","a' OR 'b'='b");
    }

    public void login1(String username,String password){
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = null;

        try{
            sql = "select * from users where username = '" + username + "' and password = '" + password+"'";
            System.out.println(sql);
            con = ConnectionSource.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            if(rs.next()){
                System.out.println("登录成功！");
            }else{
                System.out.println("登录失败！");
            }
        }catch(SQLException e){
            System.out.println("数据库访问异常！");
            throw new RuntimeException(e);
        }finally{
            try{
                if(rs!=null){
                    rs.close();
                }
                if(stmt!=null){
                    stmt.close();
                }
                if(con!=null){
                    con.close();
                }
            }catch(SQLException e){
                System.out.println("释放资源发生异常");
            }
        }
    }

    public void login(String username,String password){
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = null;

        try{
            sql = "select * from users where username = ? and password = ?";
            con = ConnectionSource.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2,password);
            rs = stmt.executeQuery();

            if(rs.next()){
                System.out.println("登录成功！");
            }else{
                System.out.println("登录失败！");
            }
        }catch(SQLException e){
            System.out.println("数据库访问异常！");
            throw new RuntimeException(e);
        }finally{
            try{
                if(rs!=null){
                    rs.close();
                }
                if(stmt!=null){
                    stmt.close();
                }
                if(con!=null){
                    con.close();
                }
            }catch(SQLException e){
                System.out.println("释放资源发生异常");
            }
        }
    }
}

