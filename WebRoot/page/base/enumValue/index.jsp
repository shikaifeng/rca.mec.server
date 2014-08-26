<%@page import="tv.zhiping.common.EnumValueKeys"%>
<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/page/common/jscommon.jsp"%>
<link href="${stCtx}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${stCtx}/css/table_com.css" rel="stylesheet" type="text/css" />

<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>

<title>字典管理</title>
<script type="text/javascript">
$(document).ready(function() {
	search();
});

function search(){
	var colName = ["id","code","name"];
	getQueryForPageData(null,null,colName,null,null,null,addTabListColourEffect);
}

//新增
function add(){
	var url = "${ctx}/base/enumValue/add?type="+$("#type").val();
	var t = new $.dialog({title:'基础库添加',id:'add', page:url,rang:true,width :610,height:233,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}


//编辑
function edit(id){
	var url = "${ctx}/base/enumValue/input/"+id;
	var t = new $.dialog({title:'基础库修改',id:'add', page:url,rang:true,width :610,height:233,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}

//删除
function del(id){
	confirmDialog(function(){
		var url = ctx+"/base/enumValue/del/"+id+"?t="+new Date().getTime();
		$.ajax({url:url,dataType:"text",error:function(){alert("删除失败，请稍后重试")}, 
			success: function(data){
				if(data=="1"){
					search();
				}
			}
		});
	});
}
</script>
</head>

<body>
   <div id="cont_r">
      <form action="${ctx}/sys/enumValue/list" id="queryForm" method="post">
      <input type="hidden" name="forwardPage" id="forwardPage" value="0" />
   	  <input type="hidden" name="totalPage" id="totalPage" value="0" />
   		
      <div id="tab_search">
         <ul>
           <li>
           <label>类型：</label>
           	<select id="type" name="type">
           		<option value="<%=EnumValueKeys.IMDB_FACT_TYPE%>">IMDB百科类型</option>
           	</select>
           </li>
         </ul>
         <div class="tab_but">
          <button type="button" id="queryButton" onclick="search()">查询</button>
         </div>  
      </div><!--wh_search-->
      </form>
      <div id="tab_cz">
         <div class="tab_but">
          <button type="button"  onclick="add()">新增</button>
         </div>
         <div id="tabpage">
         	<%@ include file="/page/common/paginate.jsp"%>
		 </div><!--tabpage结束--> 
      </div><!--tab_cz-->
      <div class="cont_table">
     <table summary="" class="tabcom">
     <tr>
	  <th style="width:80%;">名称</th>
	 <th style="width:20%;">操作</th>
     </tr>
      <tbody id="tableList">
      </tbody>
    </table> 
    </div>
   </div>
   	<table style="display: none">
   		<tbody id="listCellTemplate">
   		<tr class="hovcolor">
			<td>
				<a href="javascript:edit('#id#')">#name#</a>
			</td>
			<td class="td01">
				<img width="14" height="14" src="${stCtx}/images/icon_ab04.png"/>
	      		<a href="javascript:edit('#id#')">编辑</a>
	      		<img width="14" height="14" src="${stCtx}/images/icon_error.png"/>
	      		<a href="javascript:del('#id#')">删除</a>
			</td>
		
	     </tr>
	     </tbody>
    </table>
</body>
</html>