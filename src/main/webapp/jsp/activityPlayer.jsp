<!DOCTYPE HTML>
<%@page import="com.bear.util.ServletUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.bear.connection.*"%>
<%@page import="java.util.Map.Entry"%>
<%@page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
</head>
<body>
	<h1>查询</h1>
	<form action="../ActivityCount" method="post">
		<p>开始日期(2014-01-01 00:00:00): <input type="text" name="beginTime"/></p>
		<p>结束日期(2014-01-03 00:00:00): <input type="text" name="endTime"/></p>
		<input type="submit" value="查询" />
	</form>
</body>
</html>
