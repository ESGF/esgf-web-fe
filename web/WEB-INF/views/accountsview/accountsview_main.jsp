
<%@ include file="/WEB-INF/views/common/include.jsp" %>

<sec:authentication property="principal" var="principal"/>


<%@ include file="/WEB-INF/views/accountsview/accountscontent.jsp" %>
<!-- 
<c:choose>
	<c:when test="${principal=='anonymousUser'}">
    		<div> YOU MUST HAVE AN ACCOUNT TO VIEW THIS PAGE</div>
  	</c:when>
  	<c:otherwise>
  		<c:set var="root"><spring:message code="esgf.openidRoot" /></c:set>
			
   		<c:choose>	
   			<c:when test="${principal.username==root}">
			</c:when>
			<c:otherwise>
			</c:otherwise>
   		</c:choose>
  	</c:otherwise>
</c:choose>     
 -->