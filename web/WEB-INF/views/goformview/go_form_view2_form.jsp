<c:choose>
<c:when test='${GoFormView_Error=="error"}' >
	<strong>Error: Globus Online could not be initialized.</strong>
<br><br>Please press back to correct the error, or try your  <a href="<c:url value="/live"/>">download</a> request again.
<br><br>
        <c:out value="${GoFormView_ErrorMsg}" escapeXml="false"/>
	</c:when>
	<c:otherwise>
<form id="new_user_form" action="goformview3" method="post">
		<h3 style="" id="form_title">Globus Online Transfer: Step 2 of 3</h3>
		
				<p>Please choose the source node(s) from where you'd like to move the data:</p>
				
             <table>
				<tr id="">
					<td>
						<div class="" style="">Source Endpoints</div> 
					<input style="margin-right:5px;" class="sourcenodes" id="thisnode" type="checkbox" name="sourcenodes" value="default" disabled checked>*This node* 
					<c:set var="j" value="0"/>
        					<c:forEach var="source" items="${GoFormView_SourceEndpoints}">
        					
        					<input style="margin-right:5px;" class="sourcenodes" id="${GoFormView_SourceEndpoints[j]}" type="checkbox" name="sourcenodes" value="${GoFormView_SourceEndpoints[j]}" >${GoFormView_SourceEndpoints[j]}
					<c:set var="j" value="${j+1}"/>
        					
       						</c:forEach>
       						
					
					</td>
					
				</tr>
				<c:set var="j" value="0"/>
        		<c:forEach var="source" items="${GoFormView_SourceEndpoints}">
        			<tr id="${GoFormView_SourceEndpoints[j]}" style="display:none;margin-top:5px;">
						<td>
							<div class="" style="">${GoFormView_SourceEndpoints[j]} MyProxy UserName</div> 
						</td>
						<td>
							<input type="text" class="text" value=""> 
						</td>
					</tr>
					<tr id="${GoFormView_SourceEndpoints[j]}" style="display:none;margin-top:5px;">
						<td>
							<div class="" style="">${GoFormView_SourceEndpoints[j]} MyProxy Password</div> 
						</td>
						<td>
							<input type="text" class="text" value=""> 
						</td>
					</tr>
        			<c:set var="j" value="${j+1}"/>
        					
       			</c:forEach>
				
			</table>
				
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
			<input type="hidden" name="id" id="${GoFormView_Dataset_Name}" value="${GoFormView_Dataset_Name}" />
                        <input type="hidden" name="usercertificate" id="${GoFormView_UserCertificate}" value="${GoFormView_UserCertificate}" />
                        <input type="hidden" name="gousername" id="${GoFormView_GOUsername}" value="${GoFormView_GOUsername}" />
                        <input type="hidden" name="srcmyproxyuser" id="${GoFormView_SrcMyproxyUser}" value="${GoFormView_SrcMyproxyUser}" />
                        <input type="hidden" name="srcmyproxypass" id="${GoFormView_SrcMyproxyPass}" value="${GoFormView_SrcMyproxyPass}" />
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

<script type="text/javascript">
$(function(){
	
	
	$('input.sourcenodes').click(function(){
		$(this).parent().find('input.sourcenodes').each(function(index) {
			var isChecked = $(this).attr('checked');
			var id = $(this).attr('id');
			if(isChecked) {
				if(id != 'thisnode') {
					$('tr#' + id).show();
				}
			} else {
				if(id != 'thisnode') {
					$('tr#' + id).hide();
				}
			}
		
		});
		
	});
	
	
	
});

</script>
