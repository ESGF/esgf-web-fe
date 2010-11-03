/**
 * Author: fwang2@ornl.gov
 * 
 * Re-written menu, greatly simplified.
 * 
    HTML structure to use:

	<ul class="menu">
	<li> 
		<a href="#"> Project</a>
		<ul class="acitem">
			<li>
				<a href="..."> ipccc4 </a>
			</li>
			...
			<li>
				<a href="..."> test </a>
			</li>
			
		</ul>
	</li>
	...	
	
	</ul>
	

 */


$(function() {  
	        
        $('.acitem:not(:has(li.selected))').hide();
        
        // live event 
        // handle clicks on "project", "model" etc.
        $('ul.menu > li > a').live('click', function(e) {
        	e.stopImmediatePropagation();
        	$(this).toggleClass('active');
    		
        	$(this).siblings().find("li").toggle();
    		
        	if ($(this).siblings().is(':hidden')) {
        		$(this).siblings().slideToggle('fast');
        	}
    		
    		return false;
        });
        
        // handle subfacets
        $('.acitem > li').live('dbclick', function(e) {
        	e.stopPropagation();
        	var p1 = $(this).parent().parent()[0];
        	if ($(this).hasClass('none')) {
        		$(this).hide();
        	} else {
        		$(this).show();
        	}
        	return false;
        });
        
});
