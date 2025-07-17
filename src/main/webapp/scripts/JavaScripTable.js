/*
 * JavaScripTable version 1.2.1
 *
 * The JavaScripTable is a dynamic HTML table created with JavaScript.
 *
 * Dependencies: 
 *  - JavaScriptUtil.js
 *  - Parsers.js
 *
 * Author: Luis Fernando Planella Gonzalez (lfpg_dev@pop.com.br)
 * Home Page: http://javascriptools.sourceforge.net
 * 
 * You may freely distribute this file, since you include this header 
 * along with the script
 */
 
///////////////////////////////////////////////////////////////////////////////
/*
 * Constant declaration
 */
//Type constants
var JST_TYPE_STRING   = 0;
var JST_TYPE_NUMERIC  = 1;
var JST_TYPE_CURRENCY = 2;
var JST_TYPE_DATE     = 3;
var JST_TYPE_BOOLEAN  = 4;

//Selection constants
var JST_SEL_NONE   = "";
var JST_SEL_SINGLE = "radio";
var JST_SEL_MULTI  = "checkbox";

//Navigation bar constants
var JST_NAV_NONE   = 0;
var JST_NAV_TOP    = 1;
var JST_NAV_BOTTOM = 2;
var JST_NAV_BOTH   = 3;

//Align constants
var JST_ALIGN_LEFT    = "left";
var JST_ALIGN_RIGHT   = "right";
var JST_ALIGN_CENTER  = "center";
var JST_ALIGN_JUSTIFY = "justify";

//Vertical align constants
var JST_VALIGN_BASELINE = "baseline";
var JST_VALIGN_SUB = "sub";
var JST_VALIGN_SUPER = "super";
var JST_VALIGN_TOP = "top";
var JST_VALIGN_TEXT_TOP = "text-top";
var JST_VALIGN_MIDDLE = "middle";
var JST_VALIGN_BOTTOM = "bottom";
var JST_VALIGN_TEXT_BOTTOM = "text-bottom";

//Edit control type constants
var JST_CONTROL_TEXT     = "text";
var JST_CONTROL_PASSWORD = "password";
var JST_CONTROL_TEXTAREA = "textarea";
var JST_CONTROL_CHECKBOX = "checkbox";
var JST_CONTROL_RADIO    = "radio";
var JST_CONTROL_SELECT   = "select";

//Table client/server side constants
var JST_CLIENT_SIDE = 0;
var JST_SERVER_SIDE = 1;

///////////////////////////////////////////////////////////////////////////////
// DEFAULT PROPERTY VALUES CONSTANTS
///////////////////////////////////////////////////////////////////////////////

//////////////   Basic behaviour constants
//Operation mode: client/server side
var JST_DEFAULT_OPERATION_MODE = JST_CLIENT_SIDE;
//Use paging?
var JST_DEFAULT_USE_PAGING = true;
//Page size
var JST_DEFAULT_PAGE_SIZE = 25;
//Maximum rows on the table
var JST_DEFAULT_MAX_ROWS = -1;

//////////////   Basic appearance constants
//Table width
var JST_DEFAULT_WIDTH = "";
//Table align
var JST_DEFAULT_ALIGN = "";
//Table border
var JST_DEFAULT_BORDER = "0";
//Table cellpadding
var JST_DEFAULT_PADDING = "1";
//Table cellspacing
var JST_DEFAULT_SPACING = "0";
//Use row highliting on mouse hover?
var JST_DEFAULT_ROW_HIGHLITING = true;

//////////////   Navigation bar constants
//Navigation bar location: top/bottom/both/none
var JST_DEFAULT_NAVIGATION = JST_NAV_BOTTOM;
//Allow the link to the table's top?
var JST_DEFAULT_ALLOW_TOP_LINK = true;
//Allow the user to change the paging style (paged results/all results)?
var JST_DEFAULT_ALLOW_CHANGE_PAGING = true;
//Allow the user to change the page size?
var JST_DEFAULT_ALLOW_CHANGE_PAGE_SIZE = false;

//////////////   Row selection constants
//Type of row selection (none/single/multiple)
var JST_DEFAULT_SELECTION_TYPE = JST_SEL_NONE;
//Selection control name (null will generate one)
var JST_DEFAULT_SELECTION_NAME = null;
//Selection control's cell's valign property
var JST_DEFAULT_SELECTION_VALIGN = JST_VALIGN_TOP;

//////////////   Encoding separators constants
//Value separator
var JST_DEFAULT_VALUE_SEPARATOR = ",";
//Column separator
var JST_DEFAULT_COLUMN_SEPARATOR = ";";
//Row separator
var JST_DEFAULT_ROW_SEPARATOR = "\n";

//////////////   Style sheet classes constants
//Table class
var JST_DEFAULT_TABLE_CLASS = "JSTTable";
//Table header class
var JST_DEFAULT_HEADER_CLASS = "JSTHeader";
//Table footer class
var JST_DEFAULT_FOOTER_CLASS = "JSTFooter";
//Column header class
var JST_DEFAULT_COLUMN_HEADER_CLASS = "JSTColumnHeader";
//Selection control class
var JST_DEFAULT_SELECTION_CONTROL_CLASS = "JSTSelectionControl";
//Edit control class (for text/password/textarea/select controls)
var JST_DEFAULT_EDIT_CONTROL_CLASS = "JSTEditControl";
//Edit control class on invalid user input
var JST_DEFAULT_INVALID_EDIT_CONTROL_CLASS = "JSTInvalidEditControl";
//Edit control class (for radio/checkbox control)
var JST_DEFAULT_EDIT_CONTROL_RADIO_CHECKBOX_CLASS = "JSTEditControlRadioCheckbox";
//Odd row class
var JST_DEFAULT_ODD_ROW_CLASS = "JSTOddRow";
//Even row class
var JST_DEFAULT_EVEN_ROW_CLASS = "JSTEvenRow";
//Highlited row class
var JST_DEFAULT_HIGHLITED_ROW_CLASS = "JSTHighlited";
//Navigation bar class
var JST_DEFAULT_NAVIGATION_CLASS = "JSTNavigation";
//Link class
var JST_DEFAULT_LINK_CLASS = "JSTLink";

//////////////   Table text constants
//Header text
var JST_DEFAULT_HEADER_TEXT = "";
//Footer text
var JST_DEFAULT_FOOTER_TEXT = "";
//Empty table text (when empty, show an empty table)
var JST_DEFAULT_EMPTY_TABLE_TEXT = "";
//Ascending Sort label
var JST_DEFAULT_ASC_LABEL = new Image();
JST_DEFAULT_ASC_LABEL.src = JST_IMAGES_DIR+"asc.gif";
//Descending Sort label
var JST_DEFAULT_DESC_LABEL = new Image();
JST_DEFAULT_DESC_LABEL.src = JST_IMAGES_DIR+"desc.gif";
//True label (showed for true values)
var JST_DEFAULT_TRUE_LABEL = new Image();
JST_DEFAULT_TRUE_LABEL.src = JST_IMAGES_DIR+"true.gif";
//False label (showed for false values)
var JST_DEFAULT_FALSE_LABEL = new Image();
JST_DEFAULT_FALSE_LABEL.src = JST_IMAGES_DIR+"false.gif";

