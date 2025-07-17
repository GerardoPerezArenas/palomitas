package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject;
import org.apache.struts.action.ActionForm;

/**
 * @author david.caamano
 * @version 14/03/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 14/03/2013 * #53275 Edición inicial</li>
 * </ol> 
 */
public class DefinicionAgrupacionCampoForm extends ActionForm {
    
    private DefinicionAgrupacionCamposValueObject agrupacionVO = new DefinicionAgrupacionCamposValueObject();

    public DefinicionAgrupacionCamposValueObject getAgrupacionVO() {
        return agrupacionVO;
    }
    public void setAgrupacionVO(DefinicionAgrupacionCamposValueObject agrupacionVO) {
        this.agrupacionVO = agrupacionVO;
    }
    
    public String getCodAgrupacion(){
        return this.agrupacionVO.getCodAgrupacion();
    }
    public void setCodAgrupacion(String codAgrupacion){
        this.agrupacionVO.setCodAgrupacion(codAgrupacion);
    }
    
    public String getDescAgrupacion(){
        return this.agrupacionVO.getDescAgrupacion();
    }
    public void setDescAgrupacion(String descAgrupacion){
        this.agrupacionVO.setDescAgrupacion(descAgrupacion);
    }
    
    public Integer getOrdenAgrupacion(){
        return this.agrupacionVO.getOrdenAgrupacion();
    }
    public void setOrdenAgrupacion(Integer ordenAgrupacion){
        this.agrupacionVO.setOrdenAgrupacion(ordenAgrupacion);
    }
    
    public String getCodProcedimiento() {
        return this.agrupacionVO.getCodProcedimiento();
    }
    public void setCodProcedimiento(String codProcedimiento) {
        this.agrupacionVO.setCodProcedimiento(codProcedimiento);
    }
    
}//class

