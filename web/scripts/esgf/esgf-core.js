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
 *  ESGF name spaced
 *
 *  @author Feiyi Wang
 */

var ESGF = ESGF || {};

ESGF.namespace = function (ns_string) {
    var parts = ns_string.split("."),
        parent = ESGF,
        i;
    // strip redundant leading global
    if (parts[0] === "ESGF") {
        parts = parts.slice(1);
    }

    for (i=0; i < parts.length; i+=1) {
        // create a property if it doesn't exist
        if (typeof parent[parts[i] === "undefined"]) {
            parent[parts[i]] = {};
        }
        parent = parent[parts[i]];
    }
    return parent;
};


// predefine a few name spaces

var esgf_core    = ESGF.namespace("ESGF.core");
var esgf_search  = ESGF.namespace("ESGF.search");
var esgf_setting = ESGF.namespace("ESGF.setting");
var esgf_util    = ESGF.namespace("ESGF.util");
var esgf_localStorage = ESGF.namespace("ESGF.localStorage");

//define common logging

ESGF.core.LOG = log4javascript.getLogger("esgf");
//ESGF.core.popUpAppender = new log4javascript.PopUpAppender();
//ESGF.core.LOG.addAppender(popUpAppender);
ESGF.core.bcAppender = new log4javascript.BrowserConsoleAppender();
ESGF.core.LOG.addAppender(ESGF.core.bcAppender);

var LOG = ESGF.core.LOG;
LOG.debug("Logging defined");


// predefine a few properties
ESGF.search.selected = ESGF.search.selected || {};

// flag for using html5 localStorage
ESGF.setting.storage = true;

//distributed search switch - need to revisit this
ESGF.setting.searchType = 'local';

//for admin's usermanagement interface - currentUserName
ESGF.setting.currentUserName = 'default';

//for admin's usermanagement interface - currentGroupName
ESGF.setting.currentGroupName = 'default';

//indication of datacart method (old vs new)
ESGF.setting.dataCartVersion = 'v1';


//need a variable representing the current state of the datacart
ESGF.setting.showAllContents = 'true';

//need a variable representing the manner in which to get the sharding information
ESGF.setting.getShards = 'solrconfig';

/**
 * Proxy for creating the template for file downloads.
 *
 * @field
 * @public
 * @type String
 */
ESGF.search.fileDownloadTemplateProxyUrl = '/esgf-web-fe/solrfileproxy',


ESGF.setting.facetFile = '/esgf-web-fe/facetfileproxy',





// general util functions
ESGF.util.toArray = function (obj) {
    var arr = [],
        id = "";
    for (id in obj) {
        arr.push({doc: obj[id]});
    }
    return arr;
};





