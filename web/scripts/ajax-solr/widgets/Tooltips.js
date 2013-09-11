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
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */


(function ($) {

AjaxSolr.TooltipsWidget = AjaxSolr.AbstractWidget.extend({
	
	init: function() {
		//alert('init version');

	},
	
	beforeRequest: function () {
		//$("input#versioncheckbox").die('change');
		$("span#hint,label#username,input#datacart_filtered'").unbind();
		$('div.tooltip').die();
	},
	
	afterRequest: function () {
		
		
		var self = this;
    

		
        var changeTooltipPosition = function(event) {
      	  var tooltipX = event.pageX - 8;
      	  var tooltipY = event.pageY + 8;
      	  $('div.tooltip').css({top: tooltipY, left: tooltipX});
      	};
      	
      	var showTooltipAll = function(event) {

      		$('<div class="tooltip" style="font-size:10px;width:150px">' + 
          			'<div>' +
          			'<p>' +
          			'Select all files in the dataset' + //': ' + //value + '"' +//'I\' am tooltips! tooltips! tooltips! :)' + 
          			'</p>' + 
        	  		'</div>' +
          	  	'</div>')
                      .appendTo('body');
          	  changeTooltipPosition(event);
        	  
        	};
         
       
      	var showTooltip = function(event) {

      	  var value = $('input#query').val();
      	  

      		
      	  $('div.tooltip').remove();
      	  
      	  if(value != '') {
      		$('<div class="tooltip" style="font-size:10px">' + 
          			'<div>' +
          			'<p>' +
          			ESGF.setting.tooltipContent.filtered + //': ' + //value + '"' +//'I\' am tooltips! tooltips! tooltips! :)' + 
          			'</p>' + 
          			'<p style="font-weight:bold">' +
          			'Current filter: "' + value + '"' +
          			'</p>' +
        	  		'</div>' +
          	  	'</div>')
                      .appendTo('body');
          	  changeTooltipPosition(event);
      	  } else {
      		$('<div class="tooltip" style="font-size:10px">' + 
          			'<div>' +
          			'<p>' +
          			ESGF.setting.tooltipContent.filtered + 
          			'</p>' + 
          			'<p style="font-weight:bold">' +
          			'No filter selected' +
          			'</p>' +
        	  		'</div>' +
          	  	'</div>')
                      .appendTo('body');
          	  changeTooltipPosition(event);
      	  }
      	  
      	};
       
      	var hideTooltip = function() {
      	   $('div.tooltip').remove();
      	};
       
      	$("span#hint,label#username,span#filter_over_all").bind({
       	   mousemove : changeTooltipPosition,
       	   mouseenter : showTooltipAll,
       	   mouseleave: hideTooltip
       	});
      	
      	$("span#hint,label#username,span#filter_over_text").bind({
      	   mousemove : changeTooltipPosition,
      	   mouseenter : showTooltip,
      	   mouseleave: hideTooltip
      	});
      	
      	var makeAlert = function() {
      	   alert('hover');	
      	};
      	
      	 $('div.tooltip').click(function() {
      		 makeAlert;
      	 });
      	
      	 
		
		
	}

});

}(jQuery));