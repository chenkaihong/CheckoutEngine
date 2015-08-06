<!DOCTYPE HTML>
<%@page import="com.bear.servlet.activityEffect.ReturnData"%>
<%@page import="com.bear.util.ServletUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.bear.connection.*"%>
<%@page import="java.util.Map.Entry"%>
<%@page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<meta HTTP-EQUIV="Expires" CONTENT="0"> 
<link href="<%=request.getContextPath() + "/css/bootstrap.min.css"%>" rel="stylesheet">
<link href="<%=request.getContextPath() + "/css/bootstrap-datetimepicker.min.css"%>" rel="stylesheet">
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-8 col-sm-offset-2">
				<h1>
					<b>新增激活统计</b>
				</h1>
				<hr>
				<form action="<%=request.getContextPath() + "/ActivationCount"%>" method="post">
					<p>
						周期: 
						<input class="Time" type="text" name="startTime" readonly="readonly" value="" /> 
						- 
						<input class="Time" type="text" name="endTime" readonly="readonly" value="" />
						<button type="submit" class="btn btn-primary btn-sm">查询</button>
					</p>
				</form>
			</div>
		</div>
	</div>

	
	<script src="<%=request.getContextPath() + "/js/jquery-2.1.1.js"%>"></script>
	<script src="<%=request.getContextPath() + "/js/bootstrap.min.js"%>"></script>
	<script src="<%=request.getContextPath() + "/js/bootstrap-datetimepicker.min.js"%>"></script>
	<script src="<%=request.getContextPath() + "/js/bootstrap-datetimepicker.zh-CN.js"%>"></script>
	<script src="<%=request.getContextPath() + "/js/bootstrap-wysiwyg.js"%>"></script>
	<script type="text/javascript">
		$('.Time').datetimepicker({
		    format: 'yyyy-mm',
		    language: 'zh-CN'
		});
	</script>
</body>
</html>
