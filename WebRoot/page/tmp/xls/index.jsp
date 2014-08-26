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
/** 新增 **/
function add(){
	var url = "${ctx}/sys/tmpUtilXls/input";
	var t = new $.dialog({title:'添加剧集',id:'add', page:url,rang:true,width :600,height:480,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
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
	    	<div class="location"><span class="current">剧集信息导入</span></div>
	        <div class="rightBtn"><a href="javascript:add()" title="添加title" target="rightFrame">添加</a></div>
	    </div>
	</div>   
</div>
</body>
</html>