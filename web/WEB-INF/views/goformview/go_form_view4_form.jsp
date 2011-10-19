<h3 style="" id="form_title">GlobusOnline Transfer Status</h3>

<c:choose>
<c:when test='${GoFormView_Error=="error"}' >
	There was an error in your request.  Please try your  <a href="<c:url value="/live"/>">download</a> request again. 
        <c:out value="${GoFormView_ErrorMsg}" />
	</c:when>
	<c:otherwise>
	Your Globus Online Transfer has been started!
        <br><br>
        <c:out value="${GoFormView_TransferInfo1}" />
        <br><br>
        <c:out value="${GoFormView_TransferInfo2}" />
        <br><br>
        <a href="https://www.globusonline.org/xfer/ViewTransfers">View Globus Online Transfer</a>
	</c:otherwise>
</c:choose>
