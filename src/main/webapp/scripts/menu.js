/**
 * Author: fwang2@ornl.gov
 * 
 * Re-written menu, greatly simplified.
 * 
    HTML structure to use:

	<ul class="menu">
	<li> 
		<a href="#"> sub menu </a>
		<ul>
			<li><a href="..."> link </a></li>
		</ul>
	</li>
	...
	
	
	
	</ul>
	

 */


$(function() {  
	        
        $('.acitem:not(:has(li.selected))').hide();
        $('li:has(li.selected)', this).addClass("expand");
        $('li.expand').show();
        

        
        $('ul.menu > li > a').bind('click', function(e) {
        	e.stopImmediatePropagation();
        	alert("got it");
        	$(this).
        		toggleClass('active');
        	$(this).siblings().slideToggle('fast');
        	
        	return false;
        });
       
});
