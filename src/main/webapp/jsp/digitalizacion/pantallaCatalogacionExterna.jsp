<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic"%>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.technical.ConstantesDatos"%>
<%@page import="es.altia.flexia.registro.digitalizacion.lanbide.vo.DocumentoCatalogacionVO"%>
<%@page import="es.altia.flexia.registro.digitalizacion.lanbide.vo.TipoDocumentalCatalogacionVO"%>
<%@page import="java.util.ArrayList"%>

<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>

<html:html>
    <head>
        <jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1" />

        <TITLE>::: Catalogación externa de documentos :::</TITLE>        
            <%
                    UsuarioValueObject usuario = new UsuarioValueObject();
                    int idioma = 1;
                    int apl = 1;
                    int munic = 0;
                    String css="";
                    String idSesion="";
                    if (session.getAttribute("usuario") != null) {
                        idSesion = session.getId();
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idioma = usuario.getIdioma();
                        apl = usuario.getAppCod();
                        munic = usuario.getOrgCod();
                        css=usuario.getCss();
                    }
                    String separador = ConstantesDatos.SEPARADORAJAX;
                    
                    Config m_Config = ConfigServiceHelper.getConfig("common");
                    String statusBar = m_Config.getString("JSP.StatusBar");
                    
            %>

        <jsp:useBean id="descriptor" scope="request"
                     class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                     type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor" property="idi_cod" value="<%= idioma%>" />
        <jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>" />

        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/digitalizacion.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>

        <!-- listaComboBox modificado para que busque sin tener en cuenta las tildes -->
        <script type="text/javascript">
            ///////////////////////////////////////////////////////////////////////////////////////////////////////
