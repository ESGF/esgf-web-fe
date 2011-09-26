<%@ include file="/WEB-INF/views/common/include.jsp" %>

<h3 style="" id="form_title">Model Params sent</h3>
		<hr />
		<p>
		DatasetName: ${GoFormView_Dataset_Name}
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
       	