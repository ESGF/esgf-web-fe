<%@ include file="/WEB-INF/views/common/include.jsp" %>

<%--
Script information is in scripts/esgf/welcome.js
Style information is in styles/welcomeview/welcome.css
 --%>
 
 

<ti:insertDefinition name="main-layout" >
	
	<ti:putAttribute name="extrascript" value="/WEB-INF/views/search/script_search.jsp" />
	
	<ti:putAttribute name="extrastyle" value="/WEB-INF/views/search/style_search.jsp" />
	
	
	<ti:putAttribute name="main">
	
		<div style="width: 980px; margin:auto;">

            <!-- CIM Viewer Triggers -->
            <div>
                <div class="testpage-header">
                    CIM Viewer Triggers
                </div>
                <div class="testpage-row">
                    <div class="testpage-row-caption">
                        Trigger 1: Combo Box
                    </div>
                    <div class="testpage-row-content">
                        Select a model :&nbsp;
                        <select id="cimModelUISelector" style="width: 160px;">
                            <option value=""></option>
                            <optgroup label="CMIP5">
                                <option value="HadGEM2-ES">HadGEM2-ES</option>
                                <option value="MPI-ESM-LR">MPI-ESM-LR</option>
                                <option value="XXXX">XXXXX</option>
                            </optgroup>
                            <optgroup label="CMIP3">
                                <option value="HadGEM2">HadGEM2</option>
                                <option value="MPEH5">MPEH5</option>
                                <option value="XXXX">XXXXX</option>
                            </optgroup>
                        </select>
                    </div>
                </div>
                <div class="testpage-row">
                    <div class="testpage-row-caption">
                        Trigger 2: Inline text
                    </div>
                    <div class="testpage-row-content">
                        <p>The <span class="cim-document-model"><b>HADGEM2</b></span>, <span class="cim-document-model"><b>MPI-ESM-LR</b></span> and <span class="cim-document-model"><b>XXXXX</b></span> models are amongst the world's most advanced climate models ... blah, blah, blah.</p>
                    </div>
                </div>
                <div class="testpage-row">
                    <div class="testpage-row-caption">
                        Trigger 3: Icon
                    </div>
                    <div class="testpage-row-content">
                        <p>The <span class="cim-document-model" style="padding-right: 18px; background-image: url(ext/css/metafor/images/16x16/std-information.png); background-position:right; background-color: transparent; background-repeat: no-repeat; ">HADGEM2</span>, <span class="cim-document-model" style="padding-right: 18px; background-image: url(ext/css/metafor/images/16x16/std-information.png); background-position:right; background-color: transparent; background-repeat: no-repeat; ">MPI-ESM-LR</span> and <span class="cim-document-model" style="padding-right: 18px; background-image: url(ext/css/metafor/images/16x16/std-information.png); background-position:right; background-color: transparent; background-repeat: no-repeat; ">XXXXX</span> models are amongst the world's most advanced climate models ... blah, blah, blah.</p>
                    </div>
                </div>
            </div>


            <!-- CIM Viewer Options -->
            <div style="margin-top:20px;">
                <div class="testpage-header">
                    CIM Viewer Options
                </div>
                <div class="testpage-row">
                    <div class="testpage-row-caption">
                        Options 1: Render mode
                    </div>
                    <div class="testpage-row-content">
                        <select id="cimModelOptionMode" style="width: 160px;">
                            <option value="tabs" selected="true">Tabbed</option>
                            <option value="inline">Inline</option>
                        </select>
                    </div>
                </div>
                <div class="testpage-row">
                    <div class="testpage-row-caption">
                        Options 2: Max. height
                    </div>
                    <div class="testpage-row-content">
                        <input id="cimModelOptionDialogMaxHeight" value="700"></input>
                    </div>
                </div>
                <div class="testpage-row">
                    <div class="testpage-row-caption">
                        Options 3: Width
                    </div>
                    <div class="testpage-row-content">
                        <input id="cimModelOptionDialogWidth" value="900"></input>
                    </div>
                </div>
                <div class="testpage-row">
                    <div class="testpage-row-caption">
                        Options 4: Sections
                    </div>
                    <div class="testpage-row-content">
                        <input id="cimModelOptionSectionOverview" type="checkbox" checked="true"></input>
                        <label for="cimModelOptionSectionOverview">Overview</label>
                        <select id="cimModelOptionSectionOverviewMode">
                            <option value="inline" selected="true">Inline</option>
                        </select>
                    </div>
                </div>
                <div class="testpage-row">
                    <div class="testpage-row-content">
                        <input id="cimModelOptionSectionParties" type="checkbox" checked="true"></input>
                        <label for="cimModelOptionSectionParties">Parties</label>
                        <select id="cimModelOptionSectionPartiesMode">
                            <option value="inline" selected="true">Inline</option>
                            <option value="table" disabled="disabled">Table</option>
                        </select>
                    </div>
                </div>
                <div class="testpage-row">
                    <div class="testpage-row-content">
                        <input id="cimModelOptionSectionCitations" type="checkbox" checked="true"></input>
                        <label for="cimModelOptionSectionCitations">Citations</label>
                        <select id="cimModelOptionSectionCitationsMode">
                            <option value="inline" selected="true">Inline</option>
                            <option value="table" disabled="disabled">Table</option>
                        </select>
                    </div>
                </div>
                <div class="testpage-row">
                    <div class="testpage-row-content">
                        <input id="cimModelOptionSectionComponents" type="checkbox" checked="true"></input>
                        <label for="cimModelOptionSectionComponents">Components</label>
                        <select id="cimModelOptionSectionComponentsMode">
                            <option value="inline">Top-level inline</option>
                            <option value="accordion" disabled="disabled">Top-level accordion</option>
                            <option value="tabs" disabled="disabled">Top-level tabs</option>
                            <option value="treeview" disabled="disabled">All levels - Treeview</option>
                            <option value="mindmap" disabled="disabled">All levels - Mindmap</option>
                        </select>
                    </div>
                </div>
                <div class="testpage-row">
                    <div class="testpage-row-content">
                        <input id="cimModelOptionSectionCIMInfo" type="checkbox" checked="true"></input>
                        <label for="cimModelOptionSectionCIMInfo">CIM Info</label>
                        <select id="cimModelOptionSectionCIMInfoMode">
                            <option value="inline" selected="true">Inline</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
		
		
		
	</ti:putAttribute>
	
</ti:insertDefinition>


        
