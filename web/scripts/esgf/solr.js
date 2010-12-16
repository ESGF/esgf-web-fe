/**
 * Experimental Solr Front for ESG
 * 
 * fwang2@ornl.gov
 * 
 */
(function ($) {
	
	$("div.search-entry").live('mouseover',  function() {
		$(this).css('background-color', '#ccffff');
	})
	
	$("div.search-entry").live('mouseout', function() {
		$(this).css("background-color", '#ffffff');
	})
	
	Manager = new AjaxSolr.Manager({
		proxyUrl: 'http://localhost:8080/esg-web/solrproxy'
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
		    		' of ' + total));
		  }
		}));

	Manager.addWidget(new AjaxSolr.CurrentSearchWidget({
		  id: 'currentsearch',
		  target: '#current-selection',
		}));

	Manager.addWidget(new AjaxSolr.TextWidget({
		  id: 'text',
		  target: '#search-box',
		  field: 'allText'
		}));

	
	Manager.init();
	
    var fields = [ 'project'];
 
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
    

	Manager.store.addByValue('q', '*:*');	
	Manager.doRequest();
	
})(jQuery);