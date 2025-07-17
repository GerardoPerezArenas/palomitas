--------------------------------------------------------
---------CREACIÓN DE LOS 3 ESQUEMAS BÁSICOS-------------
--------------------------------------------------------

DECLARE @esq_pruebas varchar(19);
DECLARE @esq_pruebas_data varchar(19);
DECLARE @dataPruebas varchar(40);
DECLARE @esq_pruebas_log varchar(19);
DECLARE @logPruebas varchar(39);
DECLARE @esq_generico_data varchar(19);
DECLARE @data varchar(40);
DECLARE @esq_generico_log varchar(19);
DECLARE @log varchar(39);
DECLARE @esq_generico varchar(19);
DECLARE @esq_real varchar(19);
DECLARE @esq_real_data varchar(19);
DECLARE @dataReal varchar(40);
DECLARE @esq_real_log varchar(19);
DECLARE @logReal varchar(39);

------------------------------------
-- VARIABLES DEL ESQUEMA GENERICO --
------------------------------------
-- Nombre del esquema genérico (ESQUEMA GENERICO)
SET @esq_generico='sge_generico';
-- Nombre de los datos (DATOS_GENERICO)
SET @esq_generico_data='sge_generico_DATA';
-- Ruta al fichero de datos (RUTA_GENERICO)
SET @data='D:\SGE-DATA\sge_generico_data.mdf';
-- Nombre del log (LOG_GENERICO)
SET @esq_generico_log ='sge_generico_LOG';
-- Ruta al fichero de log (RUTA_LOG_GENERICO)
SET @log='D:\SGE-DATA\sge_generico_log.ldf';

--------------------------------------
-- VARIABLES DEL ESQUEMA DE PRUEBAS --
--------------------------------------
-- Nombre del esquema (ESQUEMA DE PRUEBAS)
SET @esq_pruebas='sge_prueba';
-- Nombre de los datos (DATOS_PRUEBAS)
SET @esq_pruebas_data='sge_prueba_DATA';
-- Ruta del fichero de datos (RUTA_PRUEBAS)
SET @dataPruebas='D:\SGE-DATA\sge_prueba_data.mdf';
-- Nombre del fichero de log (LOG_PRUEBAS)
SET @esq_pruebas_log ='sge_prueba_LOG';
-- Ruta del fichero de log (RUTA_LOG_PRUEBAS)
SET @logPruebas='D:\SGE-DATA\sge_prueba_log.ldf';

--------------------------------
-- VARIABLES DEL ESQUEMA REAL --
--------------------------------
-- Nombre del esquema real (ESQUEMA_REAL)
SET @esq_real='sge_real';
-- Nombre de los datos (DATOS_REAL)
SET @esq_real_data='sge_real_DATA';
-- Ruta al fichero de datos (RUTA_REAL)
SET @dataReal='D:\SGE-DATA\sge_real_data.mdf';
-- Nombre del log (LOG_REAL)
SET @esq_real_log ='sge_real_LOG';
-- Ruta al fichero de log (RUTA_LOG_REAL)
SET @logReal='D:\SGE-DATA\sge_real_log.ldf';


-----------------------------------------
-- INSTRUCCIONES PARA CREACIÓN DE BBDD --
-----------------------------------------

-- Declarar esquema de pruebas

