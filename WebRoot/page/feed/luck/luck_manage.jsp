<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>活动配置</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet"
	href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		search();
	});

	function search() {
		var colName = [ "id", "udid", "lucky_id", "option_id", "created_at",
				'updated_at', 'option_title' ];
		getQueryForPageData(null, null, colName, null, null,
				function(obj, html) {
					return html;
				}, addTabListColourEffect);
		window.top.scroll(0, 0);
	}

	function input(event_id) {
		var url = "${ctx}/sys/luck/input?event_id=" + event_id;
		var t = new $.dialog({
			title : '编辑活动',
			id : 'edit',
			page : url,
			rang : true,
			width : 600,
			height : 540,
			onXclick : function() {
				t.cancel();
			}
		});
		t.ShowDialog();
	}
	function config_awards(event_id) {
		var url = "${ctx}/sys/luck/config_awards?event_id=" + event_id;
		var t = new $.dialog({
			title : '奖项配置',
			id : 'edit',
			page : url,
			rang : true,
			width : 920,
			height : 640,
			onXclick : function() {
				t.cancel();
			}
		});
		t.ShowDialog();
	}
	function exportAwards(lucky_id) {
		document.queryForm.action = "${ctx}/sys/luck/export_awards?lucky_id="
				+ lucky_id;
		$('#queryForm').submit();
	}
	function relate_episode(id) {
		var url = "${ctx}/sys/luck/relate_episode_index?id=" + id
				+ "&program_id=147401";
		var t = new $.dialog({
			title : '关联节目',
			id : 'edit',
			page : url,
			rang : true,
			width : 880,
			height : 540,
			onXclick : function() {
				t.cancel();
			}
		});
		t.ShowDialog();
	}
</script>
</head>
<body>
	<div class="pop">
		<div class="container">
			<div class="radius">
				<div class="title">
					<div class="location">
						<span class="current">${obj.title}</span>
					</div>
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="10%" align="right">开始时间：<input type="hidden"
							name="obj.id" id="id" value="${obj.id}" /></td>
						<td width="10%" height="30"><fmt:formatDate
								value="${obj.start_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">结束时间：</td>
						<td width="10%" height="30"><fmt:formatDate
								value="${obj.end_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">关联节目：</td>
					</tr>
					<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
					<c:forEach varStatus="i" begin="0"
						end="${fn:length(episode_list)-1}" step="4">
						<tr>
							<td width="10%" align="right"></td>
							<td width="10%" height="30">${episode_list[i.index].title}</td>
							<td width="10%" height="30">${episode_list[i.index+1].title}</td>
							<td width="10%" height="30">${episode_list[i.index+2].title}</td>
							<td width="10%" height="30">${episode_list[i.index+3].title}</td>
						</tr>
					</c:forEach>
				</table>
				<p class="pBtn">
					<a class="save" href="javascript:relate_episode(${obj.id});"
						title="关联节目">关联节目</a> <a class="back" href="${ctx}/sys/luck/index"
						title="修改设置">返&nbsp;回</a>
				</p>
			</div>
		</div>
	</div>
	<div class="pop">
		<div class="container">
			<div class="radius">
				<div class="title">
					<div class="location">
						<span class="current">&nbsp;规则配置</span>
					</div>
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="10%" align="right">单账户最大获奖率：</td>
						<td width="40%" height="30">${obj.win_day}&nbsp;天
							${obj.win_count}&nbsp;次</td>
					</tr>
				</table>
				<p class="pBtn">
					<a class="save" href="javascript:input(${obj.id});" title="修改设置">设&nbsp;置</a>
					<a class="back" href="${ctx}/sys/luck/index" title="修改设置">返&nbsp;回</a>
				</p>
			</div>
		</div>
	</div>
	<div class="pop">
		<div class="container">
			<div class="radius">
				<div class="title">
					<div class="location">
						<span class="current">&nbsp;奖项配置</span>
					</div>
				</div>
				<table class="borderTable" width="100%" border="0" cellspacing="0"
					cellpadding="0">
					<tr>
						<th class="th01">奖项位置</th>
						<th class="th02">奖项名称</th>
						<th class="th03">物品链接</th>
						<th class="th03">中奖几率</th>
						<th class="th04">奖品数量</th>
						<th class="th05">剩余数量</th>
					</tr>
					<tbody>
						<c:forEach var="opt" items="${opt_list}">
							<tr>
								<td>${opt.position}</td>
								<td><input type="hidden" name="optId" value="${opt.id}" />
									${opt.title}</td>
								<td><a href="${opt.url}" target="_blank">${opt.url}</a></td>
								<td>${opt.win_rate}</td>
								<td>${opt.prize_count}</td>
								<td>${opt.surplus_prize_count}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<table class="borderTable" width="27%" border="0" cellspacing="0"
					cellpadding="0">
					<tr>
						<th class="th01" style="width: 5%">非奖项位置</th>
						<th class="th02" style="width: 10%">非奖项名称</th>
					</tr>
					<tbody id="noTableList">
						<c:forEach var="cell" items="${no_opt_list}">
							<tr>
								<td>${cell.position}</td>
								<td>${cell.title}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<p class="pBtn">
					<a class="save" href="javascript:config_awards(${obj.id});"
						title="修改设置">设&nbsp;置</a> <a class="back"
						href="${ctx}/sys/luck/index" title="修改设置">返&nbsp;回</a>
				</p>
			</div>
		</div>
	</div>
	<div class="pop">
		<div class="container">
			<div class="radius">
				<div class="title">
					<div class="location">
						<span class="current">&nbsp;中奖信息</span>
					</div>
					<div class="rightBtn">
						<a class="save" href="javascript:exportAwards(${obj.id});"
							title="导出中奖信息">导&nbsp;出</a>
					</div>
				</div>
				<form action="${ctx}/sys/luck/awards_list" id="queryForm"
					name="queryForm" method="post">
					<input type="hidden" name="lucky_id" id="lucky_id"
						value="${obj.id}" />
					<table class="borderTable" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<th style="width: 5%">序号</th>
							<th style="width: 10%">中奖时间</th>
							<th style="width: 20%">用户UDID</th>
							<th style="width: 10%">奖品名称</th>
						</tr>
						<tbody id="tableList">
						</tbody>
					</table>
					<div class="pageCode" id="tabpage">
						<%@ include file="/page/common/paginate.jsp"%>
					</div>
					<table style="display: none">
						<tbody id="listCellTemplate">
							<tr class="hovcolor">
								<td>#_seq#</td>
								<td>#created_at#</td>
								<td>#udid#</td>
								<td>#option_title#</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
		</div>
	</div>

</body>
</html>