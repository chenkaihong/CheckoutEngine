<!DOCTYPE html>
<%@page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta name="viewport" content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, height=device-height, target-densitydpi=device-dpi" />
<link rel="stylesheet" href="../css/jquery.mobile-1.4.2.css">
<meta charset="utf-8" />
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<meta name="format-detection" content="telephone=no" />
<meta name="msapplication-tap-highlight" content="no" />
<title>Hello World</title>
</head>
<body>

	<div data-role="page" id="SearchPage">
		<div data-role="header">
			<h1>活动效果-欢迎使用</h1>
			<a href="#showPage" data-role="button" data-icon="search"
				id="searchButton">搜索</a>
		</div>

		<div data-role="content">
			<p>
				<b>活动周期一: </b>
			</p>
			<div class="ui-grid-b">
				<div class="ui-block-b">
					<input type="date" name="beginTimeA" id="beginTimeA">
				</div>
				<div class="ui-block-c">
					<input type="date" name="endTimeA" id="endTimeA">
				</div>
			</div>
			<p>
				<b>活动周期二: </b>
			</p>
			<div class="ui-grid-b">
				<div class="ui-block-b">
					<input type="date" name="beginTimeB" id="beginTimeB">
				</div>
				<div class="ui-block-c">
					<input type="date" name="endTimeB" id="endTimeB">
				</div>
			</div>

		</div>
	</div>

	<div data-role="page" id="showPage">
		<div data-role="header">
			<a href="#SearchPage" data-role="button" data-icon="home">首页</a>
			<h1>活动效果-欢迎使用</h1>
		</div>
		<div data-role="content" id="show"></div>
	</div>

	<script type="text/javascript" src="../js/jquery-2.1.1.js"></script>
	<script type="text/javascript" src="../js/jquery.mobile-1.4.2.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#searchButton").on("click", function() {
				$.post("http://127.0.0.1:8080/CheckoutEngine/Test", {
					beginTime : "2015-07-06 11:00:00",
					endTime : "2015-07-06 11:00:00"
				}, function(data, result) {
					alert(data);
				});
			});
		});
	</script>
</body>
</html>
