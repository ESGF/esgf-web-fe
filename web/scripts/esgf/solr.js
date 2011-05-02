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
 *
 */



(function ($) {

    $(function () {
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

         var fields = ['project', 'model', 'experiment', 'frequency', 'realm', 'instrument', 'variable', 'cf_variable', 'gcmd_variable']; //['project' , 'model', 'experiment', 'frequency', 'realm', 'instrument', 'variable', 'cf_variable', 'gcmd_variable'];

         for (var i = 0, l = fields.length; i < l; i++) {
             Manager.addWidget(new AjaxSolr.FacetBrowserWidget({
               id: fields[i],
               target: '#' + fields[i],
               field: fields[i]
             }));
           }

         
         Manager.addWidget(new AjaxSolr.CurrentSearchWidget({
              id: 'currentsearch',
              target: '#current-selection'
            }));

         
         Manager.addWidget(new AjaxSolr.AutocompleteWidget({
              id: 'text',
              target: '#search-box',
              field: 'text',
              fields: [ 'project', 'model' , 'experiment']
            }));

         
         Manager.addWidget(new AjaxSolr.GeospatialSearchWidget({
              id: 'geo_browse'
            }));
		

         Manager.addWidget(new AjaxSolr.TemporalWidget({
              id: 'temp-browse'
            }));

         Manager.addWidget(new AjaxSolr.MetadataWidget({
                id: 'metadata-browse'
              }));
         
         Manager.addWidget(new AjaxSolr.FacetSideBarWidget({
             id: 'facet-sidebar'
           }));

         
         
         Manager.init();
         Manager.store.addByValue('q','*:*');

         
         var params = {
                  'facet': true,
                  'facet.field': fields,
                  'facet.limit': 20,
                  'facet.mincount': 1,
                  'f.topics.facet.limit': 50,
                  'json.nl': 'map'
                };

         for (var name in params) {
            if(name == 'facet.field') {
                for(var i=0;i<fields.length;i++) {
                    Manager.store.addByValue(name,fields[i]);
                }
            }
            else {
                Manager.store.addByValue(name, params[name]);
            }
         }

        
         


         Manager.doRequest();
    });



})(jQuery);

