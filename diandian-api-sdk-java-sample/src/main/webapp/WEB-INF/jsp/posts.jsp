<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/inc/taglibs.inc"%>
<c:set value="tag" var="home" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>博客内容</title>
</head>
<body>
${postsView.blog }

<br/>

${postsView.totalPosts }
<c:set var="posts" value="${postsView.posts }" />
<%@include file="/WEB-INF/jsp/inc/posts.inc"%>
</body>
</html>