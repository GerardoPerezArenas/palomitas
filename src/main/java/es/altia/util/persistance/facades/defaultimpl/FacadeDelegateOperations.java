/*______________________________BOF_________________________________*/
package es.altia.util.persistance.facades.defaultimpl;

import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.facades.BusinessRequestManager;
import es.altia.util.persistance.facades.defaultimpl.requests.*;
import es.altia.util.persistance.PersistentObject;
import es.altia.util.persistance.PrimaryKey;
import es.altia.util.persistance.SQLDAOFactory;
import es.altia.util.persistance.SQLDAO;
import es.altia.util.persistance.daocommands.OrderCriteria;
import es.altia.util.persistance.exceptions.InstanceNotFoundException;
import es.altia.util.persistance.exceptions.DuplicateInstanceException;
import es.altia.util.persistance.exceptions.StaleUpdateException;
import es.altia.util.persistance.exceptions.IntegrityViolationAttemptedException;
import es.altia.util.persistance.searchcriterias.SearchCriteria;
import es.altia.util.jdbc.DataSourceLocator;

import java.util.List;

/**
 * @author
 * @version $\Date$ $\Revision$
 */
public final class FacadeDelegateOperations {
    /*_______Operations_____________________________________________*/
    private FacadeDelegateOperations(){
    }//constructor

    public static final SQLDAO getDAO(String dsKey, Class voClass) throws InternalErrorException {
        return SQLDAOFactory.getInstance(DataSourceLocator.getDBKeyForDataSource(dsKey)).getDAOInstance(voClass);
    }//getDAO

    public static final PersistentObject simpleCreate(String dsKey, Class voClass, PersistentObject vo)
            throws InternalErrorException, DuplicateInstanceException {
        try {
            final DefaultCreateRequest br;
            if (dsKey!=null)
                br = new DefaultCreateRequest(dsKey,voClass,vo);
            else
                br = new DefaultCreateRequest(voClass,vo);
            BusinessRequestManager.getInstance().handleRequest(br);
            return br.getResult();
        } catch (DuplicateInstanceException e) {
            throw e;
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }//try-catch
    }//create

    public static final PersistentObject simpleCreateOrUpdate(String dsKey, Class voClass, PersistentObject vo)
            throws InternalErrorException {
        try {
            final DefaultCreateOrUpdateRequest br;
            if (dsKey!=null)
                br = new DefaultCreateOrUpdateRequest(dsKey,voClass,vo);
            else
                br = new DefaultCreateOrUpdateRequest(voClass,vo);
            BusinessRequestManager.getInstance().handleRequest(br);
            return br.getResult();
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }//try-catch
    }//createOrUpdate

    public static final PersistentObject simpleRetrieve(String dsKey, Class voClass, PrimaryKey pk)
            throws InternalErrorException, InstanceNotFoundException {
        try {
            final DefaultRetrieveRequest br;
            if (dsKey!=null)
                br = new DefaultRetrieveRequest(dsKey,voClass,pk);
            else
                br = new DefaultRetrieveRequest(voClass,pk);
            BusinessRequestManager.getInstance().handleRequest(br);
            return br.getResult();
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }//try-catch
    }//retrieve

    public static final void simpleUpdate(String dsKey, Class voClass, PersistentObject vo)
            throws InternalErrorException, InstanceNotFoundException, StaleUpdateException {
        try {
            final DefaultUpdateRequest br;
            if (dsKey!=null)
                br = new DefaultUpdateRequest(dsKey,voClass,vo);
            else
                br = new DefaultUpdateRequest(voClass,vo);
            BusinessRequestManager.getInstance().handleRequest(br);
            br.getResult();
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (StaleUpdateException e) {
            throw e;
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }//try-catch
    }//update

    public static final void simpleDelete(String dsKey, Class voClass, PrimaryKey pk)
            throws InternalErrorException, InstanceNotFoundException, IntegrityViolationAttemptedException {
        try {
            final DefaultRemoveRequest br;
            if (dsKey!=null)
                br = new DefaultRemoveRequest(dsKey,voClass,pk);
            else
                br = new DefaultRemoveRequest(voClass,pk);
            BusinessRequestManager.getInstance().handleRequest(br);
            br.getResult();
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (IntegrityViolationAttemptedException e) {
            throw e;
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }//try-catch
    }//delete

    public static final List simpleFind(String dsKey, Class voClass, SearchCriteria sc, OrderCriteria oc, int startIndex, int count)
            throws InternalErrorException {
        try {
            final DefaultFindRequest br;
            if (dsKey!=null)
                br = new DefaultFindRequest(dsKey,voClass,sc, oc, startIndex, count);
            else
                br = new DefaultFindRequest(voClass,sc, oc, startIndex, count);
            BusinessRequestManager.getInstance().handleRequest(br);
            return br.getResult();
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }//try-catch
    }//find

    public static final List simpleFindCustom(String dsKey, Class mainVOClass, Class customVOClass, SearchCriteria sc, OrderCriteria oc, int startIndex, int count)
            throws InternalErrorException {
        try {
            final DefaultFindCustomRequest br;
            if (dsKey!=null)
                br = new DefaultFindCustomRequest(dsKey, mainVOClass, customVOClass, sc, oc, startIndex, count);
            else
                br = new DefaultFindCustomRequest(mainVOClass, customVOClass, sc, oc, startIndex, count);
            BusinessRequestManager.getInstance().handleRequest(br);
            return br.getResult();
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }//try-catch
    }//find

    public static final long simpleCount(String dsKey, Class voClass, SearchCriteria sc)
            throws InternalErrorException {
        try {
            final DefaultCountRequest br;
            if (dsKey!=null)
                br = new DefaultCountRequest(dsKey,voClass,sc);
            else
                br = new DefaultCountRequest(voClass,sc);
            BusinessRequestManager.getInstance().handleRequest(br);
            return br.getResult();
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }//try-catch
    }//count
}//class
/*______________________________EOF_________________________________*/
