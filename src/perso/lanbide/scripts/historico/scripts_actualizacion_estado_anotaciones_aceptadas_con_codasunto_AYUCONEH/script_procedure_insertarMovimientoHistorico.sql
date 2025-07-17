create or replace
PROCEDURE insertarMovimientoHistorico(departamento in VARCHAR2,uor in VARCHAR2,tipo in VARCHAR2,ejercicio in VARCHAR2,numero in VARCHAR2)
as
contador number;
codigo NUMBER(10,0);
usuario	NUMBER(4,0);
fecha	DATE;
tipoEntidad	VARCHAR2(12 BYTE);
codEntidad	VARCHAR2(30 BYTE);
tipoMov	NUMBER(2,0);
detalles	CLOB;

BEGIN 
  
  codigo:=getMaximoCodigoHistorico;
  usuario:=5;
  fecha:=sysdate;
  tipoEntidad:='ANOTACION';
  codEntidad:=departamento||'/'||uor||'/'||tipo||'/'||ejercicio||'/'||numero;
  tipoMov:=9;
  detalles:='<DescripcionMovimiento><RecuperarHistorico/></DescripcionMovimiento>';
  
  dbms_output.put_line('=========> codigo:' || codigo || ',usuario:' || usuario || ',fecha: ' || fecha || ',tipoEntidad: ' || tipoEntidad || ',codEntidad: ' || codEntidad || ',tipoMov: ' || tipoMov || ',detalles: ' || detalles);
  
 insert into R_HISTORICO values ( codigo,usuario, fecha, tipoEntidad, codEntidad, tipoMov, detalles);
 insert into R_HISTORICO_PREV values ( codigo,usuario, fecha, tipoEntidad, codEntidad, tipoMov);
 
 
end;