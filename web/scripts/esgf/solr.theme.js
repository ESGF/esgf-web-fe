/**
 * Presentation theme for Solr search results
 *
 * fwang2@ornl.gov
 *
 */

(function ($) {


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
      output += "<p/>" + snippet + actions + '</div>';

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
    output += '<span class="actionitem ai_meta"><a href="/esgf-web-fe/scripts/esgf/metadata_overlay.html" class="met" rel="#metadata_overlay"' + allStr + '> Metadata Summary</a></span>';

    selectID = 'ai_select_'+ doc.id.replace(/\./g, "_");
    output += '<span class="actionitem"> <a href="#" id="' + selectID + '">Select</a></span>';

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
            if ( jQuery.trim(this.innerHTML) == "Select") {
                var $dialog = $('<div></div>')
                    .html('Dataset <b>' + evt.data.doc.id + "</b> has been added to the selection")
                    .dialog({
                        autoOpen: true,
                        show: 'blind',
                        hide: 'explode'
                    });
                this.innerHTML="Remove";

            } else {
                var $dialog = $('<div></div>')
                .html('Dataset <b>' + evt.data.doc.id + "</b> has been removed to the selection")
                .dialog({
                    autoOpen: true,
                    show: 'blind',
                    hide: 'explode'
                });
                this.innerHTML ="Select";
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
    return $('<h1>' + 'Facet Browser' + '<\h1>');
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

    //alert(self);

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
    $('div#abstract_metadata').after('<div class="addedMetadata"><p>' + self.description + '</p></div>');

    //add title and constraints to the page
    $('div#metadata_summary_dataset').after('<div class="addedMetadataTitle">' + 'Dataset: ' + self.title);

    //add investigators to the page
    $('div#investigator_metadata').after('<div class="addedMetadata"><p>' + self.invesigators + '</p></div>');

    //add contact information to the page
    $('div#contact_metadata').after('<div class="addedMetadata"><p>' + self.contact + '</p></div>');

    //add start and stop times to the page
    $('div#time_metadata').after('<div class="addedMetadata"><p>Begin: ' + self.startTime + ' End: ' + self.stopTime + '</p></div>');

    //add geospatial info to the page
    $('div#geospatial_metadata').after('<div class="addedMetadata"><p>' + 'coordinates (N,W,S,E):<br />(' + self.north_degrees + ',' + self.west_degrees + ',' + self.south_degrees + ',' + self.east_degrees + ')</p></div>');
    //self.display_meta_map(west_degreesText,east_degreesText,north_degreesText,south_degreesText);
    self.display_meta_map();


};


})(jQuery);


