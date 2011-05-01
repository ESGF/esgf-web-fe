// $Id$

/**
 * @see http://wiki.apache.org/solr/SolJSON#JSON_specific_parameters
 * @class Manager
 * @augments AjaxSolr.AbstractManager
 */
AjaxSolr.Manager = AjaxSolr.AbstractManager.extend(
  /** @lends AjaxSolr.Manager.prototype */
  {
      executeRequest: function (servlet) {
          var self = this;
          self.loadExistingQueries();
          if (this.proxyUrl) {
          // jQuery.post(this.proxyUrl, 
          // { query: "I am something" }, 
          // function (data) { self.handleResponse(data); }, 
          // 'json');
          jQuery.ajax({
              url: this.proxyUrl,
              data: this.store.string(),
              type: 'GET',
              success: function(data) {self.handleResponse(data);},
              error: function() {alert("error");},
              dataType: 'json'
          });
          }
          else {
	          jQuery.getJSON(this.solrUrl + servlet + 
	              '?' + this.store.string() + '&wt=json&json.wrf=?', 
	              {}, function (data) { self.handleResponse(data); });
          }
      },
  
      
      /**
       * This property (private) loads any existing constraint from the local store into the search
       * application.
       *
       * @field
       * @public
       * @type String
       */
  	loadExistingQueries: function () {
  		LOG.debug('In Manager.loadingExistingQueries');
  		var self = this;
  		
  		//compare the parameter store with the local store
  		//if the local store contains something that is not in the parameter store, then add to parameter store
  		var fq = localStorage['fq'];
  		
  		if(fq != undefined) {
  			var allFqs = fq.split(";");
  			for(var i=0;i<allFqs.length-1;i++)
  			{
  				if(self.store.string().search(escape(allFqs[i])) == -1) {
  					Manager.store.addByValue('fq',allFqs[i]);
  				}
  			}
  	  	}
  		LOG.debug('\tlocalStorage: ' + fq);
  		LOG.debug('\tparameter store: ' + Manager.store.values('fq'));
  		
  		
  		LOG.debug('End in Manager.loadingExistingQueries');
  		
  		
  	}
});
