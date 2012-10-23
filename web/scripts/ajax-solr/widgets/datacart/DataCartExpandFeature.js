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
			
			//kill the view more files link
			$('a.view_more_files_short').die('click');
			
			
			
		},
		
		afterRequest: function() {
			

			
		    $('a.showAllFiles_short').live('click',function() {

				var openid = $('span#principal_username').html();
				//alert('openid: ' + $('span#principal_username').html());
		    	
		    	//extract the dataset Id from the span tag
				var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();
				
				
				
				//change verbage of the expand link
				if(this.innerHTML === "Collapse") {
					
					
					var idStr = selectedDocId;
					
					
					//collapse the first 10
					var removeTag = 'remove_' + 'file_rows_' + replaceChars(idStr);
					$('.'+removeTag).remove();
					
					var removeExtraTag = 'addedrow_' + replaceChars(selectedDocId);
					

	                this.innerHTML="Expand";

					
				} else {

					var self = this;

					var parentElement = $(this).parent();
					

					parentElement.find('a.showAllFiles_short').hide();
					parentElement.find('span.showAllFiles_short').show();
					

					
					var idStr = selectedDocId;
					
					var peerStr = getIndividualPeer(idStr);//getPeerStr();
					
					
					var technoteStr = getTechnoteStr();
									
			    	var fqParamStr = getFqParamStr();
			    	
			    	
			    	
			    	var queryStr = {"idStr" : idStr, 
							"peerStr" : peerStr, 
							"technotesStr" : technoteStr, 
							"showAllStr" : ESGF.setting.showAllContents, 
							"fqStr" : fqParamStr, 
							"initialQuery" : "true",
        					"fileCounter" : ESGF.setting.fileCounter};
					
			    	
			    	//NEED TO FIX THIS!!!!
			    	selectedDocId = 'aa.gov';
			    	selectedDocId = Url.encode(selectedDocId);
			    	var url = '/esgf-web-fe/solrfileproxy2/datacart/'+selectedDocId;
					
					
					$.ajax({
						url: url,
						global: false,
						type: "POST",
						data: queryStr,
						dataType: 'json',
						success: function(data) {

							
							var tagid = 'file_rows_' + replaceChars(idStr);
							
							
							if(data.doc.files.file == undefined) {
								
								var appendedFiles = '';
								appendedFiles += '<tr class="remove_' + tagid + '">';
								appendedFiles += '<td style="width: 40px"></td>';
								appendedFiles += '<td style="width: 300px;font-size:13px">'
								appendedFiles += '	<div style="word-wrap: break-word;font-weight:bold;color:gray;font-style:italic;font-weight:bold;color:gray;margin-top:5px" class="datasetId">';
								
								appendedFiles += 'NOTE: There are no files in this dataset that match the search criteria';
								
								appendedFiles += '</div>';
								appendedFiles += '</td>';
								appendedFiles += '</tr>';
									
								$('.'+tagid).after(appendedFiles);
								

								parentElement.find('a.showAllFiles_short').show();
								parentElement.find('span.showAllFiles_short').hide();

								self.innerHTML="Collapse";
								
								
							} else {
								var fileLength = data.doc.files.file.length;

								
								if(fileLength == undefined) {
									var fileArray = new Array();
									fileArray.push(data.doc.files.file);
									data.doc.files['file'] = fileArray;
									fileLength = data.doc.files.file.length;
								}

								
								var fileDownloadTemplate = data.doc;
								
								
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
				    					
				    					var mimesArray = new Array();
				    					mimesArray.push(data.doc.files.file[j].mimes.mime);
				    					data.doc.files.file[j].mimes['mime'] = mimesArray;
										
									}
								}
								

								//alert('technote: ' + data.doc.files.file[3].technote);
								
								var appendedFiles = '';
								
								for(var i=0;i<fileLength;i++) {
									
									
									appendedFiles += '<tr class="remove_' + tagid + '">';
										
									//appendedFiles += '<td>a</td>';
									appendedFiles += '<td style="width: 40px">' +
													 '<input style="margin-left: 10px;display:none"' + 
										   					'class="fileLevel"' + 
										   					'type="checkbox"' + 
										   					'class="fileId"' + 
										   					'id="${fileId}"' + 
										   					'checked="true"' + 
										   					'/>' +
										   					'</td>';
									
									
									appendedFiles += '<td style="width: 425px;padding-left:10px;font-size:11px;">' +
													 '<div style="word-wrap: break-word;">' + 
													 '<span style="font-weight:bold">' + data.doc.files.file[i].fileId + '</span>' +
													 '	<br />' + 
													 '<span style="font-style:italic">' + 'tracking_id: ' + data.doc.files.file[i].tracking_id + '</span>' +
													 '  <br />' +
													 '<span style="font-style:italic">checksum: ' + data.doc.files.file[i].checksum + ' (' + data.doc.files.file[i].checksum_type + ')' + '</span>' +
													 '</div>' +
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
												//appendedFiles += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right"> <a style="cursor:pointer" class="go_individual_gridftp_short">' + service + '</a> </span>';
												appendedFiles += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right">' +
												                 '<span class="file_id" style="display:none">' + file_id + '</span>' + 
												                 '<span class="globus_url" style="display:none">' + url + '</span>' +
												                 '<a style="cursor:pointer" class="go_individual_gridftp_short">' + service + '</a> </span>';
												
											}
										} else {
											if(openid != 'anonymousUser') {
												appendedFiles += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right"> <a href="'  + url  + '?openid=' + openid +  '" ' + 'target="_blank">' + service + '</a> </span>';
											} else {
												appendedFiles += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right"> <a href="'  + url  + '" ' + 'target="_blank">' + service + '</a> </span>';
											}
										}
										
									}
									
									if(data.doc.files.file[i].technote != 'NA') {
										
										
										var technoteStr = data.doc.files.file[i].technote;
										//alert('i: ' + i + ' ' + technoteStr);
										//var technoteArr = technoteStr.split("\\|");
										//alert('len: ' + technoteArr.length);
										appendedFiles += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right"> <a style="cursor:pointer" href="' + technoteStr + '" target="_blank" >' + 'TECHNOTE' + '</a> </span>';
									}
									
									appendedFiles += '</td>';
									
									appendedFiles += '</tr>';
									
								}

								appendedFiles += '<tr class="remove_' + tagid + '">';
								appendedFiles += '<td></td>';
								appendedFiles += '</tr>';
								
								
								if(fileLength >= ESGF.setting.fileCounter) {
									appendedFiles += '<tr class="view_files_' + '' + ' remove_' + tagid + '" style="">';
									appendedFiles += '<td></td>';
									appendedFiles += '<td style="display:none"><span class="datasetId">' + data.doc.datasetId + '</td>';
									appendedFiles += '<td><a class="view_more_files_short" style="cursor:pointer;font-size:11px">' + 'View more files' + '</a></td>';
									appendedFiles += '</tr>';
									
									
									appendedFiles += '<tr class="file_append_' + replaceChars(data.doc.datasetId) + ' remove_' + tagid + '">';
									//appendedFiles += '<tr class="file_append_' + replaceChars(data.doc.datasetId) + ' remove_' + tagid + '">';
									appendedFiles += '<td></td>';
									appendedFiles += '</tr>';
								}
								
								
								
								$('.'+tagid).after(appendedFiles);

								parentElement.find('a.showAllFiles_short').show();
								parentElement.find('span.showAllFiles_short').hide();


								
								
								

								self.innerHTML="Collapse";
							}
							
						},
						error: function() {
							alert('error in getting new rows');
						}
					});
					
					
					
					
					
					
					
					
					
						
				}
				
				
				
		    	
				
			});
			
			
		    
			
			$('a.view_more_files_short').live('click',function() {

				var openid = $('span#principal_username').html();
				
				
				var selectedDocId = ($(this).parent().parent().find('span.datasetId').html());
				
				
				var self=this;
				
				if(this.innerHTML == "View more files") {
					
					
					var idStr = selectedDocId;
					
					var peerStr = getIndividualPeer(idStr);//getPeerStr();
					
					//alert('peerStr: ' + peerStr);
					
					var technoteStr = getTechnoteStr();
					
			    	var fqParamStr = getFqParamStr();
			    	
			    	
			    	var queryStr = {"idStr" : idStr, 
							"peerStr" : peerStr, 
							"technotesStr" : technoteStr, 
							"showAll" : ESGF.setting.showAllContents, 
							"fqStr" : fqParamStr, 
							"initialQuery" : "false",
        					"fileCounter" : ESGF.setting.fileCounter};
					
			    	var appendedRows = $(this).parent().parent().parent().find('tr.file_append_' + replaceChars(selectedDocId));

			    	
			    	appendedRows.after('<tr id="spinner"><td></td><td><img src="images/ajax-loader.gif" /></td></tr>');
			    	
					$.ajax({
						url: '/esgf-web-fe/solrfileproxy2/datacart',
						global: false,
						type: "GET",
						data: queryStr,
						dataType: 'json',
						success: function(data) {
				
							$('tr#spinner').remove();
							
							var newRows = '';
							
							data.docs = rewriteDocsObject(data.docs);
							
							for(var key in data.docs) {
								var value = data.docs[key];
								
								for(var key2 in value) {

									var value2 = value[key2]; 
									for(var key3 in value2) {
										//alert('key3: ' + key3);
									}
									//alert('key: ' + key + ' key2: ' + key2 + ' value2: ' + value2);
								}
							}
							
							var tagid = 'file_rows_' + replaceChars(idStr);
							
							for(var i=0;i<data.docs.doc[0].files.file.length;i++){
								var file = data.docs.doc[0].files.file[i];
								var newRow = '<tr class="file_rows_' + replaceChars(selectedDocId) + ' addedrow_' + replaceChars(selectedDocId) + ' remove_' + tagid + '">';
								
								//add the checkbox here
								newRow += '<td style="width: 40px;">';
								newRow += '<input style="margin-left: 10px;display:none" '
								newRow += 'class="fileLevel fileId" ';
								newRow += 'type="checkbox" ';
								newRow += 'checked="true" ';
								newRow += 'value="' + file.fileId + '" />';
								
								
								//add the fileId, checksum, tracking id here
								newRow += '<td style="width: 325px;padding-left:10px;font-size:11px;">';
								newRow += '<div style="word-wrap: break-word;">';
								newRow += '<span style="font-weight:bold"> ' + file.fileId + ' (' + sizeConversion(file.size)  + ') </span> <br />';
								newRow += '<span style="font-weight:italic"> Tracking Id: ' + file.tracking_id + '</span> <br />';
								newRow += '<span style="font-weight:italic"> Checksum: ' + file.checksum + ' (' + file.checksum_type + ') </span>';
								newRow += '</div>';
								newRow += '</td>';
								

								//services
								
								newRow += '<td style="float:right;font-size:11px;">';
								
								for(var j=0;j<file.services.service.length;j++) {
									//var file = data.docs.doc[0].files.file[i];
									
									var service = file.services.service[j];
									var url = file.urls.url[j];
									var file_id = file.fileId;
									
									if(file.services.service[j] == 'HTTPServer') {
										
										newRow += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right">';
										newRow += '<a href="' + file.urls.url[j];
										if(openid != 'anonymousUser') {
											newRow += '?openid=' + openid;
										}
										newRow += '">HTTP</a> ';
										newRow += '</span>';
												  
										
									} else if(file.services.service[j] == 'GridFTP') {
										
										if(ESGF.setting.globusonline) {
											newRow += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right">' +
													  '<span class="file_id" style="display:none">' + file_id + '</span>' + 
							                          '<span class="globus_url" style="display:none">' + url + '</span>' +
							                          '<a style="cursor:pointer" class="go_individual_gridftp_short">' + service + '</a> </span>';
										}
									}
									
								}
								
								newRow += '</td>';
								
								if(file.technote != 'NA') {
									newRow += '<td style="float:right;font-size:11px;">';
									newRow += '<div style="word-wrap: break-word;vertical-align:middle">';
									newRow += '<a href="${technote}" target="_blank">TECHNOTE </a>';
									newRow += '</div>';
									newRow += '</td>';
								}
								
								
								newRow += '</tr>';
								
								
								newRows += newRow;
							
							}

							appendedRows.after(newRows);
							
							
							self.innerHTML = 'Collapse files';
							
						},
						error: function() {
							alert('error');
						}
					});
					
					
				} else {

					$('tr.addedrow_'+replaceChars(selectedDocId)).remove();
					this.innerHTML = 'View more files';
				}
				
				
				
			});
			
			
			
		}
		
		
		
	});	
})(jQuery);
