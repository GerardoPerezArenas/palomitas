package es.altia.agora.interfaces.user.web.util;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.persistence.MenuManager;

import java.util.Vector;

public class CargaMenu {
    private String cadenaHTML;
    private Vector menu;

    public CargaMenu(){
        cadenaHTML="";
    }

    public String getMenu(UsuarioValueObject usuarioVO){
        
        menu = MenuManager.getInstance().buscaMenu(usuarioVO);
        if ( menu !=null){
            Vector subMenu = buscaSubMenu(String.valueOf(0));
            int filas = Integer.parseInt((String)subMenu.lastElement());
            if(filas != 0){
                recMenu(usuarioVO, subMenu);
            } 

            return cadenaHTML;
        }else 
            return null;
    }

    private void recMenu(UsuarioValueObject usuarioVO, Vector subMenu){

        int filas = Integer.parseInt((String)subMenu.lastElement());
        if (filas != 0){
            for (int i=0; i < subMenu.size()-1; i+=3){
                String padre = (String) subMenu.elementAt(i);
                String descripcion = (String) subMenu.elementAt(i+1);
                String proceso = (String) subMenu.elementAt(i+2);

                Vector menuHijo = buscaSubMenu(padre);
                if(proceso==null) proceso = "";
                
                if ("".equals(proceso.trim()))
                    cadenaHTML += "<div><span>"+ descripcion + "</span>";
                else if (proceso.contains("http://") || proceso.contains("https://"))
                    cadenaHTML += "<div><a href='javascript:abrirUrlAplicacionExterna(\""+ proceso + 
                            "\");' >"+ descripcion + "</a>";
                else
                    cadenaHTML += "<div><a href='javascript:lanzarProceso(\""+ usuarioVO.getCaminoContexto()+ 
                            proceso + "\");' >"+ descripcion + "</a>";
                    
                
                recMenu(usuarioVO, menuHijo);
                cadenaHTML += "</div>";
            }
        }
    }

    private Vector buscaSubMenu(String pad){
        Vector auxSubMenu = new Vector();
        for (int i=0; i < menu.size(); i+=4){
            String auxPadre = (String)menu.elementAt(i);
            if(pad.equals(auxPadre)){
                auxSubMenu.addElement((String)menu.elementAt(i+1));
                auxSubMenu.addElement((String)menu.elementAt(i+2));
                auxSubMenu.addElement((String)menu.elementAt(i+3));
            }
        }
        auxSubMenu.addElement((String.valueOf(auxSubMenu.size()/3)));
        return auxSubMenu;
    }

}
