<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>节目日程查询</title>
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
		var colName = [ "id", "avatar", "name", "name_en", "gender",
				'birthday', 'education', "born_place", "constellation",
				"height", "weight", "country", "program_count", "mtime_url",
				"imdb_url" ];
		getQueryForPageData(
				null,
				null,
				colName,
				null,
				null,
				function(obj, html) {
					if (!isNotBlank(obj.avatar)) {
						html = html.replace("#avatar#", defaultPhotp);
					}

					var str = "";
					if (isNotBlank(obj.mtime_url)) {
						str = str
								+ '<a href="'+obj.mtime_url+'" target="_blank">时光网地址</a>';
					}
					if (isNotBlank(obj.imdb_url)) {
						str = str
								+ ' <a href="'+obj.imdb_url+'" target="_blank">IMDB地址</a>';
					}

					html = html.replace("#link_url#", str);
					return html;
				}, addTabListColourEffect);
		window.top.scroll(0, 0);
	}
</script>
<style>
a {
	text-align: center;
}
</style>
</head>
<body>
	<div class="container">
		<div class="radius">
			<div class="title">
				<div class="location">
					<span class="current">节目日程查看</span>
				</div>
			</div>
			<div class="content">
				<form action="${ctx}/sys/person/list" id="queryForm" method="post">
					<table class="search" width="340" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="180"><span>电台：</span> <select id="gender"
								name="obj.gender" style="width: 160px;">
									<option value="">--请选择--</option>
									<option value="male">男</option>
									<option value="female">女</option>
									<option value="privacy">保密</option>
							</select></td>
							<td width="40"><a href="javascript:search()" title="查 询">查
									询</a></td>
						</tr>
					</table>
					<table class="borderTable" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<th style="width: 5%">编号</th>
							<th style="width: 30%">电台</th>
							<th style="width: 15%">开始时间</th>
							<th style="width: 15%">结束时间</th>
							<th style="width: 20%">节目名</th>
							<th style="width: 20%">剧集</th>
						</tr>
						<tbody id="tableList">
						</tbody>
					</table>
					<div class="pageCode" id="tabpage">
						<%@ include file="/page/common/paginate.jsp"%>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>