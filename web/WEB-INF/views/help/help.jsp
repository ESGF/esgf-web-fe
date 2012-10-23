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
		Welcome to the ESGF User Help Page!  Note that a more detailed guide for ESGF Users resides in the <a href="http://esgf.org" target="_blank">ESGF Wiki pages</a>.  
		For general questions, please email us at <a href="mailto:esgf-user@lists.llnl.gov">esgf-user@lists.llnl.gov</a>.
		</div>
		
		<div style='margin-bottom:15px'>
		<a href='http://www.esgf.org/wiki/fe-user-guide' style='font-weight:bold'>ESGF Web Interface User Guide</a>
		<br/>A complete guide to using the ESGF web portal user interface.
		</div>
		
		<div style='margin-bottom:15px'>
		<a href='http://www.esgf.org/wiki/ESGF_User_Guide' style='font-weight:bold'>ESGF Search User Guide</a>
		<br/>A short user guide to discovering data within ESGF.  The <a href='http://www.esgf.org/wiki/ESGF_Search_REST_API' style='font-weight:bold'>ESGF Search API documentation</a> is also available for developers interested in writing their own clients. 
		</div>
		
		<div style='margin-bottom:15px'>
		<a href='http://www.esgf.org/wiki/ESGF_Data_Download'  style='font-weight:bold'>Register and download data from ESGF</a>
		<br/>A description of extracting datasets from the ESGF portal.
		</div>
		
		<div style='margin-bottom:15px'>
		<a class="mailto" href="mailto:cmip5-helpdesk@stfc.ac.uk" style='font-weight:bold'>Scientific questions related to CMIP5</a>
		<br/>Send any CMIP5-related questions here.
		</div>
		
		<div style='margin-bottom:15px'>
		<a href='http://www.esgf.org/wiki/ESGF_User_FAQ' style='font-weight:bold'>User FAQ</a>
		<br/>Commonly asked question
		</div>
		
		
		</div>
		
			
		
	</ti:putAttribute>
	
	
</ti:insertDefinition>