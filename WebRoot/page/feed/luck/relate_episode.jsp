<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>抽奖管理</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		search();
	});

	function search() {
		var url = "${ctx}/sys/luck/relate_episode_list";
		$("#queryForm").attr("action",url);
		var colName = [ "id", "title", "start_time", "end_time"];
		getQueryForPageData(null,null,colName,null,null,function(obj, html) {
			var str = obj.checked;
			if(isNotBlank(str) && str == 1){
				str = "checked";
			}else{
				str = "";
			}
			html = html.replace("#check_opt#",str);
			return html;
		}, addTabListColourEffect);
		window.top.scroll(0, 0);
	}
	
	
	function doSubmit(){
		var url = "${ctx}/sys/luck/relate_episode_save";
		$("#queryForm").attr("action",url);
		$("#queryForm").ajaxSubmit({
			dataType : "json",
			success : function(data) {
				if (data.status == 0) {
					getLhgdialogParent().location.reload();
					frameElement.lhgDG.cancel();					
				} else {
					alert(data.msg);
				}
			}
		});
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
					<span class="current">抽奖节目关联</span>
				</div>
				<div class="rightBtn">
					<a href="javascript:doSubmit()" title="添加活动" target="rightFrame">提交</a>
				</div>
			</div>
			<div class="content">
				<form action="${ctx}/sys/luck/relate_episode_list" id="queryForm" method="post">
					<input type="hidden" name="id" id="id" value="${obj.id}" />
					<input type="hidden" name="program_id" id="program_id" value="${program.id}" />
					<table class="borderTable" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<th style="width: 5%">选择</th>
							<th style="width: 5%">编号</th>
							<th style="width: 10%">名称</th>
							<!-- <th style="width: 10%">开始时间</th>
							<th style="width: 10%">结束时间</th> -->
						</tr>
						<tbody id="tableList">
						</tbody>
					</table>
					<div class="pageCode" id="tabpage">
					</div>
					<table style="display: none">
						<tbody id="listCellTemplate">
							<tr class="hovcolor">
								<td><input type="checkbox" name="checks" value="#id#" #check_opt#/></td>
								<td>#id#</td>
								<td>#title#</td>
								<!-- <td>#start_time#</td>
								<td>#end_time#</td> -->
							</tr>
						</tbody>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>