--------------------------------------------------------------------------
-- FLEXIA 10.4 
-- Altia Consultores 
-- Scripts creación BD flexia 
-- Oracle
-- Da permisos sobre el esquema generico a la organización o al entorno de
-- pruebas. Estos permisos se necesitan para las vistas.
-- Ejecutar con usuario del esquema generico
--------------------------------------------------------------------------

spool 7_permisos_generico_a_organizacion.log
SET ECHO ON;

-- Nombre del esquema de la organizacion a la que se quiere dar acceso de consulta
DEFINE esquema_organizacion=flexia_pruebas

-- Genera los permisos de consulta
declare cursor c1 is
select * from user_tables;
begin
    for r1 in c1 loop
      execute immediate 'grant select on '|| r1.table_name || ' to &esquema_organizacion with grant option';
    end loop;
end;
/

-- Salida al sistema
spool off;
quit;
