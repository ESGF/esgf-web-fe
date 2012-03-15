<%@ include file="/WEB-INF/views/common/include.jsp" %>
<c:choose>
<c:when test='${GoFormView_Error=="error"}' >
	<strong>Oops, we could not start the download just yet.</strong>
<br><br>
        <c:out value="${GoFormView_ErrorMsg}" escapeXml="false"/>
<br><br>Press back to correct the error, or try your  <a href="<c:url value="/live"/>">download</a> request again.
<br><br>
	</c:when>
	<c:otherwise>
<form id="new_user_form" action="goformview2" method="post" >
	<h3 style="" id="form_title">Globus Online Transfer: Step 1 of 2</h3>
        <p>If you don't have a
        <a href="http://www.globusonline.org">Globus Online</a>
        account, you can sign-up for one at
        <a target="_blank" href="https://www.globusonline.org/SignUp">https://www.globusonline.org/SignUp</a>. </p>
        <p>If you are not familiar with doing Globus Online transfers
        in ESG, or this is your first time, please read this
        <a target="_blank" href="http://www.esgf.org/wiki/ESGF_GO">documentation</a>
        to be sure that your account is setup properly and that this transfer can work.</p>

	<table id="adduser_table_id" class="adduser_table" >
		<tr id="userName_input">
			<td>
				<div class="adduser_properties" style=""><b>Globus Online</b> Username</div> 
			</td>
			<td>
				<input type="text" class="text" id="goUserName" name="goUserName" value=""> 
			</td>
		</tr>
		<tr>
			<td>
				<div class="adduser_properties" style=""><b>ESGF</b> Portal Password</div> 
			</td>
			<td>
				<input type="password" class="text" id="myProxyUserPass" name="myProxyUserPass" value=""> 
			</td>
		</tr>
    </table>
<!--
    <p>
    <input type="checkbox" name="goEmail" value="goEmail" /> Email when transfer starts<br />
    </p>
-->    
    <p>
      	<%-- Input params to go_form2 
      	goUserName -> Globus Online User Name
      	myProxyUserPass -> userpass of the source myproxy
	GoFormView_Myproxy_Server -> OpenID matching MyProxy server 
      	id -> DatasetName
      	child_id[] -> FileNames
      	child_url[] -> FileUrls
      	--%>
      	<input type="hidden" name="id" id="${GoFormView_Dataset_Name}" value="${GoFormView_Dataset_Name}" />
      	<input type="hidden" name="GoFormView_SrcMyproxyUser" value="${GoFormView_SrcMyproxyUser}" />
      	<input type="hidden" name="GoFormView_Myproxy_Server" value="${GoFormView_Myproxy_Server}" />
	<c:set var="j" value="0"/>
      	<c:forEach var="group" items="${GoFormView_File_Names}">
      		<input type="hidden" name="child_id" id="${GoFormView_File_Names[j]}" value="${GoFormView_File_Names[j]}" />
      		<input type="hidden" name="child_url" id="${GoFormView_File_Urls[j]}" value="${GoFormView_File_Urls[j]}" />
      			
	  		<c:set var="j" value="${j+1}"/>
     	</c:forEach>
	  	
     	<input style="" class="adminbutton" type="submit" value="next">
	</p>
</form>
	</c:otherwise>
</c:choose>
