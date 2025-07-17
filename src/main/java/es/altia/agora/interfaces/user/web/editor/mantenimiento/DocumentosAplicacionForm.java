package es.altia.agora.interfaces.user.web.editor.mantenimiento;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.*;
import org.apache.struts.upload.FormFile;


public class DocumentosAplicacionForm extends ActionForm{

   protected static Log log =
           LogFactory.getLog(DocumentosAplicacionForm.class.getName());
    
   private Vector listaAplicaciones;
   private Vector listaDocumentos;
   private Vector listaProcedimientos;
   private Vector listaTramites;
   private Vector listaActivos;
   private Vector listaEtiquetas;
   private String codAplicacion;
   private String codProcedimiento;
   private String codTramite;
   private String codActivo;
   private String resultadoEliminarDocumentos;
   private String codDocumento;
   private String opcion;
   private String nombreDocumento;
   private String docActivo;
   private String interesado;
   private String relacion;
   private FormFile ficheroWord;
   private String codTramiteSeleccionado;
   private String numeroExpediente;
   private String editorTexto;
      private String visibleExt;
   private boolean modificando;

    public Vector getListaActivos() {
        return listaActivos;
    }

    public void setListaActivos(Vector listaActivos) {
        this.listaActivos = listaActivos;
    }

    public String getCodActivo() {
        return codActivo;
    }

    public void setCodActivo(String codActivo) {
        this.codActivo = codActivo;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }
    
   public void setListaAplicaciones(Vector lista) { this.listaAplicaciones = lista; }
   public Vector getListaAplicaciones() { return listaAplicaciones; }

   
   public void setListaDocumentos(Vector lista) { this.listaDocumentos = lista; }
   public Vector getListaDocumentos() { return listaDocumentos; }

   public void setListaProcedimientos(Vector lista) { this.listaProcedimientos = lista; }
   public Vector getListaProcedimientos() { return listaProcedimientos; }
   
   public void setListaTramites(Vector lista) { this.listaTramites = lista; }
   public Vector getListaTramites() { return listaTramites; }
   
   public void setListaEtiquetas(Vector lista) { this.listaEtiquetas = lista; }
   public Vector getListaEtiquetas() { return listaEtiquetas; }

   public void setCodAplicacion(String codapli) { this.codAplicacion = codapli; }
   public String getCodAplicacion() { return this.codAplicacion; }
   
   public void setCodProcedimiento(String codproc) { this.codProcedimiento = codproc; }
   public String getCodProcedimiento() { return this.codProcedimiento; }
   
   public void setCodTramite(String codtramite) { this.codTramite = codtramite; }
   public String getCodTramite() { return this.codTramite; }

   public void setResultadoEliminarDocumentos(String resultado) {this.resultadoEliminarDocumentos = resultado;}
   public String getResultadoEliminarDocumentos() { return(this.resultadoEliminarDocumentos); }
   
   public void setCodDocumento(String codigoDocumento) {this.codDocumento = codigoDocumento;}
   public String getCodDocumento() { return(this.codDocumento); }

   public FormFile getFicheroWord() { return ficheroWord; }
   public void setFicheroWord(FormFile ficheroWord) { this.ficheroWord = ficheroWord; }
   
   public void setOpcion(String opcion) {this.opcion = opcion;}
   public String getOpcion() { return(this.opcion); }

   public void setNombreDocumento(String nombre) {this.nombreDocumento = nombre;}
   public String getNombreDocumento() { return(this.nombreDocumento); }
   
   public void setInteresado(String interesado) {this.interesado = interesado;}
   public String getInteresado() { return(this.interesado); }

   public void setRelacion(String relacion) {this.relacion = relacion;}
   public String getRelacion() { return(this.relacion); }

    public String getDocActivo() {
        return docActivo;
    }

    public void setDocActivo(String documentoActivo) {
        this.docActivo = documentoActivo;
    }

    public String getEditorTexto() {
        return editorTexto;
    }

    public void setEditorTexto(String editorTexto) {
        this.editorTexto = editorTexto;
    }

    public String getVisibleExt() {
        return visibleExt;
    }

    public void setVisibleExt(String visibleExt) {
        this.visibleExt = visibleExt;
    }

    public boolean isModificando() {
        return modificando;
    }

    public void setModificando(boolean modificando) {
        this.modificando = modificando;
    }
    
    
    
    

    public void limpiar(){
   	listaAplicaciones=null;
   	listaDocumentos=null;
   	listaProcedimientos=null;
   	listaTramites=null;
   	listaEtiquetas=null;
   	codAplicacion=null;
   	codProcedimiento=null;
   	codTramite=null;
   	resultadoEliminarDocumentos=null;
   	codDocumento=null;
   	opcion=null;
   	ficheroWord=null;
   	nombreDocumento=null;
   	editorTexto=null;
    docActivo = null;
    visibleExt=null;
   }
   
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
   	log.debug("validate");
   	ActionErrors errors = new ActionErrors();
   	return errors;
   }

  /* Función que procesa los errores de validación a formato struts */
   private ActionErrors validationException(ValidationException ve,ActionErrors errors){
   	Iterator iter = ve.getMessages().get();
   	while (iter.hasNext()) {
      Message message = (Message)iter.next();
      errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
   	}
   	return errors;
   }

    /**
     * @return the codTramiteVisible
     */
    public String getCodTramiteSeleccionado() {
        return codTramiteSeleccionado;
    }

    /**
     * @param codTramiteVisible the codTramiteVisible to set
     */
    public void setCodTramiteSeleccionado(String codTramiteSeleccionado) {
        this.codTramiteSeleccionado = codTramiteSeleccionado;
    }
}