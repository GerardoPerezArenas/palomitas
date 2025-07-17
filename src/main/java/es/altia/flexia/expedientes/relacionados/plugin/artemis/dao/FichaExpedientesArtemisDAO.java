package es.altia.flexia.expedientes.relacionados.plugin.artemis.dao;

import es.altia.flexia.expedientes.relacionados.plugin.artemis.vo.EmpresaAdjudicatariaVO;
import es.altia.flexia.expedientes.relacionados.plugin.artemis.vo.ExpedienteArtemisVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author oscar.rodriguez
 */
public class FichaExpedientesArtemisDAO {

        private static FichaExpedientesArtemisDAO instance = null;
        private Logger log = Logger.getLogger(FichaExpedientesArtemisDAO.class);
        private final String DOT_COMMA = ";";
        private final String COMMA = ",";
        private final String BLANK_SPACE = " ";

        private FichaExpedientesArtemisDAO(){}

        public static FichaExpedientesArtemisDAO getInstance(){
            if(instance==null)
                instance = new FichaExpedientesArtemisDAO();

            return instance;
        }


        /**
         * Recupera la información a mostrar de un determinado expediente  de Artemis en su ficha de expediente
         * @param numExpediente: Número de expediente
         * @param con: Conexión a la base de datos
         * @return ExpedienteArtemisVO con toda la información recuperada
         */
        public ExpedienteArtemisVO getInfoExpediente(String numExpediente,Connection con){
            ExpedienteArtemisVO expediente = null;
            Statement st = null;
            ResultSet rs  = null;
            
            try{
                String sql = "SELECT NOMEXP,OBJETO,DEPRESPEXP,AREAADQ,NATURCONTRATO,ALCANCEMAXCONIVA," +
                                 "ALCANCEMAXSINIVA,NUMLOTES,IMPORTEMODELOOFERTACONIVA,IMPORTEMODELOOFERTASINIVA, "   +
                                 "CODIGOSCPV,PLAZORECEPOFERTASDIAS,DURACIONCONTRATONUM,DURACIONCONTRATOUNID,ESTADOEXPEDIENTE,EMPRESASADJUDICATARIAS, " +
                                 "CREATEDDATE,FECHAADJ,FECHARESOLLIC " +
                                 " FROM AISCSP_ELIC_CONS_EXP  WHERE NUMEXP='" + numExpediente + "'";
                
                log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    expediente = new ExpedienteArtemisVO();
                    expediente.setNumExpediente(numExpediente);
                    expediente.setNombreExpediente(rs.getString("NOMEXP"));
                    expediente.setObjeto(rs.getString("OBJETO"));
                    expediente.setDepartamentoResponsable(this.getDepartamentoResponsable(rs.getString("DEPRESPEXP"), con));
                    expediente.setAreaAdquisicionesResponsable(this.getAreaAdquisicionesResponsable(rs.getString("AREAADQ"), con));
                    String naturalezaContrato           = rs.getString("NATURCONTRATO");
                    String[] listaNaturalezaContrato   = naturalezaContrato.split(DOT_COMMA);
                    expediente.setNaturalezaContrato(this.getNaturalezaContrato(listaNaturalezaContrato, con));
                    expediente.setAlcanceMaximoConIVA(rs.getString("ALCANCEMAXCONIVA"));
                    expediente.setAlcanceMaximoSinIVA(rs.getString("ALCANCEMAXSINIVA"));
                    expediente.setNumeroLotes(rs.getString("NUMLOTES"));
                    expediente.setImporteModeloOfertaConIVA(rs.getString("IMPORTEMODELOOFERTACONIVA"));
                    expediente.setImporteModeloOfertaSinIVA(rs.getString("IMPORTEMODELOOFERTASINIVA"));
                    Timestamp tFechaInicio =  rs.getTimestamp("CREATEDDATE");
                    String sFechaInicio = "";
                    String fechaAdjudicacionDefinitiva   = "";
                    String fechaAdjudicacionProvisional = "";
                    if(tFechaInicio!=null){
                        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                        sFechaInicio = sf.format(tFechaInicio.getTime());                        
                    }
                    fechaAdjudicacionDefinitiva   = rs.getString("FECHAADJ");
                    fechaAdjudicacionProvisional = rs.getString("FECHARESOLLIC");
                    expediente.setFechaInicio(sFechaInicio);
                    expediente.setFechaAdjudicacionDefinitiva(fechaAdjudicacionDefinitiva);
                    expediente.setFechaAdjudicacionProvisional(fechaAdjudicacionProvisional);
                    String codigosCPV  = rs.getString("CODIGOSCPV");
                    expediente.setCodigosCPV(codigosCPV);      
                    expediente.setPlazoRecepcionOfertas(rs.getString("PLAZORECEPOFERTASDIAS"));
                    expediente.setDuracionContratoNum(rs.getString("DURACIONCONTRATONUM"));
                    expediente.setDuracionContratoUnidades(rs.getString("DURACIONCONTRATOUNID"));
                    expediente.setEstadoExpediente(this.getEstadoExpediente(rs.getString("ESTADOEXPEDIENTE"),con));
                    String empresasAdjudicatarias = rs.getString("EMPRESASADJUDICATARIAS");                                        
                    if(empresasAdjudicatarias!=null){
                        String[] codigosEmpresas = empresasAdjudicatarias.split(DOT_COMMA);                        
                        expediente.setEmpresasAdjudicatarias(this.getEmpresasAdjudicatarias(codigosEmpresas, con));
                    }
                }

            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }

