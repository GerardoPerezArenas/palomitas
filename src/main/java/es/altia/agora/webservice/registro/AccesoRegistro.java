package es.altia.agora.webservice.registro;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.webservice.registro.exceptions.RegistroException;

import java.util.ArrayList;
import java.util.Vector;

public interface AccesoRegistro {

    public Vector getAsientosEntradaRegistro(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                             String fechaDesde, String fechaHasta,String documento,String nombre,String apellido1,String apellido2,String codAsunto,String unidadRegistroAsunto,String tipoRegistroAsunto,String codUorDestino,String ejercicio,String numAnotacion, String codUorAnotacion) throws RegistroException;

    public Vector getAsientosExpedientesHistorico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                                  String fechaDesde, String fechaHasta,String documento,String nombre,String primerApellido,String segundoApellido,String codAsuntoSeleccionado,String unidadRegistroAsuntoSeleccionado,String tipoRegistroAsuntoSeleccionado,String codUorInterno, String ejercicio, String numAnotacion,String codUorAnotacion) throws RegistroException;

    public RegistroValueObject getInfoAsientoConsulta(RegistroValueObject registroVO, String[] params)
            throws RegistroException;

    public void recuperarAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException;

    public void cambiaEstadoAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, int estado, String[] params)
            throws RegistroException;

    public void adjuntarExpedientesDesdeUnidadTramitadora(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException;

    public ArrayList<AsientoFichaExpedienteVO> cargaListaAsientosExpediente(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException;

    public void iniciarExpedienteAsiento(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException;

    public void setNombreServicio(String idServicio);
    public String getNombreServicio();

    public String getPrefijoPropiedad();
    public void setPrefijoPropiedad(String prefijoPropiedad);
    
    public DocumentoValueObject viewDocument(RegistroValueObject registroVO,int codDocumento,String[] params)
            throws RegistroException;
    
    public Vector obtenerInteresados(RegistroValueObject gVO, String[] params) throws RegistroException;
    
    public Vector getAsientosEntradaRegistroPluginTecnico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
            String fechaDesde, String fechaHasta, String documento, String nombre, String apellido1,
            String apellido2, String codAsunto, String unidadRegistroAsunto, String tipoRegistroAsunto,
            String codUorDestino, String ejercicio, String numAnotacion,
            String codUorAnotacion, String codClasificacionAsunto, String unidadRegistroClasifAsunto, String docTecnicoRegistro)
            throws RegistroException;

    public Vector getAsientosExpedientesHistoricoPluginTecnico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
            String fechaDesde, String fechaHasta, String documento, String nombre, String primerApellido,
            String segundoApellido, String codAsuntoSeleccionado, String unidadRegistroAsuntoSeleccionado,
            String tipoRegistroAsuntoSeleccionado, String codUorInterno, String ejercicio,
            String numAnotacion, String codUorAnotacion, String codClasificacionAsunto, String unidadRegistroClasifAsunto, String docTecnicoRegistro)
            throws RegistroException;
}
