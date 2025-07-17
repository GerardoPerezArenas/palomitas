--------------------------------------------------------------------------
-- FLEXIA 10.4 
-- Altia Consultores 
-- Scripts creaci�n BD flexia 
-- Oracle
-- Creaci�n del esquema de una organizaci�n de producci�n
-- Ejecutar con usuario con permisos DBA
--------------------------------------------------------------------------

spool 2_crea_usuarios_organizacion.log
SET ECHO ON;

-- Esquema organizaci�n
DEFINE nom_esquema_organizacion = FLEXIA_ORGANIZACION
DEFINE pass_esquema_organizacion  = password_organizacion
DEFINE nom_tablespace_organizacion = FLEXIA_ORGANIZACION

-- esquema de organizacion
create user &nom_esquema_organizacion profile default identified by &pass_esquema_organizacion default tablespace &nom_tablespace_organizacion temporary tablespace temp account unlock;
grant unlimited tablespace to &nom_esquema_organizacion;
grant connect to &nom_esquema_organizacion;
grant resource to &nom_esquema_organizacion;
grant create view to &nom_esquema_organizacion;

-- Salida al sistema
spool off;
quit;



