/*****************************************************************************
 * Copyright � 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * �Licensor�) hereby grants to any person (the �Licensee�) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization�s name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.�
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

AjaxSolr.DistributedSearchWidget = AjaxSolr.AbstractWidget.extend({
	
	init: function() {
		//alert('init distributed search ' + !($("input#distribcheckbox").attr("checked")));

		//$("input#distribcheckbox").attr('checked','true');
		
		
		ESGF.localStorage.remove('esgf_queryString','distrib');

		ESGF.localStorage.put('esgf_queryString','distrib','distrib=false');
		
		
	},
	
	beforeRequest: function () {
		
		
		$("input#distribcheckbox").die('change');
		
	},
	
	afterRequest: function () {
		var self = this;
		
		$('input#distribcheckbox').live('change',function () {
			//alert('changed checkbox');
			//alert('checked?' + $("input#distribcheckbox").attr("checked"));
			if($("input#distribcheckbox").attr("checked")) { 
				//ESGF.localStorage.remove('esgf_queryString','distrib');
				//ESGF.localStorage.put('esgf_queryString','distrib','distrib=false');
				ESGF.localStorage.remove('esgf_queryString','distrib');
            	Manager.doRequest(0);
			} else {
				ESGF.localStorage.put('esgf_queryString','distrib','distrib=false');

            	Manager.doRequest(0);
			}
		});	
    
		
		
		
	}

});

}(jQuery));



/*
//alert('distrib button click');
//alert($('a#distributed').html());
//if($('a#distributed').html() == 'Turn off Distributed Search') {
if(localStorage['distrib'] == 'local') {
	//change the text to Distributed
	$('input#distribbutton').val('Turn off Distributed Search');
	
	
	//change the flag to Distributed
	ESGF.setting.searchType = 'Distributed';
	localStorage['distrib'] = 'distributed';
	
	Manager.doRequest(0);
} else {
	//change the text to Local
	$('input#distribbutton').val('Turn on Distributed Search');
	
	//change the flag to Local
	ESGF.setting.searchType = 'local';
	localStorage['distrib'] = 'local';
	
	
	Manager.doRequest(0);
}
*/

//alert('distributed search afterRequest');



//if the distrib localstorage has not been defined
//define it as local here

/*
if(localStorage['distrib'] == undefined) {
	localStorage['distrib'] = 'local';
	
} else if(localStorage['distrib'] == null) {
	localStorage['distrib'] = 'local';
} else if(localStorage['distrib'] == '') {
	localStorage['distrib'] = 'local';
}

if(localStorage['distrib'] == 'local') {
	$('input#distribbutton').val('Turn on Distributed Search');
} else {
	$('input#distribbutton').val('Turn off Distributed Search');
}
*/

/*
//need to introduce the esg search api here
var distrib = ESGF.localStorage.get('esgf_queryString','distrib');
//alert('distrib ' + distrib);
if(distrib == undefined) {
	ESGF.localStorage.put('esgf_queryString','distrib','distrib=true');
} 

//put a value on the button
if(ESGF.localStorage.get('esgf_queryString','distrib') == 'distrib=false') {
	$('input#distribbutton').val('Turn on Distributed Search');
} else {
	$('input#distribbutton').val('Turn off Distributed Search');
}


//alert(ESGF.localStorage.get('esgf_queryString','distrib'));
if(ESGF.localStorage.get('esgf_queryString','distrib') == 'distrib=false') {
	//alert('change button message to turn on distributed search');
} else {
	//alert('change button message to turn off distributed search');
}


$('input#distribbutton').live('click',function () {
	
	//for the search api
	var distrib = ESGF.localStorage.get('esgf_queryString','distrib');
	//alert('distrib ' + distrib);
	
	if(ESGF.localStorage.get('esgf_queryString','distrib') == 'distrib=false') {
		ESGF.localStorage.update('esgf_queryString','distrib','distrib=true');
		$('input#distribbutton').val('Turn off Distributed Search');
		//alert('change button message to turn on distributed search');
    	Manager.doRequest(0);
	} else {
		ESGF.localStorage.update('esgf_queryString','distrib','distrib=false');
		$('input#distribbutton').val('Turn on Distributed Search');
		//alert('change button message to turn off distributed search');
    	Manager.doRequest(0);
	}
	
	
	
	
	
});  
*/