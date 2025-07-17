/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.sge.plantillafirma;

import es.altia.util.struts.DefaultActionForm;
import es.altia.util.commons.BasicTypesOperations;
import es.altia.util.commons.BasicValidations;
import es.altia.agora.business.sge.plantillafirma.vo.PlantillaFirmaVO;
import es.altia.agora.business.sge.plantillafirma.vo.PlantillaFirmaPK;

import javax.servlet.http.HttpServletRequest;

/**
 * @version $\Date$ $\Revision$
 */
public class PlantillaFirmaActionForm extends DefaultActionForm {
    /*_______Constants______________________________________________*/
    private static final String LIST_SEPARATOR = "$";

    /*_______Atributes______________________________________________*/
    protected int pUsuario;

    private int pIdMunicipio;
    private String pIdProcedimiento;
    private int pIdTramite;
    private int pIdPlantilla;
    protected String pRequiereFirma;
    protected int[] pIdsUsuariosFirmantes;
    protected String pIdsUsuariosFirmantesAsString;
    protected String[] pNombresUsuariosFirmantes;
    protected String pNombresUsuariosFirmantesAsString;
    protected int idFlujo;
    protected String nombreFlujo;

    /*_______Operations_____________________________________________*/
    protected void reset() {
        pUsuario=-1;

        pIdMunicipio = -1;
        pIdProcedimiento = null;
        pIdTramite = -1;
        pIdPlantilla = -1;
        pRequiereFirma = null;
        pIdsUsuariosFirmantes = new int[0];
        pIdsUsuariosFirmantesAsString = null;
        pNombresUsuariosFirmantes = new String[0];
        pNombresUsuariosFirmantesAsString = null;
        idFlujo = -1;
        nombreFlujo = null;
    }//reset

    public String toString() {
        final StringBuffer buff = new StringBuffer("[PlantillaFirmaActionForm:");

        buff.append("|]");
        return buff.toString();
    }//toString

    public int getUsuario() {
        return pUsuario;
    }
    public void setUsuario(int usuario) {
        pUsuario = usuario;
    }

    public int getIdMunicipio() {
        return pIdMunicipio;
    }
    public void setIdMunicipio(int idMunicipio) {
        pIdMunicipio = idMunicipio;
    }

    public String getIdProcedimiento() {
        return pIdProcedimiento;
    }
    public void setIdProcedimiento(String idProcedimiento) {
        pIdProcedimiento = idProcedimiento;
    }

    public int getIdTramite() {
        return pIdTramite;
    }
    public void setIdTramite(int idTramite) {
        pIdTramite = idTramite;
    }

    public int getIdPlantilla() {
        return pIdPlantilla;
    }
    public void setIdPlantilla(int idPlantilla) {
        pIdPlantilla = idPlantilla;
    }

    public String getRequiereFirma() {
        if (BasicValidations.isEmpty(pRequiereFirma)) return null;
        else return pRequiereFirma;
    }
    public void setRequiereFirma(String requiereFirma) {
        pRequiereFirma = requiereFirma;
    }

    public int[] getIdsUsuariosFirmantes() {
        return pIdsUsuariosFirmantes;
    }
    public void setIdsUsuariosFirmantes(int[] idsUsuariosFirmantes) {
        pIdsUsuariosFirmantes = idsUsuariosFirmantes;
        pIdsUsuariosFirmantesAsString = BasicTypesOperations.toString(idsUsuariosFirmantes,LIST_SEPARATOR);
    }

    public String getIdsUsuariosFirmantesAsString() {
        return pIdsUsuariosFirmantesAsString;
    }
    public void setIdsUsuariosFirmantesAsString(String idsUsuariosFirmantesAsString) {
        pIdsUsuariosFirmantesAsString = idsUsuariosFirmantesAsString;
        pIdsUsuariosFirmantes = BasicTypesOperations.toIntArray(idsUsuariosFirmantesAsString,LIST_SEPARATOR);
    }

    public String[] getNombresUsuariosFirmantes() {
        return pNombresUsuariosFirmantes;
    }
    public void setNombresUsuariosFirmantes(String[] nombresUsuariosFirmantes) {
        pNombresUsuariosFirmantes = nombresUsuariosFirmantes;
        pNombresUsuariosFirmantesAsString = BasicTypesOperations.toString(nombresUsuariosFirmantes,LIST_SEPARATOR);
    }

    public String getNombresUsuariosFirmantesAsString() {
        return pNombresUsuariosFirmantesAsString;
    }
    public void setNombresUsuariosFirmantesAsString(String nombresUsuariosFirmantesAsString) {
        pNombresUsuariosFirmantesAsString = nombresUsuariosFirmantesAsString;
        pNombresUsuariosFirmantes = BasicTypesOperations.toStringArray(nombresUsuariosFirmantesAsString,LIST_SEPARATOR);
    }

    public int getIdFlujo() {
        return idFlujo;
    }

    public void setIdFlujo(int idFlujo) {
        this.idFlujo = idFlujo;
    }

    public String getNombreFlujo() {
        return nombreFlujo;
    }

    public void setNombreFlujo(String nombreFlujo) {
        this.nombreFlujo = nombreFlujo;
    }

    public void setPlantillaFirmaVO(PlantillaFirmaVO vo, HttpServletRequest request) {
        this.setIdMunicipio(vo.getIdMunicipio());
        this.setIdProcedimiento(vo.getIdProcedimiento());
        this.setIdTramite(vo.getIdTramite());
        this.setIdPlantilla(vo.getIdPlantilla());
        this.setRequiereFirma(vo.getRequiereFirma());
        this.setIdsUsuariosFirmantes(vo.getIdsUsuariosFirmantes());
        this.setIdFlujo(vo.getIdFlujo());
    }

    public PlantillaFirmaVO getPlantillaFirmaVO(HttpServletRequest request) {
        PlantillaFirmaVO result = new PlantillaFirmaVO(
                                    new PlantillaFirmaPK(
                                            this.getIdMunicipio(),
                                            this.getIdProcedimiento(),
                                            this.getIdTramite(),
                                            this.getIdPlantilla()
                                                ),
                                    this.getRequiereFirma(),
                                    this.getIdsUsuariosFirmantes(),
                                    this.getIdFlujo() );
        return result;
    }
}//class
/*______________________________EOF_________________________________*/