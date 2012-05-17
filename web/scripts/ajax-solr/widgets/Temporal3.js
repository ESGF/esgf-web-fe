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
 * Temporal component for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */

(function ($) {
    AjaxSolr.TemporalWidget = AjaxSolr.AbstractWidget.extend({
    	
    	dateFrom: '*',
		dateTo: '*',
    
    	beforeRequest: function() {
    		
    	},
    	
    	jj: function() {
    		alert('jjjj');
    	},
    	
        afterRequest: function () {

    		var self = this;
        	
           
        	$('a#temporal').click(function() {
                
            	$( "#dialog" ).dialog({

            		width: 500,
            		maxWidth: 500,
            		height: 350,
            		maxHeight: 350,
            		show: 'blind',
            		
            	
            		open: function() {
                		
                		var dateFrom = '';
                        var dateTo ='';
                        
                        $(function() {
                            dates = $( "#from, #to" ).datepicker({
                                defaultDate: "+1w",
                                dateFormat: "yy-mm-dd",
                                changeMonth: true,
                                changeYear: true,
                                showOn: "button",
                                buttonImage: "images/temporal_overlay/calendar.png",
                                buttonImageOnly: true,
                                onSelect: function( selectedDate ) {
                                    var option = this.id === "from" ? "minDate" : "maxDate",
                                    instance = $( this ).data( "datepicker");
                                    date = $.datepicker.parseDate(
                                    instance.settings.dateFormat,
                                    selectedDate, instance.settings );
                                    dateFrom = document.getElementById('from');
                                    self.dateFrom = dateFrom.value;
                                    dateTo = document.getElementById('to');
                                    self.dateTo = dateTo.value;
                                    dates.not(this).datepicker("option",option,date);
                                }
                            });
                            
                            
                        }); //end datepicker
                        
                        
                	},
            	
                	buttons: {
                		Submit: function() {
                			$( this ).dialog( "close" );
                			
                			self.executeDateRequest();
                		}
                	}
            		
            	})
            	
            	
            	
        	});
        	
        	$('div#tButton').click(function() {
                 self.executeDateRequest();
            });
        	
        	
       },//end afterRequest
  
       /*
        * Function for creating the temporal filter query
        * Utilizes both the datetime_start and datetime_stop fields from solr
        * The default searches each from [* TO *]
        */
       executeDateRequest: function() {
    	   var self = this;
    	   
    	   var datetime_start, datetime_startFQ,
           datetime_stop, datetime_stopFQ; 

    	   
    	   //datetime_start
           if(self.dateFrom == '*' || self.dateFrom == '') {
               datetime_start = '*';
           } else {
               datetime_start = self.dateFrom + 'T00:00:00Z';
           }
           //datetime_stop
           if(self.dateTo == '*' || self.dateTo == '') {
               datetime_stop = '*';
           } else {
               datetime_stop = self.dateTo + 'T00:00:00Z';
           }
           
           
           
           var startStr = 'start='+datetime_start;
           var stopStr = 'end='+datetime_stop;
           

           ESGF.localStorage.put('esgf_fq',startStr,startStr);
           ESGF.localStorage.put('esgf_fq',stopStr,stopStr);
           ESGF.localStorage.put('esgf_queryString',startStr,startStr);
           ESGF.localStorage.put('esgf_queryString',stopStr,stopStr);
           /*
           datetime_startFQ = 'datetime_start:[' + datetime_start + ' TO *]';
           datetime_stopFQ = 'datetime_stop:[* TO ' + datetime_stop + ']';
           Manager.store.addByValue('fq', datetime_startFQ );	
           Manager.store.addByValue('fq', datetime_stopFQ );	

           //add the datetime start and stop parameters to esgf_fq localstorage
           ESGF.localStorage.put('esgf_fq',datetime_startFQ,datetime_startFQ);
           ESGF.localStorage.put('esgf_fq',datetime_stopFQ,datetime_stopFQ);
           
           
           ESGF.localStorage.put('esgf_queryString','start='+datetime_start,'start='+datetime_start);
           ESGF.localStorage.put('esgf_queryString','stop='+datetime_stop,'stop='+datetime_stop);
		*/
           //$('a#temporal').unbind('click');
           //$('#dialog').unbind('dialog');
           
           Manager.doRequest(0);
    	   
       }
       
       
	
    });

}(jQuery));
