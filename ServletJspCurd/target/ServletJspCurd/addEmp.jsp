<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<title>addEmp</title>
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
					<p id="whereami"></p>
					<h1>增加员工：</h1>
						<form action="add.do" method="POST">
							<table cellpadding="0" cellspacing="0" border="0" class="form_table">
								<tr>
									<td valign=middle align="right">姓名：</td>
									<td valign=middle align="left"><input type="text" class="inputgri" name="name" /></td>
								</tr>
								<tr>
									<td valign=middle align="right">薪水：</td>
									<td valign="middle" align="left"><input type="text" class="inputgri" name="salary" /></td>
								</tr>
								<tr>
									<td valign="middle" align="right">年龄：</td>
									<td valign="middle" align="left"><input type="text" class="inputgri" name="age" /></td>
								</tr>
						</table>
						<p>
							<input type="submit" class="button" value="增加" />
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
