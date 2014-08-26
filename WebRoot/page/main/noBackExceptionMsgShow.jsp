<%@ include file="/page/common/common.jsp"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>提示信息</title>
<link href="${cssCtx}/css/class.css" rel="stylesheet" type="text/css">
</head>
<body>
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
        <td width="17"><img src="${imgCtx}/image/login/left.png" width="17" height="85"></td>
        <td background="${imgCtx}/image/login/center.png"><table width="500"  border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td width="40"><img src="${imgCtx}/image/login/biaosi.gif" width="31" height="29"></td>
              <td><s:property value="%{exception.msgBean.msg}"/></td>
            </tr>
        </table></td>
        <td width="17"><img src="${imgCtx}/image/login/right.png" width="17" height="85"></td>
      </tr>
    </table>
      <table width="100%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td><div align="center"><img src="${imgCtx}/image/login/fh.jpg" width="71" height="24" border="0" style="cursor:pointer;" onclick="<s:property value="%{exception.msgBean.onclickTodo}"/>"></div></td>
        </tr>
      </table></td>
  </tr>
</table>
</body>
</html>