EXECUTE('CREATE DATABASE ' + @esq_pruebas + ' ON PRIMARY 
( NAME = "' + @esq_pruebas_data + '", FILENAME = "' + @dataPruebas + '" , SIZE = 3072KB , FILEGROWTH = 1024KB )
LOG ON 
( NAME = "' + @esq_pruebas_log + '", FILENAME = "' + @logPruebas + '" , SIZE = 1024KB , FILEGROWTH = 10%) COLLATE Modern_Spanish_CI_AS');

EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET ANSI_NULL_DEFAULT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET ANSI_NULLS OFF');
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET ANSI_PADDING OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET ANSI_WARNINGS OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET ARITHABORT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET AUTO_CLOSE OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET AUTO_CREATE_STATISTICS ON'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET AUTO_SHRINK OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET AUTO_UPDATE_STATISTICS ON'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET CURSOR_CLOSE_ON_COMMIT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET CURSOR_DEFAULT  GLOBAL'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET CONCAT_NULL_YIELDS_NULL OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET NUMERIC_ROUNDABORT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET QUOTED_IDENTIFIER OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET RECURSIVE_TRIGGERS OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET AUTO_UPDATE_STATISTICS_ASYNC OFF');
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET DATE_CORRELATION_OPTIMIZATION OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET PARAMETERIZATION SIMPLE'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET  READ_WRITE'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET RECOVERY FULL'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET  MULTI_USER'); 
EXECUTE('ALTER DATABASE ' + @esq_pruebas + ' SET PAGE_VERIFY CHECKSUM');  


--Declarar esquema real
EXECUTE('CREATE DATABASE ' + @esq_real + ' ON PRIMARY 
( NAME = "' + @esq_real_data + '", FILENAME = "' + @dataReal + '" , SIZE = 3072KB , FILEGROWTH = 1024KB )
LOG ON 
( NAME = "' + @esq_real_log + '", FILENAME = "' + @logReal + '" , SIZE = 1024KB , FILEGROWTH = 10%) COLLATE Modern_Spanish_CI_AS');

EXECUTE('ALTER DATABASE ' + @esq_real + ' SET ANSI_NULL_DEFAULT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET ANSI_NULLS OFF');
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET ANSI_PADDING OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET ANSI_WARNINGS OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET ARITHABORT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET AUTO_CLOSE OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET AUTO_CREATE_STATISTICS ON'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET AUTO_SHRINK OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET AUTO_UPDATE_STATISTICS ON'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET CURSOR_CLOSE_ON_COMMIT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET CURSOR_DEFAULT  GLOBAL'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET CONCAT_NULL_YIELDS_NULL OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET NUMERIC_ROUNDABORT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET QUOTED_IDENTIFIER OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET RECURSIVE_TRIGGERS OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET AUTO_UPDATE_STATISTICS_ASYNC OFF');
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET DATE_CORRELATION_OPTIMIZATION OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET PARAMETERIZATION SIMPLE'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET  READ_WRITE'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET RECOVERY FULL'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET  MULTI_USER'); 
EXECUTE('ALTER DATABASE ' + @esq_real + ' SET PAGE_VERIFY CHECKSUM');  


--Declarar esquema generico
EXECUTE('CREATE DATABASE ' + @esq_generico + ' ON PRIMARY 
( NAME = "' + @esq_generico_data + '", FILENAME = "' + @data + '" , SIZE = 3072KB , FILEGROWTH = 1024KB )
LOG ON 
( NAME = "' + @esq_generico_log + '", FILENAME = "' + @log + '" , SIZE = 1024KB , FILEGROWTH = 10%) COLLATE Modern_Spanish_CI_AS');

EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET ANSI_NULL_DEFAULT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET ANSI_NULLS OFF');
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET ANSI_PADDING OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET ANSI_WARNINGS OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET ARITHABORT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET AUTO_CLOSE OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET AUTO_CREATE_STATISTICS ON'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET AUTO_SHRINK OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET AUTO_UPDATE_STATISTICS ON'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET CURSOR_CLOSE_ON_COMMIT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET CURSOR_DEFAULT  GLOBAL'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET CONCAT_NULL_YIELDS_NULL OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET NUMERIC_ROUNDABORT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET QUOTED_IDENTIFIER OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET RECURSIVE_TRIGGERS OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET AUTO_UPDATE_STATISTICS_ASYNC OFF');
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET DATE_CORRELATION_OPTIMIZATION OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET PARAMETERIZATION SIMPLE'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET  READ_WRITE'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET RECOVERY FULL'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET  MULTI_USER'); 
EXECUTE('ALTER DATABASE ' + @esq_generico + ' SET PAGE_VERIFY CHECKSUM');  
GO

