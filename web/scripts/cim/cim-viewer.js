// ECMAScript 5 Strict Mode
"use strict";

// --------------------------------------------------------
// $jq :: JQuery nonconflict reference.
// See :: http://www.tvidesign.co.uk/blog/improve-your-jquery-25-excellent-tips.aspx#tip19
// --------------------------------------------------------
window.$ = window.$jq = jQuery.noConflict();

// --------------------------------------------------------
// cim :: CIM nonconflict reference.
// See :: http://www.tvidesign.co.uk/blog/improve-your-jquery-25-excellent-tips.aspx#tip19
// --------------------------------------------------------
if (window.cim === undefined) {
    window.cim = {
        // Global error handler.
        onError : function (component, componentVersion, errorType, errors) {
            if (errors.length === 0) {
                return true;
            } else {
                var error = Error();
                error.name = 'CIM {0} v{1} - {2} error'
                    .replace('{0}', component)
                    .replace('{1}', componentVersion)
                    .replace('{2}', errorType.toLowerCase());
                error.message = _.reduce(errors, function (memo, error) { return memo + error + '\n'; }, '');
                throw error;
            }
        },

        // Latest version of cim standard.
        latest_version : '1.5'
    };
}
var cim = window.cim;

// --------------------------------------------------------
// cim.viewer :: Renders views of cim instances.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// --------------------------------------------------------
window.cim.viewer = (function() {
    // Viewer version.
    var version = '0.4';

    // Viewer renderers.
    var renderers;

    // Viewer error handler.
    var onError;

    // Viewer render from drs function.
    var renderFromDRS;

    // Viewer render from ID function.
    var renderFromID;

    // Viewer render from name function.
    var renderFromName;

    // Viewer error handler.
    // @type            Error type.
    // @errors          Error collection.
    onError = function (type, errors) {
        return cim.onError('Viewer', version, type, errors);
    };

    // Renders a view of a CIM instance based upon its id/version and view options.
    // @cim_version     Target cim version.
    // @id              ID of CIM instance being rendered.
    // @version         Version of CIM instance being rendered.
    // @opts            Rendering options.
    renderFromID = function(cim_version, id, version, opts) {
        alert('TODO - render by ID');
    };

    // Event handler for document ready event.
    $jq(document).ready(function() {
        // Inject cim viewer dialog div.
        cim.viewer.HTML.appendHTMLDiv('body', 'cimViewerDialog', 'cim-viewer-dialog');

        // Initialise set of supported CIM renderers.
        renderers = {
            '1.5' : {
                'MODEL' : {
                    defaultOpts : cim.viewer.options.forModel.defaults,
                    renderFn : cim.viewer.renderer.forModel.render,
                    serviceFns : {
                        'byID' : cim.servicesProxy.query.getModelByName,
                        'byName' : cim.servicesProxy.query.getModelByName,
                        'byDRS' : cim.servicesProxy.query.getModelByName
                    }
                },
                'DATAOBJECT' : {
                    defaultOpts : cim.viewer.options.forDataObject.defaults,
                    renderFn : cim.viewer.renderer.forDataObject.render,
                    serviceFns : {
                        'byID' : cim.servicesProxy.query.getDataObjectByAcronym,
                        'byName' : cim.servicesProxy.query.getDataObjectByAcronym,
                        'byDRS' : cim.servicesProxy.query.getDataObjectByAcronym
                    }
                },
                'EXPERIMENT' : {
                    defaultOpts : cim.viewer.options.forExperiment.defaults,
                    renderFn : cim.viewer.renderer.forExperiment.render,
                    serviceFns : {
                        'byID' : cim.servicesProxy.query.getExperimentByName,
                        'byName' : cim.servicesProxy.query.getExperimentByName,
                        'byDRS' : cim.servicesProxy.query.getExperimentByName
                    }
                },
                'SIMULATION' : {
                    defaultOpts : cim.viewer.options.forSimulation.defaults,
                    renderFn : cim.viewer.renderer.forSimulation.render,
                    serviceFns : {
                        'byID' : cim.servicesProxy.query.getSimulationByName,
                        'byName' : cim.servicesProxy.query.getSimulationByName,
                        'byDRS' : cim.servicesProxy.query.getSimulationByName
                    }
                },
                'PLATFORM' : {
                    defaultOpts : cim.viewer.options.forPlatform.defaults,
                    renderFn : cim.viewer.renderer.forPlatform.render,
                    serviceFns : {
                        'byID' : cim.servicesProxy.query.getPlatformByName,
                        'byName' : cim.servicesProxy.query.getPlatformByName,
                        'byDRS' : cim.servicesProxy.query.getPlatformByName
                    }
                }
            }
        };
    });

    // Renders a view of a CIM instance dervied from DRS mappings.
    // @drsMap          Map of drs elements.
    // @cim_version     Target cim version (optional).
    // @opts            Rendering options (optional).
    renderFromDRS = function(drsMap, cim_version, opts) {
        var renderer;           // CIM Renderer.
        var getRenderer;        // Function to derive target renderer from drs map.
        var validateParams;     // Function to validate input parameters.
        var serviceCallback;    // Function to invoke when cim search api returns a result.

        // Override optional parameters.
        if (cim_version === undefined) {
            cim_version = cim.latest_version;
        }

        // Validates input parameters.
        validateParams = function() {
            var errors = [];        // Set of validation errors.
            var msg;                // Error message to be output.

            // Required parameters.
            if (drsMap === undefined) {
                errors.push('Parameter drsMap is required');
            }

            // Type checks.
            if (cim_version && _.isString(cim_version) === false) {
                errors.push('Parameter cim_version must be a string');
            }
            if (drsMap && $jq.isPlainObject(drsMap) === false) {
                errors.push('Parameter drsMap must be a dictionary of drs name/value pairs');
            }

            // CIM version.
            if (cim_version &&
                _.has(renderers, cim_version) === false){
                msg = 'CIM version {0} is unsupported';
                errors.push(msg.replace('{0}', cim_version));
            }

            // DRS map.
            if (drsMap && $jq.isPlainObject(drsMap)) {
                // Key checks.
                if (_.has(drsMap, 'project') === false) {
                    errors.push('DRS map \'project\' is required');
                }
                if (_.has(drsMap, 'model') == false &&
                    _.has(drsMap, 'experiment') == false) {
                    errors.push('DRS map either \'model\' or \'experiment\' is required');
                }
                if (_.has(drsMap, 'institute') == false &&
                    _.has(drsMap, 'model') == true &&
                     _.has(drsMap, 'experiment') == true) {
                    errors.push('If DRS map contains both \'model\' and \'experiment\' then it must also contain \'institute\'');
                }

                // Value checks.
                _.each(['project', 'institute', 'model', 'experiment'], function(key) {
                    if (_.has(drsMap, key)) {
                        if (_.isString(drsMap[key]) === false ||
                            $jq.trim(drsMap[key]).length === 0) {
                            errors.push('DRS \'{0}\' is a required string'.replace('{0}', key));
                        }
                    }
                });
            }

            // Either return true or throw errors.
            return cim.viewer.onError('parameter validation', errors);
        }

        // If params are valid then call web service and pass result to renderer.
        getRenderer = function() {
            if (_.has(drsMap, 'model') == true &&
                _.has(drsMap, 'experiment') == false) {
                return renderers[cim_version]['MODEL'];
            }

            if (_.has(drsMap, 'model') == false &&
                _.has(drsMap, 'experiment') == true) {
                return renderers[cim_version]['MODEL'];
            }

            if (_.has(drsMap, 'institute') == true &&
                _.has(drsMap, 'model') == true &&
                _.has(drsMap, 'experiment') == true) {
                return renderers[cim_version]['MODEL'];
            }

            // Throw error.
            cim.viewer.onError('parameter validation', ['DRS map does not contain a valid permutation of values.']);
        }

        // If params are valid then call web service and pass result to renderer.
        if (validateParams()) {
            renderer = getRenderer();
            if (opts === undefined) {
                opts = renderer.defaultOpts;
            }
            if (renderer) {
                serviceCallback = function(cim) {
                    renderer.renderFn(cim, opts);
                };
                renderer.serviceFns['byDRS'](drsMap['project'], drsMap['model'], serviceCallback);
            }
        }
    };

    // Renders a view of a CIM instance based upon its version, type, project, name, and view options.
    // @cim_version     CIM version being rendered.
    // @cim_type        CIM instance type being rendered.
    // @project         Project with which CIM metadata is associated.
    // @name            Name  of CIM instance being rendered.
    // @opts            Rendering options.
    renderFromName = function(cim_version, cim_type, project, name, opts) {
        var renderer;           // CIM Renderer.
        var getRenderer;        // Function to derive target renderer from drs map.
        var validateParams;     // Function to validate input parameters.
        var serviceCallback;    // Function to invoke when cim search api returns a result.

        // Validates input parameters.
        validateParams = function() {
            var errors = [];        // Set of validation errors.
            var msg;                // Error message to be output.

            // Required parameters.
            if (cim_version === undefined) {
                errors.push('Parameter cim_version is required');
            }
            if (cim_type === undefined) {
                errors.push('Parameter cim_type is required');
            }
            if (project === undefined) {
                errors.push('Parameter project is required');
            }
            if (name === undefined) {
                errors.push('Parameter name is required');
            }

            // Type checks.
            if (cim_version && _.isString(cim_version) === false) {
                errors.push('Parameter cim_version must be a string');
            }
            if (cim_type && _.isString(cim_type) === false) {
                errors.push('Parameter cim_type must be a string');
            }
            if (project && _.isString(project) === false) {
                errors.push('Parameter project must be a string');
            }
            if (name && _.isString(name) === false) {
                errors.push('Parameter name must be a string');
            }

            // CIM version.
            if (cim_version &&
                _.has(renderers, cim_version) === false){
                msg = 'CIM version {0} is unsupported';
                errors.push(msg.replace('{0}', cim_version));
            }

            // CIM type.
            if (cim_type &&
                _.has(renderers, cim_version) &&
                _.has(renderers[cim_version], cim_type.toUpperCase()) === false){
                msg = 'CIM version {0} type {1} is unsupported';
                errors.push(msg.replace('{0}', cim_version).replace('{1}', cim_type));
            }

            // Returns true if no errors.
            return cim.viewer.onError('parameter validation', errors);
        }

        // Get renderer.
        getRenderer = function() {
            return renderers[cim_version][cim_type.toUpperCase()];
        }

        // If params are valid then call web service and pass result to renderer.
        if (validateParams()) {
            renderer = getRenderer();
            if (renderer) {
                serviceCallback = function(cim) {
                    renderer.renderFn(cim, opts);
                };
                renderer.serviceFns['byName'](project, name, serviceCallback);
            }
        }
    };

    // Return object pointer wrapped in functional closure.
    return {
        // Viewer global error handler.
        onError : onError,

        // Renders a view of a CIM instance dervied from DRS mappings and view options.
        renderFromDRS : renderFromDRS,

        // Renders a view of a CIM instance dervied from type, name and view options.
        renderFromName : renderFromName,

        // Renders a view of a CIM instance dervied from id/version and view options.
        renderFromID : renderFromID,

        // Default view options.
        viewOptions : {
            // Default view options for CIM model rendering.
            forModel : undefined,

            // Default view options for CIM data object rendering.
            forDataObject : undefined,

            // Default view options for CIM experiment rendering.
            forExperiment : undefined,

            // Default view options for CIM simulation rendering.
            forSimulation : undefined
        }
    };
}());


