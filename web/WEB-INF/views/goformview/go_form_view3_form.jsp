<form id="new_user_form" action="goformview4" method="post" >
<h3 style="" id="form_title">Globus Online Transfer: Step 3 of 3</h3>

<c:choose>
<c:when test='${GoFormView_GOConnect=="true"}' >
The endpoint that you've selected is a Globus Connect Endpoint that
does not require activation.<br><br>
Please click Next to continue.
	</c:when>
	<c:otherwise>

<table id="adduser_table_id" class="adduser_table" >  
        <tr>
          <td colspan="2">
          Please provide your MyProxy login information
	  for the Target Endpoint: <c:out value="${GoFormView_DestMyproxyServer}" escapeXml="false"/>
          </td>
        </tr>
	<tr id="userName_input">
		<td>
			<div class="adduser_properties" style="">Target MyProxy Username</div>
		</td>
		<td>
			<input type="text" class="text" name="myproxyUserName" id="myproxyUserPass" value=""> 
		</td>
	</tr>
	<tr>
		<td>
			<div class="adduser_properties" style="">Target MyProxy Password</div>
		</td>
		<td>
			<input type="password" class="text" name="myproxyUserPass" id="myproxyUserPass" value=""> 
		</td>
	</tr>
   </table>
	</c:otherwise>
</c:choose>
   	
   <p>
   		<%-- Input params to go_form4 
		      	myproxyUserName -> username for the myproxy target
		      	myproxyUserPass -> userpass for the myproxy target
		      	id -> DatasetName
		      	child_id[] -> FileNames
		      	child_url[] -> FileUrls
      		--%>
  		<input type="hidden" name="usercertificate" id="${GoFormView_UserCertificate}" value="${GoFormView_UserCertificate}" />
  		<input type="hidden" name="id" id="${GoFormView_Dataset_Name}" value="${GoFormView_Dataset_Name}" />
  		<input type="hidden" name="target" id="${GoFormView_DestTargetPath}" value="${GoFormView_DestTargetPath}" />
  		<input type="hidden" name="gousername" id="${GoFormView_GOUsername}" value="${GoFormView_GOUsername}" />
                <input type="hidden" name="srcmyproxyuser" id="${GoFormView_SrcMyproxyUser}" value="${GoFormView_SrcMyproxyUser}" />
                <input type="hidden" name="srcmyproxypass" id="${GoFormView_SrcMyproxyPass}" value="${GoFormView_SrcMyproxyPass}" />
                <input type="hidden" name="srcmyproxyserver" id="${GoFormView_Myproxy_Server}" value="${GoFormView_Myproxy_Server}" />
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
