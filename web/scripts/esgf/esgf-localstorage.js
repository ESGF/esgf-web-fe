//general functions for localStorage

Storage.prototype.setObject = function(key,value) {
	this.setItem(key,JSON.stringify(value));
}

Storage.prototype.getObject = function(key,value) {
	return JSON.parse(this.getItem(key));
}


ESGF.localStorage.search = function(category, searchTerm) {
	
	if(localStorage[category] == undefined) {
		localStorage.setObject(category,{'' : ''});
	}
	var map = localStorage.getObject(category);
	var found = false;
	
	//search through the keys
	for(var key in map) {
		if(key == searchTerm) {
			found = true;
		}
	}
	return found;
};


ESGF.localStorage.get = function(category, key) {
	if(localStorage[category] == undefined) {
		localStorage.setObject(category,{'' : ''});
	}
	var map = localStorage.getObject(category);
	return map[key];
};

ESGF.localStorage.getAll = function(category) {
	if(localStorage[category] == undefined) {
		localStorage.setObject(category,{'' : ''});
	}
	var map = localStorage.getObject(category);
	return map;
};

ESGF.localStorage.put = function(category, key, value) {
	if(localStorage[category] == undefined) {
		localStorage.setObject(category,{'' : ''});
	}
	var map = localStorage.getObject(category);
	var canPut = true;
	for (var mapKey in map) {
		if (key == mapKey) {
			canPut = false;
		}
	}
	//If there is no duplicate,
	//add item to the map and place it back in the localStorage category map
	if(canPut) {
		map[key] = value;		
		localStorage.setObject(category,map);
	}
};

ESGF.localStorage.update = function(category, key, value) {
	if(localStorage[category] == undefined) {
		localStorage.setObject(category,{'' : ''});
	}
	var map = localStorage.getObject(category);
	var canUpdate = false;
	for (var mapKey in map) {
		if (key == mapKey) {
			canUpdate = true;
		}
	}
	if(canUpdate) {
		map[key] = value;
		localStorage.setObject(category,map);
	}
};

ESGF.localStorage.remove = function(category, key, value) {
	if(localStorage[category] == undefined) {
		localStorage.setObject(category,{'' : ''});
	}
	var map = localStorage.getObject(category);
	var canRemove = false;
	for (var mapKey in map) {
		if (key == mapKey) {
			canRemove = true;
		}
	}
	if(canRemove) {
		delete map[key];
		localStorage.setObject(category,map);
	}
};

ESGF.localStorage.toKeyArr = function(category) {
	if(localStorage[category] == undefined) {
		localStorage.setObject(category,{'' : ''});
	}
	
	var arr = new Array();
	
	var dataCartMap = ESGF.localStorage.getAll(category);
	for(var key in dataCartMap) {
		if(key != '') {       			
    		arr.push(key);
		}
	}
	return arr;
};
