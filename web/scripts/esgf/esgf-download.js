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
            
            $( "#cartTemplate").tmpl(arr, { 
        			replacePeriods : function (word) {
        				return replacePeriod(word);
        			}
        		}
            )
            .appendTo("#datasetList")
            //.find( "a").click(function() {alert('clicka');})
			.find( "a.showAllChildren" ).click(function() {
				var id = $(this).parent().attr("id").replace(/\./g,"_");
				$('tr.rows_'+id).toggle();//.css('background-color','yellow');
				if(this.innerHTML === "Expand") {
					this.innerHTML="Collapse";
				} else {
					this.innerHTML="Expand";
				}
				
			});
        }


    });
    
    $(".wgetAllChildren").live ('click', function (){
    	
    	var selectedItem = $.tmplItem(this);
    	var selectedDoc = selectedItem.data.doc;
    	var selectedDocId = selectedDoc.id;
    	var selectedChildUrls = selectedDoc.child_dataset_url;
    	var selectedChildIds = selectedDoc.child_dataset_id;
    	var queryString = 'type=create&id=' + selectedDocId;
    	for(var i=0;i<selectedChildUrls.length;i++) {
    		queryString += '&child_url=' + selectedChildUrls[i] + '&child_id=' + selectedChildIds[i];
    	}
    	
    	//generate the wget
    	jQuery.ajax({
            url: 'http://localhost:8080/esgf-web-fe/wgetproxy',
            data: queryString,
            type: 'POST',
            success: function() {download(selectedDocId);},
            error: function() {alert("error http://localhost:8080/esgf-web-fe/wgetproxy");},
            dataType: 'text'
        }); 
    	
    	
    });
    

});

function download(selectedDocId) {
	//change me
	jQuery('<form action="'+ 'http://localhost:8080/esgf-web-fe/scripts/esgf/' + 'wget_download_' + selectedDocId + '.sh' +'" method="'+ 'get' +'">'+''+'</form>').appendTo('body').submit().remove();

	//delete the wget here (keep it a synchronous process)
	
}

/*
 * This function is used primarily to avoid annoying errors that occur with strings that have periods in them
 */
function replacePeriod(word)
{
	var newWord = word.replace(/\./g,"_");
	return newWord;
}