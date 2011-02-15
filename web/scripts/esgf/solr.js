/**
 * Experimental Solr Front for ESG
 *
 * fwang2@ornl.gov
 *
 */



(function ($) {

    Manager = new AjaxSolr.Manager({
        proxyUrl: '/esgf-web-fe/solrproxy',
        //proxyUrl: 'http://esg-gw.ornl.teragrid.org:8080/esgf-web-fe/solrproxy'
        metadataProxyUrl: '/esgf-web-fe/metadataproxy'
    });

    Manager.addWidget(new AjaxSolr.ResultWidget({
          id: 'result',
          target: '#search-results'
        }));


    Manager.addWidget(new AjaxSolr.PagerWidget({
          id: 'pager',
          target: '#pager',
          prevLabel: '&lt;',
          nextLabel: '&gt;',
          innerWindow: 1,
          renderHeader: function (perPage, offset, total) {
            $('#pager-header').html($('<span/>').text('displaying ' +
                    Math.min(total, offset + 1) + ' to ' + Math.min(total, offset + perPage) +
                    ' of ' + total + " search results"));
          }
        }));

    Manager.addWidget(new AjaxSolr.CurrentSearchWidget({
          id: 'currentsearch',
          target: '#current-selection'
        }));

    Manager.addWidget(new AjaxSolr.AutocompleteWidget({
          id: 'text',
          target: '#search-box',
          field: 'text',
          fields: [ 'project', 'model' , 'experiment', 'realm', 'instrument', 'cf_variable' , 'gcmd_variable']
        }));


    /*
    Manager.addWidget(new AjaxSolr.MetadataWidget({
          id: 'metadata-browse',
          target: '#metadata-browse'
        }));



    Manager.addWidget(new AjaxSolr.TemporalWidget({
          id: 'temp-browse',
          target: '#temp-browse'
        }));
	*/

    Manager.addWidget(new AjaxSolr.GeospatialSearchWidget({
          id: 'geo_browse'
          /* target: '#geo_browse' */
        }));

    /*

    */
//	Manager.addWidget(new AjaxSolr.TextWidget({
//		  id: 'text',
//		  target: '#search-box',
//		  field: 'allText'
//		}));




    /*Manager.addWidget(new AjaxSolr.CalendarWidget({
          id: 'calendar',
          target: '#calendar',
          field: 'datetime_start'
        }));*/

    Manager.init();

    var fields = ['project' , 'model', 'experiment', 'frequency', 'realm', 'instrument', 'variable', 'cf_variable', 'gcmd_variable'];



    var params = {
              'facet': true,
              'facet.field': fields,
              'facet.limit': 20,
              'facet.mincount': 1,
              'f.topics.facet.limit': 50,
              'json.nl': 'map'
            };
    for (var name in params) {
              Manager.store.addByValue(name, params[name]);
    }


    for (var i = 0, l = fields.length; i < l; i++) {
      Manager.addWidget(new AjaxSolr.TagcloudWidget({
        id: fields[i],
        target: '#' + fields[i],
        field: fields[i]
      }));
    }


    for (var i = 0, l = fields.length; i < l; i++) {
          Manager.addWidget(new AjaxSolr.FacetBrowserWidget({
            id: fields[i],
            target: '#' + fields[i],
            field: fields[i]
          }));
        }

    Manager.store.addByValue('q', '*:*');


    Manager.doRequest();

//	$("div.search-entry").live('mouseover',  function() {
//		$(this).css('background-color', '#ccffff');
//	})
//
//	$("div.search-entry").live('mouseout', function() {
//		$(this).css("background-color", '#ffffff');
//	})


})(jQuery);


$(document).ready(function(){

    /* scroll wheel for metadata and facet overlays */
    $(".scrollable").scrollable({ vertical: true, mousewheel: true });

    /* radio buttons for sorting facets */
    
    $("#facetSort").buttonset({
    	
    });
	
    
    /* event trigger for facet sorting buttons */
    $("input[name='sorter']").change(function() {
        if ($("input[name='sorter']:checked").val() == 'sortbyabc') {
            Manager.sortType = 'sortbyabc';
            Manager.doRequest(0);
        } else {
            Manager.sortType = 'sortbycount';
            Manager.doRequest(0);
        }
    });



    //This is a widget that is not used as of yet...but may be added later
    //$("#s1").dropdownchecklist();

});
