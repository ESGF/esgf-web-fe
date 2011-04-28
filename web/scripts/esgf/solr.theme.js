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

    //alert('loading solr.theme.js document ready');


AjaxSolr.theme.prototype.result = function (doc, snippet, actions) {
    var output = '';

    if (doc.title.length > 78) {
        doc.title = doc.title.substring(0,70) + "...";
    }


    var idStr = 'id="' + doc.id + '" ';
    var titleStr = 'title="' + doc.title + '" ';
    var urlStr = 'url="' + doc.url + '" ';
    var formatStr = 'format="' + doc.metadata_format + '" ';
    var metadataURLStr = 'metadata_url="' + doc.metadata_url + '" ';

    var allStr = idStr + titleStr + urlStr + formatStr +  metadataURLStr; //+ descriptionStr + westDegreesStr + eastDegreesStr + northDegreesStr + southDegreesStr + datetime_startStr + datetime_stopStr;

    output += '<div class="search-entry">';
      output += '<h4 class="desc">';
      output += '<a href="#" style="text-decoration:none">';
      output += doc.title + '</a>';
      output += '</h4>' ;
      output += '<p id="links_' + doc.id + '" class="links"></p>';
      output += "<p/><div class='snippet'>" + snippet + "</div>" + actions + '</div>';

      return output;
};


