--------------------------------------------------------
----------USUARIOS, LOGIN Y COMPATIBILIDAD--------------
--------------------------------------------------------

-- Debido al funcionamiento de SQL Server Management Studio, es necesarios realizar una sustituci�n de
-- t�rminos en todo el fichero antes de su ejecuci�n.

-- Sustituir sge_generico por el nombre del usuario que se usar� en el esquema gen�rico.
-- Sustituir sge_prueba por el nombre del usuario que se usar� en el esquema de pruebas.
-- Sustituir sge_real por el nombre del usuario que se usar� en el esquema real.
-- Sustituir contrase�a_generico por la constrase�a deseada para el esquema gen�rico.
-- Sustituir contrase�a_prueba por la constrase�a deseada para el esquema de pruebas.
-- Sustituir contrase�a_real por la constrase�a deseada para el esquema real.



-- COMPATIBILIDAD DE LAS BASES DE DATOS
USE [master]
GO
EXEC dbo.sp_dbcmptlevel @dbname=N'sge_generico', @new_cmptlevel=80
GO
EXEC dbo.sp_dbcmptlevel @dbname=N'sge_prueba', @new_cmptlevel=80
GO
EXEC dbo.sp_dbcmptlevel @dbname=N'sge_real', @new_cmptlevel=80
GO

-- CREAR LOGIN PRUEBA CON PERMISOS SOBRE GENERICO (EJECUTAR SOBRE PRUEBAS)
USE [master]
GO
CREATE LOGIN [sge_prueba] WITH PASSWORD=N'contrase�a_prueba', DEFAULT_DATABASE=[sge_prueba], DEFAULT_LANGUAGE=[Espa�ol], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF
GO
USE [sge_generico]
GO
CREATE USER [sge_prueba] FOR LOGIN [sge_prueba]
GO
USE [sge_generico]
GO
ALTER USER [sge_prueba] WITH DEFAULT_SCHEMA=[dbo]
GO
USE [sge_prueba]
GO
CREATE USER [sge_prueba] FOR LOGIN [sge_prueba]
GO
USE [sge_prueba]
GO
ALTER USER [sge_prueba] WITH DEFAULT_SCHEMA=[dbo]
GO

-- CREAR LOGIN REAL CON PERMISOS SOBRE GENERICO (EJECUTAR SOBRE REAL)
USE [master]
GO
CREATE LOGIN [sge_real] WITH PASSWORD=N'contrase�a_real', DEFAULT_DATABASE=[sge_real], DEFAULT_LANGUAGE=[Espa�ol], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF
GO
USE [sge_generico]
GO
CREATE USER [sge_real] FOR LOGIN [sge_real]
GO
USE [sge_generico]
GO
ALTER USER [sge_real] WITH DEFAULT_SCHEMA=[dbo]
GO
USE [sge_real]
GO
CREATE USER [sge_real] FOR LOGIN [sge_real]
GO
USE [sge_real]
GO
ALTER USER [sge_real] WITH DEFAULT_SCHEMA=[dbo]
GO


-- CREAR LOGIN GENERICO (EJECUTAR SOBRE GENERICO)
USE [master]
GO
CREATE LOGIN [sge_generico] WITH PASSWORD=N'contrase�a_generico', DEFAULT_DATABASE=[sge_generico], DEFAULT_LANGUAGE=[Espa�ol], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF
GO
USE [sge_generico]
GO
CREATE USER [sge_generico] FOR LOGIN [sge_generico]
GO
USE [sge_generico]
GO
ALTER USER [sge_generico] WITH DEFAULT_SCHEMA=[dbo]
GO

-- PERMISOS EN GENERICO
use [sge_generico]
GRANT DELETE TO [sge_generico]
GRANT INSERT TO [sge_generico]
GRANT SELECT TO [sge_generico]
GRANT UPDATE TO [sge_generico]
GO
use [sge_generico]
GRANT DELETE TO [sge_prueba]
GRANT INSERT TO [sge_prueba]
GRANT SELECT TO [sge_prueba]
GRANT UPDATE TO [sge_prueba]
GO
use [sge_generico]
GRANT DELETE TO [sge_real]
GRANT INSERT TO [sge_real]
GRANT SELECT TO [sge_real]
GRANT UPDATE TO [sge_real]
GO

USE [sge_generico]
GO
EXEC sp_addrolemember N'db_owner', N'sge_generico'
GO

-- PERMISOS PRUEBAS
use [sge_prueba]
GRANT DELETE TO [sge_prueba]
GRANT INSERT TO [sge_prueba]
GRANT SELECT TO [sge_prueba]
GRANT UPDATE TO [sge_prueba]
GO
EXEC sp_addrolemember N'db_owner', N'sge_prueba'
GO

-- PERMISOS REAL
use [sge_real]
GRANT DELETE TO [sge_real]
GRANT INSERT TO [sge_real]
GRANT SELECT TO [sge_real]
GRANT UPDATE TO [sge_real]
GO
EXEC sp_addrolemember N'db_owner', N'sge_real'
GO
