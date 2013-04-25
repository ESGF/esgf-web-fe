/* Copyright � 2011 , UT-Battelle, LLC All rights reserved
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
 * Experimental Solr Front for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */


(function ($) {
	
	AjaxSolr.DataCartExpandFeatureWidget = AjaxSolr.AbstractWidget.extend({

		
		view_more_files_tag: 'view_more_files_',
		
		view_first_files_tag: 'view_first_files_',
		
		no_files_message: 'NOTE: There are no files in this dataset that match the search criteria',
		
		
		td_file_left: 'width: 40px',
		td_file_middle: '',
		td_right_middle: '',
		
		show_files: 'Show Files',
		hide_files: 'Hide Files',
		expanding_files: 'Expanding...',
		
		/**
		 * 
		 */
		searchConstraints: null,

		/**
		 * An array of dataset ids selected by the user in the data cart
		 */
		selected_arr: null,
		
		/**
		 * DOCUMENT ME
		 */
		beforeRequest: function () {
			
			var self = this;

			//grab the search constraints
	    	self.searchConstraints = ESGF.localStorage.toKeyArr('esgf_fq');
	    	
	    	
	    	//grab all the keys from the datacart map and place in an array
	    	self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');

			//kill the show all files link
			$('a.showAllFiles_short').die('click');
			
			//kill the span of show files
			$('span.show_files').die('click');
			
			//kill the view more files link
			$('a.view_more_files_short').die('click');
			
			
			
		},
		
		
		writeEmptyFileMessage: function(data) {
			
			var self = this;
			
			var view_first_files_tag = self.view_first_files_tag + 'initial_true_' + ESGF.datacart.replaceChars(data.doc.datasetId);
			
	    	
			var appendedFiles = '';
			
			appendedFiles = '<tr class="' + view_first_files_tag + '">';
			//appendedFiles += '<td style="width: 40px"></td>';
			appendedFiles += '<td style="' + self.td_file_left + '"></td>';
			appendedFiles += '<td style="width: 300px;font-size:13px">';
			appendedFiles += '	<div style="word-wrap: break-word;font-weight:bold;color:gray;font-style:italic;font-weight:bold;color:gray;margin-top:5px" class="datasetId">';
			
			appendedFiles += self.no_files_message;
			
			appendedFiles += '</div>';
			appendedFiles += '</td>';
			appendedFiles += '</tr>';
			
			return appendedFiles;
			
		},
		
		
		appendFileData: function (data,openid,initial) {

			
			var self = this;
			
			var initialStr = 'initial_' + new String(initial);
			
			var appendedFiles = '';
			
			//get the file length from the data retrieved
			var fileLength = data.doc.files.file.length;

			//if there is one and only one file in the dataset
			if(fileLength == undefined) {
				var fileArray = new Array();
				fileArray.push(data.doc.files.file);
				data.doc.files['file'] = fileArray;
				fileLength = data.doc.files.file.length;
			}

			//traverse all the files names retrieved and find the associated service,url,and mime
			for(var j=0;j<data.doc.files.file.length;j++) {
				if(data.doc.files.file[j].services.service == 'HTTPServer' ||
    					data.doc.files.file[j].services.service == 'OPENDAP' || 
    					data.doc.files.file[j].services.service == 'SRM' ||
    					data.doc.files.file[j].services.service == 'GridFTP') {
					
					var serviceArray = new Array();
					serviceArray.push(data.doc.files.file[j].services.service);
					data.doc.files.file[j].services['service'] = serviceArray;
					
					var urlsArray = new Array();
					urlsArray.push(data.doc.files.file[j].urls.url);
					data.doc.files.file[j].urls['url'] = urlsArray;
					/*
					var mimesArray = new Array();
					mimesArray.push(data.doc.files.file[j].mimes.mime);
					data.doc.files.file[j].mimes['mime'] = mimesArray;
					*/
				}
			}
			//loop over the number of files
			for(var i=0;i<fileLength;i++) 
			{
				var displayed_files_tag = self.view_first_files_tag + initialStr + '_' + ESGF.datacart.replaceChars(data.doc.datasetId);
					
				appendedFiles += '<tr class="' + displayed_files_tag + '">';
				
				appendedFiles += '<td style="' + self.td_file_left + '">' +
				 '<input style="margin-left: 10px;display:none"' + 
	  					'type="checkbox"' + 
	  					'checked="true"' + 
	  					'/>' +
	  					'</td>';

				appendedFiles += '<td style="width: 425px;padding-left:10px;font-size:11px;">' +
				 '<div style="word-wrap: break-word;">';
			 	 
				appendedFiles += '<span style="font-weight:bold">' + data.doc.files.file[i].fileId + '</span>' +
				 '	<br />';
				if(data.doc.files.file[i].technotes.technote != undefined) {
					 appendedFiles += '<span style="font-style:italic">Technote: <a href="' + data.doc.files.file[i].technotes.technote + '" target="_blank">TECHNOTE</a>' + '</span>' +
					 ' <br />';
				 } 
				appendedFiles += '<span style="font-style:italic">' + 'tracking_id: ' + data.doc.files.file[i].tracking_id + '</span>' +
				 '  <br />' +
				 '<span style="font-style:italic">checksum: ' + data.doc.files.file[i].checksum + ' (' + data.doc.files.file[i].checksum_type + ')' + '</span>' +
				 '  <br />';
				 //alert('technote: ' + data.doc.files.file[i].technotes.technote);
				 
				 appendedFiles += '</div>' +
				 '</td>';
							
				appendedFiles += '<td style="float-right;font-size:11px;text-align:right">';
				
				for(var j=0;j<data.doc.files.file[i].services.service.length;j++) {
					var service = data.doc.files.file[i].services.service[j];
					var url = data.doc.files.file[i].urls.url[j];
					var file_id = data.doc.files.file[i].fileId;
					if(service == 'HTTPServer') {
						service = 'HTTP';
					}
					
					if(service == 'GridFTP') {
						if(ESGF.setting.globusonline) {
							appendedFiles += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right">' +
							                 '<span class="file_id" style="display:none">' + file_id + '</span>' + 
							                 '<span class="globus_url" style="display:none">' + url + '</span>' +
							                 '<a style="cursor:pointer" class="go_individual_gridftp_short">' + 'Globus Online' + '</a> </span>';
							
						}
					} else if(service == 'SRM') {
						appendedFiles += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right">' +
		                 '<span class="file_id" style="display:none">' + file_id + '</span>' + 
		                 '<span class="srm_urll" style="display:none">' + url + '</span>' +
		                 '<a style="cursor:pointer" class="single_srm">' + 'SRM' + '</a> </span>';
		
					} 
						else {
						if(openid != 'anonymousUser') {
							appendedFiles += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right"> <a href="'  + url  + '?openid=' + openid +  '" ' + 'target="_blank">' + service + '</a> </span>';
						} else {
							appendedFiles += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right"> <a href="'  + url  + '" ' + 'target="_blank">' + service + '</a> </span>';
						}
					}
					
				}
				appendedFiles += '</tr>';
			
			}
			return appendedFiles;
			
			
		},
		
		
		afterRequest: function() {
			

			var self = this;
			
		    //$('a.showAllFiles_short').live('click',function() {
			$('span.show_files').live('click',function() {
		    	
				var openid = $('span#principal_username').html();
		    	
		    	//extract the dataset Id from the span tag
				var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();
				
				//alert($(this).parent().find('a.hideAllFiles_short').html());//.find('span.hideAllFiles_short').attr('style'));
				
				var hideFilesStyle = $(this).parent().find('a.hideAllFiles_short').attr('style');//html();
				
				var showFilesDisplay = false;
				// display none is found, then the page is displaying "show files"
				if(hideFilesStyle.search('display:none') > -1) {
					showFilesDisplay = true;
				}
				
				//alert('hideFilesStyle: ' + hideFilesStyle + ' displayName: ' + showFilesDisplay);
				//alert(this.innerHTML + ' ' + self.hide_files);
				
				var parentElement = $(this).parent();
				
				var showFilesElement = parentElement.find('a.showAllFiles_short');
				//alert('display: ' + parentElement.find('a.showAllFiles_short').attr('style'));
				
				
				//detects if there is a 'none' in the style attr.
				//if this is true, then showing the files is hidden and enters the first code block,
				//meaning the event is the removal of the file content
				//
				if(parentElement.find('a.showAllFiles_short').attr('style').search('none') > -1) {	
					
					var idStr = selectedDocId;
					
					
					
					//need to remove all of the content displayed within this dataset
					
					//remove all the files from the additional list
					var view_next_files_tag = self.view_first_files_tag + 'initial_false' + '_' + ESGF.datacart.replaceChars(idStr);
					$('.' + view_next_files_tag).remove();
					
					//remove all the files from the initial list 
					var view_first_files_tag = self.view_first_files_tag + 'initial_true' + '_' + ESGF.datacart.replaceChars(idStr);
					$('.' + view_first_files_tag).remove();
					
					//remove the "view files"
					var view_more_files_tag = self.view_files_tag + ESGF.datacart.replaceChars(idStr);//'view_more_files_' + replaceChars(idStr);
					$('.' + view_more_files_tag).remove();
					
					parentElement.find('a.hideAllFiles_short').hide();
					parentElement.find('a.showAllFiles_short').show();
					
				} else {

	                var parentElement = $(this).parent();
					parentElement.find('a.showAllFiles_short').hide();
					parentElement.find('span.showAllFiles_short').show();
					
	                var idStr = selectedDocId;
	                
	                
					var peerStr = ESGF.datacart.getIndividualPeer(idStr);
					var technoteStr = ESGF.datacart.getTechnoteStr();
			    	var fqParamStr = ESGF.datacart.getFqParamStr();
			    	
			    	var constraints = fqParamStr;
			    	
			    	var queryStr = 
			    	{
			    			"dataset_id" : idStr, 
			    			"isInitialQuery" : "true",
			    			"limit" : ESGF.setting.fileCounter,
			    			"isShowAll" : ESGF.setting.showAllContents,
							"constraints" : constraints, 
							"peerStr" : peerStr, 
							"technotesStr" : technoteStr, 
					};
			    	
			    	//NEED TO FIX THIS!!!!
			    	var url = '/esgf-web-fe/solrfileproxy3/datacart/';
					
					
			    	//CHANGE ME!
			    	LOG.debug('---before expansion---');
			    	LOG.debug('dataset_id :' + idStr);
			    	LOG.debug("isInitialQuery: " + "true");
			    	LOG.debug("limit: " + ESGF.setting.fileCounter);
			    	LOG.debug("isShowAll: " + ESGF.setting.showAllContents);
			    	LOG.debug("constraints: " + constraints);
			    	LOG.debug("peerStr: " + peerStr) 
			    	LOG.debug("technotesStr: " + technoteStr); 
			    	LOG.debug('----------------------');
					
					
			    	//initial ajax call for first x number of files in dataset
					$.ajax({
						url: url,
						global: false,
						type: "POST",
						data: queryStr,
						dataType: 'json',
						success: function(data) {
							
							//no files
							if(data.doc.files.file == undefined) {
								
								var tagid = 'file_rows_' + ESGF.datacart.replaceChars(data.doc.datasetId);
								
								var appendedFiles = self.writeEmptyFileMessage(data);

								$('.'+tagid).after(appendedFiles);
								
								//self.innerHTML=self.hide_files;
								

								parentElement.find('span.showAllFiles_short').hide();
								parentElement.find('a.hideAllFiles_short').show();
								
							} else {

								var fileLength = data.doc.files.file.length;
								
								
								var tagid = 'file_rows_' + ESGF.datacart.replaceChars(data.doc.datasetId);
								
								//add the initial flag
								var initial = true;
								
								var appendedFiles = self.appendFileData(data, openid, initial);


								var view_more_tag = '<tr class="' + self.view_files_tag + ESGF.datacart.replaceChars(idStr) + '">';
								
								if(fileLength >= ESGF.setting.fileCounter) {
									appendedFiles += view_more_tag;
									appendedFiles += '<td></td>';
									appendedFiles += '<td style="display:none"><span class="datasetId">' + data.doc.datasetId + '</td>';
									appendedFiles += '<td><a class="view_more_files_short" style="cursor:pointer;font-size:11px">' + 'View more files' + '</a></td>';
									appendedFiles += '</tr>';
									
								}

								
								$('.'+tagid).after(appendedFiles);
								
								parentElement.find('span.showAllFiles_short').hide();
								parentElement.find('a.hideAllFiles_short').show();
								
							}
							
						},
						error: function() {
							alert('Error in expanding files for dataset ');
							//alert('Error in expanding files for dataset ');
						}
					});
					
					
					
						
				}

				
			});
			
			
		    
			
			$('a.view_more_files_short').live('click',function() {

				if(this.innerHTML == "View more files") {
				

					var openid = $('span#principal_username').html();

					//extract the dataset Id from the span tag
					var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();
					
					var idStr = selectedDocId;
					var peerStr = ESGF.datacart.getIndividualPeer(idStr);
					var technoteStr = ESGF.datacart.getTechnoteStr();
			    	var fqParamStr = ESGF.datacart.getFqParamStr();

			    	var constraints = fqParamStr;
			    	
			    	/* OLD
			    	var queryStr = {"idStr" : idStr, 
							"peerStr" : peerStr, 
							"technotesStr" : technoteStr, 
							"showAllStr" : ESGF.setting.showAllContents, 
							"fqStr" : fqParamStr, 
							"initialQuery" : "false",
	    					"fileCounter" : ESGF.setting.fileCounter};
					
			    	//NEED TO FIX THIS!!!!
			    	selectedDocId = 'aa.gov';
			    	selectedDocId = Url.encode(selectedDocId);
			    	var url = '/esgf-web-fe/solrfileproxy2/datacart/'+selectedDocId;

			    	//CHANGE ME!
			    	//queryStr['peerStr'] = 'localhost';
					*/
			    	
			    	
			    	var queryStr = 
			    	{
			    			"dataset_id" : idStr, 
			    			"isInitialQuery" : "false",
			    			"limit" : ESGF.setting.fileCounter,
			    			"isShowAll" : ESGF.setting.showAllContents,
							"constraints" : constraints, 
							"peerStr" : peerStr, 
							"technotesStr" : technoteStr, 
					};
			    	
			    	
			    	var tagid = 'file_rows_' + ESGF.datacart.replaceChars(idStr);
					
			    	var url = '/esgf-web-fe/solrfileproxy3/datacart/';
					
			    	//initial ajax call for first x number of files in dataset
					$.ajax({
						url: url,
						global: false,
						type: "POST",
						data: queryStr,
						dataType: 'json',
						success: function(data) {
							
							var tagid = self.view_files_tag + ESGF.datacart.replaceChars(idStr);//'view_more_files_' + replaceChars(idStr);
							
							var initial = false;
							var appendedFiles = self.appendFileData(data, openid,initial);
							$('.'+tagid).after(appendedFiles);
							
						},
						error: function() {
							alert('Error in expanding files for dataset ' + data.doc.datasetId);
						}
					});
					
					
					//set collapsing files
					this.innerHTML = 'Collapse files';
					
				
				} else {
					
					//extract the dataset Id from the span tag
					var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();
					
					var idStr = selectedDocId;
					
					var initialStr = 'initial_' + 'false';
					
					//var tagid = 'remove_' + initialStr + '_' + replaceChars(idStr);
					//remove all the files from the additional list
					var view_first_files_tag = self.view_first_files_tag + 'initial_false' + '_' + ESGF.datacart.replaceChars(idStr);
					$('.' + view_first_files_tag).remove();
					
					//set collapsing files
					this.innerHTML = 'View more files';
				}
				
			});

		}
		
		
		
	});	
})(jQuery);









