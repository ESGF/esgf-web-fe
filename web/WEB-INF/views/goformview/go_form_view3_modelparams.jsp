<%@ include file="/WEB-INF/views/common/include.jsp" %>

<h3 style="" id="form_title">Model Params sent</h3>
		<hr />
		<p>
		DatasetName: ${GoFormView_Dataset_Name}
		</p>

		<hr />
		<p>
		GO Username: ${GoFormView_GOUsername}
		</p>

                <hr />
                <p>
                Source Myproxy Username: ${GoFormView_SrcMyproxyUser}
                </p>

                <hr />
                <p>
                Source Myproxy Password: *****
                </p>

		<hr />
		<p>
		User Certificate: ${GoFormView_UserCertificate}
		</p>

		<hr />
		<p>
		Destination Endpoint: ${GoFormView_DestEndpointInfo}
		</p>

		<hr />
		<p>
		Destination Target Path: ${GoFormView_DestTargetPath}
		</p>
		
		<hr />
		<p>
		File Names:
		</p>
		<c:set var="j" value="0"/>
        <c:forEach var="group" items="${GoFormView_File_Urls}">
        	
        		${GoFormView_File_Urls[j]}
        	
            <c:set var="j" value="${j+1}"/>
       	</c:forEach>
		<hr />
		<p>
		File Urls:
		</p>
		<c:set var="j" value="0"/>
        <c:forEach var="group" items="${GoFormView_File_Urls}">
        	
        		<p>${GoFormView_File_Urls[j]}</p>
        	
            <c:set var="j" value="${j+1}"/>
       	</c:forEach>
<hr />
<p>
Endpoint Infos:
<c:set var="j" value="0"/>
<c:forEach var="group" items="${GoFormView_EndpointInfos}">
	${GoFormView_EndpointInfos[j]}
	<c:set var="j" value="${j+1}"/>
</c:forEach>
</p>
