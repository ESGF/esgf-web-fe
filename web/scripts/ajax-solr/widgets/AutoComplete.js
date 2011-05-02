/**
 * 
 */

(function ($) {

AjaxSolr.AutocompleteWidget = AjaxSolr.AbstractFacetWidget.extend({

	init: function() {
		var self = this;
		
		$('#search-button').live('click',function(){
			//var value = $(this).val();
			var value = $('input#query').val();
			if(ESGF.setting.storage) {
				var fq = localStorage['fq'];
				if(fq == undefined) {
			  		fq = 'text:' + value + ';';
			  		localStorage['fq'] = fq;
			  	} else {
			  		fq += 'text:' + value + ';';
			  		localStorage['fq'] = fq;
			  	}
			}
			
			if (value && self.add(value)) {
		        self.manager.doRequest(0);
		      }
		});
		
		
		$(this.target).find('input').bind('keydown', function(e) {
			if (self.requestSent === false && e.which == 13) {
		        var value = $(this).val();
		        if (value && self.add(value)) {
		          self.manager.doRequest(0);
		        }
		      }
			
			if(self.requestSent === false && e.which === 13) {
				var value = $('input#query').val();
				if(ESGF.setting.storage) {
					
						var fq = localStorage['fq'];
						if(fq == undefined) {
					  		fq = 'text:' + value + ';';
					  		localStorage['fq'] = fq;
					  	} else {
					  		fq += 'text:' + value + ';';
					  		localStorage['fq'] = fq;
					  	}
					}
				  
				  if(value && self.add(value)) {
					  self.manager.doRequest(0);
				  }
			}
			
			
		});
		
		
	},

  afterRequest: function () {
	  
	  $(this.target).find('input').val('');
	  
	  var self = this;
	  
	  var list = [];
	  for (var i=0;i<this.fields.length;i++) {
		  var field = this.fields[i];
		  
		  for(var facet in this.manager.response.facet_counts.facet_fields[field]) {
			  list.push({
				  field: field,
				  value: facet,
				  text: facet + ' (' + this.manager.response.facet_counts.facet_fields [field][facet] + ') - ' + field
			  });
		  }
	  }
	  
	  this.requestSent = false;
	  $(this.target).find('input').autocomplete(list, {
		  formatItem: function (facet) {
			  return facet.text;
		  }
	  }).result(function(e, facet) {
		  self.requestSent = true;
		  if(self.manager.store.addByValue('fq',facet.field + ':' + facet.value)){
			  if(ESGF.setting.storage) {
					var fq = localStorage['fq'];
					if(fq == undefined) {
				  		fq = facet.field + ':' + facet.value + ';';
				  		localStorage['fq'] = fq;
				  	} else {
				  		fq += facet.field + ':' + facet.value + ';';
				  		localStorage['fq'] = fq;
				  	}
				}
			  self.manager.doRequest(0);
		  }
	  });
	  
	  
	
  }
  



});




}(jQuery));


