/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.xpdl;


import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.ExistenciaUorImportacionVO;
import es.altia.util.StringUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;


/**
 *
 * @author Ricardo Iglesias
 */
public class XPDLUtil {

    
    private static Namespace XMLNS=Namespace.getNamespace("","http://www.wfmc.org/2008/XPDL2.1");
    private static Logger log = Logger.getLogger(XPDLUtil.class);
     
    
   public static  Element fechaToXML(){
		
        Calendar fecha = Calendar.getInstance();
        fecha.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dateFormat2 =new SimpleDateFormat("HH:mm:ss");
        Element fechaElement = new Element("Created",XMLNS);
        fechaElement.setText(dateFormat.format(fecha.getTime())+" "+dateFormat2.format(fecha.getTime()));
        return fechaElement;
			
    }
       
    public static Collection rellenarTramite(DefinicionTramitesValueObject dtVO){
        Collection resultado = new ArrayList();
        String obligatorio = dtVO.getObligatorio();
        String obligatorioDesf = dtVO.getObligatorioDesf();
        String tipoCondicion = dtVO.getTipoCondicion();
        String tipoFavorableSI = (dtVO.getTipoFavorableSI()==null || dtVO.getTipoFavorableSI().length() <= 0)?
                "":dtVO.getTipoFavorableSI().substring(0, 1);
        String tipoFavorableNO = (dtVO.getTipoFavorableNO()==null || dtVO.getTipoFavorableNO().length() <= 0)?
                "":dtVO.getTipoFavorableNO().substring(0, 1);
        
         if(obligatorio==null) obligatorio="";
         Element extendedAttributeElement = new Element ("ExtendedAttribute",XMLNS);
         //extendedAttributeElement.setAttribute("Name","SAL_OBL");
         extendedAttributeElement.setAttribute("Name","caso_obligatorio_favorable");
         extendedAttributeElement.setAttribute("Value",obligatorio);
         resultado.add(extendedAttributeElement);
        
         String sObligatorioDesfavorable = "";
         if (obligatorioDesf!=null && !"".equals(obligatorioDesf))
             sObligatorioDesfavorable = obligatorioDesf;
         Element extendedAttributeElement2 = new Element ("ExtendedAttribute",XMLNS);
         //extendedAttributeElement2.setAttribute("Name","SAL_OBLD");
         extendedAttributeElement2.setAttribute("Name","caso_obligatorio_desfavorable");
         extendedAttributeElement2.setAttribute("Value",sObligatorioDesfavorable);
         resultado.add(extendedAttributeElement2);
        
        if("Tramite".equals(tipoCondicion))
            tipoCondicion = "T";
        else if("Pregunta".equals(tipoCondicion))
            tipoCondicion = "P";
        else if("Resolucion".equals(tipoCondicion))
            tipoCondicion = "R";
        else if("Finalizacion".equals(tipoCondicion))
            tipoCondicion = "F";
        else
            tipoCondicion = "";

         Element extendedAttributeElement3 = new Element ("ExtendedAttribute",XMLNS);
         //extendedAttributeElement3.setAttribute("Name","SAL_TAC");
         extendedAttributeElement3.setAttribute("Name","tipo_accion");
         extendedAttributeElement3.setAttribute("Value",tipoCondicion);
         resultado.add(extendedAttributeElement3);
        
         Element extendedAttributeElement4 = new Element ("ExtendedAttribute",XMLNS);
         //extendedAttributeElement4.setAttribute("Name","SAL_TAA");
         extendedAttributeElement4.setAttribute("Name","tipo_accion_afirmativa");
         extendedAttributeElement4.setAttribute("Value",tipoFavorableSI);
         resultado.add(extendedAttributeElement4);
        
         Element extendedAttributeElement5 = new Element ("ExtendedAttribute",XMLNS);
         //extendedAttributeElement5.setAttribute("Name","SAL_TAN");
         extendedAttributeElement5.setAttribute("Name","tipo_accion_negativa");
         extendedAttributeElement5.setAttribute("Value",tipoFavorableNO);
         resultado.add(extendedAttributeElement5);

         String texto = "";
         if(dtVO.getTexto()!=null) texto = dtVO.getTexto();
         Element extendedAttributeElement6 = new Element ("ExtendedAttribute",XMLNS);
         //extendedAttributeElement6.setAttribute("Name","SML_VALOR");
         extendedAttributeElement6.setAttribute("Name","pregunta");
         extendedAttributeElement6.setAttribute("Value",texto);
         resultado.add(extendedAttributeElement6);
        
        String tipoUsuarioFirmaNotificacion = "";
        if(dtVO. getTipoUsuarioFirma()!=null && dtVO. getTipoUsuarioFirma().length()>0){
            tipoUsuarioFirmaNotificacion = dtVO.getCodigoOtroUsuarioFirma();
        }

        Element tipoFirmaNotificacion = new Element ("ExtendedAttribute",XMLNS);
        tipoFirmaNotificacion.setAttribute("Name","tipo_firma_electronica");
        tipoFirmaNotificacion.setAttribute("Value",tipoUsuarioFirmaNotificacion);
        resultado.add(tipoFirmaNotificacion);

        String codUsuarioFirmaNotificacion = "";
        if(dtVO.getCodigoOtroUsuarioFirma()!=null && dtVO.getCodigoOtroUsuarioFirma().length()>0){
            codUsuarioFirmaNotificacion = dtVO.getCodigoOtroUsuarioFirma();
        }

        Element usuarioFirmaNotificacion = new Element ("ExtendedAttribute",XMLNS);
        usuarioFirmaNotificacion.setAttribute("Name","cod_usuario_firma_notificacion");
        usuarioFirmaNotificacion.setAttribute("Value",codUsuarioFirmaNotificacion);
        resultado.add(usuarioFirmaNotificacion);
        
        // Trámites con notificaciones
        
        // Tramite notificado
        Element tramiteNotificado = new Element ("ExtendedAttribute",XMLNS);
        tramiteNotificado.setAttribute("Name","tramite_notificado");
        tramiteNotificado.setAttribute("Value",dtVO.isTramiteNotificado() ? "1" : "0");
        resultado.add(tramiteNotificado);
        
        // Admite Notificacion Electrónica
        String admiteNotificacion = "";
        if (dtVO.getAdmiteNotificacionElectronica() != null && dtVO.getAdmiteNotificacionElectronica().length() > 0) {
            admiteNotificacion = dtVO.getAdmiteNotificacionElectronica();
        }

        Element notificacion = new Element("ExtendedAttribute", XMLNS);
        notificacion.setAttribute("Name", "admite_notificacion_electronica");
        notificacion.setAttribute("Value", admiteNotificacion);
        resultado.add(notificacion);
        
        
        // codigo tipo notificacion Electrónica
        Element coditoTipoNotificacionElectrónica = new Element ("ExtendedAttribute",XMLNS);
        coditoTipoNotificacionElectrónica.setAttribute("Name","codigo_tipo_notificacion_electronica");
        coditoTipoNotificacionElectrónica.setAttribute("Value",StringUtils.isNotNullOrEmpty(dtVO.getCodigoTipoNotificacionElectronica()) ? dtVO.getCodigoTipoNotificacionElectronica() : "");
        resultado.add(coditoTipoNotificacionElectrónica);
        
        // departamento
        Element codigoDepartamento = new Element ("ExtendedAttribute",XMLNS);
        codigoDepartamento.setAttribute("Name","codigo_departamento");
        codigoDepartamento.setAttribute("Value",StringUtils.isNotNullOrEmpty(dtVO.getCodDepartamentoNotificacion()) ? dtVO.getCodDepartamentoNotificacion() : "");
        resultado.add(codigoDepartamento);
        
        // descripción departamento
         Element descripcionDepartamento = new Element ("ExtendedAttribute",XMLNS);
        descripcionDepartamento.setAttribute("Name","descripcion_departamento");
        descripcionDepartamento.setAttribute("Value", StringUtils.isNotNullOrEmpty(dtVO.getDescripcionDepartamentoNotificacion()) ? dtVO.getDescripcionDepartamentoNotificacion(): "");
        resultado.add(descripcionDepartamento);
        
        
        // notificacion obligatoria
        Element notificacionObligatoria = new Element("ExtendedAttribute", XMLNS);
        notificacionObligatoria.setAttribute("Name", "notificacion_obligatoria");
        notificacionObligatoria.setAttribute("Value", dtVO.getNotificacionElectronicaObligatoria() ? "1" : "0");
        resultado.add(notificacionObligatoria);
        
        // certificado de organismo 
        Element certificadoOrganismo = new Element("ExtendedAttribute", XMLNS);
        certificadoOrganismo.setAttribute("Name", "certificado_organismo");
        certificadoOrganismo.setAttribute("Value", dtVO.getCertificadoOrganismoFirmaNotificacion() ? "1" : "0");
        resultado.add(certificadoOrganismo);

        return resultado;
    }
    
    /**
     * Recuperar los códigos internos de las unidades de inicio de un procedimiento para poder importarlo en un determinado entorno. 
     * @param uorsProcedimiento: ArrayList<ExistenciaUorImportacionVO>
     * @return Vector<String>: Coleccion con códigos internos de las uor
     */
    public static Vector<String> recuperarCodigosUorProcedimiento(ArrayList<ExistenciaUorImportacionVO> uorsProcedimiento){
        Vector<String> unidades = new Vector<String>();
        for(int i=0;i<uorsProcedimiento.size();i++){
            unidades.add(uorsProcedimiento.get(i).getCodigoUor());
        }
        return unidades;
    }

}
