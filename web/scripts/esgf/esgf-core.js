/**
 *  ESGF name spaced
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

// general util functions
ESGF.util.toArray = function (obj) {
    var arr = [],
        id = "";
    for (id in obj) {
        arr.push({doc: obj[id]});
    }
    return arr;
};
