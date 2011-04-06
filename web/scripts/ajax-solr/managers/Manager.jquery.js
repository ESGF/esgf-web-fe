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
          self.cookies();
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
	              {}, function (data) { alert(self.store.string()); self.handleResponse(data); });
          }
      },
  
  	cookies: function () {
  		var self = this;
  		
  		
  		
  		//load the cookies in the fq  and q arrays to the store
  		
  		//add those cookes to the store
  		
  		//this.store.addByValue('fq','hhh:iii');
  		
  		/*
  		alert(this.store.string());
  		for(var att in self) {
    		alert('att: ' + att + ' ' + self[att]);
    		
    	}
    	*/
  		
  		alert('in the cookiehandler');
  		
  	}
});
