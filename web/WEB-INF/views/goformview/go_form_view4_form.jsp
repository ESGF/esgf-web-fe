<c:choose>
<c:when test='${GoFormView_Error=="error"}' >
	There was an error in your request.  Please try your  <a href="<c:url value="/live"/>">download</a> request again. 
	</c:when>
	<c:otherwise>
	Success
	</c:otherwise>
</c:choose>