<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>抽奖管理</title>
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
		var colName = [ "id", "title", "summary", "start_time", "end_time", 'estimate_count',
				"win_day", "win_count"];
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
	function edit(event_id){
		var url = "${ctx}/sys/luck/input?event_id="+event_id;
		var t = new $.dialog({title:'编辑活动',id:'edit', page:url,rang:true,width :600,height:540,
			onXclick:function(){
				t.cancel();
			}
		});
		t.ShowDialog();
	}
	function add(){
		var url = "${ctx}/sys/luck/input?";
		var t = new $.dialog({title:'添加活动',id:'edit', page:url,rang:true,width :600,height:540,
			onXclick:function(){
				t.cancel();
			}
		});
		t.ShowDialog();
	}
	function relate_episode(id){
		var url = "${ctx}/sys/luck/relate_episode_index?id="+id+"&program_id=147401";
		var t = new $.dialog({title:'关联节目',id:'edit', page:url,rang:true,width :880,height:540,
			onXclick:function(){
				t.cancel();
			}
		});
		t.ShowDialog();
	}
	function config_awards(event_id){
		var url = "${ctx}/sys/luck/config_awards?event_id="+event_id;
		var t = new $.dialog({title:'奖项配置',id:'edit', page:url,rang:true,width :920,height:480,
			onXclick:function(){
				t.cancel();
			}
		});
		t.ShowDialog();
	}

	function del(id){
		confirmDialog(function(){
			var url = "${ctx}/sys/luck/del/"+id+"?t="+new Date().getTime();
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
					<span class="current">抽奖管理</span>
				</div>
				<div class="rightBtn">
					<a href="javascript:add()" title="添加活动" target="rightFrame">添加活动</a>
				</div>
			</div>
			<div class="content">
				<form action="${ctx}/sys/luck/list" id="queryForm" method="post">
					<table class="borderTable" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<th style="width: 5%">编号</th>
							<th style="width: 10%">名称</th>
							<th style="width: 20%">简介</th>
							<!-- <th style="width: 20%">相关联节目</th> -->
							<th style="width: 10%">单账户最大获奖率</th>
							<th style="width: 10%">开始时间</th>
							<th style="width: 10%">结束时间</th>
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
								<td><a href="javascript:edit('#id#')">#title#</a></td>
								<td>#summary#</td>
								<!-- <td></td> -->
								<td>#win_day#天#win_count#次</td>
								<td>#start_time#</td>
								<td>#end_time#</td>
								<td class="code">
								<a href="javascript:edit('#id#')">编辑</a>
								<!-- <a href="javascript:config_awards('#id#')">奖项配置</a> -->
								<a href="${ctx}/sys/luck/luck_manage?event_id=#id#" title="活动管理" target="rightFrame">活动管理</a>
								<!-- <a href="javascript:relate_episode('#id#')">关联节目</a> -->
								<!-- <a href="${ctx}/sys/luck/check_awards?event_id=#id#" title="中奖信息" target="rightFrame">中奖信息</a> -->								
								<a href="javascript:del('#id#')">删除</a></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>