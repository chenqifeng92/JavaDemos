package com.chen.action;

import com.chen.bean.Workers;
import com.chen.dao.WorkersDao;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 不同操作之间的跳转
 */
public class ActionServlet extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        PrintWriter out = response.getWriter();

        //获取请求资源的路径
        String uri = request.getRequestURI();
        //获取请求资源路径中除应用名以外的部分
        String action = uri.substring(uri.lastIndexOf("/")+1,uri.lastIndexOf("."));
        System.out.println("action:" + action);

        WorkersDao dao = new WorkersDao();

        if(action.equals("list")){//员工信息列表
            try{
                List<Workers> wkrs = dao.findAll();
                request.setAttribute("wkrs", wkrs);
                request.getRequestDispatcher("listEmp.jsp").forward(request, response);
            } catch (Exception e){
                e.printStackTrace();
                //编程式处理异常
                request.setAttribute("err_msg","系统出错，请重试");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }else if(action.equals("add")){//增加员工
            String name = request.getParameter("name");
            try{
                double salary = Double.parseDouble(request.getParameter("salary"));
                int age = Integer.parseInt(request.getParameter("age"));

                Workers wkr = new Workers();
                wkr.setName(name);
                wkr.setSalary(salary);
                wkr.setAge(age);

                dao.save(wkr);

                response.sendRedirect("list.do");
            }catch(Exception e){
                e.printStackTrace();
                //将异常抛给容器，在web.xml中添加声明，变成声明式处理异常
                throw new ServletException(e);
            }
        }else if(action.equals("delete")){//删除员工
            int id = Integer.parseInt(request.getParameter("id"));
            try{
                dao.delete(id);
                response.sendRedirect("list.do");
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(action.equals("load")){//更新之前加载员工全部信息
            int id = Integer.parseInt(request.getParameter("id"));
            try{
                Workers wkr = dao.findById(id);
                request.setAttribute("wkr", wkr);
                request.getRequestDispatcher("updateEmp.jsp").forward(request, response);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(action.equals("update")){//更新员工信息
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            double salary = Double.parseDouble(request.getParameter("salary"));
            int age = Integer.parseInt(request.getParameter("age"));

            Workers wkr = new Workers();
            wkr.setName(name);
            wkr.setSalary(salary);
            wkr.setAge(age);
            wkr.setId(id);

            try{
                dao.modify(wkr);
                response.sendRedirect("list.do");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

