function buscarOperacion(tipoOperacion,nombreOperacion,nombreModulo,descripcionWebServices,tipoOrigenOperaciones){
    var exito =false;
    
    var descripcion = null;
    for(h=0;h<descripcionWebServices.length;h++){
        var datosAuxiliar = descripcionWebServices[h].split("(");
        var nombre = datosAuxiliar[0].trim();
        if(nombre.trim()==nombreOperacion.trim()){
            // Se comprueba si la operación es de WS o de módulo
            if(tipoOrigenOperaciones[h]=="WS"){
                descripcion = descripcionWebServices[h];
                break;
            }else
            if(tipoOrigenOperaciones[h]=="MODULO" && tipoOrigenOperaciones[h]==tipoOperacion){
                if(nombreModulos[h]==nombreModulo){
                    descripcion = descripcionWebServices[h];
                }else
                    descripcion = null;
                break;
            }
        }
    }
	
    if(descripcion!=null && descripcion!="") 
        exito = true;    
    
    return exito;
}


function buscarCodigoOperacion(tipoOperacion,nombreOperacion,nombreModulo,descripcionWebServices,codigosWebServices,tipoOrigenOperaciones){
    
    var codigo = null;
    for(h=0;h<descripcionWebServices.length;h++){
        var datosAuxiliar = descripcionWebServices[h].split("(");
        var nombre = datosAuxiliar[0].trim();
        if(nombre.trim()==nombreOperacion.trim()){
            // Se comprueba si la operación es de WS o de módulo
            if(tipoOrigenOperaciones[h]=="WS"){
                codigo = codigosWebServices[h];
                break;
            }else
            if(tipoOrigenOperaciones[h]=="MODULO" && tipoOrigenOperaciones[h]==tipoOperacion){
                if(nombreModulos[h]==nombreModulo){
                    codigo = codigosWebServices[h];
                }else
                    codigo = null;
                break;
            }
        }
    }

    return codigo;
}