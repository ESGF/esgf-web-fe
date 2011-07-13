<%@ include file="/WEB-INF/views/common/include.jsp" %>

<sec:authentication property="principal" var="principal"/>

<div style="margin-top:20px;margin-bottom:20px;">
	<c:choose>
		<c:when test="${principal=='anonymousUser'}">
    		<div> <c:out value="${principal}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  		</c:when>
  		<c:otherwise>
      		<c:choose>
      			<c:when test="${principal.username=='https://pcmdi3.llnl.gov/esgcet/myopenid/jfharney'}">
  					<div style="margin-top:20px">
						<h2>
						usermanagment
						</h2>
					</div>
  				</c:when>
  				<c:otherwise>
  					<div> <c:out value="${principal.username}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  				</c:otherwise>
      		</c:choose>
  		</c:otherwise>
	</c:choose>      
</div>