// --------------------------------------------------------
// cim.viewer.dialog :: Helper encapulating displaying views as a modal dialog.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// --------------------------------------------------------
window.cim.viewer.dialog = (function() {
    // Returns dialog title based upon CIM instance being rendered.
    // @ctx         Rendering context.
    var getTitle = function(ctx) {
        var title = 'CIM ';
        title += ctx.cim.cimInfo.schema;
        title += ' - ';
        title += ctx.cim.cimInfo.type;
        title += ' : ';
        title += ctx.cim.shortName;
        return title;
    };

    // Opens dialog.
    // @ctx         Rendering context.
    var open = function(ctx) {
        // Dialog options.
        var options = ctx.options.dialog;

        // Destroy previous.
        $jq('#cimViewerDialog').dialog("destroy");

        // Create new.
        $jq('#cimViewerDialog').dialog({
            bgiframe: false,
            autoOpen: false,
            maxHeight : options.maxHeight,
            width: options.width,
            position: ['center', 80],
            modal: true,
            resizable: false,
            open: function(event, ui) {
                $(this).css({'max-height': options.maxHeight, 'overflow-y': 'auto'});
            }
        });

        // Set title and open.
        $jq('#cimViewerDialog').dialog('option', 'title', getTitle(ctx));
        $jq('#cimViewerDialog').dialog('open');
    };

    // Return object pointer wrapped in functional closure.
    return {
        open : open
    };
}());