// INICIO OBJETO COMBOBOX
///////////////////////////////////////////////////////////////////////////////////////////////////////

            CB_RowHeight = 19;
            CB_Borde = 1;
            CB_Scroll = 15;

            var cursor;
            var operadorConsulta = "";
            if (document.all)
                cursor = 'hand';
            else if (document.getElemenById)
                cursor = 'pointer';

            function CB_OcultarCombo(combo) {
                var cmb = document.getElementById('desc' + combo);
                if (cmb && cmb.combo) {
                    cmb.combo.ocultar();
                } else if (window["combo" + combo] != undefined) {
                    window["combo" + combo].ocultar();
                }
            }

            function Combo(nombre, idiomaUsuario) {
                this.id = nombre;
                this.idioma = 0;
                if (idiomaUsuario != undefined && idiomaUsuario != null) {
                    this.idioma = idiomaUsuario;
                }
                //Referenciamos los inputs del codigo y la descripcion.  

                /* ORIGINAL
                 this.cod = document.getElementById("cod"+nombre);
                 this.des = document.getElementById("desc"+nombre);
                 */


                var codigos = document.getElementsByName("cod" + nombre);
                var descripciones = document.getElementsByName("desc" + nombre);
                var anchors = document.getElementsByName("anchor" + nombre);

                if (codigos != null && codigos[0] != null)
                    this.cod = codigos[0];
                else {
                    //this.cod = "";
                    this.cod = document.getElementById("cod" + nombre);
                }

                if (descripciones != null && descripciones[0] != null)
                    this.des = descripciones[0];
                else {
                    //this.des = "";
                    this.des = document.getElementById("desc" + nombre);
                }

                if (anchors != null && anchors[0] != null) {
                    this.anchor = anchors[0];
                } else
                    this.anchor = document.getElementById("anchor" + nombre);
                /** ORIGINAL
                 this.boton = this.anchor.children(0);
                 */

                var hijos = new Array();
                hijos = this.anchor.children;
                if (hijos != null && hijos.length >= 1)
                    this.boton = hijos[0];

                this.selectedIndex = -1;
                this.timer = null;

                this.des.introducido = "";
                this.original = null;

                this.codigos = new Array();
                this.items = new Array();
                this.auxItems = new Array();
                this.auxCodigos = new Array();
                this.i_codigos = new Array();
                this.i_items = new Array();

                //Creamos la vista del combo, que será un DIV que incluirá la tabla con los elementos de la lista.
                this.base = document.createElement("DIV");
                this.base.combo = this;
                this.base.style.position = 'absolute';
                this.base.style.display = "none";
                this.base.onblur = function (event) {
                    var event = (event) ? event : ((window.event) ? window.event : "");
                    this.combo.timer = setTimeout('CB_OcultarCombo("' + this.combo.id + '")', 150);
                };
                this.base.onkeydown = function (event) {
                    var event = (event) ? event : ((window.event) ? window.event : "");
                    var tecla = "";

                    if (event.keyCode)
                        tecla = event.keyCode;
                    else
                        tecla = event.which;

                    if (tecla == 8) {
                        this.combo.buscaItem("-1");
                    } else if (tecla == 40) {
                        this.combo.selectItem(this.combo.selectedIndex + 1);
                    } else if (tecla == 38) {
                        this.combo.selectItem(this.combo.selectedIndex - 1);
                    } else if (tecla == 13) {
                        this.combo.ocultar();
                        if (this.combo.cod)
                            this.combo.cod.select();
                        else
                            this.combo.des.select();
                    } else {
                        if (tecla > 95)
                            tecla = tecla - 48;
                        var letra = String.fromCharCode(tecla);
                        this.combo.buscaItem(letra);
                    }
                    if (window.event) {
                        event.cancelBubble = true;
                        event.returnValue = false;
                    } else {
                        event.stopPropagation();
                        event.preventDefault();
                    }
                    return false;
                };

                this.view = document.createElement("DIV");
                this.base.appendChild(this.view);
                this.view.combo = this;
                this.view.className = 'xC';
                this.view.style.overflowY = 'auto';
                this.view.style.position = 'relative';
                this.view.onselectstart = function () {
                    return false;
                }
                this.view.ondblclick = function () {
                    return false;
                }
                this.view.onclick = function (event) {
                    event = (event) ? event : ((window.event) ? window.event : "");

                    var padre = "";
                    if (window.event)
                        padre = event.srcElement;
                    else
                        padre = event.target;

                    if (padre.tagName != 'DIV')
                        return;
                    var rowID = 1;

                    if (!!navigator.userAgent.match(/Trident.*rv[ :]*11\./) || navigator.appName.indexOf("Internet Explorer") != -1) {
                        // Internet Explorer
                        /*
                         var i = window.event.srcElement.parentElement.sourceIndex;
                         var j = window.event.srcElement.sourceIndex;
                         */
                        // Se calcula la posición de item del combo que ha sido seleccionado
                        var i = padre.parentElement.sourceIndex;
                        var j = padre.sourceIndex;
                        rowID = (j - i - 1);

                    } else {

                        // Firefox u otro navegador

                        /** Se obtiene el valor del item de menú seleccionado, para a partir de él, obtener la posición en el combo y seleccionar dicha fila **/
                        var hijos = padre.childNodes;
                        var valorFilaSeleccionada = "";
                        if (hijos != null) {
                            valorFilaSeleccionada = hijos[0].nodeValue;
                        }

                        var padreRaiz = padre.parentNode;
                        var hijosRaiz = padreRaiz.childNodes;
                        for (z = 0; z < hijosRaiz.length; z++) {
                            var nietos = hijosRaiz[z].childNodes;
                            if (nietos != null && nietos.length > 0) {
                                if (nietos[0].nodeValue == valorFilaSeleccionada) {
                                    break;
                                }
                            }
                        }
                        // En z está la posición de la fila seleccionada por el usuario
                        rowID = z;
                    }// else       

                    this.combo.selectItem(rowID);
                    this.combo.ocultar();
                    if (this.combo.cod)
                        this.combo.cod.select();
                    else
                        this.combo.des.select();
                    return false;
                };
                this.view.onfocus = function (event) {
                    event = (event) ? event : ((window.event) ? window.event : "");
                    if (this.combo.timer != null)
                        clearTimeout(this.combo.timer);
                    this.combo.timer = null;
                    this.combo.base.focus();
                };

                //*************************************************  
                this.resize = CB_resize;

                this.addItems = CB_addItems;
                this.addItems2 = CB_addItems2;
                this.clearItems = CB_clearItems;
                this.restoreIndex = CB_restoreIndex;
                this.selectItem = CB_selectItem;
                this.buscaCodigo = CB_buscaCodigo;
                this.buscaItem = CB_buscaItem;
                this.scroll = CB_scroll;
                this.display = CB_display;
                this.ocultar = CB_ocultar;
                this.init = CB_init;
                this.activate = CB_activate;
                this.deactivate = CB_deactivate;
                this.obligatorio = CB_obligatorio;
                this.buscaLinea = CB_buscaLinea;
                this.contieneOperadoresConsulta = CB_contieneOperadoresConsulta;
                this.clearSelected = CB_clearSelected;
                this.change = function () {};

                this.init();
            }

            function CB_init() {
                //Guardamos una referencia del combo en el imput de la descripcion
                if (this.cod) {
                    this.cod.combo = this;
                    this.cod.onfocus = function () {
                        this.select();
                    };
                    this.cod.onblur = function (event) {
                        if (!this.combo.contieneOperadoresConsulta(this))
                            this.combo.buscaCodigo(this.value);
                        else {
                            var codOld = this.value;
                            this.combo.selectItem(-1);
                            this.value = codOld;
                            this.combo.change();
                        }
                    };
                    this.cod.onkeydown = function (event) {
                        var event = (event) ? event : ((window.event) ? window.event : "");
                        var tecla = "";
                        if (event.keyCode)
                            tecla = event.keyCode;
                        else
                            tecla = event.which;

                        //if(event.keyCode == 40){
                        if (tecla == 40) {
                            this.combo.selectItem(this.combo.selectedIndex + 1);
                            if (window.event) {
                                event.cancelBubble = true;
                                event.returnValue = false;
                            } else {
                                event.stopPropagation = true;
                                event.preventDefault = false;
                            }
                            //} else if(event.keyCode == 38){
                        } else if (tecla == 38) {
                            this.combo.selectItem(this.combo.selectedIndex - 1);
                            /*
                             event.cancelBubble=true;
                             event.returnValue=false; */
                            if (window.event) {
                                event.cancelBubble = true;
                                event.returnValue = false;
                            } else {
                                event.stopPropagation = true;
                                event.preventDefault = false;
                            }

                            //} else if(event.keyCode==13) {
                        } else if (tecla == 13) {

                            this.combo.display();
                            /*
                             event.cancelBubble=true;
                             event.returnValue=false; */
                            if (window.event) {
                                event.cancelBubble = true;
                                event.returnValue = false;
                            } else {
                                event.stopPropagation = true;
                                event.preventDefault = false;
                            }
                        } else if (tecla == 9)
                        {
                            this.combo.ocultar();
                        }

                    };
                }


                /** ORIGINAL
                 *this.des.combo = this */
                if (this.des != null)
                    this.des.combo = this;

                this.des.onfocus = function () {
                    this.select();
                };
                this.des.onclick = function (event) {
                    this.introducido = "";
                    if (this.combo.auxCodigos.length > 0)
                        this.combo.addItems(this.combo.auxCodigos, this.combo.auxItems);

                    event = (event) ? event : ((window.event) ? window.event : "");

                    if (this.combo.cod) {

                        if (!this.combo.cod.readOnly) {

                            if (!this.combo.contieneOperadoresConsulta(this.combo.cod))
                                this.combo.display();
                        }
                    } else
                    {
                        this.combo.display();
                    }
                    event.stopPropagation();
                    return false;
                };

                this.des.onkeydown = function (event) {
                    event = (event) ? event : ((window.event) ? window.event : "");
                    var tecla = "";
                    if (event.keyCode)
                        tecla = event.keyCode;
                    else
                        tecla = event.which;

                    if (tecla == 8) {
                        this.combo.buscaItem("-1");
                        if (window.event) {
                            event.cancelBubble = true;
                            event.returnValue = false;
                        } else {
                            event.stopPropagation();
                            event.preventDefault();
                        }
                    } else if (tecla == 40) {
                        this.combo.selectItem(this.combo.selectedIndex + 1);
                        if (window.event) {
                            event.cancelBubble = true;
                            event.returnValue = false;
                        } else {
                            event.stopPropagation();
                            event.preventDefault();
                        }
                    } else if (tecla == 38) {
                        this.combo.selectItem(this.combo.selectedIndex - 1);
                        if (window.event) {
                            event.cancelBubble = true;
                            event.returnValue = false;
                        } else {
                            event.stopPropagation();
                            event.preventDefault();
                        }
                    } else if (tecla == 13) {
                        this.combo.display();
                        if (window.event) {
                            event.cancelBubble = true;
                            event.returnValue = false;
                        } else {
                            event.stopPropagation();
                            event.preventDefault();
                        }
                    } else if (tecla == 9) {
                        this.combo.ocultar();
                    }
                };

                this.des.onkeypress = function (event) {
                    var event = (event) ? event : ((window.event) ? window.event : "");
                    var tecla = "";
                    if (event.keyCode)
                        tecla = event.keyCode;
                    else
                        tecla = event.which;

                    //	var letra = String.fromCharCode(event.keyCode);
                    var letra = String.fromCharCode(tecla);
                    if (this.readOnly)
                        this.combo.buscaItem(letra);
                };

                this.des.onblur = function (event) {
                    if (!this.readOnly && this.value.length != 0) {
                        if (this.combo.cod) {
                            if (!this.combo.contieneOperadoresConsulta(this))
                                this.combo.buscaCodigo(this.combo.cod.value);

                            else {
                                var codOld = this.value;
                                this.combo.selectItem(-1);
                                this.value = codOld;
                                this.combo.change();
                            }
                        }//else this.combo.display();
                    }
                    var isChromium = window.chrome,
                            vendorName = window.navigator.vendor,
                            isOpera = window.navigator.userAgent.indexOf("OPR") > -1;
                    if (isChromium !== null && isChromium !== undefined && vendorName === "Google Inc." && isOpera == false) {
                        this.combo.timer = setTimeout('CB_OcultarCombo("' + this.combo.id + '")', 150);
                    } else if (navigator.userAgent.indexOf("Firefox") > 0) {
                        this.combo.timer = setTimeout('CB_OcultarCombo("' + this.combo.id + '")', 150);
                    }

                };

                if (this.anchor) {
                    this.anchor.combo = this;
                    this.anchor.onkeydown = function (event) {

                        var event = (event) ? event : ((window.event) ? window.event : "");
                        var tecla = "";
                        if (event.keyCode)
                            tecla = event.keyCode;
                        else
                            tecla = event.which;

                        //if(event.keyCode == 40){
                        if (tecla == 40) {
                            this.combo.selectItem(this.combo.selectedIndex + 1);
                            /*
                             event.cancelBubble=true;
                             event.returnValue=false; */
                            if (window.event) {
                                event.cancelBubble = true;
                                event.returnValue = false;
                            } else {
                                event.stopPropagation = true;
                                event.preventDefault = false;
                            }
                            //} else if(event.keyCode == 38){
                        } else if (tecla == 38) {
                            this.combo.selectItem(this.combo.selectedIndex - 1);
                            /*
                             event.cancelBubble=true;
                             event.returnValue=false; */
                            if (window.event) {
                                event.cancelBubble = true;
                                event.returnValue = false;
                            } else {
                                event.stopPropagation = true;
                                event.preventDefault = false;
                            }
                            //} else if(event.keyCode==13) {
                        } else if (tecla == 13) {

                            this.combo.display();
                            /*
                             event.cancelBubble=true;
                             event.returnValue=false; */
                            if (window.event) {
                                event.cancelBubble = true;
                                event.returnValue = false;
                            } else {
                                event.stopPropagation = true;
                                event.preventDefault = false;
                            }
                        } else if (tecla == 9)
                        {
                            this.combo.ocultar();
                        }
                    };
                    this.anchor.onclick = function (event) {
                        if (this.combo.cod) {
                            if (!this.combo.contieneOperadoresConsulta(this.combo.cod))
                                this.combo.display();
                        } else
                            this.combo.display();
                        event.stopPropagation();
                        return false;
                    };
                }
                document.getElementsByClassName("contenidoPantalla")[0].appendChild(this.base);
                this.addItems([], []);
            }

            function CB_buscaCodigo(cod) {
                if (cod == null || cod == undefined)
                    return true;
                var str = cod;
                if (str == '') {
                    this.selectItem(0);
                } else if (this.codigos[this.selectedIndex] != str) {
                    var i = this.i_codigos[str + ''];
                    if (i != null && i != undefined) {
                        this.selectItem(i);
                    } else {
                        if (this.des.readOnly)
                            jsp_alerta('A', 'Código inexistente');
                        this.selectItem(-1);
                        return false;
                    }
                }
                return true;
            }

            function CB_buscaLinea(cod) {
                if (cod == null || cod == 'undefined')
                    return true;
                var str = cod;

                if (this.selectedIndex >= 0 && this.selectedIndex < this.items.length) {
                    if (this.view.children[this.selectedIndex].className != 'xCDisabled') {
                        this.view.children[this.selectedIndex].className = 'xCSelected';
                    }
                }

                if (str == '') {
                    this.selectedIndex = 0;
                } else if (this.codigos[this.selectedIndex] != str) {
                    var i = this.i_codigos[str + ''];
                    if (i != null && i != undefined) {
                        this.selectedIndex = i;
                    } else {
                        this.selectedIndex = -1;
                    }
                }

                if (this.selectedIndex >= 0 && this.selectedIndex < this.items.length) {
                    if (this.view.children[this.selectedIndex].className != 'xCDisabled') {
                        this.view.children[this.selectedIndex].className = 'xCSelected';
                    }
                    this.scroll();
                    if (this.cod)
                        this.cod.value = this.codigos[this.selectedIndex];
                    this.des.value = this.items[this.selectedIndex];
                } else {
                    if (this.cod)
                        this.cod.value = "";
                    this.des.value = "";
                }

                return true;
            }

            function quitarTildes(st) {
                st = st.replace(new RegExp(/[àáâãäå]/g), "a");
                st = st.replace(new RegExp(/[èéêë]/g), "e");
                st = st.replace(new RegExp(/[ìíîï]/g), "i");
                st = st.replace(new RegExp(/[òóôõö]/g), "o");
                st = st.replace(new RegExp(/[ùúûü]/g), "u");

                return st;
            }

            function CB_buscaItem(letra) {
                if (letra) {
                    if (letra == "-1") {
                        if (this.des.introducido.length > 0)
                            this.des.introducido = this.des.introducido.substr(0, this.des.introducido.length - 1);
                        if (this.auxItems.length > 0) {
                            this.items = this.auxItems;
                            this.codigos = this.auxCodigos;
                        }
                    } else {
                        var regex = new RegExp("[a-z]");
                        if (regex.test(letra))
                            letra = letra.toUpperCase();
                        this.des.introducido += letra;
                    }
                }
                if (this.des.introducido == "") {
                    this.selectItem(0);
                    return true;
                }
                this.des.value = this.des.introducido;
                var novoItems = [];
                var novoCodigos = [];
               
                for (var i = 0; i < this.items.length; i++) {
                    var itemTemp = this.items[i];
                    itemTemp = quitarTildes(itemTemp);

                    //if (this.items[i].toUpperCase().indexOf(this.des.introducido.toUpperCase()) >= 0) {
                    if (itemTemp.toUpperCase().indexOf(this.des.introducido.toUpperCase()) >= 0) {
                        novoItems.push(this.items[i]);
                        novoCodigos.push(this.codigos[i]);
                    }
                }
                if (this.auxItems.length == 0) {
                    this.auxItems = this.items;
                    this.auxCodigos = this.codigos;
                }
                this.addItems(novoCodigos, novoItems);

                return true;
            }

            function CB_display() {
                if (this.base.style.display != "") {
                    this.resize();
                    this.original = this.selectedIndex;
                    this.base.style.display = "";
                    if (this.cod)
                        this.buscaCodigo(this.cod.value);
                    else {
                        for (i = 0; i < this.items.length; i++) {
                            if (this.items[i].toUpperCase() == this.des.value.toUpperCase()) {
                                this.selectItem(i);
                                break;
                            }
                        }
                        if (this.selectedIndex < 0)
                            this.selectItem(0);
                    }
                    this.base.focus();
                } else
                    this.base.style.display = "none";
            }

            function CB_ocultar() {
                if (this.selectedIndex >= this.items.length)
                    this.selectedIndex = -1;
                this.base.style.display = "none";
                if (this.selectedIndex != this.original)
                    this.change();
                this.original = this.selectedIndex;
            }

