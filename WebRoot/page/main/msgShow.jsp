<%@ include file="/page/common/common.jsp"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%@page import="tv.zhiping.sys.bean.MsgBean"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提示信息</title>
<script type="text/javascript">
	$(document).ready(function() {
		var autoType = '${msgBean.autoExcute}';
		if(autoType=='<%=MsgBean.AUTO_EXCUTE_YES%>'){
			setTimeout('back()',5000);
		}
	});

	function back(){
		var location = '${msgBean.onclickTodo}';
		var urlType = '${msgBean.urlType}';
		if(urlType=='<%=MsgBean.URL_TYPE_JS%>'){//直接自动 history.go(-1);
			eval(location);
		}else{
			window.location=location;
		}
	}
</script>
</head>
<body  scroll="no">
<table width="97%" height="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="25"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="25">提示信息</td>
        </tr>
    </table>    </td>
  </tr>
  <tr>
    <td valign="top"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="17"></td>
        <td><table width="500"  border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td width="40"></td>
              <td>${msgBean.msg}</td>
            </tr>
        </table></td>
        <td width="17"></td>
      </tr>
    </table>
      <table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td><div align="center">
          <input type="button" value=" 返回 " onclick="back()">
          </div></td>
        </tr>
      </table></td>
  </tr>
</table>
</body>
</html>
