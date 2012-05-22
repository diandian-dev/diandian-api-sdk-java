<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/inc/taglibs.inc"%>
<c:set value="tag" var="home" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页动态</title>
</head>
<body>
<c:set var="posts" value="${dashboardView.posts }" />
<%@include file="/WEB-INF/jsp/inc/posts.inc"%>
</body>
</html>