//********************************************************** //
// Calculamos el tamaño y posicion que tendrá el Combo.      //
//***********************************************************//
            function CB_resize() {

                var alto = 0;
                var altoElemento = this.des.offsetHeight;
                var altoVentana = document.documentElement.clientHeight;// Para que funcione en IE9 document.body.height devuelve un valor incorrecto
                var altoEncima = getOffsetTop(this.des); //this.des.getBoundingClientRect().top;	
                var altoDebajo = altoVentana - (altoEncima + altoElemento);
                var altoMayor = (altoDebajo > altoEncima ? altoDebajo : altoEncima);
                var numItems = this.items.length;
                var maxi = ((10 * CB_RowHeight) + 1) + (2 * CB_Borde) + CB_Scroll;
                var maxDiv = (maxi < altoDebajo ? maxi : (maxi < altoEncima ? maxi : altoMayor));
                var ctrlMayor = (maxi < altoDebajo ? 1 : (maxi < altoEncima ? -1 : (altoDebajo > altoEncima ? 2 : -2)));

                if (numItems > 10)
                    numItems = 10;

                for (var i = 0; i < numItems; i++) {
                    if ((alto + CB_RowHeight) < maxDiv)
                        alto += CB_RowHeight;
                }
                if (numItems == 0)
                    alto = CB_RowHeight;
                pX = getOffsetLeft(this.des);
                pY = (((ctrlMayor == 1) || (ctrlMayor == 2)) ? altoEncima + altoElemento : altoEncima - (alto + 2 * CB_Borde + CB_Scroll));
                if (isTabPage(this.des)) {
                    pX++;
                    pY++;
                }


                if (typeof (this.base.style.posTop) !== "undefined") //es IE 9
                {
                    this.base.style.posLeft = pX;
                    this.base.style.posTop = pY - document.getElementsByClassName("contenidoPantalla")[0].getBoundingClientRect().top;
                    this.base.style.posHeight = this.view.style.posHeight = (alto + 2 * CB_Borde + CB_Scroll);
                    this.base.style.posWidth = this.view.style.posWidth = this.des.offsetWidth + ((this.view.scrollHeight == this.view.clientHeight) ? 0 : 16);
                } else {
                    this.base.style.left = +pX + "px";
                    this.base.style.top = pY - document.getElementsByClassName("contenidoPantalla")[0].getBoundingClientRect().top + "px";
                    this.base.style.height = this.view.style.height = (alto + 2 * CB_Borde + CB_Scroll) + "px";
                    this.base.style.width = this.view.style.width = this.des.offsetWidth + ((this.view.scrollHeight == this.view.clientHeight) ? 0 : 16) + "px";

                }
                //this.base.style.top    = pY-document.getElementsByClassName("contenidoPantalla")[0].getBoundingClientRect().top;

            }

