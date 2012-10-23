<h3 style="" id="form_title">GlobusOnline Transfer Status</h3>

<c:choose>
<c:when test='${GoFormView_Error=="error"}' >
	<b>There was an error in your request.  Please try your  <a href="<c:url value="/live"/>">download</a> request again.</b><br><br>
	If you need to contact your administrator please send the information below:
        <c:out value="${GoFormView_ErrorMsg}" escapeXml="false"/>
	</c:when>
	<c:otherwise>
	<b>Your Globus Online Transfer has been submitted</b>
        <br>
        (<c:out value="${GoFormView_TransferInfo1}" />)
        <br><br>
        <c:out value="${GoFormView_TransferInfo2}" />
        <br><br>
        <a href="https://www.globusonline.org/xfer/ViewTransfers">You can monitor your transfer here. </a><br><br>You will receive email notifications from Globus Online when your transfer is complete.
	</c:otherwise>
</c:choose>
