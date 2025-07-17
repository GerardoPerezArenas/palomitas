package es.altia.agora.business.sge.plugin.documentos.util;

import es.altia.agora.business.editor.mantenimiento.persistence.DocumentosAplicacionManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.technical.ConstantesDatos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class DocumentoTramitacionHelper {
    private static Logger log = Logger.getLogger(DocumentoTramitacionHelper.class);
    
    public static Hashtable<String, Object> construirMapaDatosDocumentoTramitacion(HashMap<String, Object> parametros, int tipoDocumento){
        Hashtable<String, Object> mapaDatos = new Hashtable<String, Object>();
        
        UsuarioValueObject usuarioVO = (UsuarioValueObject) parametros.get("usuario");
        Integer codMunicipio = (Integer) parametros.get("codMunicipio");
        String codProcedimiento = (String) parametros.get("codProcedimiento");
        Integer codTramite = (Integer) parametros.get("codTramite");
        Integer codDocumento = (Integer) parametros.get("codDocumento");
        String[] params = (String[]) parametros.get("paramsBBDD");
        String nombreGestor = (String) parametros.get("nombreServicioAlmacen");
        
        log.debug("esDocumentoParaRelacion codMunicipio: " + codMunicipio + " , codProcedimiento: " + 
                codProcedimiento + ", codTramite: " + codTramite + ", codDocumento: " + codDocumento + ", params: " + params );
        boolean paraRelacion = DocumentosAplicacionManager.getInstance().esDocumentoParaRelacion(codMunicipio, codProcedimiento, codTramite, codDocumento, params);
        
        log.debug("paraRelacion: " + paraRelacion);
        
        mapaDatos.put("codProcedimiento", codProcedimiento);
        mapaDatos.put("codMunicipio", codMunicipio);
        mapaDatos.put("ejercicio", parametros.get("ejercicio"));
        mapaDatos.put("numeroExpediente", parametros.get("numExpediente"));
        mapaDatos.put("codTramite", codTramite);
        mapaDatos.put("ocurrenciaTramite", parametros.get("ocurrenciaTramite"));
        mapaDatos.put("codDocumento", codDocumento);
        mapaDatos.put("nombreDocumento", parametros.get("nombreDocumento"));
        mapaDatos.put("codUsuario", usuarioVO.getIdUsuario());
        mapaDatos.put("fichero", parametros.get("ficheroDoc"));
        mapaDatos.put("perteneceRelacion", paraRelacion ? "true" : "false");
        mapaDatos.put("params", params);
        
        if(tipoDocumento == DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR){
            
            log.debug("tipoDocumento es de tipo " + tipoDocumento);
            
            String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(Integer.toString(usuarioVO.getOrgCod()), codProcedimiento, String.valueOf(codTramite), params);
            
            log.debug("codigoVisibleTramite: " + codigoVisibleTramite);
            
            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
            
            log.debug("nombreProcedimiento: " + nombreProcedimiento);
            
            mapaDatos.put("codigoVisibleTramite", codigoVisibleTramite);
            mapaDatos.put("codificacion", ConstantesDatos.CODIFICACION_UTF_8);
            mapaDatos.put("extension", parametros.get("extension"));
            mapaDatos.put("tipoMime", parametros.get("tipoMimeOriginal"));
            
            /**
            * SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS
            * QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL *
            */
           ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");

           String carpetaRaiz = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuarioVO.getOrgCod() + ConstantesDatos.BARRA + nombreGestor + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

           log.debug("carpetaRaiz: " + carpetaRaiz);
           
           String descripcionOrganizacion = usuarioVO.getOrg();
           ArrayList<String> listaCarpetas = new ArrayList<String>();
           listaCarpetas.add(carpetaRaiz);
           listaCarpetas.add(usuarioVO.getOrgCod() + ConstantesDatos.GUION + descripcionOrganizacion);
           listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
           listaCarpetas.add(((String) parametros.get("numExpediente")).replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));

           mapaDatos.put("listaCarpetas", listaCarpetas);
           
        }
        
        return mapaDatos;
    }
}
