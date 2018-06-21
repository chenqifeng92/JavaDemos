<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.java.bean.*,java.util.*" %>
<!DOCTYPE html>
<html>
	<head>
		<title>listEmp</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="css/style.css" />
		<script type="text/javascript" src="js/workers.js"></script>
	</head>
	<body>
		<div id="wrap">
			<div id="top_content"> 
				<div id="header">
					<div id="rightheader">
						<p id="time">
							<br />
						</p>
					</div>
					<div id="topheader">
						<h1 id="title">
							<a href="#">JspCurdMysql</a>
						</h1>
					</div>
					<div id="navigation">
					</div>
				</div>
				<div id="content">
					<p id="whereami">
					</p>
					<h1>
						员工信息
					</h1>
					<table class="table">
						<tr class="table_header">
							<td>编号</td>
							<td>姓名</td>
							<td>薪水</td>
							<td>年龄</td>
							<td>操作</td>
						</tr>
							<%
								List<Workers> wkrs = (List<Workers>) request.getAttribute("wkrs");
								for(int i=0;i<wkrs.size();i++){
									Workers wkr = wkrs.get(i);
							 %>
						<tr class="row<%=i%2+1 %>">
							<td><%=wkr.getId() %></td>
							<td><%=wkr.getName() %></td>
							<td><%=wkr.getSalary() %></td>
							<td><%=wkr.getAge() %></td>
							<td>
								<a href="delete.do?id=<%=wkr.getId()%>" 
								onclick="return confirm('是否确认删除  <%=wkr.getName()%> 的信息？');">删除</a>&nbsp;
								<a href="load.do?id=<%=wkr.getId()%>">修改</a>
							</td>
						</tr>
						<%
							}
						 %>
					</table>
					<p>
						<input type="button" class="button" value="增加" onclick="location='addEmp.jsp'"/>
					</p>
				</div>
			</div>
			<div id="footer">
				<div id="footer_bg">
				ABC@126.com
				</div>
			</div>
		</div>
	</body>
</html>
