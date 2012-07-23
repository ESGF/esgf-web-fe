

//general functions for localStorage

/**
 * DOCUMENT ME!
 */
Storage.prototype.setObject = function(key,value) {
	this.setItem(key,JSON.stringify(value));
};

/**
 * DOCUMENT ME!
 */
Storage.prototype.getObject = function(key) {
	return JSON.parse(this.getItem(key));
};

/**
 * DOCUMENT ME!
 */
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
};

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.toString = function(category) {
	
	if(localStorage.getItem(category) == null) {
		ESGF.localStorage.initialize(category);
	}
	
	var map = JSON.parse(localStorage.getItem(category));
	
	return JSON.stringify(map);

},

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.search = function(category, searchTerm) {
	
		if(localStorage.getItem(category) == null) {
			ESGF.localStorage.initialize(category);
		}
		
		var map = JSON.parse(localStorage.getItem(category));
		
		var found = false;
		
		//search through the keys
		for(var key in map) {
			if(key == searchTerm) {
				found = true;
			}
		}
		return found;
		
};

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.get = function(category, key) {
	
		if(localStorage.getItem(category) == null) {
			ESGF.localStorage.initialize(category);
		}
		
		var map = JSON.parse(localStorage.getItem(category));
		
		return map[key];
		
};

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.getAll = function(category) {
	
		if(localStorage.getItem(category) == null) {
			ESGF.localStorage.initialize(category);
		}
		
		var map = JSON.parse(localStorage.getItem(category));
		return map;
	
};

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.put = function(category, key, value) {
		
		var added = false;
	
		if(localStorage.getItem(category) == null) {
			ESGF.localStorage.initialize(category);
		}
		
		var map = JSON.parse(localStorage.getItem(category));
		
		var canPut = true;
		for (var mapKey in map) {
			if (key == mapKey) {
				canPut = false;
			}
		}
		//If there is no duplicate,
		//add item to the map and place it back in the localStorage category map
		if(canPut) {
			added = true;
			map[key] = value;		
			localStorage.setItem(category,JSON.stringify(map));
		}
		
		
		return added;
};

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.update = function(category, key, value) {

		var map = JSON.parse(localStorage.getItem(category));
		var canUpdate = false;
		for (var mapKey in map) {
			if (key == mapKey) {
				canUpdate = true;
			}
		}
		if(canUpdate) {
			map[key] = value;
			localStorage.setItem(category,JSON.stringify(map));
		}

	
};

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.removeAll = function(category) {
	
		
		if(localStorage.getItem(category) == null) {
			ESGF.localStorage.initialize(category);
		}
		
		var map = JSON.parse(localStorage.getItem(category));
		
		delete localStorage[category];
		
};

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.remove = function(category, key) {
	
		
		if(localStorage.getItem(category) == null) {
			ESGF.localStorage.initialize(category);
		}
		
		var map = JSON.parse(localStorage.getItem(category));
		
		var canRemove = false;
		for (var mapKey in map) {
			if (key == mapKey) {
				canRemove = true;
			}
		}
		if(canRemove) {
			delete map[key];
			localStorage.setItem(category,JSON.stringify(map));
		}
	
};

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.append = function(category, key, value) {
	
		if(localStorage.getItem(category) == null) {
			ESGF.localStorage.initialize(category);
		}
		
		var map = JSON.parse(localStorage.getItem(category));
		
		
		if(map[key] == undefined) {
			map[key] = value;
		} else {
			var keyStr = map[key];
			if(keyStr.search(value) != -1) {
				keyStr = value + ';' + keyStr;
				map[key] = keyStr;
			}
		}
		localStorage.setItem(category,JSON.stringify(map));
		
		
	
	
};

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.removeFromValue = function(category, key, value) {

		
		if(localStorage.getItem(category) == null) {
			ESGF.localStorage.initialize(category);
		}
		
		var map = JSON.parse(localStorage.getItem(category));
		
		if(map != undefined) {
			if(map[key] == undefined) {
				map[key] = value;
			} else {
				var keyStr = map[key];
				if(keyStr.search(value) != -1) {
					var newStr = keyStr.replace(value,"");
					map[key] = newStr;
				}
			}

			localStorage.setItem(category,JSON.stringify(map));
			
		}
		
}; 

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.toKeyArr = function(category) {
	
		/*
		if(localStorage.getItem(category) == null) {
			ESGF.localStorage.initialize(category);
		}
		*/
	
		if(localStorage.getItem(category) != null) {
			var dataCartMap = ESGF.localStorage.getAll(category);

			var arr = new Array();
			
			for(var key in dataCartMap) {
				
				if(key != '') {       			
		    		arr.push(key);
				}
			}
			
			return arr;
		} else {
			return null;
		}
	
		
		

	
};

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.printMap = function(category) {

		if(localStorage.getItem(category) == null) {
			ESGF.localStorage.initialize(category);
		}
		
		var map = ESGF.localStorage.getAll(category);

		
		LOG.debug("*****Map of " + category + "****");
		for(var key in map) {
			LOG.debug("key: " + key + " -> " + map[key]);
		}
		LOG.debug("*****End Map of " + category + "****");

	
}; 

/**
 * DOCUMENT ME!
 */
ESGF.localStorage.initialize = function(category) {
	localStorage.setItem(category,JSON.stringify({'':''}));
}
