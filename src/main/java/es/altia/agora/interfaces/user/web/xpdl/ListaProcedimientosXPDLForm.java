/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.interfaces.user.web.xpdl;

import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import java.util.Vector;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author ricardo.iglesias
 */
public class ListaProcedimientosXPDLForm extends ActionForm{
    
    private Vector<DefinicionProcedimientosValueObject> listaProcedimientos;
    
    public Vector<DefinicionProcedimientosValueObject> getListaProcedimientos (){
    
        return listaProcedimientos;
    }
    
    public void setListaProcedimientos(Vector<DefinicionProcedimientosValueObject> listaProcs){
        this.listaProcedimientos = listaProcs;
    }
}
