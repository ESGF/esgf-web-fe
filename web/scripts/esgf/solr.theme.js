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
 * Presentation theme for Solr search results
 *
 * fwang2@ornl.gov
 *
 */

//alert('loading solr.theme.js');

(function ($) {


AjaxSolr.theme.prototype.result = function (doc, snippetReplica, snippetVersion, snippet, actions) {
    var output = '';

    if (doc.title.length > 7000) {
        doc.title = doc.title.substring(0,7000) + "...";
    }


    var idStr = 'id="' + doc.id + '" ';
    var titleStr = 'title="' + doc.title + '" ';
    var urlStr = 'url="' + doc.url + '" ';
    var formatStr = 'format="' + doc.metadata_format + '" ';
    var metadataURLStr = 'metadata_url="' + doc.metadata_url + '" ';

    var allStr = idStr + titleStr + urlStr + formatStr +  metadataURLStr; //+ descriptionStr + westDegreesStr + eastDegreesStr + northDegreesStr + southDegreesStr + datetime_startStr + datetime_stopStr;

    output += '<div class="search-entry">';

      output += '<div style="font-size:14px;font-style:bold" class="desc">';//'<h4 class="desc">';

      //output += '<a href="#" style="text-decoration:none">';
      if(doc['replica']) {
    	  output += '<span style="font-size:9px;color:#7d5f45;font-weight:bold;font-style:italic;font-type:Trade Gothic;margin-right:3px"> Replica </span>';
      }
      output += '<span class="actionitem ai_meta">';
      //output += '<a href="metadataview?' + 'id=' + doc.id + '" ';
      //alert('')
      output += '<a href="metadataview/' + doc.id + '.html"' ;//?' + 'id=' + doc.id + '" ';
      //output += 'class="met" rel="#metadata_overlay"';
      output += allStr + '>';
      output += doc.title + '</a>';
      output += '</div>' ;
      output += '<p id="links_' + doc.id + '" class="links"></p>';
      output += "<p/><div>" + snippetReplica + "</div>" + "<div class='snippetVersion'>" + snippetVersion + "</div>" + "<div class='snippet'>" + snippet + "</div>" + actions + '</div>';

      return output;
};


AjaxSolr.theme.prototype.actions = function (doc) {
    var output = '<div class="actions" style="font-size:12px">',
        selectID = '',
        selected = ESGF.search.selected,
        carts = [];

    var idStr = 'id="' + doc.id + '" ';
    var titleStr = 'title="' + doc.title + '" ';
    var urlStr = 'url="' + doc.url + '" ';
    var formatStr = 'format="' + doc.metadata_format + '" ';
    var metadataURLStr = 'metadata_url="' + doc.metadata_url + '" ';
    var allStr = idStr + titleStr + urlStr + formatStr +  metadataURLStr;

    output += "Further options: ";
    
    selectID = 'ai_select_'+ doc.id.replace(/\./g, "_");

    selectID = selectID.replace("|","_");
    
    
    
    selectMetID = 'meta_select_'+ doc.id.replace(/\./g, "_");
    
    if(ESGF.localStorage.search('dataCart',doc.id)) {
    	output += '<span class="actionitem"> <a href="#" class="' + 'selections"' + ' id="' + selectID + '">Remove From Cart</a></span>';
    } else {
    	output += '<span class="actionitem"> <a href="#" class="' + 'selections"' + ' id="' + selectID + '">Add To Cart</a></span>';
    }
    
    if(doc.url instanceof Array) {
    	for(var i=0;i<doc.url.length;i++) {
    		
    		var url = doc.url[i];
    		
    		//alert('url: ' + url);
    		if(url.search("LAS") > -1) {
    	    	var tuple = url.split("\|");
        	    output += '<span class="actionitem ai_las"><a href="' + tuple[0] + '" target="_blank">Visualize and Analyze</a></span>';
    	    } else if(url.search("OPENDAP") > -1) {
    	    	var tuple = url.split("\|");
        	    output += '<span class="actionitem ai_las"><a href="' + tuple[0] + '" target="_blank">OPENDAP</a></span>';
            } else if(url.search("application/gis") > -1) {
                var tuple = url.split("\|");
                output += '<span class="actionitem ai_las"><a href="' + tuple[0] + '" target="_blank">'+tuple[2]+'</a></span>';
            } 
    	}
    }
    
    var projectStr = 'project="' + doc.project + '" ';
    var modelStr = 'model="' + doc.model + '" ';
    var instituteStr = 'institute="' + doc.institute + '" ';
    var experimentStr = 'experiment="' + doc.experiment + '" ';
    var cimStr = projectStr + modelStr + instituteStr + experimentStr;
    
    if(doc.project == 'CMIP5' || doc.project == 'cmip5') {
    	//output += '<span class="__actionitem__"> <a class="cim-model" href="#" id="' + selectMetID + '">CIM Metadata</a></span>';
        output += '<span class="__actionitem__"> <a class="cim-model" href="#" ' + cimStr + '">Model Metadata</a></span>';
        
    }
    
    
    
    if(doc.xlink != undefined) {
        var techNote = doc.xlink;
        
        for (var i=0;i<techNote.length;i++) {
        	//url.length - 1 => "Technical Note"
        	//url.length - 2 => <type of technical note>
        	//url.length - 3 => <physical url of technical note>
        	
        	var url = techNote[i].split("|");
        	
    	    output += '<span class="actionitem ai_las"><a href="' + url[url.length-3] + '" target="_blank">' + url[url.length-2] + '</a></span>';
        } 
        
    }
    
    if (ESGF.setting.annotate === true) {

        output += '<span class="actionitem"><a class="annotate" href="/esgf-web-fe/scripts/esgf/annotation_overlay.html" rel="#annotator_overlay"> Annotate</a></span>';
        output += "</div>";
    }

    //output += '<span><a href="metadataview?' + 'id=' + doc.id + '">metadata</a></span>';
    
    
    //alert('solr theme selectID ' + selectID);
    
    //alert('killing live link for ' + selectID);
    //$("a[id=" + selectID + "]").die('click');
	//alert('creating live link for ' + selectID);
    //$("a[id=" + selectID + "]").live('click', {doc:doc}, function (evt) {
    $("a#" + selectID).live('click', {doc:doc}, function (evt) {
        	
    	
        var metadataFormat = doc.metadata_format;
        
        //right now, we only support downloads through TDS
        //when we support others, this if guard will be removed
        if(metadataFormat === 'THREDDS') {

        	//alert('number of files: ' + evt.data.doc['number_of_files']);
        	//var docInfo = 
        	
            selected[evt.data.doc.id] = doc;
            if ( jQuery.trim(this.innerHTML) == "Add To Cart") {
            	
            	//alert('adding to cart');
            	if(evt.data.doc['xlink'] != undefined) {
            		alert('xlink defined');
            		//alert(evt.data.doc['xlink']);
            		//add to the datacart localstorage
                	if(evt.data.doc['index_node'] != undefined) {
                		
                		var datasetInfo = {'numFiles' : evt.data.doc['number_of_files'], 'peer' : evt.data.doc['index_node'] , 'xlink' : evt.data.doc['xlink']};
                		
                    	ESGF.localStorage.put('dataCart',evt.data.doc.id,datasetInfo);
                	
                	
                	} else {
                    
                		//alert('peer should be undefined');
                		
                		var datasetInfo = {'numFiles' : evt.data.doc['number_of_files'], 'peer' : 'undefined' , 'xlink' : evt.data.doc['xlink']};
                		
                		alert('xlink: ' + datasetInfo['xlink']);
                		
                		ESGF.localStorage.put('dataCart',evt.data.doc.id,datasetInfo);
                	
                	
                	}
            		
            	} else {
            		alert('xlink undefined');
            	
            		//add to the datacart localstorage
                	if(evt.data.doc['index_node'] != undefined) {
                    
                		var datasetInfo = {'numFiles' : evt.data.doc['number_of_files'], 'peer' : evt.data.doc['index_node'] , 'xlink' : 'undefined' };
                		
                		ESGF.localStorage.put('dataCart',evt.data.doc.id,datasetInfo);
                	
                	
                	} else {
                		//alert('peer should be undefined');
                		var datasetInfo = {'numFiles' : evt.data.doc['number_of_files'], 'peer' : 'undefined' , 'xlink' : 'undefined' };
                		
                		
                		ESGF.localStorage.put('dataCart',evt.data.doc.id,datasetInfo);
                	
                	
                	}
            	
            	}
            	
            	
            	
            	//add to the datacart searchstates localstorage
            	
                var key = ESGF.localStorage.toString('esgf_fq');
                var value = evt.data.doc.id;

            	ESGF.localStorage.append('esgf_searchStates', key, value);
            	
                this.innerHTML="Remove From Cart";

            } else {
            	//remove from super cookie
            	ESGF.localStorage.remove('dataCart',evt.data.doc.id);

            	//remove from stateful super cookie
                var key = ESGF.localStorage.toString('esgf_fq');
                var value = evt.data.doc.id;
            	ESGF.localStorage.removeFromValue('esgf_searchStates', key, value);
            	
            	
                this.innerHTML ="Add To Cart";
                delete selected[evt.data.doc.id];

            }

        } else {
            alert('Dataset: ' + doc.id + ' cannot be downloaded at this time');
        }


        return false;
    });


    return output;

};

AjaxSolr.theme.prototype.snippet = function (doc) {

    var output = '';

    if (doc.description != undefined)
        doc.text = doc.description[0];
    if (doc.text != undefined) {
    	output += 'Description: ';
        if (doc.text.length > 500) {
            output += doc.text.substring(0, 500);
            output += '<span style="display:none;">' + doc.text.substring(500);
            output += '</span> <a href="#" class="more"> ... more</a>';
        } else {
            output += doc.text;
        }
    } else {
        output = "No description available.";
    }

    return output;
};

AjaxSolr.theme.prototype.snippetReplica = function (doc) {

    var output = '';
    if(doc['replica']) {
        //output += 'Replica dataset at datanode: ' + doc['data_node'] + '<br />';
        output += 'Data Node: ' + doc['data_node'];
        
    } else {
        //output += 'Master dataset at datanode: ' + doc['data_node'];
        output += 'Data Node: ' + doc['data_node'];
    }

    return output;
};

AjaxSolr.theme.prototype.snippetVersion = function (doc) {

    var output = '<span style="font-style:italic;font-weight:bold">';
    //alert('latest: ' + doc['latest']);
    if(doc['latest'] == 'true') {
    	output += 'Version: ' + doc['version'] + ' (Most Recent)';
    } else {
        output += 'Version: ' + doc['version'] + ' ';
    }
    
    output += '</span>';

    return output;
};

AjaxSolr.theme.prototype.facet_browser_title = function(value) {
    return $('<div class="facet_browser_title">' + 'Facet Browser' + '<\div>');
};

AjaxSolr.theme.prototype.facet_title = function(value) {
    var title = $('<span class="facet_title"><h3>' + value + '</h3></span>');
    return title;
};

AjaxSolr.theme.prototype.tag = function (value, weight, handler) {
  return $('<a href="#" class="tagcloud_item"/>').text(value).addClass('tagcloud_size_' + weight).click(handler);
};

AjaxSolr.theme.prototype.facet_link = function (value, handler) {
  return $('<a href="#"/>').text(value).click(handler);
};


AjaxSolr.theme.prototype.no_items_found = function () {
  return 'no items found in current selection';
};


AjaxSolr.theme.prototype.prevLink = function (stopValue,objectedItems,divFieldId,thisObject) {
    var $facet_link = $('<a href="#" id="prev_' + divFieldId + '"> prev ' + thisObject.incrementValue + '...</a>').click(thisObject.prevClickHandler(divFieldId));
    return $facet_link;
};

AjaxSolr.theme.prototype.nextLink = function (divFieldId,thisObject) {
    var $facet_link = $('<a href="#" id="next_' + divFieldId + '"> next ' + thisObject.incrementValue + '...</a>').click(thisObject.nextClickHandler(divFieldId));
    return $facet_link;
};


AjaxSolr.theme.prototype.facet_content = function(stopValue,objectedItems,thisObject) {
    var $facet_content = $('<div></div>');
    if(thisObject.startingValue < objectedItems.length) {
        $facet_content.append('<p>');
        for(var i = thisObject.startingValue, l = stopValue; i < l; i++) {
            var facetTextValue = objectedItems[i].facet + ' (' + objectedItems[i].count + ')';
            var facet = objectedItems[i].facet;
            $facet_content.append($('<a href="#" class="tag_item" />').text(facetTextValue).click(thisObject.clickHandler(facet)));
            if(i != (stopValue - 1)) {
                $facet_content.append(' | ');
            }
        };
        $facet_content.append('</p>');

    }
    return $facet_content;
};


})(jQuery);


