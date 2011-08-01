<%@ include file="/WEB-INF/views/common/include.jsp" %>

<sec:authentication property="principal" var="principal"/>

<div style="margin-top:20px;margin-bottom:20px;">
	<c:choose>
		<c:when test="${principal=='anonymousUser'}">
    		<div> <c:out value="${principal}"/> anonymousUser IS NOT AUTHORIZED TO VIEW THIS PAGE (usermanagementcontent) </div>
  		</c:when>
  		<c:otherwise>
      		<c:choose>
      			<c:when test="${principal.username=='https://pcmdi3.llnl.gov/esgcet/myopenid/banks12'}">
  					<div style="margin-top:20px">
						<h2>
						usermanagment
						<c:out value="${ManageUsers_USER}"/>
						</h2>
					</div>
  				</c:when>
  				<c:otherwise>
  					<div> <c:out value="${principal.username}"/> Eddy IS NOT AUTHORIZED TO VIEW THIS PAGE (usermanagementcontent) </div>
  				</c:otherwise>
      		</c:choose>
  		</c:otherwise>
	</c:choose>      
</div>


