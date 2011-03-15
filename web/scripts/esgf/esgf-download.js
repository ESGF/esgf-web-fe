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
            			if(word.length > 16) {
            				abbreviation = word.slice(0,7) + '...' + word.slice(word.length-8,word.length-1);
            			}
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
				var selectedItem = $.tmplItem(this);
		    	var selectedDoc = selectedItem.data.doc;
		    	var selectedDocId = selectedDoc.id;
				$('input[name=' + selectedDocId + ']').toggle();
				
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
    
    $(".topLevel").live('change', function() {
    	var selectedItem = $.tmplItem(this);
    	var selectedDoc = selectedItem.data.doc;
    	var selectedDocId = selectedDoc.id;
    	$('input#' + replacePeriod(selectedDocId)).attr('checked', 'checked');
    	var selectedFileIds = selectedDoc.file_id;
    	for(var i=0;i<selectedFileIds.length;i++) {
    		
    		if(selectedDoc.service_type[i] == 'HTTPServer') {
    			if($('input#' + replacePeriod(selectedDocId)).is(':checked')) {
        			$('input#' + replacePeriod(selectedFileIds[i])).attr('checked', 'checked');
    			}
    			else {
    				$('input#' + replacePeriod(selectedFileIds[i])).attr('checked', 'unchecked');
    			}
    		}
    	}
    	
    });
    
    /*
	$('.topLevel').click(function () {
		$(this).parents('fieldset:eq(0)').find(':checkbox').attr('checked', this.checked);
	});
	*/
	
    $(".wgetAllChildren").live ('click', function (e){
    	
    	
    	
    	var selectedItem = $.tmplItem(this);
    	var selectedDoc = selectedItem.data.doc;
    	var selectedDocId = selectedDoc.id;
    	
    	
    	var selectedFileUrls = selectedDoc.file_url;
    	var selectedFileIds = selectedDoc.file_id;
    	var queryString = 'type=create&id=' + selectedDocId;
    
    	
    	 var ids   = new Array(); 
    	 var values = new Array();
    	    jQuery("input:checkbox:checked").each(function(){ 
    	         ids.push(this.id) ;
    	         values.push(this.value);
    	    });
    	    
    	
    	
    	for(var j=0;j<ids.length;j++) {
    		alert(ids[j]);
    	
    	
    	
    	}
    	
    	
    	//for(var i=0;i<selectedFileUrls.length;i++) {
    	for(var i=0;i<ids.length;i++) {
    		alert('pushing ' + values[i] + ' and ' + ids[i]);
    		if(selectedDoc.service_type[i] == 'HTTPServer') {
    			queryString += '&child_url=' + values[i] + '&child_id=' + ids[i];
    		}
    	}
    	
    	alert('queryString: ' + queryString);
    	//generate the wget
    	jQuery.ajax({
            url: '/esgf-web-fe/wgetproxy',
            data: queryString,
            type: 'POST',
            success: function() {download(selectedItem); },
            error: function() {alert("error http://localhost:8080/esgf-web-fe/wgetproxy");},
            dataType: 'text'
        }); 
    	
    	
    });
    

    
    $.extend( $.tmpl.tag, {
        "var": {
            open: "var $1;"
        }
    });

    

});

function download(selectedItem) {
	
	var selectedDocId = selectedItem.data.doc.id;
	window.location.href = 'http://localhost:8080/esgf-web-fe/scripts/esgf/' + 'wget_download_' + selectedDocId + '.sh';
	
	//delete the wget here (keep it a synchronous process)
	var queryString = 'type=delete&id=' + selectedDocId;
	
	//delete the wget
	jQuery.ajax({
        url: 'http://localhost:8080/esgf-web-fe/wgetproxy',
        data: queryString,
        type: 'POST',
        success: function() {},
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


function printObject(object) {
	var output = '';
	for (var property in object) {
	  output += property + ': ' + object[property]+'; ';
	}
	alert(output);
}