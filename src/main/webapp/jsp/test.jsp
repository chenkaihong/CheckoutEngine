<!DOCTYPE HTML>
<%@page import="com.bear.util.TimeUtil"%>
<%@page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<link href="../css/bootstrap.min.css" rel="stylesheet">
<link href="../css/bootstrap-datetimepicker.min.css" rel="stylesheet">
<style type="text/css">
.show-grid [class^="col-"] {
	padding-top: 10px;
	padding-bottom: 10px;
	background-color: #eee;
	border: 1px solid #ddd;
	background-color: rgba(86, 61, 124, .15);
	border: 1px solid rgba(86, 61, 124, .2);
}
</style>
</head>
<body>
	<div class="container">
		<div class="hero-unit">
			<div class="pull-right">
				<br> 
				<a class="twitter-follow-button" href="https://twitter.com/mindmup">Follow @mindmup</a>
			</div>
			<h1>
				bootstrap-wysiwyg 
				<br> 
				<small>为Bootstrap定制的微型所见即所得（What you see is what you get）富文本编辑器</small>
			</h1>
			<hr>
		</div>
		<div class="row">
			<canvas id="myChart" style="width: 100%;height: 100%"></canvas>
		</div>
		<div class="row">
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
						<td>500</td>
						<td>700</td>
					</tr>
					<tr>
						<td>充值总额</td>
						<td>49999</td>
						<td>63742</td>
					</tr>
					<tr>
						<td>活跃充值人数</td>
						<td>200</td>
						<td>500</td>
					</tr>
					<tr>
						<td>ARPU值</td>
						<td>3000</td>
						<td>6000</td>
					</tr>
					<tr>
						<td>登录ARPU值</td>
						<td>2000</td>
						<td>4000</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="row">
			<div class="col-sm-12" style="padding: 0px">
				<button class="pull-right">next</button>
				<input size="16" type="text" value="<%=TimeUtil.nowTimeString() %>" id="datetimepicker" readonly class="form_datetime pull-right">
			</div>
			
		</div>
	</div>

	<script src="../js/jquery-1.11.1.min.js"></script>
	<script src="../js/bootstrap.min.js"></script>
	<script src="../js/Chart.min.js"></script>
	<script src="../js/bootstrap-wysiwyg.js"></script>
	<script src="../js/bootstrap-datetimepicker.min.js"></script>
	<script src="../js/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
	<script type="text/javascript">
		var data = {
			labels : ["日活跃用户","充值总额","活跃充值人数","ARPU值","登录ARPU值"],
			datasets : [
				{
					fillColor : "rgba(220,220,220,0.5)",
					strokeColor : "rgba(220,220,220,1)",
					pointColor : "rgba(220,220,220,1)",
					pointStrokeColor : "#fff",
					data : [65,59,90,81,56,55,40]
				},
				{
					fillColor : "rgba(151,187,205,0.5)",
					strokeColor : "rgba(151,187,205,1)",
					pointColor : "rgba(151,187,205,1)",
					pointStrokeColor : "#fff",
					data : [28,48,40,19,96,27,100]
				}
			]
		}
		var options = {};
		var ctx = $("#myChart").get(0).getContext("2d");
		var myNewChart = new Chart(ctx).Line(data,options);
		
		$('#datetimepicker').datetimepicker({
		    format: 'yyyy-mm-dd hh:ii:ss'
		});
	</script>
</body>
</html>
