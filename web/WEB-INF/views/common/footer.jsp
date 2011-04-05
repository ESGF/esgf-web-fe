<%@ include file="/WEB-INF/views/common/include.jsp" %>

<div class="span-24 last footer">

	<div class="span-15 left">
		<!-- display username -->	 
		<div class="footerleft">
			<p>
			<sec:authentication property="principal" var="principal"/>
			<c:choose>
		    	<c:when test="${principal=='anonymousUser'}">Guest User</c:when>
		   		<c:otherwise>User: <c:out value="${principal.username}"/></c:otherwise>
		   	</c:choose>
		   	</p> 
		</div>
	</div>
	
	<!-- display copyright -->
	<div class="prepend-3 span-6 last">
		<div class="footerright">
        	<a href="#">Privacy Policy</a> | <a href="#">Terms of Use</a>
        </div>
	</div>
</div>  
	