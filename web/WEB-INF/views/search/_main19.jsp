<%@ include file="/WEB-INF/views/search/_overlay.jsp" %>
<%@ include file="/WEB-INF/views/search/_select_tbl.jsp" %>

<div id="myTabs">

    <ul>

    <li><a href="#search-results"> Results</a></li>

    <li><a href="#carts"> Selections </a></li>

    </ul>

    <div id="search-results"> </div>

    <div id="carts"> <table>
    <tbody id="datasetList">

    </tbody>
    </table>
    </div>
</div>

<script type="text/javascript">

    $(function(){
        $('#myTabs').tabs();
        
        
        /*Facet overlay
        */
      //scroll wheel for facet overlay 
        $(".scrollable").scrollable({ vertical: true, mousewheel: true });

        // For the facet overlay 
        $("span#facet a[rel]").overlay({
        		 
        		mask: {opacity: 0.5, color: '#000'},
        		effect: 'apple',
        		left: "5%",
        		top: "2%",
        		
        		onBeforeLoad: function() {
        		
        			$('.apple_overlay').css({'width' : '700px'});
        			
        		},

        		onLoad: function() {
        			 //radio buttons for sorting facets 
        		    $("#facetSort").buttonset();
        			$(".overlay_header").show();
        			$(".overlay_content").show();
        			$(".overlay_footer").show();
        			$(".overlay_border").show();
        	},
        	
        	onClose: function() {
        			$(".overlay_header").hide();
        			$(".overlay_content").hide();
        			$(".overlay_footer").hide();
        			$(".overlay_border").hide();
        		}
        	
        });  

         //event trigger for facet sorting buttons 
        $("input[name='sorter']").change(function() {
            if ($("input[name='sorter']:checked").val() == 'sortbyabc') {
                Manager.sortType = 'sortbyabc';
                Manager.doRequest(0);
            } else {
                Manager.sortType = 'sortbycount';
                Manager.doRequest(0);
            }
        });
    });

</script>
