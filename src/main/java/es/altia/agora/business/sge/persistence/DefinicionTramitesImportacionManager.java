/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.sge.persistence;

import es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject;
import es.altia.agora.business.sge.DefinicionCampoValueObject;
import es.altia.agora.business.sge.DefinicionTramitesImportacionValueObject;
import es.altia.agora.business.sge.ResultadoAltaCampoProcedimientoBibliotecaVO;
import es.altia.agora.business.sge.ResultadoImportacionBibliotecaVO;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.sge.persistence.manual.DefinicionTramitesImportacionDAO;
import es.altia.agora.technical.EstructuraCampoAgrupado;
import es.altia.common.exception.ImportacionFlujoBibliotecaException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author MilaNP
 */
public class DefinicionTramitesImportacionManager {

    private static DefinicionTramitesImportacionManager instance = null;
    private static Logger log = Logger.getLogger(DefinicionTramitesImportacionManager.class);

    protected DefinicionTramitesImportacionManager() {
    }

    public static DefinicionTramitesImportacionManager getInstance() {
        // Necesitamos sincronización aquí para serializar (no multithread)
        // las invocaciones a este metodo
        synchronized (DefinicionTramitesImportacionManager.class) {
            if (instance == null) {
                instance = new DefinicionTramitesImportacionManager();
            }
        }
        return instance;
    }

    /**
     *
     * @param codProcDestino
     * @param codBiblioteca
     * @param params
     * @return Un int con los siguientes valores: 
     * 0 --> OK 1
     * 10 --> No se ha podido obtener conexión a la BBDD 
     * 20 --> Error al recuperar los campos suplementarios de procedimiento de la biblioteca 
     * 30 --> Error al grabar agrupaciones de procedimiento en el procedimiento destino
     * 31 --> Error al comprobar existencia codigo agrupaciones de biblioteca en el procedimiento destino
     * 40 --> Existen codigos de agrupaciones de la biblioteca en el procedimiento destino
     * 50 --> Error al comprobar existencia codigo campos suplementarios de biblioteca en destino
     * 60 --> Existen codigos de campo suplementario de la biblioteca en el procedimiento destino
     * 70 --> Error al grabar campos suplementarios de procedimiento en el procedimiento destino
     * 80 --> Error al recuperar flujo a importar
     * 81 --> Error al recuperar datos generales de tramite
     * 82 --> Error al recuperar datos de documentos de tramite
     * 83 --> Error al recuperar datos de enlaces de tramite
     * 84 --> Error al recuperar datos de condiciones de entrada de tramite
     * 85 --> Error al recuperar datos de condiciones de salida de tramite
     * 86 --> Error al recuperar datos de campos suplementarios de tramite
     * 87 --> Error al recuperar datos integraciones SW de tramite
     * 90 --> Error al recuperar codigos de tramite del procedimiento destino
     * 91 --> Error al grabar datos generales de tramite
     * 92 --> Error al grabar datos de documentos de tramite
     * 93 --> Error al grabar datos de enlaces de tramite
     * 94 --> Error al grabar datos de condiciones de entrada de tramite
     * 95 --> Error al grabar datos de condiciones de salida de tramite
     * 96 --> Error al grabar datos de campos suplementarios de tramite
     * 97 --> Error al grabar datos integraciones SW de tramite
     * 981 --> Error al grabar mapeo de codigos de procedimiento y de tramite al importar
     * 982 --> Error al recuperar mapeo de codigos de  tramite al importar
     * 99 --> Error al grabar flujo a importar
     */
    public ResultadoImportacionBibliotecaVO importarFlujo(String codProcDestino, String codBiblioteca, int codMunicipio, String[] params) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        int salida = -1;
        int totalCamposIns = 0;
        ResultadoImportacionBibliotecaVO result = new ResultadoImportacionBibliotecaVO();

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            if (con == null) {
                result.setEstado(1);
                return result;
            }

            DefinicionCampoValueObject defCVO = DefinicionProcedimientosDAO.getInstance().cargarEstructuraDatosSuplementariosProcedimiento(con, codBiblioteca, codMunicipio);
            ArrayList<EstructuraCampoAgrupado> listaCampos = new ArrayList<EstructuraCampoAgrupado>(defCVO.getListaCampos());
            ArrayList<DefinicionAgrupacionCamposValueObject> listaAgrupaciones = new ArrayList<DefinicionAgrupacionCamposValueObject>(defCVO.getListaAgrupaciones());

            ResultadoAltaCampoProcedimientoBibliotecaVO resAltaAgrupaciones = DefinicionProcedimientosDAO.getInstance().altaDefinicionAgrupaciones(con, codProcDestino, listaAgrupaciones);
            int estadoAltaAgr = resAltaAgrupaciones.getEstado();
            if(estadoAltaAgr==0){
                ArrayList<EstructuraCampoAgrupado> camposNoIns = new ArrayList<EstructuraCampoAgrupado>();
                if (listaCampos.size() > 0) {
                    for (EstructuraCampoAgrupado eCS : listaCampos) {
                        String codCampo = eCS.getCodCampo();
                        boolean existeCampo = DefinicionProcedimientosDAO.getInstance().existeCampoSuplementarioProcedimiento(codCampo, codProcDestino, con);

                        if (!existeCampo) {
                            totalCamposIns += DefinicionProcedimientosDAO.getInstance().altaDefinicionCampoSuplementario(eCS, codProcDestino, con);
                        } else camposNoIns.add(eCS);

                    }
                } 
                if(!camposNoIns.isEmpty()){
                    salida = 60;
                    result.setCamposSuplConflicto(camposNoIns);
                } else if(totalCamposIns == listaCampos.size()){
                    ArrayList<DefinicionTramitesImportacionValueObject> flujoAImportar = DefinicionTramitesImportacionDAO.getInstance().getTramitesImportacion(codBiblioteca, codMunicipio, con);
                    boolean respuesta = DefinicionTramitesImportacionDAO.getInstance().grabarTramitesImportacion(flujoAImportar, codProcDestino, codBiblioteca, con);
                    if (respuesta) {
                        salida = 0;
                    } else {
                        salida = 99;
                    }

                } else salida = 70;
            } else if(estadoAltaAgr==1) {
                salida = 30;
            } else {
                salida = 40;
                result.setAgrupCamposConflicto(resAltaAgrupaciones.getNoInsertado());
            }

            if(salida == 0)
                adapt.finTransaccion(con);
            else
                adapt.rollBack(con);
        } catch (BDException bdex) {
            try {
                adapt.rollBack(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bdex.printStackTrace();
            log.error("No se ha podido obtener conexión a la BBDD ");
            salida = 10;
        } catch (ImportacionFlujoBibliotecaException ifbex) {
            try {
                adapt.rollBack(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ifbex.printStackTrace();
            log.error("Ha habido errores en la importacion");
            salida = ifbex.getEstado();

        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.setEstado(salida);
        }
        
        return result;
    }
}
