<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.chen.bean.*" %>
<!DOCTYPE html>
<html>
	<head>
		<title>æ´æ°</title>
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
						更新员工：
					</h1>
					<% Workers wkr = (Workers)request.getAttribute("wkr"); %>
					<form action="update.do?id=<%=wkr.getId() %>" method="POST">
						<table cellpadding="0" cellspacing="0" border="0" class="form_table">
							<tr>
								<td valign="middle" align="right">编号：</td>
								<td valign="middle" align="left"><%=wkr.getId() %></td>
							</tr>
							<tr>
								<td valign="middle" align="right">姓名：</td>
								<td valign="middle" align="left">
									<input type="text" class="inputgri" name="name" value="<%=wkr.getName() %>" />
								</td>
							</tr>
							<tr>
								<td valign="middle" align="right">薪水：</td>
								<td valign="middle" align="left">
									<input type="text" class="inputgri" name="salary" value="<%=wkr.getSalary() %>"/>
								</td>
							</tr>
							<tr>
								<td valign="middle" align="right">年龄：</td>
								<td valign="middle" align="left">
									<input type="text" class="inputgri" name="age" value="<%=wkr.getAge() %>"/>
								</td>
							</tr>
						</table>
						<p>
							<input type="submit" class="button" value="修改" />
						</p>
					</form>
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
