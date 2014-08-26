<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
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

	function exportAwards(lucky_id) {
		document.queryForm.action = "${ctx}/sys/luck/export_awards?lucky_id="+lucky_id;
		$('#queryForm').submit();
	}
</script>
</head>
<body style="background: #f6f9fb;">
	<div class="container">
		<div class="radius">
			<div class="title">
				<div class="location">
					<span class="current">中奖信息</span>
				</div>
				<div class="rightBtn">
					<a href="javascript:exportAwards(${lucky_id});" title="导出活动" target="rightFrame">导 出</a>
					<a href="${ctx}/sys/luck/index" title="返回" target="rightFrame">返 回</a>
				</div>
			</div>
			<div class="content">
				<form action="${ctx}/sys/luck/awards_list" id="queryForm" name="queryForm"
					method="post">
					<input type="hidden" name="lucky_id" id="lucky_id"
						value="${lucky_id}" />
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
								<td>#id#</td>
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