//*******************************//
// Reinicia el item selecionado  //
//*******************************//
            function CB_restoreIndex() {
                this.selectedIndex = -1;
            }

            function CB_obligatorio(esObligatorio) {
                if (esObligatorio) {
                    if ('inputTextoObligatorio' != this.des.className) {
                        this.codigos.shift();
                        this.items.shift();
                        if (this.cod)
                            this.cod.className = 'inputTextoObligatorio';
                        this.des.className = 'inputTextoObligatorio';
                        if (this.selectedIndex > 0)
                            this.selectedIndex--;
                    } else {
                        return;
                    }
                } else {
                    if ('inputTextoObligatorio' == this.des.className) {
                        this.codigos = [""].concat(this.codigos);
                        this.items = [""].concat(this.items);
                        if (this.cod)
                            this.cod.className = 'inputTexto';
                        this.des.className = 'inputTexto';
                        if (this.selectedIndex >= 0)
                            this.selectedIndex++;
                    } else {
                        return;
                    }
                }
                var str = '';
                for (var i = 0; i < this.codigos.length; i++) {
                    this.i_codigos[this.codigos[i] + ''] = i;
                    str += '<DIV>' + ((this.items[i]) ? this.items[i] : '&nbsp;') + '</DIV>';
                }
                this.view.innerHTML = str;

                //this.selectedIndex = -1;
                //if(this.cod) this.cod.value = '';  
                //this.des.value = '';
                this.selectItem(this.selectedIndex);
                return;
            }

            function CB_addItems(listaCodigos, listaItems) {
                this.codigos = listaCodigos;
                this.items = listaItems;
                if (this.des.className.indexOf('inputTextoObligatorio') < 0) {
                    this.codigos = [""].concat(this.codigos);
                    this.items = [""].concat(this.items);
                } else if (this.codigos == null || this.codigos.length == 0) {
                    this.codigos = [""];
                    this.items = [""];
                }
                var str = '';
                for (var i = 0; i < this.codigos.length; i++) {
                    this.i_codigos[this.codigos[i] + ''] = i;

                    if (this.items[i]) {
                        var auxItem = (this.items[i]);
                        if (this.idioma > 1) {
                            if (auxItem.indexOf("|") > -1)
                                auxItem = auxItem.split("|")[1];
                        } else if (this.idioma == 1) {
                            auxItem = auxItem.split("|")[0];
                        }
                        this.items[i] = auxItem;
                    }
                    str += '<DIV>' + ((this.items[i]) ? this.items[i] : '&nbsp;') + '</DIV>';
                }
                this.view.innerHTML = str;
                this.selectedIndex = -1;
                return true;
            }


            function CB_addItems2(listaCodigos, listaItems, listaEstados) {
                this.codigos = listaCodigos;
                this.items = listaItems;
                this.estados = listaEstados;
                var estados = listaEstados;
                if (this.des.className.indexOf('inputTextoObligatorio') < 0) {
                    this.codigos = [""].concat(this.codigos);
                    this.items = [""].concat(this.items);
                    this.estados = [""].concat(this.estados);
                } else if (this.codigos == null || this.codigos.length == 0) {
                    this.codigos = [""];
                    this.items = [""];
                    this.estados = [""];
                }
                var str = '';
                for (var i = 0; i < this.codigos.length; i++) {
                    this.i_codigos[this.codigos[i] + ''] = i;
                    if (this.items[i]) {
                        var auxItem = (this.items[i]);
                        if (this.idioma > 1) {
                            if (auxItem.indexOf("|") > -1)
                                auxItem = auxItem.split("|")[1];
                        } else if (this.idioma == 1) {
                            auxItem = auxItem.split("|")[0];
                        }
                        this.items[i] = auxItem;
                    }
                    var est = estados[i];
                    if (this.estados[i] != "B") {
                        str += '<DIV>' + ((this.items[i]) ? this.items[i] : '&nbsp;') + '</DIV>';
                    } else {
                        str += '<DIV  class="xCDisabled">' + ((this.items[i]) ? this.items[i] : '&nbsp;') + '</DIV>';
                    }
                }
                this.view.innerHTML = str;
                this.selectedIndex = -1;
                return true;
            }

            function CB_clearItems() {
                this.addItems([""], [""]);
                if (this.cod)
                    this.cod.value = '';
                this.des.value = '';
            }

            function CB_selectItem(rowID) {
                arglen = arguments.length;
                var old = this.selectedIndex;
                var index = (arglen != 0) ? rowID : this.selectedIndex;
                if (this.selectedIndex >= 0 && this.selectedIndex < this.items.length) {
                    if (this.view.children[this.selectedIndex].className != 'xCDisabled') {
                        this.view.children[this.selectedIndex].className = 'xC';
                        /** ORIGINAL
                         this.view.children(this.selectedIndex).className = 'xC';	
                         */
                    } else {
                        var disabled = this.selectedIndex;
                    }
                }
                if (index >= 0 && index < this.items.length && this.view.children[index].className != 'xCDisabled') {
                    //this.view.children(index).className = 'xCSelected';
                    this.view.children[index].className = 'xCSelected';
                    this.selectedIndex = index;
                    this.scroll();
                    if (this.cod)
                        this.cod.value = this.codigos[index];
                    this.des.value = this.items[index];
                } else if (index >= 0 && this.view.children[index].className == 'xCDisabled') {
                    if (old > 0) {
                        this.selectedIndex = old;
                        this.scroll();
                        if (this.cod)
                            this.cod.value = this.codigos[old];
                        this.des.value = this.items[old];
                    } else {
                        this.selectedIndex = -1;
                        if (this.cod)
                            this.cod.value = "";
                        this.des.value = "";
                    }
                    if (this.view.children[this.selectedIndex].className != 'xCDisabled') {
                        this.view.children[this.selectedIndex].className = 'xCSelected';
                    }
                } else {
                    if (index < 0) {
                        this.selectedIndex = -1;
                    } else if (index >= this.items.length)
                        this.selectedIndex = this.items.length;
                    if (this.cod)
                        this.cod.value = "";
                    this.des.value = "";
                }
                if (this.selectedIndex != old && this.base.style.display != '' && this.selectedIndex != disabled)
                    this.change();
            }

            function CB_scroll() {

                /** ORIGINAL
                 var selRow = this.view.children(this.selectedIndex);	
                 */
                var selRow = this.view.children[this.selectedIndex];
                var selDiv = this.view;

                if (selRow.offsetTop < selDiv.scrollTop)
                    selDiv.scrollTop = selRow.offsetTop;
                else if (selRow.offsetTop > (selDiv.scrollTop + selDiv.clientHeight - selRow.offsetHeight))
                    selDiv.scrollTop = (selRow.offsetTop - selDiv.clientHeight + selRow.offsetHeight);
            }

            function CB_activate() {
                var clase = new Array();
                if (this.cod) {
                    clase = this.cod.className.split(" ");
                    if (clase[clase.length - 1] == "inputTextoDeshabilitado") {
                        this.cod.disabled = false;
                        CB_removeClass(this.cod);
                    }
                }

                clase = this.des.className.split(" ");
                if (clase[clase.length - 1] == "inputTextoDeshabilitado") {
                    this.des.disabled = false;
                    CB_removeClass(this.des);
                }

                if (this.anchor) {
                    this.anchor.disabled = false;
                    this.anchor.onclick = function () {
                        this.combo.display();
                        return false;
                    };
                }

                if (this.boton) {
                    this.boton.style.cursor = 'hand';
                    this.boton.className = this.boton.className.replace(new RegExp('(?:^|\\s)' + 'faDeshabilitado' + '(?:\\s|$)'), "");
                }
            }

            function CB_deactivate() {
                var clase = new Array();
                if (this.cod) {
                    clase = this.cod.className.split(" ");
                    if (clase[clase.length - 1] != "inputTextoDeshabilitado") {
                        this.cod.disabled = true;
                        this.cod.className += " inputTextoDeshabilitado";
                    }
                }

                clase = this.des.className.split(" ");
                if (clase[clase.length - 1] != "inputTextoDeshabilitado") {
                    this.des.disabled = true;
                    this.des.className += " inputTextoDeshabilitado";
                }

                if (this.anchor) {
                    this.anchor.disabled = true;
                    this.anchor.onclick = function () {
                        return false;
                    };
                }

                if (this.boton) {
                    this.boton.style.cursor = 'default';
                    if (this.boton.className.indexOf("faDeshabilitado") < 0)
                        this.boton.className += " faDeshabilitado";
                }
            }

            function CB_removeClass(ele) {
                var clase = ele.className.split(" ");
                if (clase.length > 1) {
                    ele.className = "";
                    for (i = 0; i < clase.length - 1; i++) {
                        if (i == 0)
                            ele.className += clase[i];
                        else
                            ele.className += " " + clase[i];
                    }
                }
            }

            function CB_contieneOperadoresConsulta(campo) {
                var contiene = false;
                var v = campo.value;
                for (i = 0; i < v.length; i++) {
                    var c = v.charAt(i);
                    if (operadorConsulta.indexOf(c) != -1)
                        contiene = true;
                }
                return contiene;
            }

