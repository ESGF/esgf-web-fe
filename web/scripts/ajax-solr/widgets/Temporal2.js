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
        afterRequest: function () {
            var self = this;
            $("div#temporal a[rel]").overlay({
                mask: {opacity: 0.5, color: '#000'},
                effect: 'apple',
        		left: "30%",
        		top: "2%",
                onBeforeLoad: function() {
                    $('.apple_overlay').css({'width' : '440px'});	
			        var wrap = this.getOverlay().find(".contentWrap");
                    wrap.load(this.getTrigger().attr("href"));			
                },
        
                onLoad: function() {
                    var dateFrom = '';
                    var dateTo ='';
                    

        			LOG.debug('\tIn Temporal.onLoad');
                    /*
                     * Date picker object created when the temporal search is activated 
                     */
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
                                dateTo = document.getElementById('to');
                                dates.not(this).datepicker("option",option,date);
                            }
                        });
                    }); //end datepicker
                    $("button#submitTemporal").button({ });
                    $(".overlay_header").show();
                    $(".overlay_content").show();
                    $(".overlay_footer").show();
                    $(".overlay_border").show();
                    $('div#tButton').click(function() {
                        //call the helper method to assemble the fq and execute it
                        self.executeDateQuery(dateFrom,dateTo);
                    });
                    

        			LOG.debug('\tEnd In Temporal.onLoad');
        			
                },//end onLoad
                
                onClose: function() {
                    $(".overlay_header").hide();
                    $(".overlay_content").hide();
                    $(".overlay_footer").hide();
                    $(".overlay_border").hide();
                }//end onClose
           });
    
       },//end afterRequest
  
    
       /*
        * Function for creating the temporal filter query
        * Utilizes both the datetime_start and datetime_stop fields from solr
        * The default searches each from [* TO *]
        */
	   executeDateQuery: function (dateFrom,dateTo){
		   var datetime_start, datetime_startFQ,
           datetime_stop, datetime_stopFQ; 
        
           //datetime_start
           if(dateFrom.value) {
               datetime_start = dateFrom.value + 'T00:00:00Z';
           } else {
               datetime_start = '*';
           }
           //datetime_stop
           if(dateTo.value) {
               datetime_stop = dateTo.value + 'T00:00:00Z';
           } else {
               datetime_stop = '*';
           }
           datetime_startFQ = 'datetime_start:[' + datetime_start + ' TO *]';
           datetime_stopFQ = 'datetime_stop:[* TO ' + datetime_stop + ']';
           Manager.store.addByValue('fq', datetime_startFQ );	
           Manager.store.addByValue('fq', datetime_stopFQ );	

           //add the datetime start and stop parameters to esgf_fq localstorage
           ESGF.localStorage.put('esgf_fq',datetime_startFQ,datetime_startFQ);
           ESGF.localStorage.put('esgf_fq',datetime_stopFQ,datetime_stopFQ);
           
           
           ESGF.localStorage.put('esgf_queryString','start='+datetime_start,'start='+datetime_start);
           ESGF.localStorage.put('esgf_queryString','stop='+datetime_stop,'stop='+datetime_stop);
			
           //ESGF.localStorage.put('esgf_queryString','text:'+value,'query='+value);
			
           /*
           start=2007-02-12T04:30:02Z&end=2007-03-11T02:28:00Z
           ESGF.localStorage.put('esgf_queryString','text:'+value,'query='+value);
			*/
           
           /*
         //add to the esgf_queryString localstore
			value = facet + '=' + facetValue;
			ESGF.localStorage.put('esgf_queryString',key,value);
			*/
           
           Manager.doRequest(0);
	  }//end executeDateQuery
	
    });

}(jQuery));