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
 * Pager component for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */


(function ($) {

/**
 * A pager widget for jQuery.
 *
 * <p>Heavily inspired by the Ruby on Rails will_paginate gem.</p>
 *
 * @expects this.target to be a list.
 * @class PagerWidget
 * @augments AjaxSolr.AbstractWidget
 */
AjaxSolr.PagerWidget = AjaxSolr.AbstractWidget.extend(
  /** @lends AjaxSolr.PagerWidget.prototype */
  {
  /**
   * How many links are shown around the current page.
   *
   * @field
   * @public
   * @type Number
   * @default 4
   */
  innerWindow: 5,

  /**
   * How many links are around the first and the last page.
   *
   * @field
   * @public
   * @type Number
   * @default 1
   */
  outerWindow: 1,

  /**
   * The previous page link label.
   *
   * @field
   * @public
   * @type String
   * @default "&laquo; previous"
   */
  prevLabel: '&laquo; Previous',

  /**
   * The next page link label.
   *
   * @field
   * @public
   * @type String
   * @default "next &raquo;"
   */
  nextLabel: 'Next &raquo;',

  /**
   * Separator between pagination links.
   *
   * @field
   * @public
   * @type String
   * @default ""
   */
  separator: ' ',

  /**
   * The current page number.
   *
   * @field
   * @private
   * @type Number
   */
  currentPage: null,

  /**
   * The total number of pages.
   *
   * @field
   * @private
   * @type Number
   */
  totalPages: null,

  /**
   * @returns {String} The gap in page links, which is represented by:
   *   <span class="pager-gap">&hellip;</span>
   */
  gapMarker: function () {
    return '<span class="pager-gap">&hellip;</span>';
  },

  /**
   * @returns {Array} The links for the visible page numbers.
   */
  windowedLinks: function () {
    var links = [];

    var prev = null;

    visible = this.visiblePageNumbers();
    for (var i = 0, l = visible.length; i < l; i++) {
      if (prev && visible[i] > prev + 1) links.push(this.gapMarker());
      links.push(this.pageLinkOrSpan(visible[i], [ 'pager-current' ]));
      prev = visible[i];
    }

    return links;
  },

  /**
   * @returns {Array} The visible page numbers according to the window options.
   */ 
  visiblePageNumbers: function () {
    var windowFrom = this.currentPage - this.innerWindow;
    var windowTo = this.currentPage + this.innerWindow;

    // If the window is truncated on one side, make the other side longer
    if (windowTo > this.totalPages) {
      windowFrom = Math.max(0, windowFrom - (windowTo - this.totalPages));
      windowTo = this.totalPages;
    }
    if (windowFrom < 1) {
      windowTo = Math.min(this.totalPages, windowTo + (1 - windowFrom));
      windowFrom = 1;
    }

    var visible = [];

    // Always show the first page
    visible.push(1);
    // Don't add inner window pages twice
    for (var i = 2; i <= Math.min(1 + this.outerWindow, windowFrom - 1); i++) {
      visible.push(i);
    }
    // If the gap is just one page, close the gap
    if (1 + this.outerWindow == windowFrom - 2) {
      visible.push(windowFrom - 1);
    }
    // Don't add the first or last page twice
    for (var i = Math.max(2, windowFrom); i <= Math.min(windowTo, this.totalPages - 1); i++) {
      visible.push(i);
    }
    // If the gap is just one page, close the gap
    if (this.totalPages - this.outerWindow == windowTo + 2) {
      visible.push(windowTo + 1);
    }
    // Don't add inner window pages twice
    for (var i = Math.max(this.totalPages - this.outerWindow, windowTo + 1); i < this.totalPages; i++) {
      visible.push(i);
    }
    // Always show the last page, unless it's the first page
    if (this.totalPages > 1) {
      visible.push(this.totalPages);
    }

    return visible;
  },

  /**
   * @param {Number} page A page number.
   * @param {String} classnames CSS classes to add to the page link.
   * @param {String} text The inner HTML of the page link (optional).
   * @returns The link or span for the given page.
   */
  pageLinkOrSpan: function (page, classnames, text) {
    text = text || page;

    if (page && page != this.currentPage) {
      return $('<a href="#"/>').html(text).attr('rel', this.relValue(page)).addClass(classnames[1]).click(this.clickHandler(page));
    }
    else {
      return $('<span/>').html(text).addClass(classnames.join(' '));
    }
  },

  /**
   * @param {Number} page A page number.
   * @returns {Function} The click handler for the page link.
   */
  clickHandler: function (page) {
    var self = this;
    return function () {
      self.manager.store.get('start').val((page - 1) * (self.manager.response.responseHeader.params && self.manager.response.responseHeader.params.rows || 10));
      self.manager.doRequest();
      return false;
    }
  },

  /**
   * @param {Number} page A page number.
   * @returns {String} The <tt>rel</tt> attribute for the page link.
   */
  relValue: function (page) {
    switch (page) {
      case this.previousPage():
        return 'prev' + (page == 1 ? 'start' : '');
      case this.nextPage():
        return 'next';
      case 1:
        return 'start';
      default: 
        return '';
    }
  },

  /**
   * @returns {Number} The page number of the previous page or null if no previous page.
   */
  previousPage: function () {
    return this.currentPage > 1 ? (this.currentPage - 1) : null;
  },

  /**
   * @returns {Number} The page number of the next page or null if no next page.
   */
  nextPage: function () {
    return this.currentPage < this.totalPages ? (this.currentPage + 1) : null;
  },

  /**
   * An abstract hook for child implementations.
   *
   * @param {Number} perPage The number of items shown per results page.
   * @param {Number} offset The index in the result set of the first document to render.
   * @param {Number} total The total number of documents in the result set.
   */
  renderHeader: function (perPage, offset, total) {},

  /**
   * Render the pagination links.
   *
   * @param {Array} links The links for the visible page numbers.
   */
  renderLinks: function (links) {
    if (this.totalPages) {
      links.unshift(this.pageLinkOrSpan(this.previousPage(), [ 'pager-disabled', 'pager-prev' ], this.prevLabel));
      links.push(this.pageLinkOrSpan(this.nextPage(), [ 'pager-disabled', 'pager-next' ], this.nextLabel));
      AjaxSolr.theme('list_items', this.target, links, this.separator);
    }
  },

  afterRequest: function () {
	  
	  if(ESGF.setting.storage) {
		  var fq = localStorage['fq'];
	  }
      
			   
	    	var perPage = parseInt(this.manager.response.responseHeader.params && this.manager.response.responseHeader.params.rows || 10);
    	    var offset = parseInt(this.manager.response.responseHeader.params && this.manager.response.responseHeader.params.start || 0);
    	    var total = parseInt(this.manager.response.response.numFound);

    	    // Normalize the offset to a multiple of perPage.
    	    offset = offset - offset % perPage;

    	    this.currentPage = Math.ceil((offset + 1) / perPage);
    	    this.totalPages = Math.ceil(total / perPage);

    	    //erase BOTH the pagination and the "extra display"
    	    $(this.target).empty();
    	    $('#pager-header').empty();
      
    	    if(ESGF.setting.storage) {
    	    	//only if there is a query should the pagination be displayed
        	    //if (fq != undefined) {
        	    if(Manager.store.values('fq') != 'type:Dataset,replica:false') {	
        			  this.renderLinks(this.windowedLinks());
        			  this.renderHeader(perPage, offset, total);
        	    }
        	    
        	    
        	    
    	    }
    	    
	  
    
  }
});

})(jQuery);