///////////////////////////////////////////////////////////////////////////////////////////////////////
// FIN OBJETO COMBO
///////////////////////////////////////////////////////////////////////////////////////////////////////
            function getOffsetLeft(el) {
                var ol = el.offsetLeft;
                while ((el = el.offsetParent) != null)
                    ol += el.offsetLeft;
                return ol;
            }

            function getOffsetTop(el) {
                var ot = el.offsetTop;
                while ((el = el.offsetParent) != null)
                    ot += el.offsetTop;
                return ot;
            }

            function isTabPage(el) {
                var pane = false;
                while ((el = el.parentElement) != null) {
                    if (el.className == 'tab-page')
                        pane = true;
                }
                return pane;
            }

            function CB_addElement(lista, elemento) {
                var i = lista.length;
                lista[i] = elemento;
            }

            function CB_deleteElement(lista, index) {
                if (index < 0 || index >= lista.length)
                    return null;
                var val = lista[index];
                var i, j;
                for (i = eval(index); i < (lista.length - 1); i++) {
                    j = i + 1;
                    lista[i] = lista[j];
                }
                lista.length--;
                return val;
            }

            function CB_clearSelected() {
                this.buscaLinea(-1);

                if (this.items) {
                    for (var i = 0; i < this.items.length; i++) {
                        this.view.children[i].className = '';
                    }
                }

                return true;
            }


        </script>

        <script type="text/javascript">
            var comboTipoDoc;
            var tablaMetadatos;
            var codTipDoc;
            var descTipDoc;

            var descTipDocEus;
            var descTipDocLCas;
            var descTipDocLEus;

            var codGrupoTipDocu;

            var codigoNuevo;
            codTipDoc = new Array();
            descTipDoc = new Array();

            descTipDocEus = new Array();
            descTipDocLCas = new Array();
            descTipDocLEus = new Array();

            codigoNuevo = new Array();

            codGrupoTipDocu = new Array();

            var argVentana;
            var botoneras = new Array();
            var listaCatalogacion;
            var listaDocumentos = [];
            var listaDocumentosOriginal = [];
            var datosRegistro = [];
            var documentoSeleccionado;
            var idDocumentoSel = -1;
            var listadoMetadatos = [];
            var urlDocumento;

            //Grupos
            var codGrupoTipDoc;
            var descGrupoTipDocES;
            var descGrupoTipDocEU;
            codGrupoTipDoc = new Array();
            descGrupoTipDocES = new Array();
            descGrupoTipDocEU = new Array();

            function inicializar() {
                cargarTiposDocumentales();
                cargarGruposTiposDocumentales();
                tablaMetadatos.displayTabla();
                cargarTablaDocs();
                $(".dataTables_filter").hide();
                $(".dataTables_scrollBody").css("border-bottom", "none");

                refrescarCodigoNuevo();
            }

            function cargarTablaDocs() {

                var cont = 0;
                <logic:present scope="request" name="listaDocumentos">
                    <logic:iterate id="meta" name="listaDocumentos">
                        var doc=[];
                        doc.idDocumento='<bean:write name="meta" property="idDocumento"/>';
                        doc.nombre='<bean:write name="meta" property="nombre"/>';
                        doc.catalogado='<bean:write name="meta" property="catalogado"/>';
                        doc.unidadOrg='<bean:write name="meta" property="unidadOrg"/>';
                        doc.fecha='<bean:write name="meta" property="fecha"/>';
                        doc.extension='<bean:write name="meta" property="extension"/>';
                        doc.tipoDocumental='<bean:write name="meta" property="tipoDocumental"/>';
                        doc.observDoc='<bean:write name="meta" property="observDoc"/>';

                        listaDocumentosOriginal[cont]=doc;

                        listaDocumentos[cont++] = ['<bean:write name="meta" property="nombre"/>','<bean:write name="meta" property="tipoDocumental"/>', '<bean:write name="meta" property="catalogado"/>'];

                    </logic:iterate>
                </logic:present>

                <logic:present scope="request" name="datosAnotacion">
                    datosRegistro.ano='<bean:write name="datosAnotacion" property="anoReg"/>';
                    datosRegistro.numero= '<bean:write name="datosAnotacion" property="numReg"/>';
                    datosRegistro.tipo = '<bean:write name="datosAnotacion" property="tipoReg"/>';
                    datosRegistro.departamento =  '<bean:write name="datosAnotacion" property="identDepart"/>';
                    datosRegistro.uorReg =  '<bean:write name="datosAnotacion" property="unidadOrgan"/>';
                    datosRegistro.tipoRegOrigen = '<bean:write name="datosAnotacion" property="tipoRegOrigen"/>';
                    datosRegistro.codUORDest='<bean:write name="datosAnotacion" property="codUORDestBD"/>';
                </logic:present>
                tabDocs.lineas = listaDocumentos;
                tabDocs.displayTabla();
                $(".dataTables_scrollBody").css("border-bottom", "none");
                documentoSeleccionado = '<%=request.getAttribute("documentoSeleccionado")%>';
                // Deberia haber algo aqui como 
                //idDocumentoSel='< %=request.getAttribute("idDocumentoSel")%>';
                cargarDocumento(false);

            }

            function sustituirCaracteres(texto, encontrar, nuevo) {
                var cadena = '';
                var i = 0;
                while (i < texto.length) {
                    if (texto[i] != encontrar)// Si el carácter de la cadena es igual al carácter a remplazar
                    {
                        // si es diferente simplemente concatena el carácter original de la cadena original.
                        cadena = cadena + texto[i];
                    } else {
                        // si no es diferente concatena el carácter que introdujiste a remplazar
                        cadena = cadena + nuevo;
                    }
                    i++;
                } // Fin del while
                return cadena;
            }

            function cargarDocumento(inicializarMetadatos) {
                pleaseWait1('on',this);
                var fechaDoc;
                var extensionDoc;
                var observDoc;
                for (var ijk = 0; ijk < listaDocumentosOriginal.length; ijk++) {
                    if (listaDocumentosOriginal[ijk].nombre == documentoSeleccionado) {
                        fechaDoc = listaDocumentosOriginal[ijk].fecha;
                        extensionDoc = listaDocumentosOriginal[ijk].extension;
                        idDocumentoSel = listaDocumentosOriginal[ijk].idDocumento;
                        observDoc = listaDocumentosOriginal[ijk].observDoc;
                        var observDocN = sustituirCaracteres(observDoc, '|', '\n');

                        $('#observDoc').val(observDocN);
                        if (listaDocumentosOriginal[ijk].catalogado === 'SI') {
                            if (inicializarMetadatos) {
                                mostrarCatalogacion(false);
                            } else {
                                mostrarCatalogacion(true);
                            }
                            mostrarBotonera(2);

                        } else {
                            $('#codNuevo').val('');
                            $('#codTipoDoc').val('');
                            setTimeout(function () {
                                $('#descTipoDoc').val(' ');
                            }, 250);

                            mostrarBotonera(1);
                            listadoMetadatos = "";
                            comboTipoDoc.change = cargarMetadatos;
                            comboTipoDoc.buscaItem("");
                            tablaMetadatos.lineas = "";
                            cargarTiposDocumentales();
                            tablaMetadatos.displayTabla();
                            $(".dataTables_filter").hide();
                            $(".dataTables_scrollBody").css("border-bottom", "none");

                        }

                        tabDocs.selectLinea(ijk);
                        break;
                    }
                }

                urlDocumento = "<%=request.getContextPath()%>/VerDocumentoAnexo?codigo=" + fechaDoc
                        + "&nombreFichero=" + documentoSeleccionado
                        + "&ejercicio=" + datosRegistro.ano
                        + "&numero=" + datosRegistro.numero
                        + "&uor=" + datosRegistro.codUORDest
                        + "&tipoReg=" + datosRegistro.tipoRegOrigen
                        + "&fechaFichero=" + fechaDoc
                        + "&extensionFichero=" + extensionDoc
                        + "&tipoRegistro=" + datosRegistro.tipo
                        + "&codUorRegistro=" + datosRegistro.uorReg
                        + "&codDepartamento=" + datosRegistro.departamento;

                var botonesDoc = new Array(document.getElementById('cmdAbrirDocumento'), document.getElementById("cmdTipificar"));
                habilitarGeneral(botonesDoc);

                $('#visorDocumento').remove();

                //si el documento es pdf se muestra en el visor
                if (extensionDoc === 'application/pdf') {

                    //$('#capaDocumentos').append("<iframe id='visorDocumento' style='width: 100%;height: 715px;'>Vista previa no disponible</iframe>");
                    $('#capaDocumentos').append("<iframe id='visorDocumento' style='width: 100%;height: 753px;'>Vista previa no disponible</iframe>");

                    $('#visorDocumento').prop('src', urlDocumento + "&embedded=true");
                    setTimeout(function () {
                        pleaseWait1('off',this);
                    }, 1200);


                } else {
                    if (extensionDoc == null || extensionDoc == '') {
                        deshabilitarGeneral(botonesDoc);

                    }

                    //$('#capaDocumentos').append('<div id="visorDocumento" class="visornodisponible">Vista previa no disponible</div>');
                    // mostrar el texto: El documento no está en formato PDF. Para abrirlo pulse el botón "Abrir Documento".
                    $('#capaDocumentos').append('<div id="visorDocumento" class="visornodisponible"><%=descriptor.getDescripcion("msgNoPDF")%></div>');
                    pleaseWait1('off',this);
                }

            }

            function cargarTiposDocumentales() {
                var cont = 0;
                <logic:present scope="request" name="listaTiposDocumentales">
                            <logic:iterate id="tipoDoc" name="listaTiposDocumentales">
                        codTipDoc[cont] = '<bean:write name="tipoDoc" property="identificador"/>'; 
                        descTipDoc[cont] = '<bean:write name="tipoDoc" property="descripcion"/>';
                            
                            descTipDocEus[cont] = '<bean:write name="tipoDoc" property="otraDesc"/>';
                        descTipDocLCas[cont] = '<bean:write name="tipoDoc" property="descripcionLargaCAS"/>';
                        descTipDocLEus[cont] = '<bean:write name="tipoDoc" property="descripcionLargaEUS"/>';
                        
                        codGrupoTipDocu[cont] = '<bean:write name="tipoDoc" property="codGrupo"/>';
                        
                        codigoNuevo[cont++] = '<bean:write name="tipoDoc" property="codigoNuevo"/>';
                        </logic:iterate>
                    </logic:present>
                //si no hay tipos documentales asociados al procedimiento se cargan todos por defecto (se cambia, ahora sólo los del procedimiento)
                if (codTipDoc.length > 0) {
                    comboTipoDoc.addItems(codTipDoc, descTipDoc);
                } /*else {
                    pulsarCargarTodosTipos();
                }*/
            }

            function cargarGruposTiposDocumentales() {
                var cont = 0;
                <logic:present scope="request" name="listaGruposTiposDocumentales">
                        <logic:iterate id="grupoTipoDoc" name="listaGruposTiposDocumentales">
                        codGrupoTipDoc[cont] = '<bean:write name="grupoTipoDoc" property="codGrupo"/>'; 
                        descGrupoTipDocES[cont] = '<bean:write name="grupoTipoDoc" property="descGrupo_es"/>';
                        descGrupoTipDocEU[cont++] = '<bean:write name="grupoTipoDoc" property="descGrupo_eu"/>';
                </logic:iterate>
                </logic:present>
            }

            function cargarMetadatos(codigo) {
                if (comboTipoDoc.selectedIndex > 0 || codigo) {

                    if (!codigo)
                        codigo = $('#codTipoDoc').val();
                    try {
                        $.ajax({
                            url: '<c:url value="/registro/digitalizacionDocumentosLanbide.do"/>',
                            type: 'POST',
                            async: true,
                            data: {'opcion': 'recuperarMetadosPorTipoDoc', 'codTipoDoc': codigo},
                            success: procesarRespuestaCargarMetadatos,
                            error: mostrarErrorRespuestaCargarMetadatos
                        });
                    } catch (Err) {
                        pleaseWait1('off',this);
                        mostrarError('2');
                    }
                }
            }


            function pulsarTipificar() {
                var tipDocSel = $('#codTipoDoc').val();
                if (tipDocSel == "") {
                    mostrarError('3');
                } else {
                    // Hay un tipo documental seleccionado en el desplegable
                    var inputs = obtenerTodosElementosTabla('vals');
                    var checkboxes = obtenerTodosElementosTabla('checks');
                    var listaMetadatosSel = "";
                    var existeObligSinVal = false;

                    $.each(inputs, function (index) {
                        var valor = $(this).val();
                        if ($(checkboxes[index]).is(':checked') && valor == '') {
                            existeObligSinVal = true;
                            return false;
                        }
                        if (valor != '') {
                            listaMetadatosSel += $(checkboxes[index]).val() + ";" + escape(valor) + '<%=separador%>';
                        }
                    });
                    if (existeObligSinVal) {
                        mostrarError('5');
                    } else {
                        var retorno;
                        if (listaMetadatosSel.length == 0 && tipDocSel !== "") {
                            retorno = {"tipoDocumental": tipDocSel};
                        } else {
                            // Todos los metadatos seleccionados tienen valor
                            retorno = {"catalogacion": listaMetadatosSel};
                        }
                        grabarCatalogacionDoc(retorno);
                    }
                }
            }

            function grabarCatalogacionDoc(datosDoc) {
                datosDoc.nombreDoc = documentoSeleccionado;
                datosDoc.idDoc = idDocumentoSel;
                datosDoc.observDoc = $('#observDoc').val();
                datosDoc.ejercicio = datosRegistro.ano;
                datosDoc.numero = datosRegistro.numero;
                datosDoc.tipo = datosRegistro.tipo;
                datosDoc.uorDoc = datosRegistro.uorReg;
                datosDoc.tipoRegOrigen = datosRegistro.tipoRegOrigen;
                datosDoc.codUorDestino = datosRegistro.codUORDest;
                datosDoc.opcion = "grabarCatalogacionDocumento";

                var url = getUrlDigitalizacion();
                pleaseWait1('on',this);

                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: datosDoc,
                        success: procesarRespuestaGrabarCatalogacionDoc,
                        error: mostrarErrorRespuestaGrabarCatalogacionDoc
                    });
                } catch (Err) {
                    pleaseWait1('off',this);
                    mostrarError('2');
                }
            }


            function procesarRespuestaGrabarCatalogacionDoc(ajaxResult) {
                //Se comenta el pleaseWait porque dentro de la petición ajax el objeto this es el propio objeto petición y esto provoca que falle
                //pleaseWait1('off',this);
                if (ajaxResult) {
                    var resp = JSON.parse(ajaxResult);
                    var listadoDocs = resp.tabla.listaDocumentos;
                    listadoMetadatos = resp.tabla.datosCatalogacionDoc;
                    var error = resp.tabla.resultadoOp;
                    if (error != "0") {
                        mostrarError(error);
                    } else {
                        for (var i = 0; i < listadoDocs.length; i++) {
                            listaDocumentos[i][0] = listadoDocs[i].nombre;
                            listaDocumentos[i][1] = listadoDocs[i].tipoDocumental;
                            listaDocumentos[i][2] = listadoDocs[i].catalogado;

                            listaDocumentosOriginal[i].nombre = listadoDocs[i].nombre;
                            listaDocumentosOriginal[i].idDocumento = listadoDocs[i].idDocumento;
                            listaDocumentosOriginal[i].extension = listadoDocs[i].extension;
                            listaDocumentosOriginal[i].catalogado = listadoDocs[i].catalogado;
                            listaDocumentosOriginal[i].fecha = listadoDocs[i].fecha;
                            listaDocumentosOriginal[i].digitalizado = listadoDocs[i].digitalizado;
                            listaDocumentosOriginal[i].tipoDocumental = listadoDocs[i].tipoDocumental;
                            listaDocumentosOriginal[i].observDoc = listadoDocs[i].observDoc;
                        }
                        documentoSeleccionado = resp.tabla.documentoSeleccionado;
                        //// Deberia venir algo como
                        //idDocumentoSel = resp.tabla.idDocumentoSel;
                        //Actualizamos el ID de el documento seleccionado - Se deberia pasar desde Action (respuesta.setAtributo("idDocumentoSel",docCatalogar.getIdDocumento());), 
                        // pero ahora no puedo tocar ese codigo 04/08/2018 hay muchos cambios pendiente por subir a pro
                        for (var k = 0; k < listaDocumentosOriginal.length; k++) {
                            if (documentoSeleccionado == listaDocumentosOriginal[k].nombre) {
                                idDocumentoSel = listaDocumentosOriginal[k].idDocumento;
                            }
                        }

                        tabDocs.lineas = listaDocumentos;
                        tabDocs.displayTabla();

                        $(".dataTables_filter").hide();
                        $(".dataTables_scrollBody").css("border-bottom", "none");

                        if (listadoMetadatos != null) {
                            cargarDocumento(true);
                        } else {
                            cargarDocumento(false);
                        }
                    }
                }
            }

            function mostrarErrorRespuestaGrabarCatalogacionDoc() {
                pleaseWait1('off',this);
                mostrarError('10');
            }


            function mostrarCatalogacion(inicializar) {
                var listaMetadatos = new Array();
                var metadatos;
                var tipDocId;
                var tipDocDesc;
                if (inicializar) {
                    recuperarDatosCatalogacion();

                    metadatos = listaCatalogacion;
                    tipDocId = metadatos[0].tipDocId;
                    tipDocDesc = metadatos[0].tipDocDesc;

                    for (var index = 0; index < metadatos.length; index++) {
                        var meta = metadatos[index];
                        if (meta.metadatoId != null && meta.metadatoId != "") {
                            listaMetadatos[index] = [(meta.metadatoOblig == 1 ? "SI" : "NO"), meta.metadatoId, meta.metadatoValor];
                        }
                    }
                } else {

                    listaCatalogacion = listadoMetadatos;

                    tipDocId = listaCatalogacion[0].tipoDocumental.identificador;
                    tipDocDesc = listaCatalogacion[0].tipoDocumental.descripcion;
                    comboTipoDoc.change = changeCombo;

                    for (var i = 0; i < listaCatalogacion.length; i++) {
                        var meta = listaCatalogacion[i].metadato;
                        if (meta.idMetadato != null && meta.idMetadato != "") {
                            listaMetadatos[i] = [(meta.obligatorio == 1 ? "SI" : "NO"), unescape(meta.idMetadato), unescape(meta.valorMetadato)];
                        }
                    }
                }

                comboTipoDoc.addItems(tipDocId, tipDocDesc);
                comboTipoDoc.buscaCodigo(tipDocId);

                comboTipoDoc.deactivate();
                $('#codNuevo').prop('disabled', true);
                //$("#cmdTodosTipos").prop('disabled', true);
                $("#cmdBuscarTipos").prop('disabled', true);

                // mostramos la tabla con los metadatos recibidos
                $('#tablaMetadatos').hide();
                mostrarTabla(listaMetadatos);

            }

            function recuperarDatosCatalogacion() {
                var metadatos = new Array();
                var cont = 0;
                        <logic:present scope="request" name="datosCatalogacionDoc">
                            <logic:iterate id="meta" name="datosCatalogacionDoc">
                            metadatos[cont++] = {
                                "codigo" : '<bean:write name="meta" property="identificador"/>',
                                "tipDocId" : '<bean:write name="meta" property="tipDocId"/>',
                                "tipDocDesc" : '<bean:write name="meta" property="tipDocDesc"/>',
                                "metadatoId" : '<bean:write name="meta" property="metadatoId"/>',
                                "metadatoOblig" : '<bean:write name="meta" property="metadatoOblig"/>',
                                "metadatoValor" : '<bean:write name="meta" property="metadatoValor"/>'
                            };
                            </logic:iterate>
                        </logic:present>

                listaCatalogacion = metadatos;
            }

            function pulsarModificar() {
                mostrarBotonera(1);

                // Guardamos los datos de la tabla antes de cargar los nuevos
                var listaMetadatos = tablaMetadatos.lineas;
                comboTipoDoc.change = cargarMetadatos;
                comboTipoDoc.activate();
                $('#codNuevo').prop('disabled', false);
                //$("#cmdTodosTipos").prop('disabled', false);
                $("#cmdBuscarTipos").prop('disabled', false);
                cargarTiposDocumentales();
                // Recuperamos el tipo documental del documento y cargamos los metadatos asociados
                var codTipoDocSel = $('#codTipoDoc').val();
                cargarMetadatos(codTipoDocSel);

                setTimeout(function () { //Esperamos 2 miliseg
                    //Recuperamos los datos de la tabla despues de cargarMetadatos()
                    var nuevaLista = tablaMetadatos.lineas;
                    var inputs = obtenerTodosElementosTabla('vals');
                    if (nuevaLista === listaMetadatos) {

                    } else {
                        $.each(listaMetadatos, function (index, item) {
                            var idMeta = item[1];
                            var valMeta = item[2];
                            $.each(nuevaLista, function (fila, el) {
                                var valorId = 'valormetadato_' + fila;
                                if (el[1] == idMeta) {
                                    $.each(inputs, function (i, input) {
                                        if (input.id == valorId) {
                                            $(input).prop("value", valMeta);
                                        }
                                    });
                                }
                            });
                        });
                    }
                }, 250);
            }

            function cargarMetadatosDocumento() {
                var datos = {};
                datos.opcion = 'recuperarMetadatosDocumento';
                datos.nombreDoc = documentoSeleccionado;
                datos.ejercicio = datosRegistro.ano;
                datos.numero = datosRegistro.numero;
                datos.tipo = datosRegistro.tipo;
                datos.uorDoc = datosRegistro.uorReg;

                var url = getUrlDigitalizacion();
                 
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: datos,
                        success: procesarRespuestaCargarMetadatosDocumento,
                        error: mostrarErrorRespuestaCargarMetadatosDocumento
                    });
                } catch (Err) {
                    pleaseWait1('off',this);
                    mostrarError(2);
                }
            }



            function procesarRespuestaCargarMetadatosDocumento(ajaxResult) {
                //Se comenta el pleaseWait porque dentro de la petición ajax el objeto this es el propio objeto petición y esto provoca que falle
                //pleaseWait1('off',this);
                if (ajaxResult) {
                    var resp = JSON.parse(ajaxResult);
                    resp = resp.tabla;
                    var codError = resp.resultadoOp;
                    if (codError == '0') {
                        listadoMetadatos = resp.datosCatalogacionDoc;
                    } else {
                        mostrarError(codError);
                    }
                } else {
                    listadoMetadatos = '';
                }

            }


            function mostrarErrorRespuestaCargarMetadatosDocumento() {
                pleaseWait1('off',this);
                mostrarError('2');
            }


            // FUNCIONES DE RESPUESTA AJAX
            function procesarRespuestaCargarMetadatos(result) {
                //Se comenta el pleaseWait porque dentro de la petición ajax el objeto this es el propio objeto petición y esto provoca que falle
                //pleaseWait1('off',this);
                if (result) {
                    var listaMetadatos = new Array();
                    var resp = JSON.parse(result);
                    resp = resp.tabla;
                    var codError = resp.codError;
                    var datos = resp.datos;
                    if (codError == '0') {
                        if (datos.length > 0) {
                            var metadato;
                            var checkVal;
                            var check;
                            var valor;
                            for (var cont = 0; cont < datos.length; cont++) {
                                metadato = datos[cont];
                                checkVal = metadato.obligatorio ? "checked disabled" : "disabled";
                                check = "<input type='checkbox' class='checkMetadato' id='metadato_" + cont + "' value='" + metadato.idTipoDoc + ";" + escape(metadato.idMetadato) + "' " + checkVal + "/>";
                                valor = "<input type='text' class='valMetadato' name='valormetadato_" + cont + "' id='valormetadato_" + cont + "' />";
                                listaMetadatos[cont] = [check, metadato.idMetadato, valor];
                            }
                        }
                        mostrarTabla(listaMetadatos);
                        // Detecta el evento click de todo los input text y evita que se ejecute el evento click del td para que el input no pierda el foco
                        var inputs = obtenerTodosElementosTabla('vals');
                        $.each(inputs, function () {
                            $(this).click(function (event) {
                                event.stopPropagation();
                            });
                        });
                    } else
                        mostrarError(codError);
                } else
                    mostrarError('2');
            }

            function mostrarErrorRespuestaCargarMetadatos() {
                mostrarError('2');
            }

            // OTRAS FUNCIONES

            function mostrarBotonera(codMostrar) {
                if (codMostrar === 1) {
                    $('#cmdTipificar').show();
                    $('#cmdModificar').hide();
                    comboTipoDoc.activate();
                    $('#codNuevo').prop('disabled', false);
                    //$("#cmdTodosTipos").prop('disabled', false);
                    $("#cmdBuscarTipos").prop('disabled', false);
                    $('#observDoc').prop('disabled', false);
                } else if (codMostrar === 2) {
                    $('#cmdTipificar').hide();
                    $('#cmdModificar').show();
                    comboTipoDoc.deactivate();
                    $('#codNuevo').prop('disabled', true);
                    //$("#cmdTodosTipos").prop('disabled', true);
                    $("#cmdBuscarTipos").prop('disabled', true);
                    $('#observDoc').prop('disabled', true);
                }
            }


            function mostrarError(codigo) {
                if (codigo == "2") {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("msgErrGenServ")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("msgTipoDocumental")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("msgMetadatosOblig")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("msgIndicarMetadatos")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("errorBBDD")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("errorCatalogacion")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("errorTipDoc")%>');
                } else if (codigo == "9") {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("errorMetadatos")%>');
                } else if (codigo == "10") {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("errorTipificar")%>');
                } else if (codigo == "11") {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("errorConExt")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=descriptor.getDescripcion("errorOper")%>');
                }
            }


            function obtenerTodosElementosTabla(tipo) {
                var datatable = tablaMetadatos.dataTable;
                var elementos = new Array();
                if (tipo == 'vals') {
                    elementos = $(".valMetadato", datatable.rows().nodes());
                } else if (tipo == 'checks') {
                    elementos = $(".checkMetadato", datatable.rows().nodes());
                }
                return elementos;
            }

            function pulsarCargarTodosTipos(isAsync) {
				if(isAsync!==false) isAsync = true;
                try {
                    $('#codTipoDoc').val('');
                    $('#descTipoDoc').val(' ');
                    $('#codNuevo').val('');
                    $.ajax({
                        url: APP_CONTEXT_PATH + "/registro/digitalizacionDocumentosLanbide.do",
                        type: 'POST',
                        async: isAsync,
                        data: {'opcion': 'cargarTodosTipos'},
                        success: procesarRespuestaCargarTodosTipos,
                        error: mostrarErrorRespuestaCargarTodosTipos
                    });
                } catch (Err) {
                    mostrarError("2");
                }
            }
            
            function pulsarCargarTiposProcedimiento(isAsync) {
				if(isAsync!==false) isAsync = true;
                try {
                    $('#codTipoDoc').val('');
                    $('#descTipoDoc').val(' ');
                    $('#codNuevo').val('');
                    
                    var ejercicio = datosRegistro.ano;
                    var nRegistro = datosRegistro.numero;
                    
                    $.ajax({
                        url: APP_CONTEXT_PATH + "/registro/digitalizacionDocumentosLanbide.do",
                        type: 'POST',
                        async: isAsync,
                        data: {'ejercicio': ejercicio,
                               'nRegistro': nRegistro,
                               'opcion': 'cargarTiposProcedimiento'},
                        success: procesarRespuestaCargarTodosTipos,
                        error: mostrarErrorRespuestaCargarTodosTipos
                    });
                } catch (Err) {
                    mostrarError("2");
                }
            }


            function procesarRespuestaCargarTodosTipos(ajaxResult) {
                if (ajaxResult) {
                    var resp = JSON.parse(ajaxResult);
                    resp = resp.tabla;
                    var codError = resp.codError;
                    var datos = resp.datos;
                    if (codError == '0') {
                        codTipDoc = new Array();
                        descTipDoc = new Array();

                        descTipDocEus = new Array();
                        descTipDocLCas = new Array();
                        descTipDocLEus = new Array();

                        codigoNuevo = new Array();
                        for (var cont = 0; cont < datos.length; cont++) {
                            codTipDoc[cont] = datos[cont].identificador;
                            descTipDoc[cont] = datos[cont].descripcion;

                            descTipDocEus[cont] = datos[cont].otraDesc;
                            descTipDocLCas[cont] = datos[cont].descripcionLargaCAS;
                            descTipDocLEus[cont] = datos[cont].descripcionLargaEUS;

                            codGrupoTipDocu[cont] = datos[cont].codGrupo;

                            codigoNuevo[cont] = datos[cont].codigoNuevo;
                        }
                        comboTipoDoc.clearItems();
                        comboTipoDoc.addItems(codTipDoc, descTipDoc);
                    } else {
                        mostrarError(codError);
                    }

                } else {
                    mostrarError('2');
                }
            }

            function mostrarErrorRespuestaCargarTodosTipos() {
                mostrarError(2);
            }

            function pulsarAbrirDocumento() {
                window.open(urlDocumento + "&embedded=true", "ventana1", "left=10, top=10, width=850, height=800, scrollbars=no, menubar=no, location=no, resizable=no");
            }

            function pulsarSalir() {
                self.parent.opener.retornoXanelaAuxiliar(listaDocumentosOriginal);
            }

            function changeCombo() {
            }

            // Funcion redefinida para procesar las pulsaciones sobre la tabla tablaDocs (evento onClick) 
            function selectRow(rowID, tab) {
                if (tab.id == tabDocs.id) {
                    documentoSeleccionado = listaDocumentosOriginal[rowID].nombre;
                    idDocumentoSel = listaDocumentosOriginal[rowID].idDocumento;
                    cargarMetadatosDocumento();

                    setTimeout(function () {
                        cargarDocumento(true);
                    }, 500);


                } else {
                    tab.tabla.selectLinea(rowID);
                }
                callClick(rowID, tab.tabla);
                return false;
            }

            function refrescarIdTipDoc() {
                var valorCodigoNuevo = $('#codNuevo').val();
                var ordenCodigoNuevo = codigoNuevo.indexOf(valorCodigoNuevo);
                if (ordenCodigoNuevo !== -1) {
                    var valorIdTipDoc = codTipDoc[ordenCodigoNuevo];
                    var valorDescTipDoc = descTipDoc[ordenCodigoNuevo];
                    $('#codTipoDoc').val(valorIdTipDoc);
                    $('#descTipoDoc').val(valorDescTipDoc);
                    cargarMetadatos(valorIdTipDoc);
                } else {
                    $('#codNuevo').val('');
                    $('#codTipoDoc').val('');
                    $('#descTipoDoc').val(' ');
                    jsp_alerta('A', 'Código inexistente');
                }

            }

            function refrescarCodigoNuevo() {
                if (!$("#codNuevo").is(":focus")) {
                    var valorId = $('#codTipoDoc').val();
                    //var ordenId = codTipDoc.indexOf(valorId);
                    var ordenId = -1;
                    for (var i = 0; i < codTipDoc.length; i++) {
                        if (codTipDoc[i] == valorId) {
                            ordenId = i;
                            break;
                        }
                    }

                    if (valorId != '' && ordenId > -1) {
                        var valorCodigoNuevo = codigoNuevo[ordenId];
                        $('#codNuevo').val(valorCodigoNuevo);
                    } else if (valorId == '') {
                        $('#codNuevo').val('');
                    }
                }
            }

            $(document).ready(function () {
                setInterval(function () {
                    refrescarCodigoNuevo();
                }, 100);
                $('.xC').mouseup(function () {
                    setTimeout(function () {
                        refrescarCodigoNuevo();
                    }, 100);
                });
            });



            function pulsarBuscarTipos() {
				// Indicamos con el parametro que la llamada ajax para cargar los valores debe ser sincrona
				//pulsarCargarTodosTipos(false);
                                pulsarCargarTiposProcedimiento(false);
				
				var argumentos = new Array();

				argumentos[0] = codTipDoc;
				argumentos[1] = codigoNuevo;
				if ('<%=idioma%>' === '1') {
					argumentos[2] = descTipDoc;
					argumentos[3] = descTipDocLCas;
				} else {
					argumentos[2] = descTipDocEus;
					argumentos[3] = descTipDocLEus;
				}
				argumentos[4] = codGrupoTipDocu;

				//Grupos
				argumentos[5] = codGrupoTipDoc;
				if ('<%=idioma%>' === '1') {
					argumentos[6] = descGrupoTipDocES;
				} else {
					argumentos[6] = descGrupoTipDocEU;
				}


				var source = "<%=request.getContextPath()%>/jsp/digitalizacion/pantallaTiposDocumentales.jsp?opcion=";
				var url = "<%=request.getContextPath()%>/jsp/digitalizacion/mainVentana.jsp?source=" + source;

				abrirXanelaAuxiliar(url, argumentos, 'width=973,height=685,status=no', function (tipo) {
					//seleccionar el tipo documental en el combo
					if (tipo!=undefined && tipo.length == 3) {
						$('#codTipoDoc').val(tipo[0]);
						$('#codNuevo').val(tipo[1]);
						$('#descTipoDoc').val(tipo[2]);

						cargarMetadatos(tipo[0]);

					}
				});
            }
            
            function verPDFAyudaCat(){	
				if ('<%=idioma%>' == 1 || '<%=idioma%>' == 4) {
					var sourc;
					if ('<%=idioma%>' == 1) {
						sourc = "<%=request.getContextPath()%>/jsp/sge/ver_pdf.jsp?fichero=/pdf/ayuda/catalogacion/pantallaCatalogacion_es.pdf";
					}else {
						sourc = "<%=request.getContextPath()%>/jsp/sge/ver_pdf.jsp?fichero=/pdf/ayuda/catalogacion/pantallaCatalogacion_eu.pdf";
					} 
					ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp?source="+sourc,'ventanaInforme','width=800px,height=550px,status='+ '<%=statusBar%>' + ',toolbar=no,resizable=1');
					ventanaInforme.focus();
				} else {
				  jsp_alerta('A','<%=descriptor.getDescripcion("msjNoPDF")%>');
				}
				
				
					
					  
            }
            
