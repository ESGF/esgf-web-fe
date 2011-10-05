<h3 style="" id="form_title">GlobusOnline Step 4/4</h3>

<c:choose>
<c:when test='${GoFormView_Error=="error"}' >
	There was an error in your request.  Please try your  <a href="<c:url value="/live"/>">download</a> request again. 
	</c:when>
	<c:otherwise>
<!--
<form id="new_user_form" action="goformview4" method="post" >
<h3 style="" id="form_title">Form 4 Info</h3>
      		<input type="hidden" name="usercertificate" id="${GoFormView_UserCertificate}" value="${GoFormView_UserCertificate}" />
  		<input type="hidden" name="id" id="${GoFormView_Dataset_Name}" value="${GoFormView_Dataset_Name}" />
  		<input type="hidden" name="target" id="${GoFormView_DestTargetPath}" value="${GoFormView_DestTargetPath}" />
  		<input type="hidden" name="gousername" id="${GoFormView_GOUsername}" value="${GoFormView_GOUsername}" />
                <input type="hidden" name="srcmyproxyuser" id="${GoFormView_SrcMyproxyUser}" value="${GoFormView_SrcMyproxyUser}" />
                <input type="hidden" name="srcmyproxypass" id="${GoFormView_SrcMyproxyPass}" value="${GoFormView_SrcMyproxyPass}" />
  		<input type="hidden" name="endpointinfo" id="${GoFormView_DestEndpointInfo}" value="${GoFormView_DestEndpointInfo}" />
		<c:set var="j" value="0"/>
 		<c:forEach var="group" items="${GoFormView_File_Names}">
 			<input type="hidden" name="child_id" id="${GoFormView_File_Names[j]}" value="${GoFormView_File_Names[j]}" />
 			<input type="hidden" name="child_url" id="${GoFormView_File_Urls[j]}" value="${GoFormView_File_Urls[j]}" />
 			<c:set var="j" value="${j+1}"/>
		</c:forEach>
		<input style="" class="adminbutton" type="submit" value="next">
   	</p>
</form>
-->
	TODO: Properly handle success/failure
	</c:otherwise>
</c:choose>
