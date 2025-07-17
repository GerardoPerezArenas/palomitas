--------------------------------------------------------------
----------------- inicio: Parche 13.01.01 --------------------
-- Script para añadir índices que faltan en instalaciones oracle. 
-- No afecta al funcionamiento. Se crean por mejora de rendimiento
-- Nota: En lanbide ya han sido creados
--------------------------------------------------------------


-- Tarea #115783 ----
-- Marcos Rama
---------------------


DEFINE nom_esquema_generico = FLEXIA_GENERICO;
DEFINE nom_tablespace_generico = FLEXIA_GENERICO;




CREATE INDEX &nom_esquema_generico..A_ORG_IDX1 ON &nom_esquema_generico..A_ORG
(      
ORG_CSS ASC
)
&nom_tablespace_generico
/
  
CREATE INDEX &nom_esquema_generico..A_UAE_IDX1 ON &nom_esquema_generico..A_UAE
(      
UAE_APL ASC,
UAE_ORG ASC,
UAE_ENT ASC
)
&nom_tablespace_generico
/

CREATE INDEX &nom_esquema_generico..A_UOU_IDX1 ON &nom_esquema_generico..A_UOU
(      
UOU_USU ASC
)
&nom_tablespace_generico
/
  
CREATE INDEX &nom_esquema_generico..A_USU_ROTACION_PASS_IDX1 ON &nom_esquema_generico..A_USU_ROTACION_PASS
(      
USU_COD ASC
)
&nom_tablespace_generico
/
  
CREATE INDEX &nom_esquema_generico..USUARIO_PROC_RESTRINGIDO_IDX1 ON &nom_esquema_generico..USUARIO_PROC_RESTRINGIDO
(      
USU_COD ASC
)
&nom_tablespace_generico
/
  
CREATE INDEX &nom_esquema_generico..USUARIO_PROC_RESTRINGIDO_IDX2 ON &nom_esquema_generico..USUARIO_PROC_RESTRINGIDO
(      
ORG_COD ASC
)
&nom_tablespace_generico
/
  
CREATE INDEX &nom_esquema_generico..USU_FIR_DEL_IDX1 ON &nom_esquema_generico..USU_FIR_DEL
(      
USU_DELEGADO ASC
)
&nom_tablespace_generico
/

-- FIN Tarea #115783 ----
---------------------



------------------------------------------------------------------
-------------------PUBLICACIÓN PARCHE 13.01.24--------------------
------------------------------------------------------------------
------------------------------------------------------------------


------------------------------------------------------------------
------------------- inicio PARCHE 13.01.25------------------------
------------------------------------------------------------------

  
  