// --------------------------------------------------------
// cim.viewer.HTML :: Helper encapulating html injection functions.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// --------------------------------------------------------
window.cim.viewer.HTML = (function() {

    // Appends an html tabs collection to passed container dom ID.
    // @parentID     ID of parent html element.
    // @tabsID       ID of tabs collection div.
    var appendHTMLTabs = function(parentID, tabsID) {
        var tabs = '<div id="{0}"><ul></ul></div>'.replace('{0}', tabsID);
        $jq("#" + parentID).append(tabs);
        return $jq('#{0} ul'.replace('{0}', tabsID));
    };

    // Appends an html tab to passed collection.
    // @tabs        Tab collection.
    // @tabInfo     Info regarding new tab to be appended to dom.
    var appendHTMLTab = function(tabs, tabInfo) {
        var tab = '<li><a href="#{0}">{1}</a></li>'
            .replace('{0}', tabInfo.domID)
            .replace('{1}', tabInfo.title);
        tabs.append(tab);
    };

    // Appends an html div to passed container.
    // @parentID     ID of parent html element.
    // @title        Title of accordion to appended to dom.
    var appendHTMLAccordion = function(parentID, title) {
        var accordion = '<h3><a href="#">{0}</a></h3>'.replace('{0}', title);
        var $div;

        // Append div.
        $jq("#" + parentID).append(accordion);

        return $div;
    };

    // Prepends an html div to passed container.
    // @parentID     ID of parent html element.
    // @title        Title of accordion to appended to dom.
    var prependHTMLAccordion = function(parentID, title) {
        var accordion = '<h3><a href="#">{0}</a></h3>'.replace('{0}', title);
        $jq("#" + parentID).before(accordion);
    };

    // Appends an html div to passed container.
    // @parentID     ID of parent html element.
    // @divID        ID of div to be appended to dom.
    // @divClass     Class of div to be appended to dom.
    // @divText      Text to be inserted into div.
    var appendHTMLDiv = function(parentID, divID, divClass, divText) {
        var div = '<div id="{0}"></div>'.replace('{0}', divID);
        var $div;

        // Append div.
        if (parentID === 'body') {
            $jq('body').append(div);
        } else {
            $jq("#" + parentID).append(div);
        }

        // Set div class.
        $div = $jq('#{0}'.replace('{0}', divID));
        if (divClass !== undefined) {
            $div.addClass(divClass);
        }

        // Set div text.
        if (divText !== undefined) {
            $div.text(divText);
        }

        return $div;
    };

    // Prepends an html div to passed container.
    // @parentID     ID of parent html element.
    // @divID        ID of div to be appended to dom.
    // @divClass     Class of div to be appended to dom.
    // @divText      Text to be inserted into div.
    var prependHTMLDiv = function(parentID, divID, divClass, divText) {
        var div = '<div id="{0}"></div>'.replace('{0}', divID);
        var $div;

        // Prepend div.
        if (parentID === 'body') {
            $jq('body').prepend(div);
        } else {
            $jq("#" + parentID).prepend(div);
        }

        // Set div class.
        $div = $jq('#{0}'.replace('{0}', divID));
        if (divClass !== undefined) {
            $div.addClass(divClass);
        }

        // Set div text.
        if (divText !== undefined) {
            $div.text(divText);
        }

        return $div;
    };

    // Returns an html snippet based upon a template field.
    // @field       An array of information: [ title, name ].
    var getHTMLForField = function(field) {
        return "\n\
            <div class='cim-row'>\n\
                <div class='cim-cell-label'>\n\
                    <span title='{title}'>{title}</span>\n\
                </div>\n\
                <div class='cim-cell-input'>\n\
                    <span title='<%= {name} %>'><%= {name} %></span>\n\
                </div>\n\
            </div>"
            .replace(/{title}/g, field[0])
            .replace(/{name}/g, field[1]);
    };

    // Returns an html snippet based upon a set of template fields.
    // @fields  Set of template fields.
    var getHTMLForFields = function (fields) {
        return _(fields).reduce(function(template, field){
            return template + getHTMLForField(field);
        }, '');
    };

    // Returns an html snippet based upon a template field group.
    // @fields  Set of template fields.
    var getHTMLForFieldGroup = function(fields) {
        return "\n\
        <div class='cim-row-group{suffix} ui-corner-all'>\n\
            {rows}\n\
        </div>"
        .replace(/{rows}/g, getHTMLForFields(fields))
        .replace('{suffix}', '');
    };

    // Returns an html snippet based upon injecting data into a template.
    // @data                JSON object form which data will be injected into template.
    // @view                View information (includes set of associated template fields).
    // @templateFactory     Factory method for creating html template to be injected with data.
    var getHTMLFromTemplate = function(data, view, templateFactory){
        var html = "";      // HTML to be rendered.
        var template;       // HTML template to be populated.

        // Create template (using underscore).
        template = templateFactory(view.fields);
        template = _.template(template);

        // Generate html (with help of underscore template engine):
        if (_.isArray(data)) {
            _(data).each(function (item) {
                html = html + template(item);
            });
        } else {
            html = html + template(data);
        }

        return html;
    };

    // Injects html into view by generating html from a json parsed template.
    // @cim                 CIM instance as a JSON object form which data will be injected into template.
    // @view                View information.
    // @templateFactory     Method to create template to be injected with data.
    var injectHTML = function(cim, view, templateFactory) {
        var data = view.getData(cim);
        var html = getHTMLFromTemplate(data, view, templateFactory);
        $('#' + view.domID).html(html);
    };

    // Return object pointer wrapped in functional closure.
    return {
        // Appends an html tabs collection to passed container dom ID.
        appendHTMLTabs : appendHTMLTabs,

        // Appends an html tab to passed collection.
        appendHTMLTab : appendHTMLTab,

        // Appends an html div to passed container.
        appendHTMLDiv : appendHTMLDiv,

        // Prepends an html div to passed container.
        prependHTMLDiv : prependHTMLDiv,

        // Appends an html accordion header to passed container.
        appendHTMLAccordion : appendHTMLAccordion,

        // Prepends an html accordion header to passed container.
        prependHTMLAccordion : prependHTMLAccordion,

        // Returns an html snippet based upon a template field.
        getHTMLForField : getHTMLForField,

        // Returns an html snippet based upon a set of template fields.
        getHTMLForFields : getHTMLForFields,

        // Returns an html snippet based upon a template field group.
        getHTMLForFieldGroup : getHTMLForFieldGroup,

        // Returns an html snippet based upon injecting data into a template.
        getHTMLFromTemplate : getHTMLFromTemplate,

        // Injects html into view by generating html from a json parsed template.
        injectHTML : injectHTML
    };
}());


// --------------------------------------------------------
// cim.viewer.metadata :: Helper encapulating view rendering metadata.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// --------------------------------------------------------
window.cim.viewer.metadata = (function() {
    // Model view metadata.
    var forModel = {
        info : {
            domID : 'cimViewerForModel',
            dialogID : 'cimDialogForModel'
        },
        getVisibleSections : function(options) {
            var all = [
                this.sections.overview,
                this.sections.parties,
                this.sections.citations,
                this.sections.components,
                this.sections.cimInfo
            ];
            return all;
        },
        sections : {
            overview : {
                title : 'Overview',
                titleLong : 'Overview',
                domID : 'cimModelOverview',
                fields : [
                    ['Short Name', 'shortName'],
                    ['Long Name', 'longName'],
                    ['Description', 'description']
                ],
                getData : function(model) {
                    return model;
                }
            },
            parties : {
                title : 'Parties',
                titleLong : 'Responsible Parties',
                domID : 'cimModelParties',
                fields : [
                    ['Name', 'name'],
                    ['Role', 'role'],
                    ['Address', 'address'],
                    ['Email', 'email']
                ],
                getData : function(model) {
                    return model.parties;
                }
            },
            citations : {
                title : 'Citations',
                titleLong : 'Citations',
                domID : 'cimModelCitations',
                fields : [
                    ['Title', 'title'],
                    ['Type', 'type'],
                    ['Reference', 'reference'],
                    ['Location', 'location']
                ],
                getData : function(model) {
                    return model.citations;
                }
            },
            components : {
                title : 'Components',
                titleLong : 'Components (top-level)',
                domID : 'cimModelComponents',
                fields : [
                    ['Short Name', 'shortName'],
                    ['Type', 'type'],
                    ['Description', 'description']
                ],
                getData : function(model) {
                    return model.components;
                }
            },
            cimInfo : {
                title : 'CIM Info',
                titleLong : 'CIM Information',
                domID : 'cimModelCIM',
                fields : [
                    ['Schema', 'schema'],
                    ['ID', 'id'],
                    ['Version', 'version'],
                    ['Creation Date', 'createDate'],
                    ['Source', 'source']
                ],
                getData : function(model) {
                    return model.cimInfo;
                }
            }
        }
    };

    // Data object view metadata.
    var forDataObject = {
        info : {
            domID : 'cimViewerForDataObject',
            dialogID : 'cimDialogForDataObject'
        }
    };

    // Experiment view metadata.
    var forExperiment = {
        info : {
            domID : 'cimViewerForExperiment',
            dialogID : 'cimDialogForExperiment'
        }
    };


    // Simulation view metadata.
    var forSimulation = {
        info : {
            domID : 'cimViewerForSimulation',
            dialogID : 'cimDialogForSimulation'
        }
    };

    // Return object pointer wrapped in functional closure.
    return {
        // Model view metadata.
        forModel : forModel,

        // Data object view metadata.
        forDataObject : forDataObject,

        // Experiment view metadata.
        forExperiment : forExperiment,

        // Simulation view metadata.
        forSimulation : forSimulation
    };
}());


