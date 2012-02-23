<%@ include file="/WEB-INF/views/common/include.jsp" %>

<div class="span-24 info last" >
  
  	<div class="prepend-2 span-20 info last">
  		<div style="color:red;font-size: 11px;margin-top:15px;">
  		Welcome to the beta test site for the CMIP5 distributed archive. 
  		This is <a href="http://esgf.org/wiki/ESGF_Transition">Phase I of IV</a> that transitions away from the ESG gateway-centric system based on gateway version 1.3.4 to the new ESGF peer-to-peer system.  
  		In this phase transition, some of the data may not be accessible due to not all data nodes have upgraded to the new software stack.
  		</div> 
  	</div>
  
	<%@ include file="/WEB-INF/views/welcome/left.jsp" %>
	
	<%@ include file="/WEB-INF/views/welcome/center.jsp" %>
	
	<%@ include file="/WEB-INF/views/welcome/right.jsp" %>
	
</div>
