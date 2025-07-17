<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<div id="hidepage">
        <div class="contenedorHidepage">
            <div class="textoHide">
                <span id="hidepage_message">
                    <c:out value="${param.cargaDatos}"/>
                </span>
            </div>
            <div class="imagenHide">
                <span class="fa fa-spinner fa-spin" aria-hidden="true"></span>
            </div>
        </div>
    </div>