// --------------------------------------------------------
// cim.viewer.options :: Helper encapulating view rendering options.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// --------------------------------------------------------
window.cim.viewer.options = (function() {
    // View options for CIM model rendering.
    var forModel = {
        // Set of default options.
        defaults : {
            mode : 'tabs',          // one of: tabs | inline
            dialog : {
                display : true,
                maxHeight : 800,
                width : 900
            },
            sections : {
                overview : {
                    display : true,
                    mode : 'inline'     // one of: inline
                },
                parties : {
                    display : true,
                    mode : 'inline'     // one of: inline | table
                },
                citations : {
                    display : true,
                    mode : 'inline'     // one of: inline | table
                },
                components : {
                    display : true,
                    mode : 'topInline'     // one of: inline | accordion | tabs | treeview | mindmap
                },
                cimInfo : {
                    display : true,
                    mode : 'inline'     // one of: inline
                }
            }
        },

        // Parses options to guarantee their validity.
        parse : function(options) {
            var defaults = forModel.defaults;
            if (options === undefined) {
                options = defaults;
            } else {
                if (options.mode === undefined) {
                    options.mode = defaults.mode;
                }
                if (options.dialog === undefined) {
                    options.dialog = defaults.dialog;
                } else {
                    if (options.dialog.display === undefined) {
                        options.dialog.display = defaults.dialog.display;
                    }
                    if (options.dialog.maxHeight === undefined) {
                        options.dialog.maxHeight = defaults.dialog.maxHeight;
                    }
                    if (options.dialog.width === undefined) {
                        options.dialog.width = defaults.dialog.width;
                    }
                }
                if (options.sections === undefined) {
                    options.sections = defaults.sections;
                } else {
                    if (options.sections.overview === undefined) {
                        options.sections.overview = defaults.sections.overview;
                    } else {
                        if (options.sections.overview.display === undefined) {
                            options.sections.overview.display = defaults.sections.overview.display;
                        }
                        if (options.sections.overview.mode === undefined) {
                            options.sections.overview.mode = defaults.sections.overview.mode;
                        }
                    }
                    if (options.sections.parties === undefined) {
                        options.sections.parties = defaults.sections.parties;
                    } else {
                        if (options.sections.parties.display === undefined) {
                            options.sections.parties.display = defaults.sections.parties.display;
                        }
                        if (options.sections.parties.mode === undefined) {
                            options.sections.parties.mode = defaults.sections.parties.mode;
                        }
                    }
                    if (options.sections.citations === undefined) {
                        options.sections.citations = defaults.sections.citations;
                    } else {
                        if (options.sections.citations.display === undefined) {
                            options.sections.citations.display = defaults.sections.citations.display;
                        }
                        if (options.sections.citations.mode === undefined) {
                            options.sections.citations.mode = defaults.sections.citations.mode;
                        }
                    }
                    if (options.sections.components === undefined) {
                        options.sections.components = defaults.sections.components;
                    } else {
                        if (options.sections.components.display === undefined) {
                            options.sections.components.display = defaults.sections.components.display;
                        }
                        if (options.sections.components.mode === undefined) {
                            options.sections.components.mode = defaults.sections.components.mode;
                        }
                    }
                    if (options.sections.cimInfo === undefined) {
                        options.sections.cimInfo = defaults.sections.cimInfo;
                    } else {
                        if (options.sections.cimInfo.display === undefined) {
                            options.sections.cimInfo.display = defaults.sections.cimInfo.display;
                        }
                        if (options.sections.cimInfo.mode === undefined) {
                            options.sections.cimInfo.mode = defaults.sections.cimInfo.mode;
                        }
                    }
                }
            }
        }
    };

    // View options for CIM data object rendering.
    var forDataObject = {
        defaults : {
            // TODO
        },

        // Parses options to guarantee their validity.
        parse : function(options) {
            var defaults = forDataObject.defaults;
            if (options === undefined) {
                options = defaults;
            } else {
                // TODO
            }
        }
    };

    // View options for CIM experiment rendering.
    var forExperiment = {
        defaults : {
            // TODO
        },

        // Parses options to guarantee their validity.
        parse : function(options) {
            var defaults = forExperiment.defaults;
            if (options === undefined) {
                options = defaults;
            } else {
                // TODO
            }
        }
    };

    // View options for CIM simulation rendering.
    var forSimulation = {
        defaults : {
            // TODO
        },

        // Parses options to guarantee their validity.
        parse : function(options) {
            var defaults = forSimulation.defaults;
            if (options === undefined) {
                options = defaults;
            } else {
                // TODO
            }
        }
    };

    // View options for CIM platform rendering.
    var forPlatform = {
        defaults : {
            // TODO
        },

        // Parses options to guarantee their validity.
        parse : function(options) {
            var defaults = forPlatform.defaults;
            if (options === undefined) {
                options = defaults;
            } else {
                // TODO
            }
        }
    };

    // Return object pointer wrapped in functional closure.
    return {
        // Default view options for CIM model rendering.
        forModel : forModel,

        // Default view options for CIM data object rendering.
        forDataObject : forDataObject,

        // Default view options for CIM experiment rendering.
        forExperiment : forExperiment,

        // Default view options for CIM simulation rendering.
        forSimulation : forSimulation,

        // Default view options for CIM Platform rendering.
        forPlatform : forPlatform
    };
}());


// --------------------------------------------------------
// cim.viewer.renderer :: Helper object encapsulating view rendering.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// --------------------------------------------------------
window.cim.viewer.renderer = {};

// --------------------------------------------------------
// cim.viewer.renderer.forModel :: Renders a view of a CIM v1.5 model.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// --------------------------------------------------------
window.cim.viewer.renderer.forModel = (function() {
    // Associated metadata used to generate view html.
    var myMetadata = cim.viewer.metadata.forModel;

    // Associated view options.
    var myOptions = cim.viewer.options.forModel;

    // Returns set of model sections to be rendered.
    // @options     Rendering options.
    var getSectionsForRendering = function(options) {
        var result = [];
        if (options.sections.overview.display === true) {
            result.push(myMetadata.sections.overview);
        }
        if (options.sections.parties.display === true) {
            result.push(myMetadata.sections.parties);
        }
        if (options.sections.citations.display === true) {
            result.push(myMetadata.sections.citations);
        }
        if (options.sections.components.display === true) {
            result.push(myMetadata.sections.components);
        }
        if (options.sections.cimInfo.display === true) {
            result.push(myMetadata.sections.cimInfo);
        }
        return result;
    }

    // Rendering task - destory/create viewer container divs.
    // @ctx         Rendering context.
    var doRenderTask1 = function(ctx) {
        var injectDiv = cim.viewer.HTML.appendHTMLDiv;  // Div injection function.
        var injectContent = cim.viewer.HTML.getHTMLForFieldGroup;  // Section content injection function.

        // Destroy precious rendering.
        $jq('#cimModelViewer').remove();

        // Inject viewer container divs.
        injectDiv = cim.viewer.HTML.appendHTMLDiv;
        injectDiv('cimViewerDialog', 'cimModelViewer', 'cim-model-viewer');
        injectDiv('cimModelViewer', 'cimModel', 'cim-model');

        // Inject section content.
        _.each(ctx.sections, function(section) {
            injectDiv('cimModel', section.domID);
            $jq('#' + section.domID).addClass('cim-model-section');
            cim.viewer.HTML.injectHTML(ctx.cim, section, injectContent);
        });
    };

    // Rendering task - inject tabs.
    // @ctx         Rendering context.
    var doRenderTask2 = function(ctx) {
        // Inject tabs container div.
        var $tabs = cim.viewer.HTML.appendHTMLTabs('cimModelViewer', 'cimModelViewTabs');

        // Setup tabs.
        _.each(ctx.sections, function(sectionInfo) {
            cim.viewer.HTML.appendHTMLTab($tabs, sectionInfo);
        });

        // Move standard view into tabs container.
        $jq('#cimModel').appendTo('#cimModelViewTabs')

        // Instantiate JQuery tab.
        $jq('#cimModelViewTabs').tabs({
            fx: {opacity : 'toggle'}
        });

        // Style JQuery tab.
        $jq("#cimModelViewTabs ul li a").css({
            'width' : '110px',
            'text-align' : 'center'
        });
    };

    // Rendering task - inject inline section headers.
    // @ctx         Rendering context.
    var doRenderTask3 = function(ctx) {
        var renderFn;      // Rendering function to apply.

        // Inject html for each section.
        renderFn = cim.viewer.HTML.prependHTMLDiv;
        _.each(ctx.sections, function(section) {
            renderFn(section.domID,
                     section.domID + 'Header',
                     'cim-section-header ui-state-highlight ui-corner-all',
                     section.titleLong);
        });
    };

    // Rendering task - inject accordion divs.
    // @ctx         Rendering context.
    var doRenderTask4 = function(ctx) {
        var renderFn;      // Rendering function to apply.

        // Inject accordion class.
        $jq('#cimModel').addClass('cim-viewer-accordion');

        // Inject accordion header elements.
        renderFn = cim.viewer.HTML.prependHTMLAccordion;
        _.each(ctx.sections, function(section) {
            renderFn(section.domID, section.title);
        });

        // Instantiate JQuery accordion.
        $jq('.cim-viewer-accordion').accordion({
            fillSpace: true
        });
    };

    // // Rendering task - display dialog.
    // @ctx         Rendering context.
    var doRenderTask5 = function(ctx) {
        if (ctx.options.dialog.display === true) {
            cim.viewer.dialog.open(ctx);
        }
    };

    // Renders CIM model instance based upon passed options.
    // @cim         JSON encoded CIM instance (model).
    // @options     Rendering options.
    var render = function(cim, options) {
        var ctx, taskMode, task;

        // Instantiate context.
        ctx = {
            cim : cim,
            options : options,
            sections : getSectionsForRendering(options),
            tasks : [
                [ 'all', doRenderTask1 ],
                [ 'tabs', doRenderTask2 ],
                [ 'inline', doRenderTask3 ],
                [ 'accordion', doRenderTask4 ],
                [ 'all', doRenderTask5 ]
            ]
        };

        // Parse options (guarantees they are valid).
        myOptions.parse(ctx.options);

        // Invoke tasks.
        _.each(ctx.tasks, function(taskInfo){
            taskMode = taskInfo[0];
            if (taskMode === 'all' || taskMode === options.mode) {
                task = taskInfo[1];
                task(ctx);
            }
        });
    };

    // Return rendering function.
    return {
        render : render
    };
}());


