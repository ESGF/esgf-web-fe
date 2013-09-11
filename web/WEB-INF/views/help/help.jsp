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
		
		<div style='margin-bottom:15px'>
		<a href='http://askbot.esgf.org/questions/' target="_blank" style='font-weight:bold'>Askbot</a>
		<br/>Find, tag or submit a question to our "askbot" forum.  Askbot is an open source question and answer 
		oriented Internet forum similar to Stack Overflow.  Questions are typically resolved much more quickly in this forum than through email.
		</div>
		
		<!--  
		
		<div style='margin-bottom:15px'>
		<a href='http://www.esgf.org/wiki/ESGF_User_FAQ' style='font-weight:bold'>ESGF User FAQ</a>
		<br/>Commonly asked questions from the ESGF user community.
		<br/><span id="userFAQ_question" style="cursor:pointer"><img src='<c:url value="/images/help/moin-email.png" />' style="margin-right: 5px;" alt="email icon" /><a>Have a question about the User FAQs?</a></span>
		</div>
		
		-->
		
		<div style='margin-bottom:15px'>
		<a href='http://www.esgf.org/wiki/ESGF_Data_Download'  style='font-weight:bold'>User registration at ESGF</a>
		<br/>A quick tutorial on how to register as a user and join scientific working groups.
		<br/><span id="registration_question" style="cursor:pointer"><img src='<c:url value="/images/help/moin-email.png" />' style="margin-right: 5px;" alt="email icon" /><a>Have a question about registration?</a></span>
		</div>
		
		<div style='margin-bottom:15px'>
		<a href='http://www.esgf.org/wiki/ESGF_Data_Download'  style='font-weight:bold'>Data download from ESGF</a>
		<br/>A description of how to extract data files from the ESGF portal.
		<br/><span id="download_question" style="cursor:pointer"><img src='<c:url value="/images/help/moin-email.png" />' style="margin-right: 5px;" alt="email icon" /><a>Have a question about downloading data?</a></span>
		</div>
		
		<div style='margin-bottom:15px'>
		<a href='http://www.esgf.org/wiki/fe-user-guide' style='font-weight:bold'>ESGF Web Interface User Guide</a>
		<br/>A complete guide to using the ESGF web portal user interface.
		<br/><span id="general_question" style="cursor:pointer"><img src='<c:url value="/images/help/moin-email.png" />' style="margin-right: 5px;" alt="email icon" /><a>Have a question about using the UI?</a></span>
		</div>
		
		<div style='margin-bottom:15px'>
		<a href='http://www.esgf.org/wiki/ESGF_Web_Search_User_Guide' style='font-weight:bold'>ESGF Search User Guide</a>
		<br/>A short user guide to discovering data within ESGF.  The <a href='http://www.esgf.org/wiki/ESGF_Search_REST_API' style='font-weight:bold'>ESGF Search API documentation</a> is also available for developers interested in writing their own clients. 
		<br/><span id="searchAPI_question" style="cursor:pointer"><img src='<c:url value="/images/help/moin-email.png" />' style="margin-right: 5px;" alt="email icon" /><a>Have a question about the search API?</a></span>
		</div>
		
		<div style='margin-bottom:15px'>
		<a href='https://www.globusonline.org/esgf/' style='font-weight:bold'>Globus Online User Guide</a>
		<br/>A complete guide for using Globus Online for data movement within ESGF.  
		<br/><span id="go_question" style="cursor:pointer"><img src='<c:url value="/images/help/moin-email.png" />' style="margin-right: 5px;" alt="email icon" /><a>Have a question about Globus Online?</a></span>
		</div>
		
		
		<div style='margin-bottom:15px'>
		<a class="mailto" href="mailto:cmip5-helpdesk@stfc.ac.uk" style='font-weight:bold'>Post scientific questions related to CMIP5</a>
		<br/>Send any CMIP5-related questions here.
		<br/><span id="CMIP5_question" style="cursor:pointer"><img src='<c:url value="/images/help/moin-email.png" />' style="margin-right: 5px;" alt="email icon"/><a>Have a question related to CMIP5?</a></span>
		</div>
		
		
		</div>
		
		<script type="text/javascript">
		$(function(){
			$('span#general_question').click(function() {
				location.href="mailto:esgf-user@lists.llnl.gov?subject=ESGF Web User Interface Question";
			});

			$('span#registration_question').click(function() {
				location.href="mailto:esgf-user@lists.llnl.gov?subject=Registration Question";
			});
			
			$('span#download_question').click(function() {
				location.href="mailto:esgf-user@lists.llnl.gov?subject=Download Question";
			});
			
			$('span#searchAPI_question').click(function() {
				location.href="mailto:esgf-user@lists.llnl.gov?subject=Search API Question";
			});	
			
			$('span#userFAQ_question').click(function() {
				location.href="mailto:esgf-user@lists.llnl.gov?subject=User FAQ Question";
			});

			$('span#go_question').click(function() {
				location.href="mailto:esgf-user@lists.llnl.gov?subject=Globus Online Question";
			});
			
			$('span#CMIP5_question').click(function() {
				location.href="mailto:cmip5-helpdesk@stfc.ac.uk?subject=CMIP5 Question";
			});
			

			
			
		});
		
		</script>
		
	</ti:putAttribute>
	
	
</ti:insertDefinition>