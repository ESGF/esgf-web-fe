<c:choose>
<c:when test='${GoFormView_Error=="error"}' >
	<strong>Oops, we could not start the download just yet.</strong>
<br><br>
        <c:out value="${GoFormView_ErrorMsg}" escapeXml="false"/>
<br><br>Press back to correct the error, or try your  <a href="<c:url value="/live"/>">download</a> request again.
<br><br>
	</c:when>
	<c:otherwise>
<form id="new_user_form" action="goformview4" method="post">
		<h3 style="" id="form_title">Globus Online Transfer: Step 2 of 2</h3>
                <p>Please choose where you'd like to move the data:</p>
			<table>
				
				<tr id="">
					<td>
						<div class="" style="">Destination Endpoint</div> 
					</td>
					<td>
						<select name="endpointdropdown">
							<c:set var="j" value="0"/>
        					<c:forEach var="group" items="${GoFormView_Endpoints}">
        						<option value="${GoFormView_Endpoints[j]}">${GoFormView_Endpoints[j]}</option>
            					<c:set var="j" value="${j+1}"/>
       						</c:forEach>
						</select>
					</td>
				</tr>
				<tr id="">
					<td>
						<div class="" style="">Target Endpoint Path Location</div> 
					</td>
					<td>
						<input type="text" class="text" id="target" name="target" value=""> 
					</td>
				</tr>
                                <tr id="">
                                        <td></td>
                                        <td>*nix: /tmp/ (leading slash
					indicates an absolute path, omitting it
					is relative to home
					directory)<br>Windows: temp\
                                        </td>
                                            
			</table>
			<p>
			<%-- Input params to go_form3 
		      	endpointdropdown -> dropdown box for available endpoints
		      	target -> text box representing the target endpoint path
		      	id -> DatasetName
		      	child_id[] -> FileNames
		      	child_url[] -> FileUrls
      		--%>
                        <input type="hidden" name="usercertificate" id="${GoFormView_UserCertificate}" value="${GoFormView_UserCertificate}" />
			<input type="hidden" name="id" id="${GoFormView_Dataset_Name}" value="${GoFormView_Dataset_Name}" />
                        <input type="hidden" name="gousername" id="${GoFormView_GOUsername}" value="${GoFormView_GOUsername}" />
                        <input type="hidden" name="srcmyproxyuser" id="${GoFormView_SrcMyproxyUser}" value="${GoFormView_SrcMyproxyUser}" />
                        <input type="hidden" name="srcmyproxypass" id="${GoFormView_SrcMyproxyPass}" value="${GoFormView_SrcMyproxyPass}" />
                        <input type="hidden" name="srcmyproxyserver" id="${GoFormView_Myproxy_Server}" value="${GoFormView_Myproxy_Server}" />
			<c:set var="j" value="0"/>
			<c:forEach var="group" items="${GoFormView_File_Names}">
				<input type="hidden" name="child_id" id="${GoFormView_File_Names[j]}" value="${GoFormView_File_Names[j]}" />
				<input type="hidden" name="child_url" id="${GoFormView_File_Urls[j]}" value="${GoFormView_File_Urls[j]}" />
                                <c:set var="j" value="${j+1}"/>
			</c:forEach>

			<c:set var="j" value="0"/>
			<c:forEach var="group" items="${GoFormView_EndpointInfos}">
                                <input type="hidden" name="endpointinfos" id="${GoFormView_EndpointInfos[j]}" value="${GoFormView_EndpointInfos[j]}" />
				<c:set var="j" value="${j+1}"/>
			</c:forEach>
	      		<input style="" class="adminbutton" type="submit" value="next">
	      	</p>
		</form>
	</c:otherwise>
</c:choose>
