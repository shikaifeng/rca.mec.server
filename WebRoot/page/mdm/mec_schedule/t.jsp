<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>节目日程查询</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link href="${stCtx}/css/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="${stCtx}/css/bootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
<style type="text/css" media="screen">
span.deleted {color: black;display: none;}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		var str = "${obj.mec_channel_id}";
		$("#mec_channel_id").val(str);
	});

	function change_channel(){
		var week = $("#week").val();
		var mec_channel_id = $("#mec_channel_id").val();
		to_schedule(mec_channel_id,week);
	}
	
	function change_week(week){
		var mec_channel_id = $("#mec_channel_id").val();
		to_schedule(mec_channel_id,week);
	}
	
	function to_schedule(channel_id,week){
		var url = "${ctx}/epg?mec_channel_id="+channel_id+"&week="+week;
		window.location.href=url;
	}
	
	function edit(id){
		
	}
</script>

</head>
<body style="padding-top: 60px;">
	<input type="hidden" id="week" name="week" value="${obj.week}" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span10">
				<div class="row">
					<select id="mec_channel_id" name="mec_channel_id" onchange="change_channel()">
						<c:forEach var="cell" items="${channel_list}">
						<option value="${cell.id}">${cell.name}</option>
						</c:forEach>
					</select>
				</div>
				<center>
					<h2>${obj.mec_channel_name} week_${obj.week}</h2>
					<h3><fmt:formatDate value="${obj.min_date}" type="both" pattern="yyyy-MM-dd"/> ~ <fmt:formatDate value="${obj.max_date}" type="both" pattern="yyyy-MM-dd"/></h3>
					<div>
						<a class="btn" href="javascript:change_week('${obj.prev_week}')">上一周</a> 
						<a class="btn" href="javascript:change_week('${obj.next_week}">下一周</a>
						
						<a class="btn" href="javascript:;">添加</a>
					</div>
				</center>
				<div>
					<table class="table table-bordered table-striped" style="width: 1800px;" border="1">
						
						<thead>
							<tr>
								<th>时间</th>
								<c:forEach var="cell" items="${schedule_list}">
								<th class="clearfix">星期${cell.week_day} <a href="javascript:;">${cell.ymd}</a>
								</th>
								</c:forEach>
							</tr>
						</thead>
						
						<tbody>
							<tr>
								<td>早间</td>
								<c:forEach var="cell" items="${schedule_list}">
								<td>
									<ol>
										<c:forEach var="child_cell" items="${cell.prev_zs}">
										<li><a href="javascript:edit('${child_cell.id}')" target="_blank"> 01:03 ${child_cell.title} </a></li>
										</c:forEach>
									</ol>
								</td>
								</c:forEach>
							</tr>
							<tr>
								<td>上午</td>
								<c:forEach var="cell" items="${schedule_list}">
								<td>
									<ol>
										<c:forEach var="child_cell" items="${cell.sw}">
										<li><a href="javascript:edit('${child_cell.id}')" target="_blank"> 01:03 ${child_cell.title} </a></li>
										</c:forEach>
									</ol>
								</td>
								</c:forEach>
							</tr>
							<tr>
								<td>下午</td>
								<c:forEach var="cell" items="${schedule_list}">
								<td>
									<ol>
										<c:forEach var="child_cell" items="${cell.xw}">
										<li><a href="javascript:edit('${child_cell.id}')" target="_blank"> 01:03 ${child_cell.title} </a></li>
										</c:forEach>
									</ol>
								</td>
								</c:forEach>
							</tr>
							<tr>
								<td>晚间</td>
								<c:forEach var="cell" items="${schedule_list}">
								<td>
									<ol>
										<c:forEach var="child_cell" items="${cell.xw}">
										<li><a href="javascript:edit('${child_cell.id}')" target="_blank"> 01:03 ${child_cell.title} </a></li>
										</c:forEach>
									</ol>
								</td>
								</c:forEach>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>