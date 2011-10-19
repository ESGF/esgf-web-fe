<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/search/_facet_select_tbl.jsp" %>


<div class="span-6 last">
	<div class="l2contentleftbox">
		<ul class="ul_class">
			<li class="li_class_title">
				Current Selections
			</li>
		</ul>	
		<div id="current-selection" class="round-content"></div>
	</div>
	
</div>

<!-- In case I need to reset the localStorage (without clearing cookies  -->
<div class="span-6 last" style="padding-bottom:10px;padding-left:10px">
	<a href="#" class="reset"  style="display:none">Reset LocalStorage</a>
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

<script type="text/javascript">

$(function(){

});

</script>