//////////////   Messages constants
//Status bar text for column sort
//var JST_DEFAULT_SORT_MESSAGE = "Click here to sort by this column";
//Text displayed for a single row
//var JST_DEFAULT_SINGLE_ROW_TEXT = "row";
//Text displayed for a multiple row
//var JST_DEFAULT_MULTIPLE_ROW_TEXT = "rows";
//Text for paged results. Use the variables: ${current} = current page, ${total} = total pages
//var JST_DEFAULT_PAGE_TEXT = "Page ${current} of ${total}";
//Status bar text for the first page link
//var JST_DEFAULT_FIRST_PAGE_DESCRIPTION = "Click here to navigate to the first page";
//Text for the first page link
//var JST_DEFAULT_FIRST_PAGE_TEXT = "<img src=\"images/first.gif\" border=\"0\">";
//Status bar text for the previous page link
//var JST_DEFAULT_PREVIOUS_PAGE_DESCRIPTION = "Click here to navigate to the previous page";
//Text for the previous page link
//var JST_DEFAULT_PREVIOUS_PAGE_TEXT = "<img src=\"images/prev.gif\" border=\"0\">";
//Status bar text for the next page link
//var JST_DEFAULT_NEXT_PAGE_DESCRIPTION = "Click here to navigate to the next page";
//Text for the next page link
//var JST_DEFAULT_NEXT_PAGE_TEXT = "<img src=\"images/next.gif\" border=\"0\">";
//Status bar text for the last page link
//var JST_DEFAULT_LAST_PAGE_DESCRIPTION = "Click here to navigate to the last page";
//Text for the next last link
//var JST_DEFAULT_LAST_PAGE_TEXT = "<img src=\"images/last.gif\" border=\"0\">";
//Status bar text for the show all rows link
//var JST_DEFAULT_SHOW_ALL_DESCRIPTION = "Click here to show all rows";
//Text for the show all rows link
//var JST_DEFAULT_SHOW_ALL_TEXT = "[show all]";
//Status bar text for the use paged results link
//var JST_DEFAULT_USE_PAGING_DESCRIPTION = "Click here to show paged rows";
//Text for the use paged results link
//var JST_DEFAULT_USE_PAGING_TEXT = "[show paged]";
//Status bar text for the go to table top link
//var JST_DEFAULT_TOP_DESCRIPTION = "Click here to go to the table top";
//Text for the go to table top link
//var JST_DEFAULT_TOP_TEXT = "[top]";
//Status bar text for the change page link
//var JST_DEFAULT_CHANGE_PAGE_DESCRIPTION = "Click here to change the current page";
//Text prompted on the change page dialog
//var JST_DEFAULT_CHANGE_PAGE_PROMPT = "Navigate to wich page?";
//Status bar text for the change page size link
//var JST_DEFAULT_CHANGE_PAGE_SIZE_DESCRIPTION = "Click here to change the page size";
//Text prompted on the change page size dialog
//var JST_DEFAULT_CHANGE_PAGE_SIZE_PROMPT = "How many rows per page?";

//////////////   Default parsers constants
//Parser for string inputs
var JST_DEFAULT_STRING_PARSER = new StringParser();
//Parser for number inputs
var JST_DEFAULT_NUMBER_PARSER = new NumberParser(-1);
//Parser for currency inputs
var JST_DEFAULT_CURRENCY_PARSER = new NumberParser(2, ",", ".", true, "R$", true, true);
//Parser for date inputs
var JST_DEFAULT_DATE_PARSER = new DateParser("dd/MM/yyyy");
//Parser for boolean inputs
var JST_DEFAULT_BOOLEAN_PARSER = new BooleanParser(JST_DEFAULT_TRUE_LABEL, JST_DEFAULT_FALSE_LABEL);
//Parser for string encoding
var JST_DEFAULT_STRING_ENCODING_PARSER = new EscapeParser();
//Parser for number encoding
var JST_DEFAULT_NUMBER_ENCODING_PARSER = new NumberParser(-1, ".", ",", false, "$", false, false);
//Parser for currency encoding
var JST_DEFAULT_CURRENCY_ENCODING_PARSER = new NumberParser(-1, ".", ",", false, "$", false, false);
//Parser for date encoding
var JST_DEFAULT_DATE_ENCODING_PARSER = new DateParser("yyyyMMdd");
//Parser for boolean encoding
var JST_DEFAULT_BOOLEAN_ENCODING_PARSER = new BooleanParser("true", "false");

var JST_DEFAULT_FILA_ID="filaID_"; // Añadido para soporte colores en onClick
    
///////////////////////////////////////////////////////////////////////////////
/*
 * This is the main class.
 * Parameters: 
 *    id: The name of the variable the table will be assigned to.
 *       A property with this name will be created in the container.
 *    container: The reference to the container object where the 
 *       table will be drawed in. A valid container is an HTML Element
 *       that supports nested elements (DIV, TD and so on)
 */
