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

var core   = EGGF.namespace("ESGF.core");
var search = ESGF.namespace("ESGF.search");
