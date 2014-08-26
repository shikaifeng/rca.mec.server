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
	search();
});

function search(){
	var colName = ["id","title","value","description"];
	getQueryForPageData(null,null,colName,null,null,function(obj,html){
		return html;
	},addTabListColourEffect);
	window.top.scroll(0,0);
}
/** 新增 **/
function add(){
	var url = "${ctx}/sys/appconfig/input";
	var t = new $.dialog({title:'添加App系统参数',id:'add', page:url,rang:true,width :600,height:480,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}


/** 编辑 **/
function edit(id){
	var url = "${ctx}/sys/appconfig/input/"+id;
	var t = new $.dialog({title:'修改App系统参数',id:'edit', page:url,rang:true,width :600,height:480,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}

/** 删除 **/
function del(id){
	confirmDialog(function(){
		var url = "${ctx}/sys/appconfig/del/"+id+"?t="+new Date().getTime();
		$.ajax({url:url,dataType:"json",error:function(){alert("删除失败，请稍后重试")}, 
			success: function(data){
				if(data.status=="0"){
					search();
					alert("删除成功！");
				}
			}
		});
	});
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
	    	<div class="location"><span class="current">App系统配置</span></div>
	        <div class="rightBtn"><a href="javascript:add()" title="添加title" target="rightFrame">添加参数</a></div>
	    </div>
		<div class="content">
			<form action="${ctx}/sys/appconfig/list" id="queryForm" method="post">
	    	<table  class="search" width="830" border="0" cellspacing="0" cellpadding="0">
	          <tr >
	            <td width="260"><span>参数名：</span><input name="obj.title" id="title" type="text" style="width: 160px;"value="${obj.title}"/></td>
	            <td width="80"><a href="javascript:search()" title="查 询">查 询</a></td>
	            <td width="474"></td>
	            <td width="56">&nbsp;</td>
	          </tr>
	        </table>	        
	        <table class="borderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <th style="width: 5%">编号</th>
	            <th style="width: 30%">参数名</th>
	            <th style="width: 15%">参数值</th>
	            <th style="width: 15%">描述</th>
	            <th style="width: 15%">操作</th>
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
					<td><a href="javascript:edit('#id#')">#title#</a></td>
					<td>#value#</td>
					<td>#description#</td>
					<td class="code">
						<a href="javascript:edit('#id#')">编辑</a>
			      		<a href="javascript:del('#id#')">删除</a>
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