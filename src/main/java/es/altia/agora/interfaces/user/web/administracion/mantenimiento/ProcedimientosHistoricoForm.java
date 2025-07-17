package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

import org.apache.struts.action.ActionForm;
import es.altia.flexia.historico.expediente.vo.ProcedimientoHistoricoVO;
import java.util.ArrayList;

public class ProcedimientosHistoricoForm extends ActionForm  {
    private static final long serialVersionUID = 1L;
    
    private ArrayList<ProcedimientoHistoricoVO> procedimientos = new ArrayList<ProcedimientoHistoricoVO>();
    private ProcedimientoHistoricoVO procedimientoEditar = new ProcedimientoHistoricoVO();
    private String resultado;
            
    public ArrayList<ProcedimientoHistoricoVO> getProcedimientos() {
        return procedimientos;
    }

    public void setProcedimientos(ArrayList<ProcedimientoHistoricoVO> procedimientos) {
        this.procedimientos = procedimientos;
    }

    public ProcedimientoHistoricoVO getProcedimientoEditar() {
        return procedimientoEditar;
    }

    public void setProcedimientoEditar(ProcedimientoHistoricoVO procedimientoEditar) {
        this.procedimientoEditar = procedimientoEditar;
    }
        
    public String getCodProcedimientoEditar() {
        return procedimientoEditar.getCodProcedimiento();
    }

    public void setCodProcedimientoEditar(String codProcedimientoEditar) {
        this.procedimientoEditar.setCodProcedimiento(codProcedimientoEditar);
    }

    public int getMesesEditar() {
        return procedimientoEditar.getMeses();
    }

    public void setMesesEditar(int mesesEditar) {
        this.procedimientoEditar.setMeses(mesesEditar);
    }

    public int getCodOrganizacionEditar() {
        return procedimientoEditar.getCodOrganizacion();
    }

    public void setCodOrganizacionEditar(int codOrganizacionEditar) {
        this.procedimientoEditar.setCodOrganizacion(codOrganizacionEditar);
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
   
}