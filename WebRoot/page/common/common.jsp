<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="stCtx" value="${pageContext.request.contextPath}" scope="request" />

<%--
dev
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="stCtx" value="${pageContext.request.contextPath}" scope="request" />
--%><%--
publish
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="stCtx" value="http://whdz.whb.cn/static/whclub" scope="request" />
--%>
<%@ page isELIgnored="false"%>