// --------------------------------------------------------
// cim.viewer.renderer.forDataObject :: Renders a view of a CIM v1.5 data object.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// --------------------------------------------------------
window.cim.viewer.renderer.forDataObject = (function() {
    // Associated metadata used to generate view html.
    var myMetadata = cim.viewer.metadata.forDataObject;

    // Associated view options.
    var myOptions = cim.viewer.options.forDataObject;

    // Renders CIM data object instance based upon passed options.
    // @cim         JSON encoded CIM instance (data object).
    // @options     Rendering options.
    var render = function(cim, options) {
        // TODO
        alert('TODO - render data objects');
    };

    // Return rendering function.
    return {
        render : render
    };
}());


// --------------------------------------------------------
// cim.viewer.renderer.forExperiment :: Renders a view of a CIM v1.5 experiment.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// --------------------------------------------------------
window.cim.viewer.renderer.forExperiment = (function() {
    // Associated metadata used to generate view html.
    var myMetadata = cim.viewer.metadata.forExperiment;

    // Associated view options.
    var myOptions = cim.viewer.options.forExperiment;

    // Renders CIM experiment instance based upon passed options.
    // @cim         JSON encoded CIM instance (experiment).
    // @options     Rendering options.
    var render = function(cim, options) {
        // TODO
        alert('TODO - render experiments');
    };

    // Return rendering function.
    return {
        render : render
    };
}());


// --------------------------------------------------------
// cim.viewer.renderer.forSimulation :: Renders a view of a CIM v1.5 simulation.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// --------------------------------------------------------
window.cim.viewer.renderer.forSimulation = (function() {
    // Associated metadata used to generate view html.
    var myMetadata = cim.viewer.metadata.forSimulation;

    // Associated view options.
    var myOptions = cim.viewer.options.forSimulation;

    // Renders CIM model instance based upon passed options.
    // @cim         JSON encoded CIM instance (simulation).
    // @options     Rendering options.
    var render = function(cim, options) {
        // TODO
        alert('TODO - render simulations');
    };

    // Return rendering function.
    return {
        render : render
    };
}());



// --------------------------------------------------------
// cim.viewer.rendererForPlatform :: Renders a view of a CIM v1.5 platform.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// --------------------------------------------------------
window.cim.viewer.renderer.forPlatform = (function() {
//    // Associated metadata used to generate view html.
//    var myMetadata = cim.viewer.metadata.forSimulation;
//
//    // Associated view options.
//    var myOptions = cim.viewer.options.forSimulation;

    // Renders CIM model instance based upon passed options.
    // @cim         JSON encoded CIM instance (simulation).
    // @options     Rendering options.
    var render = function(cim, options) {
        // TODO
        alert('TODO - render platform');
    };

    // Return rendering function.
    return {
        render : render
    };
}());


