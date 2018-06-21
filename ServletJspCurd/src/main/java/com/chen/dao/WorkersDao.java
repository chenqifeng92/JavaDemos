package com.chen.dao;

import com.chen.bean.Workers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class WorkersDao {
    /**
     * 查询所有员工
     */
    public List<Workers> findAll() throws Exception{
        List<Workers> wkrs = new ArrayList<Workers>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement("SELECT id,name,salary,age FROM workers");
            rs = stmt.executeQuery();
            while(rs.next()){
                Workers wkr = new Workers(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("salary"),
                        rs.getInt("age")
                );
                wkrs.add(wkr);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            DBUtil.close(conn);
        }
        return wkrs;
    }

    /**
     * 删除员工信息
     */
    public void delete(int id) throws Exception{
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement("DELETE FROM workers WHERE id=?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            DBUtil.close(conn);
        }
    }

    /**
     * 增加员工信息
     */
    public void save(Workers wkr) throws Exception{
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement("INSERT INTO workers (name,salary,age) VALUES(?,?,?)");
            stmt.setString(1,wkr.getName());
            stmt.setDouble(2,wkr.getSalary());
            stmt.setInt(3,wkr.getAge());
            stmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            DBUtil.close(conn);
        }
    }

    /**
     * 根据id查询员工信息
     */
    public Workers findById(int id) throws Exception{
        Workers wkr = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM workers WHERE id=?");
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.next()){
                wkr = new Workers(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("salary"),
                        rs.getInt("age")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            DBUtil.close(conn);
        }
        return wkr;
    }

    /**
     * 保存修改的员工信息
     */
    public void modify(Workers wkr) throws Exception{
        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement("UPDATE workers SET name=?,salary=?,age=? WHERE id=?");
            stmt.setString(1,wkr.getName());
            stmt.setDouble(2,wkr.getSalary());
            stmt.setInt(3,wkr.getAge());
            stmt.setInt(4, wkr.getId());
            stmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            DBUtil.close(conn);
        }
    }
}

