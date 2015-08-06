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
					<b>活动效果统计</b>
					<small>用于对比观察活动周期内活跃用户充值信息</small>
				</h1>
				<hr>
				<form action="<%=request.getContextPath() + "/activityEffect"%>" method="post">
					<%
						String beginTimeAInput = (String)request.getAttribute("beginTimeAInput");
						String endTimeAInput = (String)request.getAttribute("endTimeAInput");
						String beginTimeBInput = (String)request.getAttribute("beginTimeBInput");
						String endTimeBInput = (String)request.getAttribute("endTimeBInput");
					%>
					<p>
						活动周期一: 
						<input class="Time" type="text" name="beginTimeA" readonly="readonly" value="<%=beginTimeAInput==null?"":beginTimeAInput %>" /> 
						- 
						<input class="Time" type="text" name="endTimeA" readonly="readonly" value="<%=endTimeAInput==null?"":endTimeAInput%>" />
					</p>
					<p>
						活动周期二: 
						<input class="Time" type="text" name="beginTimeB" readonly="readonly" value="<%=beginTimeBInput==null?"":beginTimeBInput%>" /> 
						- 
						<input class="Time" type="text" name="endTimeB" readonly="readonly" value="<%=endTimeBInput==null?"":endTimeBInput%>" />
						<button type="submit" class="btn btn-primary btn-sm">查询</button>
					</p>
				</form>
			</div>
		</div>
		<%
		ReturnData returnDataA = (ReturnData)request.getAttribute("returnDataA");
		ReturnData returnDataB = (ReturnData)request.getAttribute("returnDataB");
		if(returnDataA != null && returnDataB != null){
		%>
		<div class="row">
			<div class="col-sm-6 col-sm-offset-3">
				<table class="table table-striped table-bordered">
					<caption>运营数据概况</caption>
					<thead>
						<tr>
							<th class="col-sm-3">运营数据</th>
							<th>时间一</th>
							<th>时间二</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>日均活跃用户</td>
							<td><%=returnDataA.activityPlayerNum %></td>
							<td><%=returnDataB.activityPlayerNum %></td>
						</tr>
						<tr>
							<td>充值总额</td>
							<td><%=returnDataA.voucherNum %></td>
							<td><%=returnDataB.voucherNum %></td>
						</tr>
						<tr>
							<td>活跃充值人数</td>
							<td><%=returnDataA.voucherPlayerNum %></td>
							<td><%=returnDataB.voucherPlayerNum %></td>
						</tr>
						<tr>
							<td>ARPU值</td>
							<td><%=String.format("%.2f", returnDataA.arup) %></td>
							<td><%=String.format("%.2f", returnDataB.arup) %></td>
						</tr>
						<tr>
							<td>登录ARPU值</td>
							<td><%=String.format("%.2f", returnDataA.arupLogin) %></td>
							<td><%=String.format("%.2f", returnDataB.arupLogin) %></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6 col-sm-offset-3">
				<table class="table table-striped table-bordered">
					<caption>渠道充值比较</caption>
					<thead>
						<tr>
							<th class="col-sm-3">渠道</th>
							<th>时间一</th>
							<th>时间二</th>
						</tr>
					</thead>
					<tbody>
						<%for(Entry<String, Integer> s: returnDataA.channelVoucher.entrySet()){ %>
							<tr>
								<td><%=s.getKey() %></td>
								<td><%=s.getValue() %></td>
								<td><%=returnDataB.channelVoucher.get(s.getKey()) %></td>
							</tr>
						<%} %>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6 col-sm-offset-3">
				<table class="table table-striped table-bordered">
					<caption>开服时间充值比较</caption>
					<thead>
						<tr>
							<th class="col-sm-3">开服时间</th>
							<th>时间一</th>
							<th>时间二</th>
						</tr>
					</thead>
					<tbody>
						<%for(Entry<String, Integer> s: returnDataA.openVoucher.entrySet()){ %>
							<tr>
								<td><%=s.getKey() %></td>
								<td><%=s.getValue() %></td>
								<td><%=returnDataB.openVoucher.get(s.getKey()) %></td>
							</tr>
						<%} %>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6 col-sm-offset-3">
				<table class="table table-striped table-bordered">
					<caption>Vip充值比较</caption>
					<thead>
						<tr>
							<th class="col-sm-3">VIP等级</th>
							<th>时间一</th>
							<th>时间二</th>
						</tr>
					</thead>
					<tbody>
						<%for(Entry<Integer, Integer> s: returnDataA.vipVoucher.entrySet()){ %>
							<tr>
								<td><%=s.getKey() %></td>
								<td><%=s.getValue() %></td>
								<td><%=returnDataB.vipVoucher.get(s.getKey()) %></td>
							</tr>
						<%} %>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6 col-sm-offset-3">
				<table class="table table-striped table-bordered">
					<caption>等级段充值比较</caption>
					<thead>
						<tr>
							<th class="col-sm-3">等级</th>
							<th>时间一</th>
							<th>时间二</th>
						</tr>
					</thead>
					<tbody>
						<%for(Entry<String, Integer> s: returnDataA.levelVoucher.entrySet()){ %>
							<tr>
								<td><%=s.getKey() %></td>
								<td><%=s.getValue() %></td>
								<td><%=returnDataB.levelVoucher.get(s.getKey()) %></td>
							</tr>
						<%} %>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6 col-sm-offset-3">
				<table class="table table-striped table-bordered">
					<caption>服务器充值比较</caption>
					<thead>
						<tr>
							<th class="col-sm-3">服务器名称</th>
							<th>时间一</th>
							<th>时间二</th>
						</tr>
					</thead>
					<tbody>
						<%for(Entry<String, Integer> s: returnDataA.serverVoucher.entrySet()){ %>
							<tr>
								<td><%=s.getKey() %></td>
								<td><%=s.getValue() %></td>
								<td><%=returnDataB.serverVoucher.get(s.getKey()) %></td>
							</tr>
						<%} %>
					</tbody>
				</table>
			</div>
		</div>
		<%} %>
	</div>

	
	<script src="<%=request.getContextPath() + "/js/jquery-2.1.1.js"%>"></script>
	<script src="<%=request.getContextPath() + "/js/bootstrap.min.js"%>"></script>
	<script src="<%=request.getContextPath() + "/js/bootstrap-datetimepicker.min.js"%>"></script>
	<script src="<%=request.getContextPath() + "/js/bootstrap-datetimepicker.zh-CN.js"%>"></script>
	<script src="<%=request.getContextPath() + "/js/bootstrap-wysiwyg.js"%>"></script>
	<script type="text/javascript">
		$('.Time').datetimepicker({
		    format: 'yyyy-mm-dd hh:ii',
		    language: 'zh-CN'
		});
	</script>
</body>
</html>
