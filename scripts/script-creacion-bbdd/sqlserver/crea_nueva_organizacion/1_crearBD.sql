--------------------------------------------------------
-----CREACIÓN DE ESQUEMA PARA NUEVA ORGANIZACIÓN--------
--------------------------------------------------------

DECLARE @esq_organizacion varchar(19);
DECLARE @esq_organizacion_data varchar(19);
DECLARE @data_organizacion varchar(40);
DECLARE @esq_organizacion_log varchar(19);
DECLARE @log_organizacion varchar(39);

--------------------------------------
---- VARIABLES DEL NUEVO ESQUEMA -----
--------------------------------------
-- Nombre del esquema (ESQUEMA DE PRUEBAS)
SET @esq_organizacion='sge_organizacion';
-- Nombre de los datos (DATOS_PRUEBAS)
SET @esq_organizacion_data='sge_organizacion_DATA';
-- Ruta del fichero de datos (RUTA_PRUEBAS)
SET @data_organizacion='D:\SGE-DATA\sge_organizacion_data.mdf';
-- Nombre del fichero de log (LOG_PRUEBAS)
SET @esq_organizacion_log ='sge_organizacion_LOG';
-- Ruta del fichero de log (RUTA_LOG_PRUEBAS)
SET @log_organizacion='D:\SGE-DATA\sge_organizacion_log.ldf';


-----------------------------------------
-- INSTRUCCIONES PARA CREACIÓN DE BBDD --
-----------------------------------------

-- Declarar esquema de pruebas

EXECUTE('CREATE DATABASE ' + @esq_organizacion + ' ON PRIMARY 
( NAME = "' + @esq_organizacion_data + '", FILENAME = "' + @data_organizacion + '" , SIZE = 3072KB , FILEGROWTH = 1024KB )
LOG ON 
( NAME = "' + @esq_organizacion_log + '", FILENAME = "' + @log_organizacion + '" , SIZE = 1024KB , FILEGROWTH = 10%) COLLATE Modern_Spanish_CI_AS');

EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET ANSI_NULL_DEFAULT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET ANSI_NULLS OFF');
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET ANSI_PADDING OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET ANSI_WARNINGS OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET ARITHABORT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET AUTO_CLOSE OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET AUTO_CREATE_STATISTICS ON'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET AUTO_SHRINK OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET AUTO_UPDATE_STATISTICS ON'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET CURSOR_CLOSE_ON_COMMIT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET CURSOR_DEFAULT  GLOBAL'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET CONCAT_NULL_YIELDS_NULL OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET NUMERIC_ROUNDABORT OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET QUOTED_IDENTIFIER OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET RECURSIVE_TRIGGERS OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET AUTO_UPDATE_STATISTICS_ASYNC OFF');
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET DATE_CORRELATION_OPTIMIZATION OFF'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET PARAMETERIZATION SIMPLE'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET  READ_WRITE'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET RECOVERY FULL'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET  MULTI_USER'); 
EXECUTE('ALTER DATABASE ' + @esq_organizacion + ' SET PAGE_VERIFY CHECKSUM');  

GO
