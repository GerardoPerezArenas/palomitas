<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>


        <script language="JavaScript" type="text/javascript">
        <!--
        var JST_IMAGES_DIR='<%=request.getContextPath()%>/images/grid/';
        var JST_DEFAULT_SORT_MESSAGE = "<fmt:message key='JSTableLabels.sortMessage'/>";
        var JST_DEFAULT_SINGLE_ROW_TEXT = "<fmt:message key='JSTableLabels.singleRowText'/>";
        var JST_DEFAULT_MULTIPLE_ROW_TEXT = "<fmt:message key='JSTableLabels.multipleRowText'/>";
        var JST_DEFAULT_PAGE_TEXT = "<fmt:message key='JSTableLabels.pageText'/>";
        var JST_DEFAULT_FIRST_PAGE_DESCRIPTION = "<fmt:message key='JSTableLabels.firstPageDescription'/>";
        var JST_DEFAULT_PREVIOUS_PAGE_DESCRIPTION = "<fmt:message key='JSTableLabels.previousPageDescription'/>";
        var JST_DEFAULT_NEXT_PAGE_DESCRIPTION = "<fmt:message key='JSTableLabels.nextPageDescription'/>";
        var JST_DEFAULT_LAST_PAGE_DESCRIPTION = "<fmt:message key='JSTableLabels.lastPageDescription'/>";
        var JST_DEFAULT_SHOW_ALL_DESCRIPTION = "<fmt:message key='JSTableLabels.showAllDescription'/>";
        var JST_DEFAULT_SHOW_ALL_TEXT = "<fmt:message key='JSTableLabels.showAllText'/>";
        var JST_DEFAULT_USE_PAGING_DESCRIPTION = "<fmt:message key='JSTableLabels.usePagingDescription'/>";
        var JST_DEFAULT_USE_PAGING_TEXT = "<fmt:message key='JSTableLabels.usePagingText'/>";
        var JST_DEFAULT_TOP_DESCRIPTION = "<fmt:message key='JSTableLabels.topDescription'/>";
        var JST_DEFAULT_TOP_TEXT = "<fmt:message key='JSTableLabels.topText'/>";
        var JST_DEFAULT_CHANGE_PAGE_DESCRIPTION = "<fmt:message key='JSTableLabels.changePageDescription'/>";
        var JST_DEFAULT_CHANGE_PAGE_PROMPT = "<fmt:message key='JSTableLabels.changePagePrompt'/>";
        var JST_DEFAULT_CHANGE_PAGE_SIZE_DESCRIPTION = "<fmt:message key='JSTableLabels.changePageSizeDescription'/>";
        var JST_DEFAULT_CHANGE_PAGE_SIZE_PROMPT = "<fmt:message key='JSTableLabels.changePageSizePrompt'/>";
        var JST_DEFAULT_FIRST_PAGE_TEXT = "<img src=\""+( (JST_IMAGES_DIR!=null)?(JST_IMAGES_DIR):("") ) + "first.gif\" border=\"0\">";
        var JST_DEFAULT_PREVIOUS_PAGE_TEXT = "<img src=\""+( (JST_IMAGES_DIR!=null)?(JST_IMAGES_DIR):("") ) + "prev.gif\" border=\"0\">";
        var JST_DEFAULT_NEXT_PAGE_TEXT = "<img src=\""+( (JST_IMAGES_DIR!=null)?(JST_IMAGES_DIR):("") ) + "next.gif\" border=\"0\">";
        var JST_DEFAULT_LAST_PAGE_TEXT = "<img src=\""+( (JST_IMAGES_DIR!=null)?(JST_IMAGES_DIR):("") ) + "last.gif\" border=\"0\">";
        //-->
        </script>
        <link rel="stylesheet" href="<c:url value='/css/table.css'/>" type="text/css">
        <script src="<c:url value='/scripts/JavaScriptUtil.js'/>"/></script>
        <script src="<c:url value='/scripts/Parsers.js'/>"/></script>
        <script src="<c:url value='/scripts/JavaScripTable.js'/>"/></script>
        <script src="<c:url value='/scripts/JavaScriptSimpleTableInitializer.js'/>"/></script>
