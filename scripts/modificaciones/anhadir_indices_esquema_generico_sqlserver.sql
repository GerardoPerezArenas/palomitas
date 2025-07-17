--------------------------------------------------------------
----------------- inicio: Parche 13.01.01 --------------------
-- Script para añadir índices que faltan en instalaciones sqlserver
-- No afecta al funcionamiento. Se crean por mejora de rendimiento





CREATE INDEX A_ORG_IDX1 ON A_ORG
(      
ORG_CSS ASC
)


  
CREATE INDEX A_UAE_IDX1 ON A_UAE
(      
UAE_APL ASC,
UAE_ORG ASC,
UAE_ENT ASC
)



CREATE INDEX A_UOU_IDX1 ON A_UOU
(      
UOU_USU ASC
)


  
CREATE INDEX A_USU_ROTACION_PASS_IDX1 ON A_USU_ROTACION_PASS
(      
USU_COD ASC
)


  
CREATE INDEX USUARIO_PROC_RESTRINGIDO_IDX1 ON USUARIO_PROC_RESTRINGIDO
(      
USU_COD ASC
)


  
CREATE INDEX USUARIO_PROC_RESTRINGIDO_IDX2 ON USUARIO_PROC_RESTRINGIDO
(      
ORG_COD ASC
)


  
CREATE INDEX USU_FIR_DEL_IDX1 ON USU_FIR_DEL
(      
USU_DELEGADO ASC
)






------------------------------------------------------------------
-------------------PUBLICACIÓN PARCHE 13.01.24--------------------
------------------------------------------------------------------
------------------------------------------------------------------


------------------------------------------------------------------
------------------- inicio PARCHE 13.01.25------------------------
------------------------------------------------------------------

  
  
