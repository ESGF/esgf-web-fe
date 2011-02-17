<%@ include file="/WEB-INF/views/common/include.jsp" %>
&nbsp;
<hr/>

<div class="footer">

	<!-- display username -->	   
	<div class="span-15 left">
		<sec:authentication property="principal" var="principal"/>
			<c:choose>
		    	<c:when test="${principal=='anonymousUser'}">Guest User</c:when>
		   		<c:otherwise><span class="openidlink">&nbsp;</span>User:<c:out value="${principal.username}"/></c:otherwise>
		   	</c:choose> 
	</div>
		
	<!-- display copyright -->
	<div class="prepend-4 span-5 last">ESGF Copyright &copy; 2011</div>

</div>

