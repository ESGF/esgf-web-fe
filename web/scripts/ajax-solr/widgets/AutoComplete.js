/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * “Licensor”) hereby grants to any person (the “Licensee”) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization’s name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.”
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
 * Experimental Solr Front for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
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


