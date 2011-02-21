<%@ include file="/WEB-INF/views/common/include.jsp" %>

<!-- overlays -->
<div class="apple_overlay" id="temporal_overlay"><div class="contentWrap"></div></div>

<div class="apple_overlay" id="geospatial_overlay"><div class="contentWrap"></div></div>

<div class="apple_overlay" id="metadata_overlay"><div class="contentWrap"></div></div>

<div class="apple_overlay" id="annotator_overlay"><div class="contentWrap"></div></div>

<!--
<div class="apple_overlay" id="facet_overlay"><div class="contentWrap"></div></div>
-->

<!-- facet overlay -->
<!-- need to replace the following with a separate html -->

<div class="apple_overlay" id="facet_overlay">
    <div class="overlay_header" style="display:none">
        <div class="overlay_header_title">
            Category Browser
        </div>
        <!--
        <div class="overlay_header_buttons">
            <div id="facetSort">
                <input type="radio" id="sortbycount" name="sorter" checked="checked" value="sortbycount" /><label for="sortbycount">Sort By Count</label>
                <input type="radio" id="sortbyabc" name="sorter" value="sortbyabc" /><label for="sortbyabc">Sort By ABC</label>
            </div>
        </div>
        -->
    </div>
    <div class="overlay_border" style="display:none"></div>
    <div class="overlay_content" style="display:none">

           <div class="scrollable facet_verticalscroll">
            <div class="facet_items">
                <div id="project"></div>
                <div id="model"></div>
                <div id="experiment"></div>

                <div id="frequency"></div>
                <div id="realm"></div>
                <div id="instrument"></div>
                <div id="variable"></div>

                <div id="cf_variable"></div>
                <div id="gcmd_variable"></div>

            </div>
        </div>
    </div>
    <div class="overlay_border" style="display:none"></div>
    <div class="overlay_footer" style="display:none">
        <div class="overlay_header_buttons">
            <div id="facetSort">
                <input type="radio" id="sortbycount" name="sorter" checked="checked" value="sortbycount" /><label for="sortbycount">Sort By Count</label>
                <input type="radio" id="sortbyabc" name="sorter" value="sortbyabc" /><label for="sortbyabc">Sort By ABC</label>
            </div>
        </div>
    </div>
</div>

<div id="temp-browse"></div>
<div id="metadata-browse"></div>