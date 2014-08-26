<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Banner图片</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet"
	href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		initOpetionsByJssj($("#year"), 1888);
		search();
	});
	function search() {
		var colName = [ "id", "program_id","episode_id","question_id","path"];
		getQueryForPageData(
				null,
				null,
				colName,
				null,
				null,
				function(obj, html) {
					
					return html;
				}, addTabListColourEffect);
		window.top.scroll(0, 0);
	}
	function edit(id){
		var url = "${ctx}/sys/banner/img_input?banner_id="+id;
		var t = new $.dialog({title:'编辑banner',id:'edit', page:url,rang:true,width :600,height:360,
			onXclick:function(){
				t.cancel();
			}
		});
		t.ShowDialog();
	}
	function add(){
		var url = "${ctx}/sys/banner/img_input?";
		var t = new $.dialog({title:'添加banner',id:'edit', page:url,rang:true,width :600,height:360,
			onXclick:function(){
				t.cancel();
			}
		});
		t.ShowDialog();
	}
	function del(id){
		confirmDialog(function(){
			var url = "${ctx}/sys/banner/img_del/"+id;
			$.ajax({url:url,dataType:"json",error:function(){alert("删除失败，请稍后重试")}, 
				success: function(data){
					if(data.status=="0"){
						search();
					}
				}
			});
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
					<span class="current">Banner图片</span>
				</div>
				
			</div>
			<div class="content">
				<form action="${ctx}/sys/banner/img_list" id="queryForm" method="post">
					<table class="borderTable" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<th style="width: 5%">编号</th>
							<th style="width: 20%">节目id</th>
							<th style="width: 10%">剧集id</th>
							<th style="width: 10%">问答id</th>
							<th style="width: 20%">图片</th>
							<th style="width: 20%">操作</th>
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
								<td>#program_id#</td>
								<td>#episode_id#</td>
								<td>#question_id#</td>
								<td><a href="#path#"><img src="#path#" alt="" width="50px" height="50px"/></a></td>
								<td>
								
							</tr>
						</tbody>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>