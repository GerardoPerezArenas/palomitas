--------------------------------------------------------
----------USUARIOS, LOGIN Y COMPATIBILIDAD--------------
--------------------------------------------------------

-- Debido al funcionamiento de SQL Server Management Studio, es necesarios realizar una sustitución de
-- términos en todo el fichero antes de su ejecución.

-- Sustituir sge_organizacion por el nombre del usuario que se usará en el esquema de la nueva organización.
-- Sustituir contraseña_organizacion por la constraseña deseada para el nuevo esquema.
-- Sustituir sge_generico por el nombre del usuario del esquema genérico de la aplicaicón.


-- COMPATIBILIDAD DE LAS BASES DE DATOS
USE [master]
GO
EXEC dbo.sp_dbcmptlevel @dbname=N'sge_organizacion', @new_cmptlevel=80
GO

-- CREAR LOGIN PRUEBA CON PERMISOS SOBRE GENERICO (EJECUTAR SOBRE PRUEBAS)
USE [master]
GO
CREATE LOGIN [sge_organizacion] WITH PASSWORD=N'contraseña_organizacion', DEFAULT_LANGUAGE=[Español], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF
GO
USE [sge_generico]
GO
CREATE USER [sge_organizacion] FOR LOGIN [sge_organizacion]
GO
USE [sge_generico]
GO
ALTER USER [sge_organizacion] WITH DEFAULT_SCHEMA=[dbo]
GO
USE [sge_organizacion]
GO
CREATE USER [sge_organizacion] FOR LOGIN [sge_organizacion]
GO
USE [sge_organizacion]
GO
ALTER USER [sge_organizacion] WITH DEFAULT_SCHEMA=[dbo]
GO

-- PERMISOS SOBRE GENERICO
use [sge_generico]
GRANT DELETE TO [sge_organizacion]
GRANT INSERT TO [sge_organizacion]
GRANT SELECT TO [sge_organizacion]
GRANT UPDATE TO [sge_organizacion]
GO

-- PERMISOS NUEVA ORGANIZACIÓN
use [sge_organizacion]
EXEC sp_addrolemember N'db_owner', N'sge_organizacion'
GO

USE [master]
GO
ALTER LOGIN [sge_organizacion] WITH DEFAULT_DATABASE=[sge_organizacion], DEFAULT_LANGUAGE=[Español], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF
GO

