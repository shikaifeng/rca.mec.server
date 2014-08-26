<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8" />
<meta name="viewport" content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no;" />
<title>明星详情</title>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/m/style.css" />
<script type="text/javascript">
</script>
</head>
<body>
	<div class="container">
		<div class="header">
			<p class="name">${obj.name}</p>
		</div>
		<div class="empty">&nbsp;</div>
		<div class="body">
			<div class="basic">
				<div class="avatar">
					<img src="${obj.avatar}" width="120" height="160" />
				</div>
				<div class="info">
					<h3>基本资料</h3>
					<c:if test="${obj.birthday!=null &&  obj.birthday!='' }">
						<p>
							<label>生日：</label> <span>${obj.birthday}</span>
						</p>
					</c:if>
					<c:if test="${obj.height!=null &&  obj.height!='' }">
						<p>
							<label>身高：</label> <span>${obj.height}</span>
						</p>
					</c:if>
					<c:if test="${obj.constellation!=null &&  obj.constellation!='' }">
						<p>
							<label>星座：</label> <span>${obj.constellation}</span>
						</p>
					</c:if>
					<c:if test="${obj.blood_type!=null &&  obj.blood_type!='' }">
						<p>
							<label>血型：</label> <span>${obj.blood_type}</span>
						</p>
					</c:if>
				</div>
			</div>
			<c:if test="${obj.description!=null &&  obj.description!='' }">
			<div class="summary">
				<h3>经历简介</h3>
				<div class="info">${obj.description}</div>
			</div>
			</c:if>
			<c:if test="${program_title!=null}">
			<div class="program">
				<h3>相关作品</h3>
				<div class="info">
					${program_title}
				</div>
			</div>
			</c:if>
		</div>
	</div>
</body>
</html>