/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * ÒLicensorÓ) hereby grants to any person (the ÒLicenseeÓ) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organizationÕs name.  If the Software is protected by a proprietary
 * trademark owned by Licensor or the Department of Energy, then derivative
 * works of the Software may not be distributed using the trademark without
 * the prior written approval of the trademark owner.
 *
 * 2. Neither the names of Licensor nor the Department of Energy may be used
 * to endorse or promote products derived from this Software without their
 * specific prior written permission.
 *
 * 3. The Software, with or without modification, must include the following
 * acknowledgment:
 *
 *    "This product includes software produced by UT-Battelle, LLC under
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.Ó
 *
 * 4. Licensee is authorized to commercialize its derivative works of the
 * Software.  All derivative works of the Software must include paragraphs 1,
 * 2, and 3 above, and the DISCLAIMER below.
 *
 *
 * DISCLAIMER
 *
 * UT-Battelle, LLC AND THE GOVERNMENT MAKE NO REPRESENTATIONS AND DISCLAIM
 * ALL WARRANTIES, BOTH EXPRESSED AND IMPLIED.  THERE ARE NO EXPRESS OR
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE,
 * OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, COPYRIGHT,
 * TRADEMARK, OR OTHER PROPRIETARY RIGHTS, OR THAT THE SOFTWARE WILL
 * ACCOMPLISH THE INTENDED RESULTS OR THAT THE SOFTWARE OR ITS USE WILL NOT
 * RESULT IN INJURY OR DAMAGE.  The user assumes responsibility for all
 * liabilities, penalties, fines, claims, causes of action, and costs and
 * expenses, caused by, resulting from or arising out of, in whole or in part
 * the use, storage or disposal of the SOFTWARE.
 *
 *
 ******************************************************************************/

/**
 * @author Feiyi Wang, John Harney
 *
 */


//alert('loading esgf-download.js');

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
                        if(word.length > 25) {
                            abbreviation = word.slice(0,10) + '...' + word.slice(word.length-11,word.length);
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
        LOG.debug("top level changed");

        var currentValue = $(this).attr('checked');

        var selectedItem = $.tmplItem(this);
        var selectedDoc = selectedItem.data.doc;
        var selectedDocId = selectedDoc.id;


        //$(this).parent().parent().parent().find(':checkbox').each( function(index) {
        $(this).parent().parent().parent().find('tr.rows_'+ replacePeriod(selectedDoc.id)).find(':checkbox').each( function(index) {
                    $(this).attr('checked', currentValue);
        });

//    	var selectedItem = $.tmplItem(this);
//    	var selectedDoc = selectedItem.data.doc;
//    	var selectedDocId = selectedDoc.id;
//    	$('input#' + replacePeriod(selectedDocId)).attr('checked', 'checked');
//
//    	/*debugging stateent id like to keep in here
//    	jQuery("input:checkbox:checked").each(function(){
//	         alert(this.id + ' ' + $(this).attr("checked"));
//	    });
//    	*/
//
//    	var selectedFileIds = selectedDoc.file_id;
//    	for(var i=0;i<selectedFileIds.length;i++) {
//
//    		if(selectedDoc.service_type[i] == 'HTTPServer') {
//    			if($('input#' + replacePeriod(selectedDocId)).attr('checked')) {
//        			$('input#' + replacePeriod(selectedFileIds[i])).attr('checked', 'checked');
//    			}
//    			else {
//    				//not working
//    				$('input#' + replacePeriod(selectedFileIds[i])).attr('checked', 'false');
//    			}
//    		}
//    	}

    });

    $('.remove_dataset_from_datacart').live ('click', function(e) {
    	
    	//remove from the selected store
    	var selectedItem = $.tmplItem(this);
        var selectedDoc = selectedItem.data.doc;
        var selectedDocId = selectedDoc.id;
    	//alert(ESGF.search.selected + ' ' + selectedDocId);
    	delete ESGF.search.selected[selectedDocId];
    	
    	//remove visually
    	//alert($(this).parent().parent().parent().find('tr.rows_'+ replacePeriod(selectedDoc.id)).html());
    	($('tr.rows_'+ replacePeriod(selectedDoc.id))).remove();
    	$('tr#' + replacePeriod(selectedDoc.id)).remove();
    	
    	//change from remove from cart to add to cart
    	$('a#ai_select_'+ selectedDoc.id.replace(/\./g, "_")).html('Add To Cart');
    	
    });

    $(".wgetAllChildren").live ('click', function (e){



        var selectedItem = $.tmplItem(this);
        var selectedDoc = selectedItem.data.doc;
        var selectedDocId = selectedDoc.id;


        var selectedFileUrls = selectedDoc.file_url;
        var selectedFileIds = selectedDoc.file_id;
        var queryString = 'type=create&id=' + selectedDocId;


         var ids   = new Array();
         var values = new Array();
            //jQuery("input:checkbox:checked").each(function(){
         //$(this).parent().parent().parent().find(':checkbox:checked').each( function(index) {
         $(this).parent().parent().parent().find('tr.rows_'+ replacePeriod(selectedDoc.id)).find(':checkbox:checked').each( function(index) {
                 if(this.id != selectedDocId) {
                 ids.push(this.id) ;
                 values.push(this.value);
                }
            });

        //for(var i=0;i<selectedFileUrls.length;i++) {
        for(var i=0;i<ids.length;i++) {
            //if(selectedDoc.service_type[i] == 'HTTPServer') {
                queryString += '&child_url=' + values[i] + '&child_id=' + ids[i];
            //}
        }

        var url = '/esgf-web-fe/wgetproxy';

        //assemble the input fields with the query string
        var input = '';
        jQuery.each(queryString.split('&'), function(){
            var pair = this.split('=');
            input+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
        });

        //send request
        jQuery('<form action="'+ url +'" method="post">'+input+'</form>')
        .appendTo('body').submit().remove();

    });


    /* creation of a variable inside the template - may need this later
    $.extend( $.tmpl.tag, {
        "var": {
            open: "var $1;"
        }
    });
    */


});



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