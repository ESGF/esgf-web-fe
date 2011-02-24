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
            $( "#cartTemplate").tmpl(arr).appendTo("#datasetList");
        }


    });

});