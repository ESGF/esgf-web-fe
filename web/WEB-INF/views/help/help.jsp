<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/help/help_style_search.jsp" %>
<%@ include file="/WEB-INF/views/help/help_script_search.jsp" %>

<ti:insertDefinition name="main-layout" >

    <ti:putAttribute name="extrastyle">
        <link rel="stylesheet" href='<c:url value="/styles/security.css" />' type="text/css">
    </ti:putAttribute>

    <ti:putAttribute name="main">
		<div class="span-18 prepend-3 last" style="min-height:500px;">
		
		<p/>
		
		<h1 style="margin-bottom:15px">ESGF Help</h1>
		
		<div style="margin-bottom:15px">
		Welcome to the ESGF User Help Page!  
		We have listed several resources below to improve the User experience.
		For a more detailed description of ESGF overall, please visit our <a href="http://esgf.org" target="_blank">Wiki</a>.  
		For general questions or feedback, please email us at <a href="mailto:esgf-user@lists.llnl.gov?subject=ESGF General Question">esgf-user@lists.llnl.gov</a>.
		</div>
		
		<c:set var="j" value="0"/>
		
		<c:forEach var="user" items="${Help_Subject_Ids}"> 
				
		<div style='margin-bottom:15px'>
		<a href='${Help_Header_Links[j]}' style='font-weight:bold' target="_blank">${Help_Header_Names[j]}</a>
		<br/>${Help_Descriptions[j]}		
		<br/>
		<img src='<c:url value="/images/help/moin-email.png" />' style="margin-right: 5px;"/><a href="mailto:esgf-user@lists.llnl.gov?subject=${Help_Emails[j]}">Have a question about the ${Help_Header_Names[j]}?</a>
		</div>
			<c:set var="j" value="${j+1}"/>
		</c:forEach> 
		</div>
		
		
		
		<script type="text/javascript">
		$(function(){

			
			
		});
		
		</script>
		
	</ti:putAttribute>
	
	
</ti:insertDefinition>