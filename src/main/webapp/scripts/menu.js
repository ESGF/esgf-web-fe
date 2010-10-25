/* 
HTML structure to use:

	<ul class="menu optional">
	<li> 
		<a href="#"> sub menu </a>
		<ul>
			<li><a href="..."> link </a></li>
		</ul>
	</li>
	...
	</ul>
	

Copyright 2007-2010 by Marco van Hylckama Vlieg

Extended - by fwang2

	- disabled class for sub-menu
	- onclick event for subfacet

*/


jQuery.fn.initMenu = function() {  
	//alert("initMenu");
    return this.each(function(){
        var theMenu = $(this).get(0);
        $('.acitem:not(:has(li.selected))').hide();
        $('li:has(li.selected)', this).addClass("expand");
        $('li.expand').show();
        
        
        $('ul.acitem > li > a').bind('click', function(e) {
        	
        	/*
        	var facetkey = $("#entry_key", this).html().trim();
        	var facetvalue = $("#subfacet_label", this).html().trim();
        	//var queryString= facetkey + '=' + facetvalue + '&offset=0' + 
        	//		'&text='+ '&limit=10';
        	//var queryString = 'project:EOSDIS';
        	var queryString = 'west_degrees' + '=' + -111.0;
        	alert("pre queryString: " + queryString);
            $.ajax({
        		type: "POST",
        		//url: '/esg-web/search.htm',
        		url: 'http://localhost:8983/solr',
        		data: queryString,
        		success: function(data) {
        			//alert("success");
        			//alert("data\n   " + data);
        			$("#search_results").html(data);
        		}
        	});
            alert("post queryString: " + queryString);
            */
        	return true;
        });
        
        $('ul.menu > li > a').bind('click', function(e) {
            
            e.stopImmediatePropagation();
            var theElement = $(this).next();
            var parent = this.parentNode.parentNode;
            if ($(parent).hasClass('noaccordion')) 
            {
                if (theElement[0] === undefined) {
                    window.location.href = this.href;
                }
                $(theElement).slideToggle('fast', function() {
                    if ($(this).is(':visible')) {
                        $(this).prev().addClass('active');
                    }
                    else {
                        $(this).prev().removeClass('active');
                    }    
                });
                return false;
            }
            else 
            { // with accordion
                if (theElement.hasClass('acitem') && theElement.is(':visible')) 
                {
                    if($(parent).hasClass('collapsible')) {
                        $('.acitem:visible', parent).first().slideUp('normal', 
                        function() {
                            $(this).prev().removeClass('active');
                        });
                        return false;  
                    }
                    return false;
                }
                
                if(theElement.hasClass('acitem') && !theElement.is(':visible')) 
                {
                    $('.acitem:visible', parent).first().slideUp('normal', function() {
                         $(this).prev().removeClass('active');
                    });
                    
                    theElement.slideDown('normal', function() {
                         $(this).prev().addClass('active');
                    });
                    return false;
                }
            }
    	}); 
    });
};
$(document).ready(function() {$('.menu').initMenu();});