            return expediente;
        }


        /**
         * Comprueba si existe un determinado expediente
         * @param numExpediente: Número del expediente
         * @param con: Conexión a la base de datos
         * @return Un boolean
         */
        public boolean existeExpediente(String numExpediente,Connection con){
            boolean exito = false;
            Statement st = null;
            ResultSet rs  = null;
            
            try{
                String sql = "SELECT COUNT(*) AS NUM " +                                 
                                 " FROM AISCSP_ELIC_CONS_EXP  WHERE NUMEXP='" + numExpediente + "'";
                log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);

                int num = 0;
                while(rs.next()){                    
                    num = rs.getInt("NUM");                  
                    if(num>=1) exito = true;
                }
                
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            
            return exito;
        }


        /**
         * Recupera la descripción del departamento responsable de un determinado expediente
         * @param codigoDepartamento: Código del departamento
         * @param con: Conexión a la base de datos
         * @return String con el nombre del departamento
         */
        private String getDepartamentoResponsable(String codigoDepartamento,Connection con){
            String descripcion = "";
            Statement st = null;
            ResultSet  rs  = null;

            try{
                String sql = "SELECT STRINGVALUE FROM AISCSP_ELIC_DEPRESPEXP  WHERE CHARACVALUEID=" + codigoDepartamento;
                log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next()){
                    descripcion = rs.getString("STRINGVALUE");
                }

            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }

            return descripcion;
        }


      /**
         * Recupera la descripción del área de adquisiciones del responsable de un determinado expediente
         * @param codigoArea: Código del área
         * @param con: Conexión a la base de datos
         * @return String con la descripción del área de adquisiciones
         */
        private String getAreaAdquisicionesResponsable(String codigoArea,Connection con){
            String descripcion = "";
            Statement st = null;
            ResultSet  rs  = null;

            try{
                String sql = "SELECT STRINGVALUE FROM AISCSP_ELIC_AREAADQ  WHERE CHARACVALUEID=" + codigoArea;
                log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next()){
                    descripcion = rs.getString("STRINGVALUE");
                }

            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }

            return descripcion;
        }


      /**
         * Recupera la descripción de la naturaleza del contrato
         * @param codigo: Array de String con los códigos de la naturaleza del contrato que puede tener un expediente
         * @param con: Conexión a la base de datos
         * @return String con la descripción de la naturaleza del contrato
         */
        private String getNaturalezaContrato(String[] codigos,Connection con){
            String descripcion = "";
            Statement st = null;
            ResultSet  rs  = null;

            try{

                StringBuffer salida = new StringBuffer();
                for(int i=0;codigos!=null && i<codigos.length;i++){
                    String codigo = codigos[i];

                    String sql = "SELECT STRINGVALUE FROM AISCSP_ELIC_NATURCONTRATO WHERE CHARACVALUEID=" + codigo;
                    log.debug(sql);
                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    while(rs.next()){
                        salida.append(rs.getString("STRINGVALUE"));
                        if(codigos.length-i>1)
                            salida.append(COMMA);
                            salida.append(BLANK_SPACE);
                    }
                }
                
                // La descripción de los códigos de la naturaleza del contrato concatenados y separados por una coma si hay más de uno.
                descripcion = salida.toString();
                
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }

            return descripcion;
        }


      /**
         * Recupera la descripción del estado del expediente
         * @param estado: Código del estado del expediente
         * @param con: Conexión a la base de datos
         * @return String con la descripción del estado del expediente
         */
        private String getEstadoExpediente(String estado,Connection con){
            String descripcion = "";
            Statement st = null;
            ResultSet  rs  = null;

            try{
                String sql = "SELECT STRINGVALUE FROM AISCSP_ELIC_ESTADOEXPEDIENTE  WHERE CHARACVALUEID=" + estado;
                log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next()){
                    descripcion = rs.getString("STRINGVALUE");
                }

            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }

            return descripcion;
        }


       /**
         * Recupera la información acerca de determinadas empresas adjudicatarias
         * @param codigos: Código de las empresas adjudicatarias
         * @param con: Conexión a la base de datos
         * @return ArrayList<EmpresaAdjudicatariaVO>
         */
        private ArrayList<EmpresaAdjudicatariaVO> getEmpresasAdjudicatarias(String[] codigos,Connection con){
            ArrayList<EmpresaAdjudicatariaVO> empresas = new ArrayList<EmpresaAdjudicatariaVO>();
            Statement st = null;
            ResultSet  rs  = null;

            try{

                for(int i=0;codigos!=null && i<codigos.length;i++){

                    String sql = "SELECT EMPRESAID,NOMBRE,NIF FROM AISCSP_ELIC_EMPRESAS  WHERE EMPRESAID=" + codigos[i];
                    log.debug(sql);
                    st = con.createStatement();
                    rs = st.executeQuery(sql);
                    while(rs.next()){
                        EmpresaAdjudicatariaVO empresa = new EmpresaAdjudicatariaVO();
                        empresa.setNif(rs.getString("NIF"));
                        empresa.setNombreEmpresa(rs.getString("NOMBRE"));
                        empresa.setCodigoEmpresa(rs.getString("EMPRESAID"));
                        empresas.add(empresa);
                    }
                }

            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }

            return empresas;
        }

}
