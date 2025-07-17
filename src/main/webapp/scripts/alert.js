function callAlertJsp(tipo,mensaje,title) {
    var resposta = 0;
    if (window.showModalDialog) {
       var msgtitle = ((title)?(title):((APP_TITLE)?(APP_TITLE):(" ")));
       resposta = window.showModalDialog(APP_CONTEXT_PATH+'/jsp/portafirmas/tpls/Alert.jsp?TipoMSG='+tipo+'&TituloMSG='+msgtitle+'&DescMSG='+mensaje,null,'center:yes;dialogHeight:150px;dialogWidth:300px;status:no;help:no;unadorned:yes;edge:raisen;scroll:no');
    } else {
        if (tipo == 'A')
            alert(mensaje);
        else
            resposta = (confirm(mensaje))?1:0;
    }
    return resposta;
}