AjaxSolr.theme.prototype.actions = function (doc) {
    var output = '<div class="actions">',
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
    output += '<span class="actionitem ai_meta"><a href="/esgf-web-fe/scripts/esgf/metadata_overlay_tdsLink.html" class="met" rel="#metadata_overlay"' + allStr + '> Metadata Summary</a></span>';
    //output += '<span class="actionitem ai_meta"><a href="/esgf-web-fe/scripts/esgf/sample.html" class="met" rel="#metadata_overlay"' + allStr + '> Metadata Summary</a></span>';
    //output += '<span class="actionitem ai_meta"><a href="http://www.forbes.com/2009/02/11/cancer-cure-experimental-lifestyle-health_0212cancer.html" class="met" rel="#metadata_overlay"' + allStr + '> Metadata Summary</a></span>';

    selectID = 'ai_select_'+ doc.id.replace(/\./g, "_");
   // alert('selectID: ' + selectID);
    output += '<span class="actionitem"> <a href="#" id="' + selectID + '">Add To Cart</a></span>';

    if (typeof doc.service != "undefined") {
        LOG.debug("LAS service tag detected");
        var svcStr = doc.service[0].split("|");
        if (svcStr[0] == "LAS") {
            output += '<span class="actionitem ai_las"><a href="' + svcStr[2] + '">LAS</a></span>';
        }
    }

    if (ESGF.setting.annotate === true) {

        output += '<span class="actionitem"><a class="annotate" href="/esgf-web-fe/scripts/esgf/annotation_overlay.html" rel="#annotator_overlay"> Annotate</a></span>';
        output += "</div>";
    }

    $("a[id=" + selectID + "]").live('click', {doc:doc}, function (evt) {
    	
        var metadataFormat = doc.metadata_format;

        //right now, we only support downloads through TDS
        //when we support others, this if guard will be removed
        if(metadataFormat === 'THREDDS') {

            selected[evt.data.doc.id] = doc;
            if ( jQuery.trim(this.innerHTML) == "Add To Cart") {
                var $dialog = $('<div></div>')
                    .html('Dataset <b>' + evt.data.doc.id + "</b> has been added to the selection")
                    .dialog({
                        autoOpen: true,
                        show: 'blind',
                        modal: true,
                        hide: 'explode'
                    });
                this.innerHTML="Remove From Cart";

            } else {
                var $dialog = $('<div></div>')
                .html('Dataset <b>' + evt.data.doc.id + "</b> has been removed to the selection")
                .dialog({
                    autoOpen: true,
                    show: 'blind',
                    modal: true,
                    hide: 'explode'
                });
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


AjaxSolr.theme.prototype.metadata = function(thisObject) {
    var self = thisObject;
    /*
     * Searchable solr fields
     */

    //alert('writing title: ' + self.searchable_title);
    //title
    $('div#metadata_summary_dataset').after('<div class="addedMetadataTitle">' + 'Dataset: ' + self.searchable_title);

    //alert('writing project: ' + self.facet_project);
    $('div#projects_metadata').after('<div class="addedMetadata"><p>' + self.facet_project + '</p></div>');

    //alert('writing investigator: '+ self.misc_investigators);
    $('div#investigator_metadata').after('<div class="addedMetadata"><p>' + self.misc_investigators + '</p></div>');

    //alert('writing contact info: ' + self.misc_contactinfo);
    $('div#contact_metadata').after('<div class="addedMetadata"><p>' + self.misc_contactinfo + '</p></div>');

    //alert('writing temporal: ' + self.searchable_datetime_start);
    //temporal
    $('div#time_metadata').after('<div class="addedMetadata"><p>Begin: ' + self.searchable_datetime_start + ' End: ' + self.searchable_datetime_stop + '</p></div>');

    //alert('writing geo (nd): ' + self.searchable_north_degrees);
    //geospatial
    $('div#geospatial_metadata').after('<div class="addedMetadata"><p>' + 'coordinates (N,W,S,E):<br />(' + self.searchable_north_degrees + ',' + self.searchable_west_degrees + ',' + self.searchable_south_degrees + ',' + self.searchable_east_degrees + ')</p></div>');
    if(self.searchable_north_degrees != 'N/A')
    self.display_meta_map();

    //alert('writing keywords: ' + self.misc_keywords);
    $('div#keywords_metadata').after('<div class="addedMetadata"><p>' + self.misc_keywords + '</p></div>');

    //alert('writing description: ' + self.searchable_description);
    //abstract/description
    $('div#abstract_metadata').after('<div class="addedMetadata"><p>' + self.searchable_description + '</p></div>');





            /*
             *  searchable fields that are not displayed
            //metadata_format
            $('div#metadataformat_metadata').after('<div class="addedMetadata"><p>Format: ' + self.searchable_metadata_format + '</p></div>');

            //metadata_url
            $('div#metadataurl_metadata').after('<div class="addedMetadata"><p>URL: ' + self.searchable_metadata_url + '</p></div>');

            //metadata_filename
            $('div#metadatafilename_metadata').after('<div class="addedMetadata"><p>File name: ' + self.searchable_metadata_file_name + '</p></div>');

            //file id
            $('div#fileid_metadata').after('<div class="addedMetadata"><p>Data File Ids: ' + self.searchable_file_id + '</p></div>');

            //file url
            $('div#fileurl_metadata').after('<div class="addedMetadata"><p>Data File URLs: ' + self.searchable_file_url + '</p></div>');

          //file size
            $('div#filesize_metadata').after('<div class="addedMetadata"><p>Data File Size: ' + self.searchable_size + '</p></div>');

            //data url
            $('div#url_metadata').after('<div class="addedMetadata"><p>URL: ' + self.searchable_url + '</p></div>');

            //data type
            $('div#type_metadata').after('<div class="addedMetadata"><p>URL: ' + self.searchable_type + '</p></div>');

          //data version
            $('div#version_metadata').after('<div class="addedMetadata"><p>Version: ' + self.searchable_version + '</p></div>');

            //data version
            $('div#source_url_metadata').after('<div class="addedMetadata"><p>URL: ' + self.searchable_source_url+ '</p></div>');

            //timestamp
            $('div#timestamp_metadata').after('<div class="addedMetadata"><p>Timestamp: ' + self.searchable_timestamp + '</p></div>');

            */




    /*
     * Solr faceted properties not displayed
     */
    /*
    //instrument
    $('div#facet_instrument_metadata').after('<div class="addedMetadata"><p>Data File URLs: ' + self.facet_instrument + '</p></div>');
    //variable
    $('div#facet_variable_metadata').after('<div class="addedMetadata"><p>Data File URLs: ' + self.facet_variable + '</p></div>');
    //cf variable
    $('div#facet_cfvariable_metadata').after('<div class="addedMetadata"><p>Data File URLs: ' + self.facet_cfvariable + '</p></div>');
    //gcmd variable
    $('div#facet_gcmdvariable_metadata').after('<div class="addedMetadata"><p>Data File URLs: ' + self.facet_gcmdvariable + '</p></div>');
    */


            /*
            var keywordsText = '';

            //add keywords to the page
            if(self.keywords != null && self.keywords != '')
            {
                for(var i = 0;i<self.keywords.length;i++) {
                    if(i == self.keywords.length-1) {
                        keywordsText += self.keywords[i] + ' (' + self.keywords[i] + ')';
                    }
                    else {
                        keywordsText += self.keywords[i] + ' (' + self.keywords[i] + '), ';
                    }
                }
                $('div#keywords_metadata').after('<div class="addedMetadata"><p>' + keywordsText + '</p></div>');
            }

            //add investigators to the page
            $('div#investigator_metadata').after('<div class="addedMetadata"><p>' + self.invesigators + '</p></div>');

            //add contact information to the page
            $('div#contact_metadata').after('<div class="addedMetadata"><p>' + self.contact + '</p></div>');

            */

};


})(jQuery);


