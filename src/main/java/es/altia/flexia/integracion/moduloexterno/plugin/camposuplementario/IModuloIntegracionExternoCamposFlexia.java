package es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario;

import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DocumentoExternoModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TerceroModuloIntegracionVO;

/**
 * Interfaz que se pone a disposición de los módulos externos para recuperar los campos suplementarios y campos desplegables de Flexia
 */
public abstract class IModuloIntegracionExternoCamposFlexia {
    // Tipos de los campos suplementarios existentes en Flexia
    public static final int CAMPO_NUMERICO     = 1;
    public static final int CAMPO_TEXTO        = 2;
    public static final int CAMPO_FECHA        = 3;
    public static final int CAMPO_TEXTO_LARGO  = 4;
    public static final int CAMPO_FICHERO      = 5;
    public static final int CAMPO_DESPLEGABLE  = 6;

    public abstract SalidaIntegracionVO getCampoSuplementarioExpediente(String codOrganizacion,String ejercicio,String numExpediente,String codProcedimiento,String codCampo,int TIPO_CAMPO);
    public abstract SalidaIntegracionVO getCampoSuplementarioTramite(String codOrganizacion,String ejercicio,String numExpediente,String codProcedimiento,int codTramite, int ocurrenciaTramite,String codCampo, int TIPO_CAMPO);
    public abstract SalidaIntegracionVO grabarCampoSuplementario(CampoSuplementarioModuloIntegracionVO campo);
    public abstract SalidaIntegracionVO eliminarValorCampoSuplementario(CampoSuplementarioModuloIntegracionVO campo);
    public abstract SalidaIntegracionVO getCampoDesplegable(String codOrganizacion,String codCampo);
    public abstract SalidaIntegracionVO getCodDesplegableCampoSup(String codCampo, String codOrganizacion, String codProcedimiento);
    public abstract SalidaIntegracionVO getCamposDesplegables(String codOrganizacion,String codProcedimiento,boolean recuperarValores);
    public abstract SalidaIntegracionVO getCamposDesplegables(String codOrganizacion,String codProcedimiento,int codTramite,boolean recuperarValores);
    public abstract SalidaIntegracionVO getExpedientesRelacionados(String codOrganizacion,String numExpediente,String codProcedimiento,String ejercicio);
    public abstract SalidaIntegracionVO getListaDocumentosTramitacion(String codOrganizacion,String numExpediente,String codProcedimiento,int codTramite,int ocurrenciaTramite,String ejercicio);
    public abstract SalidaIntegracionVO getDocumentoTramitacion(String codOrganizacion,String numExpediente,String codProcedimiento,int codTramite,int ocurrenciaTramite,int numeroDocumento,String ejercicio,String nombreDocumento);
    public abstract SalidaIntegracionVO getTramite(String codOrganizacion,String numExpediente,String codProcedimiento,int codTramite,int ocurrenciaTramite,String ejercicio);
    public abstract SalidaIntegracionVO getExpediente(String codOrganizacion,String numExpediente,String codProcedimiento,String ejercicio);
    public abstract SalidaIntegracionVO getDocumentoPresentado(String codOrganizacion,String numExpediente,String codProcedimiento, String ejercicio, String codDocumento);
    public abstract SalidaIntegracionVO getCircuitoFirmasDocumentoPresentado(String codOrganizacion,String codProcedimiento,String codDocumento);
    public abstract SalidaIntegracionVO getFirmasDocumentoPresentado (String codOrganizacion,String codProcedimiento,String numExpediente,String ejercicio,String nombreDocumento);
    public abstract SalidaIntegracionVO getCampoSuplementarioTercero(String codOrganizacion, Integer codTercero, String codCampo, int TIPO_CAMPO);
    public abstract SalidaIntegracionVO getUOR(String codOrganizacion, String uorCodVis);
    public abstract SalidaIntegracionVO getUsuario(String codOrganizacion, String idUsuario);
    public abstract SalidaIntegracionVO getUsuariosPermisoUOR (String codOrganizacion, String uorCodVis);
    public abstract SalidaIntegracionVO getExpedientesByInteresadoAndProc (String codOrganizacion, String codPro, String idDocumento,String documento);
    public abstract SalidaIntegracionVO iniciarExpediente(String codOrganizacion, ExpedienteModuloIntegracionVO expediente,String unidadInicioTramite);
    public abstract SalidaIntegracionVO getTerceros(String codOrganizacion,int tipoDocumento, String documento, String nombre, String apellido1, String apellido2);
    public abstract SalidaIntegracionVO altaTercero(String codOrganizacion, Integer usuarioAlta,TerceroModuloIntegracionVO tercero,Integer moduloAlta);
    public abstract SalidaIntegracionVO altaInteresadoExpediente(String codOrganizacion, ExpedienteModuloIntegracionVO expediente,
            TerceroModuloIntegracionVO tercero,  int codigoRolInteresado,int mostrar,Integer usuarioAlta,Integer moduloAlta);
    
    public abstract SalidaIntegracionVO setDocumentoExterno(DocumentoExternoModuloIntegracionVO documento,String codOrg);
    public abstract SalidaIntegracionVO getDocumentoExterno(DocumentoExternoModuloIntegracionVO documento,String codOrg);
    public abstract String getModulosExtensionXmlBuzonEntrada(String codOrganizacion,String numExpediente);


}//class