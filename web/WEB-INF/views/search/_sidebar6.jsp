<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/search/_facet_select_tbl.jsp" %>


<div class="span-6 last">

	<div class="l2contentleftbox">        
		<!--  current selection -->
		  
		<div class="round-box">
		    <div class="round-header round-top"> Current Selections</div> 
		    <div id="current-selection" class="round-content"></div>
		
		</div> 
	</div>      
	
</div>




<div class="span-6 last" style="padding-bottom:10px">
	<div class="l2contentleftbox"> 
		<ul class="ul_class" >
			<li class="li_class_title">
	   			Search Categories
			</li>
		</ul>
		 
		<table>
			<tbody id="facetList">
		
	    	</tbody>
		</table>
		
		
	</div>
	
</div>