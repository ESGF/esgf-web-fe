// $Id$

/**
 * Baseclass for all facet widgets.
 *
 * @class AbstractFacetWidget
 * @augments AjaxSolr.AbstractWidget
 */
AjaxSolr.AbstractFacetWidget = AjaxSolr.AbstractWidget.extend(
  /** @lends AjaxSolr.AbstractFacetWidget.prototype */
  {
  /**
   * The field to facet on.
   *
   * @field
   * @public
   * @type String
   */
  field: null,

  /**
   * The starting offset value of the displayed facet values.
   *
   * @field
   * @public
   * @type Integer
   */
  startingValue: 0,
  
  /**
   * The increment value of the pagination mechanism in the facet browser.
   *
   * @field
   * @public
   * @type Integer
   */
  incrementValue: 20,
  
  /**
   * @returns {Boolean} Whether any filter queries have been set using this
   *   widget's facet field.
   */
  isEmpty: function () {
    return !this.manager.store.find('fq', new RegExp('^-?' + this.field + ':'));
  },

  /**
   * Adds a filter query.
   *
   * @returns {Boolean} Whether a filter query was added.
   */
  add: function (value) {
    return this.changeSelection(function () {
      return this.manager.store.addByValue('fq', this.fq(value));
    });
  },

  /**
   * Removes a filter query.
   *
   * @returns {Boolean} Whether a filter query was removed.
   */
  remove: function (value) {
    return this.changeSelection(function () {
      return this.manager.store.removeByValue('fq', this.fq(value));
    });
  },

  /**
   * Removes all filter queries using the widget's facet field.
   *
   * @returns {Boolean} Whether a filter query was removed.
   */
  clear: function () {
    return this.changeSelection(function () {
      return this.manager.store.removeByValue('fq', new RegExp('^-?' + this.field + ':'));
    });
  },

  /**
   * Helper for selection functions.
   *
   * @param {Function} Selection function to call.
   * @returns {Boolean} Whether the selection changed.
   */
  changeSelection: function (func) {
    changed = func.apply(this);
    if (changed) {
      this.afterChangeSelection();
    }
    return changed;
  },

  /**
   * An abstract hook for child implementations.
   *
   * <p>This method is executed after the filter queries change.</p>
   */
  afterChangeSelection: function () {},

  /**
   * @param {String} value The value.
   * @returns {Function} Sends a request to Solr if it successfully adds a
   *   filter query with the given value.
   */
  clickHandler: function (value) {
    var self = this;
    return function () {
      if (self.add(value)) {
        self.manager.doRequest(0);
      }
      return false;
    }
  },

  /**
   * @param {String} divFieldId The jquery variable representing the dom object for facet values and counts.
   * @returns {Function} Retrieves the next set of facet values.  Increments by this.incrementValue.
   */
  nextClickHandler: function (divFieldId) {
    var self = this;
    return function () {
    	$("div#"+divFieldId).remove();
    	self.startingValue += self.incrementValue;
    	self.displayFacetValues();
      return false;
    }
  },
  
  /**
   * @param {String} divFieldId The jquery variable representing the dom object for facet values and counts.
   * @returns {Function} Retrieves the previous facet values.  Decrements by this.incrementValue.
   */
  prevClickHandler: function (divFieldId) {
    var self = this;
    return function () {
    	$("div#"+divFieldId).remove();
    	self.startingValue -= self.incrementValue;
    	self.displayFacetValues();
    	
      return false;
    };
  },
  
  /**
   * @returns {Function} Display the values (with counts) of the facets
   */
  displayFacetValues: function () {
	var self = this;
	var numFields = 0;
	for (var facet in self.manager.response.facet_counts.facet_fields[self.field]) {
		numFields += 1;
	}
	var maxCount = 0;
	var objectedItems = [];
	for (var facet in self.manager.response.facet_counts.facet_fields[self.field]) {
		var count = parseInt(self.manager.response.facet_counts.facet_fields[self.field][facet]);
		if (count > maxCount) {
			maxCount = count;
		}
		objectedItems.push({ facet: facet, count: count });
	}
	
	objectedItems.sort(function (a, b) {
		if(Manager.sortType == 'sortbycount') {
			return a.count > b.count ? -1 : 1;
		} else {
			return a.facet.value < b.facet.value ? -1 : 1;
		}
	});
	
	var divFieldID = 'facet_value_' + self.field;
	var stopValue = objectedItems.length;
	if(objectedItems.length > (self.startingValue + self.incrementValue)) {
		stopValue = (self.startingValue + self.incrementValue);
	}
	$(self.target).append('<div id="' + divFieldID + '"></div>');
	if(numFields > 0) {
		$('div#'+divFieldID).append('<div>');
		if(self.startingValue > 0){
			$('div#'+divFieldID).append(AjaxSolr.theme('prevLink',stopValue,objectedItems,divFieldID,self));
		}
		if(stopValue == (this.startingValue + this.incrementValue)) {
			$('div#'+divFieldID).append(AjaxSolr.theme('nextLink',divFieldID,self));
		}
		$('div#'+divFieldID).append('</div>');
		$('div#'+divFieldID).append(AjaxSolr.theme('facet_content',stopValue,objectedItems,self));
	}
	
  },
  
  
  /**
   * @param {String} value The value.
   * @returns {Function} Sends a request to Solr if it successfully removes a
   *   filter query with the given value.
   */
  unclickHandler: function (value) {
    var self = this;
    return function () {
      if (self.remove(value)) {
        self.manager.doRequest(0);
      }
      return false;
    }
  },

  /**
   * @param {String} value The facet value.
   * @param {Boolean} exclude Whether to exclude this fq parameter value.
   * @returns {String} An fq parameter value.
   */
  fq: function (value, exclude) {
    // If the field value has a space or a colon in it, wrap it in quotes,
    // unless it is a range query.
    if (value.match(/[ :]/) && !value.match(/[\[\{]\S+ TO \S+[\]\}]/)) {
      value = '"' + value + '"';
    }
    return (exclude ? '-' : '') + this.field + ':' + value;
  }
});
