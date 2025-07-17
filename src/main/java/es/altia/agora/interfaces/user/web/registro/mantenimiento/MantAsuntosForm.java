package es.altia.agora.interfaces.user.web.registro.mantenimiento;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.registro.mantenimiento.MantAsuntosValueObject;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.registro.mantenimiento.MantClasifAsuntosValueObject;
import es.altia.agora.business.util.ElementoListaValueObject;
import java.util.ArrayList;

public class MantAsuntosForm extends ActionForm {
    
    public static final long serialVersionUID=0;
    
    private Vector<MantAsuntosValueObject> asuntos;
    private ArrayList<MantClasifAsuntosValueObject> clasificaciones;
    private Vector<UORDTO> uors;
    private Vector<UORDTO> uorsDeRegistro;
    private Vector<DefinicionProcedimientosValueObject> procedimientos;
    private MantAsuntosValueObject asuntoVO = new MantAsuntosValueObject();    
    private String opcion;
    private String apl;
    private String unidadRegistroActual;
    // Estas son para tener una copia de los valores anteriores de la clave al modificar
    private String unidadRegistroAnterior;
    private String tipoRegistroAnterior;
    private String codigoAnterior;
    //Si estamos en la modificacion de un asunto, este es el codigo de Clasificacion de asunto del asunto en cuesti?n.
    private Integer codigoClasificacion;
    
    //indica si el asunto permite seleccionar un interesado con tipo documento 'sin documento'
    private String tipoDocObligatorio;
    
    
     public ArrayList<MantClasifAsuntosValueObject> getClasificaciones() {
        return clasificaciones;
    }

    public void setClasificaciones(ArrayList<MantClasifAsuntosValueObject> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }


    public String getUnidadRegistroActual() {
        return unidadRegistroActual;
    }
    
    public void setUnidadRegistroActual(String unidadRegistroActual) {
        this.unidadRegistroActual = unidadRegistroActual;
    }
    
    public String getApl() {
        return apl;
    }

    public void setApl(String apl) {
        this.apl = apl;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }        

    public String getTxtListaDocs() {
        return null;
    }

    /**
     * Construye un vector de elementoListaValueObjects a partir de la lista de documentos
     * y lo asigna al VO.
     */
    public void setTxtListaDocs(String txtListaDocs) {
        Vector<ElementoListaValueObject> listaDocs = new Vector<ElementoListaValueObject>();
        if (!txtListaDocs.equals("")) {
            String[] lista = txtListaDocs.split("��");        
            for (int i=0; i<lista.length; i++) {
                ElementoListaValueObject doc = new ElementoListaValueObject();
                doc.setDescripcion(lista[i]);
                listaDocs.add(doc);    
            }            
        }
        this.setListaDocs(listaDocs);
        
    }

    public Vector<MantAsuntosValueObject> getAsuntos() {
        return asuntos;
    }

    public void setAsuntos(Vector<MantAsuntosValueObject> asuntos) {
        this.asuntos = asuntos;
    }
              
    public Vector<UORDTO> getUors() {
        return uors;
    }

    public void setUors(Vector<UORDTO> uors) {
        this.uors = uors;
    }
    
    public Vector<UORDTO> getUorsDeRegistro() {
        return uorsDeRegistro;
    }

    public void setUorsDeRegistro(Vector<UORDTO> uorsDeRegistro) {
        this.uorsDeRegistro = uorsDeRegistro;
    }
    
    public Vector<DefinicionProcedimientosValueObject> getProcedimientos() {
        return procedimientos;
    }

    public void setProcedimientos(
            Vector<DefinicionProcedimientosValueObject> procedimientos) {
        this.procedimientos = procedimientos;
    }

    public MantAsuntosValueObject getAsuntoVO() {
        return asuntoVO;
    }

    public void setAsuntoVO(MantAsuntosValueObject asuntoVO) {
        this.asuntoVO = asuntoVO;
    }

    public String getCodigo() {
        return asuntoVO.getCodigo();
    }

    public void setCodigo(String codigo) {
        asuntoVO.setCodigo(codigo);
    }

    public String getUnidadRegistro() {
        return asuntoVO.getUnidadRegistro();
    }

    public void setUnidadRegistro(String unidadRegistro) {
        asuntoVO.setUnidadRegistro(unidadRegistro);
    }

    public String getTipoRegistro() {        
        return asuntoVO.getTipoRegistro();
    }

    public void setTipoRegistro(String tipoRegistro) {
        asuntoVO.setTipoRegistro(tipoRegistro);
    }    
    
    public String getDescripcion() {
        return asuntoVO.getDescripcion();
    }
    
    public void setDescripcion(String descripcion) {
        asuntoVO.setDescripcion(descripcion);
    }
    
    public String getExtracto() {
        return asuntoVO.getExtracto();
    }
    
    public void setExtracto(String extracto) {
        asuntoVO.setExtracto(extracto);               
    }
    
    public String getCodProcedimiento() {
        return asuntoVO.getProcedimiento();
    }
    
    public void setCodProcedimiento(String procedimiento) {
        asuntoVO.setProcedimiento(procedimiento);
    }
    
     public String getDesProcedimiento(){	
        return asuntoVO.getDesProcedimiento();	
    }	
    	
