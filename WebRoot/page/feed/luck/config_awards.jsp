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
<link href="${stCtx}/css/table_com.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script src="${stCtx}/js/jquery.validate-1.9.min.js"
	type="text/javascript"></script>
<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>
<style type="text/css">
#uploadUploader {
	width: 83px;
	height: 83px;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		setFormValidate();
// 		inputQueReady();
	});
	function doSave() {
		$('#inputForm').submit();
	}
	function setFormValidate() {
		$("#inputForm").validate({
			rules : {
				"lucky_id" : "required",
				"winRate" : "required",
				"prizeCount" : "required",
			},
			messages : {
				"lucky_id" : "请先选择活动",
				"winRate" : " 请添加",
				"prizeCount" : " 请添加",
			},
			submitHandler : function(form) {
				if ($("#inputForm").valid()) {
					$("#inputForm").ajaxSubmit({
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
			}
		});
	}
	function append_opt() {
		$("#tableList").append($("#trTemplate").html());
	}
	function delTr(obj) {
		$(obj).parents("tr").remove();
	}
	
	//非奖品选项
	function append_no_opt() {
		$("#noTableList").append($("#noTrTemplate").html());
	}
	function delNoTr(obj) {
		$(obj).parents("tr").remove();
	}
</script>
</head>
<body style="background: #f6f9fb;">
	<form action="${ctx}/sys/luck/save_awards" method="post" id="inputForm">
		<input type="hidden" name="lucky_id" id="lucky_id" value="${event_id}" />
		<div class="pop ">
			<div class="popContent">
				<table summary="" border="0" class="tabcom">
					<tr>
						<th class="th03">奖项位置</th>
						<th class="th02">奖项名称</th>
						<th class="th03">物品链接</th>
						<th class="th03">中奖几率</th>
						<th class="th04">奖品数量</th>
						<th class="th05">剩余数量</th>
						<th class="th06">操作</th>
					</tr>
					<tbody id="tableList">
						<tr>
							<td style="width: 55px;">1</td>
							<td style="width: 150px;">范例</td>
							<td style="width: 200px;">http://tao.51yaobao.tv/</td>
							<td style="width: 65px;">1/10000</td>
							<td style="width: 65px;">10000</td>
							<td style="width: 50px;">(不可改)</td>
							<td class="tab_cent" style="width: 50px;"><img
								src="${stCtx}/images/icon_error.png" alt="删除" /></td>
						</tr>
						<c:forEach var="cell" items="${opt_list}">
							<tr>
								<td><input type="text" name="position"
									value="${cell.position}" maxlength="10" size="20"
									style="width: 55px;" /></td>
								<td><input type="hidden" name="optId" value="${cell.id}" />
									<input type="text" name="optTitle" value="${cell.title}"
									maxlength="30" size="40" style="width: 150px;" /></td>
								<td><input type="text" name="url" value="${cell.url}" size="40" style="width: 200px;" /></td>
								<td><input type="text" name="winRate"
									value="${cell.win_rate}" maxlength="10" size="30"
									style="width: 65px;" /><span style="float: none;" class="red">*<label
										for="title" class="error">${_msgFor_winRate}</label></span></td>
								<td><input type="text" name="prizeCount"
									value="${cell.prize_count}" maxlength="10" size="30"
									style="width: 65px;" /><span style="float: none;" class="red">*<label
										for="title" class="error">${_msgFor_prizeCount}</label></span></td>
								<td><input type="text" name="surplusCount"
									value="${cell.surplus_prize_count}"
									maxlength="10" size="40" style="width: 50px;" /></td>
								<td class="tab_cent"><a href="#"
									onclick="delTr(this);return false"><img
										src="${stCtx}/images/icon_error.png" alt="删除" /></a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div id="optDivId">
					<div class="cont_table" style="width: 400px">
						<div id="tab_cz" style="width: 100%; padding: 0; margin: 0;">
							<div class="tab_but">
								<button type="button" id="addButtonId" onclick="append_opt()">增加奖项</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="pop ">
			<div class="popContent">
				<table summary="" border="0" class="tabcom">
					<tr>
						<th class="th02">非奖项位置</th>
						<th class="th02">非奖项名称</th>
						<th class="th06">操作</th>
					</tr>
					<tbody id="noTableList">
						<c:forEach var="cell" items="${no_opt_list}">
						<tr>
							<td><input type="text" name="no_position" value="${cell.position}" maxlength="10" size="20" style="width: 55px;" /></td>
							<td><input type="hidden" name="no_opt_id" value="${cell.id}" /> <input type="text" name="no_opt_title" value="${cell.title}" maxlength="30" size="40" style="width: 150px;" /></td>
							<td class="tab_cent"><a href="#" onclick="delNoTr(this);return false"><img src="${stCtx}/images/icon_error.png" alt="删除" /></a></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
				<div id="optDivId">
					<div class="cont_table" style="width: 400px">
						<div id="tab_cz" style="width: 100%; padding: 0; margin: 0;">
							<div class="tab_but">
								<button type="button" onclick="append_no_opt()">非奖项</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<p class="pBtn">
			<a class="save" href="javascript:doSave();" title="保  存">保&nbsp;存</a><a
				class="back" href="javascript:frameElement.lhgDG.cancel();"
				title="关  闭">关&nbsp; 闭</a>
		</p>
	</form>
	<table style="display: none">
		<tbody id="noTrTemplate">
			<tr>
				<td><input type="text" name="no_position" value=""
					maxlength="10" size="40" style="width: 55px;" /></td>
				<td><input type="hidden" name="no_opt_id" value="" /> <input type="text" name="no_opt_title" value="" maxlength="30" size="40" style="width: 150px;" /></td>
				<td class="tab_cent"><a href="#" onclick="delNoTr(this);return false"><img src="${stCtx}/images/icon_error.png" alt="删除" /></a></td>
			</tr>
		</tbody>
		
		<tbody id="trTemplate">
			<tr>
				<td><input type="text" name="position" value=""
					maxlength="10" size="40" style="width: 55px;" /></td>
				<td><input type="hidden" name="optId" value="" /> <input
					type="text" name="optTitle" value="" maxlength="30"
					size="40" style="width: 150px;" /></td>
				<td><input type="text" name="url" value="" size="40" style="width: 200px;" /></td>
				<td><input type="text" name="winRate" value=""
					maxlength="10" size="30" style="width: 65px;" /><span
					style="float: none;" class="red">*<label for="winRate"
						class="error">${_msgFor_winRate}</label></span></td>
				<td><input type="text" name="prizeCount"
					value="" maxlength="10" size="30"
					style="width: 65px;" /><span style="float: none;" class="red">*<label
						for="prizeCount" class="error">${_msgFor_prizeCount}</label></span></td>
				<td><input type="text" name="surplusCount" value="" maxlength="10" size="40"
					style="width: 50px;" /></td>
				<td class="tab_cent"><a href="#"
					onclick="delTr(this);return false"><img
						src="${stCtx}/images/icon_error.png" alt="删除" /></a></td>
			</tr>
		</tbody>
	</table>
</body>
</html>