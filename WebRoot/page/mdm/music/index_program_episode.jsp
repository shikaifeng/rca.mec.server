<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	initOpetionsByJssj($("#year"),1888);
	search();
});


function search(){
	var colName = ["id","cover","title","url","singer",'type_title'];
	getQueryForPageData(null,null,colName,null,null,function(obj,html){
		return html;
	},addTabListColourEffect);
	window.top.scroll(0,0);
}

/** 新增 **/
function add(){
	return;
	var url = "${ctx}/sys/music/input";
	var t = new $.dialog({title:'添加music',id:'add', page:url,rang:true,width :600,height:480,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}


/** 编辑 **/
function edit(id){
	return;
	var url = "${ctx}/sys/music/input/"+id;
	var t = new $.dialog({title:'修改music',id:'edit', page:url,rang:true,width :600,height:480,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}

/** 删除 **/
function del(id){
	confirmDialog(function(){
		var url = "${ctx}/sys/music/del/"+id+"?t="+new Date().getTime();
		$.ajax({url:url,dataType:"json",error:function(){alert("删除失败，请稍后重试")}, 
			success: function(data){
				if(data.status=="0"){
					search();
				}
			}
		});
	});
}

//feed查看
function feed_index(pid){
	var url = ctx+"/sys/feed/index?episode_id="+pid;
	window.location.href=url;
}
</script>
<style>
a{ text-align:center;}
</style>
</head>
<body>

<div class="container">
	<div class="radius">
		<div class="title">
	    	<div class="location"><span class="current">music管理</span></div>
	        <div class="rightBtn"><a href="javascript:add()" title="添加music" target="rightFrame">添加person</a></div>
	    </div>
		<div class="content">
			<form action="${ctx}/sys/music/list_program_episode" id="queryForm" method="post">
			<input type="hidden" name="program_id" id="program_id" value="${program_id}"/>
			<input type="hidden" name="episode_id" id="episode_id" value="${episode_id}"/>
	    	<table  class="search" width="830" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <!-- <td width="220"><span>标题：</span><input name="obj.title" id="title" type="text" style="width: 160px;"/></td> -->
	            <td width="80"><a href="javascript:search()" title="查 询">查 询</a></td>
	            <td width="56">&nbsp;</td>
	          </tr>
	        </table>
	        
	        <table class="borderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <th style="width: 5%">编号</th>
	            <th style="width: 10%">海报</th>
	            <th style="width: 10%">名称</th>
	            <th style="width: 25%">类型</th>
	            <th style="width: 10%">歌唱者</th>
	            <th style="width: 2%">操作</th>
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
		   			<td><img src="#cover#" alt="" width="20px" height="20px"/></td>
					<td><a href="javascript:edit('#id#')">#title#</a></td>
					<td>#type_title#</td>
					<td>#singer#</td>
					<td class="code">
					<a href="#url#" target="_blank">点击播放</a>
					</td>
			     </tr>
			     </tbody>
		    </table>
		    </form>
	    </div>
	</div>   
</div>
</body>
</html>