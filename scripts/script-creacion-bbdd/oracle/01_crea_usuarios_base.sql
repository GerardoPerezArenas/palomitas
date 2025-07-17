--------------------------------------------------------------------------
-- FLEXIA 10.4 
-- Altia Consultores 
-- Scripts creación BD flexia 
-- Oracle
-- Creacións de los esquemas genericos y de pruebas
-- Ejecutar con usuario con permisos DBA
--------------------------------------------------------------------------

spool 1_crea_usuarios_base.log
SET ECHO ON;

-- Esquema genérico
DEFINE nom_esquema_generico = FLEXIA_GENERICO
DEFINE pass_esquema_generico = password_generico
DEFINE nom_tablespace_generico = FLEXIA_GENERICO

-- Esquema entorno de pruebas 
DEFINE nom_esquema_pruebas = FLEXIA_PRUEBAS
DEFINE pass_esquema_pruebas = password_pruebas
DEFINE nom_tablespace_pruebas = FLEXIA_PRUEBAS


-- Creación esquema genérico 
create user &nom_esquema_generico profile default identified by &pass_esquema_generico default tablespace &nom_tablespace_generico temporary tablespace temp account unlock;
grant unlimited tablespace to &nom_esquema_generico;
grant connect to &nom_esquema_generico;
grant resource to &nom_esquema_generico;

-- Creación esquema de pruebas
create user &nom_esquema_pruebas profile default identified by &pass_esquema_pruebas default tablespace &nom_tablespace_pruebas temporary tablespace temp account unlock;
grant unlimited tablespace to &nom_esquema_pruebas;
grant connect to &nom_esquema_pruebas;
grant resource to &nom_esquema_pruebas;
grant create view to &nom_esquema_pruebas;


-- Salida al sistema
spool off;
quit;


