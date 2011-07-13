
<%@ include file="/WEB-INF/views/common/include.jsp" %>

<sec:authentication property="principal" var="principal"/>

<c:choose>
	<c:when test="${principal=='anonymousUser'}">
    		<div> YOU MUST HAVE AN ACCOUNT TO VIEW THIS PAGE</div>
  	</c:when>
  	<c:otherwise>
  		<c:set var="root"><spring:message code="esgf.openidRoot" /></c:set>
			
   		<c:choose>	
   			<c:when test="${principal.username==root}">
				<%@ include file="/WEB-INF/views/accountsview/accounts_root_content.jsp" %>
			</c:when>
			<c:otherwise>
				<%@ include file="/WEB-INF/views/accountsview/accounts_user_content.jsp" %>
			</c:otherwise>
   		</c:choose>
  	</c:otherwise>
</c:choose>     
