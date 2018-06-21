package com.chen.oracle.statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmpDAO {
    public static void main(String[] args){
        //1.SELECT
        EmpDAO dao = new EmpDAO();
        dao.findAll();

        //2.INSERT
        //Emp emp = new Emp(1001,"rose","Analyst",7901,"2014-05-01",4500.00,500.00,10);
        //dao.add(emp);

        //3.UPDATE
        //emp.setSal(4500.00);
        //dao.update(emp);
    }
    public void findAll(){
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            con = ConnectionSource.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("select empno,ename,sal,hiredate from emp");
            while(rs.next()){
                System.out.println(rs.getInt("empno")+","+rs.getString("ename")+","+rs.getDouble("sal")+","
                        +rs.getDate("hiredate"));
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
                System.out.println("释放资源发生异常！");
            }
        }
    }

    public void add(Emp emp){
        Connection con = null;
        Statement stmt = null;
        int flag = -1;
        String sql = "insert into emp(empno,ename,job,mgr,hiredate,sal,comm,deptno)values("
                +emp.getEmpNo()+","+"'"
                +emp.getEname()+","+"'"
                +emp.getJob()+","+"'"
                +emp.getMgr()+","+"'"
                +"to date('"+emp.getHiredate()+"','yyyy-mm-dd'),"
                +emp.getSal()+","
                +emp.getComm()+","
                +emp.getDeptno()+")";
        try{
            con = ConnectionSource.getConnection();
            stmt = con.createStatement();

            flag = stmt.executeUpdate(sql);
            if(flag > 0){
                System.out.println("新增记录成功！");
            }
        }catch(SQLException e){
            System.out.println("数据库访问异常！");
            throw new RuntimeException(e);
        }finally{
            try{
                if(stmt!=null){
                    stmt.close();
                }
                if(con!=null){
                    con.close();
                }
            }catch(SQLException e){
                System.out.println("释放资源异常");
            }
        }
    }

    public void update(Emp emp){
        Connection con = null;
        Statement stmt = null;
        int flag = -1;
        String sql = "update emp set sal = "+emp.getSal()+","+"comm = "+emp.getComm()+"where empno = "+emp.getEmpNo();
        try{
            con = ConnectionSource.getConnection();
            stmt = con.createStatement();

            flag = stmt.executeUpdate(sql);
            if(flag>0){
                System.out.println("更新记录成功！");
            }
        }catch(SQLException e){
            System.out.println("数据库访问异常");
            throw new RuntimeException(e);
        }finally{
            try{
                if(stmt!=null){
                    stmt.close();
                }
                if(con!=null){
                    con.close();
                }
            }catch(SQLException e){
                System.out.println("释放资源异常");
            }
        }
    }
}

