package es.altia.util.struts;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Provides convenience methods to facilitate the implementation of concrete
 * <code>ActionForm</code>s for searchs.
 */
public abstract class DefaultSearchActionForm extends DefaultActionForm {
    /*_______Constants______________________________________________*/
	public static final String COUNT="count";
	public static final String STARTINDEX="startIndex";
	public static final String TOTALCOUNT="totalCount";
	public static final String POPUP="doPopUp";
	public static final String PRINTPREVIEW="doPrintPreview";
	public static final String PAGEBYPAGE="doPageByPage";
	public static final String SEARCH="doSearch";
	public static final String CANEDIT="canEdit";
    public static final String SORT="sort";
    public static final String SORTFIELD="sortField";

    public static final int SORT_NO=0;
    public static final int SORT_ASC=1;
    public static final int SORT_DESC=2;

    /*_______Attributes_____________________________________________*/
	private Collection results;
	private int pStartIndex;
	private int pCount;
	private long pTotalCount;
	private boolean pDoPopUp;
	private boolean pDoPrintPreview;
	private boolean pDoPageByPage; 
	private boolean pDoSearch; 
	private boolean pCanEdit;
    private int pSort;
    private String pSortField;

    /*_______Operations_____________________________________________*/
	public DefaultSearchActionForm() {
        	reset();
	}//constructor

	public void reset(ActionMapping mapping, HttpServletRequest request) {
        	reset();
	}//reset

	/**
	 * Initializes custom properties
	**/
	protected abstract void doReset();

	public int getCount() {
		return pCount;
	}//getCount

	public void setCount(int pCount) {
		this.pCount = pCount;
	}//setCount

	public int getStartIndex() {
		return pStartIndex;
	}//getStartIndex

	public void setStartIndex(int pStartIndex) {
		this.pStartIndex = pStartIndex;
	}//setStartIndex

	public long getTotalCount() {
		return pTotalCount;
	}//getTotalCount

	public void setTotalCount(long pTotalCount) {
		this.pTotalCount = pTotalCount;
	}//setTotalCount

	public Collection getResults() {
		return results;
	}//getResults

	public void setResults(Collection results) {
		this.results = results;
	}//setResults

	public boolean getDoPageByPage() {
		return pDoPageByPage;
	}//getDoPageByPage

	public void setDoPageByPage(boolean pDoPageByPage) {
		this.pDoPageByPage = pDoPageByPage;
	}//setDoPageByPage

	public boolean getDoPrintPreview() {
		return pDoPrintPreview;
	}//getDoPrintPreview

	public void setDoPrintPreview(boolean pDoPrintPreview) {
		this.pDoPrintPreview = pDoPrintPreview;
	}//setDoPrintPreview

	public boolean getDoSearch() {
		return pDoSearch;
	}//getDoSearch

	public void setDoSearch(boolean pDoSearch) {
		this.pDoSearch = pDoSearch;
	}//setDoSearch

	public boolean getDoPopUp() {
		return pDoPopUp;
	}//getDoPopUp

	public void setDoPopUp(boolean pDoPopUp) {
		this.pDoPopUp = pDoPopUp;
	}//setDoPopUp

	public boolean getCanEdit() {
		return pCanEdit;
	}//getCanEdit

	public void setCanEdit(boolean pCanEdit) {
		this.pCanEdit = pCanEdit;
	}//setCanEdit

    public int getSort() {
        return pSort;
    }

    public void setSort(int sort) {
        pSort = sort;
    }

    public String getSortField() {
        return pSortField;
    }

    public void setSortField(String sortField) {
        pSortField = sortField;
    }


	/**
	 * Initializes properties
	 **/
	public void reset() {
		results = null;
		pStartIndex = 0;
		pCount = 5;
		pTotalCount = 0;
		
		pDoPopUp = false;
		pDoPrintPreview = false;
		pDoPageByPage = true;
		pDoSearch = false;
		pCanEdit = true;

        pSort = SORT_NO;
        pSortField = null;

		doReset();
	}//reset

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("[DefaultSearchActionForm:");
        buf.append("results=").append(results);
        buf.append("|pStartIndex=").append(pStartIndex);
        buf.append("|pCount=").append(pCount);
        buf.append("|pTotalCount=").append(pTotalCount);
        buf.append("|pDoPopUp=").append(pDoPopUp);
        buf.append("|pDoPrintPreview=").append(pDoPrintPreview);
        buf.append("|pDoPageByPage=").append(pDoPageByPage);
        buf.append("|pDoSearch=").append(pDoSearch);
        buf.append("|pCanEdit=").append(pCanEdit);
        buf.append("|pSort=").append(pSort);
        buf.append("|pSortField=").append(pSortField);
        buf.append(']');
        return buf.toString();
    }


}//class