</script>
</head>

<body class="bandaBody" onload="inicializar();">

    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>


    <div id="titulo" class="txttitblanco" style="text-align: center;">
        <%=descriptor.getDescripcion("etiqCatalogacion")%>
        <a id="imAyudaCat" href="javascript:ayuda();" title="Ayuda">
            <span style="position: absolute; right: 20px; top: 10px; font-size: 30px !important; cursor: pointer;" class="fa fa-question" aria-hidden="true" id="botonAyudaCat" name="botonAyudaCat" onclick="verPDFAyudaCat()"></span>
        </a>
    </div>
    
    <div class="contenidoPantalla" style="height: 800px;">
        <div id="capaListadoDocumentos" style="width: 37.5%; margin-top:-5px; float:right">
            <div id="tablaDocs"></div>
        </div>
        <div id="capaDocumentos" style="width: 60%; display:inline-block; float: left; margin-top: -5px;">              
        </div>
        <div id="capaTipificar"> 
            <div style="margin-top: 5%;margin-left: 6%; width: 94%">
                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gEtiq_MasInfo")%>" name="cmdBuscarTipos" id="cmdBuscarTipos" onclick="pulsarBuscarTipos();" style="float:right; margin-top:5px;">
            </div>
            <div id="capaTipoDocumental" style="width: 94%; margin-top:2%; margin-left: 3%; clear: right;">
                <div>
                    <label style="margin-left: 3%;" class="etiqueta"><%=descriptor.getDescripcion("etiqTipoDocumental")%>:</label>
                    <!-- <span style="margin-left: 12%;">
                        <input type= "button" class="botonGeneral" value='<%//=descriptor.getDescripcion("gbTodosTipos")%>' name="cmdTodosTipos" id="cmdTodosTipos" onclick="pulsarCargarTodosTipos();"/>
                    </span> -->
                    <!-- <span style="position: absolute; right: 11px; top: 119px;">
                        <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gEtiq_MasInfo")%>' name="cmdBuscarTipos" id="cmdBuscarTipos" onclick="pulsarBuscarTipos();"/>
                    </span> -->
                </div>
                <div style="margin-top: 1%">
                    <span style="margin-left: 3%">
                        <!-- se mantiene codTipoDoc para el TIPDOC_ID y se añade codNuevo para el CODTIPDOC -->
                        <input type="hidden" name="codTipoDoc" id="codTipoDoc" />
                        <input type="text" name="codNuevo" id="codNuevo" class="inputTexto" style="width:7%" onchange="refrescarIdTipDoc()" />
                        <input type="text" class="inputTexto" name="descTipoDoc" id="descTipoDoc" style="width:84%" readonly="true" />
                        <a href="" id="anchorTipoDoc" name="anchorTipoDoc" style="text-decoration: none"> 
                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDoc" name="botonTipoDoc" style="cursor:hand;"></span> 
                        </a>
                    </span>
                </div>
            </div>

            <div id="capaMetadatos" style="margin-top: 5%;margin-left: 6%; width: 94%">
                <div style="max-height: 300px; margin-bottom: 2%;">
                    <label id="msgNoMetadatos" class="etiqueta" ><%=descriptor.getDescripcion("msgNoMetadatos")%></label>
                    <span id="tablaMetadatos"></span>
                </div>

                <label class="etiqueta" ><%=descriptor.getDescripcion("etiqObservDoc")%>:</label>
                <div>
                    <textarea id="observDoc" name="observDoc" rows="7" cols="80" class="inputTexto" style="text-transform: none;" ></textarea>
                </div>

                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("etiqTipificar")%>" name="cmdTipificar" id="cmdTipificar" onclick="pulsarTipificar();" style="float:right; margin-top:5px;">
                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificar" id="cmdModificar" onclick="pulsarModificar();" style="display:none;float:right;margin-top: 5px;"> 
            </div>
            <div id ="capaBotones" style=" margin-top: 20px; margin-left:2%; position:absolute; top:732px">
                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("etiqAbrirDoc")%>" name="cmdAbrirDocumento" id="cmdAbrirDocumento" onclick="pulsarAbrirDocumento();">
                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" namme=""cmdSalir id="cmdSalir" onclick="pulsarSalir();">
            </div>
        </div>

    </div>                        

    <script type="text/javascript">
        comboTipoDoc = new Combo("TipoDoc");

        tablaMetadatos = new Tabla(false, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('tablaMetadatos'), "", 250);
        tablaMetadatos.addColumna('20', 'center', '<%=descriptor.getDescripcion("etiq_obligC")%>', 'String');
        tablaMetadatos.addColumna('50', 'center', '<%=descriptor.getDescripcion("etiqCampo")%>', 'String');
        tablaMetadatos.addColumna('80', 'left', '<%=descriptor.getDescripcion("etiqValor")%>', 'String');

        tablaMetadatos.displayCabecera = true;

        $('#tablaMetadatos').hide();
        $('#msgNoMetadatos').hide();

        //tabla listado Documentos 
        tabDocs = new Tabla(false, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('tablaDocs'), "", 250);

        tabDocs.addColumna('150', 'left', '<%=descriptor.getDescripcion("gEtiqDocumento")%>');
        tabDocs.addColumna('100', 'left', '<%=descriptor.getDescripcion("etiqTipoDocumental")%>');
        tabDocs.addColumna('50', 'center', '<%=descriptor.getDescripcion("etiqCatalogado")%>');


        tabDocs.displayCabecera = true;


        function mostrarTabla(datos) {
            if (datos.length > 0) {
                $('#msgNoMetadatos').hide();
                $('#tablaMetadatos').show();

            } else {
                $('#tablaMetadatos').hide();
                $('#msgNoMetadatos').show();
            }
            tablaMetadatos.lineas = datos;
            tablaMetadatos.displayTabla();
            $(".dataTables_filter").hide();
            $(".dataTables_scrollBody").css("border-bottom", "none");
        }
    </script>
</body>
</html:html>
