/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas.delegacionfirma;

import es.altia.util.collections.CollectionsOperations;
import es.altia.util.struts.DefaultActionForm;

import java.util.List;

/**
 * @version $\Date$ $\Revision$
 */
public class PopUpSeleccionUsuarioForm extends DefaultActionForm {
    /*_______Constants______________________________________________*/

    /*_______Atributes______________________________________________*/
    protected List pListaUsuarios;

    /*_______Operations_____________________________________________*/
    protected void reset() {
        pListaUsuarios = CollectionsOperations.getEmptyInmutableList();
    }//reset

    public String toString() {
        final StringBuffer buff = new StringBuffer("[PopUpSeleccionUsuarioForm:");
        buff.append("ListaUsuarios.size()="+pListaUsuarios.size());
        buff.append("|]");
        return buff.toString();
    }//toString

    public List getListaUsuarios() {
        return pListaUsuarios;
    }

    public void setListaUsuarios(List listaUsuarios) {
        pListaUsuarios = listaUsuarios;
    }

    public int getTotalCount(){
        return pListaUsuarios.size();
    }

}//class
/*______________________________EOF_________________________________*/