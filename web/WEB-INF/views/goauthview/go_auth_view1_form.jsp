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
	<c:choose>
	<c:when test='${GoFormView_Error=="autherror"}' >
        <b>Error authenticating with myProxy server.</b><br><br>
	You may have mistyped your password.<p>
	Please try logging in again<br><br>
        <c:out value="${GoFormView_ErrorMsg}" escapeXml="false"/>
        </c:when>
	</c:choose>
<form id="new_user_form" action="goauthview3" method="post" >
	<h3 style="" id="form_title">Globus Online Transfer</h3>
	<p> Your ESGF credentials are needed to access the data set on the server.  Please provide your ESGF Portal password.</p>

	<table id="adduser_table_id" class="adduser_table" >
		<tr>
			<td>
				<div class="adduser_properties" style=""><b>ESGF</b> Portal Password (for OpenID: ${GoFormView_openId} <c:out value="${GoFormView_SrcMyproxyUser}"/>)</div>
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
      	<input type="hidden" name="action" value="${pageContext.request.contextPath}/goview4" />
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
