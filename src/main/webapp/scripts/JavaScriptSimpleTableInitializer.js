
function setUpTableClientSide(theColumns, theRows, theContainerName, theSelectionType, theWidth, thePageSize) {
        var table;
        //Instantiate the table		        
    	table = new JavaScripTable("jstTable"+theContainerName+"Id", getObject(theContainerName));
        
        //Set the table properties
        table.selectionType = theSelectionType;
        table.width = theWidth;
        table.headerText = "";
        table.footerText = "";
        table.usePaging = true;
        table.pageSize = thePageSize;
        table.allowChangePaging = false;
        table.allowTopLink = false;
        table.rowFunction = "rowFunction";
         table.rowFunctionClick = "rowFunctionClick";
        //Add the columns
        var col;
        for (var i = 0; i < theColumns.length; i++) {
            col = table.addColumn(theColumns[i]);
            col.editable=false;
        }
        
        //Add the rows
        for (var i = 0; i < theRows.length; i++) {
            table.addRow(new Row(i, theRows[i]));
        }

        //Render the table
        table.render();
        return table;
}

function setUpTableClientSideClass(theColumns, theRows, theRowsClass, theContainerName, theSelectionType, theWidth, thePageSize) {
        var table;
        //Instantiate the table		        
    	table = new JavaScripTable("jstTable"+theContainerName+"Id", getObject(theContainerName));
        
        //Set the table properties
        table.selectionType = theSelectionType;
        table.width = theWidth;
        table.headerText = "";
        table.footerText = "";
        table.usePaging = true;
        table.pageSize = thePageSize;
        table.allowChangePaging = false;
        table.allowTopLink = false;
        table.rowFunction = "rowFunction";
         table.rowFunctionClick = " ";
         table.rowsClass = theRowsClass;
        //Add the columns
        var col;
        for (var i = 0; i < theColumns.length; i++) {
            col = table.addColumn(theColumns[i]);
            col.editable=false;
        }
        
        //Add the rows
        for (var i = 0; i < theRows.length; i++) {
            table.addRow(new Row(i, theRows[i]));
        }

        //Render the table
        table.render();
        return table;
}



function setUpTableClientSideClick(theColumns, theRows, theContainerName, theSelectionType, theWidth, thePageSize,click) {
        var table;
        //Instantiate the table		        
    	table = new JavaScripTable("jstTable"+theContainerName+"Id", getObject(theContainerName));
        
        //Set the table properties
        
        table.selectionType = theSelectionType;
        table.width = theWidth;
        table.headerText = "";
        table.footerText = "";
        table.usePaging = true;
        table.pageSize = thePageSize;
        table.allowChangePaging = false;
        table.allowTopLink = false;
        table.rowFunction = "rowFunction";
         table.rowFunctionClick = click;
        //Add the columns
        var col;
        for (var i = 0; i < theColumns.length; i++) {
            col = table.addColumn(theColumns[i]);
            col.editable=false;
        }
        
        //Add the rows
        for (var i = 0; i < theRows.length; i++) {
            table.addRow(new Row(i, theRows[i]));
        }

        //Render the table
        table.render();
        return table;
}
    

function setUpTable(theSortableColumnsIds, theColumns, theRows, theContainerName, theSelectionType, theWidth, theStartIndex, theCount, theTotalCount, theSortProperty, theSortAscending, theUpdateCallback) {
        var table;
        //Instantiate the table		        
    	table = new JavaScripTable("jstTable"+theContainerName+"Id", getObject(theContainerName));
        
        //Set the table properties
        table.operationMode = JST_SERVER_SIDE;
        table.updateTableFunction = theUpdateCallback;
        table.selectionType = theSelectionType;
        table.width = theWidth;
        table.headerText = "";
        table.footerText = "";
        table.useRowHighliting = false;
        table.usePaging = true;
        table.allowChangePaging = false;
        table.allowTopLink = false;
        table.pageSize = theCount;
        table.rowCount = theTotalCount;
        table.currentPage = ( (theTotalCount>theCount)?( (theStartIndex / theCount) + 1):(1) );

        //table.rowFunction = "rowClick";
        //table.navigation = JST_NAV_NONE;

        //Add the columns
        var col;
        for (var i = 0; i < theColumns.length; i++) {
            col = table.addColumn(theColumns[i]);
            col.sortable=( theSortableColumnsIds[i] != null );
            col.editable=false;
        }
        
        //Add the rows
        for (var i = 0; i < theRows.length; i++) {
            table.addRow(new Row(i, theRows[i]));
        }

        //Set ordering state
        if (!isEmpty(theSortProperty)) {
            for (i = 0; i < theSortableColumnsIds.length; i++) {
                if (theSortableColumnsIds[i] == theSortProperty) {
                    table.sortColumn = i;
                    table.ascSort = theSortAscending;
                    break;
                }
            }
        }

        //Render the table
        table.render();
        return table;
}


function getSingleSelectedKey(tbl,keys) {
    var result = null;
    if ( (tbl) && (keys) ) {
        var theRows = tbl.getSelectedRows();
        if ( (theRows!=null) && (theRows.length>=1) ) {
            var selectedId = theRows[0].id;
            result = keys[selectedId];
        }
    }
    return result;
}