// --------------------------------------------------------
// cim.repository :: CIM repository (will be replaced by call to web-service).
// N.B. Declared in a functional closure so as not to pollute global namespace.
// N.B. Executes remote CIM web services.
// --------------------------------------------------------
window.cim.repository = {
    // Collection of models.
    'models' : [
        // HadGEM2-ES model.
        {
            cimInfo : {
                id : '309f6a26-e2b3-11df-bf17-00163e9152a5',
                version : '1',
                schema : '1.5',
                createDate : '2011-07-19T16:34:10.197064',
                source : 'Metafor CMIP5 Questionnaire',
                type : 'Model'
            },
            shortName : 'HadGEM2-ES',
            synonyms : 'HadGEM2',
            longName : 'Hadley Global Environment Model 2 - Earth System',
            description : 'The HadGEM2-ES model was a two stage development from HadGEM1, representing improvements in the physical model (leading to HadGEM2-AO) and the addition of earth system components and coupling (leading to HadGEM2-ES). [1] The HadGEM2-AO project targeted two key features of performance: ENSO and northern continent land-surface temperature biases. The latter had a particularly high priority in order for the model to be able to adequately model continental vegetation. Through focussed working groups a number of mechanisms that improved the performance were identified. Some known systematic errors in HadGEM1, such as the Indian monsoon, were not targeted for attention in HadGEM2-AO. HadGEM2-AO substantially improved mean SSTs and wind stress and improved tropical SST variability compared to HadGEM1. The northern continental warm bias in HadGEM1 has been significantly reduced. The power spectrum of El Nino is made worse, but other aspects of ENSO are improved. Overall there is a noticeable improvement from HadGEM1 to HadGEM2-AO when comparing global climate indices. [2] In HadGEM2-ES the vegetation cover is better than in the previous HadCM3LC model especially for trees, and the productivity is better than in the non-interactive HadGEM2-AO model. The presence of too much bare soil in Australia though may cause problems for the dust emissions scheme. The simulation of global soil and biomass carbon stores are good and agree well with observed estimates except in regions of errors in the vegetation cover. HadGEM2-ES compares well with the C4MIP ensemble of models. The distribution of NPP is much improved relative to HadCM3LC. At a site level the component carbon fluxes validate better against observations and in particular the timing of the growth season is significantly improved. The ocean biology (HadOCC) allows the completion of the carbon cycle and the provision of di-methyl sulphide (DMS) emissions from phytoplankton. DMS is a significant source of sulphate aerosol over the oceans. The diat-HadOCC scheme is an improvement over the standard HadOCC scheme as it differentiates between diatom and non-diatom plankton. These have different processes for removing carbon from the surface to the deep ocean, and respond differently to iron nutrients. The HadOCC scheme performs well with very reasonable plankton distributions, rates of productivity and emissions of DMS. The diat-HadOCC scheme has slightly too low levels of productivity which requires further tuning to overcome. The additions of a tropospheric chemistry scheme, new aerosol species (organic carbon and dust) and coupling between the chemistry and sulphate aerosols have significantly enhanced the earth system capabilities of the model. This has improved the tropospheric ozone distribution and the distributions of aerosol species compared to observations, both of which are important for climate forcing. Including interactive earth system components has not significantly affected the physical performance of the model.',
            parties : [
                { role : 'PI',
                  name : 'Chris Jones',
                  address : 'Met Office Hadley Centre, Fitzroy Road, Exeter, Devon, UK, EX1 3PB',
                  email : '--'
                },
                { role : 'funder',
                  name : 'UK Met Office Hadley Centre',
                  address : 'Met Office Hadley Centre, Fitzroy Road, Exeter, Devon, UK, EX1 3PB',
                  email : '--'
                },
                { role : 'contact',
                  name : 'Chris Jones',
                  address : 'Met Office Hadley Centre, Fitzroy Road, Exeter, Devon, UK, EX1 3PB',
                  email : '--'
                },
                { role : 'centre',
                  name : 'UK Met Office Hadley Centre',
                  address : 'Met Office Hadley Centre, Fitzroy Road, Exeter, Devon, UK, EX1 3PB',
                  email : '--'
                }
            ],
            citations : [
                { title : 'Bellouin et al. 2007',
                  type : 'Online Other',
                  reference : 'Bellouin N., O. Boucher, J. Haywood, C. Johnson, A. Jones, J. Rae, and S. Woodward. (2007) Improved representation of aerosols for HadGEM2.. Meteorological Office Hadley Centre, Technical Note 73, March 2007.',
                  location : 'http://www.metoffice.gov.uk/publications/HCTN/HCTN_73.pdf' },
                { title : 'Collins et al. 2008',
                  type : 'Online Other',
                  reference : 'Collins W.J. , N. Bellouin, M. Doutriaux-Boucher, N. Gedney, T. Hinton, C.D. Jones, S. Liddicoat, G. Martin, F. OConnor, J. Rae, C. Senior, I. Totterdell, and S. Woodward (2008) Evaluation of the HadGEM2 model. Meteorological Office Hadley Centre, Technical Note 74.',
                  location : 'http://www.metoffice.gov.uk/publications/HCTN/HCTN_74.pdf' }
            ],
            components : [
                { shortName : 'Aerosols',
                  longName : 'Aerosols',
                  description : 'The model includes interactive schemes for sulphate, sea salt, black carbon from fossil-fuel emissions, organic carbon from fossil-fuel emissions, mineral dust, and biomass-burning aerosols. The model also includes a fixed monthly climatology of mass-mixing ratios of secondary organic aerosols from terpene emissions (biogenic aerosols).',
                  type : 'Aerosols',
                  keyProperties : [
                      { name : 'biomass_burning',
                        units : 'kg/m2/s',
                        description : 'Emissions of aerosols from biomass burning injected at the surface for grassfires and homogeneously throughout the boundary layer for forest fire emissions. Represented as a monthly mean field on the N96 grid' },
                      { name : 'fossil_fuel_black_carbon',
                        units : 'kg/m2/s',
                        description : 'Emissions of primary black carbon from fossil fuel and biofuel injected at 80m as a monthly mean field on the N96 grid' },
                      { name : 'fossil_fuel_organic_carb',
                        units : 'kg/m2/s',
                        description : 'Emissions of primary organic carbon from fossil fuel and biofuel injected at 80m as a monthly mean field on the N96 grid' }
                  ]},
                { shortName : 'Atmosphere',
                  longName : 'Atmosphere',
                  description : 'The HadGEM2-ES model incorporates the Met Offices New Dynamics framework that provides a non-hydrostatic, fully compressible, deep atmosphere formulation with fewer approximations to the basic equations; semi-Lagrangian advection of all prognostic variables except density, permitting relatively long timesteps to be used at higher resolution; a conservative and monotone treatment of tracer transport; and improved geostrophic adjustment properties bringing better balance to the coupling. HadGEM2-ES includes interactive modelling of atmospheric aerosols, driven by surface and elevated emissions and including tropospheric chemical processes as well as physical removal processes such as washout. The aerosol species represented in the model are sulphate, black carbon, biomass smoke, sea salt, organic carbon, mineral dust and a biogenic climatology. The atmospheric component has a horizontal resolution of 1.25 degrees of latitude by 1.875 degrees of longitude with 38 layers in the vertical extending to over 39 km in height. The model uses the Arakawa C-grid horizontally and the Charney-Phillips grid vertically. The atmospheric timestep period is 30 minutes (48 timesteps per day).',
                  type : 'Atmosphere',
                  keyProperties : [
                      { name : 'anthro_SO2_aerosol',
                        units : 'kg/m2/s',
                        description : 'Anthropogenic sulphur dioxide emissions injected at the surface, except for energy emissions and half of industrial emissions which are injected at 0.5 km to represent chimney-level emissions.' },
                      { name : 'biogenic_emission_aeroso',
                        units : 'kg/kg',
                        description : '3D concentrations of organic aerosols from biogenic emissions. [CHECK cf name and short name]' },
                      { name : 'land DMS emissions',
                        units : 'kg/m2/s',
                        description : 'Constant for land-based emissions amounting to 0.86 Tg/yr from Spiro et al., 1992)' }
                  ]},
                { shortName : 'Atmospheric Chemistry',
                  longName : 'Atmospheric Chemistry',
                  description : 'The atmospheric chemistry component of HadGEM2-ES is a configuration of the United Kingdom Chemistry and Aerosols (UKCA) model (OConnor et al., 2009; 2010; www.ukca.ac.uk) running a relatively thorough description of inorganic odd oxygen (Ox), nitrogen (NOy), hydrogen (HOx), and carbon monoxide (CO) chemistry with near-explicit treatment of methane (CH4), ethane (C2H6), propane (C3H8), and acetone (Me2CO) degradation (including formaldehyde (HCHO), acetaldehyde (MeCHO), peroxy acetyl nitrate (PAN), and peroxy propionyl nitrate (PPAN)). It makes use of 25 tracers and represents 41 species. Large-scale transport, convective transport, and boundary layer mixing are treated. The chemistry scheme accounts for 25 photolytic reactions, 83 bimolecular reactions, and 13 uni- and termolecular reactions.',
                  type : 'Atmospheric Chemistry',
                  keyProperties : [
                      { name : 'acetaldehyde_emissions',
                        units : 'kg/m2/s',
                        description : 'Surface emissions of acetaldehyde, prescribed as annual quantities on the model grid' },
                      { name : 'acetone_emissions',
                        units : 'kg/m2/s',
                        description : 'Surface emissions of acetone, prescribed as annual quantities on the model grid' },
                      { name : 'aircraft_NOx_emissions',
                        units : 'kg/m2/s',
                        description : '3D emissions of nitrogen oxides from aircraft, prescribed as monthly quantities on the 3D model grid.' }
                  ]},
                { shortName : 'Land Ice',
                  longName : 'Land Ice',
                  description : 'The major ice sheets (Greenland and Antarctica), and minor ice caps (Ellesmere, Devon and Baffin islands, Iceland, Svalbard, Novaya and Severnaya Zemlya, and Stikine) are depicted as static ice. They are initialised with a snow depth of 50,000 mm of water equivalent. Further ablation or accumulation has an impact on sea level. Runoff follows the river routing scheme and enters the oceans at predefined river outflow points. Calving at the coastal boundaries is simulated through a fresh water flux to the ocean evenly distributed over the area of observed icebergs in both hemispheres. The value of the freshwater flux is calculated to exactly balance the time mean ice sheet surface mass balance over the preindustrial control simulation.',
                  type : 'Land Ice',
                  keyProperties : [
                      { name : 'LandIceAlbedo',
                        units : '',
                        description : 'prescribed' }
                  ]},
                { shortName : 'Land Surface',
                  longName : 'Land Surface',
                  description : 'The second version of the U.K. Met Office Surface Exchange Scheme (MOSES-II) (Cox et al. 1999; Essery et al. 2003) is used. This allows tiling of land surface heterogeneity using nine different surface types. A separate surface energy balance is calculated for each tile and area-weighted grid box mean fluxes are computed, which are thus much more realistic than when a single surface type is assumed. In addition, vegetation leaf area is allowed to vary seasonally, providing a more realistic representation of seasonal changes in surface fluxes. Tiling of coastal grid points allows separate treatment of land and sea fractions. This in combination with the increased ocean model resolution greatly improves the coastline, particularly in island regions.',
                  type : 'Land Surface',
                  keyProperties : [
                      { name : 'changing_anthro_land_use',
                        units : 'dimensionless value between 0 and 1',
                        description : 'Time varying fractional mask of anthropogenic disturbance' },
                      { name : 'initial_land_use',
                        units : 'dimensionless value between 0 and 1',
                        description : 'Prescribed fractions of urban areas, lakes, ice, broadleaf tree, needleleaf tree, C3 grass, C4 grass, shrub and bare soil' },
                      { name : 'well_mixed_gas_CO2',
                        units : 'kg/kg',
                        description : 'CO2 concentrations prescribed as a single global constant provided as an annual number but interpolated in the model at each timestep. Provided as a mass mixing ratio with units of kg/kg. CO2 concentrations are passed to the models radiation scheme, terrestrial carbon cycle and ocean carbon cycle.' }
                  ]},
                { shortName : 'Ocean',
                  longName : 'Ocean',
                  description : 'The ocean component is based on the Bryan-Cox code (Bryan 1969; Cox 1984) and follows the ocean component of HadGEM1 (Johns et al., 2006) very closely. The model uses a latitude-longitude grid with a zonal resolution of 1 degree, and a meridional resolution of 1 degree between the poles and 30 degrees, from which it increases smoothly to 0.333 degrees at the equator - giving a grid of 360 x 216 points. It has 40 unevenly spaced levels in the vertical, with enhanced resolution near the surface better to resolve the mixed layer and thermocline. The forward timestep period is 1 hour, with a mixing timestep of once per day. HadGEM2 uses a bathymetry derived from the Smith and Sandwell (1997) 1/30 degrees depth dataset merged with ETOPO5 (1988) 1/12 degrees data at high latitudes, interpolated to the model grid and smoothed using a five-point (1:4:1) two-dimensional filter. Where this procedure obstructs important narrow pathways (e.g., Denmark Strait, Faroes-Shetland Channel, Vema Channel, and around the Indonesian archipelago), the bathymetry is adjusted to allow some flow at realistic depths (with reference to Thompson 1995). The land masks for the ocean grid differs from that used for the atmosphere model, due to the differences in model resolutions. To enable daily coupling between the models a tiling scheme has been introduced. For each atmosphere grid box, fractions of the fluxes can be coupled to land, sea and sea ice models so that the total flux is conserved - though locally the flux may not be conserved so diagnosis can be difficult. The only ancillary flux used by the ocean model is to enable a balance in the freshwater flux to be maintained, since the accumulation of frozen water on land is not returned into the freshwater cycle, i.e there is no representation of icebergs calving off ice shelves. The ancillary flux is used to add freshwater back into the model, calibrated from a HadGEM2 reference integration to give a balanced freshwater budget.',
                  type : 'Ocean',
                  keyProperties : [
                      { name : 'Ocean Key Properties',
                        units : '',
                        description : 'The sea water formulation of the equation of state is following that of UNESCO.' },
                      { name : 'well_mixed_gas_CO2',
                        units : 'kg/kg',
                        description : 'CO2 concentrations prescribed as a single global constant provided as an annual number but interpolated in the model at each timestep. Provided as a mass mixing ratio with units of kg/kg. CO2 concentrations are passed to the models ocean carbon cycle.' }
                  ]},
                { shortName : 'Ocean Biogeo Chemistry',
                  longName : 'Ocean Biogeo Chemistry',
                  description : 'There is a simple prognostic ecosystem model (the Diat-HadOCC model), with three nutrients (combined nitrate and ammonium, dissolved silicate and dissolved iron), two phytoplankton (diatoms and other phytoplankton), one zooplankton and three detrital compartments (detrital nitrogen, detrital carbon and detrital silicate). There are also two prognostic tracers that are only used in a diagnostic capacity (i.e. their concentrations are affected by, but do not affect, the other compartments): these are dissolved ammonium and dissolved oxygen. Nitrogen is used as the model currency. The diatoms have a variable silicate:nitrate ratio, so diatom silicate is another compartment. All three of the detrital compartments sink with a constant velocity, and are remineralised at a rate that is depth-dependent The zooplankton and both phytoplankton compartments have fixed elemental carbon:nitrogen ratios which allow the flows of carbon through the ecosystem to be linked to the corresponding nitrogen flows. As well as the ecosystem compartments listed above dissolved inorganic carbon (DIC) and total alkalinity (TAlk) are included as prognostic tracers. Those two compartments, along with the model temperature and salinity, are used to calculate the ocean surface pCO2, the air-sea flux of CO2 and the ocean pH. The air-sea fluxes of CO2 and DMS are each calculated every ocean time-step and their daily means are passed through the coupler to the atmosphere each day.',
                  type : 'Ocean Biogeo Chemistry',
                  keyProperties : [
                      { name : 'BasicApproximations',
                        units : '',
                        description : 'Eulerian model, N3 P2 Z1 D1 ecosystem; no diel cycle, constant detrital sinking rate, constant (carbonate) rain ratio.' },
                      { name : 'ListOfPrognosticVariables',
                        units : '',
                        description : 'Dissolved inorganic nitrogen (nitrate+ammonia); dissolved silicate; dissolved iron; diatoms; other phytoplankton; zooplankton; diatom silicate; detrital nitrogen; detrital carbon; detrital silicate; dissolved inorganic carbon; total alkalinity; dissolved oxygen.' }
                  ]},
            ]
        },

        // MPI-ESM-LR model.
        {
            cimInfo : {
                id : '8a6778c6-daa9-11df-b8ba-00163e9152a5',
                version : '3',
                schema : '1.5',
                createDate : '2011-09-22T13:21:14.832846',
                source : 'Metafor CMIP5 Questionnaire',
                type : 'Model'
            },
            shortName : 'MPI-ESM-LR',
            synonyms : 'MPEH5',
            longName : 'MPI Earth System Model running on low resolution grid',
            description : 'ECHAM6 and JSBACH / MPIOM and HAMOCC coupled via OASIS3.',
            parties : [
                { role : 'PI',
                  name : 'Marco Giorgetta',
                  address : 'Department (modelling group): Atmosphäre im Erdsystem',
                  email : 'marco.giorgetta@zmaw.de'
                },
                { role : 'funder',
                  name : 'Max Planck Institute for Meteorology',
                  address : '--',
                  email : '--'
                },
                { role : 'contact',
                  name : 'cmip5-mpi-esm',
                  address : '--',
                  email : '--'
                },
                { role : 'centre',
                  name : 'Max Planck Institute for Meteorology',
                  address : '--',
                  email : '--'
                }
            ],
            citations : [
                { title : 'HAMMOC',
                  type : 'Online Other',
                  reference : 'HAMOCC: Technical Documentation',
                  location : 'http://www.mpimet.mpg.de/fileadmin/models/MPIOM/HAMOCC5.1_TECHNICAL_REPORT.pdf' },
                { title : 'Marsland_etal_2003',
                  type : 'Online Refereed',
                  reference : 'Marsland, S. J., H. Haak, J. H. Jungclaus, M. Latif and F. Roeske, 2003: The Max-Planck-Institute global ocean/sea ice model with orthogonal curvilinear coordinates.", Ocean Modelling, 5, 91-127.',
                  location : '--' },
                { title : 'Raddatz_etal_2007',
                  type : 'Online Refereed',
                  reference : '"Raddatz T.J., Reick C.H., Knorr W., Kattge J., Roeckner E., Schnur R., Schnitzler K.-G., Wetzel P., Jungclaus J., 2007:Will the tropical land biosphere dominate the climate-carbon cycle feedback during the twenty first century?",Climate Dynamics, 29, 565-574, doi 10.1007/s00382-007-0247-8.',
                  location : '--' }

            ],
            components : [
                { shortName : 'ECHAM6',
                  longName : 'ECHAM6 atmospheric global general circulation model',
                  description : 'The land vegetation module JSBACH is integrated module of the ECHAM6 code but can run stand alone.',
                  type : 'Aerosols',
                  keyProperties : [
                      { name : 'biomass_burning',
                        units : 'kg/m2/s',
                        description : 'Emissions of aerosols from biomass burning injected at the surface for grassfires and homogeneously throughout the boundary layer for forest fire emissions. Represented as a monthly mean field on the N96 grid' },
                      { name : 'fossil_fuel_black_carbon',
                        units : 'kg/m2/s',
                        description : 'Emissions of primary black carbon from fossil fuel and biofuel injected at 80m as a monthly mean field on the N96 grid' },
                      { name : 'fossil_fuel_organic_carb',
                        units : 'kg/m2/s',
                        description : 'Emissions of primary organic carbon from fossil fuel and biofuel injected at 80m as a monthly mean field on the N96 grid' }
                  ]},
                { shortName : 'HAMOCC',
                  longName : 'Hamburg Model of Ocean Carbon Cycle',
                  description : 'HAMOCC is a sub-model that simulates biogeochemical tracers in the oceanic water column and in the sediment. All biogeochemical tracers are fully advected and mixed by the hosting OGCM (MPIOM). The biogeochemical model itself is driven by the same radiation as the OGCM to compute photosynthesis. Temperature and salinity are used to calculate various transformation rates and constants e.g., for solubility of carbon dioxide.',
                  type : 'OceanBiogeoChemistry',
                  keyProperties : [
                      { name : 'anthro_SO2_aerosol',
                        units : 'kg/m2/s',
                        description : 'Anthropogenic sulphur dioxide emissions injected at the surface, except for energy emissions and half of industrial emissions which are injected at 0.5 km to represent chimney-level emissions.' },
                      { name : 'biogenic_emission_aeroso',
                        units : 'kg/kg',
                        description : '3D concentrations of organic aerosols from biogenic emissions. [CHECK cf name and short name]' },
                      { name : 'land DMS emissions',
                        units : 'kg/m2/s',
                        description : 'Constant for land-based emissions amounting to 0.86 Tg/yr from Spiro et al., 1992)' }
                  ]},
                { shortName : 'JSBACH',
                  longName : 'JSBACH',
                  description : 'JSBACH can be run as stand alone model, but is an integrated code part of ECHAM6.',
                  type : 'LandSurface',
                  keyProperties : [
                      { name : 'acetaldehyde_emissions',
                        units : 'kg/m2/s',
                        description : 'Surface emissions of acetaldehyde, prescribed as annual quantities on the model grid' },
                      { name : 'acetone_emissions',
                        units : 'kg/m2/s',
                        description : 'Surface emissions of acetone, prescribed as annual quantities on the model grid' },
                      { name : 'aircraft_NOx_emissions',
                        units : 'kg/m2/s',
                        description : '3D emissions of nitrogen oxides from aircraft, prescribed as monthly quantities on the 3D model grid.' }
                  ]},
                { shortName : 'MPIOM',
                  longName : 'Max - Planck Institute Ocean Model in low resolution',
                  description : 'The Max- Planck- Institute ocean model (MPIOM) is the ocean- sea ice component of the Max- Planck- Institute climate model (Roeckner et al., 2006; Jungclaus et al., 2006). It includes an embedded dynamic/ thermodynamic sea ice model with a viscous- plastic rheology following Hibler (1979) and a bottom boundary layer scheme for the flow across steep topography. A model descriptin can be found at Marsland et al. (2003). Furthermore the ocean biogeochemical modul HAMOCC is an integrated module of the MPI-OM code, but can run stand alone.',
                  type : 'Ocean',
                  keyProperties : [
                      { name : 'LandIceAlbedo',
                        units : '',
                        description : 'prescribed' }
                  ]},
                { shortName : 'Sea Ice',
                  longName : 'MPIOM SeaIceComponent',
                  description : 'Sea ice component is included in MPI-OM, It includes an embedded dynamic/ thermodynamic sea ice model with a viscous- plastic rheology following Hibler (1979).',
                  type : 'SeaIce',
                  keyProperties : [
                      { name : 'changing_anthro_land_use',
                        units : 'dimensionless value between 0 and 1',
                        description : 'Time varying fractional mask of anthropogenic disturbance' },
                      { name : 'initial_land_use',
                        units : 'dimensionless value between 0 and 1',
                        description : 'Prescribed fractions of urban areas, lakes, ice, broadleaf tree, needleleaf tree, C3 grass, C4 grass, shrub and bare soil' },
                      { name : 'well_mixed_gas_CO2',
                        units : 'kg/kg',
                        description : 'CO2 concentrations prescribed as a single global constant provided as an annual number but interpolated in the model at each timestep. Provided as a mass mixing ratio with units of kg/kg. CO2 concentrations are passed to the models radiation scheme, terrestrial carbon cycle and ocean carbon cycle.' }
                  ]}
            ]
        },
    ],

    dataObjects : [
        {
            acronym : 'HADGEM2_20C3M_1_D0_mrsos1',
            description : 'This dataset represents daily output: instantaneous daily values at 0UTC (D0) or daily averaged/accumulated/max/min values (DM) of the selected variable for ENSEMBLES. The model output was prepared for the IPCC Fourth Assessment 20C3M experiment. For specific scenario details see experiment summary. These data are in netCDF format.',
            dataContent : {
                name : 'moisture_content_of_soil_layer',
                description : 'moisture_content_of_soil_layer [CF-Standard Name]',
                standardName : 'moisture_content_of_soil_layer',
                units : 'm s-1',
                aggregation : 'sum',
                frequency : 'Other'
            }
        }
    ]
};