function JavaScripTable(id, container, width, align, border, padding, spacing) {

    //Property declarations
    this.columns = new Array();
    this.rows = new Array();
    this.rowsClass = undefined;
    this.width = width || JST_DEFAULT_WIDTH;
    this.align = align || JST_DEFAULT_ALIGN;
    this.border = border || JST_DEFAULT_BORDER;
    this.padding = padding || JST_DEFAULT_PADDING;
    this.spacing = spacing || JST_DEFAULT_SPACING;
    this.sortColumn = -1;
    this.ascSort = true;
    this.usePaging = JST_DEFAULT_USE_PAGING;
    this.pageSize = JST_DEFAULT_PAGE_SIZE;
    this.currentPage = 1;
    this.rowFunction = "";
      this.rowFunctionClick = "";
    this.useRowHighliting = JST_DEFAULT_ROW_HIGHLITING;
    this.printing = false;
    this.maxRows = JST_DEFAULT_MAX_ROWS;
    this.selectedRow = undefined;

    //Navigation properties
    this.operationMode = JST_DEFAULT_OPERATION_MODE;
    this.updateTableFunction = function () {alert("The callback for table update events was not defined")};
    this.rowCount = 0;
    this.navigation = JST_DEFAULT_NAVIGATION;
    this.allowTopLink = JST_DEFAULT_ALLOW_TOP_LINK;
    this.allowChangePaging = JST_DEFAULT_ALLOW_CHANGE_PAGING;
    this.allowChangePageSize = JST_DEFAULT_ALLOW_CHANGE_PAGE_SIZE;

    //Selection properties
    this.selectionType = JST_DEFAULT_SELECTION_TYPE;
    this.selectionName = JST_DEFAULT_SELECTION_NAME;
    this.selectionValign = JST_DEFAULT_SELECTION_VALIGN;
        
    //Separators
    this.valueSeparator = JST_DEFAULT_VALUE_SEPARATOR;
    this.columnSeparator = JST_DEFAULT_COLUMN_SEPARATOR;
    this.rowSeparator = JST_DEFAULT_ROW_SEPARATOR;

    //StyleSheet classes for the elements
    this.tableClass = JST_DEFAULT_TABLE_CLASS;
    this.headerClass = JST_DEFAULT_HEADER_CLASS;
    this.footerClass = JST_DEFAULT_FOOTER_CLASS;
    this.columnHeaderClass = JST_DEFAULT_COLUMN_HEADER_CLASS;
    this.selectionControlClass = JST_DEFAULT_SELECTION_CONTROL_CLASS;
    this.editControlClass = JST_DEFAULT_EDIT_CONTROL_CLASS;
    this.invalidEditControlClass = JST_DEFAULT_INVALID_EDIT_CONTROL_CLASS;
    this.editControlRadioCheckboxClass = JST_DEFAULT_EDIT_CONTROL_RADIO_CHECKBOX_CLASS;
    this.oddRowClass = JST_DEFAULT_ODD_ROW_CLASS;
    this.evenRowClass = JST_DEFAULT_EVEN_ROW_CLASS;
    this.highlitedRowClass = JST_DEFAULT_HIGHLITED_ROW_CLASS;
    this.navigationClass = JST_DEFAULT_NAVIGATION_CLASS;
    this.linkClass = JST_DEFAULT_LINK_CLASS;

    //Table's texts
    this.headerText = JST_DEFAULT_HEADER_TEXT;
    this.footerText = JST_DEFAULT_FOOTER_TEXT;
    this.emptyTableText = JST_DEFAULT_EMPTY_TABLE_TEXT;
    
    //Table's labels
    this.ascLabel = JST_DEFAULT_ASC_LABEL;
    this.descLabel = JST_DEFAULT_DESC_LABEL;
    this.trueLabel = JST_DEFAULT_TRUE_LABEL;
    this.falseLabel = JST_DEFAULT_FALSE_LABEL;

    //Messages
    this.invalidTableIdMessage = "The id must be set";
    this.invalidTableContainerMessage = "The container object must be set, and must contain the innerHTML property";
    this.tableNotInitializedMessage = "The JavaScripTable was not correctly initialized";
    this.columnAddWithRowsMessage = "A column cannot be added to the table if it already contains rows";
    this.invalidColumnMessage = "The specified column is invalid";
    this.invalidRowMessage = "The specified row is invalid";
    this.invalidRowIdMessage = "The row's identifier must be informed";
    this.rowIdAlreadyInUseMessage = "The row's identifier is already in use";

    this.sortMessage = JST_DEFAULT_SORT_MESSAGE;
    this.singleRowText = JST_DEFAULT_SINGLE_ROW_TEXT;
    this.multipleRowText = JST_DEFAULT_MULTIPLE_ROW_TEXT;
    this.pageText = JST_DEFAULT_PAGE_TEXT; //Use the variables: ${current} = current page, ${total} = total pages
    this.firstPageDescription = JST_DEFAULT_FIRST_PAGE_DESCRIPTION;
    this.firstPageText = JST_DEFAULT_FIRST_PAGE_TEXT;
    this.previousPageDescription = JST_DEFAULT_PREVIOUS_PAGE_DESCRIPTION;
    this.previousPageText = JST_DEFAULT_PREVIOUS_PAGE_TEXT;
    this.nextPageDescription = JST_DEFAULT_NEXT_PAGE_DESCRIPTION;
    this.nextPageText = JST_DEFAULT_NEXT_PAGE_TEXT;
    this.lastPageDescription = JST_DEFAULT_LAST_PAGE_DESCRIPTION;
    this.lastPageText = JST_DEFAULT_LAST_PAGE_TEXT;
    this.showAllDescription = JST_DEFAULT_SHOW_ALL_DESCRIPTION;
    this.showAllText = JST_DEFAULT_SHOW_ALL_TEXT;
    this.usePagingDescription = JST_DEFAULT_USE_PAGING_DESCRIPTION;
    this.usePagingText = JST_DEFAULT_USE_PAGING_TEXT;
    this.topDescription = JST_DEFAULT_TOP_DESCRIPTION;
    this.topText = JST_DEFAULT_TOP_TEXT;
    this.changePageDescription = JST_DEFAULT_CHANGE_PAGE_DESCRIPTION;
    this.changePagePrompt = JST_DEFAULT_CHANGE_PAGE_PROMPT;
    this.changePageSizeDescription = JST_DEFAULT_CHANGE_PAGE_SIZE_DESCRIPTION;
    this.changePageSizePrompt = JST_DEFAULT_CHANGE_PAGE_SIZE_PROMPT;

    //Parsers for data entry
    this.stringParser   = JST_DEFAULT_STRING_PARSER;
    this.numberParser   = JST_DEFAULT_NUMBER_PARSER;
    this.currencyParser = JST_DEFAULT_CURRENCY_PARSER;
    this.dateParser     = JST_DEFAULT_DATE_PARSER;
    this.booleanParser  = JST_DEFAULT_BOOLEAN_PARSER;
    
    //Parsers for data encoding
    this.stringEncodingParser   = JST_DEFAULT_STRING_ENCODING_PARSER;
    this.numberEncodingParser   = JST_DEFAULT_NUMBER_ENCODING_PARSER;
    this.currencyEncodingParser = JST_DEFAULT_CURRENCY_ENCODING_PARSER;
    this.dateEncodingParser     = JST_DEFAULT_DATE_ENCODING_PARSER;
    this.booleanEncodingParser  = JST_DEFAULT_BOOLEAN_ENCODING_PARSER;

    // Restauración clases de visualización tras clic
    this.lastSelected = null;
    this.lastSelectedClass = null;

    //Initializes the table
    var valid = true;
    if (id == null || id == "") {
        alert(this.invalidTableIdMessage);
        valid = false;
    }
    if (isInstance(container, String)) {
        container = getObject(container);
    }
    if (container == null || container.innerHTML == null) {
        alert(this.invalidTableContainerMessage);
        valid = false;
    }
    this.id = id;
    this.container = container;
    this.form = null;
    
    //Create an property on the top frame for later calling the table
    top["_" + this.id + "_"] = this;
    
    /*
     * Adds the specified column to the table
     */
    this.addColumn = function(column) {
        if (this.rows.length > 0) {
            alert(this.columnAddWithRowsMessage);
            return;
        }
        if (!(isInstance(column, Column))) {
            alert(this.invalidColumnMessage);
            return;
        }
        column.index = this.columns.length;
        column.table = this;
        if (column.watch) {
            column.watch("index", function (prop, oldVal, newVal) { return oldVal; });
            column.watch("table", function (prop, oldVal, newVal) { return oldVal; });
        }
        this.columns[this.columns.length] = column;
        return column;
    }
    
    /*
     * Removes the column specified by the index
     */
    this.removeColumn = function(index) {
        var ret = this.columns[index];
        this.columns.splice(index, 1);
        return ret;
    }
    
    /*
     * Removes all columns from the table
     */
    this.removeAllColumns = function() {
        var ret = this.columns;
        this.columns = new Array();
        return ret;
    }
    
    /*
     * Returns the number of columns in the table
     */
    this.getColumnCount = function() {
        return this.columns.lenght;
    }
    
    /*
     * Returns the column speficied by the index
     */
    this.getColumnByIndex = function(index) {
        return this.columns[index];
    }

    /*
     * Returns the current sort column
     */
    this.getSortColumn = function() {
        return this.sortColumn >= 0 ? this.getColumnByIndex(this.sortColumn) : null;
    }

    /*
     * Returns all the columns
     */
    this.getAllColumns = function() {
        return this.columns;
    }

    /*
     * Returns all the visible columns
     */
    this.getVisibleColumns = function() {
        var ret = new Array();
        for (var i = 0; i < this.columns.length; i++) {
            column = this.columns[i];
            if (column.visible) {
                ret[ret.length] = column;
            }
        }
        return ret;
    }
    
    /*
     * Adds the specified row. If refresh is set to true, re-renders the table
     */
    this.addRow = function(row, refresh) {
        //Check if the maximum number of rows was already reached
        if ((this.maxRows >= 0) && (this.rows.length >= this.maxRows)) {
            return;
        }
        
        //Validate the row
        if (!isInstance(row, Row)) {
            alert(this.invalidRowMessage);
            return;
        }
        if (row.id == null) {
            alert(this.invalidRowIdMessage);
            return;
        }
        if (this.getRowById(row.id) != null) {
            alert(this.rowIdAlreadyInUseMessage);
            return;
        }
        //Ensures the values size will be, at least, the columns' size
        while (row.values.length < this.columns.length) {
            row.values[row.values.length] = null;
            row.possibleValues[row.possibleValues.length] = new Map();
        }
        //Ensures the possibleValues length to be correct
        while (row.possibleValues.length < row.values.length) {
            row.possibleValues[row.possibleValues.length] = null;
        }    

        //Add the row
        this.rows[this.rows.length] = row;
        row.table = this;
        
        //Refresh the table if needed
        if (booleanValue(refresh)) {
            this.render();
        }
        return row;
    }
    
    /*
     * Removes the row specified by it's id. If refresh is set to true, re-renders the table
     */
    this.removeRowById = function(id, refresh) {
        var ret = null;
        
        //Search the id
        for (var i = 0; i < this.rows.length; i++) {
            row = this.rows[i];
            if (row.id == id) {
                //Return the deleted row
                ret = row;
                this.rows.splice(i, 1);
                break;
            }
        }
                
        //Refresh the table if needed
        if (booleanValue(refresh)) {
            this.render();
        }

        return ret;
    }
    
    /*
     * Removes selected rows from the table. If refresh is set to true, re-renders the table
     */
    this.removeSelectedRows = function(refresh) {
        var ret = new Array();
        
        //Search the selected rows
        var i = 0;
        while (i < this.rows.length) {
            var row = this.rows[i];
            if (row.selected) {
                ret[ret.length] = row;
                this.rows.splice(i, 1);
            } else {
                i++;
            }
        }
        
        //Refresh the table if needed
        if (booleanValue(refresh)) {
            this.render();
        }

        //Return the deleted rows
        return ret;
    }
    
    /*
     * Removes all rows from the table. If refresh is set to true, re-renders the table
     */
    this.removeAllRows = function(refresh) {
        var ret = this.rows;
        this.rows = new Array();
        
        //Refresh the table if needed
        if (booleanValue(refresh)) {
            this.render();
        }

        return ret;
    }
    
    /*
     * Returns all the rows
     */
    this.getAllRows = function() {
        return this.rows;
    }
    
    /*
     * Returns the all the row identifiers
     */
    this.getAllRowIds = function() {
        var ret = new Array();
        for (var i = 0; i < this.rows.length; i++) {
            row = this.rows[i];
            ret[ret.length] = row.id;
        }
        return ret;
    }

    /*
     * Returns all the selected rows
     */
    this.getSelectedRows = function() {
        var ret = new Array();
        for (var i = 0; i < this.rows.length; i++) {
            row = this.rows[i];
            if (row.selected) {
                ret[ret.length] = row;
            }
        }
        return ret;
    }
    
    /*
     * Returns the identifiers of the selected rows
     */
    this.getSelectedRowIds = function() {
        var ret = new Array();
        for (var i = 0; i < this.rows.length; i++) {
            row = this.rows[i];
            if (row.selected) {
                ret[ret.length] = row.id;
            }
        }
        return ret;
    }
    
    /*
     * Returns the selected row count
     */
    this.getSelectedRowCount = function() {
        return this.getSelectedRows().length;
    }
    
    /*
     * Returns the row with the given identifier
     */
    this.getRowById = function(id) {
        var ret = null;
        for (var i = 0; (ret == null) && (i < this.rows.length); i++) {
            row = this.rows[i];
            if (row.id == id) {
                ret = row;
            }
        }
        return ret;
    }
    
    /*
     * Gets the value of the cell from the row with the given id and 
     * the specified column index
     */
    this.getCellValue = function(rowId, colIndex) {
        var row = this.getRowById(rowId);
        return row.values[colIndex];
    }
    
    /*
     * Gets the formatted value of the cell from the row with the given id and 
     * the specified column index
     */
    this.getFormattedCellValue = function(rowId, colIndex) {
        var column = this.getColumnByIndex(colIndex);
        if (column == null) {
            alert(this.invalidColumnMessage);
            return null;
        }
        var row = this.getRowById(rowId);
        if (row == null) {
            alert(this.invalidRowIdMessage);
            return null;
        }
        return column.getParser(row).format(this.getCellValue(row.id, column.index));
    }
    
    /*
     * Sets the value of the cell from the row with the given id and 
     * the specified column index. If refresh is set to true, re-renders the table
     */
    this.setCellValue = function(rowId, colIndex, value, refresh) {
        var row = this.getRowById(rowId);
        row.values[colIndex] = value;
        cellEditor = row.editors[colIndex];
        if (cellEditor != null) {
            setValue(cellEditor, value);
        }
            
        //Refresh the table if needed
        if (booleanValue(refresh)) {
            this.render();
        }
    }
    
    /*
     * Sets the formatted value of the cell from the row with the given id and 
     * the specified column index. If refresh is set to true, re-renders the table
     */
    this.setFormattedCellValue = function(rowId, colIndex, value, refresh) {
        var column = this.getColumnByIndex(colIndex);
        if (column == null) {
            alert(this.invalidColumnMessage);
            return null;
        }
        var row = this.getRowById(rowId);
        if (row == null) {
            alert(this.invalidRowIdMessage);
            return null;
        }
        value = column.getParser(row).parse(value);
        this.setCellValue(row.id, column.index, value, refresh);
    }
    
    /*
     * Encodes the data into a String. The data may be an Array or not, of
     * rows or single data. Rows are encoded by their encode() method.
     * Other data type is used directly. Each row or data is separated by the
     * String contained in table's rowSeparator property.
     * When the data is a row or an Array of rows, the columns parameter can be
     * set as an Array of integers, indicating wich columns will be processed.
     * The row identifier is always encoded, unless the encodeRowId parameter is
     * set to false
     */
    this.encode = function(data, columns, encodeRowId) {
        
        columns = columns || null;
        encodeRowId = encodeRowId || true;
        
        //If nothing was informed, encode all table data
        if (data == null) {
            data = this.getAllRows();
        }
        
        //If something was informed, assume an array
        if (!isInstance(data, Array)) {
            data = [data];
        }
        var temp = new Array();

        //Encode each array item
        for (var i = 0; i < data.length; i++) {
            var current = data[i];
            temp[i] = escapeCharacters(((isInstance(current, Row)) ? current.encode(columns, encodeRowId) : current), this.rowSeparator, true);
        }
        
        //Return the single string
        return temp.join(this.rowSeparator);
    }

    /*
     * Sets the current page, re-rendering the table
     */
    this.setPage = function(page) {
        this.currentPage = page;
        this.update();
    }

    /*
     * Sets if the table will use paging, re-rendering it
     */
    this.setUsePaging = function(flag) {
        this.usePaging = flag;
        this.update();
    }
    
    /*
     * Sets the sort for the specified column
     */
    this.setSort = function(column, asc) {

        if (column == this.sortColumn) {
            if (asc == null) {
                this.ascSort = !this.ascSort;
            } else {
                this.ascSort = booleanValue(asc);
            }
        } else {
            this.sortColumn = column;
            this.ascSort = (asc == null ? true : booleanValue(asc));
        }

        //Sorts the array
        this.rows.sort(this.sort);
        this.update();
    }
    
    
       this.setSortOriginal = function(column, asc) {

        if (column == this.sortColumn) {
            if (asc == null) {
                this.ascSort = !this.ascSort;
            } else {
                this.ascSort = booleanValue(asc);
            }
        } else {
            this.sortColumn = column;
            this.ascSort = (asc == null ? true : booleanValue(asc));
        }
        
        //Sorts the array
        this.rows.sort(this.sort);
        this.update();
    }
    
    
    //Para que ordene un array especifico
    //BUZON DE ENTRADA    
      this.setSortColumna = function(column, asc) {
        
        if (column == this.sortColumn) {
            if (asc == null) {
                this.ascSort = !this.ascSort;
            } else {
                this.ascSort = booleanValue(asc);
            }
        } else {
            this.sortColumn = column;
            this.ascSort = (asc == null ? true : booleanValue(asc));
        }
        this.update();
    }
    
    
    /*
     * Sets the selection for all rows
     */
    this.selectAll = function(flag) {
        for (var i = 0; i < this.rows.length; i++) {
           var row = this.rows[i];
           this.setSelection(row.id, flag);
        }
    }
        
    /*
     * Sets the selection for the specified row
     */
    this.setSelection = function(rowId, flag) {
        this.getRowById(rowId).selected = flag;
        if (this.form != null) {
            var selection = this.form.elements[this.getSelectionName() + rowId];
            if (selection != null) {
                selection.checked = flag;
            }
        }
    }

    /*
     * Returns the external table variable
     */
    this.getTableVar = function() {
        return "top['_" + this.id + "_']";
    }

    /*
     * Returns the name of the variable representing the selection
     */
    this.getSelectionName = function() {
        return this.selectionName != null ? this.selectionName : "selection_" + this.id;
    }

    /*
     * Returns the name of the edit control for the row / column
     */
    this.getControlName = function(row, column) {
        var isRadioCheckbox = inArray(column.editControl.type, [JST_CONTROL_RADIO, JST_CONTROL_CHECKBOX]);
        if (column.editControl != null && column.editControl.name != null) {
            var name = column.editControl.name;
            if (isRadioCheckbox) {
                name += "_" + row.id;
            }
            return name;
        } else {
            return "edit_" + this.id + (isRadioCheckbox ? "_" + row.id : "") +  "_" + column.index;
        }
    }

    /*
     * Returns the name of the edit control for the row x column
     */
    this.getControlId = function(row, column) {
        return "id_edit_" + this.id + "_" + row.id + "_" + column.index;
    }

    //Checks for a valid input
    function validateValue(value, row, column) {
        var parser = column.getParser(row);
        var valid = parser.isValid(value);
        if (column.validateFunction != null) {
            var ret = column.validateFunction(value, column, row);
            if (ret == null) {
                //nothing returned
                valid = true;
            } else if ((ret == true) || (ret == false)) {
                //ret is the validation flag
                valid = ret;
            } else {
                //ret is a validation message
                alert(ret);            
                valid = false;
            }
        }
        return valid;
    }

    /*
     * Updates the value from the edit control for the row x column
     */
    this.updateValue = function(rowId, columnIndex) {
        var column = this.getColumnByIndex(columnIndex);
        var row = this.getRowById(rowId)
        var parser = column.getParser(row);
        var valid;
        var value;
        var targetControls;
        var isRadioCheckbox = inArray(column.editControl.type, [JST_CONTROL_RADIO, JST_CONTROL_CHECKBOX]);
        
        //Checkboxes and radios must be handled separatedly
        if (isRadioCheckbox) {
            var controls = getObject(this.getControlName(row, column));
            targetControls = controls;
            var stringValues = getValue(controls);
            if (stringValues == null) {
                stringValues = [];
            } else if (!isInstance(stringValues, Array)) {
                stringValues = [stringValues];
            }
            value = new Array(stringValues.length);
            for (var i = 0; i < stringValues.length; i++) {
                value[i] = parser.parse(stringValues[i]);
                valid = validateValue(value[i], row, column);
                if (!valid) break;
            }
            if (value.length == 0) {
                value = null;
            } else if (value.length == 1) {
                value = value[0];
            }
        } else {
            var control = getObject(this.getControlId(row, column));
            targetControls = [control];
            var stringValue = getValue(control);
            var value = parser.parse(value);
            valid = validateValue(value, row, column);
        }

        var className;
        if (valid) {
            className = isRadioCheckbox ? this.editControlRadioCheckboxClass : this.editControlClass;
            row.values[column.index] = value;
        } else {
            className = this.invalidEditControlClass;
            row.values[column.index] = null;
        }
        for (var i = 0; i < targetControls.length; i++) {
            targetControls[i].className = className;
        }
    }
    
    /*
     * Finishes the editing of a field, formatting it's value
     */
    this.finishEdit = function(rowId, columnIndex) {
        var column = this.getColumnByIndex(columnIndex);
        if (inArray(column.editControl.type, [JST_CONTROL_TEXT, JST_CONTROL_PASSWORD, JST_CONTROL_TEXTAREA])) {
            var row = this.getRowById(rowId)
            var control = getObject(this.getControlId(row, column));
            var parser = column.getParser(row);
            var value = parser.parse(getValue(control));
            if (!isEmpty(value)) {
                value = parser.format(value);
            }
            setValue(control, value);
        }
        if (validateMessage != null) {
            validateMessage = null;
        }
    }

    /*
     * Returns the number of rows
     */
    this.getRowCount = function() {
        return (this.operationMode == JST_CLIENT_SIDE) ? this.rows.length : this.rowCount;
    }
    
    /*
     * Returns the greatest page
     */
    this.getMaxPage = function() {
        return Math.ceil(this.getRowCount() / this.pageSize);
    }
    
    /*
     * Updates the table
     */
    this.update = function() {
        if (this.operationMode == JST_CLIENT_SIDE) {
            this.render();
        } else {
            this.updateTableFunction(this);
        }
    }
    
    
   function rowFunction(rowId,table){
alert("Clique no quadrinho ao lado dessa linha e depois no botão edit para edita-la.");
}
       function rowFunctionClick(rowId,table){
alert("Clique no quadrinho ao lado dessa linha e depois no botão edit para edita-la.");
}
    /*
     * Renders the table into the container
     */
    this.render = function() {
        //Check if the table was correctly initialized
        if (!valid) {
            alert(this.tableNotInitializedMessage);
            return;
        }
        
        //Check if the table is empty and the empty text is set
        if (((this.columns.length == 0) || (this.rows.length == 0)) && (!isEmpty(this.emptyTableText))) {
            this.container.innerHTML = this.emptyTableText;
            return;
        }
        
        //Renders the table
        var output = "";
        var usesSelection = this.selectionType != JST_SEL_NONE && !this.printing;
        var visibleColumns = this.getVisibleColumns();
        var renderedColumnCount = visibleColumns.length + (usesSelection ? 1 : 0);
        var tableVar = this.getTableVar();
        var navBar = "";
        var images = new Map();
        var editControls = new Array();
        
        //Gets the navigation bar code
        if ((this.navigation != JST_NAV_NONE) && (!this.printing)) {
            navBar = this.buildNavigation(renderedColumnCount);
        }

        //Determine wich rows will be visible
        var firstRow, lastRow, maxPage;
        if (this.usePaging) {
            var maxPage = this.getMaxPage();
            if (this.currentPage <= 0) {
                this.currentPage = 1;
            } else if (this.currentPage > maxPage) {
                this.currentPage = maxPage;
            }
            if (this.operationMode == JST_CLIENT_SIDE) {
                firstRow = Math.max(this.pageSize * (this.currentPage-1), 0);
                lastRow = Math.min(firstRow + this.pageSize, this.rows.length);
            } else {
                firstRow = 0;
                lastRow = Math.min(this.rows.length, this.pageSize);
            }
        } else {
            firstRow = 0;
            lastRow = this.rows.length;
        }
                
        //Creates an anchor above the table
        output = "<a name=\"tableTop" + this.id + "\"></a>";
        
        //Creates the main table tag
        output += "<table width=\"" + this.width + "\" align=\"" + this.align + "\" border=\"" + this.border + "\" class=\"" + this.tableClass + "\" cellpadding=\"" + this.padding + "\" cellspacing=\"" + this.spacing + "\">";

        //If the header text is set, put it
        if (!isEmpty(this.headerText)) {
            output += "<tr><td colspan=\"" + renderedColumnCount + "\" class=\"" + this.headerClass + "\">" + this.headerText + "</td></tr>";
        }
        
        //If the navigation bar is on top, render it
        if (this.navigation == JST_NAV_TOP || this.navigation == JST_NAV_BOTH) {
            output += navBar;
        }
        
        //Column header
        output += "<tr>";

        //Builds the selection column, if selection is being used
        if (usesSelection) {
            output += "<th width=\"20px\" class=\"" + this.columnHeaderClass + "\" >"
            if (this.selectionType == JST_SEL_SINGLE) {
                //Single selection: no select all control
                output += "&nbsp;";
            } else {
                //Multiple selection: Builds the select all control
                output += "<input style=\"border:0px\" type=\"checkbox\" class=\"" + this.selectionControlClass + "\" onclick=\"" + tableVar + ".selectAll(this.checked)\">";
            }
            output += "</th>";
        }
        
        //Builds the column headers
        for (var i = 0; i < visibleColumns.length; i++) {
            var column = visibleColumns[i];
            output += "<th nowrap width=\"" + column.width + "\" class=\"" + this.columnHeaderClass + "\" align=\"" + column.align + "\">";
            //Renders the header text
            if (isEmpty(trim(column.header))) {
                output += "&nbsp;";
            } else {
                //Only let the user change the sorting when the table is not for printing
                if (column.sortable && !this.printing) {
                    output += "<a class=\"" + this.columnHeaderClass + "\" href=\"JavaScript:" + tableVar + ".setSort(" + i + ");\" onMouseOver=\"window.status='" + this.sortMessage + "';return true;\" onMouseOut=\"window.status='';return true;\">";
                }
                output += column.header;
                //If the sorting link was created, close it
                if (column.sortable && !this.printing) {
                    output += "</a>";
                }
                //If the column being sorted is this, render the direction beside it
                if (column.sortable && this.sortColumn == i && !this.printing) {
                    output += "&nbsp;<a href=\"#\" onMouseOver=\"window.status='" + this.sortMessage + "';return true;\" onMouseOut=\"window.status='';return true;\" onclick=\"" + tableVar + ".setSort(" + i + ");\">";
                    output += this.handleLabel(((this.ascSort) ? this.ascLabel : this.descLabel), images);
                    output += "</a>";
                }
            }
            output += "</th>";
        }
        output += "</tr>";
        
        //Renders the rows
        var even = true;
        for (var i = firstRow; i < lastRow; i++) {
            var row = this.rows[i];
            // Redefinido para incorporar estilos particulares de cada fila
            var rowClass;

            if (this.rowsClass != undefined) {
                rowClass = this.rowsClass[i];
            } else {
                rowClass = (even ? this.evenRowClass : this.oddRowClass);
            }

            even = !even;
            output += "<tr id='"+this.JST_DEFAULT_FILA_ID+row.id+"' class=\"" + rowClass + "\" ";
            //If there's a row function (that is called when the user clicks a row), render it
            if (!this.printing && this.rowFunction != null) {
                output += "ondblclick=\"" + this.rowFunction + "('" + row.id + "', " + tableVar + ")\" ";
                // output += "onclick=\"" + this.rowFunctionClick + "('" + row.id + "', " + tableVar + ")\" ";
                output += "style=\"cursor:hand\"";
            }
          if (!this.printing && this.rowFunctionClick != null) {
                output += "onclick=\"if (" + tableVar + ".selectedRow != undefined && " + tableVar + ".selectedRow != this) " + tableVar + ".selectedRow.className='" + rowClass + "';"
                                   + tableVar + ".selectedRow = this;" + this.rowFunctionClick + "('" + row.id + "', " + tableVar + ");\" ";                
                output += "style=\"cursor:hand\"";
            }
            //If there's row highliting, renders it
            if (this.useRowHighliting && !this.printing) {
                output += "onmouseover=\"this.className='" + this.highlitedRowClass + "';\" " 
                        + "onmouseout=\"if (" + tableVar + ".selectedRow != this) this.className='" + rowClass + "';\"";
            }
            output += ">";
            
            //Renders the selection column, if used
            if (usesSelection) {
                output += "<td width=\"20px\" onclick=\"event.cancelBubble=true\">";
                if (row.selectable) {
                    output += "<input type=\"" + this.selectionType + "\" class=\"" + this.selectionControlClass + "\" name=\"" + this.getSelectionName() + "\" " 
                            + "id=\"" + this.getSelectionName() + row.id + "\" onclick=\"" + tableVar + ".setSelection('" + row.id + "', this.checked);\" "
                            + "value=\"" + row.id + "\" " + (row.selected ? "checked" : "") + "></td>";
                } else {
                    output += "&nbsp;";
                }
            }

            //Renders the columns
            for (var j = 0; j < visibleColumns.length; j++) {
                var column = visibleColumns[j];
                var values = row.values[column.index];
                //Ensures value is an array
                if (!isInstance(values, Array)) {
                    values = [values];
                }
                //Format the values
                var formattedValues = new Array();
                var tmpFmt = new Array();
                var parser = column.getParser(row);
                for (var k = 0; k < values.length; k++) {
                    value = values[k];
                    //Format the value
                    if (column.editable && isInstance(value, String)) {
                        value = replaceAll(value, "&", "&amp;");
                        value = replaceAll(value, "\"", "&quot;");
                        value = replaceAll(value, "'", "&apos;");
                    }
                    formattedValues[k] = isEmpty(value) ? null : parser.format(value);
                    if (!isEmpty(value)) {
                        //Checks if the value is an image
                        if ((formattedValues[k]) && (formattedValues[k].src)) {
                            formattedValues[k] = this.handleLabel(formattedValues[k], images);
                        }
                        
                        tmpFmt[tmpFmt.length] = formattedValues[k];
                    }
                }

                //Gets all the values
                var formattedValue = tmpFmt.join(this.valueSeparator + " ");
                
                output += "<td width=\"" + column.width + "\" align=\"" + column.align + "\" valign=\"" + column.valign + "\">";
                //Checks if the column is editable
                if (column.editable && row.editable && (isInstance(column.editControl, EditControl))) {
                    var controlName = this.getControlName(row, column);
                    var controlId = this.getControlId(row, column);
                    var editControl = column.editControl;
                    var updateFunction = this.getTableVar() + ".updateValue('" + row.id + "', " + column.index + ")";
                    var finishEditFunction = this.getTableVar() + ".finishEdit('" + row.id + "', " + column.index + ")";
                    var possibleValues = new Map();
                    if (isInstance(column.possibleValues, Map)) {
                        possibleValues.putAll(column.possibleValues);
                    }
                    if ((row.possibleValues != null) && (isInstance(row.possibleValues[column.index], Map))) {
                        possibleValues.putAll(row.possibleValues[column.index]);
                    }
                            
                    formattedValue = replaceAll(formattedValue, "\\", "\\\\");
                    //Render the correct control
                    switch (editControl.type) {
                        case JST_CONTROL_TEXTAREA:
                            output += "<textarea id=\"" + controlId + "\" class=\"" + this.editControlClass + "\" onblur=\"" + finishEditFunction + "\" onkeyup=\"" + updateFunction + "\" name=\"" + controlName + "\" " + editControl.attributes + " onclick=\"event.cancelBubble=true\">" + formattedValue + "</textarea>";
                            break;
                        case JST_CONTROL_SELECT:
                            output += "<select id=\"" + controlId + "\" class=\"" + this.editControlClass + "\" onchange=\"" + updateFunction + "\" name=\"" + controlName + "\" " + editControl.attributes + " onclick=\"event.cancelBubble=true\">";
                            //Get the possible values
                            var keys = possibleValues.getKeys();
                            for (var k = 0; k < keys.length; k++) {
                                var key = keys[k];
                                var value = possibleValues.get(key);
                                output += "<option value=\"" + key + "\" " + (inArray(key, values) ? "selected" : "") + " onclick=\"event.cancelBubble=true\">" + value + "</option>";
                            }
                            output += "</select>"
                            break;
                        case JST_CONTROL_RADIO:
                        case JST_CONTROL_CHECKBOX:
                            //If there were no values and is a boolean column, use the defaults
                            if ((possibleValues.size() == 0) && (column.type == JST_TYPE_BOOLEAN)) {
                                if (editControl.type == JST_CONTROL_CHECKBOX) {
                                    possibleValues.put('true', '');
                                } else {
                                    possibleValues.put('true', 'true');
                                    possibleValues.put('false', 'false');
                                }
                            }

                            var entries = possibleValues.getEntries();
                            for (var k = 0; k < entries.length; k++) {
                                var key = entries[k].key;
                                var value = entries[k].value;
                                if (k > 0) {
                                    if (editControl.multiLine) {
                                        output += "<br>";
                                    } else {
                                        output += "&nbsp;&nbsp;&nbsp;";
                                    }
                                }
                                var isChecked;
                                //Checks the true / false values
                                if (column.type == JST_TYPE_BOOLEAN) {
                                    isChecked = values[k];
                                } else {
                                    isChecked = inArray(key, values);
                                }
                                
                                output += "<div style=\"display:inline\" nowrap><input id=\"" + controlId + "\" class=\"" + this.editControlRadioCheckboxClass + "\" onclick=\"event.cancelBubble=true;" + updateFunction + "\" name=\"" + controlName + "\" " + editControl.attributes + " type=\"" + editControl.type + "\" value=\"" + key + "\" " + (isChecked ? "checked" : "") + ">" + (isEmpty(value) ? "" : ("&nbsp;" + value)) + "</div>";
                            }
                            break;
                        default:
                            output += "<input id=\"" + controlId + "\" class=\"" + this.editControlClass + "\" onclick=\"event.cancelBubble=true\" onblur=\"" + finishEditFunction + "\" onkeyup=\"" + updateFunction + "\" name=\"" + controlName + "\" " + editControl.attributes + " type=\"" + editControl.type + "\" value=\"" + formattedValue + "\">";
                            break;
                    }
                } else {
                    //Check if there's the function
                    var useCellFunction = (!isEmpty(column.cellFunction) && (row.useCellFunction));
                    if (useCellFunction) {
                        output += "<a class=\"\"" + this.linkClass + "\" href=\"JavaScript:" + column.cellFunction + "('" + row.id + "', " + column.index + ", " + tableVar + ")\" onclick=\"event.cancelBubble=true\">";
                    }
                    output += (isEmpty(formattedValue) ? "&nbsp;" : formattedValue);
                    if (useCellFunction) {
                        output += "</a>";
                    }
                }
                output += "</td>";
            }
            output += "</tr>";
        }

        //If the navigation bar is on bottom, render it
        if (this.navigation == JST_NAV_BOTTOM || this.navigation == JST_NAV_BOTH) {
            output += navBar;
        }
                    
        //If the footer text is set, put it
        if (!isEmpty(this.footerText)) {
            output += "<tr><td colspan=\"" + renderedColumnCount + "\" class=\"" + this.footerClass + "\">" + this.footerText + "</td></tr>";
        }
        
        output += "</table>";

        //This hidden is only used to determine the form enclosing the table
        this.container.innerHTML = "<input type=\"hidden\" name=\"hidden" + this.id + "\">";

        //Get the form enclosing the table
        var hidden = getObject("hidden" + this.id);
        if (hidden == null || hidden.form == null)  {
            this.form = null;
        } else {
            this.form = hidden.form;
        }

        //Renders the table into its container
        this.container.innerHTML = output;
        //window.open().document.write("<textarea rows=40 cols=150>" + output + "</textarea>");
        
        //Sets the images src properties
        var keys = images.getKeys();
        for (var i = 0; i < keys.length; i++) {
            var key = keys[i];
            var ref = document.images[key];
            var img = images.get(key);
            if (ref && img.src) {
                ref.src = img.src;
            }
        }
        
        //Apply the masks
        for (var i = 0; i < visibleColumns.length; i++) {
            var column = visibleColumns[i];
            if (column.getMaskFunction != null) {
                for (var j = firstRow; j < lastRow; j++) {
                    var row = this.rows[j];
                    var controls = getObject(this.getControlId(row, column));
                    if (controls == null) continue;
                    if (!isInstance(controls, Array)) {
                        controls = [controls];
                    }
                    for (var k = 0; k < controls.length; k++) {
                        var mask = column.getMaskFunction(controls[k], column, row);
                        if (mask != null) {
                            mask.updateFunction = tableVar + ".updateValue('" + row.id + "', " + column.index + ")";
                            mask.finishEditFunction = tableVar + ".finishEdit('" + row.id + "', " + column.index + ")";
                            mask.keyUpFunction = function() {
                                eval(this.updateFunction);
                            }
                            mask.blurFunction = function() {
                                eval(this.finishEditFunction);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /*
     * Sets the current page. If the page is not specified, prompts the user for the new page
     */
    this.changePage = function(page) {
        if (isNaN(page)) {
            do {
                page = prompt(this.changePagePrompt, this.currentPage);
                //Check if user canceled
                if (page == null) {
                    return;
                }
            } while (isNaN(page));
        }
        //Validates the bounds
        var maxPage = this.getMaxPage();
        page = parseInt(page);
        if (page <= 0) {
            page = 1;
        } else if (page > maxPage) {
            page = maxPage;
        }        
        //Updates the table
        this.currentPage = page;
        this.update();
    }

    /*
     * Sets the page size. If the size is not specified, prompts the user for the new size
     */
    this.changePageSize = function(size) {
        if (isNaN(size)) {
            do {
                size = prompt(this.changePageSizePrompt, this.pageSize);
                //Check if user canceled
                if (size == null) {
                    return;
                }
                size = parseInt(size);
            } while (isNaN(size));
        }
        //Check the bounds
        if (size <= 1) {
            size = 1;
        }        
        //Updates the table, checking if the page is not after the max
        this.pageSize = parseInt(size);
        var maxPage = this.getMaxPage();
        if (this.currentPage > maxPage) {
            this.currentPage = maxPage;
        }
        
        //Refresh the table
        this.update();
    }
        
    /*
     * Builds the source for the navigation bar
     */
    this.buildNavigation = function(renderedColumnCount) {
        var output;
        var rowCount = this.getRowCount();
        var tableVar = this.getTableVar();
        output  = "<tr><td colspan=\"" + renderedColumnCount + "\" class=\"" + this.navigationClass + "\">";
        output += rowCount + " " + (rowCount == 1 ? this.singleRowText : this.multipleRowText) + "&nbsp;&nbsp;&nbsp;"
        if (rowCount > 0) {
            //Checks if paging is being used or not
            if (this.usePaging) {
                //Using paging
                var maxPage = this.getMaxPage();
                //Put the back navigation if not on first page
                if (this.currentPage > 1) {
                    output += "<a class=\"" + this.linkClass + "\" href=\"JavaScript:" + tableVar + ".setPage(1);\" " +
                        "onMouseOver=\"window.status='" + this.firstPageDescription + "';return true;\" " +
                        "onMouseOut=\"window.status='';return true;\">" + this.firstPageText + "</a>&nbsp;";
                    output += "<a class=\"" + this.linkClass + "\" href=\"JavaScript:" + tableVar + ".setPage(" + (this.currentPage - 1) + ");\" " +
                        "onMouseOver=\"window.status='" + this.previousPageDescription +  "';return true;\" " +
                        "onMouseOut=\"window.status='';return true;\">" + this.previousPageText + "</a>&nbsp;";
                }
                //Change the variables
                var current = "<a class=\"" + this.linkClass + "\" href=\"JavaScript:" + tableVar + ".changePage()\" " 
                            + "onMouseOver=\"window.status='" + this.changePageDescription + "';return true;\" " 
                            + "onMouseOut=\"window.status='';return true;\">" + this.currentPage + "</a>";
                var total;
                if (this.allowChangePageSize) {
                     total = "<a class=\"" + this.linkClass + "\" href=\"JavaScript:" + tableVar + ".changePageSize()\" " 
                                + "onMouseOver=\"window.status='" + this.changePageSizeDescription + "';return true;\" " 
                                + "onMouseOut=\"window.status='';return true;\">" + maxPage + "</a>";
                } else {
                    total = maxPage;
                }
                output += "&nbsp;" + this.pageText.replace("${current}", current).replace("${total}", total) + "&nbsp;";
                //Put the forward navigation if not on last page
                if (this.currentPage < maxPage) {
                    output += "<a class=\"" + this.linkClass + "\" href=\"JavaScript:" + tableVar + ".setPage(" + (this.currentPage + 1) + ");\" " +
                        "onMouseOver=\"window.status='" + this.nextPageDescription + "';return true;\" " +
                        "onMouseOut=\"window.status='';return true;\">" + this.nextPageText + "</a>&nbsp;";
                    output += "<a class=\"" + this.linkClass + "\" href=\"JavaScript:" + tableVar + ".setPage(" + maxPage + ");\" " +
                        "onMouseOver=\"window.status='" + this.lastPageDescription + "';return true;\" " +
                        "onMouseOut=\"window.status='';return true;\">" + this.lastPageText + "</a>&nbsp;";
                }
                if (this.allowChangePaging) {
                    output += "&nbsp;&nbsp;<a class=\"" + this.linkClass + "\" href=\"JavaScript:" + tableVar + ".setUsePaging(false);\" " +
                            "onMouseOver=\"window.status='" + this.showAllDescription + "';return true;\" " +
                            "onMouseOut=\"window.status='';return true;\">" + this.showAllText + "</a>";
                }
            } else {
                //Showing all rows
                if (this.allowChangePaging) {
                    output += "<a class=\"" + this.linkClass + "\" href=\"JavaScript:" + tableVar + ".setUsePaging(true);\" " +
                            "onMouseOver=\"window.status='" + this.usePagingDescription + "';return true;\" " +
                            "onMouseOut=\"window.status='';return true;\">" + this.usePagingText +"</a>";
                }
            }
            //Put the table top link
            if (this.allowTopLink) {
                output += "&nbsp;&nbsp;&nbsp;<a class=\"" + this.linkClass + "\" href=\"#tableTop" + this.id + "\" " +
                            "onMouseOver=\"window.status='" + this.topDescription + "';return true;\" " +
                            "onMouseOut=\"window.status='';return true;\">" + this.topText + "</a>";
            }
        }
        output += "</td></tr>";
        return output;
    }
    
    /*
     * Returns the correct text and, if an image, adds its reference to the images map
     */
    var imgId = 0;
    this.handleLabel = function(label, images) {
        if (label.src) {
            var id = "img" + imgId;
            imgId++;
            if (isInstance(images, Map)) {
                images.put(id, label);
            }
            return "<img name='" + id + "' border='0'>";
        } else {
            return label;
        }
    }
    
    /*
     * Used on the Array.sort() method when sorting rows
     */
    this.sort = function(row1, row2) {
        var jst = row1.table;
        var column = jst.getColumnByIndex(jst.sortColumn);
        var type = column.type;
        var value1, values2;
        
        //Gets both values to test
        values1 = row1.values[jst.sortColumn];
        values2 = row2.values[jst.sortColumn];
        
        //Ensures both values are arrays
        if (!isInstance(values1, Array)) {
            values1 = [values1];
        }
        if (!isInstance(values2, Array)) {
            values2 = [values2];
        }

        //Gets the function that prepares the values
        var prepare;
        switch (column.type) {
            case JST_TYPE_NUMERIC:
            case JST_TYPE_CURRENCY:
                prepare = function(value) {
                    if (isNaN(value)) {
                        value = 0;
                    }
                    return value;
                }
                break;
            case JST_TYPE_DATE:
                prepare = function(value) {
                    return value.valueOf();
                }
                break;
            case JST_TYPE_BOOLEAN:
                prepare = function(value) {
                    return booleanValue(value) ? 1 : 0;
                }
                break;
            case JST_TYPE_STRING:
            default:
                prepare = function(value) {
                    return String(value).replace(/\<[^\>]*\>/g,"").toUpperCase();
                }
                break;
        }
        
        //Returns the comparision value
        var maxIndex = Math.max(values1.length, values2.length);
        var comp = 0;
        for (var i = 0; (comp == 0) && (i < maxIndex); i++) {
            //Get the values, checking null values
            var var1 = values1[i];
            if (var1 == null) {
                comp = -1;
            }
            var var2 = values2[i];
            if (var2 == null) {
                comp = 1;
            }
            //Compare the values
            if (comp == 0) {
                var1 = prepare(var1);
                var2 = prepare(var2);
                comp = (var1 == var2) ? 0 : (var1 > var2) ? 1 : -1;
            }
            //Check asc / desc sorting
            if (comp != 0) {
                comp *= (jst.ascSort ? 1 : -1);
            }
        }
        return comp;
    }
    
    /* Returns a String representation of this table */
    this.toString = function() {
        return "Table: Id = '" + this.id + "', Rows = " + this.getRowCount() + ", Columns = " + this.columns.length;
    };
}

///////////////////////////////////////////////////////////////////////////////
/*
 * This class represents a column.
 */
function Column(header, type, width, align, valign, possibleValues) {
    this.table = null;
    this.index = -1;
    this.header = header || "";
    this.type = type || JST_TYPE_STRING;
    this.width = width || "";
    this.align = align || JST_ALIGN_LEFT;
    this.valign = valign || JST_VALIGN_MIDDLE;
    this.possibleValues = possibleValues || null;
    this.visible = true;
    this.sortable = true;
    this.editable = false;
    this.editControl = new EditControl();
    this.parser = null;
    this.encodingParser = null;
    this.validateFunction = null;
    this.cellFunction = null;
    this.getMaskFunction = null;
    
    /*
     * Gets the parser for data entry
     */
    this.getParser = function(row) {
        //Checks for the given interface
        if (this.parser != null && isInstance(this.parser, Parser)) {
            return this.parser;
        }
        
        //Check if there's a editControl or row with multiple values
        var colValues = isInstance(this.possibleValues, Map) && this.possibleValues.size() > 0;
        var rowValues = false;
        try {
            rowValues = isInstance(row.possibleValues[this.index], Map) && (row.possibleValues[this.index].size() > 0);
        } catch (e) {
        } 
        //If has multi values, return a MapParser
        if (colValues || rowValues) {
            var values = new Map();
            //Put the EditControl values
            if (colValues) {
                var entries = this.possibleValues.getEntries();
                for (var i = 0; i < entries.length; i++) {
                    var entry = entries[i];
                    values.put(entry.key, entry.value);
                }
            }
            //Put the row values
            if (rowValues) {
                var entries = row.possibleValues[this.index].getEntries();
                for (var i = 0; i < entries.length; i++) {
                    var entry = entries[i];
                    values.put(entry.key, entry.value);
                }
            }
            //Build the parser
            return new MapParser(values, true);
        }
        
        //If the column does not have a specific parser, return the default from the table
        switch (this.type) {
            case JST_TYPE_STRING:
                return this.table.stringParser;
            case JST_TYPE_NUMERIC:
                return this.table.numberParser;
            case JST_TYPE_CURRENCY:
                return this.table.currencyParser;
            case JST_TYPE_DATE:
                return this.table.dateParser;
            case JST_TYPE_BOOLEAN:
                return this.table.booleanParser;
            default:
                return this.table.stringParser;
         }
    }
    
    /*
     * Gets the parser for encoding data
     */
    this.getEncodingParser = function() {
        //Checks for the given interface
        if (this.encodingParser != null && (this.encodingParser.parse) && (this.encodingParser.format)) {
            return this.encodingParser;
        }
        
        //If the column does not have a specific EncodingParser, return the default from the table
        switch (this.type) {
            case JST_TYPE_STRING:
                return this.table.stringEncodingParser;
            case JST_TYPE_NUMERIC:
                return this.table.numberEncodingParser;
            case JST_TYPE_CURRENCY:
                return this.table.currencyEncodingParser;
            case JST_TYPE_DATE:
                return this.table.dateEncodingParser;
            case JST_TYPE_BOOLEAN:
                return this.table.booleanEncodingParser;
            default:
                return this.table.stringEncodingParser;
         }
    }
    
    /* Returns a String representation of this column */
    this.toString = function() {
        return "Column: Index = " + this.index + ", Header = '" + this.header + "'";
    };
}

///////////////////////////////////////////////////////////////////////////////
/*
 * This class represents a row.
 */
function Row(id, values, possibleValues) {
    this.table = null;
    this.id = id;
    this.values = values || new Array();
    this.possibleValues = possibleValues || new Array();
    this.selectable = true;
    this.selected = false;
    this.editable = true;
    this.useCellFunction = true;
    
    /*
     * Encodes the data on the row, on the following format:
     * id<table.columnSeparator>value0<table.columnSeparator>value1<table.columnSeparator>...valueN
     * If a 'value' has multiple values (an Array), they are joined using the 
     * <table.valueSeparator>
     * If the columns parameter is set, it must be an Array of integers. Only those columns will be encoded. 
     * The row identifier is always encoded, unless the encodeRowId parameter is
     * set to false
     */
    this.encode = function(columns, encodeRowId) {
        encodeRowId = encodeRowId || true;
        
        //Get the encoded identifier
        var ret = encodeRowId ? escapeCharacters(id, this.table.columnSeparator + this.table.valueSeparator) : "";
        columns = columns || null;

        //Get the values
        if (this.values.length > 0) {
            if (encodeRowId) {
                ret += this.table.columnSeparator;
            }
            var tempVals = new Array();
            for (var i = 0; i < this.values.length; i++) {
                var cellValue = this.values[i];
                //Determine if the column will be encoded
                if ((columns != null) && (!inArray(i, columns))) {
                    continue;
                }
                //Get the column and it's encoding parser
                var column = this.table.getColumnByIndex(i);
                var encodingParser = column.getEncodingParser();
                if (!isInstance(cellValue, Array)) {
                    cellValue = [cellValue];
                }
                //Treat cellValue as an array of values
                values = new Array();
                //Parse the value and escape the values separator
                for (var j = 0; j < cellValue.length; j++) {
                    var value = cellValue[j];
                    if (value == null) {
                        values[j] = "";
                    } else {
                        var formattedValue = encodingParser.format(value);
                        values[j] = escapeCharacters(formattedValue, this.table.valueSeparator, false);
                    }
                }
                cellValue = values.join(this.table.valueSeparator);
                
                //Escape the column separators
                cellValue = escapeCharacters(cellValue, this.table.columnSeparator, true);
                
                //Store the current values
                tempVals[tempVals.length] = cellValue;
            }

            //Build the single String
            ret += tempVals.join(this.table.columnSeparator);
        }
        return ret;
    }
    
    /* Returns a String representation of this row */
    this.toString = function() {
        return "Row: Id = '" + this.id + "', Values = " + this.values;
    };    
}

///////////////////////////////////////////////////////////////////////////////
/*
 * This class represents an edit control.
 */
function EditControl(type, attributes, multiLine, name) {
    this.type = type || JST_CONTROL_TEXT;
    this.attributes = attributes || "";
    this.multiLine = booleanValue(multiLine);
    this.name = null;
    
    /* Returns a String representation of this edit control */
    this.toString = function() {
        return "Edit Control: Type = '" + this.type + "'";
    };
}
