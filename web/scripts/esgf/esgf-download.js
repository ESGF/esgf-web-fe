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
        			},
            		abbreviate : function (word) {
            			var abbreviation = word;
            			/*
            			if(word.length > 16) {
            				abbreviation = word.slice(0,7) + '...' + word.slice(word.length-8,word.length-1);
            			}
            			*/
            			return abbreviation;
            		},
            		addOne: function(num) {
            			return (num+1);
            		},
            		sizeConversion : function(size) {
            			var convSize;
            			if(size == null) {
            				convSize = 'N/A';
            			} else {
            				var sizeFlt = parseFloat(size,10);
                			if(sizeFlt > 1000000000) {
                				var num = 1000000000;
                				convSize = (sizeFlt / num).toFixed(2) + ' GB';
                			} else if (sizeFlt > 1000000) {
                				var num = 1000000;
                				convSize = (sizeFlt / num).toFixed(2) + ' MB';
                			} else {
                				var num = 1000;
                				convSize = (sizeFlt / num).toFixed(2) + ' KB';
                			}
            			}
            			return convSize;
            		} 
        			
        		}
            )
            .appendTo("#datasetList")
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
            success: function() {download(selectedItem);},
            error: function() {alert("error http://localhost:8080/esgf-web-fe/wgetproxy");},
            dataType: 'text'
        }); 
    	
    	
    });
    

    $("#demo img[title]").tooltip();
    $("#demo2 div[id]").tooltip();
    
    $.extend( $.tmpl.tag, {
        "var": {
            open: "var $1;"
        }
    });

});

function download(selectedItem) {
	
	var selectedDocId = selectedItem.data.doc.id;
	
	//change me
	jQuery('<form action="'+ 'http://localhost:8080/esgf-web-fe/scripts/esgf/' + 'wget_download_' + selectedDocId + '.sh' +'" method="'+ 'get' +'">'+''+'</form>').appendTo('body').submit().remove();

	//delete the wget here (keep it a synchronous process)
	var queryString = 'type=delete&id=' + selectedDocId;
	
	//generate the wget
	jQuery.ajax({
        url: 'http://localhost:8080/esgf-web-fe/wgetproxy',
        data: queryString,
        type: 'POST',
        success: function() {alert('success');},
        error: function() {alert("error http://localhost:8080/esgf-web-fe/wgetproxy");},
        dataType: 'text'
    }); 
}

/*
 * This function is used primarily to avoid annoying errors that occur with strings that have periods in them
 */
function replacePeriod(word)
{
	var newWord = word.replace(/\./g,"_");
	return newWord;
}