// --------------------------------------------------------
// cim.servicesProxy :: CIM services proxy.
// N.B. Declared in a functional closure so as not to pollute global namespace.
// N.B. Executes remote CIM web services.
// --------------------------------------------------------
window.cim.servicesProxy = (function() {

    // Instance of mocked cim repository.
    var _repository = window.cim.repository;

    // Module level error handler.
    // @exception   Exception that has occurred processing a request.
    // @onError     Callback to invoke when function unsucessfully executes.
    var _errorHandler = function(exception, operation, onError) {
        var error;      // Error message to be returned.

        // Invoke client error handler (if specified).
        if (_.isFunction(onError) === true) {
            onError(exception, operation);

        // Otherwise perform default tasks.
        } else {
            error = "CIM web service processing error:\n";
            error += "\tOperation\t: "+ operation + "\n";
            error += "\tError\t\t: " + exception.message + "\n";
            alert(error);
        }
    };

    // Object wrapped in functional closure to avaoid global namespace pollution.
    return {
        // Query service executes search against backend CIM repository.
        query : {
            // Gets a model.
            // @project         Project with which model is associated.
            // @name            Short Name of model being returned.
            // @onSuccess       Callback to invoke when function sucessfully executes.
            // @onError         Callback to invoke when function unsucessfully executes.
            getModelByName : function (project, name, onSuccess, onError) {
                var result;      // Model to be returned.

                // Search for model against CIM repository web-service.
                try {
                    // Search by short name.
                    result = _(_repository.models).detect(function(m) {
                        return m.shortName.toUpperCase() === name.toUpperCase();
                    });

                    // If not found search by synonym.
                    if (result === undefined) {
                        result = _(_repository.models).detect(function(m) {
                            return m.synonyms.toUpperCase().search(name.toUpperCase()) !== -1;
                        });
                    }

                    // If not found return first.
                    if (result === undefined) {
                        result = _.first(_repository.models);
                    }
                }
                catch(exception) {
                    _errorHandler(exception, 'query.getModelByName', onError);
                }

                // Invoke callback (if specified).
                if (_.isFunction(onSuccess) === true) {
                    onSuccess(result);
                }

                return result;
            },

            // Gets a data object by acronym.
            // @project         Project with which data object is associated.
            // @acronym         Name of data object being returned.
            // @onSuccess       Callback to invoke when function sucessfully executes.
            // @onError         Callback to invoke when function unsucessfully executes.
            getDataObjectByAcronym : function (project, acronym, onSuccess, onError) {
                var result;      // data object to be returned.

                // Search for data object against CIM repository web-service.
                try {
                    // TODO
                }
                catch(exception) {
                    _errorHandler(exception, 'query.getDataObjectByAcronym', onError);
                }

                // Invoke callback (if specified).
                if (_.isFunction(onSuccess) === true) {
                    onSuccess(result);
                }

                return result;
            },

            // Gets a experiment by name.
            // @project         Project with which experiment is associated.
            // @name            Name of data object being returned.
            // @onSuccess       Callback to invoke when function sucessfully executes.
            // @onError         Callback to invoke when function unsucessfully executes.
            getExperimentByName : function (project, name, onSuccess, onError) {
                var result;      // Experiment to be returned.

                // Search for experiment against CIM repository web-service.
                try {
                    // TODO
                }
                catch(exception) {
                    _errorHandler(exception, 'query.getExperimentByName', onError);
                }

                // Invoke callback (if specified).
                if (_.isFunction(onSuccess) === true) {
                    onSuccess(result);
                }

                return result;
            },

            // Gets a simulation by name.
            // @project         Project with which simulation is associated.
            // @name            Name of data object being returned.
            // @onSuccess       Callback to invoke when function sucessfully executes.
            // @onError         Callback to invoke when function unsucessfully executes.
            getSimulationByName : function (project, name, onSuccess, onError) {
                var result;      // Simulation to be returned.

                // Search for simulation against CIM repository web-service.
                try {
                    // TODO
                }
                catch(exception) {
                    _errorHandler(exception, 'query.getSimulationByName', onError);
                }

                // Invoke callback (if specified).
                if (_.isFunction(onSuccess) === true) {
                    onSuccess(result);
                }

                return result;
            }
        }
    };
}());