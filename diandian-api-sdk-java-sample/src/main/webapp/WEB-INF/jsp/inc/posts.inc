<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<c:forEach var="item" items="${posts}">
  <c:choose>
  <c:when test="${item.type == 'link'}">
  link:${item.title }----${item.url}----${item.description }
  </c:when>
  <c:when test="${item.type=='text'}">
  text:${item.title }----${item.body}
  </c:when>
  <c:when test="${item.type=='photo'}">
        ${photo.caption}
  <c:forEach var="photo" items="${item.photos}">
        ${photo.caption}
  <c:forEach var="image" items="${photo.altSizes}">
  ${image.key}
        <image src="${image.value.url}"/>
  </c:forEach>
  </c:forEach>
  </c:when>
  <c:when test="${item.type=='audio'}">
  audio:${item.musicName}----${item.musicSinger }----${item.playerUrl }
  </c:when>
  <c:when test="${item.type=='video'}">
  video:${item.sourceUrl}
  </c:when>
  </c:choose>
  <br/>
 </c:forEach>