    public void setDesProcedimiento(String desProcedimiento){	
        asuntoVO.setDesProcedimiento(desProcedimiento);	
    }	
    
    
    public String getMunProc() {
        return asuntoVO.getMunProc();        
    }
    
    public void setMunProc(String munProc) {
        asuntoVO.setMunProc(munProc);
    }
    
    public String getUnidadTram() {
        return asuntoVO.getUnidadTram();                
    }
    
    public void setUnidadTram(String unidadTram) {
        asuntoVO.setUnidadTram(unidadTram);
    }
     
    public Vector<ElementoListaValueObject> getListaDocs() {
        return asuntoVO.getListaDocs();
    }

    public void setListaDocs(Vector<ElementoListaValueObject> listaDocs) {
        asuntoVO.setListaDocs(listaDocs);
    }      
    
    public String getUnidadRegistroAnterior() {
        return unidadRegistroAnterior;
    }

    public void setUnidadRegistroAnterior(String unidadRegistroAnterior) {
        this.unidadRegistroAnterior = unidadRegistroAnterior;
    }

    public String getTipoRegistroAnterior() {
        return tipoRegistroAnterior;
    }

    public void setTipoRegistroAnterior(String tipoRegistroAnterior) {
        this.tipoRegistroAnterior = tipoRegistroAnterior;
    }

    public String getCodigoAnterior() {
        return codigoAnterior;
    }

    public void setCodigoAnterior(String codigoAnterior) {
        this.codigoAnterior = codigoAnterior;
    }   

    public boolean isEnviarCorreo() {
        return asuntoVO.isEnviarCorreo();
    }

    public void setEnviarCorreo(boolean enviarCorreo) {
        asuntoVO.setEnviarCorreo(enviarCorreo);
    }

    public String getTxtListaUorsCorreo() {
        return asuntoVO.getTxtListaUorsCorreo();
    }

    public void setTxtListaUorsCorreo(String txtListaUorsCorreo) {
        asuntoVO.setTxtListaUorsCorreo(txtListaUorsCorreo);
    }
    
      public boolean isBloquearDestino(){	
        return asuntoVO.isBloquearDestino();	
    }	
    public void setBloquearDestino(boolean bloquearDestino){	
        asuntoVO.setBloquearDestino(bloquearDestino);	
    }
	public boolean isBloquearProcedimiento() {
		return asuntoVO.isBloquearProcedimiento();
	}

	public void setBloquearProcedimiento(boolean bloquearProcedimiento) {
		asuntoVO.setBloquearProcedimiento(bloquearProcedimiento);
	}

    public boolean isBloqueoPAC() {
        return asuntoVO.isBloqueoPAC();
    }

    public void setBloqueoPAC(boolean bloqueoPAC) {
        asuntoVO.setBloqueoPAC(bloqueoPAC);
    }
    // Para cargar la lista en la jsp, se tranforma el String separado por comas en Vector
    public Vector<String> getListaUorsCorreo() { 
        Vector<String> listaUorsCorreo = new Vector<String>();
        String[] listaTxt = asuntoVO.getTxtListaUorsCorreo().split(",");
        
        // Si la lista estaba vac�a, tendremos una array con un elemento: ""
        // en este caso devolvemos el vector vac�o.
        if (listaTxt.length == 1) {
            if (listaTxt[0].equals("")) return listaUorsCorreo;
        }
        
        // Se construye el vector
        for (int i=0; i<listaTxt.length; i++) {
            listaUorsCorreo.add(listaTxt[i]);
        }        
        return listaUorsCorreo;
    }
    
     public Integer getCodigoClasificacion() {
        return codigoClasificacion;
    }

    public void setCodigoClasificacion(Integer codigoClasificacion) {
        this.codigoClasificacion = codigoClasificacion;
    }
    
    
    // Necesario para que Struts reconozca cuando el checkbox no esta seleccionado.
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setEnviarCorreo(false);
    }
    
    // Los metodos siguientes existen tan solo para poder usar html:text con 
    // los atributos a los que se refieren y no hacen nada en realidad.
    public void   setCodUnidadRegistro(String str) {}    
    public String getCodUnidadRegistro() {return "";}    
    public void   setDescUnidadRegistro(String str) {}    
    public String getDescUnidadRegistro() {return "";}    
    public void   setCodUnidadTram(String str) {}    
    public String getCodUnidadTram() {return "";}    
    public void   setDescUnidadTram(String str) {}    
    public String getDescUnidadTram() {return "";}
    public void   setDescProcedimiento(String str) {}    
    public String getDescProcedimiento() {return "";} 
    
     //TODO:Ver si es esto (Para el combo de clasificacion de asuntos)
    public void setCodClasifAsunto(String str){}
    public String getCodClasifAsunto(){return "";}
    public void  setDescClasifAsunto(String str) {}    
    public String getDescClasifAsunto() {return "";}    

    /**
     * @return the tipoDocObligatorio
     */
    public String getTipoDocObligatorio() {
        return tipoDocObligatorio;
    }

    /**
     * @param tipoDocObligatorio the tipoDocObligatorio to set
     */
    public void setTipoDocObligatorio(String tipoDocObligatorio) {
        this.tipoDocObligatorio = tipoDocObligatorio;
    }
}
