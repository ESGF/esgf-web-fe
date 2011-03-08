/**
 * fwang2@ornl.gov
 */

$(document).ready( function() {
    $('#myTabs').bind('tabsselect', function(event, ui) {
        if (ui.index == 1) {
            $("#datasetList").empty();
            // selection tab
            LOG.debug("Selection tab");
            // convert object to array
            var arr = ESGF.util.toArray(ESGF.search.selected);
            //need a function that replaces periods in the name of the dataset (events in jquery cannot access elements that have these)
            $( "#cartTemplate").tmpl(arr, 
        		{ 
        			replacePeriods : function (word) {
        				var newWord = word.replace(/\./g,"_");
        				return newWord;
        			}
        		}
            )
            .appendTo("#datasetList")
            // Add click handler
			.find( "a.showChildren" ).click(function() {
				var id = $(this).parent().attr("id").replace(/\./g,"_");
				$('tr.rows_'+id).toggle();
			})
			
            
        }


    });
    
   
});