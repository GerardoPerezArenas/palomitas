package es.altia.agora.business.sge;

//Objecto VO
//para los roles que puede tener un interesado en un expediente.
//Aplicacion Gestion de Expedientes.
//Tabla E_ROL.
//No confundir, con los roles que puede tener un interesado en
//una anotación de Registro. (Tabla R_ROL)

public class RolVO {
    
    private int organizacion; // código de organizacion 
    private String codigoProcedimiento; // Código de procedimineto
    private int codigo; //Código del ROL
    private String descripcion; //Descripción del ROL
    private String pde; // ROL_PDE (Rol por defecto)
    private String pcw; // ROL_PCW (Rol por consulta Web)

   
    
    public RolVO() {
        super();
    }

    public int getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(int organizacion) {
        this.organizacion = organizacion;
    }

    public String getCodigoProcedimiento() {
        return codigoProcedimiento;
    }

    public void setCodigoProcedimiento(String codigoProcedimiento) {
        this.codigoProcedimiento = codigoProcedimiento;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPde() {
        return pde;
    }

    public void setPde(String pde) {
        this.pde = pde;
    }

    public String getPcw() {
        return pcw;
    }

    public void setPcw(String pcw) {
        this.pcw = pcw;
    }

   
}
