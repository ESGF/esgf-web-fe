/**
 * Presentation theme for Solr search results
 *
 * fwang2@ornl.gov
 *
 */

(function ($) {

    var selected = {};

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
    var descriptionStr = 'description="' + doc.description + '" ';
    var westDegreesStr = 'west_degrees="' + doc.west_degrees + '" ';
    var eastDegreesStr = 'east_degrees="' + doc.east_degrees + '" ';
    var northDegreesStr = 'north_degrees="' + doc.north_degrees + '" ';
    var southDegreesStr = 'south_degrees="' + doc.south_degrees + '" ';
    var datetime_startStr = 'datetime_start="' + doc.datetime_start + '" ';
    var datetime_stopStr = 'datetime_stop="' + doc.datetime_stop + '" ';

    var allStr = idStr + titleStr + urlStr + formatStr +  metadataURLStr + descriptionStr + westDegreesStr + eastDegreesStr + northDegreesStr + southDegreesStr + datetime_startStr + datetime_stopStr;

    output += '<div class="search-entry">';
      output += '<h4 class="desc"> <div class="m"><a href="#"' + allStr + '></a>';
      output += '<a href="/esgf-web-fe/scripts/esgf/metadata_overlay.html" rel="#metadata_overlay" class="met" style="text-decoration:none">';
      output += doc.title + '</a>';
      output += '</h4>' ;
      output += '<p id="links_' + doc.id + '" class="links"></p>';
      output += "<p/>" + snippet + actions + '</div>';

      return output;
};


AjaxSolr.theme.prototype.actions = function (doc) {
    var output = '<div class="actions">',
        selectID = '';

    output += "Further options: ";
    output += '<span class="actionitem ai_meta"><a href="#"> Metadata Summary</a></span>';

    selectID = 'ai_select_'+ doc.id.replace(/\./g, "_");
    output += '<span class="actionitem ' + selectID + ' "><a href="#"> Select </a></span>';
    output += '<span class="actionitem ai_annotate"><a href="#"> Annotate</a></span>';
    output += "</div>";

    $("." + selectID).live('click', {doc:doc}, function (evt) {

        selected[evt.data.doc.id] = doc;

        var $dialog = $('<div></div>')
                .html('Dataset <b>' + evt.data.doc.id + "</b> has been added to the selection")
                .dialog({
                    autoOpen: true,
                    show: 'blind',
                    hide: 'explode'
                });
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
    return $('<span class="facet_title"><h3>' + value + '</h3></span>');
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

})(jQuery);


