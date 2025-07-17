/**
  * Crea una barra de paginacion en el elemento del DOM cuyo id  se pasa como parametro. Expone la funcion establecerPaginacion que genera
  * el contenido de la barra a partir de los datos de paginacion pasados como parametro.
  * @param {object} options opciones de configuracion de la barra paginadora
  * @param {String} id identificador del elemento del DOM en el que se creara la barra de paginacion. Obligatorio.
  * @param {Integer} numPaginas numero de paginas a mostrar simultaneamente en la barra. Si no se establece, por defecto es 5.
  * @param {function} click(numeroPagina) funcion que se ejecutara en el click de cada elemento de la barra. Recibe como parametro el numero
  * de pagina seleccionado. Si no se establece, por defecto no se ejecuta ninguna accion.
  */
var BarraPaginacionBuscador = function(options){
        
    /* Opciones por efecto. */
    var _defaultOptions = {
      click : function(){},
      numPaginas: 5
    };
    
    /* Opciones del componente. */
    var _options;
    
    /* Elemento del DOM en el que se crea la barra. */
    var _domElement;    
    
    /* Datos actuales de la barra. */    
    var _datosActuales = {
        numPaginaActual: null ,
        numPaginasTotal: null,            
        numPaginasBarra: null,
        numPrimeraPaginaBarra: null,
        numUltimaPaginaBarra: null
    };
    
    /*
     * Inicializa la barra de paginacion.
     */
    var _init = function(options){
        _options = $.extend(_defaultOptions, options);
        _domElement = $('#' + _options.id);
        _domElement.on("click", "a.paginador", function(){
            $("html, body").delay(200).animate({
                scrollTop: 0 
            }, 1000); 
            var dataPagina = $(this).data("pagina");
            var paginaSeleccionada = dataPagina;
            if (dataPagina === "prev"){                
                paginaSeleccionada = _datosActuales.numPaginaActual - 1;
            }            
            if(dataPagina === "next"){                
                paginaSeleccionada = _datosActuales.numPaginaActual + 1;
            }                     
            _options.click(paginaSeleccionada);
        });                
    }
    
    /* 
     * Plantilla con el HTML de la barra de paginacion. 
     */
    
    // Boostrap 4
    var _templatePaginacion = '\
            <div class="d-flex justify-content-center"> \
                <nav aria-label="Page navigation example"> \
                    <ul id="numerosPagina" class="pagination justify-content-center"> \
                        <% if(numPaginaActual !== 1 && numPaginaActual !== null) { %> \
                            <li><a href="javascript:void(0)" class="paginador page-link page-item-ant-sig" data-pagina="prev">&lt; Anterior</a></li> \
                            <% } %> \
                        <% if(numPaginaActual !== 1 && numPaginaActual > 4) { %> \
                            <li class="page-item"><a href="javascript:void(0)" class="paginador page-link page-link-bh vertical" data-pagina="1">1 ...</a></li> \
                            <% } %> \
                               <% for(var i= numPrimeraPaginaBarra; i <= numUltimaPaginaBarra ; i++) { %>\
                                       <% if(i==numPaginaActual || (i == 1 && numPaginaActual == null) ) { %> \
                                           <li class="page-item active">\
                                            <a href="javascript:void(0)" class="paginador page-link page-link-bh vertical" data-pagina="<%=i%>"><%=i%></a></li> \
                                       <% }else { %>\
                                           <li class="page-item">\
                                            <a href="javascript:void(0)" class="paginador page-link page-link-bh vertical" data-pagina="<%=i%>"><%=i%></a></li> \
                                    <% } %>\
                                <% } %>\
                        <% if(numPaginaActual !== numPaginasTotal && (numPaginasTotal - numPaginaActual) > 3) { %> \
                               <li class ="page-item botonOculto"><a href="javascript:void(0)" class="paginador page-link page-link-bh" data-pagina="<%= numPaginasTotal %>">... <%= numPaginasTotal %></a></li> \
                        <% } %> \
                        <% if(numPaginaActual !== numPaginasTotal) { %> \
                           <li class="page-item"><a href="javascript:void(0)" class="paginador page-link page-item-ant-sig" data-pagina="next">Siguiente &gt;</a></li> \
                        <% } %> \
                    </ul> \
                </nav> \
        </div>';
    
    /* Plantilla compilada. */
    var _generarHTMLPaginacion = _.template(_templatePaginacion); 

    /*
     * Calcula cual debe ser la primera pagina que muestre la barra en funcion de la pagina actual y el numero de paginas que muestra la 
     * barra en cada momento. 
     */
    var _calcularPrimeraPaginaBarra = function(numPaginaActual, numPaginasBarra, numPaginasTotal){      
        var paridad = numPaginasBarra % 2;
        var mitad = (paridad == 0) ? (numPaginasBarra / 2) : ((numPaginasBarra + 1) / 2);
        if(mitad + 1 >= numPaginaActual){
            var pagina = 1;
        }else{
            if(numPaginasTotal - numPaginaActual < 3){
                pagina = numPaginasTotal - numPaginasBarra;
                pagina = (pagina===0?1:pagina);
            }else{
                var pagina = numPaginaActual - mitad + 1;
            }
            
        }
        
        return  pagina;    
    }
    
    /*
     * Calcula cual debe ser la primera Utima pagina que muestre la barra en funcion de la primera pagina que debe mostrar, el numero de
     * paginas que muestra la barra en cada momento y el numero de paginas total.
     */
    var _calcularUltimaPaginaBarra = function(numPaginasBarra, numPrimeraPaginaBarra, numPaginasTotal){
        if(numPrimeraPaginaBarra !== 1){
            if(numPrimeraPaginaBarra + numPaginasBarra == numPaginasTotal){
                var calculoUltima = numPrimeraPaginaBarra + numPaginasBarra;
            }else{
                var calculoUltima = numPrimeraPaginaBarra + numPaginasBarra - 1;
            }
            
        }else{
        	var calculoUltima = (numPaginasTotal!==numPaginasBarra ? numPrimeraPaginaBarra + numPaginasBarra : numPaginasBarra);
        }
        
        var ultima = (calculoUltima < numPaginasTotal) ? calculoUltima : numPaginasTotal;
        
        return ultima;
    }
    
    /*
     * Establece el html de la barra de paginacion a partir de los datos de paginacion.
     */
    var _establecerPaginacion = function(datosPaginacion){        
        var numPaginaActual = datosPaginacion.numPaginaActual;
        var numPaginasTotal = datosPaginacion.numPaginasTotal;
        var numPaginasBarra = _options.numPaginas;
        // Se calcula la primera pagina a mostrar
        var numPrimeraPaginaBarra = _calcularPrimeraPaginaBarra(numPaginaActual, numPaginasBarra,numPaginasTotal);        
        var numUltimaPaginaBarra = _calcularUltimaPaginaBarra(numPaginasBarra, numPrimeraPaginaBarra, numPaginasTotal);
        
        _datosActuales = {
            numPaginaActual: numPaginaActual ,
            numPaginasTotal: numPaginasTotal,            
            numPaginasBarra: numPaginasBarra,
            numPrimeraPaginaBarra: numPrimeraPaginaBarra,
            numUltimaPaginaBarra: numUltimaPaginaBarra
        };
        if (numPaginasTotal > 1){
            _domElement.html(_generarHTMLPaginacion(_datosActuales));
        } else {
            _domElement.html("");
        }
    }
        
    
    
    // API publica      
    
    // Se inicializa el componente
    _init(options);
    
    return {
    	/**
         * Establece el HTML con la  paginacion en el elemento DOM en el que se ha creado la barra.
         * @param {object} datosPaginacionoptions datos para pintar la paginacion
         * @param {Integer} datosPaginacion.numPaginaActual n√umero de pagina actual
         * @param {Integer} datosPaginacion.numPaginasTotal numero e paginas total
         */
        establecerPaginacion: _establecerPaginacion
    }
};
